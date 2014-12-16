package com.lela.commons.test.utilities.image;

import com.lela.util.utilities.image.ImageComparison;
import com.lela.util.utilities.image.ImageComparisonResult;
import com.lela.util.utilities.image.dto.ImageData;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.net.URL;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/16/12
 * Time: 10:29 AM
 * To change this
 * template use File | Settings | File Templates.
 */
public class ImageComparisonTest {

    private static final Logger log = LoggerFactory.getLogger(ImageComparisonTest.class);

    @Test
    public void testStrollerComparisons() throws Exception
    {
        ClassPathResource stroller1 = new ClassPathResource("testdata/images/similar/stroller1.jpg");
        ClassPathResource stroller2 = new ClassPathResource("testdata/images/similar/stroller2.jpg");
        ClassPathResource stroller3 = new ClassPathResource("testdata/images/similar/stroller3.jpg");
        ClassPathResource stroller4 = new ClassPathResource("testdata/images/similar/stroller4.jpg");
        ClassPathResource stroller5 = new ClassPathResource("testdata/images/similar/stroller5.jpg");

        URL[] images = new URL[]{stroller1.getURL(), stroller2.getURL(), stroller3.getURL(), stroller4.getURL(), stroller5.getURL()};

        Set<ImageData> dups2 = new HashSet<ImageData>();
        dups2.add(new ImageData(stroller1.getURL(), null, null, null, 0, 0l));
        dups2.add(new ImageData(stroller2.getURL(), null, null, null, 0, 0l));
        dups2.add(new ImageData(stroller3.getURL(), null, null, null, 0, 0l));


        //Set<ImageData> dups1 = new HashSet<ImageData>();
        //dups1.add(new ImageData(stroller4.getURL(), null, null, null));
        //dups1.add(new ImageData(stroller5.getURL(), null, null, null));

        Set<Set<ImageData>> expectedDups = new HashSet<Set<ImageData>>();
        //expectedDups.add(dups1);
        expectedDups.add(dups2);

        Set<Set<ImageData>> dups = doComparison(images);

        assertEquals("There should be 1 sets of images", 1, dups.size());
        assertTrue("These should contain all the duplicate images", dups.containsAll(expectedDups));


    }

    @Test
    public void compareCribComparison() throws Exception
    {
        ClassPathResource crib1 = new ClassPathResource("testdata/images/similar/crib_b_1.jpg");
        ClassPathResource crib2 = new ClassPathResource("testdata/images/similar/crib_b_2.jpg");
        ClassPathResource crib3 = new ClassPathResource("testdata/images/similar/crib_b_3.jpg");
        ClassPathResource crib4 = new ClassPathResource("testdata/images/similar/crib_b_4.jpg");
        ClassPathResource crib5 = new ClassPathResource("testdata/images/similar/crib_b_5.jpg");
        ClassPathResource crib6 = new ClassPathResource("testdata/images/similar/crib_b_6.jpg");
        ClassPathResource crib7 = new ClassPathResource("testdata/images/similar/crib_b_7.jpg");
        ClassPathResource crib8 = new ClassPathResource("testdata/images/similar/crib_b_8.jpg");

        URL[] images = new URL[]{crib1.getURL(), crib2.getURL(), crib3.getURL(), crib4.getURL(), crib5.getURL(), crib6.getURL(), crib7.getURL(), crib8.getURL()};

        Set<ImageData> dups2 = new HashSet<ImageData>();
        dups2.add(new ImageData(crib2.getURL(), null, null, null, 0, 0l));
        dups2.add(new ImageData(crib1.getURL(), null, null, null, 0, 0l));


        Set<ImageData> dups1 = new HashSet<ImageData>();
        dups1.add(new ImageData(crib7.getURL(), null, null, null, 0, 0l));
        dups1.add(new ImageData(crib6.getURL(), null, null, null, 0, 0l));

        Set<Set<ImageData>> expectedDups = new HashSet<Set<ImageData>>();
        expectedDups.add(dups1);
        //expectedDups.add(dups2);

        Set<Set<ImageData>> dups = doComparison(images);

        assertEquals("There should be 1 sets of images", 1, dups.size());
        assertTrue("There should contain all the duplicate images", dups.containsAll(expectedDups));
    }

