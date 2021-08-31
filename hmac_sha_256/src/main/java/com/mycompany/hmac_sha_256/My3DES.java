package com.mycompany.hmac_sha_256;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.*;
import javax.crypto.spec.*;

public class My3DES extends Ciphers {

    byte[] keyGen(int secLevel, String clave) {

        try {
            
            byte[] salt = new byte[secLevel*2];

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(clave.toCharArray(), salt, 1200, secLevel);
            SecretKey tmp = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "DESede");
            
            return secret.getEncoded();

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
        }

        return null;
    }

    byte[] encrypt(byte[] plaintext, byte[] key) {

        javax.crypto.Cipher cipher = null;

        try {

            cipher = javax.crypto.Cipher.getInstance("DESede/CBC/PKCS5Padding");

            if (cipher == null) {
                System.out.println("No es posible cifrar los datos");
                return null;
            }

            final SecretKey keyS = new SecretKeySpec(key, "DESede");
            //SecretKey myDesKey = SecretKeyFactory.getInstance("DESede").generateSecret(new DESedeKeySpec(key));
            final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            cipher.init(Cipher.ENCRYPT_MODE, keyS,iv);
            

            byte[] textEncrypted = cipher.doFinal(plaintext);
            


            return textEncrypted;
        } catch (Exception e) {
        }

        return null;
    }

    byte[] decrypt(byte[] cipherText, byte[] key) {

        javax.crypto.Cipher cipher = null;

        try {
            cipher = javax.crypto.Cipher.getInstance("DESede/CBC/PKCS5Padding");

            if (cipher == null) {
                System.out.println("No es posible cifrar los datos");
                return null;
            }

            final SecretKey keyS = new SecretKeySpec(key, "DESede");
            //SecretKey myDesKey = SecretKeyFactory.getInstance("DESede").generateSecret(new DESedeKeySpec(key));
            final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, keyS,iv);
            
            byte[] textDecrypted = cipher.doFinal(cipherText);

            return textDecrypted;
        } catch (Exception e) {
        }

        return null;

    }

}
