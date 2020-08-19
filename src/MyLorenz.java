import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyLorenz {

    double x;
    double y;
    double z;
    Color colour;
    boolean toSave;

    int sigma = 10;
    double beta = 8.0/3.0;
    int rho = 28;
    double h = 0.001; // step-size for euler method
    boolean isNewExec = true;
    String jsonFile = "lorenz-coordinates";

    /**
     * Initialises the coordinates for a Lorenz Curve
     * @param x initial x coordinate
     * @param y initial y coordinate
     * @param z initial z coordinate
     * @param colour colour of curve
     */

    public MyLorenz(double x, double y, double z, Color colour, boolean toSave) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.colour = colour;
        this.toSave = toSave;
    }

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

    /**
     * Sets up the canvas to draw the Lorenz Curve
     * @param width width of the canvas
     * @param height height of the canvas
     * @param xmax the maximum value of the x-scale
     * @param ymax the minimum value of the y-scale
     */
    public void setUp(int width, int height, int xmax, int ymax) {
        StdDraw.setCanvasSize(width,height);
        StdDraw.setXscale(-xmax,xmax);
        StdDraw.setYscale(-ymax,ymax);
        StdDraw.clear(Color.black); // paints the canvas black
        StdDraw.enableDoubleBuffering(); // paints into an invisible canvas, and then renders it all into the visible canvas
    }

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
     * Draws the Lorenz curve represented by a MyLorenz instance
     * @throws IOException if there is a problem saving the coordinates of the points in the Lorenz curve
     */
    public void drawCurve() throws IOException {
        setUp(600,600,50,50);

        JSONObject jsonCoords = new JSONObject(); // used to store the coordinates of the curve

        for (int i = 0; i < 1000001; i++) {
            StdDraw.setPenColor(changeHue(colour,i));
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
     * Used to create a JSONArray containing the x,y,z coordinates of a point in the curve
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @param z the z-coordinate of the point
     * @return a JSONArray contianing all the information about a point
     */
    public JSONArray createPoint(double x, double y, double z) {
        JSONArray coords = new JSONArray();
        coords.add(x);
        coords.add(y);
        coords.add(z);

        return coords;
    }

    /**
     * Used to manage the saving of the Lorenz coordinates
     * @param jsonCoords the JSONObject to which we save the coordinates
     * @throws IOException
     */
    public void updateFile(JSONObject jsonCoords) throws IOException {
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

    public static void main(String[] args) throws IOException {
        MyLorenz lorenz = new MyLorenz(0.0,20.0,12.0,Color.RED,false);
        lorenz.drawCurve();
    }

}
