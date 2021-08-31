/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lab_02;

import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author morin
 */
public class AES {

    byte[] generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key.getEncoded();
    }
    
    
    byte[] encrypt(byte[] plaintext, byte[] key) {
        try {
            //"AES/ECB/PKCS5Padding"
            SecretKey secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] bytesEncriptados = cipher.doFinal(plaintext);

            return bytesEncriptados;

        } catch (Exception ex) {

        }
        return null;
    }

    byte[] decrypt(byte[] cipherText, byte[] key) {

        try {
            //"AES/ECB/PKCS5Padding"
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] textDecrypted = cipher.doFinal(cipherText);

            return textDecrypted;

        } catch (Exception ex) {

        }
        return null;

    }
}
