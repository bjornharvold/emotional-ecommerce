/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.util.utilities.mixpanel;

import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.Manufacturer;
import nl.bitwalker.useragentutils.OperatingSystem;
import nl.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * This class mimics the request information functionality in the Mixpanel Javascript Libary http://cdn.mxpnl.com/libs/mixpanel-2.1.js
 *
 * User: Chris Tallent
 * Date: 10/23/12
 * Time: 3:39 PM
 */
public class RequestUtils {

    private static final Logger log = LoggerFactory.getLogger(RequestUtils.class);

    private static final String USER_AGENT = "User-Agent";
    private static final String REFERRER = "Referer";

    private static final String[] CAMPAIGN_KEYWORDS = { "utm_source", "utm_medium", "utm_campaign", "utm_content", "utm_term" };

    public static Map<String, Object> getCampaignParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();

        for (String kwkey : CAMPAIGN_KEYWORDS) {
            String kw = request.getParameter(kwkey);
            if (kw != null && kw.length() > 0) {
                params.put(kwkey, kw);
            }
        }

        return params;
    }

    public static String getSearchEngine(HttpServletRequest request) {
        if (request != null) {
            String referrer = request.getHeader(REFERRER);
            if (referrer != null) {
                if (referrer.matches("https?://(.*)google.([^/?]*)")) {
                    return "google";
                } else if (referrer.matches("https?://(.*)bing.com")) {
                    return "bing";
                } else if (referrer.matches("https?://(.*)yahoo.com")) {
                    return "yahoo";
                } else if (referrer.matches("https?://(.*)duckduckgo.com")) {
                    return "duckduckgo";
                }
            }
        }

        return null;
    }

    public static String getSearchKeyword(HttpServletRequest request) {
        if (request != null) {
            String referrer = request.getHeader(REFERRER);
            String search = getSearchEngine(request);
            String param = (search != null && search != "yahoo") ? "q" : "p";

            if (referrer != null && search != null) {
                String keyword = getQueryParam(referrer, param);
                if (keyword != null && keyword.length() > 0) {
                    return keyword;
                }
            }
        }
        return null;
    }

    public static Map<String, Object> getSearchInfo(HttpServletRequest request) {
        Map<String, Object> ret = new HashMap<String, Object>();

        if (request != null) {
            String referrer = request.getHeader(REFERRER);
            String search = getSearchEngine(request);
            String param = (search != null && search != "yahoo") ? "q" : "p";

            if (referrer != null && search != null) {
                ret.put("$search_engine", search);

                String keyword = getQueryParam(referrer, param);
                if (keyword != null && keyword.length() > 0) {
                    ret.put("mp_keyword", keyword);
                }
            }
        }

        return ret;
    }


    /**
     * This function detects which browser is running this script.
     * The order of the checks are important since many user agents
     * include key words used in later checks.
     */
    public static String getBrowser(HttpServletRequest request) {
        if (request != null) {
            String ua = request.getHeader(USER_AGENT);
            if (ua != null) {
                UserAgent userAgent = new UserAgent(ua);


                // If the user agent browser name includes a version, return the browser group (e.g. - Firefox)
                if (userAgent.getBrowser().getName().matches(".*\\d.*")) {
                    return userAgent.getBrowser().getGroup().getName();
                } else if (Manufacturer.BLACKBERRY.equals(userAgent.getOperatingSystem().getManufacturer())) {
                    return "BlackBerry";
                } else {
                    return userAgent.getBrowser().getName();
                }
            }
        }

        return null;
    }

    /**
     * This function detects which browser is running this script.
     * The order of the checks are important since many user agents
     * include key words used in later checks.
     */
    public static boolean isBot(HttpServletRequest request) {
        if (request != null) {
            String ua = request.getHeader(USER_AGENT);
            if (StringUtils.isNotBlank(ua)) {
                UserAgent userAgent = new UserAgent(ua);
                //return true if it's definitely a bot
                if (userAgent.getBrowser().equals(Browser.BOT))
                    return true;
                else if (StringUtils.containsIgnoreCase(ua, "chartbeat") || StringUtils.containsIgnoreCase(ua, "Health"))
                    return true;
                return false;
            }
            //The user-agent is blank, this is not a legitimate browser
            else{
                return true;
            }
        }
        else
        {
            //there's no request at all so it's internal, therefore it is not a bot
            return false;
        }
    }

    public static String getOs(HttpServletRequest request) {
        if (request != null) {
            String a = request.getHeader(USER_AGENT);
            if (a != null) {
                if (a.toLowerCase().contains("windows")) {
                    if (a.contains("Phone")) { return "Windows Mobile"; }
                    return "Windows";
                } else if (a.matches(".*(iPhone|iPad|iPod).*")) {
                    return "iOS";
                } else if (a.contains("Android")) {
                    return "Android";
                } else if (a.toLowerCase().matches(".*(blackberry|playbook|bb10).*")) {
                    return "BlackBerry";
                } else if (a.toLowerCase().contains("mac")) {
                    return "Mac OS X";
                } else if (a.contains("Linux")) {
                    return "Linux";
                }
            }
        }

        return null;
    }

    public static String getDevice(HttpServletRequest request) {
        if (request != null) {
            String a = request.getHeader(USER_AGENT);
            if (a != null) {
                if (a.contains("iPhone")) {
                    return "iPhone";
                } else if (a.contains("iPad")) {
                    return "iPad";
                } else if (a.contains("iPod")) {
                    return "iPod Touch";
                } else if (a.toLowerCase().matches(".*(blackberry|playbook|bb10).*")) {
                    return "BlackBerry";
                } else if (a.toLowerCase().contains("windows phone")) {
                    return "Windows Phone";
                } else if (a.contains("Android")) {
                    return "Android";
                }
            }
        }

        return null;
    }

    public static String getReferrer(HttpServletRequest request) {
        if (request != null) {
            return request.getHeader(REFERRER);
        }

        return null;
    }

    public static String getReferringDomain(HttpServletRequest request) {
        if (request != null) {
            String referrer = request.getHeader(REFERRER);
            if (referrer != null) {
                String[] split = referrer.split("/");
                if (split.length >= 3) {
                    return split[2];
                }
            }
        }

        return null;
    }

    /**
     * These represent properties sent with every tracking event by the JS library
     */
    public static Map<String, Object> getProperties(HttpServletRequest request) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (request != null) {
            ret.put("$os", getOs(request));
            ret.put("$browser", getBrowser(request));
            ret.put("$referrer", request.getHeader(REFERRER));
            ret.put("$referring_domain", getReferringDomain(request));
            ret.put("$device", getDevice(request));
        }

        return stripEmptyProperties(ret);
    }

    /**
     * This supports the Streams functionality but the data will not be available to Funnels and Segmentation.
     * This is called by the JS library with every page load when the library initializes
     */
    public static Map<String, Object> getPageviewInfo(HttpServletRequest request) {
        Map<String, Object> ret = getProperties(request);

        if (request != null) {
            String query = request.getQueryString();
            StringBuffer url = request.getRequestURL();
            if (query != null) {
                url.append("?").append(query).toString();
            }

            ret.put("mp_page", url);
            ret.put("mp_referrer", request.getHeader(REFERRER));
            ret.put("mp_browser", getBrowser(request));
            ret.put("mp_platform", getOs(request));
        }

        return stripEmptyProperties(ret);
    }

    private static Map<String, Object> stripEmptyProperties(Map<String, Object> p) {
        Map<String, Object> ret = new HashMap<String, Object>();
        if (p != null) {
            for (Map.Entry<String, Object> entry : p.entrySet()) {
                if (entry.getValue() != null && entry.getValue().toString().length() > 0) {
                    ret.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return ret;
    }

    private static String getQueryParam(String url, String param) {
        if (url != null) {
            try {
                for (NameValuePair pair : URLEncodedUtils.parse(new URI(url), "UTF-8")) {
                    if (pair.getName() != null && pair.getName().equals(param)) {
                        return URLDecoder.decode(param, "UTF-8").replace('+', ' ');
                    }
                }
            } catch (URISyntaxException e) {
                log.error("Could not parse URL: " + url, e);
            } catch (UnsupportedEncodingException e) {
                log.error("URL decode caused unsupported encoding exception for URL: " + url, e);
            }
        }

        return null;
    }
}
