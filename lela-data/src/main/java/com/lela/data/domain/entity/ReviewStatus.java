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
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "review_status")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ReviewStatusId", columnDefinition = "int(11)"))
public class ReviewStatus extends AbstractEntity{

    @Column(name="ReviewStatusName", unique=true)
    @NotNull
    private String reviewStatusName;

}
