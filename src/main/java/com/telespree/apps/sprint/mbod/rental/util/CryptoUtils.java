package com.telespree.apps.sprint.mbod.rental.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * CryptoUtils
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2013/03/14 23:11:38 $
 * @version $Revision: #1 $
 * 
 */
public abstract class CryptoUtils {

    private static final int ITERATIONS = 2;

    private static final String SALT = "JUstASimPlEGraiNOfSAlt";

    /**
     * @param value
     * @param keyStr
     * @param algorithm
     * @return
     * @throws Exception
     */
    public static String encrypt(String value, String keyStr, String algorithm)
            throws Exception {

        byte[] keyBytes = keyStr.getBytes();

        Key key = generateKey(keyBytes, algorithm);
        Cipher c = Cipher.getInstance(algorithm);
        c.init(Cipher.ENCRYPT_MODE, key);

        String valueToEnc = null;
        String eValue = value;
        for (int i = 0; i < ITERATIONS; i++) {
            valueToEnc = SALT + eValue;
            byte[] encValue = c.doFinal(valueToEnc.getBytes());
            eValue = new String(Base64.encodeBase64(encValue));
        }
        return eValue;
    }

    /**
     * @param value
     * @param keyStr
     * @param algorithm
     * @return
     * @throws Exception
     */
    public static String decrypt(String value, String keyStr, String algorithm)
            throws Exception {
        byte[] keyBytes = keyStr.getBytes();
        Key key = generateKey(keyBytes, algorithm);
        Cipher c = Cipher.getInstance(algorithm);
        c.init(Cipher.DECRYPT_MODE, key);

        String dValue = null;
        String valueToDecrypt = value;
        for (int i = 0; i < ITERATIONS; i++) {
            byte[] decordedValue = Base64.decodeBase64(valueToDecrypt
                    .getBytes());
            byte[] decValue = c.doFinal(decordedValue);
            dValue = new String(decValue).substring(SALT.length());
            valueToDecrypt = dValue;
        }
        return dValue;
    }

    /**
     * @param keyValue
     * @param algorithm
     * @return
     * @throws Exception
     */
    private static Key generateKey(byte[] keyValue, String algorithm)
            throws Exception {
        Key key = new SecretKeySpec(keyValue, algorithm);
        // Commented out code is for DES algorithm
        // SecretKeyFactory keyFactory =
        // SecretKeyFactory.getInstance(ALGORITHM);
        // key = keyFactory.generateSecret(new DESKeySpec(keyValue));
        return key;
    }
}