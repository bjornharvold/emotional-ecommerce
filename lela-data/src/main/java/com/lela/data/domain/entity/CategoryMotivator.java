package com.lela.data.domain.entity;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "category_motivator")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "CategoryMotivatorId", columnDefinition = "int(11)"))
public class CategoryMotivator extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_category_motivator_category")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "MotivatorId", nullable = false)
    @ForeignKey(name = "FK_category_motivator_motivator")
    private Motivator motivator;

    @Column(name = "DtRulesQuery", length = 25000)
    @NotNull
    @Lob
    private String dtRulesQuery;

    @Column(name = "DtRuleSet", length = 50)
    @NotNull
    private String dtRuleSet;

    @Column(name = "DtRuleSheet", length = 50)
    @NotNull
    private String dtRuleSheet;

    @Column(name = "DtRuleDataTag", length = 50)
    @NotNull
    private String dtRuleDataTag;
}
