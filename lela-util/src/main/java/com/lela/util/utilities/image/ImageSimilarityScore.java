package com.lela.util.utilities.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;


/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/15/12
 * Time: 2:28 PM
 * This classw as derived fromt the Java Image Processing Cookbook
 */

/*
* Part of the Java Image Processing Cookbook, please see
* http://www.lac.inpe.br/~rafael.santos/JIPCookbook.jsp
* for information on usage and distribution.
* Rafael Santos (rafael.santos@lac.inpe.br)
*/
/**
 * This class uses a very simple, naive similarity algorithm to compare an image
 * with all others in the same directory.
 */


public class ImageSimilarityScore {
    // The reference image "signature" (25 representative pixels, each in R,G,B).
    // We use instances of Color to make things simpler.

    // The base size of the images.
    private static final int baseSize = 300;

    private static final double max = 25 * (Math.sqrt( 255*255 + 255*255 + 255*255 ));

    private boolean trim = true;

    ImageTrim imageTrim = new ImageTrim(new Color(240, 240, 240).getRGB());

    public ImageSimilarityScore(){}

    public ImageSimilarityScore(boolean trim)
    {
        this.trim = trim;
    }
    /*
    * The constructor, which creates the GUI and start the image processing task.
    */
    public double calculate(URL originalUrl, URL compareUrl) throws IOException
    {
        BufferedImage ref = ImageIO.read(originalUrl);
        BufferedImage other = ImageIO.read(compareUrl);
        return calculate(ref, other);
    }

    public double calculate(BufferedImage ref, BufferedImage other) throws IOException
    {

        // Put the reference, scaled, in the left part of the UI.

        if(trim){
            ref = imageTrim.trim(ref);
        }
        ref = rescale(ref);


        // Calculate the signature vector for the reference.
        Color[][] signature = calcSignature(ref);

        // For each image, calculate its signature and its distance from the
        // reference signature.

        if(trim)
          other = imageTrim.trim(other);

        double distance = 0.0d;

        other = rescale(other);
        distance = calcDistance(other, signature);

        return round(100.0d - (100*(distance/max))); //this gives us a percentage
    }

    /*
    * This method rescales an image to 300,300 pixels using the JAI scale
    * operator.
    */
    private BufferedImage rescale(BufferedImage i)
    {
        float scaleW = ((float) baseSize) / i.getWidth();
        float scaleH = ((float) baseSize) / i.getHeight();
        // Scales the original image
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(i);
        pb.add(scaleW);
        pb.add(scaleH);
        pb.add(0.0F);
        pb.add(0.0F);
        pb.add(new InterpolationNearest());
        // Creates a new, scaled image and uses it on the DisplayJAI component
        RenderedOp renderImage = JAI.create("scale", pb);

        BufferedImage mine = renderImage.getAsBufferedImage();

//                new BufferedImage(renderImage.getWidth(),
//                        renderImage.getHeight(),
//                        BufferedImage.TYPE_INT_RGB);
//        mine.getGraphics().drawImage((Image)renderImage, 0, 0, null);
        return mine;
    }

    /*
    * This method calculates and returns signature vectors for the input image.
    */
    private Color[][] calcSignature(RenderedImage i)
    {
        // Get memory for the signature.
        Color[][] sig = new Color[5][5];
        // For each of the 25 signature values average the pixels around it.
        // Note that the coordinate of the central pixel is in proportions.
        float[] prop = new float[]
                {1f / 10f, 3f / 10f, 5f / 10f, 7f / 10f, 9f / 10f};
        for (int x = 0; x < 5; x++)
            for (int y = 0; y < 5; y++)
                sig[x][y] = averageAround(i, prop[x], prop[y]);
        return sig;
    }

    /*
    * This method averages the pixel values around a central point and return the
    * average as an instance of Color. The point coordinates are proportional to
    * the image.
    */
    private Color averageAround(RenderedImage i, double px, double py)
    {
        // Get an iterator for the image.
        RandomIter iterator = RandomIterFactory.create(i, null);
        // Get memory for a pixel and for the accumulator.
        double[] pixel = new double[3];
        double[] accum = new double[3];
        // The size of the sampling area.
        int sampleSize = 15;
        int numPixels = 0;
        // Sample the pixels.
        for (double x = px * baseSize - sampleSize; x < px * baseSize + sampleSize; x++)
        {
            for (double y = py * baseSize - sampleSize; y < py * baseSize + sampleSize; y++)
            {
                iterator.getPixel((int) x, (int) y, pixel);
                accum[0] += pixel[0];
                accum[1] += pixel[1];
                accum[2] += pixel[2];
                numPixels++;
            }
        }
        // Average the accumulated values.
        accum[0] /= numPixels;
        accum[1] /= numPixels;
        accum[2] /= numPixels;
        return new Color((int) accum[0], (int) accum[1], (int) accum[2]);
    }

    /*
    * This method calculates the distance between the signatures of an image and
    * the reference one. The signatures for the image passed as the parameter are
    * calculated inside the method.
    */
    private double calcDistance(RenderedImage other, Color[][] signature)
    {
        // Calculate the signature for that image.
        Color[][] sigOther = calcSignature(other);
        // There are several ways to calculate distances between two vectors,
        // we will calculate the sum of the distances between the RGB values of
        // pixels in the same positions.
        double dist = 0;
        for (int x = 0; x < 5; x++)
            for (int y = 0; y < 5; y++)
            {
                int r1 = signature[x][y].getRed();
                int g1 = signature[x][y].getGreen();
                int b1 = signature[x][y].getBlue();
                int r2 = sigOther[x][y].getRed();
                int g2 = sigOther[x][y].getGreen();
                int b2 = sigOther[x][y].getBlue();
                double tempDist = Math.sqrt((r1 - r2) * (r1 - r2) + (g1 - g2)
                        * (g1 - g2) + (b1 - b2) * (b1 - b2));
                dist += tempDist;
            }
        return dist;
    }

    private static double round(double dub)
    {
        DecimalFormat twoDForm = new DecimalFormat("#");
        return Double.valueOf(twoDForm.format(dub));
    }




}