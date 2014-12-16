package com.lela.util.utilities.image;

import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/21/12
 * Time: 8:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageColor {

    BufferedImage image;
    int baseSize = 128;

    public ImageColor(BufferedImage image)
    {
        this.image = image;
    }

    public int getColor()
    {
        image = rescale(image);
        long total = 0;
        long resolution = image.getHeight() * image.getWidth();
        for(int x = 0; x < image.getWidth(); x++)
        {
            for(int y = 0; y < image.getHeight(); y++)
            {
                total = total + image.getRGB(x, y);
            }
        }


        int color = (int)(total/resolution);
        return color;
    }

    public static int[] getRGBArr(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        return new int[]{red,green,blue};

    }

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
}
