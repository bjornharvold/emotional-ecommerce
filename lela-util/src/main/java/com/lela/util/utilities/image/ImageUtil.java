/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.util.utilities.image;

import com.lela.util.UtilityException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import ij.ImagePlus;
import ij.process.ImageConverter;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.imgscalr.Scalr;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.ColorConvertOp;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.UUID;

import static org.imgscalr.Scalr.*;

/**
 * User: bjorn
 * Date: Oct 4, 2008
 * Time: 5:18:29 PM
 */
public class ImageUtil {

    private static final int PIXELS_300 = 300;


    /**
     * Resizes an image quickly
     *
     * @param image        image
     * @param desiredWidth desiredWidth
     * @param extension    extension
     * @return resized input stream
     * @throws IOException exception
     */
    public BufferedImage resize(InputStream image, int desiredWidth, String extension) throws IOException {

        BufferedImage original = ImageIO.read(image);
        BufferedImage resized = resize(original, desiredWidth);

        return resized;
    }

    public BufferedImage resize(BufferedImage original, int desiredWidth) throws IOException {
        BufferedImage result;

        int currentWidth = original.getWidth();

        if (currentWidth > desiredWidth) {
            result = Scalr.resize(original, Method.ULTRA_QUALITY, Mode.FIT_TO_WIDTH, desiredWidth);
        } else if (currentWidth < desiredWidth) {
            result = padImageToDesiredSize(original, desiredWidth);
        } else {
            result = original;
        }

        return result;
    }

    public BufferedImage scale(BufferedImage original, int desiredWidth)
    {
        BufferedImage scaledImage = Scalr.resize(original, Method.ULTRA_QUALITY, Mode.FIT_TO_WIDTH, desiredWidth);
        return scaledImage;
    }


    /**
     * We are assuming that we are working with a square image here
     *
     * @param image        image
     * @param desiredWidth desiredWidth
     * @return new image with desired size
     */
    public BufferedImage padImageToDesiredSize(BufferedImage image, int desiredWidth) {
        // create a new image
        BufferedImage newImage = new BufferedImage(desiredWidth, desiredWidth, image.getType());

        // set the background color to white
        Graphics g = newImage.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, desiredWidth, desiredWidth);

        // draw the original image on top of the new image
        // we need it to be centered in the new, bigger, image
        // 500 - 200 / 2 =
        int xy = (desiredWidth - image.getWidth()) / 2;
        g.drawImage(image, xy, xy, null);
        g.dispose();

