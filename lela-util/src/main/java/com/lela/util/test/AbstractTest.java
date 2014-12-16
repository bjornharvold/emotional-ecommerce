/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.util.test;

import com.icegreen.greenmail.util.GreenMail;
import com.lela.util.utilities.GreenMailHelper;
import com.lela.util.utilities.jackson.CustomObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Chris Tallent
 * Date: 6/23/11
 * Time: 11:12 AM
 * Responsibility:
 */
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractTest extends AbstractJUnit4SpringContextTests {

    /**
     * Spring injected mapping of domain class to the mongodb repository responsible for saving it
     */
    @javax.annotation.Resource(name = "classToRepositoryMapping")
    protected Map<Class, CrudRepository> classToRepositoryMapping;

    /**
     * Base test resource directly to look in for JSON test data
     */
    private static String TESTDATA_DIR = "testdata";

    /**
     * Deserialize from JSON to an object
     */
    protected CustomObjectMapper mapper = new CustomObjectMapper();

    /**
     * The loaded JSON cache must be static so that it will be available between individual
     * test methods in a single test class since JUnit creates a class instance per test method
     */
    private static final Map<Class, Map<String, String>> loadedJsonCache = new HashMap<Class, Map<String, String>>();

    /**
     * The Objects to remove tracking must be static so that it will be available between individual
     * test methods in a single test class since JUnit creates a class instance per test method.  This
     * will allow an attempt at object removal even it a runtime exception terminated an individual test
     * method which skips the @After method
     */
    private static final Map<Class, HashSet<Object>> objectsToRemove = new HashMap<Class, HashSet<Object>>();

    protected static GreenMail greenMail;

    @BeforeClass
    public static void beforeClass() {
        greenMail = GreenMailHelper.getGreenMail();
    }

    @AfterClass
    public static void afterClass() {
        GreenMailHelper.stopGreenMail();
    }

    @After
    public void afterTestCase() {
        removeTrackedObjects();
    }

    /**
     * Load a JSON test file for the given type and rlnm into the Mongo database and track
     * it to be automatically removed after the test method has executed
     *
     * @param targetClass Class to be loaded (e.g. - Item.class)
     * @param rlnm rlnm of the test datafile (e.g. - teststroller for teststroller.json)
     * @return Instance deserialized from the file
     */
    protected <T> T loadToDatabase(Class<T> targetClass, String rlnm) {

        // Load the object
        T object = loadJson(targetClass, rlnm);

        // Save the object to the appropriate repository
        if (object != null) {
            CrudRepository repository = (CrudRepository)classToRepositoryMapping.get(targetClass);
            if (repository == null) {
                throw new IllegalStateException("No Mongo repository found for type: " + targetClass);
            }

            // Save the object to mongo and get the version with a defined ObjectId
            object = (T)repository.save(object);

            trackObjectToRemove(object);
        } else {
            throw new IllegalStateException("No object parsed from loaded document for class: " + targetClass + ", rlnm: " + rlnm);
        }

        return (T)object;
    }

    /**
     * Track an domain object that was loaded into the database and should be automatically removed
     * when the test method has completed execution
     *
     * @param object Domain object implementing AbstractDocument with an Object ID
     */
    protected <T> void trackObjectToRemove(T object) {
        HashSet<Object> objects = objectsToRemove.get(object.getClass());
        if (objects == null) {
            objects = new HashSet<Object>();
            objectsToRemove.put(object.getClass(), objects);
        }

        objects.add(object);
    }

    /**
     * Remove all objects from the mongodb that had been loaded and tracked in this test method execution
     */
    protected void removeTrackedObjects() {
        // Remove any tracked objects of each type
        for (Class objectType : objectsToRemove.keySet()) {
            CrudRepository repository = (CrudRepository)classToRepositoryMapping.get(objectType);
            if (repository == null) {
                throw new IllegalStateException("No Mongo repository found for type: " + objectType);
            }

            for (Object object : objectsToRemove.get(objectType)) {
                try {
                    repository.delete(object);
                } catch (Exception e) {
                    // Don't let a single exception interrupt the removal of all tracked objects
                    logger.error("Could not remove object of type: " + objectType + " with object: " + object, e);
                }
            }
        }

        // Clear the map of tracked objects to remove (The loaded JSON cache should NOT be cleared)
        objectsToRemove.clear();
    }

    /**
     * Load JSON test data and deserialize for the given Class and rlnm where the expected location
     * of the test data file is /src/test/resources/testdata/[mongo-collection-name]\[rlnm].json
     */
    protected <T> T loadJson(Class<T> targetClass, String rlnm) {

        String json = null;
        T result = null;

        // Check to see if this JSON has already been loaded
        if (loadedJsonCache.containsKey(targetClass) && loadedJsonCache.get(targetClass).containsKey(rlnm)) {
            json = loadedJsonCache.get(targetClass).get(rlnm);

            try {
                result = mapper.readValue(json, targetClass);
            } catch (IOException e) {
                throw new IllegalStateException("Could not deserialize previously saved JSON for targetClass: " + targetClass + ", rlnm: " + rlnm);
            }
        } else {

            // Load the JSON from a file
            String collectionName = StringUtils.uncapitalize(targetClass.getSimpleName());
            String path = TESTDATA_DIR + "/" + collectionName + "/" + rlnm + ".json";
            Resource file = new ClassPathResource(path);

            if (file.exists()) {
                try {

                    // Read in the JSON as string to cache
                    json = FileUtils.readFileToString(file.getFile());

                    // Deserialize the JSON to an object
                    result = mapper.readValue(json, targetClass);

                    // Save the loaded JSON assuming it deserialized correctly
                    Map<String, String> loadedJsonForClass = loadedJsonCache.get(targetClass);
                    if (loadedJsonForClass == null) {
                        loadedJsonForClass = new HashMap<String, String>();
                        loadedJsonCache.put(targetClass, loadedJsonForClass);
                    }

                    loadedJsonForClass.put(rlnm, json);
                } catch (IOException e) {
                    throw new RuntimeException("Could not load test data from JSON file: " + path, e);
                }
            } else {
                throw new IllegalArgumentException("No file exists: " + path);
            }
        }

        return result;
    }
}
