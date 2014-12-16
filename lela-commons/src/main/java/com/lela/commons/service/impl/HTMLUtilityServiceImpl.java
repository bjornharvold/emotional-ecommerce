/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.commons.service.HTMLUtilityService;
import com.lela.commons.service.ImageUtilityService;
import com.lela.domain.dto.dom.DOMElement;
import com.lela.util.UtilityException;
import com.lela.util.utilities.storage.dto.ImageDigest;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 10/1/12
 * Time: 4:12 PM
 * Responsibility: Utility service for everything dealing with external urls and content
 */
@Service("htmlUtilityService")
public class HTMLUtilityServiceImpl implements HTMLUtilityService {
    private final static Logger log = LoggerFactory.getLogger(HTMLUtilityServiceImpl.class);
    private static final String IMG = "img";
    private static final String HTTP = "http";
    private static final String SRC = "src";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String FILE_NAME = "filename";

    /* defaults to 15 seconds */
    @Value("${html.fetch.timeout.in.millis:15000}")
    private Integer timeoutInMillis;

    private final ImageUtilityService imageUtilityService;

    @Autowired
    public HTMLUtilityServiceImpl(ImageUtilityService imageUtilityService) {
        this.imageUtilityService = imageUtilityService;
    }

    @Override
    public List<DOMElement> fetchImagesFromUrl(String urlS) {
        return fetchImagesFromUrl(urlS, null, null);
    }

    /**
     * Retrieves all IMG elements from the given web page
     *
     * @param urlS      urlS
     * @param minWidth  minWidth
     * @param minHeight minHeight
     * @return List of dom elements
     */
    @Override
    public List<DOMElement> fetchImagesFromUrl(String urlS, Integer minWidth, Integer minHeight) {
        List<DOMElement> result = null;

        try {
            // help form a proper http url
            urlS = determineExternalUrl(urlS);

            List<DOMElement> images = fetchElementsFromUrl(urlS, IMG);

            if (images != null && !images.isEmpty()) {

                for (DOMElement image : images) {
                    boolean imageOk = false;
                    boolean dialOut = true;
                    String srcUrl = image.getAttribute(SRC);
                    // we first need to check for absolute versus relative urls. If they are relative we need to prepend the url
                    if (srcUrl != null) {
                        if (!StringUtils.startsWith(srcUrl, HTTP)) {
                            // append either entire url if the image url is something like: pictures/image.jpg
                            // or just the root url if the image is something like: /pictures/image.jpg
                            srcUrl = determineImageUrl(urlS, srcUrl);

                            image.getAttributes().put(SRC, srcUrl);
                        }
                        if (minWidth != null && minHeight != null) {
                            Integer width = 0;
                            Integer height = 0;
                            // in order for the images to be useful - we need to retrieve width and height
                            // if width and height are already included as attributes - we don't have to dial out
                            if (image.getAttribute(WIDTH) != null && image.getAttribute(HEIGHT) != null) {
                                width = Integer.parseInt(image.getAttribute(WIDTH).replaceAll("[^\\d.]", ""));
                                height = Integer.parseInt(image.getAttribute(HEIGHT).replaceAll("[^\\d.]", ""));
                                dialOut = false;
                            }

                            if (dialOut) {
                                // we're missing required width and height so we have to grab the image to determine size
                                try {
                                    ImageDigest id = imageUtilityService.retrieveExternalImage(srcUrl);
                                    height = id.getOriginalImage().getHeight();
                                    width = id.getOriginalImage().getWidth();
                                    image.getAttributes().put(WIDTH, width.toString());
                                    image.getAttributes().put(HEIGHT, height.toString());
                                } catch (UtilityException e) {
                                    if (log.isErrorEnabled()) {
                                        log.error(e.getMessage(), e);
                                    }
                                }
                            }

                            // ok, we have the definitive width and height
                            // if the image is big enough, we'll include it
                            if (minHeight < height && minWidth < width) {
                                imageOk = true;
                            } else {
                                if (log.isDebugEnabled()) {
                                    log.debug("minWidth : " + minWidth + ", width : " + width + " | minHeight : " + minHeight + ", height : " + height);
                                }
                            }
                        } else {
                            imageOk = true;
                        }

                        // image is ok and we can add it to the result
                        if (imageOk) {
                            if (result == null) {
                                result = new ArrayList<DOMElement>();
                            }
                            result.add(image);
                        }
                    }
                }
            }
        } catch (UtilityException e) {
            log.error(e.getMessage(), e);
        }

        return result;
    }

