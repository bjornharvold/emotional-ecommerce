package com.lela.domain.document;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 5/14/12
 * Time: 1:20 PM
 * To change this template use File | Settings | File Templates.
 */

@Document
public class UserVerification extends AbstractDocument implements Serializable {

    /**
     * Field description
     */
    private static final long serialVersionUID = -5850171871971023139L;

    /**
     * USER ID that followed the redirect
     */
    private ObjectId srid;

    /**
     * Random GUID embeded in url
     */
    private String tkn;

    /**
     * Email being verified
     */
    private String ml;

    /**
     * Date the user verified their email
     */
    private Date vrfdt;

    public UserVerification(ObjectId srid, String ml)
    {
        this.srid = srid;
        this.ml = ml;
        this.generateTkn();
    }

    public ObjectId getSrid() {
        return srid;
    }

    public void setSrid(ObjectId srid) {
        this.srid = srid;
    }

    public String getTkn()
    {
        return this.tkn;
    }

    public Date getVrfdt() {
        return vrfdt;
    }

    public void setVrfdt(Date vrfdt) {
        this.vrfdt = vrfdt;
    }

    public String getMl() {
        return ml;
    }

    private void generateTkn()
    {
        this.tkn = UUID.randomUUID().toString();
    }

    public boolean verify(String ml)
    {
        return this.ml.equals(ml);
    }
}
