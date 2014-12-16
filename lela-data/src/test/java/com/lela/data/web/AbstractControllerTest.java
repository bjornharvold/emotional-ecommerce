package com.lela.data.web;

import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 5/25/12
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={"/META-INF/spring/applicationContext.xml"})
@ContextConfiguration( locations={"/META-INF/spring/applicationContext.xml","/META-INF/spring/webmvc-component-scan.xml","/META-INF/spring/webmvc-config-test.xml", "/META-INF/spring/mocks.xml"})
public abstract class AbstractControllerTest extends CommonControllerTest{



}
