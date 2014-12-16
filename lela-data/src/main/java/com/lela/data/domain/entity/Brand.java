package com.lela.data.domain.entity;

import java.util.List;
import java.util.Set;
import javax.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooSerializable
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "brand", finders = { "findBrandsByBrandName" })
@AttributeOverride(name = "id", column = @Column(name = "BrandId", columnDefinition = "int(11)"))
public class Brand extends AbstractEntity {

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private Set<BrandCategory> brandCategories;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private Set<BrandCategoryMotivator> brandCategoryMotivators;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private Set<BrandCountry> brandCountries;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private Set<BrandHistory> brandHistories;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private Set<BrandIdentifier> brandIdentifiers;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private Set<BrandMotivator> brandMotivators;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private Set<BrandName> brandNames;

    @Column(name = "BrandName", length = 80)
    @NotNull
    private String brandName;

    @Column(name = "Manufacturer", length = 80)
    private String manufacturerName;

    @Column(name = "FacebookUrl", length = 80)
    private String facebookUrl;

    @Column(name = "FacebookAccess")
    private Integer facebookAccess;

    @Column(name = "FacebookLikes")
    private Integer facebookLikes;

    @Column(name = "TwitterUrl", length = 80)
    private String twitterUrl;

    @Column(name = "TwitterName", length = 80)
    private String twitterName;

    @Column(name = "TwitterFollowers")
    private Integer twitterFollowers;

    @Column(name = "PopshopsBrandId")
    private Integer popshopsBrandId;

    @Column(name = "URLName", length = 100)
    private String urlname;

    @Column(name = "LelaUrl", length = 100)
    private String lelaUrl;

    @Column(name = "objectId", length = 24)
    private String objectId;

    @Column(name = "PopshopsSuccess", columnDefinition = "INT(11) DEFAULT 0")
    private Boolean popshopsSuccess;

    @Column(name = "IsDirty", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean isDirty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ManufacturerId")
    @ForeignKey(name = "FK_brand_manufacturer")
    private Manufacturer manufacturer;

    public static List<Brand> findBrandsForACategory(Category category) {
        return entityManager().createQuery("SELECT o FROM Brand o left join o.brandCategories c where c.category = :Category group by o order by o.brandName", Brand.class).setParameter("Category", category).getResultList();
    }
}
