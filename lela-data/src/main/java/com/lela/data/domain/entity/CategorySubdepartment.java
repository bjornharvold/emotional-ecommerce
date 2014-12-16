package com.lela.data.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "category_subdepartment")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "CategorySubdepartmentId", columnDefinition = "int(11)"))
public class CategorySubdepartment extends AbstractEntity {


    @NotNull
    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_category_subdepartment_category")
    private Category category;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "SubdepartmentId", nullable = false)
    @ForeignKey(name = "FK_category_subdepartment_subdepartment")
    private Subdepartment subdepartment;

    @NotNull
    @Column(name = "CategoryOrder")
    private java.lang.Integer categoryOrder;

    public static CategorySubdepartment findByCategory(Category category)
    {
       return entityManager().createQuery("from CategorySubdepartment o where o.category = :category", CategorySubdepartment.class).setParameter("category", category).getSingleResult();
    }
}
