// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.MerchantOfferKeyword;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

privileged aspect MerchantOfferKeyword_Roo_Jpa_Entity {
    
    declare @type: MerchantOfferKeyword: @Entity;
    
    declare @type: MerchantOfferKeyword: @Table(name = "merchant_offer_keyword");
    
    declare @type: MerchantOfferKeyword: @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS);
    
}
