package com.lela.util.utilities.image;

import com.lela.util.utilities.image.dto.ImageData;
import org.apache.commons.lang.StringUtils;

import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/16/12
 * Time: 10:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageComparisonResult {
    //private URL url1;
    //private URL url2;
    private ImageData image1;
    private ImageData image2;

    public ImageEquality pHashEquality;
    public ImageEquality similarity;
    public ImageEquality color;

    public ImageComparisonResult(ImageData image1, ImageData image2)
    {
        this.image1 = image1;
        this.image2 = image2;
    }

    public String toString()
    {
        return StringUtils.substringAfterLast(image1.getUrl().toString(),"/") + " - " + StringUtils.substringAfterLast(image2.getUrl().getFile().toString(), "/") + " HASH:" + pHashEquality + " SCORE:"+ similarity;
    }

    public ImageData getImage1() {
        return image1;
    }

    public ImageData getImage2() {
        return image2;
    }

    public boolean isDuplicate()
    {
        return  (((this.color == ImageEquality.SAME && this.similarity == ImageEquality.SAME) || this.pHashEquality == ImageEquality.SAME)
                || (this.color == ImageEquality.SAME && this.similarity == ImageEquality.SIMILAR && this.pHashEquality == ImageEquality.SIMILAR));
    }

    public boolean isSimilar()
    {
        return ((this.similarity == ImageEquality.SIMILAR ^ this.pHashEquality == ImageEquality.SIMILAR));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageComparisonResult that = (ImageComparisonResult) o;

        if (image1.equals(that.image1) && image2.equals(that.image2)) return true;
        if (image2.equals(that.image1) && image1.equals(that.image2)) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int result = image1.hashCode();
        result = result + image2.hashCode();
        return result;
    }
}
