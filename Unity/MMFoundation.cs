using System.Collections.Generic;
using UnityEngine;
using System.Reflection;
using System.Linq; 
using UnityEngine.SceneManagement;


//Tipos que se aceptan como inputs válidos:
//1. Booleanos
//2. Enteros
//3. Floats
//4. Componentes de un array


//Espacio de nombres del Music Maker
namespace MM
{
    //Los distintos paquetes que el usuario va a poder utilizar para la creación de musica
    public enum Package { None, Ambient, Desert, Horror, Fantasy };

    //Efecto del input sobre el output
    public enum MusicEffect { None, Activate, Deactivate, Increase, Decrease };

    //Los 4 aspectos de la música
    [System.Serializable]
    public enum Aspect { None, Rhythm, Harmony, Melody, FX };


    #region Tuplas
    /**
     * Representa un input (i.e), la variable que tenemos linkada y el componente y GO al que hace pertenece
     * Además incluye parámetros que se usarán dependiendo del tipo de dato que sea la variable
     **/
    [System.Serializable]
    public class MusicInput
    {
        //Lo básico (los booleanos no necesitan más)
        public Object objeto;
        public Component componente;
        public string variable;

        //Solo ara floats
        public float min;
        public float max;

        //Solo ara arrays
        public int index;

        #region Constructoras
        public MusicInput()
        {
            this.objeto = null;
            this.componente = null;
            this.variable = "";
            this.index = -1;
        }

        public MusicInput(Component component, string value)
        {
            this.objeto = component.gameObject;
            this.componente = component;
            this.variable = value;
            this.index = -1;
        }


        public MusicInput(UnityEngine.Object objeto, Component component, string value)
        {
            this.objeto = objeto;
            this.componente = component;
            this.variable = value;
            this.index = -1;
        }
        #endregion

        #region Equals y Hash
        public override bool Equals(object obj)
        {
            var input = obj as MusicInput;
            return input != null &&
                   variable == input.variable &&
                   EqualityComparer<object>.Default.Equals(componente, input.componente) &&
                   EqualityComparer<Object>.Default.Equals(objeto, input.objeto);
        }

        public override int GetHashCode()
        {
            var hashCode = -1495079906;
            hashCode = hashCode * -1521134295 + EqualityComparer<string>.Default.GetHashCode(variable);
            hashCode = hashCode * -1521134295 + EqualityComparer<object>.Default.GetHashCode(componente);
            hashCode = hashCode * -1521134295 + EqualityComparer<Object>.Default.GetHashCode(objeto);
            return hashCode;
        }
        #endregion

        #region Métodos

        /*
         * Devuelve el tipo del input (Double, Boolean, Int32...)
         */
        public System.Type GetType()
        {
            //Obtenemos la propiedad del componente
            System.Type t = componente.GetType();
            PropertyInfo prop = t.GetProperty(variable);
            if (prop == null)
                return null;

            //Devolvemos el valor de la propiedad
            return prop.PropertyType;
        }

        /*
         * Devuelve el valor del input (2.0, false, -3...) -> usa reflexión
         */
        public object GetValue()
        {
            //Obtenemos la propiedad del componente
            object obj = componente; //casting necesario
            System.Type type = obj.GetType();
            PropertyInfo prop = type.GetProperty(variable);

            //Si no existe, nada
            if (prop == null)
                return null;

            //Devolvemos el valor de la propiedad
            return prop.GetValue(obj);
        }

        /*
         * Indica si la variable forma parte de un array
         */
        public bool IsAnArray()
        {
            System.Type type = this.GetType();
            return (type.IsArray);
        }

        /*
         * Indica si el input es correcto (i.e.; si la variable existe y el input tiene sentido)
         */
        public bool IsCorrect()
        {
            //1. Comprobamos que hay un gameobject y objeto seleccionados
            object obj = componente;
            if (obj == null || objeto == null)
                return false;

            //2. Comprobamos que el componente tiene esa propiedad
            System.Type t = obj.GetType(); //Tipo
            List<PropertyInfo> props = t.GetProperties().ToList();
            PropertyInfo p = props.Find((x) => x.Name == variable);
            if (p == null)
                return false;

            //4. Comprobamos que está correcto
            if (IsAnArray())
            {
                if (index < 0)
                    return false;
            }
            else if (GetType().Name != "Boolean" && min >= max)
                return false;

            return true;
        }

