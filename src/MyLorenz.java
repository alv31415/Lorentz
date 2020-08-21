import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

// 1) Constants & Constructor
// 2) Euler Method
// 3) Colour Methods
// 4) File Management Methods
// 5) Curve Plotting

public class MyLorenz {

    //_______________________________________ Constants & Contructor _______________________________________

    double x;
    double y;
    double z;
    Color colour;
    boolean toSave;
    boolean toRainbowPen;

    int sigma = 10;
    double beta = 8.0/3.0;
    int rho = 28;
    double h = 0.001; // step-size for euler method
    int width = 600;
    int height = 600;
    int xmax = 50;
    int ymax = 50;
    boolean isNewExec = true;
    public static String jsonFile = "lorenz-coordinates";
    public static String fileToLoad = jsonFile + ".json";

    /**
     * Initialises the coordinates for a Lorenz Curve
     * @param x initial x coordinate
     * @param y initial y coordinate
     * @param z initial z coordinate
     * @param colour colour of curve
     * @param toSave whether the coordinates should be saved
     * @param toRainbowPen whether the colour of the curve should be "rainbow"
     */

    public MyLorenz(double x, double y, double z, Color colour, boolean toSave, boolean toRainbowPen) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.colour = colour;
        this.toSave = toSave;
        this.toRainbowPen = toRainbowPen;
    }

    //_______________________________________ Euler Method _______________________________________

    /**
     * Uses Euler's method, with step size h, to numerically solve the ODEs
     */

    public void getNewCoordinates() {

        double newx = x + h*fx(x,y,z);
        double newy = y + h*fy(x,y,z);
        double newz = z + h*fz(x,y,z);

        x = newx;
        y = newy;
        z = newz;

    }

    /**
     * ODE with the x differential
     */

    public double fx (double x, double y, double z) {
        return sigma*(y-x);
    }

    /**
     * ODE with the y differential
     */

    public double fy (double x, double y, double z) {
        return x*(rho - z) - y;
    }

    /**
     * ODE with the z differential
     */

    public double fz (double x, double y, double z) {
        return x*y - beta*z;
    }

    //_______________________________________ Colour Methods _______________________________________

    /**
     * Used to make the colour of the line change in a rainbow fashion
     * @param colour the initial colour of the line
     * @param factor the "speed" with which the colours transition (from red to orange to yellow etc ...)
     * @return the original colour with an increased hue
     */

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

    /**
     * Special method if the colour is green
     * @return a nice colour gradient that alternates between green and yellow
     */

    public Color changeGreen(Color colour, int factor) {
        int red = (int) ((colour.getRed() + factor*0.8) % 255);
        int green = colour.getGreen() > factor ? (int) ((colour.getGreen() - factor * 0.8) % 255) : colour.getGreen();
        int blue = colour.getBlue() % 64;

        return new Color(red,green,blue);
    }

    //_______________________________________ File Management Methods _______________________________________

    /**
     * Used to create a JSONArray containing the x,y,z coordinates of a point in the curve
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @param z the z-coordinate of the point
     * @return a JSONArray containing all the information about a point
     */

    public JSONArray createPoint(double x, double y, double z) {
        JSONArray coords = new JSONArray();
        coords.add(x);
        coords.add(y);
        coords.add(z);

        return coords;
    }

    /**
     * Used to manage the saving of the Lorenz coordinates into a json file
     * @param jsonCoords the JSONObject to which we save the coordinates
     */

    public void updateFile(JSONObject jsonCoords) {
        String extension = ".json";
        try {
            if (isNewExec) {  // executes if we are running the program for the first time
                String newfile = jsonFile + extension;
                File potentialFile = new File(newfile);
                int i = 0;
                while (potentialFile.exists()) {  // creates a new file name so that old files are not overridden
                    newfile = String.format(jsonFile + "-%f-%f-%f-%d%s", x, y, z, i, extension);
                    potentialFile = new File(newfile);
                    i++;
                }
                jsonFile = newfile;  // remember the file name so that we write to it the next time
            }
            FileWriter writer = new FileWriter(jsonFile);
            writer.write(jsonCoords.toJSONString());
            writer.close();
        }
        catch (IOException e) {
            System.out.println("There was a problem!");
        }
    }

    /**
     * Used to extract the data from a json file
     * @param jsonFile the file from which to take the data
     * @return a TreeMap containing the coordinates of a Lorenz Curve
     * Uses the order of the coordinates as the key, and a double's array containing the (x,y,z) coordinates
     */

    public TreeMap<Integer,double[]> getCoordsFromJSON(String jsonFile) {
        JSONParser parser = new JSONParser();
        TreeMap<Integer,double[]> coordinates = new TreeMap<>();

        try {
            Object obj = parser.parse(new FileReader(jsonFile));
            JSONObject myFile = (JSONObject) obj;
            for (Object key : myFile.keySet()) {
                int intKey = Integer.parseInt((String) key);
                double[] coords = fromJSONtoDouble((JSONArray) myFile.get(key));
                coordinates.put(intKey,coords);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return coordinates;
    }

    /**
     * Takes in a JSONArray of doubles and turns it into a Java array of doubles (of size 3)
     * @param jsonArray the JSONArray from which we extract the data
     * @return the double's array contining the data of the JSONArray
     */

    private double[] fromJSONtoDouble(JSONArray jsonArray) {

        double[] doubleArray = new double[3];

        for (int i = 0; i < 3; i++) {
            doubleArray[i] = Double.parseDouble(jsonArray.get(i) + "");
        }

        return doubleArray;
    }

    //_______________________________________ Curve Plotting Methods _______________________________________

    /**
     * Sets up the canvas to draw the Lorenz Curve based on widthm height, xmax and ymax attributes
     */

    public void setUp() {
        StdDraw.setCanvasSize(width,height);
        StdDraw.setXscale(-xmax,xmax);
        StdDraw.setYscale(-ymax,ymax);
        StdDraw.clear(Color.black); // paints the canvas black
        StdDraw.enableDoubleBuffering(); // paints into an invisible canvas, and then renders it all into the visible canvas
    }

    /**
     * Draws the Lorenz curve represented by a MyLorenz instance
     */

    public void drawCurve() {
        setUp();

        JSONObject jsonCoords = new JSONObject(); // used to store the coordinates of the curve

        for (int i = 0; i < 1000001; i++) {
            if (colour == Color.GREEN)
                StdDraw.setPenColor(changeGreen(colour,i));
            else if (toRainbowPen)
                StdDraw.setPenColor(changeHue(colour,i));
            else
                StdDraw.setPenColor(colour);
            StdDraw.point(x,z);
            if (toSave) {
                jsonCoords.put(i, createPoint(x, y, z));
                updateFile(jsonCoords);
                isNewExec = false;
            }
            getNewCoordinates(); // updates the coordinates for the next iteration
            StdDraw.show();
            StdDraw.pause(10);
        }
    }

    /**
     * Uses the coordinates in a JSON file to plot a Lorenz Curve
     * @param jsonFile the file from which the data is taken
     */

    public void drawCurveFromJSON(String jsonFile) {

        TreeMap<Integer,double[]> coordinates = getCoordsFromJSON(jsonFile);

        setUp();

        for(Map.Entry<Integer,double[]> entry : coordinates.entrySet()) {

            int key = entry.getKey();
            double[] value = entry.getValue();

            if (colour == Color.GREEN)
                StdDraw.setPenColor(changeGreen(colour,key));
            else if (toRainbowPen)
                StdDraw.setPenColor(changeHue(colour,key));
            else
                StdDraw.setPenColor(colour);
            StdDraw.point(value[0],value[2]);

            StdDraw.show();
            StdDraw.pause(10);
        }
    }


    public static void main(String[] args) {
        MyLorenz lorenz = new MyLorenz(0.0,20.0,12.0,Color.BLUE,false,false);
        lorenz.drawCurve();
        //lorenz.drawCurveFromJSON(fileToLoad);
    }

}
