package com.lela.data.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "affiliate")
@AttributeOverride(name = "id", column = @Column(name = "AffiliateId", columnDefinition = "int(11)"))
public class Affiliate extends AbstractEntity {

    @NotNull
    @Column(name = "AffiliateName")
    private String affiliateName;
}
