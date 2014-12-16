package com.lela.data.domain.entity;

import org.hibernate.annotations.Index;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS")
@RooSerializable
@Table(name = "local_store", uniqueConstraints = {@UniqueConstraint(name = "UK_local_store_LelaLocalCode", columnNames={"LelaLocalCode"})})
@AttributeOverride(name = "id", column = @Column(name = "LocalStoreId", columnDefinition = "int(11)"))
public class LocalStore extends AbstractEntity {
    @Column(name = "MerchantName", length = 50)
    private String merchantName;

    @Column(name = "MerchantId")
    private Integer merchantId;

    @Column(name = "StoreNumber")
    private Integer storeNumber;

    @Column(name = "StoreName", length = 100)
    private String storeName;

    @Column(name = "Address", length = 100)
    private String address;

    @Column(name = "City", length = 50)
    private String city;

    @Column(name = "State", length = 15)
    private String state;

    @Column(name = "ZipCode", length = 10)
    private String zipCode;

    @Column(name = "PhoneNumber", length = 25)
    private String phoneNumber;

    @Column(name = "Latitude", length = 200)
    private String latitude;

    @Column(name = "Longitude", length = 200)
    private String longitude;

    @Column(name = "IsDirty", columnDefinition = "INT(11) NOT NULL DEFAULT 0")
    private Boolean dirty;

    @Column(name = "ObjectId", length = 24)
    private String objectId;

    @Column(name = "LelaLocalCode", length = 50)
    private String lelaLocalCode;
}
