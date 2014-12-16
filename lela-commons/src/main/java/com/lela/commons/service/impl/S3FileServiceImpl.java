package com.lela.commons.service.impl;

import com.lela.commons.service.S3FileService;
import com.lela.util.UtilityException;
import com.lela.util.utilities.storage.FileStorage;
import com.lela.util.utilities.storage.dto.FileData;
import com.lela.util.utilities.storage.s3.S3FileStorage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 6/1/12
 * Time: 8:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class S3FileServiceImpl implements S3FileService {
    /**
     * Field description
     */
    private final static Logger log = LoggerFactory.getLogger(ImageUtilityServiceImpl.class);


    public S3FileServiceImpl() {
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     * @param accessKey accessKey
     * @param secretKey secretKey
     * @param fileName  fileName
     * @return Return value
     * @throws com.lela.util.UtilityException UtilityException
     */
    @Override
    public void saveFile(String accessKey, String secretKey, String bucketName, String fileName,String folder)
            throws UtilityException {
        if (StringUtils.isBlank(accessKey)) {
            throw new UtilityException("accessKey cannot be null");
        }

        if (StringUtils.isBlank(secretKey)) {
            throw new UtilityException("secretKey cannot be null");
        }

        if (StringUtils.isBlank(bucketName)) {
            throw new UtilityException("bucketName cannot be null");
        }

        if (StringUtils.isBlank(fileName)) {
            throw new UtilityException("fileName cannot be null");
        }

        File file = new File(fileName);

        if (!file.exists()) {
            throw new UtilityException("file does not exist");
        }


        //read in the file
        byte[] fileData = readFile(file);

        // save to file storage
        saveFileToStorage(fileData, accessKey, secretKey, bucketName, file, folder);
    }

    /**
     * Method description
     *
     * @param file file
     * @return byte[] contents of file
     * @throws UtilityException UtilityException
     */
    private byte[] readFile(File file) throws UtilityException {
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException ioe) {
            throw new UtilityException(ioe.getMessage());
        }
    }

    /**
     * Method description
     *
     * @param fileData   byte [] of file data
     * @param accessKey  accessKey
     * @param secretKey  secretKey
     * @param bucketName bucketName
     * @param file       file instance
     * @throws UtilityException UtilityException
     */
    private void saveFileToStorage(byte[] fileData, String accessKey, String secretKey, String bucketName, File file, String folder)
            throws UtilityException {
        // set up a storage location
        FileStorage storage = new S3FileStorage(accessKey, secretKey, bucketName, true);

        String imageUrl = storage.storeFile(new FileData(folder + "/" + file.getName(),
                fileData, URLConnection.guessContentTypeFromName(file.getName())));
    }

    /**
     * Method description
     *
     * @param file file
     * @return Return value
     * @throws IOException IOException
     */
    private String guessContentType(File file) throws IOException {
        return URLConnection.guessContentTypeFromName(file.getName());
    }
}
