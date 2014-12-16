package com.lela.data.domain.entity;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "category_group")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "CategoryGroupId", columnDefinition = "int(11)"))
public class CategoryGroup extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "NavbarId", nullable = false)
    @ForeignKey(name = "FK_category_group_navbar")
    @NotNull
    private Navbar navbar;

    @Column(name = "CategoryGroupName", length = 255)
    private String categoryGroupName;

    @Column(name = "CategoryGroupURLName", length = 255)
    private String categoryGroupUrlName;

    @Column(name = "CategoryGroupOrder")
    private Integer categoryGroupOrder;

    @Column(name = "CategoryGroupStatus", length = 255)
    private String categoryGroupStatus;

    @Column(name = "IsDirty", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean dirty;
}
