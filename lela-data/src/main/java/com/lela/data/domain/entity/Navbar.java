package com.lela.data.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "navbar")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "NavbarId", columnDefinition = "int(11)"))
public class Navbar extends AbstractEntity {

    @Column(name = "Rlnm", length = 255)
    private String rlnm;

    @Column(name = "IsDirty", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean dirty;

    @Column(name = "ObjectId", length = 24)
    private String objectId;

    @ManyToOne
    @JoinColumn(name = "LocaleId", nullable = false, referencedColumnName = "LocaleId")
    @ForeignKey(name = "FK_navbar_locale")
    @NotNull
    private Locale locale;

    @Column(name = "IsDefault", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean defaultNavbar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="AffiliateId", nullable = false, referencedColumnName = "AffiliateId")
    @ForeignKey(name = "FK_navbar_affiliate")
    @NotNull
    private Affiliate affiliate;
}
