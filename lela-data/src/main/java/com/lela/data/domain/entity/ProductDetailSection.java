package com.lela.data.domain.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "product_detail_section")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "SectionId", columnDefinition = "int(11)"))
public class ProductDetailSection extends AbstractEntity {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "section")
    private Set<ProductDetailGroup> productDetailGroups = new HashSet<ProductDetailGroup>();

    @ManyToOne
    @JoinColumn (name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_product_detail_section_category")
    private Category category;

    @NotNull
    @Column(name = "SectionName", length = 100)
    private String sectionName;

    @NotNull
    @Column(name = "SectionOrder")
    private Integer sectionOrder;

    @Column(name = "IsDirty", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean dirty;

    @Column(name = "ObjectId", length = 24)
    private String objectId;

    @NotNull
    @Column(name = "ProductDetailSectionLabel", nullable = false)
    private String productDetailSectionLabel;

    public static List<ProductDetailSection> findByCategory(Category category)
    {
        Query query = entityManager().createQuery("FROM ProductDetailSection o where o.category = :category").setParameter("category", category);
        return query.getResultList();
    }
}
