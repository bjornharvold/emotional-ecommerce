/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.test.utilities;

import com.lela.commons.service.S3FileService;
import com.lela.commons.service.impl.S3FileServiceImpl;
import com.lela.util.UtilityException;
import com.lela.util.utilities.storage.s3.S3FileStorage;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Bjorn Harvold
 * Date: 3/27/11
 * Time: 8:32 PM
 * Responsibility:
 */
public class S3FileServiceFunctionalTest {
    private final static Logger log = LoggerFactory.getLogger(S3FileServiceFunctionalTest.class);

    private final static ClassPathResource affiliateReport = new ClassPathResource("testdata/affiliatereporting/AffiliateNetwork_20120503.txt");

    private final static String accessKey = "AKIAJL3BN3SG5RPJOCCQ";
    private final static String secretKey = "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";
    private final static String bucketName = "lela-feeds-test";
    private final static String folder = "affiliatereporting";

    @Test
    public void testS3FileServiceWithNoErrors() {
        log.info("Testing S3FileService saving a file");

        // instantiate utility service
        S3FileService service = new S3FileServiceImpl();

        try {
            S3FileStorage s3FileStorage = new S3FileStorage(accessKey, secretKey, bucketName, false);

            File file = affiliateReport.getFile();

            String fileName = folder + "/" + file.getName();

            //remove the file from s3 if it exists
            s3FileStorage.removeObjectFromBucket(bucketName, fileName);

            //make sure it's not on s3 anymore
            List<String> objects = s3FileStorage.listAllObjectsInABucket(bucketName);
            assertFalse("File already exists in S3", objects.contains(fileName));

            //test that the service saves the file
            service.saveFile(accessKey, secretKey, bucketName, file.getAbsolutePath(), "affiliatereporting");

            //check that the file is there
            objects = s3FileStorage.listAllObjectsInABucket(bucketName);
            assertTrue(objects.contains(fileName));
        } catch (UtilityException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

        log.info("Testing S3FileService complete!");
    }

}
