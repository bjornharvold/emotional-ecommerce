package com.lela.data.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@RooSerializable
@Table(name = "department", uniqueConstraints = {@UniqueConstraint(name = "UK_department_DepartmentName", columnNames={"DepartmentName"}), @UniqueConstraint(name = "UK_department_DepartmentURLName", columnNames={"DepartmentURLName"})})
@AttributeOverride(name = "id", column = @Column(name = "DepartmentId", columnDefinition = "int(11)"))
public class Department extends AbstractEntity {

    @NotNull
    @Column(name = "DepartmentName")
    private String departmentName;

    @NotNull
    @Column(name = "DepartmentURLName")
    private String departmentURLName;

    @NotNull
    @Column(name = "DepartmentOrder")
    private java.lang.Integer departmentOrder;

    @Column(name = "DepartmentStatus")
    private String departmentStatus;

    @Column(name = "LelaURL")
    @NotNull
    private String lelaUrl;

    @Column(name = "IsDirty", columnDefinition = "INT(11) NOT NULL DEFAULT 1")
    private Boolean dirty;


    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "NavbarId", nullable = false)
    @ForeignKey(name = "FK_department_navbar")
    private Navbar navbar;
}
