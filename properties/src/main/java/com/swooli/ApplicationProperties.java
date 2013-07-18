package com.swooli;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ApplicationProperties {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);

    private static final Properties APP_PROPERTIES = new Properties();
    static {
        try {
            final URL propertiesResource = ApplicationProperties.class.getResource("/application.properties");
            if(propertiesResource == null) {
                logger.info("No application properties file found on classpath.");
            } else {
                logger.info("Found application properties file on classpath.  Loading '{}'", propertiesResource);
                APP_PROPERTIES.load(propertiesResource.openStream());
            }
        } catch (final IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public static String get(final String key) {
        return APP_PROPERTIES.getProperty(key);
    }

    public static Charset getCharEncoding() {
        return Charset.forName(APP_PROPERTIES.getProperty("swooli.character.encoding"));
    }

    public static int getEncryptPoolSize() {
        return Integer.parseInt(APP_PROPERTIES.getProperty("swooli.encrypt.pool.size"));
    }

    public static int getRememberMeDays() {
        return Integer.parseInt(APP_PROPERTIES.getProperty("swooli.remember.me.days"));
    }
}