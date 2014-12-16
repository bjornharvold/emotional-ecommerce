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
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "merchant_outofstock_pattern")
@AttributeOverride(name = "id", column = @Column(name = "MerchantOutOfStockPatternId", columnDefinition = "int(11)"))
@RooSerializable
public class MerchantOutOfStockPattern extends AbstractEntity {

    @Column(name="Pattern", length = 512)
    String pattern;

    @ManyToOne
    @JoinColumn(name = "MerchantId", nullable=false)
    @ForeignKey(name = "FK_merchant_outofstock_pattern_merchant")
    @NotNull
    private Merchant merchant;

    @Column(name = "Enabled", columnDefinition = "INT(11) NOT NULL DEFAULT 1")
    @NotNull
    private Boolean enabled;
}
