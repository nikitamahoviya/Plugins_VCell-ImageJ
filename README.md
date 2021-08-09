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
