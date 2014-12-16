package com.lela.data.domain.entity;

import com.lela.data.domain.dto.ItemSearchCommand;
import com.lela.data.enums.AttributeTypeSources;
import com.lela.data.enums.ItemReportingAttribute;
import com.lela.data.excel.command.ItemWorkbookCommand;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.criterion.Restrictions;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import java.util.List;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "attribute_type_category", uniqueConstraints = {@UniqueConstraint(columnNames={"CategoryId","AttributeTypeId"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "AttributeTypeCategoryId", columnDefinition = "int(11)"))
public class AttributeTypeCategory extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "AttributeTypeId", nullable = false)
    @ForeignKey(name = "FK_attribute_type_category_attribute_type")
    private AttributeType attributeType;

    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_attribute_type_category_category")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "InputTypeId")
    @ForeignKey(name = "FK_attribute_type_category_input_type")
    private InputType inputType;

    @ManyToOne
    @JoinColumn(name = "InputValidationListId")
    @ForeignKey(name = "FK_attribute_type_input_validation_list")
    private InputValidationList inputValidationList;

    @Column(name = "DefaultValue", length = 255)
    private String defaultValue;

    @Column(name = "SuppressUnknown", columnDefinition = "INT(11) NOT NULL DEFAULT 1")
    private Boolean suppressUnknown;


    /**
     * Find all the attribute types that can be used for a category
     * @param category
     * @return
     */
    public static List<AttributeTypeCategory> findAllAttributeTypesCategoryByCategory(Category category)
    {
        return entityManager().createQuery("SELECT o FROM AttributeTypeCategory o where o.category = :Category", AttributeTypeCategory.class).setParameter("Category", category).getResultList();
    }

    public static List<AttributeTypeCategory> findByItemSearchCommand(ItemSearchCommand itemSearchCommand, ItemWorkbookCommand itemWorkbookCommand)
    {
        ((Session)entityManager().getDelegate()).createCriteria(AttributeTypeCategory.class);
        Session session = ((Session)entityManager().getDelegate());
        Criteria criteria = session.createCriteria(AttributeTypeCategory.class).
                add(Restrictions.eq("category.id", itemSearchCommand.getCategoryId()));

        if(itemWorkbookCommand.getShowAttribute().equals(ItemReportingAttribute.LELA))
        {
            criteria = criteria.createCriteria("attributeType").add(Restrictions.eq("attributeTypeSource.id", AttributeTypeSources.LELA_SPREADSHEET.getId()));
        }
        if(itemWorkbookCommand.getShowAttribute().equals(ItemReportingAttribute.CNET))
        {
            criteria = criteria.createCriteria("attributeType").add(Restrictions.eq("attributeTypeSource.id", AttributeTypeSources.CNET.getId()));
        }

        return criteria.list();
    }

    public static List<AttributeTypeCategory> findLelaResearchAttributes(Category category)
    {
        ((Session)entityManager().getDelegate()).createCriteria(AttributeTypeCategory.class);
        Session session = ((Session)entityManager().getDelegate());
        Criteria criteria = session.createCriteria(AttributeTypeCategory.class).
                add(Restrictions.eq("category.id", category.getId()));

        criteria = criteria.createCriteria("attributeType").add(Restrictions.eq("attributeTypeSource.id", AttributeTypeSources.LELA_SPREADSHEET.getId()));

        return criteria.list();
    }
}
