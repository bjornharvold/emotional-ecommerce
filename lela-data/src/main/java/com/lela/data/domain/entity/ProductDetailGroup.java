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
@Table(name = "product_detail_group", uniqueConstraints = {@UniqueConstraint(columnNames={"SectionId","GroupId"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ProductDetailGroupId", columnDefinition = "int(11)"))
public class ProductDetailGroup extends AbstractEntity {

    //@ManyToOne(cascade = CascadeType.ALL, mappedBy = "productDetailGroups")
    //private Set<ProductDetailGroupAttribute> productDetailGroupAttributes = new HashSet<ProductDetailGroupAttribute>();

    @ManyToOne
    @JoinColumn(name = "SectionId", nullable = false)
    @ForeignKey(name = "FK_product_detail_group_product_detail_section")
    private ProductDetailSection section;

    @OneToMany(mappedBy = "productDetailGroup", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private Set<ProductDetailGroupAttribute> productDetailGroupAttributes;

    @NotNull
    @Column(name = "GroupId")
    private Integer groupId;

    @NotNull
    @Column(name = "GroupName", length  = 58)
    private String groupName;

    @NotNull
    @Column(name = "GroupOrder")
    private Integer groupOrder;

    @NotNull
    @Column(name = "ProductDetailGroupLabel", nullable = false)
    private String productDetailGroupLabel;

    @Override
    public void prePersist() {
        super.prePersist();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void preUpdate() {
        super.preUpdate();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
