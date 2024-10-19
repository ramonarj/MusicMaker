using UnityEngine;
using UnityEditor;
using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using UnityEngine.SceneManagement;


#region Parte de la ventana
//Ventana del editor para elegir parámetros sobre la música generativa
public class MMWindow : EditorWindow
{
    #region Variables

    //Editor
    string userMsg = "Ningún paquete seleccionado";
    bool customPackage;
    bool[] openFolds;

    //Serializable
    MM.Package package; //Paquete
    bool[,] layers; //Información sobre qué capas empezarán activas
    float tempo = 1f; //Tempo

    //Nombre de archivos
    string layersFile;
    string packageFile;
    string defaultLayers = "1000100010001000";

    #endregion


    #region Métodos de Unity
    /*
     * Inicializa la ventana y crea una entrada en el menú
     */
    [MenuItem("Window/Music Maker")]
    static void Init()
    {
        MMWindow window = (MMWindow)EditorWindow.GetWindow(typeof(MMWindow));
        window.Show();
    }

    /*
     * Se llama cada vez que se abre la ventana
     */
    private void Awake()
    {
        //Nombre de los archivos
        packageFile = MM.Utils.GetPackageFile();
        layersFile = MM.Utils.GetLayersFile();

        //Para recuperar el paquete que habíamos puesto (con EditorPrefs vale)
        string packageName = EditorPrefs.GetString("PackageType");
        package = (MM.Package)Enum.Parse(typeof(MM.Package), packageName);

        //Para recuperar las capas (de archivo)
        layers = new bool[MM.Constants.NUM_ASPECTS, MM.Constants.MAX_LAYERS];
        if (File.Exists(layersFile))
        {
            //Personalizado 
            customPackage = true;

            //Leemos las capas activas del archivo
            string data = File.ReadAllText(layersFile);
            int c = 0;
            for (int i = 0; i < MM.Constants.NUM_ASPECTS; i++)
                for (int j = 0; j < MM.Constants.MAX_LAYERS; j++)
                    layers[i, j] = data[c++] == '1' ? true : false;
        }

        //Si no había archivo...
        else
        {
            ///...ponemos la 1º capa activa por defecto
            for (int i = 0; i < MM.Constants.NUM_ASPECTS; i++)
                layers[i, 0] = true;
        }

        //Menús abiertos
        openFolds = new bool[MM.Constants.NUM_ASPECTS];
    }

