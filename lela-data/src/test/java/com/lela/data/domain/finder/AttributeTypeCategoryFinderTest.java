package com.lela.data.domain.finder;

import com.lela.data.domain.entity.AttributeTypeCategory;
import com.lela.data.domain.entity.AttributeTypeCategoryDataOnDemand;
import com.lela.data.domain.entity.Item;
import com.lela.data.domain.entity.ItemDataOnDemand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 5/30/12
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/applicationContext.xml"})
public class AttributeTypeCategoryFinderTest {
    @Autowired
    AttributeTypeCategoryDataOnDemand attributeTypeCategoryDataOnDemand;

    @Test
    public void testFindAllAttributeTypesForACategory()
    {
        AttributeTypeCategory attributeTypeCategory = attributeTypeCategoryDataOnDemand.getRandomAttributeTypeCategory();
        List<AttributeTypeCategory> attributeTypeCategories = AttributeTypeCategory.findAllAttributeTypesCategoryByCategory(attributeTypeCategory.getCategory());
        assertNotNull("Finder returned null results", attributeTypeCategories);
        assertTrue("Finder returned zero results", attributeTypeCategories.size()>0);
    }
}
