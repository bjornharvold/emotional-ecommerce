/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.RelationType;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 10/5/11
 * Time: 12:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class Relation implements Serializable {

    private static final long serialVersionUID = 1810252171387568951L;

    /** User Id */
    private ObjectId uid;

    /** Relationship Type */
    private RelationType tp;

    /** Celebration Dates */
    private List<CelebrationDate> clbrtndts;

    /** Date acknowledged */
    private Date dt;

    public Relation(ObjectId uid, RelationType tp, List<CelebrationDate> clbrtndts, Date dt) {
        this.uid = uid;
        this.tp = tp;
        this.clbrtndts = clbrtndts;
        this.dt = dt;
    }

    public Relation() {
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public ObjectId getUid() {
        return uid;
    }

    /**
     * Method description
     *
     *
     * @param uid uid
     */
    public void setUid(ObjectId uid) {
        this.uid = uid;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public RelationType getTp() {
        return tp;
    }

    /**
     * Method description
     *
     *
     * @param tp tp
     */
    public void setTp(RelationType tp) {
        this.tp = tp;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Date getDt() {
        return dt;
    }

    /**
     * Method description
     *
     *
     * @param dt dt
     */
    public void setDt(Date dt) {
        this.dt = dt;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public List<CelebrationDate> getClbrtndts() {
        return clbrtndts;
    }

    /**
     * Method description
     *
     *
     * @param clbrtndts clbrtndts
     */
    public void setClbrtndts(List<CelebrationDate> clbrtndts) {
        this.clbrtndts = clbrtndts;
    }
}
