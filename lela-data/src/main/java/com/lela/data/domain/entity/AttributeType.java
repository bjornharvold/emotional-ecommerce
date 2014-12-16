package com.lela.data.domain.entity;

import com.lela.data.enums.AttributeTypeSources;
import com.lela.data.enums.ValidationTypes;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.criterion.Restrictions;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@RooJavaBean
@RooToString(excludeFields = {"dateCreated", "dateModified", "version"})
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "attribute_type",
        uniqueConstraints = {@UniqueConstraint(name = "UK_attribute_type_AttributeName", columnNames={"AttributeName"}),
                             @UniqueConstraint(name = "UK_attribute_type_LelaAttributeName", columnNames={"LelaAttributeName"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "AttributeTypeId", columnDefinition = "int(11)"))
public class AttributeType extends AbstractEntity {
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "ValidationTypeId", nullable = false, columnDefinition = "INT(11) DEFAULT 1")
    @ForeignKey(name = "FK_attribute_type_validation_type")
    private ValidationType validationType;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "AttributeTypeSourceId", nullable = false)
    @ForeignKey(name = "FK_attribute_type_attribute_type_source")
    private AttributeTypeSource attributeTypeSource;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "CleanseRuleId", nullable = false, columnDefinition = "INT(11) DEFAULT 0")
    @ForeignKey(name = "FK_attribute_type_cleanse_rule")
    private CleanseRule cleanseRule;

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "AccessoryTypeId", nullable = false, columnDefinition = "INT(11) DEFAULT 1")
    @ForeignKey(name = "FK_attribute_type_accessory_type_AccessoryTypeId")
    private AccessoryType accessoryType;

    @Column(name = "AttributeName", length = 255)
    @NotNull
    private String attributeName;

    @Column(name = "AttributeSourceName", length = 255)
    @NotNull
    private String attributeSourceName;

    @Column(name = "IsAccessory", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean accessory;

    @Column(name = "LelaAttributeName", length = 255)
    private String lelaAttributeName;

    /**
     * This will provide a list of all the Attribute Types used by items in a category
     * @param category
     * @return
     */
    public static List<AttributeType> findAllAttributeTypesForACategory(Category category)
    {
        return entityManager().createQuery("SELECT o FROM AttributeType o where exists (select i.id from ItemAttribute i where i.item.category = :Category)", AttributeType.class).setParameter("Category", category).getResultList();
    }

    public static List<AttributeType> findByLelaResearched()
    {
        Session session = ((Session)entityManager().getDelegate());
        Criteria criteria = session.createCriteria(AttributeType.class).
                add(Restrictions.eq("attributeTypeSource.id", AttributeTypeSources.LELA_SPREADSHEET.getId()));

        //TODO fix hardcoding
        criteria = criteria.add(Restrictions.not(Restrictions.in("validationType.id", new Long[]{ValidationTypes.NONE.getId(), ValidationTypes.DERIVED_ATTRIBUTE.getId()})));

        return criteria.list();
    }
}
