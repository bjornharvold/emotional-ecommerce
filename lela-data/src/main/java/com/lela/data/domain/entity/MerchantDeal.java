package com.lela.data.domain.entity;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "merchant_deal")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "MerchantDealId", columnDefinition = "int(11)"))
public class MerchantDeal extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "MerchantId")
    @ForeignKey(name = "FK_merchant_deal_merchant")
    private Merchant merchant;

    @Column(name = "DealName", length = 255)
    private String dealName;

    @Column(name = "Url", length = 255)
    private String url;

    @Column(name = "StartOn", length = 50)
    private String startOn;

    @Column(name = "EndOn", length = 50)
    private String endOn;

    @Column(name = "DealSpecifics", length = 20)
    private String dealSpecifics;

    @Column(name = "ObjectId", length = 24)
    private String ObjectId;
}
