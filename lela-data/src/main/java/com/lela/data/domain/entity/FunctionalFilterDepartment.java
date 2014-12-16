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
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "functional_filter_department")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "FunctionalFilterDepartmentId", columnDefinition = "int(11)"))
public class FunctionalFilterDepartment extends AbstractEntity {

    @ManyToOne(targetEntity = FunctionalFilter.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "FunctionalFilterId", nullable = false)
    @ForeignKey(name = "FK_functional_filter_department_functional_filter")
    @NotNull
    FunctionalFilter functionalFilter;

    @ManyToOne(targetEntity = Department.class)
    @JoinColumn(name = "DepartmentId", nullable = false)
    @ForeignKey(name = "FK_functional_filter_department_department")
    @NotNull
    private Department department;

    @Column(name="ObjectId", length = 24)
    private String objectId;

}
