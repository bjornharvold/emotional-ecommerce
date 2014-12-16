package com.lela.data.domain.entity;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "product_motivator_error", uniqueConstraints = {@UniqueConstraint(columnNames={"UniqueItemId", "MotivatorId"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ProductMotivatorErrorId", columnDefinition = "int(11)"))
public class ProductMotivatorError extends AbstractEntity {

    @ManyToOne
    @JoinColumn (name = "UniqueItemId", nullable = false)
    @ForeignKey(name = "FK_product_motivator_error_item_UniqueItemId")
    private Item item;

    @ManyToOne
    @JoinColumn (name = "MotivatorId", nullable = false)
    @ForeignKey(name = "FK_product_motivator_error_motivator_MotivatorId")
    private Motivator motivator;

    @Column(name = "Error", length = 4096)
    private String error;

    @Column(name = "StackTrace", length = 4096)
    private String stackTrace;

    @Override
    public void prePersist() {
        super.prePersist();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void preUpdate() {
        super.preUpdate();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
