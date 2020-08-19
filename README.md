# Lorentz

A project that uses the stdlib to draw a Lorenz Attractor, using Euler's Method to numerically solve the 3 equations:

<p align="center"> <a href="https://www.codecogs.com/eqnedit.php?latex=\\&space;\frac{dx}{dt}&space;=&space;\sigma(y-x)&space;\\&space;\\&space;\frac{dy}{dt}&space;=&space;x(\rho&space;-&space;z)&space;-&space;y&space;\\&space;\\&space;\frac{dz}{dt}&space;=&space;xy&space;-&space;\beta&space;z" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\\&space;\frac{dx}{dt}&space;=&space;\sigma(y-x)&space;\\&space;\\&space;\frac{dy}{dt}&space;=&space;x(\rho&space;-&space;z)&space;-&space;y&space;\\&space;\\&space;\frac{dz}{dt}&space;=&space;xy&space;-&space;\beta&space;z" title="\\ \frac{dx}{dt} = \sigma(y-x) \\ \\ \frac{dy}{dt} = x(\rho - z) - y \\ \\ \frac{dz}{dt} = xy - \beta z" /></a></p>

where we used standard values for the constants:

<p align = "center"><a href="https://www.codecogs.com/eqnedit.php?latex=\\&space;\sigma&space;=&space;10&space;\\&space;\\&space;\rho&space;=&space;28&space;\\&space;\\&space;\beta&space;=&space;\frac{8}{3}" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\\&space;\sigma&space;=&space;10&space;\\&space;\\&space;\rho&space;=&space;28&space;\\&space;\\&space;\beta&space;=&space;\frac{8}{3}" title="\\ \sigma = 10 \\ \\ \rho = 28 \\ \\ \beta = \frac{8}{3}" /></a></p>

## Description

The Lorenz Attractor is a popular mathematical figure developed by Edward Lorenz when attempting a series of ODEs modelling atmospheric convection.
It is a prime example used when showcasing how chaos arises in deterministic equations.
The program uses the stdlib to create a canvas, upon which we plot the (x,z) coordinates of the numerical solution. 
The solution is found via Euler's Method, using (x,y,z) = (0,20,12) as initial conditions, and a step size h = 0.01.
We use a for loop with 100001 iterations to iteratively calculate the new coordinates that will be plotted.

## Features

There are 2 key distinguishing features in this program.
