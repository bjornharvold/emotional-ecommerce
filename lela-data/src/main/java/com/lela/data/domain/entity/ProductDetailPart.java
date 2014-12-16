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
@Table(name = "product_detail_part", uniqueConstraints = {@UniqueConstraint(columnNames={"ProductDetailGroupAttributeId", "PartSeq"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ProductDetailPartId", columnDefinition = "int(11)"))
public class ProductDetailPart extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "ProductDetailGroupAttributeId", nullable = false)
    @ForeignKey(name = "FK_product_detail_part_product_detail_group_attribute")
    private ProductDetailGroupAttribute productDetailGroupAttribute;

    @OneToMany(mappedBy = "productDetailPart", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private Set<ProductDetailPartValue> productDetailPartValue;

    @Column(name = "PartSeq")
    private Integer partSeq;

    @Column(name = "PartName", length = 255)
    private String partName;

    @Override
    public void prePersist() {
        super.prePersist();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void preUpdate() {
        super.preUpdate();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
