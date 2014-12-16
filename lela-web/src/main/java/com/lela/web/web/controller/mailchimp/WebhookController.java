package com.lela.web.web.controller.mailchimp;

import java.io.StringWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mailjimp.dom.MailJimpConstants;
import mailjimp.dom.response.list.MemberInfo;
import mailjimp.service.MailJimpException;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.lela.web.web.mailchimp.IWebHookAdapter;
import com.lela.web.web.mailchimp.WebHookData;
import com.lela.web.web.mailchimp.WebHookType;

/**
 * Copied from 
 * https://github.com/limone/MailJimp/tree/master/mailjimp-webhook/src/main/java/mailjimp/webhook
 * and modified
 * 
 * @author pankajtandon
 *
 */
@Controller
@RequestMapping(value = "/mailchimp")
public class WebhookController {


	private static final Logger LOG = LoggerFactory.getLogger(WebhookController.class);

	  private static Pattern DATA_PATTERN = Pattern.compile("data\\[(\\w+)\\](\\[(\\w+)\\](\\[(\\d+)\\]\\[(\\w+)\\])?)?");

	  private static int INDEX_PARAM_NAME = 1;
	  private static int INDEX_MAPPED_PARAM_NAME = 3;
	  private static int INDEX_MAPPED_ARRAY_INDEX = 5;
	  private static int INDEX_MAPPED_ARRAY_PARAM_NAME = 6;

	  @Autowired
	  private IWebHookAdapter webHookAdapter;

	  private final ObjectMapper m = new ObjectMapper();
	  
	  public WebhookController() {
	    // empty
	  }

	  /**
	* WebHook for the subscribe callbacks. This will call
	* {@link IWebHookAdapter#userSubscribed(mailjimp.dom.WebHookData)}.
	*
	* @param request
	* The request containing all the data.
	*
	* @return A simple string MailChimp doesn't care what the answer looks like
	* as long as it's a 2xx status code.
	*
	* @throws Exception
	* if parsing fails.
	*/
	  @RequestMapping(params = "type=subscribe")
	  public @ResponseBody
	  String subscribe(WebRequest request) throws Exception {
	    LOG.info("Subscribe...");
	    try {
	      WebHookData data = new WebHookData(WebHookType.SUBSCRIBE);
	      webHookAdapter.userSubscribed(buildData(request, data));
	    } catch (Exception e) {
	      LOG.error("Error while processing request.", e);
	    }
	    return "Copy that!";
	  }

	  /**
	* WebHook for the unsubscribe callbacks. This will call
	* {@link IWebHookAdapter#userUnsubscribed(mailjimp.dom.WebHookData)}.
	*
	* @param request
	* The request containing all the data.
	*
	* @return A simple string MailChimp doesn't care what the answer looks like
	* as long as it's a 2xx status code.
	*
	* @throws Exception
	* if parsing fails.
	*/
	  @RequestMapping(value="/unsubscribe")
	  public @ResponseBody
	  String unsubscribe(WebRequest request) throws Exception {
	    LOG.info("Unsubscribe...");
	    try {
	      WebHookData data = new WebHookData(WebHookType.UNSUBSCRIBE);
	      webHookAdapter.userUnsubscribed(buildData(request, data));
	    } catch (Exception e) {
	      LOG.error("Error while processing request.", e);
	    }
	    return "Ten four!";
	  }

	  /**
	* WebHook for the subscribe callbacks. This will call
	* {@link IWebHookAdapter#userSubscribed(mailjimp.dom.WebHookData)}.
	*
	* @param request
	* The request containing all the data.
	*
	* @return A simple string MailChimp doesn't care what the answer looks like
	* as long as it's a 2xx status code.
	*
	* @throws Exception
	* if parsing fails.
	*/
	  @RequestMapping(params = "type=profile")
	  public @ResponseBody
	  String profile(WebRequest request) throws Exception {
	    LOG.info("Profile updated...");
	    try {
	      WebHookData data = new WebHookData(WebHookType.UPDATE_PROFILE);
	      webHookAdapter.profileUpdated(buildData(request, data));
	    } catch (Exception e) {
	      LOG.error("Error while processing request.", e);
	    }
	    return "Roger that!";
	  }

	  /**
	* WebHook for the subscribe callbacks. This will call
	* {@link IWebHookAdapter#userSubscribed(mailjimp.dom.WebHookData)}.
	*
	* @param request
	* The request containing all the data.
	*
	* @return A simple string MailChimp doesn't care what the answer looks like
	* as long as it's a 2xx status code.
	*
	* @throws Exception
	* if parsing fails.
	*/
	  @RequestMapping(params = "type=upemail")
	  public @ResponseBody
	  String upemail(WebRequest request) throws Exception {
	    LOG.info("E-Mail updated...");
	    try {
	      WebHookData data = new WebHookData(WebHookType.UPDATE_EMAIL);
	      webHookAdapter.eMailUpdated(buildData(request, data));
	    } catch (Exception e) {
	      LOG.error("Error while processing request.", e);
	    }
	    return "Understood!";
	  }

