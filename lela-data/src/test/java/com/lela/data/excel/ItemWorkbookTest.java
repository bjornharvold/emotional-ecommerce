package com.lela.data.excel;

import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.data.domain.dto.ItemSearchCommand;
import com.lela.data.domain.entity.*;
import com.lela.data.enums.ItemReportingAttribute;
import com.lela.data.excel.command.ItemWorkbookCommand;
import com.lela.data.web.commands.ItemReportingCommand;
import com.lela.ingest.test.AbstractFunctionalTest;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/2/12
 * Time: 2:22 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"/META-INF/spring/services.xml",
		"/META-INF/spring-test/applicationContext-test.xml",
		"/META-INF/spring/test.xml",
        "/META-INF/spring/webmvc-config.xml",
        "/META-INF/spring/webmvc-security.xml",
        "/META-INF/spring/mocks.xml",
        "/META-INF/spring/disable-bootstrap.xml"
})
public class ItemWorkbookTest {

  @Autowired
  CategoryDataOnDemand categoryDataOnDemand;

  @Autowired
  ItemDataOnDemand itemDataOnDemand;

  @Autowired
  MerchantOfferDataOnDemand merchantOfferDataOnDemand;

  @Autowired
  ItemRecallDataOnDemand itemRecallDataOnDemand;

  @Autowired
  AttributeTypeCategoryDataOnDemand attributeTypeCategoryDataOnDemand;

  @Autowired
  private ApplicationContext applicationContext;

  @Before
  public void setUp()
  {
      SpringSecurityHelper.secureChannel();
  }

  @After
  public void tearDown()
  {
      SpringSecurityHelper.unsecureChannel();
  }

  @Test
  @Transactional
  public void testGenerateWorkbookOfItems()
  {
      Item item = itemDataOnDemand.getRandomItem();

      ItemWorkbookCommand itemWorkbookCommand = new ItemWorkbookCommand();
      ItemSearchCommand itemSearchCommand = new ItemSearchCommand();
      itemSearchCommand.setCategoryId(item.getCategory().getId());
      itemWorkbookCommand.setShowAttribute(ItemReportingAttribute.NONE);

      ItemWorkbook itemWorkbook = (ItemWorkbook)applicationContext.getBean("itemWorkbook");
      ItemReportingCommand itemReportingCommand = new ItemReportingCommand();
      itemReportingCommand.setItemSearchCommand(itemSearchCommand);
      itemReportingCommand.setItemWorkbookCommand(itemWorkbookCommand);
      itemWorkbook.setItemReportingCommand(itemReportingCommand);

      try {
          Workbook wb = itemWorkbook.generate();

          ByteArrayOutputStream out = new ByteArrayOutputStream();
          itemWorkbook.writeCached(out);
          assertTrue("file is empty", out.toByteArray().length > 0);

      } catch (FileNotFoundException e) {
          fail(e.getMessage());
          e.printStackTrace();
      } catch (IOException e) {
          fail(e.getMessage());
          e.printStackTrace();
      }
  }

