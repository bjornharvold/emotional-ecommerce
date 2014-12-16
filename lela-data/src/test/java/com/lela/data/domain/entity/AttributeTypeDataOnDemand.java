package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@RooDataOnDemand(entity = AttributeType.class)
public class AttributeTypeDataOnDemand {
    @Autowired
    private ValidationTypeDataOnDemand validationTypeDataOnDemand2;

    @Autowired
    private AttributeTypeSourceDataOnDemand attributeTypeSourceDataOnDemand2;

    @Autowired
    private CleanseRuleDataOnDemand cleanseRuleDataOnDemand2;

    @Autowired
    private AccessoryTypeDataOnDemand accessoryTypeDataOnDemand;


    public AttributeType getNewTransientAttributeType(int index) {
        AttributeType obj = new AttributeType();
        setAccessory(obj, index);
        setAttributeName(obj, index);
        setAttributeSourceName(obj, index);
        setAttributeTypeSource(obj, index);
        setCleanseRule(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setLelaAttributeName(obj, index);
        setValidationType(obj, index);
        setAttributeTypeSource(obj, index);
        setAccessoryType(obj, index);
        return obj;
    }

    public void setValidationType(AttributeType obj, int index) {
        ValidationType validationType = validationTypeDataOnDemand2.getRandomValidationType();
        obj.setValidationType(validationType);
    }

    public void setAttributeTypeSource(AttributeType obj, int index) {
        AttributeTypeSource attributeTypeSource = attributeTypeSourceDataOnDemand2.getRandomAttributeTypeSource();
        obj.setAttributeTypeSource(attributeTypeSource);
    }

    public void setCleanseRule(AttributeType obj, int index) {
        CleanseRule cleanseRule = cleanseRuleDataOnDemand2.getRandomCleanseRule();
        obj.setCleanseRule(cleanseRule);
    }

    public void setAccessoryType(AttributeType obj, int index) {
        AccessoryType accessoryType = accessoryTypeDataOnDemand.getRandomAccessoryType();
        obj.setAccessoryType(accessoryType);
    }


}
