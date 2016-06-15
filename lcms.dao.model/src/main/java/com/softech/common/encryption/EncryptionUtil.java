package com.softech.common.encryption;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.util.EncodingUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by abdul.wahid on 6/14/2016.
 */
public class EncryptionUtil {
    private static final String ALGO = "AES";
    public static String generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGO);
        SecureRandom random = new SecureRandom();
        keyGen.init(random);
        SecretKey secretKey = keyGen.generateKey();
        return new String(Hex.encode(secretKey.getEncoded())).toUpperCase();
    }

    public static String encrypt(String key,String text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGO);
        SecretKeySpec skeySpec = new SecretKeySpec(Hex.decode(key), ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return  new String(Hex.encode(cipher.doFinal(text.getBytes()))).toUpperCase();
    }

    public static String decrypt(String key,String text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGO);
        SecretKeySpec skeySpec = new SecretKeySpec(Hex.decode(key), ALGO);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decByte = cipher.doFinal(Hex.decode(text));
        return new String(decByte);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        String enc = encrypt("F43E8A778263A605BFBC8A7CD95D7307", "lms!@#$");
        System.out.println(enc);

        String dec = decrypt("F43E8A778263A605BFBC8A7CD95D7307", enc);
        System.out.println(dec);
    }
}
