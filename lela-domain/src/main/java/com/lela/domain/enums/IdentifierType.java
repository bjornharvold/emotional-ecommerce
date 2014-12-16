package com.lela.domain.enums;

public enum IdentifierType {
    UPC("upc"),
    MPN("mpn"),
    ASIN("asin");
    
    private String value;
    
    
    private  IdentifierType(String value){
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
