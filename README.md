# Plugins_VCell-ImageJ

This repository is for the GSoC'21 project @ NRNB to [Develop GUI for ImageJ groovy script calling VCell API](https://github.com/nrnb/GoogleSummerOfCode/issues/148).

## Project Overview

We have to create a service for VCell users that allows Fiji/ImageJ scripting to directly access the VCell client and then expand this service into a series of user-friendly plugins for ImageJ that will automate processing and analyzing cell imaging simulation experiments. The task will include:
- Entering the appropriate images to create a geometry
- Setting initial conditions for the simulation
- Running multiple simulations with varying parameter sets and
- Visualizing and comparing simulation results to the original experimental image set

## Mentors 

- [Ann Cowan](https://github.com/ACowan0105)
- [Frank Morgan](https://github.com/vcfrmgit)
- [Michael Blinov](https://github.com/vcellmike)

## Getting Started with the project

The important thing before starting the development process is to make the environment compatible for various changes. So beginning with the setting up of the environment, [VCell README](https://github.com/nikitamahoviya/vcell/tree/master#readme) will walk you through all the steps. 
> - IDE -> Eclipse2019-06
> - jdk8.251
> - maven 3.6.1

Make your Eclipse workspace inside the local VCell folder and set all the arguments accordingly and install the dependencies.
![image](https://user-images.githubusercontent.com/43717626/128692639-341d1977-8a4a-493e-a53f-ad2e489aee61.png)

## Moving ahead with the project

The project was basically to design a menu structure that could give us options to choose from various ImageJ plugins calling VCell API. These plugins are basically Groovy files which when run in Macros produce the desired results. The workflow was such that on launching ImageJ interface, we got many option to explore in the toolbar, the ‘plugins’ option is the one that suits our needs.

![image](https://user-images.githubusercontent.com/43717626/129489338-322a7f77-3bbe-43b5-af18-63ea5080dbcc.png)

### Designing the menu structure

For designing the menu structure, I referred [ClearRecent.java](https://github.com/scijava/scijava-plugins-commands/blob/35790dea3819a7e46b3741abc179505d56e84a17/src/main/java/org/scijava/plugins/commands/io/ClearRecent.java) and the code is 

![image](https://user-images.githubusercontent.com/43717626/130117103-37f81749-0c13-4cb6-8106-39a037d57216.png)

Which signifies:

- The **Header** under which the plugin will be placed
- The **icon image** to clarify the purpose and make the plugin stand out
- The **hierarchy** or the serial order in which the plugin should appear
- The **short-cut keys** to access the plugin with keyboard

Taking the example of [VCellPlugin.java](https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/src/main/java/org/vcell/imagej/plugin/VCellPlugin.java)
The structure is designed as follows:

![image](https://user-images.githubusercontent.com/43717626/130122480-3c6d95b0-2cdc-47b4-b7f9-fc436a7d9ba7.png)

- **label = "VCell Plugins"** is the submenu of Plugins which has various other plugins with various functionality
- **iconPath="search_icon.gif"** is for assigning an icon image to the plugin
- **weight = 4** assigns a priority to submenu VCell Plugins and seperates it from other available options with a line.
- **mnemonic = 'V'** helps the user to access VCell Plugins with keyboard by pressing the key 'v'. it is represented by an underline on 'V' Of VCell Plugins
- **label = "VCell Model Search"** is for accessing the the plugin named VCell Model Search inside the VCell Plugins Submenu
- **weight = RecentFileService.MAX_FILES_SHOWN + 10** is for assigning the hierachy to the plugin inside the list of the submenu
- **mnemonic = 'm'** helps the user to access the plugin VCell Model Search with keyboard by pressing the key 'm'. it is represented by an underline on 'M' Of VCell Model Search
- **accelerator = "^M"** is another way to access the plugin VCell Model Search through keyboard by pressing a combination of Ctrl and M. This is indicated on the side of the Plugin name.
- **label = "VCell Model Search "** is for naming the GUI of the result obtained after simulation


So in this menu structure we have Plugins which has various options like VCell Plugins and VCell Help which in turn has a list of functionalities to offer.

![image](https://user-images.githubusercontent.com/43717626/129489349-6486daf7-4753-447b-8bc3-3928ac528edd.png)

#### VCell Plugins

![image](https://user-images.githubusercontent.com/43717626/129489386-27bf3a2d-1e47-43db-8d10-5e20224856b9.png)

The VCell Plugins has a drop-down list of Plugins which are in communication with VCell:
Line Plot - The Groovy script [chart.groovy](https://github.com/virtualcell/vcell/blob/master/vcell-imagej-helper/chart.groovy) is converted into a Java plugin which results into into 2 plots
- Line Plot by Time
- Time Plot by Distance

It takes up data like, ModelType, VCell User ID, VCell User Name, Application, Simulation, Variable, TimePont, Start and End Index.
VCell Model Search - The Groovy script []() is converted into a Java plugin. In addition to its just the direct conversion it has certain additional features like to select various variable and suitable times through sliders.By default it takes up data such as  ModelType, VCell User ID, VCell User Name, Application, and then accordingly loads the Simulation, Variable values


#### VCell Help

![image](https://user-images.githubusercontent.com/43717626/129489402-ac731a0e-6f26-412e-b52a-4089009c5b85.png)

VCell Help is basically for future users and developers. If someone wants to create plugins out of some Groovy scripts then they may refer to these as how elements are added to a GUI and make them functional.

- VCell ImageJ-Help - This plugin open into a GUI which retrieves information from your machine, like your Operating System, Architecture and returns it. Informs about the pre requisites for running the VCell plugins and also gives online assistance by directing to browser with a click

- VCell ImageJ Groovy Scripts - This plugins opens into a GUI that gives information about running the various groovy scripts in Macros and also directs the user to various scripts on Github
- VCell ImageJ Template - This plugin is not related to any functionality but is a reference for future developers which might help them in making their own plugins It has various commands for text editing, adding buttons, browsing files etc
- VCell ImageJ Template Example - This plugin is another template which is functional and tells how we can implement  various elements in a GUI which can run as a plugin.

## Testing of Plugin in Various Cases

Reference: VCellPlugin.java

### Testing A:

 - Did Not logged in into my VCell Account
 - Activated my Fiji Imagej service
 
 Which gave the resultant as

![image](https://user-images.githubusercontent.com/43717626/124156564-68f8be80-dab5-11eb-97cd-150fba74c6db.png)

### Testing B:
 - Logged into my VCell Account
 - Stopped the Fiji (Imagej) service

Which gave the resultant as

![image](https://user-images.githubusercontent.com/43717626/124158009-099bae00-dab7-11eb-9784-30dda620335b.png)
