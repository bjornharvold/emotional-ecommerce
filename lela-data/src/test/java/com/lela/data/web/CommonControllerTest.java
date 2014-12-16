package com.lela.data.web;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 9/12/12
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommonControllerTest
{
        protected BindingResult getBindingResultErrors() {
            BindingResult results = mock(BindingResult.class);
            when(results.hasErrors()).thenReturn(true);
            return results;
        }

        protected BindingResult getBindingResult() {
            BindingResult results = mock(BindingResult.class);
            when(results.hasErrors()).thenReturn(false);
            return results;
        }

        protected BindingAwareModelMap getModel() {
            return new BindingAwareModelMap();
        }

        protected MockHttpServletRequest getHttpServletRequest() {
            return new MockHttpServletRequest();
        }

        protected MockHttpServletResponse getHttpServletResponse()
        {
            return new MockHttpServletResponse();
        }
}
