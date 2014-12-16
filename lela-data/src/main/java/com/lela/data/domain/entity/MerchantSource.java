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
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "merchant_source", uniqueConstraints = {@UniqueConstraint(columnNames={"SourceId","SourceTypeId"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "MerchantSourceId", columnDefinition = "int(11)"))
public class MerchantSource extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "SourceTypeId")
    @ForeignKey(name = "FK_merchant_source_merchant_source_type")
    private MerchantSourceType merchantSourceType;

    @Column(name = "SourceId")
    @NotNull
    private Integer sourceId;

    @Column(name = "SourceName", length = 255)
    private String sourceName;
}
