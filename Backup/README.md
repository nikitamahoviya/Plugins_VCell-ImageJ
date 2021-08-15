# Backup

These java files are for future reference purposes. During the course of time I had tried out various permutations with the plugins and for that made many changes in the existing ones from making them easily understandable to creating a whole new file.

### The 'Minimal' Files

A lot of time gets elapsed till the result gets loaded after clicking ‘OK’ on the information displayed on the GUI. So tomake this elapsing time attractive, there is an addition of a code segment that shows the ‘percentage’ loaded. 

To understand the overall functionality of code easily, we can get rid of these progress bars and even understand what segment of code they are affecting other than just showing the progress. This way we can easily understand the dependency of various segments.

#### [ModelLoad_Minimal](https://github.com/nikitamahoviya/Plugins_VCell-ImageJ/blob/main/Backup/ModelLoad_Minimal.java)
This is a reduced version of [ModelLoad](https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/src/main/java/org/vcell/imagej/plugin/ModelLoad.java) .  , whose sole purpose is to display the GUI and the result image. The segment showing the GUI loading time and the time elapsed in loading the result has been omitted. This way it's just a file performing a minimum number of tasks.

#### [VCellPlugin_Minimal](https://github.com/nikitamahoviya/Plugins_VCell-ImageJ/blob/main/Backup/VCellPlugin_Minimal.java)
This is a reduced version of [VCellPlugin](https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/src/main/java/org/vcell/imagej/plugin/VCellPlugin.java]) , whose sole purpose is to display the GUI and the result image. The segment showing the GUI loading time and the time elapsed in loading the result has been omitted. This way it's just a file performing a minimum number of tasks.

### Other Files

#### [LinePlot_Search](https://github.com/nikitamahoviya/Plugins_VCell-ImageJ/blob/main/Backup/LinePlot_Search.java)
It was an attempt to combine  [VCellPlugin](https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/src/main/java/org/vcell/imagej/plugin/VCellPlugin.java)  and [ModelLoad](https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/src/main/java/org/vcell/imagej/plugin/ModelLoad.java) to give an interface in which the user doesn't have to type-in the information but just select the most appropriate one from a drop down list.

#### [ModelLoad_Analyse](https://github.com/nikitamahoviya/Plugins_VCell-ImageJ/blob/main/Backup/ModelLoad_Analyse.java)
This has a vivid description, explaining the workflow of [ModelLoad](https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/src/main/java/org/vcell/imagej/plugin/ModelLoad.java) 

#### [VCellPlugin_JComboBox](https://github.com/nikitamahoviya/Plugins_VCell-ImageJ/blob/main/Backup/VCellPlugin_JComboBox.java)
This is to understand how JComboBox is functioning inside [VCellPlugin](https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/src/main/java/org/vcell/imagej/plugin/VCellPlugin.java]). The original file was having the JComboBox commands as comments so on uncommenting them we can see how those commands are responding.

#### [Combinefig](https://github.com/nikitamahoviya/Plugins_VCell-ImageJ/blob/main/Backup/Combinefig.java)
This is the initiation of the plug-in development of groovy script [combineFig.groovy](https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/combineFig.groovy) 