    @Test
    public void compareCribPerformanceComparison() throws Exception
    {
        ClassPathResource crib6 = new ClassPathResource("testdata/images/similar/crib_b_6.jpg");
        ClassPathResource crib7 = new ClassPathResource("testdata/images/similar/crib_b_7.jpg");

        URL[] images = new URL[]{crib6.getURL(), crib7.getURL()};

        Set<ImageData> dups1 = new HashSet<ImageData>();
        dups1.add(new ImageData(crib7.getURL(), null, null, null, 0, 0l));
        dups1.add(new ImageData(crib6.getURL(), null, null, null, 0, 0l));

        Set<Set<ImageData>> expectedDups = new HashSet<Set<ImageData>>();
        expectedDups.add(dups1);

        Set<Set<ImageData>> dups = doComparison(images);

        assertEquals("There should be 1 sets of images", 1, dups.size());
        assertTrue("There should contain all the duplicate images", dups.containsAll(expectedDups));
    }

    @Test
    public void compareTvAComparison() throws Exception
    {
        ClassPathResource tv1 = new ClassPathResource("testdata/images/similar/tv_a_1.jpg");
        ClassPathResource tv2 = new ClassPathResource("testdata/images/similar/tv_a_2.jpg");
        ClassPathResource tv3 = new ClassPathResource("testdata/images/similar/tv_a_3.jpg");

        URL[] images = new URL[]{tv1.getURL(), tv2.getURL(), tv3.getURL()};

        Set<Set<ImageData>> dups = doComparison(images);

        assertEquals("There should be 0 sets of images", 0, dups.size());
    }

    @Test
    public void compareTvBComparison() throws Exception
    {
        ClassPathResource tv1 = new ClassPathResource("testdata/images/similar/tv_b_1.jpg");
        ClassPathResource tv2 = new ClassPathResource("testdata/images/similar/tv_b_2.jpg");
        ClassPathResource tv3 = new ClassPathResource("testdata/images/similar/tv_b_3.jpg");
        ClassPathResource tv4 = new ClassPathResource("testdata/images/similar/tv_b_4.jpg");
        ClassPathResource tv5 = new ClassPathResource("testdata/images/similar/tv_b_5.jpg");
        ClassPathResource tv6 = new ClassPathResource("testdata/images/similar/tv_b_6.jpg");


        URL[] images = new URL[]{tv1.getURL(), tv2.getURL(), tv3.getURL(), tv4.getURL(), tv5.getURL(), tv6.getURL()};

        Set<Set<ImageData>> dups = doComparison(images);

        assertEquals("There should be 0 sets of images", 0, dups.size());
    }

    @Test
    public void compareCribUrlComparison() throws Exception
    {
        URL crib1 = new URL("http://ecx.images-amazon.com/images/I/212ps6PkEZL._SL500_.jpg");
        URL crib2 = new URL("http://ecx.images-amazon.com/images/I/212ps6PkEZL._SL500_.jpg");

        URL[] images = new URL[]{crib1, crib2};

        List<ImageData> imageDatas = new ArrayList<ImageData>();
        for(long i = 0; i < images.length; i ++)
        {
            imageDatas.add(new ImageData(images[(int)i], i, null, null, 0, 0l));
        }
        ImageComparison imageComparison = new ImageComparison(imageDatas.toArray(new ImageData[0]));
        for(ImageData imageData:imageDatas)
        {
            assertTrue("Image height is not set", imageData.getHeight() > 0);
            assertTrue("Image width is not set", imageData.getWidth() > 0);
        }
        imageComparison.compare();
        Set<Set<ImageData>> dups = imageComparison.getDuplicates();

        assertEquals("There should be 1 sets of images", 1, dups.size());
    }

    @Test
    public void compare404URLs() throws Exception
    {
        URL crib1 = new URL("http://ecx.images-amazon.com/images/I/212ps6PkEZL._SL500_.jpg");
        URL crib2 = new URL("http://ecx.images-amazon.com/images/I/i_do_not_exist.jpg");

        URL[] images = new URL[]{crib1, crib2};

        List<ImageData> imageDatas = new ArrayList<ImageData>();
        for(long i = 0; i < images.length; i ++)
        {
            imageDatas.add(new ImageData(images[(int)i], i, null, null, 0, 0l));
        }
        ImageComparison imageComparison = new ImageComparison(imageDatas.toArray(new ImageData[0]));
        imageComparison.compare();
        Set<Set<ImageData>> dups = imageComparison.getDuplicates();

        //The second image is a 404 so we should just assume that it is not a duplicate
        assertEquals("There should be 0 sets of images", 0, dups.size());
    }

