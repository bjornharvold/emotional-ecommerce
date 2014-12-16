/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.quiz;

import com.lela.domain.document.Answer;
import com.lela.domain.document.Category;
import com.lela.domain.document.Item;
import com.lela.domain.enums.PublishStatus;
import com.lela.util.utilities.string.StringUtility;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Bjorn Harvold
 * Date: 3/24/12
 * Time: 12:18 PM
 * Responsibility:
 */
public class AnswerEntry implements Serializable {
    private static final long serialVersionUID = -213628756132253431L;

    /**
     * Image
     */
    private String mg;

    /**
     * This is a map of all answer values the user selected. It supports multiple answers.
     * The key is the emotional key e.g. A, B, C and the value is value from 0 - 10
     */
    private Map<String, Integer> mtvtrs;

    /**
     * Name
     */
    private String nm;

    /**
     * unique id
     */
    private String d;

    /**
     * Order
     */
    @NotNull
    @Min(1)
    private Integer rdr;

    /**
     * question url name
     */
    @NotNull
    @NotEmpty
    private String qrlnm;

    private MultipartFile multipartFile;

    public AnswerEntry(Answer answer) {
        this.qrlnm = answer.getQrlnm();
        this.nm = answer.getNm();
        this.d = answer.getD();
        this.mtvtrs = new LinkedHashMap<String, Integer>();

        // TODO this will need to be refactored if we run out of alphabetical motivator keys A-Z
        if (answer.getMtvtrs() != null && !answer.getMtvtrs().isEmpty()) {
            for (String letter : StringUtility.getAlphabet()) {
                String uc = letter.toUpperCase();
                if (answer.getMtvtrs().containsKey(uc)) {
                    mtvtrs.put(uc, answer.getMtvtrs().get(uc));
                } else {
                    mtvtrs.put(uc, 0);
                }
            }
        } else {
            for (String letter : StringUtility.getAlphabet()) {
                mtvtrs.put(letter.toUpperCase(), 0);
            }
        }

        this.mg = answer.getMg();
        if (StringUtils.isNotBlank(answer.getKy())) {
            this.rdr = Integer.parseInt(answer.getKy());
        }
    }

    public AnswerEntry(String qrlnm) {
        this.qrlnm = qrlnm;
        this.mtvtrs = new LinkedHashMap<String, Integer>();

        for (String letter : StringUtility.getAlphabet()) {
            mtvtrs.put(letter.toUpperCase(), 0);
        }
    }

    public AnswerEntry() {
    }

    public String getMg() {
        return mg;
    }

    public void setMg(String mg) {
        this.mg = mg;
    }

    public Map<String, Integer> getMtvtrs() {
        return mtvtrs;
    }

    public void setMtvtrs(Map<String, Integer> mtvtrs) {
        this.mtvtrs = mtvtrs;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getQrlnm() {
        return qrlnm;
    }

    public void setQrlnm(String qrlnm) {
        this.qrlnm = qrlnm;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public Integer getRdr() {
        return rdr;
    }

    public void setRdr(Integer rdr) {
        this.rdr = rdr;
    }
}
