package com.swooli.security;

import com.swooli.ApplicationProperties;
import java.security.Security;
import java.util.Random;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.digest.PooledStringDigester;
import org.jasypt.exceptions.EncryptionInitializationException;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class for digesting strings.  The instance is constructed based on application
 * properties.
 *
 * @author jmcarey
 */
public enum StringDigester {

    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(StringDigester.class);

    private static final PooledStringDigester digester = new PooledStringDigester();

    private static final Random randomGenerator = new Random(System.currentTimeMillis());

    static {

        Security.addProvider(new BouncyCastleProvider());

        digester.setPoolSize(ApplicationProperties.getEncryptPoolSize());
        digester.setAlgorithm(ApplicationProperties.get("swooli.digest.algorithm"));

        try {
            digester.initialize();
        } catch(final EncryptionInitializationException ex) {
            logger.error("Failed initializing digester.", ex);
            throw ex;
        }

    }

    public String digest(final String value) throws EncryptionException {
        try {
            return digester.digest(value);
        } catch(final EncryptionOperationNotPossibleException ex) {
            throw new EncryptionException(ex);
        }
    }

    public boolean matches(final String clearText, final String cipherText) throws EncryptionException {
        try {
            return digester.matches(clearText, cipherText);
        } catch(final EncryptionOperationNotPossibleException ex) {
            throw new EncryptionException(ex);
        }
    }

    public String createRandomDigest() {
        return StringDigester.INSTANCE.digest(String.valueOf(randomGenerator.nextInt()));
    }

}