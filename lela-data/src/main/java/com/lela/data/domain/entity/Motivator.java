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

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "motivator")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "MotivatorId", columnDefinition = "int(11)"))
public class Motivator extends AbstractEntity {
    @Column(name = "MotivatorLabel")
    private Character motivatorLabel;

    @ManyToOne
    @JoinColumn(name = "CategoryId", nullable = false)
    @ForeignKey(name = "FK_motivator_category")
    private Category category;

    @Column(name = "SubCategoryId")
    private Integer subCategoryId;

    @Column(name = "RulesEngineEdd", length = 50)
    private String rulesEngineEdd;

    @Column(name = "RulesEngineDt", length = 50)
    private String rulesEngineDt;
}
