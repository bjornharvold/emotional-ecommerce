package com.lela.data.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "network")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "NetworkId", columnDefinition = "int(11)"))
public class Network extends AbstractEntity {

    @Column(name = "NetworkName", length = 25)
    private String networkName;

    @Column(name = "PopshopsDesignator", length = 5)
    private String popshopsDesignator;

    @Column(name = "Enabled", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean enabled;
}
