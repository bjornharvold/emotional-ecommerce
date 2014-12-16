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
@Table(name = "product_detail_attribute_type", uniqueConstraints = {@UniqueConstraint(columnNames={"ProductDetailGroupAttributeId", "AttributeTypeID"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ProductDetailAttributeTypeId", columnDefinition = "int(11)"))
public class ProductDetailAttributeType extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "AttributeTypeID", nullable = false)
    @ForeignKey(name = "FK_product_detail_attribute_type_attribute_type")
    private AttributeType attributeType;

    @ManyToOne
    @JoinColumn(name = "ProductDetailGroupAttributeId", nullable = false)
    @ForeignKey(name = "FK_product_detail_attribute_type_product_detail_group_attribute")
    private ProductDetailGroupAttribute productDetailGroupAttribute;

    @Override
    public void preUpdate() {
        super.preUpdate();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void prePersist() {
         super.prePersist();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
