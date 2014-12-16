package com.lela.data.web.finder;

import com.lela.data.domain.entity.BrandDataOnDemand;
import com.lela.data.web.AbstractControllerTest;
import com.lela.data.web.BrandController;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 5/30/12
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class BrandControllerFinderTest extends AbstractControllerTest {
    BrandController brandController = new BrandController();

    @Autowired
    BrandDataOnDemand brandDataOnDemand;

    @Test
    public void testFindBrandsByBrandNameForm()
    {
        String result = brandController.findBrandsByBrandNameForm(getModel());
        assertEquals("Incorrect brand finder view returned.", "crud/brands/findBrandsByBrandName", result);
    }

    @Test
    public void testFindBrandsByBrandName()
    {
        String brandName = brandDataOnDemand.getRandomBrand().getBrandName();

        Model model = getModel();
        String result = brandController.findBrandsByBrandName(brandName, model);
        assertEquals("Incorrect brand finder result view returned.", "crud/brands/list", result);
        assertTrue("Brands not returned from finder.", model.containsAttribute("brands"));
    }
}
