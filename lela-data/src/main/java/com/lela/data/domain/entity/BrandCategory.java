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
@Table(name = "brand_category", uniqueConstraints = {@UniqueConstraint(columnNames={"BrandId", "CategoryId"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "BrandCategoryId", columnDefinition = "int(11)"))
public class BrandCategory extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "BrandId", nullable = false)
    @ForeignKey(name = "FK_brand_category_brand")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_brand_category_category")
    private Category category;

    @Column(name = "EnableLoad", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    @NotNull
    private Boolean enableLoad;

}
