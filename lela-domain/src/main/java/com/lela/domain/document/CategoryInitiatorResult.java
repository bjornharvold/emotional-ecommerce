package com.lela.domain.document;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/2/12
 * Time: 1:19 PM
 * To change this template use File | Settings | File Templates.
 */
@Document
public class CategoryInitiatorResult extends AbstractDocument{

    /* CategoryInitiatorQuery Id */
    @Indexed
    private ObjectId qryid;

    /* Count */
    private Integer cnt;

    private Integer rtrvd;

    /* Url for excel output (usually on S3) */
    private String rl;

    private Date cmpltdt;

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public ObjectId getQryid() {
        return qryid;
    }

    public void setQryid(ObjectId qryid) {
        this.qryid = qryid;
    }

    public String getRl() {
        return rl;
    }

    public void setRl(String rl) {
        this.rl = rl;
    }

    public Date getCmpltdt() {
        return cmpltdt;
    }

    public void setCmpltdt(Date cmpltdt) {
        this.cmpltdt = cmpltdt;
    }

    public Integer getRtrvd() {
        return rtrvd;
    }

    public void setRtrvd(Integer rtrvd) {
        this.rtrvd = rtrvd;
    }

}
