package com.lela.commons.test.service;

import com.google.common.collect.Maps;
import com.mixpanel.java.mpmetrics.MPConfig;
import com.mixpanel.java.mpmetrics.MPMetrics;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateMidnight;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 9/25/12
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/spring/mixpanel.xml"})
public class MixPanelTest{



    //private String token = "4fd800765c150a59ef72cc5456f7f1cf";
    //private String token = "a818cce3c5140f155e3077eb63ce619c";
    //private String token = "8930f829edf7ae2023ca0a2335ef4399";
    //private String token = "a1d727738b3865b654c13ed3daf2fa61";
    @Autowired
    MPConfig mpConfig;


    private static String username = RandomStringUtils.randomAlphabetic(10);
    private static String name = RandomStringUtils.randomAlphabetic(15) + "_name";
    private static Map motivators = getMotivators();

    private static String[] affiliates = new String[]{"affiliate1", "affiliate2", "affiliate3", "affiliate4"};
    private static String[] genders = new String[]{"male", "female"};

    private static final Map superProperties = getSuperProperties();

    /*
    @Test
    public void ping()
    {
        MPMetrics mpMetrics = MPMetrics.getInstance(token);
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("ip", "123.123.123.123");
        properties.put("action", "test");

        mpMetrics.track("testEvent2", properties);
        mpMetrics.shutdown();
    }*/

    @Ignore
    private static Map getSuperProperties()
    {
        Map superProps = new HashMap();
        superProps.put("mp_name_tag", username);
        superProps.put("name", name);
        superProps.put("city", "Huntington");
        superProps.put("age", getRandomBetween(13, 70));
        superProps.put("affiliate", affiliates[getRandomBetween(0,3)]);
        superProps.put("gender", genders[getRandomBetween(0,1)]);
        superProps.put("email", RandomStringUtils.randomAlphabetic(5) + "@" +RandomStringUtils.randomAlphabetic(15)+".com");
        superProps.put("created", new Date());
        superProps.putAll(motivators);
        return superProps;
    }



    @Test
    @Ignore
    public void b_tracking()
    {
        MPMetrics mpMetrics = MPMetrics.getInstance(mpConfig);
        mpMetrics.identify(username);
        mpMetrics.registerSuperProperties(superProperties);

        Map props = new HashMap();
        mpMetrics.track("register", props);

        Map props2 = new HashMap();
        mpMetrics.track("login", props2);

        Map props3 = new HashMap();
        props3.put("productid", "123");
        props3.put("price", 10.00);
        props3.put("commission", 1.54);
        mpMetrics.track("purchase", props3);
        mpMetrics.shutdown();
    }


    /*
    @Test
    public void a_tracking()
    {
        MPMetrics mpMetrics = MPMetrics.getInstance(token);
        mpMetrics.identify(username);
        mpMetrics.registerSuperProperties(superProperties);

        Map props = new HashMap();
        //props.put("distinct_id", username);
        //props.put("name", name);
        //props.putAll(motivators);

        mpMetrics.track("login", props);
        mpMetrics.shutdown();
    }
    */

    @Test
    @Ignore
    public void c_tracking()
    {
        MPMetrics mpMetrics = MPMetrics.getInstance(mpConfig);
        mpMetrics.identify(username);

        mpMetrics.registerSuperProperties(superProperties);

        Map props = new HashMap();
        //props.put("distinct_id", username);
        //props.put("name", name);
        props.put("productid", "123");
        props.put("price", 10.00);
        props.put("commission", 1.65);
        //props.putAll(motivators);

        mpMetrics.track("purchase", props);
        mpMetrics.shutdown();
    }




    @Test
    @Ignore
    public void people()
    {
        MPMetrics mpMetrics = MPMetrics.getInstance(mpConfig);
        mpMetrics.identify(username);

        //Map superProps = new HashMap();
        //superProps.put("mp_name_tag", username);
        //mpMetrics.registerSuperProperties(superProps);

        Map props = new HashMap();

        props.putAll(getMotivators());
        props.put("$name", superProperties.get("name"));
        props.put("city", superProperties.get("city"));
        props.put("affiliate", superProperties.get("affiliate"));
        props.put("gender", superProperties.get("gender"));
        props.put("age", superProperties.get("age"));
        props.put("$email", superProperties.get("email"));
        props.put("$created", superProperties.get("created"));
        props.put("$last_login", new DateMidnight().minusDays(getRandomBetween(5,25)).toDate());

        mpMetrics.set(props);

        Map props2 = new HashMap();
        props2.put("Total Commission", 0);

        mpMetrics.set(props2);

        mpMetrics.increment("Total Commission", getRandomBetween(0,100));

        mpMetrics.shutdown();



    }



    private static int getRandomBetween(int min, int max)
    {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }
    private static Map getMotivators() {
        int min = 1;
        int max = 9;
        Map motivators = new HashMap();
        motivators.put("motivatorA", getRandomBetween(min, max));
        motivators.put("motivatorB", getRandomBetween(min, max));
        motivators.put("motivatorC", getRandomBetween(min, max));
        motivators.put("motivatorD", getRandomBetween(min, max));
        motivators.put("motivatorE", getRandomBetween(min, max));
        motivators.put("motivatorF", getRandomBetween(min, max));
        motivators.put("motivatorG", getRandomBetween(min, max));


        return motivators;
    }

}