    /*
     * "Tick" de la ventana (para mostrar cosas al usuario)
     */
    void OnGUI()
    {
        //Selección de paquete
        GUILayout.Label("Selección de paquete temático", EditorStyles.boldLabel);
        package = (MM.Package)EditorGUILayout.EnumPopup("Elige un paquete:", package);

        //Hay un paquete seleccionado
        if (package != MM.Package.None)
        {
            //Personalizar paquete
            customPackage = EditorGUILayout.BeginToggleGroup("Personalizar paquete", customPackage);

            //Capa rítmica
            var indent = EditorGUI.indentLevel;
            openFolds[0] = EditorGUILayout.Foldout(openFolds[0], "Ritmo");
            if (openFolds[0])
            {
                EditorGUI.indentLevel++;
                layers[0, 0] = EditorGUILayout.Toggle("Percusión base", layers[0, 0]);
                layers[0, 1] = EditorGUILayout.Toggle("Percusión secundaria", layers[0, 1]);
                layers[0, 2] = EditorGUILayout.Toggle("Efectos", layers[0, 2]);
            }

            //Capa armónica
            EditorGUI.indentLevel = indent;
            openFolds[1] = EditorGUILayout.Foldout(openFolds[1], "Armonía");
            if (openFolds[1])
            {
                EditorGUI.indentLevel++;
                layers[1, 0] = EditorGUILayout.Toggle("Progresión de acordes", layers[1, 0]);
                layers[1, 1] = EditorGUILayout.Toggle("Acordes octavados", layers[1, 1]);
            }

            //Capa melódica
            EditorGUI.indentLevel = indent;
            openFolds[2] = EditorGUILayout.Foldout(openFolds[2], "Melodía");
            if (openFolds[2])
            {
                EditorGUI.indentLevel++;
                layers[2, 0] = EditorGUILayout.Toggle("Melodía aleatoria", layers[2, 0]);
                layers[2, 1] = EditorGUILayout.Toggle("Clichés (1)", layers[2, 1]);
                layers[2, 2] = EditorGUILayout.Toggle("Clichés (2)", layers[2, 2]);
            }

            //Capa de efectos
            //EditorGUI.indentLevel = indent;
            //openFolds[3] = EditorGUILayout.Foldout(openFolds[3], "FX");
            //if (openFolds[3])
            //{
            //    EditorGUI.indentLevel++;
            //    layers[3, 0] = EditorGUILayout.Toggle("Fx 1", layers[3, 0]);
            //    layers[3, 1] = EditorGUILayout.Toggle("Fx 2", layers[3, 1]);
            //    layers[3, 2] = EditorGUILayout.Toggle("Fx 3", layers[3, 2]);
            //    layers[3, 3] = EditorGUILayout.Toggle("Fx 4", layers[3, 3]);
            //}

            //Tempo deseado
            EditorGUI.indentLevel = indent;
            tempo = EditorGUILayout.Slider("Tempo (sobre el base)", tempo, 0.5f, 1.5f);

            //Termina la personalización
            EditorGUILayout.EndToggleGroup();
        }

        //GUARDA LOS CAMBIOS
        if (GUILayout.Button("Guardar cambios"))
        {
            //Se ha elegido algún paquete
            if (package != MM.Package.None)
            {
                //Guardamos en EditorPrefs y también en archivo para que luego lo lea el MonoBehaviour
                EditorPrefs.SetString("PackageType", package.ToString());
                File.WriteAllText(packageFile, package.ToString());

                //Guardamos las capas como un string
                string layersStr = "";
                for (int i = 0; i < MM.Constants.NUM_ASPECTS; i++)
                    for (int j = 0; j < MM.Constants.MAX_LAYERS; j++)
                        layersStr += layers[i, j] ? '1' : '0';

                //Vemos si se ha personalizado el paquete o no
                userMsg = "Has elegido el paquete " + package.ToString();
                if (layersStr != defaultLayers && customPackage)
                    userMsg += " (Personalizado)";
                else
                    customPackage = false;

                //Escribimos a disco las capas
                File.WriteAllText(layersFile, layersStr);

                //Si no existe el GO, lo creamos
                GameObject musicMaker = GameObject.Find("MusicMaker");
                if (!musicMaker)
                {
                    //Creamos el MusicMaker
                    musicMaker = new GameObject("MusicMaker");
                    musicMaker.AddComponent<MusicMaker>();
                }
            }

            //No se ha elegido
            else
            {
                //Borrar el archivo con el nombre y el GameObject
                userMsg = "Ningún paquete seleccionado";
                if (File.Exists(packageFile))
                {
                    File.Delete(packageFile);
                    GameObject MMgo = GameObject.Find("MusicMaker");
                    if (MMgo)
                        DestroyImmediate(MMgo);
                }
                if (File.Exists(layersFile))
                    File.Delete(layersFile);
            }
        }
        //Mensaje al usuario
        GUILayout.Label(userMsg, EditorStyles.boldLabel);
    }
    #endregion
}
#endregion

#region Parte del inspector (drawer)
[CustomPropertyDrawer(typeof(MM.MusicInput))]
public class MusicInputDrawer : PropertyDrawer
{
    private int height = 58;
    private int compIndex = 0;

    public override Single GetPropertyHeight(SerializedProperty property, GUIContent label)
    {
        return height;
    }


    // Draw the property inside the given rects
    public override void OnGUI(Rect position, SerializedProperty property, GUIContent label)
    {
        // Using BeginProperty / EndProperty on the parent property means that
        // prefab override logic works on the entire property.
        EditorGUI.BeginProperty(position, label, property);

        // Draw label
        position = EditorGUI.PrefixLabel(position, GUIUtility.GetControlID(FocusType.Passive), label);

        // Don't make child fields be indented
        var indent = EditorGUI.indentLevel;
        EditorGUI.indentLevel = 0;

        // Propiedades 
        SerializedProperty objProp = property.FindPropertyRelative("objeto");
        SerializedProperty compProp = property.FindPropertyRelative("componente");
        SerializedProperty varProp = property.FindPropertyRelative("variable");
        SerializedProperty minProp = property.FindPropertyRelative("min");
        SerializedProperty maxProp = property.FindPropertyRelative("max");
        SerializedProperty indexProp = property.FindPropertyRelative("index");

        // Rectángulos donde se pintan
        var objRect = new Rect(position.x, position.y, 100, 16);
        var compRect = new Rect(position.x, position.y + 18, 150, 16);
        var varRect = new Rect(position.x, position.y + 36, 120, 20);
        var indexRect = new Rect(position.x + 130, position.y + 36, 35, 16);
        var minRect = new Rect(position.x + 125, position.y + 36, 35, 16);
        var maxRect = new Rect(position.x + 165, position.y + 36, 35, 16);


        // Objetos (es un field normal y corriente) NOTA: todos los objetos tienen al menos un componente (el Transform)
        EditorGUI.PropertyField(objRect, objProp, GUIContent.none);

        if (objProp.objectReferenceValue == null)
            return;
        // Componente
        DisplayComponents(property, compRect);

        // Variables
        DisplayVariables(property, varRect);

        // Para los floats
        MM.MusicInput input = new MM.MusicInput((Component)compProp.objectReferenceValue, varProp.stringValue);
        object variable = input.GetValue();
        if (variable != null || input.IsAnArray())
        {
            if (input.IsAnArray())
                EditorGUI.PropertyField(indexRect, indexProp, GUIContent.none);
            else if (input.GetType().Name != "Boolean")
            {
                EditorGUI.PropertyField(minRect, minProp, GUIContent.none);
                EditorGUI.PropertyField(maxRect, maxProp, GUIContent.none);
            }
            //else -> es boolean
        }

        // Set indent back to what it was
        EditorGUI.indentLevel = indent;

        EditorGUI.EndProperty();
    }

