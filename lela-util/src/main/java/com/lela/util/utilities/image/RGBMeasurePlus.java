/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.util.utilities.image;

//~--- non-JDK imports --------------------------------------------------------

import ij.ImagePlus;
import ij.Prefs;
import ij.process.ImageProcessor;

import java.awt.*;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Class description
 *
 *
 * @version        ${project.version}, 11/09/16
 * @author         Bjorn Harvold    
 */
public class RGBMeasurePlus {

    /**
     * Value of pixels included in masks.
     */
    private static final int BLACK = 0xFF000000;

    /** Field description */
    private static final String
        RTMIN = "rTmin",
        RTMAX = "rTmax",
        GTMIN = "gTmin",
        GTMAX = "gTmax",
        BTMIN = "bTmin",
        BTMAX = "bTmax";

    /** Field description */
    private static int rtmin = Prefs.getInt(RTMIN, 0);

    /** Field description */
    private static int rtmax = Prefs.getInt(RTMAX, 255);

    /** Field description */
    private static int gtmin = Prefs.getInt(GTMIN, 0);

    /** Field description */
    private static int gtmax = Prefs.getInt(GTMAX, 255);

    /** Field description */
    private static int btmin = Prefs.getInt(BTMIN, 0);

    /** Field description */
    private static int btmax = Prefs.getInt(BTMAX, 255);

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private int area = 1;

    /**
     * stores the histogram area per channel
     */
    protected double[] histArea = new double[3];

    /**
     * stores the histogram means
     */
    protected double[] histMean = new double[3];

    /**
     * stores the histogram standard deviation
     */
    protected double[] histStdev = new double[3];

    /**
     * stores the 3-channel ROI histogram
     */
    protected int[][] histogram = new int[3][256];

    /** Field description */
    private int[] tamax = new int[3];

    /** Field description */
    private int[] tamin = new int[3];

    /** Field description */
    private int width,
                height = 1;

    /** Field description */
    private final ImagePlus imp;

    /** Field description */
    private byte[] mask;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     *
     * @param imp imp
     */
    public RGBMeasurePlus(ImagePlus imp) {
        this.imp = imp;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * runs the plugin
     *
     * @return Return value
     */
    public Integer[] calculateMeanRGBValues() {
        Integer[] result = new Integer[3];

        tamin[0] = rtmin;
        tamin[1] = gtmin;
        tamin[2] = btmin;
        tamax[0] = rtmax;
        tamax[1] = gtmax;
        tamax[2] = btmax;

        ImageProcessor ipmask = imp.getMask();

        mask   = (ipmask != null)
                 ? (byte[]) ipmask.getPixels()
                 : null;
        width  = imp.getWidth();
        height = imp.getHeight();

        Rectangle rect;

        try {
            // TODO fix deprecation
            rect = imp.getRoi().getBoundingRect();
            area = rect.width * rect.height;
        } catch (NullPointerException e) {
            rect = new Rectangle(width, height);
            area = width * height;
        }

        int[] pixels = (int[]) imp.getProcessor().getPixels();

        if (mask != null) {
            histogram = getHistogram(width, pixels, mask, rect);
        } else {
            histogram = getHistogram(width, pixels, rect);
        }

        calculateStatistics(histogram, tamin, tamax);
        result[0] = new Double(histMean[0]).intValue();
        result[1] = new Double(histMean[1]).intValue();
        result[2] = new Double(histMean[2]).intValue();

        return result;
    }

    /**
     * calculates the statistics histogram between Tmin and Tmax
     *
     * @param histogram histogram
     * @param tmin tmin
     * @param tmax tmax
     */
    private void calculateStatistics(int[][] histogram, int[] tmin, int[] tmax) {
        for (int col = 0; col < 3; col++) {
            double cumsum  = 0;
            double cumsum2 = 0;
            double aux     = 0;

            for (int i = tmin[col]; i <= tmax[col]; i++) {
                cumsum  += i * histogram[col][i];
                aux     += histogram[col][i];
                cumsum2 += i * i * histogram[col][i];
            }

            histMean[col] = cumsum / area;
            histArea[col] = aux / area;

            double stdDev = (area * cumsum2 - cumsum * cumsum) / area;

            histStdev[col] = Math.sqrt(stdDev / (area - 1.0));
        }
    }

    //~--- get methods --------------------------------------------------------

    /**
     * calculates the histogram
     *
     * @param width width
     * @param pixels pixels
     * @param roi roi
     *
     * @return Return value
     */
    private int[][] getHistogram(int width, int[] pixels, Rectangle roi) {
        int     c, r, g, b, v;
        int     roiY      = roi.y;
        int     roiX      = roi.x;
        int     roiWidth  = roi.width;
        int     roiHeight = roi.height;
        int[][] histogram = new int[3][256];

        for (int y = roiY; y < (roiY + roiHeight); y++) {
            int i = y * width + roiX;

            for (int x = roiX; x < (roiX + roiWidth); x++) {
                c = pixels[i++];
                r = (c & 0xff0000) >> 16;
                g = (c & 0xff00) >> 8;
                b = c & 0xff;
                histogram[0][r]++;
                histogram[1][g]++;
                histogram[2][b]++;
            }
        }

        return histogram;
    }

    /**
     * calculates the histogram
     *
     * @param width width
     * @param pixels pixels
     * @param mask mask
     * @param roi roi
     *
     * @return Return value
     */
    private int[][] getHistogram(int width, int[] pixels, byte[] mask, Rectangle roi) {
        int     c, r, g, b, v;
        int[][] histogram = new int[3][256];
        int     roiY      = roi.y;
        int     roiX      = roi.x;
        int     roiWidth  = roi.width;
        int     roiHeight = roi.height;

        for (int y = roiY, my = 0; y < (roiY + roiHeight); y++, my++) {
            int i  = y * width + roiX;
            int mi = my * roiWidth;

            for (int x = roiX; x < (roiX + roiWidth); x++) {
                if (mask[mi++] != BLACK) {
                    c = pixels[i];
                    r = (c & 0xff0000) >> 16;
                    g = (c & 0xff00) >> 8;
                    b = c & 0xff;

                    // v = (int)(r*0.299 + g*0.587 + b*0.114 + 0.5);
                    histogram[0][r]++;
                    histogram[1][g]++;
                    histogram[2][b]++;
                }

                i++;
            }
        }

        return histogram;
    }
}