    @Test
    public void compare404URLs2() throws Exception
    {
        URL crib1 = new URL("http://ecx.images-amazon.com/images/I/212ps6PkEZL._SL500_.jpg");
        URL crib2 = new URL("http://ecx.images-amazon.com/images/I/i_do_not_exist.jpg");
        URL crib3 = new URL("http://ecx.images-amazon.com/images/I/i_do_not_exist.jpg");

        URL[] images = new URL[]{crib2, crib3};

        ImageData imageData = new ImageData(crib1, -1l, null, null, 0, 0l);

        List<ImageData> imageDatas = new ArrayList<ImageData>();
        for(long i = 0; i < images.length; i ++)
        {
            imageDatas.add(new ImageData(images[(int)i], i, null, null, 0, 0l));
        }
        ImageComparison imageComparison = new ImageComparison(imageData,  imageDatas.toArray(new ImageData[0]));
        imageComparison.compare();
        Set<Set<ImageData>> dups = imageComparison.getDuplicates();

        //The second image is a 404 so we should just assume that it is not a duplicate
        assertEquals("There should be 0 sets of images", 0, dups.size());
    }

    @Test
    public void compareComboComparison() throws Exception
    {
        ClassPathResource combo1 = new ClassPathResource("testdata/images/similar/combo1.jpg");
        ClassPathResource combo2 = new ClassPathResource("testdata/images/similar/combo2.jpg");
        ClassPathResource combo3 = new ClassPathResource("testdata/images/similar/combo3.jpg");
        ClassPathResource combo4 = new ClassPathResource("testdata/images/similar/combo4.jpg");
        ClassPathResource combo5 = new ClassPathResource("testdata/images/similar/combo5.jpg");
        ClassPathResource combo6 = new ClassPathResource("testdata/images/similar/combo6.jpg");
        ClassPathResource combo7 = new ClassPathResource("testdata/images/similar/combo7.jpg");
        ClassPathResource combo8 = new ClassPathResource("testdata/images/similar/combo8.jpg");
        ClassPathResource combo9 = new ClassPathResource("testdata/images/similar/combo9.jpg");
        ClassPathResource combo10 = new ClassPathResource("testdata/images/similar/combo10.jpg");
        ClassPathResource combo11 = new ClassPathResource("testdata/images/similar/combo11.jpg");
        ClassPathResource combo12 = new ClassPathResource("testdata/images/similar/combo12.jpg");
        ClassPathResource combo13 = new ClassPathResource("testdata/images/similar/combo13.jpg");

        URL[] images = new URL[]{combo1.getURL(), combo2.getURL(), combo3.getURL(), combo4.getURL(), combo5.getURL(), combo6.getURL(), combo7.getURL(), combo8.getURL(), combo9.getURL(), combo10.getURL(), combo11.getURL(), combo12.getURL(), combo13.getURL()};

        Set<ImageData> dups2 = new HashSet<ImageData>();
        dups2.add(new ImageData(combo1.getURL(), null, null, null, 0, 0l));
        dups2.add(new ImageData(combo2.getURL(), null, null, null, 0, 0l));
        dups2.add(new ImageData(combo4.getURL(), null, null, null, 0, 0l));
        dups2.add(new ImageData(combo5.getURL(), null, null, null, 0, 0l));
        dups2.add(new ImageData(combo8.getURL(), null, null, null, 0, 0l));
        dups2.add(new ImageData(combo11.getURL(), null, null, null, 0, 0l));

        Set<ImageData> dups1 = new HashSet<ImageData>();
        dups1.add(new ImageData(combo3.getURL(), null, null, null, 0, 0l));
        dups1.add(new ImageData(combo6.getURL(), null, null, null, 0, 0l));
        dups1.add(new ImageData(combo7.getURL(), null, null, null, 0, 0l));
        dups1.add(new ImageData(combo9.getURL(), null, null, null, 0, 0l));
        dups1.add(new ImageData(combo10.getURL(), null, null, null, 0, 0l));
        dups1.add(new ImageData(combo12.getURL(), null, null, null, 0, 0l));
        dups1.add(new ImageData(combo13.getURL(), null, null, null, 0, 0l));

        Set<Set<ImageData>> expectedDups = new HashSet<Set<ImageData>>();
        expectedDups.add(dups1);
        expectedDups.add(dups2);

        Set<Set<ImageData>> dups = doComparison(images);

        assertEquals("There should be 2 sets of images", 2, dups.size());
        assertTrue("There should contain all the duplicate images", dups.containsAll(expectedDups));

    }

