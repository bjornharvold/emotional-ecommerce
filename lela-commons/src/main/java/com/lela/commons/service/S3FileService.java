package com.lela.commons.service;

import com.lela.util.UtilityException;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 6/1/12
 * Time: 8:25 AM
 * To change this template use File | Settings | File Templates.
 */
public interface S3FileService {
    void saveFile(String accessKey, String secretKey, String bucketName, String fileName, String folder) throws UtilityException;
}