    /**
     * Retrieves all IMG elements from the given web page but with width constraints
     *
     * @param urlS     urlS
     * @param minWidth minWidth
     * @return List of dom elements
     */
    @Override
    public List<DOMElement> fetchImagesFromUrl(String urlS, Integer minWidth) {
        List<DOMElement> result = null;

        try {
            // help form a proper http url
            urlS = determineExternalUrl(urlS);

            List<DOMElement> images = fetchElementsFromUrl(urlS, IMG);

            if (images != null && !images.isEmpty()) {

                for (DOMElement image : images) {
                    boolean imageOk = false;
                    boolean dialOut = true;

                    // grab the image source attribute
                    String srcUrl = image.getAttribute(SRC);

                    // determine image name
                    image.getAttributes().put(FILE_NAME, determineFilename(srcUrl));

                    // we first need to check for absolute versus relative urls. If they are relative we need to prepend the url
                    if (StringUtils.isNotBlank(srcUrl)) {
                        if (!StringUtils.startsWith(srcUrl, HTTP)) {
                            // append either entire url if the image url is something like: pictures/image.jpg
                            // or just the root url if the image is something like: /pictures/image.jpg
                            srcUrl = determineImageUrl(urlS, srcUrl);

                            image.getAttributes().put(SRC, srcUrl);
                        }
                        if (minWidth != null) {
                            Integer width = 0;
                            // in order for the images to be useful - we need to retrieve width and height
                            // if width and height are already included as attributes - we don't have to dial out
                            if (image.getAttribute(WIDTH) != null) {
                                String attributeWidth = image.getAttribute(WIDTH).replaceAll("px", "");
                                width = Integer.parseInt(attributeWidth);
                                dialOut = false;
                            }

                            if (dialOut) {
                                // we're missing required width and height so we have to grab the image to determine size
                                try {
                                    ImageDigest id = imageUtilityService.retrieveExternalImage(srcUrl);
                                    width = id.getOriginalImage().getWidth();
                                    image.getAttributes().put(WIDTH, width.toString());
                                } catch (UtilityException e) {
                                    if (log.isErrorEnabled()) {
                                        log.error(e.getMessage(), e);
                                    }
                                }
                            }

                            // ok, we have the definitive width and height
                            // if the image is big enough, we'll include it
                            if (minWidth < width) {
                                imageOk = true;
                            } else {
                                if (log.isDebugEnabled()) {
                                    log.debug("minWidth : " + minWidth + ", width : " + width);
                                }
                            }
                        } else {
                            imageOk = true;
                        }

                        // image is ok and we can add it to the result
                        if (imageOk) {
                            if (result == null) {
                                result = new ArrayList<DOMElement>();
                            }
                            result.add(image);
                        }
                    }
                }
            }
        } catch (UtilityException e) {
            if (log.isWarnEnabled()) {
                log.warn("Invalid url: " + urlS);
            }
        }

        return result;
    }

