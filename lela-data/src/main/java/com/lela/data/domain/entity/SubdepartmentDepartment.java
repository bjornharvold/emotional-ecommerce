package com.lela.data.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "subdepartment_department")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "SubdepartmentDepartmentId", columnDefinition = "int(11)"))
public class SubdepartmentDepartment extends AbstractEntity{

    @NotNull
    @Column(name = "SubdepartmentOrder")
    private java.lang.Integer subdepartmentOrder;

    @NotNull
    @ManyToOne
    @JoinColumn( name = "SubdepartmentId", nullable = false)
    @ForeignKey(name = "FK_subdepartment_department_subdepartment")
    private Subdepartment subdepartment;

    @NotNull
    @ManyToOne
    @JoinColumn( name = "DepartmentId", nullable = false)
    @ForeignKey(name = "FK_subdepartment_department_department")
    private Department department;

    public static SubdepartmentDepartment findBySubdepartment(Subdepartment subdepartment)
    {
        return entityManager().createQuery("from SubdepartmentDepartment o where o.subdepartment = :subdepartment", SubdepartmentDepartment.class).setParameter("subdepartment", subdepartment).getSingleResult();
    }
}
