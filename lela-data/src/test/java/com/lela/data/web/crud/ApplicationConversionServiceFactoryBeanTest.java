package com.lela.data.web.crud;

import com.lela.data.domain.entity.*;
import com.lela.data.web.ApplicationConversionServiceFactoryBean;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 5/30/12
 * Time: 1:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationConversionServiceFactoryBeanTest {
    ApplicationConversionServiceFactoryBean conversion = new ApplicationConversionServiceFactoryBean();

    @Test
    public void testGetNetworkToStringConverter() {
        Network network = new Network();
        network.setNetworkName("VALUE");
        String result = conversion.getNetworkToStringConverter().convert(network);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
        
    }

    @Test
    public void testGetMerchantSourceToStringConverter() {
        MerchantSource merchantSource = new MerchantSource();
        merchantSource.setSourceName("VALUE");
        String result = conversion.getMerchantSourceToStringConverter().convert(merchantSource);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
    }

    @Test
    public void testGetMerchantToStringConverter() {
        Merchant merchant = new Merchant();
        merchant.setMerchantName("VALUE");
        String result = conversion.getMerchantToStringConverter().convert(merchant);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
    }

    @Test
    public void testGetMerchantSourceTypeToStringConverter() {
        MerchantSourceType merchantSourceType = new MerchantSourceType();
        merchantSourceType.setSourceTypeName("VALUE");
        String result = conversion.getMerchantSourceTypeToStringConverter().convert(merchantSourceType);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
    }

    @Test
    public void testGetMerchantOfferToStringConverter() {
        MerchantOffer merchantOffer = new MerchantOffer();
        merchantOffer.setOfferItemName("VALUE");
        String result = conversion.getMerchantOfferToStringConverter().convert(merchantOffer);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
    }

    @Test
    public void testGetInvestigationStatusToStringConverter() {
        ReviewStatus reviewStatus = new ReviewStatus();
        reviewStatus.setReviewStatusName("VALUE");
        String result = conversion.getReviewStatusToStringConverter().convert(reviewStatus);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
    }

    @Test
    public void testGetConditionTypeToStringConverter() {
        ConditionType conditionType = new ConditionType();
        conditionType.setConditionTypeName("VALUE");
        String result = conversion.getConditionTypeToStringConverter().convert(conditionType);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
    }

    @Test
    public void testGetBrandToStringConverter() {
        Brand brand = new Brand();
        brand.setBrandName("VALUE");
        String result = conversion.getBrandToStringConverter().convert(brand);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
    }

    @Test
    public void testGetManufacturerToStringConverter() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerName("VALUE");
        String result = conversion.getManufacturerToStringConverter().convert(manufacturer);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);

    }

    public void testGetInputTypeToStringConverter() {
        InputType inputType = new InputType();
        inputType.setInputTypeName("VALUE");
        String result = conversion.getInputTypeToStringConverter().convert(inputType);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
    }

    public void testGetMultiValuedTypeToStringConverter() {
        MultiValuedType multiValuedType = new MultiValuedType();
        multiValuedType.setMultiValuedTypeName("VALUE");
        String result = conversion.getMultiValuedTypeToStringConverter().convert(multiValuedType);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
    }

    public void testGetAttributeTypeToStringConverter() {
        AttributeType attributeType = new AttributeType();
        attributeType.setAttributeName("VALUE");
        String result = conversion.getAttributeTypeToStringConverter().convert(attributeType);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
    }

    public void testGetItemToStringConverter() {
        Item item = new Item();
        item.setLelaUrl("VALUE");
        String result = conversion.getItemToStringConverter().convert(item);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);

    }

    public void testGetColorToStringConverter() {
        Color color = new Color();
        color.setColorName("VALUE");
        String result = conversion.getColorToStringConverter().convert(color);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);

    }

    public void testGetDataSourceTypeToStringConverter() {
        DataSourceType dataSourceType = new DataSourceType();
        dataSourceType.setDataSourceTypeName("VALUE");
        String result = conversion.getDataSourceTypeToStringConverter().convert(dataSourceType);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
    }

    public void testGetItemAttributeToStringConverter() {
        ItemAttribute itemAttribute = new ItemAttribute();
        //test that it can handle nulls
        String result = conversion.getItemAttributeToStringConverter().convert(itemAttribute);
        assertEquals("Converter provided the incorrect result.", "", result);

        AttributeType attributeType = new AttributeType();
        attributeType.setAttributeName("NAME");
        itemAttribute.setAttributeType(attributeType);
        itemAttribute.setAttributeValue("VALUE");
        result = conversion.getItemAttributeToStringConverter().convert(itemAttribute);
        assertEquals("Converter provided the incorrect result.", "NAME = VALUE", result);

    }

    public void testGetCategoryToStringConverter() {
        Category category = new Category();
        category.setCategoryName("VALUE");
        String result = conversion.getCategoryToStringConverter().convert(category);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
    }

    public void testGetAttributeTypeSourceToStringConverter() {
        AttributeType attributeType = new AttributeType();
        attributeType.setAttributeName("VALUE");
        String result = conversion.getAttributeTypeToStringConverter().convert(attributeType);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
    }

    public void testGetValidationTypeToStringConverter() {
        ValidationType validationType = new ValidationType();
        validationType.setValidationTypeName("VALUE");
        String result = conversion.getValidationTypeToStringConverter().convert(validationType);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
    }

    public void testGetCategoryCategoryGroupToStringConverter() {
        CategoryCategoryGroup categoryCategoryGroup = new CategoryCategoryGroup();
        String result = conversion.getCategoryCategoryGroupToStringConverter().convert(categoryCategoryGroup);
        assertEquals("Converter provided the incorrect result.", "", result);
    }

    public void testGetFunctionalFilterAnswerToStringConverter() {
        FunctionalFilterAnswer functionalFilterAnswer = new FunctionalFilterAnswer();
        functionalFilterAnswer.setAnswerKey("KEY");
        functionalFilterAnswer.setAnswerValue("VALUE");
        String result = conversion.getFunctionalFilterAnswerToStringConverter().convert(functionalFilterAnswer);
        assertEquals("Converter provided the incorrect result.", "KEY VALUE", result);
    }

    public void testGetFunctionalFilterTypeToStringConverter() {
        FunctionalFilterType functionalFilterType = new FunctionalFilterType();
        functionalFilterType.setFunctionalFilterTypeName("VALUE");
        String result = conversion.getFunctionalFilterTypeToStringConverter().convert(functionalFilterType);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
    }

    public void testGetFunctionalFilterToStringConverter() {
        FunctionalFilter functionalFilter = new FunctionalFilter();
        functionalFilter.setFunctionalFilterName("VALUE");
        String result = conversion.getFunctionalFilterToStringConverter().convert(functionalFilter);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
    }

    public void testGetLocaleToStringConverter() {
        Locale locale = new Locale();
        locale.setLocaleName("VALUE");
        String result = conversion.getLocaleToStringConverter().convert(locale);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
    }

    public void testGetCleanseRuleToStringConverter() {
        CleanseRule cleanseRule = new CleanseRule();
        cleanseRule.setCleanseRuleValue("VALUE");
        String result = conversion.getCleanseRuleToStringConverter().convert(cleanseRule);
        assertEquals("Converter provided the incorrect result.", "VALUE", result);
    }
}
