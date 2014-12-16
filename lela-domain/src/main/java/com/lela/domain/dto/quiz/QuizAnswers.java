/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto.quiz;

import com.lela.domain.dto.AbstractJSONPayload;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/17/11
 * Time: 12:52 PM
 * Responsibility:
 */
public final class QuizAnswers extends AbstractJSONPayload implements Serializable {
    private static final long serialVersionUID = -3158378934672931681L;

    @NotNull
    @NotEmpty
    private String affiliateId;

    @NotNull
    @NotEmpty
    private String applicationId;

    private String email;

    @NotNull
    @NotEmpty
    private String quizUrlName;

    private List<QuizAnswer> list;

    private String campaignId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<QuizAnswer> getList() {
        return list;
    }

    public void setList(List<QuizAnswer> list) {
        this.list = list;
    }

    public String getAffiliateId() {
        return affiliateId;
    }

    public void setAffiliateId(String affiliateId) {
        this.affiliateId = affiliateId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getQuizUrlName() {
        return quizUrlName;
    }

    public void setQuizUrlName(String quizUrlName) {
        this.quizUrlName = quizUrlName;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }
}
