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
@Table(name = "attribute_type_motivator", uniqueConstraints = {@UniqueConstraint(columnNames={"CategoryId", "AttributeTypeId","Motivator"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "AttributeTypeMotivatorId", columnDefinition = "int(11)"))
public class AttributeTypeMotivator extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_attribute_type_motivator_category")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "AttributeTypeId", nullable = false)
    @ForeignKey(name = "FK_attribute_type_motivator_attribute_type")
    private AttributeType attributeType;

    @Column(name = "Motivator")
    @NotNull
    private Integer motivator;

    @Column(name = "EddDataType", length = 255)
    private String eddDataType;

    @Column(name = "IsRequired", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean required;

    @Override
    public void prePersist() {
        super.prePersist();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void preUpdate() {
        super.preUpdate();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public static List<AttributeTypeMotivator> findAllAttributeTypeMotivatorByCategory(Category category)
    {
        return entityManager().createQuery("SELECT o FROM AttributeTypeMotivator o where o.category = :Category", AttributeTypeMotivator.class).setParameter("Category", category).getResultList();
    }


}
