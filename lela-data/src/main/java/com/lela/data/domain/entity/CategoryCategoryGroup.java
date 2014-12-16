package com.lela.data.domain.entity;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "category_category_group", uniqueConstraints = {@UniqueConstraint(columnNames={"CategoryId","CategoryGroupId"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "CategoryCategoryGroupId", columnDefinition = "int(11)"))
public class CategoryCategoryGroup extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "CategoryId")
    @ForeignKey(name = "FK_category_category_group_category")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "CategoryGroupId")
    @ForeignKey(name = "FK_category_category_group_category_group")
    private CategoryGroup categoryGroup;
}
