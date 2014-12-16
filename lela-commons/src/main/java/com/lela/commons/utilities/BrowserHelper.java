package com.lela.commons.utilities;

import com.lela.commons.exception.PageNotLoadedException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 9/21/12
 * Time: 11:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class BrowserHelper {

    private final static Logger log = LoggerFactory.getLogger(BrowserHelper.class);

    String url;

    HttpResponse response;
    String content;

    boolean pageLoaded = false;

    private static final Executor executor;

    static{
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.setRedirectStrategy(new DefaultRedirectStrategy()
        {
            @Override
            protected URI createLocationURI(String location) throws ProtocolException {
                log.debug("location:" + location);
                location = encodeLocation(location);
                return super.createLocationURI(location);
            }
        });
        executor = Executor.newInstance(httpClient);
    }

    private static String encodeLocation(String location) {
        location = StringUtils.replace(location, "<", "%3C");
        location = StringUtils.replace(location, " ", "%20");
        return location;
    }

    public BrowserHelper(String url)
    {
         this.url = encodeLocation(url);
    }

    public void load() throws PageNotLoadedException
    {
        try
        {

            Response urlResponse = executor.execute(Request.Get(url)
                    .version(HttpVersion.HTTP_1_1)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1"));

            response = urlResponse.returnResponse();
            content = IOUtils.toString(response.getEntity().getContent());

            pageLoaded = true;
        }
        catch(Exception e)
        {
            log.error(e.getMessage(),  e);
            throw new PageNotLoadedException("The page could not be loaded correctly, there could be a communication problem.", e);
        }

    }


    public boolean verifyURL()
    {
        if(pageLoaded)
          return response != null && response.getStatusLine().getStatusCode() == 200;
        else
            throw new RuntimeException("The page was never loaded successfully");
    }

    public boolean verifyPageContains(String containsText)
    {
        if(pageLoaded)
        {
            String text = content;
            if(StringUtils.containsIgnoreCase(text, containsText))
                return true;
            else
                return false;
        }
        else
        {
            throw new RuntimeException("The page was never loaded successfully");
        }
    }

    public boolean verifyPageDoesNotContain(String containsText)
    {

        if(pageLoaded)
        {
            String text = content;
            if(StringUtils.containsIgnoreCase(text, containsText))
                return false;
            else
                return true;
        }
        else
        {
            throw new RuntimeException("The page was never loaded successfully");
        }

    }

    public boolean verifyPageDoesNotContain(List<String> containsTexts)
    {
        if(verifyURL()){
            for(String containsText:containsTexts)
            {
                if(!verifyPageDoesNotContain(containsText))
                {
                    return false;
                }
            }
        }
        else{
            return false;
        }

        return true;
    }
}
