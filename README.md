# MusicMaker
A plugin for Unity which lets you include procedural and adaptative music to your game.  
Check the publication paper here: https://docta.ucm.es/entities/publication/8e5799fb-6f73-474a-870a-8e74b26efd6d

## How to install:

### SUPERCOLLIDER

1. Go to https://supercollider.github.io/ and download SuperCollider's last stable version
2. Open SuperCollider and check your computer's "Extensions" folder route by executing the line "Platform.systemExtensionDir;" (Ctrl + .)
3. Move all Supercollider files, as well as the "buffers" folder into that Extensions folder 
4. Compile all SuperCollider classes with "Language >> recompile class library"
5. Open the startup file with "file >> open startup file"
6. Paste the contents of main.scd into this startup file
7. Close SuperCollider and open it again


### UNITY PLUGIN

1. Move the "Unity" folder into the "Assets" folder of your Unity project
2. Once this is done, a new window called "Music Maker" appears in the Editor (Window >> Music Maker)
3. Customize the procedural music within this window and save. A new GameObject should appear in the scene.
4. Customize, if needed, the procedural tuples in this GameObject's inspector
5. When pressing "Play" in the Editor, the music should start playing after a few seconds
