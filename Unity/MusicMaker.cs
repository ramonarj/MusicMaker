using UnityEngine;
using System.Collections.Generic;
using System.IO;
using System;
using UnityEditor;
using UnityEngine.SceneManagement;

//Se encarga de gestionar todo lo referente al plugin
[ExecuteInEditMode]
public class MusicMaker : MonoBehaviour
{
    #region Variables

    //Instancia del singleton
    private static MusicMaker instance_ = null;

    //Paquete elegido para la música 
    private MM.Package package;
   
    //Lista de tuplas <input, efecto, output> elegidas por el usuario
    [Tooltip("La lista de tuplas que condicionarán la música")]
    public List<MM.MusicTuple> tuples;
    //El valor de cada variable en el frame anterior (para compararlo)
    private List<object> varValues;
    //Capas que hay activas en este momento
    bool[,] activeLayers; 

    //La id del cliente
    private const string ClientId = "SuperCollider";

    #endregion

    //Singleton
    public static MusicMaker Instance
    {
        get
        {
            if (instance_ == null)
            {
                instance_ = new GameObject("MusicMaker").AddComponent<MusicMaker>();
            }

            return instance_;
        }
    }


    #region Callbacks de Unity
    private void Awake()
    {
        if (instance_ == null)
            instance_ = this;
    }

    // NOTA: el usuario debe inicializar sus variables en el Awake para que en la primera vuelta todo esté bajo control
    // (mejor rendimiento porque ahorra una vuelta de mensajes)
    // Aquí se hace que solo queden tuplas válidas para no tener que comprobarlo en cada tick
    private void Start()
    {
        if (EditorApplication.isPlaying)
        {

            //Paquete actual
            string packageData = File.ReadAllText(MM.Utils.GetPackageFile());
            package = (MM.Package)Enum.Parse(typeof(MM.Package), packageData);

            //Capas
            string layersFile = MM.Utils.GetLayersFile();
            Debug.Assert(File.Exists(layersFile));
            string layersData = File.ReadAllText(layersFile);

            //TODO: se prodía hacer una estructura única para representar el array bidimensional de 4x4. Más fácil de usar
            //Inicializamos los valores de las capas
            int c = 0;
            activeLayers = new bool[MM.Constants.NUM_ASPECTS, MM.Constants.MAX_LAYERS];
            for (int i = 0; i < MM.Constants.NUM_ASPECTS; i++) //ESTE CÓDIGO ESTÁ REPE
                for (int j = 0; j < MM.Constants.MAX_LAYERS; j++)
                    activeLayers[i, j] = layersData[c++] == '1' ? true : false;


            //Para asegurarnos de que hay paquete elegido
            Debug.Assert(package != MM.Package.None); 

            //Iniciamos SuperCollider:
            //a) Crea el cliente de SuperCollider en la dirección de loopback
            //b) Crea el servidor para recibir mensajes de SuperCollider
            OSCHandler.Instance.Init();

            //Eliminar tuplas no válidas 
            tuples.RemoveAll(x => !x.IsCorrect());

            //Inicializar los valores auxiliares copiando de los originales
            varValues = new List<object>();
            foreach (MM.MusicTuple t in tuples)
                varValues.Add(t.input.GetValue());

            //Le mandamos el tipo de paquete al cliente de SuperCollider
            OSCHandler.Instance.SendMessageToClient(ClientId, "/Init", package.ToString());

            //Damos tiempo pa que se inicialice antes de reproducir la música
            Invoke("PlayMusic", 10.0f);
        }
    }

    //Empieza a reproducir la música
    private void PlayMusic()
    {
        OSCHandler.Instance.SendMessageToClient(ClientId, "/Play", package.ToString());
        //TODO: hacer que empiecen las capas que sea sonando

        //NOTA: los FX no pueden empezar activos
        string[] aspects = { "Rhythm", "Harmony", "Melody" };
        for (int i = 0; i < MM.Constants.NUM_ASPECTS - 1; i++)
            for (int j = 0; j<MM.Constants.MAX_LAYERS;j++)
                if(activeLayers[i,j])
                    OSCHandler.Instance.SendMessageToClient(ClientId, "/PlayLayer", aspects[i] + " " + j);
    }