        return newImage;
    }

    /*
    private BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    */

    public String toHex(Integer n) {
        if (n == null) {
            return "00";
        }

        n = Math.max(0, Math.min(n, 255));

        StringBuilder sb = new StringBuilder();
        sb.append("0123456789ABCDEF".charAt((n - n % 16) / 16));
        sb.append("0123456789ABCDEF".charAt(n % 16));

        return sb.toString();
    }

    /**
     * This will only work if both images are the same size
     *
     * @param original   original
     * @param comparable comparable
     * @return Distance computed between the images. 0 is a perfect match.
     * @throws com.lela.util.UtilityException
     */
    public Double computeImageDistance(BufferedImage original, BufferedImage comparable) throws UtilityException {
        Double result = null;

        try {
            // make a square
            if (original.getWidth() > original.getHeight()) {
                original = padImageHeightAndCenter(original);
            } else if (original.getWidth() < original.getHeight()) {
                original = padImageWidthAndCenter(original);
            }

            // make a square
            if (comparable.getWidth() > comparable.getHeight()) {
                comparable = padImageHeightAndCenter(comparable);
            } else if (comparable.getWidth() < comparable.getHeight()) {
                comparable = padImageWidthAndCenter(comparable);
            }

            // first we need to scale to 300 for this to work properly
            BufferedImage original_300 = resize(original, PIXELS_300);
            BufferedImage comparable_300 = resize(comparable, PIXELS_300);

            Color[][] signature = calcSignature(original_300);

            result = calcDistance(signature, comparable_300);
        } catch (IOException e) {
            throw new UtilityException(e.getMessage(), e);
        }

        return result;
    }

    /*
    * Scaling without any libraries
    
    public RenderedImage resize(RenderedImage image, float desiredWidth) {
        float scaleW = desiredWidth / image.getWidth();
        float scaleH = desiredWidth / image.getHeight();
        // Scales the original image
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image);
        pb.add(scaleW);
        pb.add(scaleH);
        pb.add(0.0F);
        pb.add(0.0F);
        pb.add(new InterpolationNearest());
        // Creates a new, scaled image and uses it on the DisplayJAI component
        return JAI.create("scale", pb);
    }
    */

    /*
    * This method calculates and returns signature vectors for the input image.
    */
    private Color[][] calcSignature(RenderedImage i) {
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
    private Color averageAround(RenderedImage i, double px, double py) {
        // Get an iterator for the image.
        RandomIter iterator = RandomIterFactory.create(i, null);
        // Get memory for a pixel and for the accumulator.
        double[] pixel = new double[3];
        double[] accum = new double[3];
        // The size of the sampling area.
        int sampleSize = 15;
        int numPixels = 0;
        // Sample the pixels.
        for (double x = px * PIXELS_300 - sampleSize; x < px * PIXELS_300 + sampleSize; x++) {
            for (double y = py * PIXELS_300 - sampleSize; y < py * PIXELS_300 + sampleSize; y++) {
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
    private double calcDistance(Color[][] signature, RenderedImage other) {
        // Calculate the signature for that image.
        Color[][] sigOther = calcSignature(other);
        // There are several ways to calculate distances between two vectors,
        // we will calculate the sum of the distances between the RGB values of
        // pixels in the same positions.
        double dist = 0;
        for (int x = 0; x < 5; x++)
            for (int y = 0; y < 5; y++) {
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

    public BufferedImage compressJpegQuality(BufferedImage image, float quality) throws IOException {

        // Get a ImageWriter for jpeg format.
        Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix("jpeg");
        if (!writers.hasNext()) throw new IllegalStateException("No writers found");
        ImageWriter writer = writers.next();
        // Create the ImageWriteParam to compress the image.
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);
        // The output will be a ByteArrayOutputStream (in memory)
        ByteArrayOutputStream bos = new ByteArrayOutputStream(32768);
        ImageOutputStream ios = ImageIO.createImageOutputStream(bos);
        writer.setOutput(ios);
        writer.write(null, new IIOImage(image, null, null), param);
        ios.flush(); // otherwise the buffer size will be zero!
        // From the ByteArrayOutputStream create a RenderedImage.
        ByteArrayInputStream in = new ByteArrayInputStream(bos.toByteArray());

        return ImageIO.read(in);
    }

    public BufferedImage padImageHeightAndCenter(BufferedImage image) {
        int diff = image.getWidth() - image.getHeight();

        // create a new image
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getWidth(), image.getType());

        // set the background color to white
        Graphics g = newImage.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, image.getWidth(), image.getWidth());

        // draw the original image on top of the new image
        g.drawImage(image, 0, diff / 2, null);
        g.dispose();
        return newImage;
    }

    public BufferedImage padImageWidthAndCenter(BufferedImage image) {
        int diff = image.getHeight() - image.getWidth();

        // create a new image
        BufferedImage newImage = new BufferedImage(image.getHeight(), image.getHeight(), image.getType());

        // set the background color to white
        Graphics g = newImage.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, image.getHeight(), image.getHeight());

        // draw the original image on top of the new image
        g.drawImage(image, diff / 2, 0, null);
        g.dispose();
        return newImage;
    }

    public Integer[] measureRGB(BufferedImage image, Double colorSwatchPercentageRectangle) {
        Integer width = image.getWidth();     // e.g. 300 pixels
        Integer height = image.getHeight();    // e.g. 300 pixels
        Integer centerX = width / 2;
        Integer centerY = height / 2;
        Integer rectangleWidth = new Double(Math.round(width * colorSwatchPercentageRectangle)).intValue();
        Integer rectangleHeight = new Double(Math.round(height * colorSwatchPercentageRectangle)).intValue();
        Integer startingPointX = centerX - rectangleWidth / 2;
        Integer startingPointY = centerY - rectangleHeight / 2;

        // load our image into ImageJ
        ImagePlus ip = new ImagePlus("rgb", image);

        ip.setRoi(startingPointX, startingPointY, rectangleWidth, rectangleHeight);

        // at this point we need to make sure we are working with an RGB image
        // if we are not we need to first convert the image to RGB
        if (ip.getType() != ImagePlus.COLOR_RGB) {
            ImageConverter converter = new ImageConverter(ip);

            converter.convertToRGB();
        }

        // create an RGB histogram to get the medium R G B values
        return new RGBMeasurePlus(ip).calculateMeanRGBValues();
    }

    public BufferedImage readMalformedJpeg(byte[] image) throws IOException
    {
        JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(new ByteArrayInputStream(image));

        BufferedImage src = decoder.decodeAsBufferedImage();

        try
        {
            ImageInfo imageInfo = Sanselan.getImageInfo(image);

            if( imageInfo.getColorType() == ImageInfo.COLOR_TYPE_CMYK)
            {
                WritableRaster srcRaster = src.getRaster();

                //prepare result image
                BufferedImage result = new BufferedImage(srcRaster.getWidth(), srcRaster.getHeight(), BufferedImage.TYPE_INT_RGB);
                WritableRaster resultRaster = result.getRaster();
                //prepare images.icc profiles
                ClassPathResource iccFile = new ClassPathResource("/images/icc/USSheetfedCoated.icc");
                ICC_Profile iccProfileCYMK = ICC_Profile.getInstance(iccFile.getInputStream());
                ColorSpace sRGBColorSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);

                //invert k channel
                for (int x = srcRaster.getMinX(); x < srcRaster.getWidth(); x++) {
                    for (int y = srcRaster.getMinY(); y < srcRaster.getHeight(); y++) {
                        float[] pixel = srcRaster.getPixel(x, y, (float[])null);
                        pixel[3] = 255f-pixel[3];
                        srcRaster.setPixel(x, y, pixel);
                    }
                }

                //convert
                ColorConvertOp cmykToRgb = new ColorConvertOp(new ICC_ColorSpace(iccProfileCYMK), sRGBColorSpace, null);
                cmykToRgb.filter(srcRaster, resultRaster);
                src = result;
            }
        }
        catch(ImageReadException readException)
        {
            throw new IOException(readException.getMessage());
        }
        return src;
    }

    public boolean isSolidBackground(BufferedImage bufferedImage)
    {
        //go around the perimeter looking for a non-white pixel
        //if one is found then we can trim the image
        int height = bufferedImage.getHeight()-1;
        int width = bufferedImage.getWidth()-1;

        int countOfNonWhiteSides = 0;

        //check the top
        for(int i = 0; i < width; i ++)
        {
            int color = bufferedImage.getRGB(i, 0);
            if(color < Color.WHITE.getRGB())
            {
                countOfNonWhiteSides++;
                //we found one, we're done
                break;
            }
        }

        //check the bottom
        for(int i = 0; i < width; i ++)
        {
            int color = bufferedImage.getRGB(i, height);
            if(color < Color.WHITE.getRGB())
            {
                countOfNonWhiteSides++;
                //we found one, we're done
                break;
            }
        }

        //check the left
        for(int i = 0; i < height; i ++)
        {
            int color = bufferedImage.getRGB(0, i);
            if(color < Color.WHITE.getRGB())
            {
                countOfNonWhiteSides++;
                //we found one, we're done
                break;
            }
        }

        //check the right
        for(int i = 0; i < height; i ++)
        {
            int color = bufferedImage.getRGB(width, i);
            if(color < Color.WHITE.getRGB())
            {
                countOfNonWhiteSides++;
                //we found one, we're done
                break;
            }
        }
        return countOfNonWhiteSides >= 2;
    }
}
