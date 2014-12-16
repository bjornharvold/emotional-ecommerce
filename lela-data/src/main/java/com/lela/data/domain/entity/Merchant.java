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
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "merchant")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "MerchantId", columnDefinition = "int(11)"))
public class Merchant extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "NetworkId")
    @ForeignKey(name = "FK_merchant_network")
    private Network networkId;

    @Column(name = "MerchantName", length = 255)
    private String merchantName;

    @Column(name = "LogoUrl", length = 255)
    private String logoUrl;

    @Column(name = "Url", length = 255)
    private String url;

    @Column(name = "UrlName", length = 255)
    private String urlName;

    @Column(name = "ImageQuality")
    private Integer imageQuality;

    @Column(name = "MerchantValid", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean merchantValid;

    @Column(name = "MerchantApproved", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean merchantApproved;

    @Column(name = "ObjectId", length = 24)
    private String objectId;

    @Column(name = "IsDirty", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean dirty;

    @Column(name = "AlwaysShowLocal", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    @NotNull
    private Boolean alwaysShowLocal;

    @Column(name = "LelaUrl", length = 255)
    private String lelaUrl;

    @Column(name = "PopshopsId", columnDefinition = "INT(11)")
    private Integer popshopsId;

    @Column(name="ReturnPolicy", length=1024)
    private String returnPolicy;
}
