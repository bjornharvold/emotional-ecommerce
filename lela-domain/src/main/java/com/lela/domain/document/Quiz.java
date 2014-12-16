/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 6/27/12
 * Time: 6:05 PM
 * Responsibility:
 */
@Document
public class Quiz extends AbstractDocument implements Serializable {
    private static final long serialVersionUID = 1216041925368001415L;

    /**
     * Language
     */
    @NotNull
    @NotEmpty
    private String lng;

    /**
     * Name
     */
    @NotNull
    @NotEmpty
    private String nm;

    /**
     * Url name
     */
    @NotNull
    @NotEmpty
    @Indexed
    private String rlnm;

    /**
     * Seo url name
     */
    @NotNull
    @NotEmpty
    private String srlnm;

    /**
     * Published
     */
    @NotNull
    private Boolean pblshd;

    /**
     * Is this the default quiz on our site
     */
    @NotNull
    private Boolean dflt;

    /**
     * Optional default theme that may be overriden by the host site
     */
    private String thm;

    /**
     * Optional URL to redirect to when the quiz is completed
     */
    private String cmpltrl;

    /**
     * Optional static content displayed when quiz is completed
     */
    private String cmpltcntnt;

    /**
     * Optional static content displayed when returning to a completed quiz
     */
    private String rtrncntnt;

    private List<QuizStep> stps;

    public Quiz() {
    }

    public Quiz(String lng) {
        this.lng = lng;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public List<QuizStep> getStps() {
        return stps;
    }

    public void setStps(List<QuizStep> stps) {
        this.stps = stps;
    }

    public String getSrlnm() {
        return srlnm;
    }

    public void setSrlnm(String srlnm) {
        this.srlnm = srlnm;
    }

    public Boolean getPblshd() {
        return pblshd;
    }

    public void setPblshd(Boolean pblshd) {
        this.pblshd = pblshd;
    }

    public Boolean getDflt() {
        return dflt;
    }

    public void setDflt(Boolean dflt) {
        this.dflt = dflt;
    }

    public String getThm() {
        return thm;
    }

    public void setThm(String thm) {
        this.thm = thm;
    }

    public String getCmpltrl() {
        return cmpltrl;
    }

    public void setCmpltrl(String cmpltrl) {
        this.cmpltrl = cmpltrl;
    }

    public String getCmpltcntnt() {
        return cmpltcntnt;
    }

    public void setCmpltcntnt(String cmpltcntnt) {
        this.cmpltcntnt = cmpltcntnt;
    }

    public String getRtrncntnt() {
        return rtrncntnt;
    }

    public void setRtrncntnt(String rtrncntnt) {
        this.rtrncntnt = rtrncntnt;
    }

    public boolean addQuizStep(QuizStep step) {

        if (this.stps == null) {
            this.stps = new ArrayList<QuizStep>();
        }

        if (StringUtils.isBlank(step.getD())) {
            step.setD(new BigInteger(130, new SecureRandom()).toString(32));
        }

        QuizStep dupe = null;

        for (QuizStep qq : this.stps) {
            if (StringUtils.equals(qq.getD(), step.getD())) {
                dupe = qq;
                break;
            }
        }

        // overwrite original
        if (dupe != null) {
            this.stps.remove(dupe);
        }

        this.stps.add(step);

        return true;
    }

    public boolean addQuizStepEntry(QuizStepEntry question) {
        boolean saved = false;

        if (StringUtils.isBlank(question.getD())) {
            question.setD(new BigInteger(130, new SecureRandom()).toString(32));
        }

        if (this.stps != null && !this.stps.isEmpty()) {
            for (QuizStep step : this.stps) {
                if (StringUtils.equals(step.getD(), question.getQd())) {
                    step.addQuizStepEntry(question);
                    saved = true;
                    break;
                }
            }
        }

        return saved;
    }

    public boolean removeQuizStep(String quizStepId) {
        QuizStep toRemove = null;

        if (this.stps != null && !this.stps.isEmpty()) {
            for (QuizStep qs : this.stps) {
                if (StringUtils.equals(qs.getD(), quizStepId)) {
                    toRemove = qs;
                    break;
                }
            }
        }

        if (toRemove != null) {
            this.stps.remove(toRemove);
        }

        return toRemove != null;
    }

    public boolean removeQuizStepEntry(String quizStepId, String questionId) {
        QuizStepEntry toRemove = null;

        if (this.stps != null && !this.stps.isEmpty()) {
            for (QuizStep qs : this.stps) {
                if (StringUtils.equals(qs.getD(), quizStepId)) {
                    if (qs.getNtrs() != null && !qs.getNtrs().isEmpty()) {
                        for (QuizStepEntry question : qs.getNtrs()) {
                            if (StringUtils.equals(question.getD(), questionId)) {
                                toRemove = question;
                                break;
                            }
                        }

                        if (toRemove != null) {
                            qs.getNtrs().remove(toRemove);
                            break;
                        }
                    }
                }
            }
        }

        return toRemove != null;
    }

    public QuizStep findQuizStepById(String quizStepId) {
        if (this.stps != null && !this.stps.isEmpty()) {
            for (QuizStep step : this.stps) {
                if (StringUtils.equals(step.getD(), quizStepId)) {
                    step.setQrlnm(getRlnm());

                    return step;
                }
            }
        }

        return null;
    }

    public QuizStep findQuizStepByUrlName(String quizStepUrlName) {
        if (this.stps != null && !this.stps.isEmpty()) {
            for (QuizStep step : this.stps) {
                if (StringUtils.equals(step.getRlnm(), quizStepUrlName)) {
                    step.setQrlnm(getRlnm());

                    return step;
                }
            }
        }

        return null;
    }

    public QuizStepEntry findQuizStepEntry(String quizStepId, String questionId) {
        if (this.stps != null && !this.stps.isEmpty()) {
            for (QuizStep step : this.stps) {
                if (StringUtils.equals(step.getD(), quizStepId)) {
                    if (step.getNtrs() != null && !step.getNtrs().isEmpty()) {
                        for (QuizStepEntry question : step.getNtrs()) {
                            if (StringUtils.equals(questionId, question.getD())) {
                                question.setQrlnm(getRlnm());
                                question.setQd(quizStepId);
                                return question;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    public QuizStep findFirstQuizStep() {
        QuizStep result = null;

        if (this.stps != null && !this.stps.isEmpty()) {
            result = this.stps.get(0);
        }

        return result;
    }

    /**
     * Returns the next step after the specified url name or null if last step
     * @param currentQuizStepUrlName currentQuizStepUrlName
     * @return
     */
    public QuizStep findNextQuizStep(String currentQuizStepUrlName) {

        if (this.stps != null && !this.stps.isEmpty()) {
            for (int i = 0; i < this.stps.size(); i++) {
                QuizStep stp = this.stps.get(i);
                int nextStep = i + 1;
                if (StringUtils.equals(stp.getRlnm(), currentQuizStepUrlName) && nextStep < this.stps.size()) {
                    return this.stps.get(nextStep);
                }
            }
        }

        return null;
    }

    public QuizStep findPreviousQuizStep(String currentQuizStepUrlName) {
        if (this.stps != null && !this.stps.isEmpty()) {
            for (int i = 0; i < this.stps.size(); i++) {
                QuizStep stp = this.stps.get(i);
                Integer previousStep = i - 1;
                if (StringUtils.equals(stp.getRlnm(), currentQuizStepUrlName) && previousStep >= 0) {
                    return this.stps.get(previousStep);
                }
            }
        }

        return null;
    }

}
