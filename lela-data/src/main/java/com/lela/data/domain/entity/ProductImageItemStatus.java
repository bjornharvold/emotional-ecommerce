package com.lela.data.domain.entity;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "product_image_item_status")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ProductImageItemStatusId", columnDefinition = "int(11)"))
public class ProductImageItemStatus extends AbstractEntity {

    @Column(name="ProductImageItemStatusName", unique = true)
    private String productImageItemStatusName;
}