	  /**
	* WebHook for the subscribe callbacks. This will call
	* {@link IWebHookAdapter#userSubscribed(mailjimp.dom.WebHookData)}.
	*
	* @param request
	* The request containing all the data.
	*
	* @return A simple string MailChimp doesn't care what the answer looks like
	* as long as it's a 2xx status code.
	*
	* @throws Exception
	* if parsing fails.
	*/
	  @RequestMapping(params = "type=cleaned")
	  public @ResponseBody
	  String cleaned(WebRequest request) throws Exception {
	    LOG.info("Account cleaned...");
	    try {
	      WebHookData data = new WebHookData(WebHookType.CLEANED);
	      webHookAdapter.cleaned(buildData(request, data));
	    } catch (Exception e) {
	      LOG.error("Error while processing request.", e);
	    }

	    return "OK!";
	  }

	  private WebHookData buildData(WebRequest request, final WebHookData data) throws ParseException, MailJimpException {
	    data.setFiredAt(MailJimpConstants.SDF.parse(request.getParameter("fired_at")));
	    data.setRawData(parseRequest(request));
	    if (WebHookType.SUBSCRIBE == data.getType() || WebHookType.UNSUBSCRIBE == data.getType() || WebHookType.UPDATE_PROFILE == data.getType()) {
	      try {
	        final StringWriter json = new StringWriter();
	        m.writeValue(json, data.getRawData());
	        LOG.debug("JSON: {}", json.toString());
	        MemberInfo val = m.readValue(json.toString(), MemberInfo.class);
	        data.setMemberInfo(val);
	      } catch (Exception ex) {
	        LOG.error("Could not convert data to MemberInfo object.", ex);
	        throw new MailJimpException("Could not convert data to MemberInfo object.", ex);
	      }
	    }
	    return data;
	  }

	  // Watch out! This is going to be dirty! You have been warned.
	  @SuppressWarnings("unchecked")
	  private Map<String, Object> parseRequest(WebRequest request) {
	    Map<String, String[]> parameterMap = request.getParameterMap();
	    Map<String, Object> convertible = new HashMap<String, Object>();
	    for (String parameterKey : parameterMap.keySet()) {
	      Matcher matcher = DATA_PATTERN.matcher(parameterKey);
	      if (matcher.matches()) {
	        String key = matcher.group(INDEX_PARAM_NAME);
	        Object value = parameterMap.get(parameterKey)[0];
	        if (null == matcher.group(INDEX_MAPPED_PARAM_NAME)) {
	          // simple values
	          convertible.put(key, value);
	        } else if (null == matcher.group(INDEX_MAPPED_ARRAY_INDEX)) {
	          // mapped values
	          Map<String, Object> map = getMapParam(convertible, key);
	          map.put(matcher.group(INDEX_MAPPED_PARAM_NAME), value);
	        } else {
	          // merge values in an array
	          // If you like good coding don't read on!
	          Map<String, Object> map = getMapParam(convertible, key);
	          // now check for the array
	          String arrayKey = matcher.group(INDEX_MAPPED_PARAM_NAME);
	          int arrayIndex = Integer.valueOf(matcher.group(INDEX_MAPPED_ARRAY_INDEX));
	          if (null == map.get(arrayKey)) {
	            map.put(arrayKey, new Object[] {});
	          }
	          Object[] array = (Object[]) map.get(arrayKey);
	          while (array.length <= arrayIndex) {
	            Object[] newArray = new Object[array.length + 1];
	            System.arraycopy(array, 0, newArray, 0, array.length);
	            array = newArray;
	          }
	          if (null == array[arrayIndex]) {
	            array[arrayIndex] = new HashMap<String, Object>();
	          }
	          map.put(arrayKey, array);
	          // noinspection unchecked
	          ((Map<String, Object>) array[arrayIndex]).put(matcher.group(INDEX_MAPPED_ARRAY_PARAM_NAME), value);
	        }
	      }
	    }
	    return convertible;
	  }

	  @SuppressWarnings({ "unchecked" })
	  private Map<String, Object> getMapParam(Map<String, Object> convertible, String key) {
	    if (!convertible.containsKey(key)) {
	      convertible.put(key, new HashMap<String, Object>());
	    }
	    return (Map<String, Object>) convertible.get(key);
	  }
}
