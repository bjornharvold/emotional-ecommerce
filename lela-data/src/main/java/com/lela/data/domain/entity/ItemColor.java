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
@Table(name = "item_color", uniqueConstraints = {@UniqueConstraint(columnNames={"UniqueItemId", "ColorId"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ItemColorId", columnDefinition = "int(11)"))
public class ItemColor extends AbstractEntity {

    @ManyToOne(targetEntity = Item.class)
    @JoinColumn(name = "UniqueItemId", nullable = false)
    @ForeignKey(name = "FK_item_color_item_UniqueItemId")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "ColorId", nullable = false)
    @ForeignKey(name = "FK_item_color_color")
    private Color color;

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
