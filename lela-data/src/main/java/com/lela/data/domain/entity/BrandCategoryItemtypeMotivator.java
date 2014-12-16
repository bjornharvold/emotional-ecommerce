package com.lela.data.domain.entity;

import javax.persistence.*;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "brand_category_itemtype_motivator", uniqueConstraints = {@UniqueConstraint(columnNames={"CategoryId","BrandId", "ItemType", "Motivator"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "BrandCategoryItemtypeMotivatorId", columnDefinition = "int(11)"))
public class BrandCategoryItemtypeMotivator extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name="CategoryId")
    @ForeignKey(name = "FK_brand_category_itemtype_motivator_category")
    private Category category;

    @ManyToOne
    @JoinColumn(name="BrandId")
    @ForeignKey(name = "FK_brand_category_itemtype_motivator_brand")
    private Brand brand;

    @Column(name = "ItemType")
    private String itemType;

    @Column(name = "Motivator")
    private Integer motivator;

    @Column(name = "MotivatorScore")
    private Integer motivatorScore;
}
