package com.swooli.security;

import com.swooli.ApplicationProperties;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionInitializationException;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class for encrypting/decrypting strings.  The instance is constructed based on application
 * properties.
 *
 * @author jmcarey
 */
public enum StringEncrypter {

    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(StringEncrypter.class);

    private static final PooledPBEStringEncryptor encrypter = new PooledPBEStringEncryptor();

    static {

        Security.addProvider(new BouncyCastleProvider());

        encrypter.setPoolSize(ApplicationProperties.getEncryptPoolSize());
        encrypter.setPassword(ApplicationProperties.get("swooli.encrypt.password"));
        encrypter.setAlgorithm(ApplicationProperties.get("swooli.encrypt.algorithm"));

        try {
            encrypter.initialize();
        } catch(final EncryptionInitializationException ex) {
            logger.error("Failed initializing encrypter.", ex);
            throw ex;
        }

    }

    public String encrypt(final String plainText) throws EncryptionException {
        try {
            return encrypter.encrypt(plainText);
        } catch(final EncryptionOperationNotPossibleException ex) {
            throw new EncryptionException(ex);
        }
    }

    public String decrypt(final String cipherText) {
        try {
            return encrypter.decrypt(cipherText);
        } catch(final EncryptionOperationNotPossibleException ex) {
            throw new EncryptionException(ex);
        }
    }

}