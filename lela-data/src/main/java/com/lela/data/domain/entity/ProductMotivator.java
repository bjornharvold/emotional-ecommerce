package com.lela.data.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "product_motivator", uniqueConstraints = {@UniqueConstraint(columnNames={"UniqueItemId", "MotivatorId"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ProductMotivatorId", columnDefinition = "int(11)"))
public class ProductMotivator extends AbstractEntity {

    @Column(name = "MotivatorScore")
    private Integer motivatorScore;

    @Column(name = "IsDirty", columnDefinition = "INT(11) DEFAULT 0")
    private Boolean dirty;

    @ManyToOne
    @JoinColumn (name = "UniqueItemId", nullable = false)
    @ForeignKey(name = "FK_product_motivator_item")
    private Item item;

    @ManyToOne
    @JoinColumn (name = "MotivatorId", nullable = false)
    @ForeignKey(name = "FK_product_motivator_motivator")
    private Motivator motivator;

    @Override
    public void prePersist() {
        super.prePersist();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void preUpdate() {
        super.preUpdate();    //To change body of overridden methods use File | Settings | File Templates.
    }

}
