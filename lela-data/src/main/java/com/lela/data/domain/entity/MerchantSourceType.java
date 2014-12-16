package com.lela.data.domain.entity;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "merchant_source_type")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "MerchantSourceTypeId", columnDefinition = "int(11)"))
public class MerchantSourceType extends AbstractEntity {
    @Column(name = "SourceTypeName", length = 255)
    private String sourceTypeName;
}
