/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- non-JDK imports --------------------------------------------------------

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 6/16/11
 * Time: 11:41 AM
 * Responsibility:
 */
@Document
public class Owner extends AbstractDocument implements Serializable {

    /** Field description */
    private static final long serialVersionUID = 5825098488976693647L;

    //~--- fields -------------------------------------------------------------

    /** Attributes */
    private List<Attribute> attrs;

    /** Name */
    private String nm;

    /** URL name */
    @Indexed
    private String rlnm;

    /** SEO URL name */
    private String srlnm;

    /** Motivators */
    private Map<String, Integer> mtvtrs = new ConcurrentHashMap<String, Integer>();

    //~--- get methods --------------------------------------------------------

    /**
     * @return Returns the attribute list in hash form for easier access on the jsp
     */
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<String, Object>();

        if ((attrs != null) && !attrs.isEmpty()) {
            for (Attribute attr : attrs) {
                attributes.put(attr.getKy(), attr.getVl());
            }
        }

        return attributes;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public List<Attribute> getAttrs() {
        return attrs;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getNm() {
        return nm;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getRlnm() {
        return rlnm;
    }

    public String getSrlnm() {
        return srlnm;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param attrs attrs
     */
    public void setAttrs(List<Attribute> attrs) {
        this.attrs = attrs;
    }

    /**
     * Method description
     *
     *
     * @param nm nm
     */
    public void setNm(String nm) {
        this.nm = nm;
    }

    /**
     * Method description
     *
     *
     * @param rlnm rlnm
     */
    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public void setSrlnm(String srlnm) {
        this.srlnm = srlnm;
    }

    public Map<String, Integer> getMtvtrs() {
        return mtvtrs;
    }

    public void setMtvtrs(Map<String, Integer> mtvtrs) {
        this.mtvtrs = mtvtrs;
    }
    
    public Integer getMotivatorValue(String motivator) {
        Integer result = null;

        if (mtvtrs != null && !mtvtrs.isEmpty()) {
            if (mtvtrs.containsKey(motivator)) {
                result = mtvtrs.get(motivator);
            }
        }

        return result;
    }
}
