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

@AttributeOverride(name = "id", column = @Column(name = "ItemAttributeHistoryId", columnDefinition = "int(11)"))
@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "item_attribute_history")
@RooSerializable
public class ItemAttributeHistory extends AbstractEntity {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "UniqueItemId")
    @ForeignKey(name = "FK_item_attribute_history_item")
    private Item item;

    @NotNull
    @ManyToOne
    @JoinColumn(name="AttributeTypeId")
    @ForeignKey(name="FK_item_attribute_history_attribute_type")
    private AttributeType attributeType;

    @NotNull
    @Column(name="AttributeSequence")
    private Integer attributeSequence;

    @Column(name = "OldAttributeValue", length = 10000)
    private String oldAttributeValue;

    @Column(name = "NewAttributeValue", length = 10000 )
    private String newAttributeValue;

    @Override
    public void prePersist() {
        super.prePersist();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void preUpdate() {
        super.preUpdate();    //To change body of overridden methods use File | Settings | File Templates.
    }

}
