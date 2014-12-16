package com.lela.commons.spring.data;

import com.mongodb.Mongo;
import org.apache.log4j.spi.LoggingEvent;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.core.MongoDbUtils;
import org.springframework.data.mongodb.log4j.MongoLog4jAppender;

import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Chris Tallent
 * Date: 5/22/12
 * Time: 9:21 AM
 */
public class AuthenticatedMongoLog4jAppender extends MongoLog4jAppender {

    private final Pattern encryptPattern = Pattern.compile("ENC\\(([^)]+)\\)");

    private String username;
    private String password;

    @Override
    protected void connectToMongo() throws UnknownHostException {
        String password = this.password;
        if (username != null && password != null) {

            // Determine if the password is encrypted
            try {
                Matcher matcher = encryptPattern.matcher(password);
                if (matcher.matches()) {

                    // Extract the actual encrypted password
                    password = matcher.group(1);

                    EnvironmentStringPBEConfig pbeConfig = new EnvironmentStringPBEConfig();
                    pbeConfig.setProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
                    pbeConfig.setAlgorithm("PBEWITHSHA-256AND256BITAES-CBC-BC");
                    pbeConfig.setPasswordEnvName("LELA_ENCRYPTION_PASSWORD");
                    pbeConfig.setKeyObtentionIterations(10000);
                    pbeConfig.setSaltGenerator(new org.jasypt.salt.RandomSaltGenerator());

                    StandardPBEStringEncryptor stringEncryptor = new StandardPBEStringEncryptor();
                    stringEncryptor.setConfig(pbeConfig);

                    password = stringEncryptor.decrypt(password);
                }
            } catch (Exception e) {
                System.err.println("Could not decrypt password: " + password);
                e.printStackTrace();
            }

            this.mongo = new Mongo(host, port);
            UserCredentials uc = new UserCredentials(username, password);
            this.db = MongoDbUtils.getDB(mongo, database, uc);
        } else {
            super.connectToMongo();    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    /**
     * Override this to make sure that the mongo logging is fail-safe
     * @param event
     */
    @Override
    protected void append(LoggingEvent event) {
        try {
            super.append(event);
        } catch (Exception e) {
            System.err.println("Failed to log to MongoDB");
            //e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
