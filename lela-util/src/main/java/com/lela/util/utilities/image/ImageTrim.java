package com.lela.util.utilities.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/16/12
 * Time: 8:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageTrim {

    final static Logger logger = LoggerFactory.getLogger(ImageTrim.class);

    //static final int WHITE = new Color(240, 240, 240).getRGB();

    int thresholdColor = 0;

    public ImageTrim(int thresholdColor)
    {
        this.thresholdColor = thresholdColor;
    }

    public BufferedImage trim(BufferedImage img) {
        img = trimOnce(img);
        //write(img);
        return img;
    }

    private BufferedImage trimOnce(BufferedImage img)
    {
        int right  = getTrimmedRight(img) -0;
        int bottom = getTrimmedBottom(img) -0;
        int top = getTrimmedTop(img) ;
        int left = getTrimmedLeft(img) ;

        BufferedImage trimmedOnce = new BufferedImage(right - left, bottom - top, BufferedImage.TYPE_INT_RGB);
        //slow...
        Graphics g = trimmedOnce.createGraphics();
        g.drawImage( img, -left -1, -top -1, null );
        g.dispose();
        return trimmedOnce;
    }

    /*
    private void write(BufferedImage img) {
        try {
            ImageIO.write(img, "jpg", new File( "/Users/ballmw/temp/"+UUID.randomUUID()+ ".jpg"));
        } catch (IOException e) {
            throw new RuntimeException( "Problem writing image", e );
        }
    }*/


    private int getTrimmedRight(BufferedImage img) {
        int height       = img.getHeight();
        int width        = img.getWidth();
        int trimmedWidth = 0;


        for(int j = width - 1; j >= 0; j--) {
            for(int i = 0; i < height; i++) {
                if(img.getRGB(j, i) < thresholdColor) {
                    return j;
                }
            }
        }

        return trimmedWidth;
    }

    private int getTrimmedLeft(BufferedImage img) {
        int height       = img.getHeight();
        int width        = img.getWidth();
        int trimmedWidth = 0;

        for(int j = 0; j < width; j++) {
            for(int i = 0; i < height; i++) {

                if(img.getRGB(j, i) < thresholdColor) {
                    return j;
                }
            }
        }

        return trimmedWidth;
    }

    private int getTrimmedBottom(BufferedImage img) {
        int width         = img.getWidth();
        int height        = img.getHeight();
        int trimmedHeight = 0;

        for(int j = height - 1; j >= 0; j--) {
            for(int i = 0; i < width; i++) {
                if(img.getRGB(i, j) < thresholdColor ) {
                    return j;
                }
            }
        }

        return trimmedHeight;
    }

    private int getTrimmedTop(BufferedImage img) {
        int width         = img.getWidth();
        int height        = img.getHeight();
        int trimmedHeight = 0;

        for(int j = 0; j < height; j++) {
            for(int i = 0; i < width; i++) {
                if(img.getRGB(i, j) < thresholdColor) {
                    return j;
                }
            }
        }

        return trimmedHeight;
    }

    private BufferedImage rotate180(BufferedImage img)
    {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, -1);
        tx.translate(-img.getWidth(null), -img.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(img, null);
    }
}
