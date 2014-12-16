package com.lela.data.domain.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "product_detail_group_attribute")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ProductDetailGroupAttributeId", columnDefinition = "int(11)"))
public class ProductDetailGroupAttribute extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "ProductDetailGroupId", nullable = true)
    @ForeignKey(name = "FK_detail_group_attribute_detail_group")
    private ProductDetailGroup productDetailGroup;

    @OneToMany(mappedBy = "productDetailGroupAttribute", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private Set<ProductDetailAttributeType> productDetailAttributeTypes;

    @OneToMany(mappedBy = "productDetailGroupAttribute", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private Set<ProductDetailPart> productDetailParts;

    @Column(name = "OrderInGroup")
    private Integer orderInGroup;

    @Column(name = "AttrLabel", length = 54)
    private String attrLabel;

    @Column(name = "AttrName", length = 54)
    private String attrName;

    @ManyToOne
    @JoinColumn(name = "ProductDetailAttributeValueTypeId", nullable = true)
    @ForeignKey(name = "FK_detail_group_attribute_detail_group_value_type")
    private ProductDetailAttributeValueType productDetailAttributeValueTypeId;

    @Override
    public void prePersist() {
        super.prePersist();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void preUpdate() {
        super.preUpdate();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
