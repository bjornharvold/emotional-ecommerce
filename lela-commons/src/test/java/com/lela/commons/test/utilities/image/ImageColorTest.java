package com.lela.commons.test.utilities.image;

import com.lela.util.utilities.image.ImageColor;
import com.lela.util.utilities.image.ImageTrim;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/21/12
 * Time: 8:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageColorTest {


    @Test
    public void testGetColorTan() throws Exception
    {
        ClassPathResource img = new ClassPathResource("testdata/images/similar/low_res_b_5.jpg");
        BufferedImage image = ImageIO.read(img.getURL());
        ImageTrim imageTrim = new ImageTrim(new Color(240, 240, 240).getRGB());
        image = imageTrim.trim(image);
        ImageColor imageColor = new ImageColor(image);
        int[] color = ImageColor.getRGBArr(imageColor.getColor());
        System.out.println(color[0]);
        System.out.println(color[1]);
        System.out.println(color[2]);
    }

    @Test
    public void testGetColorRed() throws Exception
    {
        ClassPathResource img = new ClassPathResource("testdata/images/similar/low_res_b_1.jpg");
        BufferedImage image = ImageIO.read(img.getURL());
        ImageTrim imageTrim = new ImageTrim(new Color(240, 240, 240).getRGB());
        image = imageTrim.trim(image);
        ImageColor imageColor = new ImageColor(image);
        int[] color = ImageColor.getRGBArr(imageColor.getColor());
        System.out.println(color[0]);
        System.out.println(color[1]);
        System.out.println(color[2]);

        img = new ClassPathResource("testdata/images/similar/low_res_b_3.jpg");
        image = ImageIO.read(img.getURL());
        image = imageTrim.trim(image);
        imageColor = new ImageColor(image);
        color = ImageColor.getRGBArr(imageColor.getColor());
        System.out.println(color[0]);
        System.out.println(color[1]);
        System.out.println(color[2]);
    }

    @Test
    public void testGetColorGray() throws Exception
    {
        ClassPathResource img = new ClassPathResource("testdata/images/similar/low_res_b_4.jpg");
        BufferedImage image = ImageIO.read(img.getURL());
        ImageTrim imageTrim = new ImageTrim(new Color(240, 240, 240).getRGB());
        image = imageTrim.trim(image);
        ImageColor imageColor = new ImageColor(image);
        int[] color = ImageColor.getRGBArr(imageColor.getColor());
        System.out.println(color[0]);
        System.out.println(color[1]);
        System.out.println(color[2]);
    }
}
