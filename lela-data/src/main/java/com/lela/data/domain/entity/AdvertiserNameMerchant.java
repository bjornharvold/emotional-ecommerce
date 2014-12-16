package com.lela.data.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table = "advertiser_name_merchant")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "AdvertiserNameMerchantId", columnDefinition = "int(11)"))
public class AdvertiserNameMerchant extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "MerchantId")
    @ForeignKey(name = "FK_advertiser_name_merchant_merchant")
    private Merchant merchant;

    @Column(name = "AdvertiserName")
    private String advertiserName;
}
