package com.lela.data.domain.finder;

import com.lela.data.domain.entity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 6/12/12
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/applicationContext.xml"})

public class AttributeTypeFinderTest {

    @Autowired
    AttributeTypeDataOnDemand attributeTypeDataOnDemand;

    @Autowired
    ItemAttributeDataOnDemand itemAttributeDataOnDemand;

    @Test
    public void testFindAllAttributeTypesForACategory()
    {
        Category category = itemAttributeDataOnDemand.getRandomItemAttribute().getItem().getCategory();
        List<AttributeType> results = AttributeType.findAllAttributeTypesForACategory(category);
        assertNotNull("Finder returned null results", results);
        assertTrue("Finder returned zero results", results.size()>0);
    }
}
