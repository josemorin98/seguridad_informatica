
package com.mycompany.hmac_sha_256;


public abstract class Ciphers{

   abstract byte[] keyGen(int secLevel,String clave);   
   abstract byte[] encrypt(byte[] plaintext, byte[] key);
   abstract byte[] decrypt(byte[] cipherText, byte[] key);
}