    //Para cuando se hace algún cambio en el inspector
    private void OnValidate()
    {
        if(tuples != null)
        {
            //Validar tuplas
            foreach (MM.MusicTuple t in tuples)
            {
                //Comprobamos que el usuario ha metido todos los valores de la tupla bien
                if (t.IsCorrect())
                {
                    if (t.input.IsAnArray())
                        Debug.Log("Tupla correcta: " + t.input.variable + "[" + t.input.index + "]->" + t.effect.ToString() + "s->" + t.output.aspect.ToString() + " nº " + t.output.layerNo);
                    else
                        Debug.Log("Tupla correcta: " + t.input.variable + "->" + t.effect.ToString() + "s->" + t.output.aspect.ToString() + " nº " + t.output.layerNo);
                }
            }
        }
    }

    //Comprueba si se han producido cambios en las variables y avisa a SC
    private void Update()
    {
        if (EditorApplication.isPlaying)
        {
            //Recorremos las tuplas
            int i = 0;
            foreach (MM.MusicTuple t in tuples)
            {
                //Cogemos el valor actual de la variable externa
                object actualVal = t.input.GetValue();
                string type = t.input.GetType().Name;
                if (t.input.IsAnArray()) //Arrays
                    actualVal = ((bool[])actualVal)[t.input.index];

                //TODO: aqui

                //Solo mandamos un mensaje a SuperCollider si la variable en cuestión ha cambiado desde el frame anterior
                if (!actualVal.Equals(varValues[i]))
                {
                    //Debug
                    //Debug.Log(t.input.variable + " (" + type + ") : " + actualVal);
                    //Debug.Log(t.effect.ToString() + " " + t.output.ToString() + "(" + actualVal +")");

                    //Actualizamos nuestra variable
                    varValues[i] = actualVal;

                    //TODO: verificación en 2 pasos, primero se actualiza "tuples" y luego se manda a supercollider esa info
                    //Procesamos el mensaje y lo mandamos
                    ProcessMessage(t);
                }
                i++;
            }
        }
    }

    #endregion

    #region Mensajes

    //Saca la información de la tupla para mandar el mensaje a SuperCollider
    private void ProcessMessage(MM.MusicTuple t)
    {
        //Para los valores numéricos; oscila entre 0 y 1
        float numValue = 0;

        //1. SACAR LA INFORMACIÓN SOBRE EL INPUT Y AJUSTARLO AL FORMATO QUE RECIBE SUPERCOLLIDER
        //Si es un número, hay que normalizar:
        object variable = t.input.GetValue();

        if (t.input.IsAnArray()) //Arrays
            variable = ((bool[])variable)[t.input.index];

        //NOTA: no se usa
        if (variable.GetType().Name != "Boolean")
        {
            float range = t.input.max - t.input.min;
            float value = Mathf.Clamp((float)variable, t.input.min, t.input.max);
            numValue = (value - t.input.min) / range;

            //Caso especial: el parámetro es inversamente proporcional
            if (t.effect == MM.MusicEffect.Decrease)
                numValue = 1 - numValue;
        }

        //Si es un booleano:
        else
        {
            numValue = (bool)variable ? 1 : 0; // 1 / 0
            //Caso especial: el booleano va al revés (false->true, true->false)
            if (t.effect == MM.MusicEffect.Deactivate)
                numValue = 1 - numValue;
        }

        //TODO: gestionar los arrays

        //2. DISTINGUIR EL OUTPUT Y MANDAR EL MENSAJE
        string msg = t.output.aspect.ToString() + " " + (t.output.layerNo - 1);

        //Activar/desactivar una capa
        //TODO: actualizar las capas de Unity (no es obligatorio de momento)
        if(numValue == 1)
            OSCHandler.Instance.SendMessageToClient(ClientId, "/PlayLayer", msg);
        else
            OSCHandler.Instance.SendMessageToClient(ClientId, "/StopLayer", msg);
    }

    //Establece el paquete
    public void SetPackage(MM.Package newPackage)
    {
        package = newPackage;
    }

    //TODO: asegurarse que esto siempre se llama antes del OnQuit() de OSCHandler, porque si no la hemos liao
    //Cuando acaba el juego, mandamos el mensaje de "/Stop"
    void OnApplicationQuit()
    {
        //Primero paramos todo en SC y luego nos desconectamos
        OSCHandler.Instance.SendMessageToClient(ClientId, "/Stop", package.ToString());
        OSCHandler.Instance.Free();
    }

    #endregion
}
