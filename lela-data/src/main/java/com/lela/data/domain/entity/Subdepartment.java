package com.lela.data.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "subdepartment")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "SubdepartmentId", columnDefinition = "int(11)"))
public class Subdepartment extends AbstractEntity {

    @NotNull
    @Column(name = "SubdepartmentName")
    private String subdepartmentName;

    @NotNull
    @Column(name = "SubdepartmentURLName")
    private String subdepartmentURLName;

    @NotNull
    @Column(name = "SubdepartmentStatus")
    private String subdepartmentStatus;

    @Column(name = "IsDirty", columnDefinition = "INT(11) NOT NULL DEFAULT 1")
    private Boolean dirty;
}
