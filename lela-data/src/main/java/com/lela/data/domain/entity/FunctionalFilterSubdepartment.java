package com.lela.data.domain.entity;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "functional_filter_subdepartment")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "FunctionalFilterSubdepartmentId", columnDefinition = "int(11)"))
public class FunctionalFilterSubdepartment extends AbstractEntity {
    @ManyToOne(targetEntity = FunctionalFilter.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "FunctionalFilterId", nullable = false)
    @ForeignKey(name = "FK_functional_filter_subdepartment_functional_filter")
    FunctionalFilter functionalFilter;

    @ManyToOne(targetEntity = Subdepartment.class)
    @JoinColumn(name = "SubdepartmentId", nullable = false)
    @ForeignKey(name = "FK_functional_filter_subdepartment_subdepartment")
    private Subdepartment subdepartment;

    @Column(name="ObjectId", length = 24)
    private String objectId;

}
