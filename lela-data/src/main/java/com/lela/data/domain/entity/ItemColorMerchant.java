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
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "item_color_merchant")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ItemColorMerchantId", columnDefinition = "int(11)"))
public class ItemColorMerchant extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name="ItemColorId", nullable=false)
    @ForeignKey(name = "FK_item_color_merchant_item_color")
    private ItemColor itemColor;

    @ManyToOne
    @JoinColumn(name = "MerchantId", nullable = false)
    @ForeignKey(name = "FK_item_color_merchant_merchant")
    private Merchant merchant;

    @Column(name = "IsAvailable", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    @NotNull
    private Boolean available;

    @Override
    public void prePersist() {
        super.prePersist();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void preUpdate() {
        super.preUpdate();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
