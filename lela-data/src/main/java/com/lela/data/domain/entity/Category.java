package com.lela.data.domain.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@RooJavaBean
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "category")
@AttributeOverride(name = "id", column = @Column(name = "CategoryId", columnDefinition = "int(11)"))
@RooSerializable
@RooToString(excludeFields = {"dataSourceType", "categoryCategoryGroup", "functionalFilter", "categoryOrder", "URLName", "categoryObjectId", "catalogKey", "categoryNavBar", "dirty", "objectId", "dataSourceString", "visible"})
public class Category extends AbstractEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DataSourceTypeId")
    @ForeignKey(name = "FK_category_data_source_type")
    private DataSourceType dataSourceType;

    @OneToMany(mappedBy="category", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<CategoryCategoryGroup> categoryCategoryGroup;

    @Column(name = "CategoryName", length = 80)
    @NotNull
    private String categoryName;

    @Column(name = "CategoryOrder")
    private Integer categoryOrder;

    @Column(name = "URLName", length = 100)
    private String urlname;

    @Column(name = "LelaUrl", length = 100)
    private String lelaUrl;

    @Column(name = "CategoryObjectId", length = 50)
    private String categoryObjectId;

    @Column(name = "CatalogKey", length = 50)
    private String catalogKey;

    @Column(name = "CategoryNavbar")
    private Integer categoryNavbar;

    @Column(name = "IsDirty", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean dirty;

    @Column(name = "objectId", length = 24)
    private String objectId;

    @Column(name = "DataSourceString", length = 255)
    private String dataSourceString;

    @Column(name = "IsVisible", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean visible;

    @Column(name = "EnableBrandFilter", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    @NotNull
    private Boolean enableBrandFilter;

    @Column(name = "EnableBrandAveragedMotivators", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    @NotNull
    private Boolean enableBrandAveragedMotivators;

    @Column(name = "RetentionLimitMonths")
    private Integer retentionLimitMonths;

    //public String toString() {
    //    return new ReflectionToStringBuilder(this, ToStringStyle.NO_FIELD_NAMES_STYLE).setExcludeFieldNames("dateCreated", "dateModified", "categoryCategoryGroup", "functionalFilter", "version").toString();
    //}

    @Transactional
    public Category merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Category merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public static List<Category> findAllCategorys() {
        return entityManager().createQuery("SELECT o FROM Category o order by categoryName asc", Category.class).getResultList();
    }

    @Transient
    public static Department getDepartment(Category category)
    {
        CategorySubdepartment categorySubdepartment = CategorySubdepartment.findByCategory(category);
        if(categorySubdepartment!=null){
          Subdepartment subdepartment = categorySubdepartment.getSubdepartment();
          if(subdepartment!=null){
              SubdepartmentDepartment subdepartmentDepartment = SubdepartmentDepartment.findBySubdepartment(subdepartment);
              if(subdepartmentDepartment!=null)
              {
                  return subdepartmentDepartment.getDepartment();
              }
          }

        }
        return null;
    }

}
