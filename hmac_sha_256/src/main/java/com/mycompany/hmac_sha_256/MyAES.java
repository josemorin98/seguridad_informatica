package com.mycompany.hmac_sha_256;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class MyAES extends Ciphers {

    byte[] keyGen(int secLevel, String clave) {
        try {

            SecretKeyFactory factory = null;
            byte[] salt = new byte[secLevel * 2];
            if (secLevel == 128) {
                factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            } else if (secLevel == 192) {
                factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA384");
            } else if (secLevel == 256){
                factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            }else{
                System.out.println("Valores erroneos");
            }

            KeySpec keySpec = new PBEKeySpec(clave.toCharArray(), salt, 1200, secLevel);
            SecretKey tmp = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            return secret.getEncoded();

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MyAES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(MyAES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
