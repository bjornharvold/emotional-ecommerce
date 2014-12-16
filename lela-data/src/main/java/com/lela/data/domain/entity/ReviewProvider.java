package com.lela.data.domain.entity;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "review_provider")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ReviewProviderId", columnDefinition = "int(11)"))
public class ReviewProvider extends AbstractEntity {

    @NotNull
    @Column(name="ReviewProviderName", nullable = false)
    String reviewProviderName;
}
