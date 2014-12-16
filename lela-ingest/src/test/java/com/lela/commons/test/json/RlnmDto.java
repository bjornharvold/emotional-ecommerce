package com.lela.commons.test.json;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * User: Chris Tallent
 * Date: 5/23/12
 * Time: 10:22 AM
 */
public class RlnmDto {

    @Id
    private ObjectId id;
    private String rlnm;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }
}
