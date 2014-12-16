/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.domain.enums.QuestionType;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 7/6/11
 * Time: 12:11 PM
 * Responsibility:
 */
@Document
public class Question extends AbstractDocument implements Serializable {

    /**
     * Field description
     */
    private static final long serialVersionUID = 415134886385841563L;

    //~--- fields -------------------------------------------------------------

    /**
     * Answers
     */
    private List<Answer> nswrs = new ArrayList<Answer>();

    /**
     * Question group
     */
    private Integer grp;

    /** Locale */
    /**
     * TODO remove in favor of having the lng property on Quiz
     */
    private String lcl;

    /**
     * Name
     */
    @NotNull
    private String nm;

    /**
     * Field description
     */
    @Indexed(unique = true)
    private String rlnm;

    /**
     * Field description
     */
    @NotNull
    private QuestionType tp;

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     * @return Return value
     */
    public Integer getGrp() {
        return grp;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getLcl() {
        return lcl;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getNm() {
        return nm;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public List<Answer> getNswrs() {
        return nswrs;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getRlnm() {
        return rlnm;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public QuestionType getTp() {
        return tp;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     * @param grp grp
     */
    public void setGrp(Integer grp) {
        this.grp = grp;
    }

    /**
     * Method description
     *
     * @param lcl lcl
     */
    public void setLcl(String lcl) {
        this.lcl = lcl;
    }

    /**
     * Method description
     *
     * @param nm nm
     */
    public void setNm(String nm) {
        this.nm = nm;
    }

    /**
     * Method description
     *
     * @param nswrs nswrs
     */
    public void setNswrs(List<Answer> nswrs) {
        this.nswrs = nswrs;
    }

    /**
     * Method description
     *
     * @param rlnm rlnm
     */
    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    /**
     * Method description
     *
     * @param tp type
     */
    public void setTp(QuestionType tp) {
        this.tp = tp;
    }

    /**
     * Used by the jsp to populate a dropdown
     *
     * @return
     */
    public String getNameType() {
        return nm + ", " + rlnm + ", " + tp.name();
    }

    public Answer findAnswer(String answerId) {
        if (this.nswrs != null && !this.nswrs.isEmpty()) {
            for (Answer answer : this.nswrs) {
                if (StringUtils.equals(answer.getD(), answerId)) {
                    answer.setQrlnm(getRlnm());

                    return answer;
                }
            }
        }

        return null;
    }

    public boolean addAnswer(Answer answer) {
        if (this.nswrs == null) {
            this.nswrs = new ArrayList<Answer>();
        }

        if (StringUtils.isBlank(answer.getD())) {
            answer.setD(RandomStringUtils.randomAlphabetic(32));
        }

        Answer dupe = null;

        for (Answer qq : this.nswrs) {
            if (StringUtils.equals(qq.getD(), answer.getD())) {
                dupe = qq;
                break;
            }
        }

        // overwrite original
        if (dupe != null) {
            this.nswrs.remove(dupe);
        }

        this.nswrs.add(answer);

        return true;
    }

    public boolean removeAnswer(String answerId) {
        Answer toRemove = null;

        if (this.nswrs != null && !this.nswrs.isEmpty()) {
            for (Answer qs : this.nswrs) {
                if (StringUtils.equals(qs.getD(), answerId)) {
                    toRemove = qs;
                    break;
                }
            }
        }

        if (toRemove != null) {
            this.nswrs.remove(toRemove);
        }

        return toRemove != null;
    }
}
