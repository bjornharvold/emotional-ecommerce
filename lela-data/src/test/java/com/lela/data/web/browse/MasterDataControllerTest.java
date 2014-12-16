package com.lela.data.web.browse;

import com.lela.data.web.AbstractControllerTest;
import com.lela.data.web.MasterDataController;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 6/8/12
 * Time: 2:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class MasterDataControllerTest extends AbstractControllerTest {

    MasterDataController masterDataController = new MasterDataController();

    @Test
    public void testIndex()
    {
        assertEquals("Returned incorrect view.", "masterdata/index", masterDataController.index());
    }
}
