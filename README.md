# Lorentz

This is a project that uses Java to plot the famous Lorenz Attractor.
Ever since I read Ian Stewart's "Does God Play Dice?", the Lorenz Attractor has fascinated me, but it seemed extremely hard to code it up. However, I was inspired by Code Train's <a href = "https://www.youtube.com/watch?v=f0lkz2gSsIk"> "Coding Challenge #12: The Lorenz Attractor in Processing"</a>.

## Description

The Lorenz Attractor is a popular mathematical figure developed by Edward Lorenz when attempting to solve a series of ODEs modelling atmospheric convection:

<p align="center"> <a href="https://www.codecogs.com/eqnedit.php?latex=\\&space;\frac{dx}{dt}&space;=&space;\sigma(y-x)&space;\\&space;\\&space;\frac{dy}{dt}&space;=&space;x(\rho&space;-&space;z)&space;-&space;y&space;\\&space;\\&space;\frac{dz}{dt}&space;=&space;xy&space;-&space;\beta&space;z" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\\&space;\frac{dx}{dt}&space;=&space;\sigma(y-x)&space;\\&space;\\&space;\frac{dy}{dt}&space;=&space;x(\rho&space;-&space;z)&space;-&space;y&space;\\&space;\\&space;\frac{dz}{dt}&space;=&space;xy&space;-&space;\beta&space;z" title="\\ \frac{dx}{dt} = \sigma(y-x) \\ \\ \frac{dy}{dt} = x(\rho - z) - y \\ \\ \frac{dz}{dt} = xy - \beta z" /></a></p>

where the standard values for the constants are:

<p align = "center"><a href="https://www.codecogs.com/eqnedit.php?latex=\\&space;\sigma&space;=&space;10&space;\\&space;\\&space;\rho&space;=&space;28&space;\\&space;\\&space;\beta&space;=&space;\frac{8}{3}" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\\&space;\sigma&space;=&space;10&space;\\&space;\\&space;\rho&space;=&space;28&space;\\&space;\\&space;\beta&space;=&space;\frac{8}{3}" title="\\ \sigma = 10 \\ \\ \rho = 28 \\ \\ \beta = \frac{8}{3}" /></a></p>

It is a prime example used when showcasing how chaos arises in deterministic equations.
The program uses the stdlib to create a canvas, upon which we plot the (x,z) coordinates of the numerical solution. 
The solution is found via Euler's Method, using (x,y,z) = (0,20,12) as initial conditions, and a step size h = 0.01.
We use a for loop with 100001 iterations to iteratively calculate the new coordinates that will be plotted.

## Features

There are 2 key distinguishing features in this program:

* the colour of the attractor
* the capacity to save the coordinates

### Colour of Attractor

The attractor is drawn using a "rainbow" effect. This is done by converting the colour from RGB to HSB (hue, saturation and brightness). The hue is then changed slightly, producing a new colour. We then turn this colour back into its RGB form. This is done by the function *changeHue*:

```
public Color changeHue(Color colour, int factor) {
        // change colour to hsb
        float[] hsb = Color.RGBtoHSB(colour.getRed(), colour.getGreen(), colour.getBlue(),null);
        
        // change the hue value
        hsb[0] = (float) ((hsb[0] + factor * 0.01) % 255);
        
        // create a new rgb constant with the new hue value
        int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
        
        // get the rgb attributes of the new colour
        // to understand how:
        // https://android.jlelse.eu/java-when-to-use-n-8-0xff-and-when-to-use-byte-n-8-2efd82ae7dd7
        int red = (rgb>>16)&0xFF;
        int green = (rgb>>8)&0xFF;
        int blue = rgb&0xFF;
        return new Color(red,green,blue);
    }
```

These are the results:

<p align="center">
  <img src="https://github.com/alv31415/Lorentz/blob/master/Lorenz%20Attractor/Screenshot%202020-08-18%20at%2019.37.07.png"/>
  <img src="https://github.com/alv31415/Lorentz/blob/master/Lorenz%20Attractor/Screenshot%202020-08-18%20at%2020.34.47.png"/>
</p>

### Saving Coordinates

With each iteration, the calculated coordinates are stored in a JSONArray, which is in turn stored in a JSONObject and ultimately saved in a json file. We ensure that old files are not overridden, and provide methods to generate new file names. Coordinates created during the program's current execution are all saved to the same file. The option to save coordinates can be enabled within the class constructor. I thought that saving the coordinates could be useful, as it could allow programs in other languages to plot the attractor without necessarily having to undertake all the calculations.



