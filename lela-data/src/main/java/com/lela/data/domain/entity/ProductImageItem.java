package com.lela.data.domain.entity;

import com.lela.data.enums.ProductImageItemStatuses;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "product_image_item", uniqueConstraints = {@UniqueConstraint(columnNames={"ImageId", "UniqueItemId"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ProductImageItemId", columnDefinition = "int(11)"))
public class ProductImageItem extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "UniqueItemId", nullable = false)
    @ForeignKey(name = "FK_product_image_item_item_UniqueItemId")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ImageId", nullable = false)
    @ForeignKey(name = "FK_product_image_item_product_image")
    private ProductImage productImage;

    @Column(name = "DoNotUse", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean doNotUse;

    @Column(name = "Duplicate", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean duplicate;

    @ManyToOne
    @JoinColumn(name = "ProductImageItemStatusId", nullable = false)
    @ForeignKey(name = "FK_product_image_item_status")
    private ProductImageItemStatus productImageItemStatus;

    @Override
    public void prePersist() {
        super.prePersist();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void preUpdate() {
        super.preUpdate();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public static long countValidProductImageItemsByItem(Item item) {
        Query query = entityManager().createQuery("SELECT COUNT(o) FROM ProductImageItem o where item = :item and (productImageItemStatus = :preferred or productImageItemStatus = :use)", Long.class).setParameter("item", item).setParameter("preferred", ProductImageItemStatuses.PREFERRED_IMAGE.getProductImageItemStatus()).setParameter("use", ProductImageItemStatuses.USE.getProductImageItemStatus());
        return (Long)query.getSingleResult();
    }

    public static long countProductImageItemsByItem(Item item) {
        Query query = entityManager().createQuery("SELECT COUNT(o) FROM ProductImageItem o where item = :item", Long.class).setParameter("item", item);
        return (Long)query.getSingleResult();
    }
}