    //Muestra un Popup con todos los componentes que tiene "objProp", devuelve el seleccionado
    private void DisplayComponents(SerializedProperty property, Rect rect)
    {
        //Cogemos las propiedades
        SerializedProperty objProp = property.FindPropertyRelative("objeto");
        SerializedProperty compProp = property.FindPropertyRelative("componente");

        //GameObject escogido y lista con sus componentes
        object go = objProp.objectReferenceValue;
        List<Component> comps = ((GameObject)go).GetComponents<Component>().ToList<Component>();

        //Quitamos los componentes que no tienen atributos públicos
        comps.RemoveAll(x => MM.Utils.GetProperties(x) == null);

        //Vemos cuál está seleccionado
        Component seleccionado = comps.Find((x) => x == compProp.objectReferenceValue);

        //Lista de componentes disponibles
        string[] compList = new string[comps.Count];
        for (int i = 0; i < comps.Count; i++)
            compList[i] = comps[i].GetType().ToString();

        //Hacemos el Popup
        int compIndex = Mathf.Max(EditorGUI.Popup(rect, comps.IndexOf(seleccionado), compList), 0);
        compProp.objectReferenceValue = comps[compIndex];
    }

    //Muestra todas las variables que tiene un componente
    private void DisplayVariables(SerializedProperty property, Rect rect)
    {
        //Cogemos las propiedades
        SerializedProperty compProp = property.FindPropertyRelative("componente");
        SerializedProperty varProp = property.FindPropertyRelative("variable");

        //Componente escogido
        object comp = compProp.objectReferenceValue;

        //Lista de variables disponibles
        List<string> varNames = MM.Utils.GetProperties(comp);
        string seleccionada = varNames.Find((x) => x == varProp.stringValue);
        string[] varList = varNames.ToArray<string>();

        //Hacemos el Popup
        int varIndex = Mathf.Max(EditorGUI.Popup(rect, varNames.IndexOf(seleccionada), varList), 0);
        varProp.stringValue = varNames[varIndex];
    }
}



[CustomPropertyDrawer(typeof(MM.MusicOutput))]
public class MusicOutputDrawer : PropertyDrawer
{
    // Draw the property inside the given rects
    public override void OnGUI(Rect position, SerializedProperty property, GUIContent label)
    {
        // Using BeginProperty / EndProperty on the parent property means that
        // prefab override logic works on the entire property.
        EditorGUI.BeginProperty(position, label, property);

        // Draw label
        position = EditorGUI.PrefixLabel(position, GUIUtility.GetControlID(FocusType.Passive), label);

        // Don't make child fields be indented
        var indent = EditorGUI.indentLevel;
        EditorGUI.indentLevel = 0;

        // Propiedades hermanas
        SerializedProperty aspectProp = property.FindPropertyRelative("aspect");
        SerializedProperty layerProp = property.FindPropertyRelative("layerNo");


        // Rectángulos donde se pintan
        var aspectRect = new Rect(position.x, position.y, 100, 16);
        var layerRect = new Rect(position.x + 105, position.y, 30, 16);

        
        //Display
        EditorGUI.PropertyField(aspectRect, aspectProp, GUIContent.none);
        if (aspectProp.enumValueIndex > 0)
            EditorGUI.PropertyField(layerRect, layerProp, GUIContent.none);

        // Set indent back to what it was
        EditorGUI.indentLevel = indent;

        EditorGUI.EndProperty();
    }
}
#endregion