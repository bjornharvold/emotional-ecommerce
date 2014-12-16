package com.lela.data.domain.entity;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(mappedSuperclass = true, identifierColumn = "ID", versionColumn = "Version")
public abstract class AbstractEntity {

    //@Version
    //@Column(name="Version", nullable = false, columnDefinition = "int(11) default 0")
    //private Integer version = 0;

    //@NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyy-MM-dd hh:mm:ss a")
    @Column(name="DateCreated", insertable = true, updatable = false)//, columnDefinition="timestamp default '0000-00-00 00:00:00'")
    private Date dateCreated;

    //@NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyy-MM-dd hh:mm:ss a")
    @Column(name="DateModified", insertable = true, updatable = true)//, columnDefinition="timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date dateModified;

    // entity listeners - populate required fields if they are empty
    @PrePersist
    public void prePersist() {
        Date d = new Date();
        if (this.dateCreated == null) {
            this.dateCreated = d;
        }
        if (this.dateModified == null) {
            this.dateModified = d;
        }

        /*
        if (StringUtils.isBlank(this.id)) {
            // let the app and not the db generate primary keys
            UUID uuid = uuidGenerator.generate();
            this.id = uuid.toString();
        }
        */
    }

    @PreUpdate
    public void preUpdate() {
        this.dateModified = new Date();
    }

    public static void flushSession()
    {
        entityManager().flush();
    }

    public static void clearSession()
    {
        entityManager().clear();
    }

}
