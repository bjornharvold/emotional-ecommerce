package com.lela.domain.dto;

import com.lela.domain.document.Offer;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 12/16/11
 * Time: 8:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class Offers extends AbstractJSONPayload {
    private List<Offer> list;

    public Offers() {
    }

    public Offers(List<Offer> list) {
        this.list = list;
    }

    public List<Offer> getList() {
        return list;
    }

    public void setList(List<Offer> list) {
        this.list = list;
    }
}