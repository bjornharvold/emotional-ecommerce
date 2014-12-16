package com.lela.data.domain.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "attribute_formula", uniqueConstraints = {@UniqueConstraint(columnNames={"CategoryId","AttributeTypeId"})})
@AttributeOverride(name = "id", column = @Column(name = "AttributeFormulaId", columnDefinition = "int(11)"))
@RooSerializable
public class AttributeFormula extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "AttributeTypeId", nullable = false)
    @ForeignKey(name = "FK_attribute_formula_attribute_type")
    private AttributeType attributeType;

    @OneToMany(mappedBy = "attributeFormula", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private Set<AttributeFormulaAttributeType> attributeFormulaAttributeTypes;

    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_attribute_formula_category")
    private Category category;

    @Column(name = "SelectClause", length = 1024)
    @NotNull
    private String selectClause;

    @Column(name = "FromClause", length = 1024)
    private String fromClause;

    @Column(name = "WhereClause", length = 1024)
    @NotNull
    private String whereClause;

    @Column(name = "SequenceGroup")
    @NotNull
    private Integer sequenceGroup;

    @Column(name = "Description")
    @NotNull
    private String description;

    public static AttributeFormula findAttributeFormulaByAttributeType(AttributeType attributeType)
    {
        Query query = entityManager().createQuery("FROM AttributeFormula o where o.attributeType = :attributeType").setParameter("attributeType", attributeType);
        return (AttributeFormula)query.getSingleResult();
    }
}