    /**
     * This method will try to retrieve a static filename from the specified url
     * e.g. /blah/blah/blah/somefile.jpg?bjbj=er => somefile.jpg
     *
     * @param srcUrl srcUrl
     * @return Filename
     */
    private String determineFilename(String srcUrl) {
        // remove everything in-front of the file name
        int beg = StringUtils.lastIndexOf(srcUrl, "/");

        // remove everything behind the extension
        int end = StringUtils.indexOf(srcUrl, "?");

        if (beg >= 0) {
            if (end > 0) {
                srcUrl = StringUtils.substring(srcUrl, beg + 1, end);
            } else {
                srcUrl = StringUtils.substring(srcUrl, beg + 1);
            }
        }

        return srcUrl;
    }

    private String determineExternalUrl(String urlS) throws UtilityException {
        if (!StringUtils.startsWith(urlS, HTTP) && StringUtils.contains(urlS, "://")) {
            throw new UtilityException("Invalid protocol on url: " + urlS);
        }
        if (!StringUtils.startsWith(urlS, HTTP)) {
            urlS = HTTP + "://" + urlS;
        }

        return urlS;
    }

    private String determineImageUrl(String urlS, String srcUrl) {
        if (StringUtils.startsWith(srcUrl, "/")) {
            // this is a root image so we only need the server name
            int hostIndex = StringUtils.ordinalIndexOf(urlS, "/", 3);
            if (hostIndex == -1) {
                if (StringUtils.endsWith(urlS, "/")) {
                    srcUrl = urlS + srcUrl;
                } else {
                    srcUrl = urlS + "/" + srcUrl;
                }
            } else {
                String host = StringUtils.substring(urlS, 0, hostIndex);
                srcUrl = host + srcUrl;
            }
        } else {
            // this is a relative image so we need the entire url
            if (StringUtils.endsWith(urlS, "/")) {
                srcUrl = urlS + srcUrl;
            } else {
                srcUrl = urlS + "/" + srcUrl;
            }
        }

        return srcUrl;
    }

    public static void main(String[] args) {
        HTMLUtilityServiceImpl impl = new HTMLUtilityServiceImpl(null);
        String url = "http://www.savorthesuccess.com/blog/angela-jia-kim/woman-of-the-week-michelle-ghilotti-says-follow-your-bliss/";
        String imageUrl = "/images/share/facebook_post.jpg?v=1210";
        System.out.println(impl.determineImageUrl(url, imageUrl));

        System.out.println(impl.determineFilename(imageUrl));

        imageUrl = "/facebook_post.jpg";
        System.out.println(impl.determineFilename(imageUrl));
        imageUrl = "http://www.savorthesuccess.com/blog/angela-jia-kim/woman-of-the-week-michelle-ghilotti-says-follow-your-bliss/test.jpg";
        System.out.println(impl.determineFilename(imageUrl));

        url = "ftp://www.savorthesuccess.com/blog/angela-jia-kim/woman-of-the-week-michelle-ghilotti-says-follow-your-bliss/";
        System.out.println(impl.fetchImagesFromUrl(url, 100));
    }

    private List<DOMElement> fetchElementsFromUrl(String urlS, String element) {
        List<DOMElement> result = null;

        try {

            URL url = new URL(urlS);

            if (StringUtils.equals(url.getProtocol(), HTTP)
                    && StringUtils.isNotBlank(url.getHost())) {
                Document doc = Jsoup.parse(url, timeoutInMillis);
                Elements elements = doc.select(element);

                if (!elements.isEmpty()) {
                    result = new ArrayList<DOMElement>();
                    for (Element e : elements) {
                        DOMElement de = new DOMElement();
                        de.setText(e.text());

                        Attributes attributes = e.attributes();

                        if (attributes != null) {
                            Map<String, String> map = new HashMap<String, String>();
                            for (Attribute attribute : attributes) {
                                map.put(attribute.getKey(), attribute.getValue());
                            }

                            de.setAttributes(map);
                        }

                        result.add(de);
                    }
                }
            } else {
                if (log.isWarnEnabled()) {
                    log.warn("Invalid url: " + urlS);
                }
            }
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("Invalid url: " + urlS);
            }
        }

        return result;
    }
}
