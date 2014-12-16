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
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "identifier_type")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "IdentifierTypeId", columnDefinition = "int(11)"))
public class IdentifierType extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "AttributeTypeId")
    @ForeignKey(name = "FK_identifier_type_attribute_type")
    private AttributeType attributeType;

    @Column(name = "IdentifierTypeName", length = 255)
    @NotNull
    private String identifierTypeName;

    @Column(name = "IsMultiValued", columnDefinition = "INT(11) NOT NULL DEFAULT 1")
    @NotNull
    private Boolean multiValued;
}