    @Test
    @Transactional
    public void testGenerateWorkbookOfItemsWithAttributes()
    {
        Item item = itemDataOnDemand.getRandomItem();
        AttributeTypeCategory attributeTypeCategory = attributeTypeCategoryDataOnDemand.getRandomAttributeTypeCategory();

        List<AttributeTypeCategory> attributeTypeCategories = AttributeTypeCategory.findAllAttributeTypesCategoryByCategory(item.getCategory());

        ItemWorkbookCommand itemWorkbookCommand = new ItemWorkbookCommand();
        itemWorkbookCommand.setShowAttribute(ItemReportingAttribute.ALL);
        ItemSearchCommand itemSearchCommand = new ItemSearchCommand();
        itemSearchCommand.setCategoryId(item.getCategory().getId());

        ItemWorkbook itemWorkbook = (ItemWorkbook)applicationContext.getBean("itemWorkbook");
        ItemReportingCommand itemReportingCommand = new ItemReportingCommand();
        itemReportingCommand.setItemSearchCommand(itemSearchCommand);
        itemReportingCommand.setItemWorkbookCommand(itemWorkbookCommand);
        itemWorkbook.setItemReportingCommand(itemReportingCommand);

        itemWorkbook.setAttributeTypeCategories(attributeTypeCategories);

        try {
            Workbook wb = itemWorkbook.generate();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            itemWorkbook.writeCached(out);
            assertTrue("file is empty", out.toByteArray().length > 0);

        } catch (FileNotFoundException e) {
            fail(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    public void testGenerateWorkbookOfItemsWithMerchantOffers()
    {
        MerchantOffer merchantOffer = merchantOfferDataOnDemand.getRandomMerchantOffer();

        List<AttributeTypeCategory> attributeTypeCategories = AttributeTypeCategory.findAllAttributeTypesCategoryByCategory(merchantOffer.getItem().getCategory());

        ItemWorkbookCommand itemWorkbookCommand = new ItemWorkbookCommand();
        itemWorkbookCommand.setShowMerchantOffers(true);
        itemWorkbookCommand.setShowAttribute(ItemReportingAttribute.NONE);
        ItemSearchCommand itemSearchCommand = new ItemSearchCommand();
        itemSearchCommand.setCategoryId(merchantOffer.getItem().getCategory().getId());

        ItemWorkbook itemWorkbook = (ItemWorkbook)applicationContext.getBean("itemWorkbook");
        ItemReportingCommand itemReportingCommand = new ItemReportingCommand();
        itemReportingCommand.setItemSearchCommand(itemSearchCommand);
        itemReportingCommand.setItemWorkbookCommand(itemWorkbookCommand);
        itemWorkbook.setItemReportingCommand(itemReportingCommand);

        try {
            Workbook wb = itemWorkbook.generate();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            itemWorkbook.writeCached(out);
            assertTrue("file is empty", out.toByteArray().length > 0);

        } catch (FileNotFoundException e) {
            fail(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    public void testGenerateWorkbookOfItemsWithRecalls()
    {
        ItemRecall itemRecall = itemRecallDataOnDemand.getRandomItemRecall();

        List<AttributeTypeCategory> attributeTypeCategories = AttributeTypeCategory.findAllAttributeTypesCategoryByCategory(itemRecall.getItem().getCategory());

        ItemWorkbookCommand itemWorkbookCommand = new ItemWorkbookCommand();
        itemWorkbookCommand.setShowItemRecalls(true);
        itemWorkbookCommand.setShowAttribute(ItemReportingAttribute.NONE);
        ItemSearchCommand itemSearchCommand = new ItemSearchCommand();
        itemSearchCommand.setCategoryId(itemRecall.getItem().getCategory().getId());

        ItemWorkbook itemWorkbook = (ItemWorkbook)applicationContext.getBean("itemWorkbook");
        ItemReportingCommand itemReportingCommand = new ItemReportingCommand();
        itemReportingCommand.setItemSearchCommand(itemSearchCommand);
        itemReportingCommand.setItemWorkbookCommand(itemWorkbookCommand);
        itemWorkbook.setItemReportingCommand(itemReportingCommand);

        try {
            Workbook wb = itemWorkbook.generate();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            itemWorkbook.writeCached(out);
            assertTrue("file is empty", out.toByteArray().length > 0);

        } catch (FileNotFoundException e) {
            fail(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    public void testGenerateWorkbookOfItemsWithEverything()
    {
        Item item = itemDataOnDemand.getRandomItem();

        List<AttributeTypeCategory> attributeTypeCategories = AttributeTypeCategory.findAllAttributeTypesCategoryByCategory(item.getCategory());

        ItemWorkbookCommand itemWorkbookCommand = new ItemWorkbookCommand();
        itemWorkbookCommand.setShowAttribute(ItemReportingAttribute.ALL);
        ItemSearchCommand itemSearchCommand = new ItemSearchCommand();
        itemSearchCommand.setCategoryId(item.getCategory().getId());

        ItemWorkbook itemWorkbook = (ItemWorkbook)applicationContext.getBean("itemWorkbook");
        ItemReportingCommand itemReportingCommand = new ItemReportingCommand();
        itemReportingCommand.setItemSearchCommand(itemSearchCommand);
        itemReportingCommand.setItemWorkbookCommand(itemWorkbookCommand);
        itemWorkbook.setItemReportingCommand(itemReportingCommand);

        itemWorkbook.setAttributeTypeCategories(attributeTypeCategories);
        try {
            Workbook wb = itemWorkbook.generate();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            itemWorkbook.writeCached(out);
            assertTrue("file is empty", out.toByteArray().length > 0);

        } catch (FileNotFoundException e) {
            fail(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    public void testGenerateWorkbookOfItemsWithFunctionalFilters()
    {
        Item item = itemDataOnDemand.getRandomItem();

        List<AttributeTypeCategory> attributeTypeCategories = AttributeTypeCategory.findAllAttributeTypesCategoryByCategory(item.getCategory());

        ItemWorkbookCommand itemWorkbookCommand = new ItemWorkbookCommand();
        itemWorkbookCommand.setShowAttribute(ItemReportingAttribute.FUNCTIONAL_FILTERS);
        ItemSearchCommand itemSearchCommand = new ItemSearchCommand();
        itemSearchCommand.setCategoryId(item.getCategory().getId());
        List<FunctionalFilter> functionalFilters = FunctionalFilterCategory.findByItemSearchCommand(itemSearchCommand);

        ItemWorkbook itemWorkbook = (ItemWorkbook)applicationContext.getBean("itemWorkbook");
        ItemReportingCommand itemReportingCommand = new ItemReportingCommand();
        itemReportingCommand.setItemSearchCommand(itemSearchCommand);
        itemReportingCommand.setItemWorkbookCommand(itemWorkbookCommand);
        itemWorkbook.setItemReportingCommand(itemReportingCommand);

        itemWorkbook.setFunctionalFilters(functionalFilters);


        itemWorkbook.setAttributeTypeCategories(attributeTypeCategories);
        try {
            Workbook wb = itemWorkbook.generate();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            itemWorkbook.writeCached(out);
            assertTrue("file is empty", out.toByteArray().length > 0);

        } catch (FileNotFoundException e) {
            fail(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    public void testGenerateWorkbookOfItemsWithProductDetails()
    {
        Item item = itemDataOnDemand.getRandomItem();

        List<AttributeTypeCategory> attributeTypeCategories = AttributeTypeCategory.findAllAttributeTypesCategoryByCategory(item.getCategory());

        ItemWorkbookCommand itemWorkbookCommand = new ItemWorkbookCommand();
        itemWorkbookCommand.setShowAttribute(ItemReportingAttribute.PRODUCT_DETAILS);
        ItemSearchCommand itemSearchCommand = new ItemSearchCommand();
        itemSearchCommand.setCategoryId(item.getCategory().getId());
        List<ProductDetailSection> sections = ProductDetailSection.findByCategory(item.getCategory());

        ItemWorkbook itemWorkbook = (ItemWorkbook)applicationContext.getBean("itemWorkbook");
        ItemReportingCommand itemReportingCommand = new ItemReportingCommand();
        itemReportingCommand.setItemSearchCommand(itemSearchCommand);
        itemReportingCommand.setItemWorkbookCommand(itemWorkbookCommand);
        itemWorkbook.setItemReportingCommand(itemReportingCommand);

        itemWorkbook.setProductDetailSection(sections);


        itemWorkbook.setAttributeTypeCategories(attributeTypeCategories);
        try {
            Workbook wb = itemWorkbook.generate();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            itemWorkbook.writeCached(out);
            assertTrue("file is empty", out.toByteArray().length > 0);

        } catch (FileNotFoundException e) {
            fail(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }
}