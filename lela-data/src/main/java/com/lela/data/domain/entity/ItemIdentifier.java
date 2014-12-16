package com.lela.data.domain.entity;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "item_identifier", uniqueConstraints = {@UniqueConstraint(columnNames={"UniqueItemId", "IdentifierTypeId", "IdentifierValue"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ItemIdentifierId", columnDefinition = "int(11)"))
public class ItemIdentifier extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "UniqueItemId", nullable = false)
    @ForeignKey(name = "FK_item_identifier_item_UniqueItemId")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "IdentifierTypeId", nullable = false)
    @ForeignKey(name = "FK_item_identifier_item_type")
    private IdentifierType identifierType;

    @Column(name = "IdentifierValue", length = 255)
    @NotNull
    private String identifierValue;

    @Override
    public void prePersist() {
        super.prePersist();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void preUpdate() {
        super.preUpdate();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
