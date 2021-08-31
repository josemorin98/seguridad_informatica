package com.mycompany.hmac_sha_256;

import java.security.spec.KeySpec;
import javax.crypto.*;
import javax.crypto.spec.*;

public class MyDES extends Ciphers {

    byte[] keyGen(int secLevel, String clave) {

        try {

            byte[] salt = new byte[8];

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithMD5AndDes");
            KeySpec keySpec = new PBEKeySpec(clave.toCharArray(), salt, 1200, 256);
            SecretKey tmp = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "DES");

            return secret.getEncoded();

        } catch (Exception e) {
        }

        return null;
    }

    byte[] encrypt(byte[] plaintext, byte[] key) {

        javax.crypto.Cipher cipher = null;

        try {
            cipher = javax.crypto.Cipher.getInstance("DES/ECB/PKCS5Padding");

            if (cipher == null) {
                System.out.println("No es posible cifrar los datos");
                return null;
            }

            SecretKey myDesKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key));

            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, myDesKey);

            byte[] textEncrypted = cipher.doFinal(plaintext);

            return textEncrypted;
        } catch (Exception e) {
        }

        return null;
    }

    byte[] decrypt(byte[] cipherText, byte[] key) {

        javax.crypto.Cipher cipher = null;

        try {
            cipher = javax.crypto.Cipher.getInstance("DES/ECB/PKCS5Padding");

            if (cipher == null) {
                System.out.println("No es posible cifrar los datos");
                return null;
            }

            SecretKey myDesKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key));

            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, myDesKey);

            byte[] textDecrypted = cipher.doFinal(cipherText);

            return textDecrypted;
        } catch (Exception e) {
        }

        return null;

    }

}