    @Test
    public void compareSingleImageComparison() throws Exception
    {
        ClassPathResource combo1 = new ClassPathResource("testdata/images/similar/combo1.jpg");
        ClassPathResource combo2 = new ClassPathResource("testdata/images/similar/combo2.jpg");
        ClassPathResource combo3 = new ClassPathResource("testdata/images/similar/combo3.jpg");

        URL[] images = new URL[]{combo3.getURL(), combo2.getURL()};

        ImageData imageData = new ImageData(combo1.getURL(), null, null, null, 0, 0l);

        List<ImageData> imageDatas = new ArrayList<ImageData>();
        for(long i = 0; i < images.length; i ++)
        {
            imageDatas.add(new ImageData(images[(int)i], null, null, null, 0, 0l));
        }

        ImageComparison imageComparison = new ImageComparison(imageData, imageDatas.toArray(new ImageData[imageDatas.size()]));

        imageComparison.compare();


        Set<ImageData> dups1 = new HashSet<ImageData>();
        dups1.add(new ImageData(combo1.getURL(), null, null, null, 0, 0l));
        dups1.add(new ImageData(combo2.getURL(), null, null, null, 0, 0l));

        Set<Set<ImageData>> expectedDups = new HashSet<Set<ImageData>>();
        expectedDups.add(dups1);

        Set<Set<ImageData>> dups = imageComparison.getDuplicates();

        assertEquals("There should be 1 sets of images", 1, dups.size());
        assertTrue("There should contain all the duplicate images", dups.containsAll(expectedDups));


    }

    public Set<Set<ImageData>> doComparison(URL[] images) throws Exception
    {
        List<ImageData> imageDatas = new ArrayList<ImageData>();
        for(long i = 0; i < images.length; i ++)
        {
            imageDatas.add(new ImageData(images[(int)i], null, null, null, 0, 0l));
        }

        ImageComparison imageComparison = new ImageComparison(imageDatas.toArray(new ImageData[imageDatas.size()]));
        imageComparison.compare();
        Set<ImageComparisonResult> compared = imageComparison.getCompared();

        for(ImageComparisonResult imageComparisonResult:compared)
        {
            log.debug(imageComparisonResult.toString());
        }

        log.debug("Dups:");
        Collection<ImageComparisonResult> duplicates = imageComparison.getDuplicateResults();
        for(ImageComparisonResult duplicate:duplicates)
        {
            log.debug(duplicate.toString());
        }

        log.debug("Duplicates");
        Set<Set<ImageData>> duppedImages = imageComparison.getDuplicates();
        log.debug(duppedImages.toString());
        return duppedImages;
    }


    @Test
    public void testLowResComparisons() throws Exception
    {
        ClassPathResource img1 = new ClassPathResource("testdata/images/similar/low_res_b_1.jpg");
        ClassPathResource img2 = new ClassPathResource("testdata/images/similar/low_res_b_2.jpg");
        ClassPathResource img3 = new ClassPathResource("testdata/images/similar/low_res_b_3.jpg");
        ClassPathResource img4 = new ClassPathResource("testdata/images/similar/low_res_b_4.jpg");
        ClassPathResource img5 = new ClassPathResource("testdata/images/similar/low_res_b_5.jpg");

        URL[] images = new URL[]{img1.getURL(), img2.getURL(), img3.getURL(), img4.getURL(), img5.getURL()};

        Set<ImageData> dups2 = new HashSet<ImageData>();
        dups2.add(new ImageData(img1.getURL(), null, null, null, 0, 0l));
        dups2.add(new ImageData(img3.getURL(), null, null, null, 0, 0l));

        Set<ImageData> dups1 = new HashSet<ImageData>();
        dups1.add(new ImageData(img2.getURL(), null, null, null, 0, 0l));
        dups1.add(new ImageData(img4.getURL(), null, null, null, 0, 0l));

        Set<Set<ImageData>> expectedDups = new HashSet<Set<ImageData>>();
        expectedDups.add(dups1);
        expectedDups.add(dups2);

        Set<Set<ImageData>> dups = doComparison(images);

        assertEquals("There should be 2 sets of images", 2, dups.size());
        assertTrue("There should contain all the duplicate images", dups.containsAll(expectedDups));


    }
}
