/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.multipart.MultipartFile;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.LelaException;
import com.lela.commons.repository.StaticContentRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.StaticContentService;
import com.lela.domain.document.QStaticContent;
import com.lela.domain.document.StaticContent;
import com.lela.domain.dto.staticcontent.StaticContentEntry;
import com.lela.domain.enums.CacheType;
import com.lela.util.utilities.number.NumberUtils;
import com.lela.util.utilities.storage.FileStorage;
import com.lela.util.utilities.storage.dto.FileData;

/**
 * Created by Bjorn Harvold
 * Date: 6/4/12
 * Time: 7:23 PM
 * Responsibility:
 */
@Service("staticContentService")
public class StaticContentServiceImpl extends AbstractCacheableService implements StaticContentService {
	private static Logger log = LoggerFactory.getLogger(StaticContentServiceImpl.class);

    private static final String OBJECT_ACCESSOR = "o";
    private final StaticContentRepository staticContentRepository;
    private VelocityEngine velocityEngine;
    private final FileStorage fileStorage;
    private static final String ENCODING = "UTF-8";
    
    @Autowired
    public StaticContentServiceImpl(CacheService cacheService,
    								@Qualifier("staticContentImageFileStorage") FileStorage fileStorage,
                                    StaticContentRepository staticContentRepository) {
        super(cacheService);
        this.staticContentRepository = staticContentRepository;
        this.fileStorage = fileStorage;
    }

    @PostConstruct 
    public void init() {
        Properties props = new Properties();
        props.put("resource.loader", "string");
        props.put("string.resource.loader.description", "Velocity StringResourceLoader");
        props.put("string.resource.loader.class", "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
        props.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");

        try {
        // configure a string-based resource loader
        velocityEngine = new VelocityEngine(props);
        velocityEngine.init();
        } catch (Exception ex) {
            if (log.isErrorEnabled()) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

    @Override
    public Page<StaticContent> findStaticContents(Integer page, Integer size) {
        return staticContentRepository.findAll(new PageRequest(page, size));
    }

    @Override
    public List<StaticContent> findStaticContents(Integer page, Integer size, List<String> fields) {
        return staticContentRepository.findAll(page, size, fields);
    }

    @Override
    public List<StaticContent> findStaticContents(List<String> fields) {
        return staticContentRepository.findAll(fields);
    }

    @Override
    public Long findStaticContentCount() {
        return staticContentRepository.count();
    }

    @Override
    public StaticContent findStaticContentByUrlName(String urlName) {
        return retrieveStaticContent(urlName);
    }



    /**
     * Only call this method if your static content is intended to be used as
     * a velocity template.
     * The template creator needs to use ${o} to access the object being passed as the 2nd param.
     *
     * @param urlName urlName
     * @param obj Object where we get dynamic properties from
     * @return
     */
    @Override
    public StaticContent findStaticContentByUrlName(String urlName, Object obj) {
        StaticContent sc = retrieveStaticContent(urlName);

    	if (sc != null && sc.isVlctytmplt()){
            // load up the static content into velocity's resource loader
            StringResourceRepository repo = StringResourceLoader.getRepository();
            repo.putStringResource(urlName, sc.getBdy());

            Map<String, Object> model = new HashMap<String, Object>(1);
            model.put(OBJECT_ACCESSOR, obj);
            String velocifiedString = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, urlName, ENCODING, model);
            sc = new StaticContent(sc, velocifiedString);
            log.debug("Resolved a velocified string representing the static content with url" + urlName);
    	} 

        return sc;
    }

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST', 'RIGHT_INSERT_STATIC_CONTENT_AS_ADMIN')")
    @Override
    public StaticContent saveStaticContent(StaticContent content) {
        content = staticContentRepository.save(content);

        // Remove from cache
        removeFromCache(CacheType.STATIC_CONTENT, content.getRlnm());

        return content;
    }
    
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST', 'RIGHT_INSERT_STATIC_CONTENT_AS_ADMIN')")
    @Override
    public StaticContentEntry saveStaticContent(StaticContentEntry staticContentEntry) { 
    	
    	//First save multipart image if theres one
    	if (staticContentEntry.getMultipartFile() != null && !staticContentEntry.getMultipartFile().isEmpty()){
            try {
                    MultipartFile file = staticContentEntry.getMultipartFile();
                    FileData data = new FileData(staticContentEntry.getStaticContent().getRlnm() + "-" + file.getOriginalFilename(), file.getBytes(), file.getContentType());
                    String url = fileStorage.storeFile(data);
                    staticContentEntry.getStaticContent().setLmgrl(url);
            } catch (IOException e) { 
                throw new LelaException(e.getMessage(), e);
            }

    	}
    	this.saveStaticContent(staticContentEntry.getStaticContent());
    	return staticContentEntry;
    }

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST', 'RIGHT_DELETE_STATIC_CONTENT_AS_ADMIN')")
    @Override
    public StaticContent removeStaticContent(String rlnm) {
        StaticContent content = staticContentRepository.findByUrlName(rlnm);

        if (content != null) {
            staticContentRepository.delete(content);

            // Remove from cache
            removeFromCache(CacheType.STATIC_CONTENT, content.getRlnm());
        }

        return content;
    }

    /**
     * TODO an improvement to this would be to only return those fields that will be used
     * @param chunk chunk
     * @return
     */
    @Override
    public List<StaticContent> paginateThroughAllStaticContent(Integer chunk, List<String> fields) {
        List<StaticContent> result = null;
        Long count;
        Integer iterations;
        count = findStaticContentCount();

        if (count != null && count > 0) {
            result = new ArrayList<StaticContent>(count.intValue());
            iterations = NumberUtils.calculateIterations(count, chunk.longValue());

            // load up items in a paginated fashion from mongo
            for (int i = 0; i < iterations; i++) {
                result.addAll(findStaticContents(i, chunk, fields));
            }
        }

        return result;
    }
    
	private StaticContent retrieveStaticContent(String urlName) {
		StaticContent result;
		Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.STATIC_CONTENT_CACHE, urlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof StaticContent) {
            result = (StaticContent) wrapper.get();
            log.debug(String.format("Retrieved static content for url %s from cache", urlName));
        } else {
        	//result = null;
            result = staticContentRepository.findOne(QStaticContent.staticContent.rlnm.eq(urlName));
            log.debug(String.format("Retrieved static content for url %s from db", urlName));
            if (result != null) {
                putInCache(ApplicationConstants.STATIC_CONTENT_CACHE, urlName, result);
            }
        }
		return result;
	}
}
