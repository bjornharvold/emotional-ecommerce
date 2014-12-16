package com.lela.data.domain.entity;

import org.codehaus.jackson.annotate.JsonIgnore;
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
import java.util.ArrayList;
import java.util.List;

@RooJavaBean
@RooToString(excludeFields = {"item", "category", "dateCreated", "dateModified", "version"})
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "item_attribute", uniqueConstraints = {@UniqueConstraint(columnNames={"UniqueItemId", "AttributeTypeId", "AttributeSequence"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ItemAttributeId", columnDefinition = "int(11)"))
public class ItemAttribute extends AbstractEntity {

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "UniqueItemId", nullable = false)
    @ForeignKey(name = "FK_item_attribute_item_UniqueItemId")
    private Item item;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "AttributeTypeId", nullable = false)
    @ForeignKey(name = "FK_item_attribute_attribute_type")
    private AttributeType attributeType;

    @Column(name = "AttributeSequence")
    @NotNull
    private Integer attributeSequence;

    @Column(name = "AttributeValue", length = 12000)
    private String attributeValue;

    @Override
    public void prePersist() {
        super.prePersist();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void preUpdate() {
        super.preUpdate();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public static List<ItemAttribute> findItemAttributeEntriesByItem(Long id, int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItemAttribute o where o.item.id = :ItemId", ItemAttribute.class).setParameter("ItemId", id).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static long countItemAttributesByItem(Long id) {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItemAttribute o where o.item.id = :ItemId", Long.class).setParameter("ItemId", id).getSingleResult();
    }

    public static List<ItemAttribute> findByItemId(List<Item> items)
    {
        List<Long> ids = new ArrayList<Long>(items.size());
        for(Item item:items)
        {
            ids.add(item.getId());
        }

        Session session = ((Session)entityManager().getDelegate());
        session.setDefaultReadOnly(true);

        Criteria query = session.createCriteria(ItemAttribute.class, "o").
                add(Restrictions.in("item.id", ids));
        query.setReadOnly(true);
        return query.list();
    }

    @Transient
    @JsonIgnore
    public static ItemAttribute getItemAttributeForItem(Item item, String attributeName) {
        List<ItemAttribute> itemAttributes = entityManager().createQuery("from ItemAttribute o where o.item = :item and o.attributeType.attributeName = :attributeName", ItemAttribute.class).setParameter("item", item).setParameter("attributeName", attributeName).getResultList();
        if(itemAttributes.size() == 0)
        {
            ItemAttribute itemAttribute = new ItemAttribute();
            itemAttribute.setAttributeValue("Unknown");
            return itemAttribute;
        }
        return itemAttributes.get(0);
    }

}
