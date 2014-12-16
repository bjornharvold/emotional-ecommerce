package com.lela.data.web.browse;

import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.data.domain.dto.ProductImageUpload;
import com.lela.data.domain.entity.*;
import com.lela.data.web.AbstractControllerTest;
import com.lela.data.web.ItemController;
import com.lela.util.UtilityException;
import com.lela.util.utilities.storage.FileStorage;
import com.lela.util.utilities.storage.dto.FileData;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.terracotta.license.util.IOUtils;

import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 5/30/12
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemControllerBrowseTest extends AbstractControllerTest {
    @Autowired
    ItemController itemController;

    @Autowired
    ItemDataOnDemand itemDataOnDemand;

    @Autowired
    CategoryDataOnDemand categoryDataOnDemand;

    @Autowired
    ProductImageItemDataOnDemand productImageItemDataOnDemand;

    @Autowired
    ImageSourceTypeDataOnDemand imageSourceTypeDataOnDemand;

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
     public void testListByCategory()
    {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        itemDataOnDemand.getRandomItem();
        String result = itemController.listByCategory(itemDataOnDemand.getRandomItem().getCategory().getId(), page, size, model);
        assertEquals("Incorrect result page returned.", "crud/items/list", result);

        page = null;
        size = 10;
        itemDataOnDemand.getRandomItem();
        result = itemController.listByCategory(itemDataOnDemand.getRandomItem().getCategory().getId(), page, size, model);
        assertEquals("Incorrect result page returned.", "crud/items/list", result);

        page = 1;
        size = null;
        itemDataOnDemand.getRandomItem();
        result = itemController.listByCategory(itemDataOnDemand.getRandomItem().getCategory().getId(), page, size, model);
        assertEquals("Incorrect result page returned.", "crud/items/list", result);

        page = null;
        size = null;
        itemDataOnDemand.getRandomItem();
        result = itemController.listByCategory(itemDataOnDemand.getRandomItem().getCategory().getId(), page, size, model);
        assertEquals("Incorrect result page returned.", "crud/items/list", result);
    }

    @Test(expected = RuntimeException.class)
    public void testListByCategoryNullCategoryId()
    {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        itemDataOnDemand.getRandomItem();
        String result = itemController.listByCategory(null, page, size, model);
    }

    @Test
    public void testlistAttributeByMotivatorAndCategory()
    {
        Model model = this.getModel();
        Long categoryId = itemDataOnDemand.getRandomItem().getCategory().getId();

        String result = itemController.listAttributeByMotivatorAndCategory(categoryId, model);
        assertEquals("Returned the wrong view.", "crud/items/attributes", result);
        assertNotNull("Did not find items.", model.asMap().get("items"));
        assertNotNull("Did not find motivator attributes.", model.asMap().get("motivators"));
        assertNotNull("Did not include category.", model.asMap().get("categoryId"));
    }

}
