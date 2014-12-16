/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.LelaException;
import com.lela.commons.comparator.PressImageComparator;
import com.lela.commons.comparator.TestimonialComparator;
import com.lela.commons.comparator.TweetComparator;
import com.lela.commons.repository.PressRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.PressService;
import com.lela.domain.document.Press;
import com.lela.domain.document.PressImage;
import com.lela.domain.document.Testimonial;
import com.lela.domain.document.Tweet;
import com.lela.domain.dto.press.PressImageEntry;
import com.lela.domain.enums.CacheType;
import com.lela.util.utilities.storage.FileStorage;
import com.lela.util.utilities.storage.dto.FileData;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 6/8/12
 * Time: 6:56 PM
 * Responsibility: Handles all interaction with the press object
 */
@Service("pressService")
public class PressServiceImpl extends AbstractCacheableService implements PressService {
    private final PressRepository pressRepository;
    private final FileStorage fileStorage;

    @Autowired
    public PressServiceImpl(CacheService cacheService,
                            PressRepository pressRepository,
                            @Qualifier("pressImageFileStorage") FileStorage fileStorage) {
        super(cacheService);
        this.pressRepository = pressRepository;
        this.fileStorage = fileStorage;
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_PRESS')")
    @Override
    public Page<Press> findPressPages(Integer size, Integer maxResults) {
        return pressRepository.findAll(new PageRequest(size, maxResults));
    }

    @PreAuthorize("hasAnyRole('RIGHT_INSERT_PRESS')")
    @Override
    public Press savePress(Press press) {
        return pressRepository.save(press);
    }

    /**
     * Adds a press image to the press object
     *
     * @param pressImage pressImage
     */
    @PreAuthorize("hasAnyRole('RIGHT_INSERT_PRESS')")
    @Override
    public void savePressImage(PressImageEntry pressImage) {
        Press press = findWholePressByUrlName(pressImage.getRlnm());

        if (press != null) {

            if (StringUtils.isBlank(pressImage.getMgid())) {
                pressImage.setMgid(RandomStringUtils.randomAlphabetic(32));
            }

            if (pressImage.getDt() == null) {
                pressImage.setDt(new Date());
            }

            try {
                // if there is a multipart file on the blog it should get uploaded to S3
                if (pressImage.getMultipartFile() != null && !pressImage.getMultipartFile().isEmpty()) {
                    MultipartFile file = pressImage.getMultipartFile();
                    String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                    FileData data = new FileData(pressImage.getMgid() + extension, file.getBytes(), file.getContentType());
                    String url = fileStorage.storeFile(data);
                    pressImage.setMgrl(url);
                }
            } catch (IOException e) {
                throw new LelaException(e.getMessage(), e);
            }

            press.addPressImage(new PressImage(pressImage));

            savePress(press);
        }

    }

    /**
     * Adds a testimonial to the press object
     *
     * @param testimonial testimonial
     */
    @PreAuthorize("hasAnyRole('RIGHT_INSERT_PRESS')")
    @Override
    public void saveTestimonial(Testimonial testimonial) {
        Press press = findWholePressByUrlName(testimonial.getRlnm());

        if (press != null) {

            if (StringUtils.isBlank(testimonial.getTid())) {
                testimonial.setTid(RandomStringUtils.randomAlphabetic(32));
            }

            if (testimonial.getCdt() == null) {
                testimonial.setCdt(new Date());
            }

            press.addTestimonial(testimonial);

            savePress(press);
        }

    }

    /**
     * Adds a tweet to the press object
     *
     * @param tweet tweet
     */
    @PreAuthorize("hasAnyRole('RIGHT_INSERT_PRESS')")
    @Override
    public void saveTweet(Tweet tweet) {
        Press press = findWholePressByUrlName(tweet.getRlnm());

        if (press != null) {

            if (StringUtils.isBlank(tweet.getTid())) {
                tweet.setTid(RandomStringUtils.randomAlphabetic(32));
            }

            if (tweet.getCdt() == null) {
                tweet.setCdt(new Date());
            }

            press.addTweet(tweet);

            savePress(press);
        }

    }

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_PRESS')")
    @Override
    public void removeTweet(String urlName, String id) {
        Press press = findWholePressByUrlName(urlName);
        Tweet toRemove = null;

        if (press != null) {
            if (press.getTwts() != null && !press.getTwts().isEmpty()) {
                for (Tweet tweet : press.getTwts()) {
                    if (StringUtils.equals(tweet.getTid(), id)) {
                        toRemove = tweet;
                        break;
                    }
                }
            }
        }

        if (toRemove != null) {
            press.getTwts().remove(toRemove);

            savePress(press);
        }
    }

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_PRESS')")
    @Override
    public void removeTestimonial(String urlName, String id) {
        Press press = findWholePressByUrlName(urlName);
        Testimonial toRemove = null;

        if (press != null) {
            if (press.getTstmnls() != null && !press.getTstmnls().isEmpty()) {
                for (Testimonial testimonial : press.getTstmnls()) {
                    if (StringUtils.equals(testimonial.getTid(), id)) {
                        toRemove = testimonial;
                        break;
                    }
                }
            }
        }

        if (toRemove != null) {
            press.getTstmnls().remove(toRemove);

            savePress(press);
        }
    }

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_PRESS')")
    @Override
    public void removePressImage(String urlName, String id) {
        Press press = findWholePressByUrlName(urlName);
        PressImage toRemove = null;

        if (press != null) {
            if (press.getMgs() != null && !press.getMgs().isEmpty()) {
                for (PressImage pi : press.getMgs()) {
                    if (StringUtils.equals(pi.getMgid(), id)) {
                        toRemove = pi;
                        break;
                    }
                }
            }
        }

        if (toRemove != null) {
            press.getMgs().remove(toRemove);

            savePress(press);
        }

    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_PRESS')")
    @Override
    public List<Press> findAllPressUrlNames() {
        return pressRepository.findAllPressUrlNames();
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_PRESS')")
    @Override
    public PressImageEntry findPressImageEntry(String urlName, String id) {
        // we're instantiating a new one here just in case we can't find the existing one
        PressImageEntry result = new PressImageEntry(urlName);

        if (StringUtils.isNotBlank(id)) {
            Press press = findWholePressByUrlName(urlName);

            if (press != null && press.getMgs() != null && !press.getMgs().isEmpty()) {
                for (PressImage pi : press.getMgs()) {
                    if (StringUtils.equals(pi.getMgid(), id)) {
                        return new PressImageEntry(urlName, pi);
                    }
                }
            }
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_PRESS')")
    @Override
    public Testimonial findTestimonial(String urlName, String id) {
        // we're instantiating a new one here just in case we can't find the existing one
        Testimonial result = new Testimonial(urlName);

        if (StringUtils.isNotBlank(id)) {
            Press press = findWholePressByUrlName(urlName);

            if (press != null && press.getTstmnls() != null && !press.getTstmnls().isEmpty()) {
                for (Testimonial testimonial : press.getTstmnls()) {
                    if (StringUtils.equals(testimonial.getTid(), id)) {
                        testimonial.setRlnm(urlName);
                        return testimonial;
                    }
                }
            }
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_PRESS')")
    @Override
    public Tweet findTweet(String urlName, String id) {
        // we're instantiating a new one here just in case we can't find the existing one
        Tweet result = new Tweet(urlName);

        if (StringUtils.isNotBlank(id)) {
            Press press = findWholePressByUrlName(urlName);

            if (press != null && press.getTwts() != null && !press.getTwts().isEmpty()) {
                for (Tweet tweet : press.getTwts()) {
                    if (StringUtils.equals(tweet.getTid(), id)) {
                        tweet.setRlnm(urlName);
                        return tweet;
                    }
                }
            }
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_PRESS')")
    @Override
    public void removePress(String urlName) {
        List<String> fields = new ArrayList<String>(2);
        fields.add("id");
        fields.add("rlnm");
        Press press = pressRepository.findByUrlName(urlName, fields);

        if (press != null) {
            pressRepository.delete(press.getId());

            removeFromCache(CacheType.PRESS, urlName);
        }
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_PRESS')")
    @Override
    public Press findWholePressByUrlName(String urlName) {
        Press result = pressRepository.findByUrlName(urlName, null);

        sortPressLists(result);

        return result;
    }

    /**
     * Returns a Press objects and sorts all nested collections by their "rdr" attribute
     *
     * @param urlName urlName
     * @return Press object
     */
    @PreAuthorize("hasAnyRole('RIGHT_READ_PRESS')")
    @Override
    public Press findPressByUrlName(String urlName, List<String> fields) {
        Press result = pressRepository.findByUrlName(urlName, fields);

        sortPressLists(result);

        return result;
    }

    private void sortPressLists(Press press) {
        if (press != null) {
            if (press.getMgs() != null && !press.getMgs().isEmpty()) {
                // now we sort the filtered list
                Collections.sort(press.getMgs(), new PressImageComparator());
            }
            if (press.getTstmnls() != null && !press.getTstmnls().isEmpty()) {
                // now we sort the filtered list
                Collections.sort(press.getTstmnls(), new TestimonialComparator());
            }
            if (press.getTwts() != null && !press.getTwts().isEmpty()) {
                // now we sort the filtered list
                Collections.sort(press.getTwts(), new TweetComparator());
            }
        }
    }

    /**
     * Returns a Press objects and sorts all nested collections by their "rdr" attribute
     * This is the consumer facing method
     *
     * @param urlName urlName
     * @return Press object
     */
    @Override
    public Press findPressByUrlName(String urlName) {
        Press result = null;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.PRESS_CACHE, urlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof Press) {
            result = (Press) wrapper.get();
        } else {
            result = pressRepository.findByUrlName(urlName, null);

            if (result != null) {
                // put the fully organized object in the cache
                putInCache(ApplicationConstants.PRESS_CACHE, urlName, result);
            }
        }

        if (result != null) {
            // make a deep copy of the results and filter out items that should not been seen yet
            result = new Press(result);

            Date now = new Date();
            // now we sort and filter out items that are not yet to be published
            // sort nested collections first
            if (result.getMgs() != null && !result.getMgs().isEmpty()) {
                // first we filter
                List<PressImage> toRemove = null;

                for (PressImage pi : result.getMgs()) {
                    // if we haven't hit the publish date yet, we remove
                    if (pi.getPblshdt().after(now)) {
                        if (toRemove == null) {
                            toRemove = new ArrayList<PressImage>();
                        }
                        toRemove.add(pi);
                    }
                }

                if (toRemove != null) {
                    result.getMgs().removeAll(toRemove);
                }
            }
            if (result.getTstmnls() != null && !result.getTstmnls().isEmpty()) {
                // first we filter
                List<Testimonial> toRemove = null;

                for (Testimonial pi : result.getTstmnls()) {
                    // if we haven't hit the publish date yet, we remove
                    if (pi.getPblshdt().after(now)) {
                        if (toRemove == null) {
                            toRemove = new ArrayList<Testimonial>();
                        }
                        toRemove.add(pi);
                    }
                }

                if (toRemove != null) {
                    result.getTstmnls().removeAll(toRemove);
                }
            }
            if (result.getTwts() != null && !result.getTwts().isEmpty()) {
                // first we filter
                List<Tweet> toRemove = null;

                for (Tweet pi : result.getTwts()) {
                    // if we haven't hit the publish date yet, we remove
                    if (pi.getPblshdt().after(now)) {
                        if (toRemove == null) {
                            toRemove = new ArrayList<Tweet>();
                        }
                        toRemove.add(pi);
                    }
                }

                if (toRemove != null) {
                    result.getTwts().removeAll(toRemove);
                }
            }

            // finally we sort
            sortPressLists(result);
        }

        return result;
    }
}
