package com.lela.data.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "product_detail_attribute_value_type")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ProductDetailAttributeValueTypeId", columnDefinition = "int(11)"))
public class ProductDetailAttributeValueType extends AbstractEntity {

    @NotNull
    @Column(name = "ProductDetailAttributeValueTypeName", length = 255)
    private String productDetailAttributeValueTypeName;
}
