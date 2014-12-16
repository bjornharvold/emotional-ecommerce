package com.lela.data.domain.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", table = "zip_code")
@RooSerializable
@AttributeOverride(name = "id", column = @Column(name = "ZipCodeId", columnDefinition = "int(11)"))
public class ZipCode extends AbstractEntity {

    @Column(name = "PostalCode", length = 11)
    @NotNull
    private String postalCode;

    @Column(name = "Population")
    private Integer population;

    @Column(name = "Household")
    private BigDecimal household;

    @Column(name = "White")
    private Integer white;

    @Column(name = "Black")
    private Integer black;

    @Column(name = "Hispanic")
    private Integer hispanic;

    @Column(name = "Asian")
    private Integer asian;

    @Column(name = "Hawaiian")
    private Integer hawaiian;

    @Column(name = "Indian")
    private Integer indian;

    @Column(name = "Other")
    private Integer other;

    @Column(name = "Male")
    private Integer male;

    @Column(name = "Female")
    private Integer female;

    @Column(name = "Income")
    private Integer income;

    @Column(name = "Age")
    private BigDecimal age;

    @Column(name = "AgeMale")
    private BigDecimal ageMale;

    @Column(name = "AgeFemale")
    private BigDecimal ageFemale;

    @Column(name = "Lat")
    private Double lat;

    @Column(name = "Lng")
    private Double lng;

    @Column(name = "State", length = 6)
    private String stateCode;

    @Column(name = "StateName", length = 20)
    private String stateName;

    @Column(name = "City", length = 255)
    private String city;

    @Column(name = "Cbsa")
    private Integer cbsa;

    @Column(name = "Region", length = 20)
    private String region;

    @Column(name = "Division", length = 20)
    private String division;

    @Column(name = "ObjectId", length = 24)
    private String objectId;

    @Column(name = "IsDirty", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean dirty;

}
