package com.lela.data.domain.entity;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "design_style_motivator", uniqueConstraints = {@UniqueConstraint(columnNames={"CategoryId","DesignStyle"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "DesignStyleMotivatorId", columnDefinition = "int(11)"))
public class DesignStyleMotivator extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_design_style_motivator_category")
    private Category category;

    @Column(name = "DesignStyle", length = 255)
    @NotNull
    private String designStyle;

    @Column(name = "H")
    private Integer h;

    @Column(name = "I")
    private Integer i;

    @Column(name = "J")
    private Integer j;

    @Column(name = "K")
    private Integer k;

    @Column(name = "L")
    private Integer l;

    public static DesignStyleMotivator findDesignStyleMotivatorsByItem(Item item) {
        return entityManager().createQuery("SELECT o FROM DesignStyleMotivator o where o.category = :Category and o.designStyle = :itemDesignStyle", DesignStyleMotivator.class).setParameter("Category", item.getCategory()).setParameter("itemDesignStyle",item.getDesignStyle()).getSingleResult();
    }
}
