package com.lela.data.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "affiliate_merchant")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "AffiliateMerchantId", columnDefinition = "int(11)"))
public class AffiliateMerchant extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "AffiliateId", nullable = false, referencedColumnName = "AffiliateId")
    @ForeignKey(name = "FK_affiliate_merchant_affiliate")
    @NotNull
    private Affiliate affiliate;

    @ManyToOne
    @JoinColumn(name = "MerchantId", nullable = false, referencedColumnName = "MerchantId")
    @ForeignKey(name = "FK_affiliate_merchant_merchant")
    @NotNull
    private Merchant merchant;
}
