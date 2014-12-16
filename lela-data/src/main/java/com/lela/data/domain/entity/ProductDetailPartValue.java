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
@Table(name = "product_detail_part_value",
        uniqueConstraints = {@UniqueConstraint(columnNames={"ProductDetailPartValueId", "AttributeTypeId", "AttributeValue"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ProductDetailPartValueId", columnDefinition = "int(11)"))
public class ProductDetailPartValue extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "ProductDetailPartId", nullable = false)
    @ForeignKey(name = "FK_product_detail_part_value_product_detail_part")
    private ProductDetailPart productDetailPart;

    @ManyToOne
    @JoinColumn(name="AttributeTypeId", nullable = false)
    @ForeignKey(name = "FK_product_detail_part_value_attribute_type")
    private AttributeType attributeType;

    @NotNull
    @Column(name = "AttributeValue", length = 255)
    private String attributeValue;

    @Override
    public void prePersist() {
        super.prePersist();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void preUpdate() {
        super.preUpdate();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
