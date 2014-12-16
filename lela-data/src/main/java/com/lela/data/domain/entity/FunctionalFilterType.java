package com.lela.data.domain.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString(excludeFields = {"dateCreated", "dateModified", "version"})
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "functional_filter_type")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "FunctionalFilterTypeId", columnDefinition = "int(11)"))
public class FunctionalFilterType extends AbstractEntity {

    @OneToMany(mappedBy = "functionalFilterType")
    private Set<FunctionalFilter> functionalFilters = new HashSet<FunctionalFilter>();

    @Column(name = "FunctionalFilterTypeName", length = 80)
    private String functionalFilterTypeName;
}
