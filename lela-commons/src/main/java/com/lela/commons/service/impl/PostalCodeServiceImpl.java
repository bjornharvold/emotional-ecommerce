/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.commons.repository.PostalCodeRepository;
import com.lela.commons.service.PostalCodeService;
import com.lela.domain.document.PostalCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 5/10/12
 * Time: 4:28 PM
 * Responsibility:
 */
@Service("postalCodeService")
public class PostalCodeServiceImpl implements PostalCodeService {


    /** Field description */
    private final PostalCodeRepository postalCodeRepository;

    @Autowired
    public PostalCodeServiceImpl(PostalCodeRepository postalCodeRepository) {
        this.postalCodeRepository = postalCodeRepository;
    }

    /**
     * Method description
     *
     *
     * @param code code
     *
     * @return Return value
     */
    @Override
    public PostalCode findPostalCodeByCode(String code) {
        return postalCodeRepository.findByPostalCode(code);
    }

    /**
     * @param city city
     * @param stateName full state name
     * @return return value
     */
    @Override
    public PostalCode findPostalCodeByCityAndStateName(String city, String stateName) {
        if (StringUtils.isNotBlank(city)) {
            city = city.toUpperCase();
        }

        return postalCodeRepository.findByCityAndStateName(city, stateName);
    }

    /**
     * Method description
     *
     *
     * @param code postalCode
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public PostalCode removePostalCode(String code) {
        PostalCode postalCode = postalCodeRepository.findByPostalCode(code);
        if (postalCode != null) {
            postalCodeRepository.delete(postalCode);
        }

        return postalCode;
    }

    /**
     * Method description
     *
     *
     * @param postalCode postalCode
     *
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public PostalCode savePostalCode(PostalCode postalCode) {
        return postalCodeRepository.save(postalCode);
    }

    /**
     * Method description
     *
     *
     * @param list list
     *
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public List<PostalCode> savePostalCodes(List<PostalCode> list) {
        return (List<PostalCode>) postalCodeRepository.save(list);
    }
}
