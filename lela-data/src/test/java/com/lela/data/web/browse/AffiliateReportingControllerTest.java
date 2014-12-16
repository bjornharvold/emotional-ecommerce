package com.lela.data.web.browse;

import com.lela.data.domain.entity.AffiliateTransactionDataOnDemand;
import com.lela.data.web.AbstractControllerTest;
import com.lela.data.web.AffiliateReportingController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.util.List;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 6/8/12
 * Time: 8:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class AffiliateReportingControllerTest extends AbstractControllerTest {
    AffiliateReportingController affiliateReportingController = new AffiliateReportingController();

    @Autowired
    AffiliateTransactionDataOnDemand affiliateTransactionDataOnDemand;

    @Before
    public void before()
    {
        affiliateTransactionDataOnDemand.getRandomAffiliateTransaction();
    }

    @Test
    public void testIndex()
    {
       assertEquals("Index returned incorrect view", "affiliatereporting/index", affiliateReportingController.index());
    }

    @Test
    public void testList()
    {
       Model model = getModel();
       assertEquals("List returned incorrect view", "affiliatereporting/list", affiliateReportingController.list(model));
       assertNotNull("Result did not contain transactions", model.asMap().get("transactions"));
       assertTrue("Result contained 0 transactions", ((List)model.asMap().get("transactions")).size() > 0);
    }
}
