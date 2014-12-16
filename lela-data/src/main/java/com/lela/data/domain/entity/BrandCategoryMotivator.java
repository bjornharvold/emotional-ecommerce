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
@Table(name = "brand_category_motivator", uniqueConstraints = {@UniqueConstraint(columnNames={"CategoryId", "BrandId", "Motivator"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "BrandCategoryMotivatorId", columnDefinition = "int(11)"))
public class BrandCategoryMotivator extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "BrandId", nullable = false)
    @ForeignKey(name = "FK_brand_category_motivator_brand")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_brand_category_motivator_category")
    private Category category;

    @Column(name = "Motivator")
    @NotNull
    private Integer motivator;

    @Column(name = "MotivatorScore")
    private Integer motivatorScore;
}
