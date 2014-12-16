package com.lela.util.utilities.image;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.lela.util.utilities.image.dto.ImageData;
import org.codehaus.jackson.map.ser.std.IterableSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/16/12
 * Time: 10:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageComparison {

    ImageData[] images;

    Set<ImageComparisonResult> compared = new HashSet<ImageComparisonResult>();

    Map<ImageData, BufferedImage> bufferedImages = new HashMap<ImageData, BufferedImage>();

    final static Logger logger = LoggerFactory.getLogger(ImageComparison.class);

    private boolean failOnFirstDuplicate = false;
    ImageTrim imageTrim = new ImageTrim(new Color(240, 240, 240).getRGB());

    //Compare a bunch of images to find duplicates
    public ImageComparison(ImageData[] images) throws IOException
    {
        this.images = images;
        for(int i = 0; i < images.length; i ++)
        {
            try
            {
                prepareImageForComparison(images[i]);
                for(int j = 0; j < images.length; j++)
                {
                    prepareImageForComparison(images[j]);
                    ImageComparisonResult imageComparisonResult = new ImageComparisonResult(images[i], images[j]);
                    if(! images[i].equals(images[j]) )
                        compared.add(imageComparisonResult);
                }
            }
            catch(IIOException e)
            {
                //Something happened trying to prep this image
                //We're going for speed so we can just ignore this image and assume it's not a duplicate
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void prepareImageForComparison(ImageData image) throws IOException {
        BufferedImage img = ImageIO.read(image.getUrl());

        img = imageTrim.trim(img);
        bufferedImages.put(image, img);
        image.setHeight(img.getHeight());
        image.setWidth(img.getWidth());
    }

    //Compare one image to a bunch of others to find if it's a duplicate
    public ImageComparison(ImageData newImage, ImageData[] images) throws IOException
    {
        this.failOnFirstDuplicate = true;
        this.images = images;
        try
        {
          prepareImageForComparison(newImage);
          for(int i = 0; i < images.length; i ++)
          {
            prepareImageForComparison(images[i]);
            ImageComparisonResult imageComparisonResult = new ImageComparisonResult(newImage, images[i]);
            compared.add(imageComparisonResult);
          }
        }
        catch(IIOException e)
        {
            //Something happened trying to prep this image
            //We're going for speed so we can just ignore this image and assume it's not a duplicate
            logger.error(e.getMessage(), e);
        }
    }



    public void compare() throws Exception
    {
        List<Thread> threads = new ArrayList<Thread>();
        for(final ImageComparisonResult imageComparisonResult:compared)
        {

            Thread thread1 = new Thread(new Runnable() {
                public void run() {
                    try
                    {
                        judgeSimilarity(imageComparisonResult);

                    }
                    catch(IOException ioe){
                    }
                }});

            Thread thread2 = new Thread(new Runnable() {
                public void run() {
                    try
                    {
                        judgePHash(imageComparisonResult);
                    }
                    catch(Exception ioe){
                    }
                }});
            Thread thread3 = new Thread(new Runnable() {
                public void run() {
                    try
                    {
                        judgeColor(imageComparisonResult);
                    }
                    catch(Exception ioe){
                    }
                }});




            thread2.start();
            thread1.start();
            thread3.start();

            thread2.join();
            thread1.join();
            thread3.join();

            if(this.failOnFirstDuplicate && imageComparisonResult.isDuplicate())
            {
                break;
            }
        }

    }

    private void judgeColor(ImageComparisonResult imageComparisonResult) throws Exception
    {
        ImageColor imageColor1 = new ImageColor(bufferedImages.get(imageComparisonResult.getImage1()));
        ImageColor imageColor2 = new ImageColor(bufferedImages.get(imageComparisonResult.getImage2()));

        int result = Math.abs(Math.abs(imageColor1.getColor()) - Math.abs(imageColor2.getColor()));
        if(result < 192000)
        {
            imageComparisonResult.color = ImageEquality.SAME;
        }
        else
        {
            imageComparisonResult.color = ImageEquality.DIFFERENT;
        }
    }

    private void judgePHash(ImageComparisonResult imageComparisonResult) throws Exception {
        ImagePHash imagePHash = new ImagePHash(false);
        int distance = imagePHash.distance(bufferedImages.get(imageComparisonResult.getImage1()), bufferedImages.get(imageComparisonResult.getImage2()));
        logger.debug("phash:"+distance);
        if (distance < 2)
            imageComparisonResult.pHashEquality = ImageEquality.SAME;
        else if( distance <= 7)
            imageComparisonResult.pHashEquality = ImageEquality.SIMILAR;
        else
            imageComparisonResult.pHashEquality = ImageEquality.DIFFERENT;
    }

    private void judgeSimilarity(ImageComparisonResult imageComparisonResult) throws IOException {
        ImageSimilarityScore imageSimilarityScore = new ImageSimilarityScore(false);
        double delta = imageSimilarityScore.calculate(bufferedImages.get(imageComparisonResult.getImage1()), bufferedImages.get(imageComparisonResult.getImage2()));
        logger.debug("Similarity:"+delta);
        if (delta >= 97.0d)
            imageComparisonResult.similarity = ImageEquality.SAME;
        else if ( delta > 94.0d )
            imageComparisonResult.similarity = ImageEquality.SIMILAR;
        else
            imageComparisonResult.similarity = ImageEquality.DIFFERENT;
    }

    public Set<ImageComparisonResult> getCompared()
    {
        return compared;
    }

    public List<ImageComparisonResult> getDuplicateResults()
    {
        return new ArrayList(Collections2.filter(compared, new Predicate<ImageComparisonResult>() {
            public boolean apply(ImageComparisonResult result)
            {
                return result.isDuplicate();
            }
        }));
    }

    public List<ImageComparisonResult> getSimilarResults()
    {
        return new ArrayList(Collections2.filter(compared, new Predicate<ImageComparisonResult>() {
            public boolean apply(ImageComparisonResult result)
            {
                return result.isSimilar();
            }
        }));
    }

    private Set<ImageData> findImagesLikeThese(Set<ImageData> images)
    {
        List<ImageComparisonResult> duplicates = getDuplicateResults();

        Set<ImageData> combinedImages = new HashSet<ImageData>();
        combinedImages.addAll(images);
        for(ImageComparisonResult result:duplicates)
        {
            if(combinedImages.contains(result.getImage1()) || combinedImages.contains(result.getImage2()))
            {
                combinedImages.add(result.getImage1());
                combinedImages.add(result.getImage2());
            }
        }
        if(combinedImages.size() > images.size())//we added new ones
            combinedImages = findImagesLikeThese(combinedImages);

        return combinedImages;
    }

    public Set<Set<ImageData>> getDuplicates()
    {
        Set<Set<ImageData>> setsOfImages = new HashSet<Set<ImageData>>();
        List<ImageComparisonResult> duplicates = getDuplicateResults();

        for(ImageComparisonResult result:duplicates)
        {
            Set<ImageData> image1 = new HashSet<ImageData>();
            image1.add(result.getImage1());
            image1.add(result.getImage2());
            setsOfImages.add(findImagesLikeThese(image1));

        }
        return setsOfImages;
    }

}