        #endregion
    }

    /**
     * Representa un output, i.e, un output
     **/
    [System.Serializable]
    public class MusicOutput
    {
        //El aspecto al que hace referencia
        public Aspect aspect;

        //El nº de capa que activa/desactiva
        public int layerNo;

        #region Constructoras
        public MusicOutput()
        {
            this.aspect = Aspect.None;
            this.layerNo = 0;
        }

        public MusicOutput(Aspect aspect, int layerNo)
        {
            this.aspect = aspect;
            this.layerNo = layerNo;
        }
        #endregion

        #region Métodos
        /*
         * Indica si el output es correcto (i.e., se ha seleccionado un aspecto y capa válida)
         */
        public bool IsCorrect()
        {
            return (aspect != Aspect.None && (layerNo > 0 && layerNo <= Constants.MAX_LAYERS)); //Empieza en 1 (claridad para el usuario)
        }

        #endregion
    }

    /**
     * Tupla que consta de input, output y efecto del primero sobre el segundo
     **/
    [System.Serializable]
    public class MusicTuple
    {
        [SerializeField]
        public MusicInput input;
        [SerializeField]
        public MusicEffect effect;
        [SerializeField]
        public MusicOutput output;

        public MusicTuple(MusicInput input, MusicEffect effect, MusicOutput output)
        {
            this.input = input;
            this.effect = effect;
            this.output = output;
        }

        #region Métodos

        /*
         * Indica si la tupla es correcta (i.e.; si sus 3 partes son correctas)
         */
        public bool IsCorrect()
        {
            return (input.IsCorrect() && effect != MusicEffect.None && output.IsCorrect());
        }

        #endregion

        #region Equals y Hash

        public override bool Equals(object obj)
        {
            var tuple = obj as MusicTuple;
            return tuple != null &&
                   EqualityComparer<MusicInput>.Default.Equals(input, tuple.input) &&
                   effect == tuple.effect &&
                   output == tuple.output;
        }

        public override int GetHashCode()
        {
            var hashCode = 1440660003;
            hashCode = hashCode * -1521134295 + EqualityComparer<MusicInput>.Default.GetHashCode(input);
            hashCode = hashCode * -1521134295 + effect.GetHashCode();
            hashCode = hashCode * -1521134295 + output.GetHashCode();
            return hashCode;
        }
        #endregion
    }
    #endregion

    #region Utilidades
    //Métodos auxiliares
    public static class Utils
    {
        /**
         * Devuelve las propiedades de un componente en forma de lista de strings (usa Reflection)
         * Ejemplo de uso: List<string> props = Utils.getProperties(player.GetComponent<Player>());
         *                 Debug.Log(props); 
        **/
        public static List<string> GetProperties(object component, bool showInherited = false)
        {
            //Propiedades del componente (mirar C# reflection)
            System.Type t = component.GetType();
            List<PropertyInfo> props = t.GetProperties().ToList();

            //Las pasamos a string 
            List<string> properties = new List<string>();
            foreach (var prop in props)
            {
                if (showInherited || prop.DeclaringType.Name == t.Name)
                    properties.Add(prop.Name);
            }

            //Caso especial
            if (properties.Count == 0)
                return null;
            return properties;
        }

        /**
         * Devuelve la ruta donde se guarda el el paquete seleccionado
         */
        public static string GetPackageFile()
        {
            return Application.persistentDataPath + "/MMPackage_" + SceneManager.GetActiveScene().name + ".json";
        }


        /**
         * Devuelve la ruta donde se guardan las capas iniciales
         */
        public static string GetLayersFile()
        {
            return Application.persistentDataPath + "/MMLayers_" + SceneManager.GetActiveScene().name + ".json";
        }
    }

    #endregion

    #region Constantes

    public static class Constants
    {
        //Número de "aspectos" en que dividir, y nº de capas máxima para cada aspecto
        public const int NUM_ASPECTS = 4;
        public const int MAX_LAYERS = 4;
    }
    #endregion
}