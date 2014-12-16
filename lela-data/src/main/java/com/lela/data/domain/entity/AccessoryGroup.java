package com.lela.data.domain.entity;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "accessory_group" )
@AttributeOverride(name = "id", column = @Column(name = "AccessoryGroupId", columnDefinition = "int(11)"))
@RooSerializable
public class AccessoryGroup extends AbstractEntity {

    @OneToMany(mappedBy = "accessoryGroup")
    private Set<AccessoryValueGroup> accessoryValueGroups;

    @Column(name = "AccessoryGroupName", length = 255)
    @NotNull
    private String accessoryGroupName;

    @NotNull
    @Column(name = "AccessoryGroupLabel", nullable = false)
    private String accessoryGroupLabel;
}
