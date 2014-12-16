package com.lela.data.domain.entity;

import org.hibernate.annotations.Index;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@Table(name = "locale", uniqueConstraints = {@UniqueConstraint(name = "UK_locale_LocaleName", columnNames={"LocaleName"})})
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "LocaleId", columnDefinition = "int(11)"))
public class Locale extends AbstractEntity {
    @Column(name = "LocaleName", length = 255)
    @NotNull
    private String localeName;
}
