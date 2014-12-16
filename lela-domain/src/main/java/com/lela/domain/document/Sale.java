package com.lela.domain.document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/22/12
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class Sale implements Serializable {

    /*
   Sale Amount
    */
    Double slmnt;

    /*
    Commission Amount
     */
    Double cmmssnmnt;

    /*
    Process Date
     */
    Date prcssdt;

    /*
    Order ID
     */
    String rdrd;

    /*
    Product ID
     */
    String prdctd;

    /*
    Quantity
     */
    int qntty;

    /*
    Product catagory description
     */
    String prdctCtgry;

    /*
    Product name
     */
    String prdctNm;

    /*
   Network name
    */
    String ntwrk;

    /*
    Advertiser Name
     */
    String advrtsrNm;


    public Double getSlmnt() {
        return slmnt;
    }

    public void setSlmnt(Double slmnt) {
        this.slmnt = slmnt;
    }

    public Double getCmmssnmnt() {
        return cmmssnmnt;
    }

    public void setCmmssnmnt(Double cmmssnmnt) {
        this.cmmssnmnt = cmmssnmnt;
    }

    public Date getPrcssdt() {
        return prcssdt;
    }

    public void setPrcssdt(Date prcssdt) {
        this.prcssdt = prcssdt;
    }

    public String getRdrd() {
        return rdrd;
    }

    public void setRdrd(String rdrd) {
        this.rdrd = rdrd;
    }

    public String getPrdctd() {
        return prdctd;
    }

    public void setPrdctd(String prdctd) {
        this.prdctd = prdctd;
    }

    public int getQntty() {
        return qntty;
    }

    public void setQntty(int qntty) {
        this.qntty = qntty;
    }

    public String getPrdctCtgry() {
        return prdctCtgry;
    }

    public void setPrdctCtgry(String prdctCtgry) {
        this.prdctCtgry = prdctCtgry;
    }

    public String getPrdctNm() {
        return prdctNm;
    }

    public void setPrdctNm(String prdctNm) {
        this.prdctNm = prdctNm;
    }

    public String getNtwrk() {
        return ntwrk;
    }

    public void setNtwrk(String ntwrk) {
        this.ntwrk = ntwrk;
    }

    public String getAdvrtsrNm() {
        return advrtsrNm;
    }

    public void setAdvrtsrNm(String advrtsrNm) {
        this.advrtsrNm = advrtsrNm;
    }
}
