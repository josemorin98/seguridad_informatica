    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lab_02;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

/**
 *
 * @author morin
 */
public class KeyStoreCert {
    
    private String alias;
    private char[] password;
    KeyStore ks;
    Certificate cert;
    Signature generadorFirma;

    public KeyStoreCert(String alias, String password) throws KeyStoreException, NoSuchAlgorithmException {
        this.alias = alias;
        this.password = password.toCharArray();
        this.ks = KeyStore.getInstance(KeyStore.getDefaultType());
        this.generadorFirma = Signature.getInstance("Sha512WithRSA");
    }
    
    public void setJKS(String dir) throws FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException{
        java.io.FileInputStream fis = null;
        try {
            fis = new java.io.FileInputStream(dir);
            this.ks.load(fis, this.password);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
    
    public PrivateKey getPrivateKey() throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException{
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(this.password);
        KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) this.ks.getEntry(this.alias, protParam);
        //PrivateKey myPrivateKey = pkEntry.getPrivateKey();
        return pkEntry.getPrivateKey();
    }
    
    public PublicKey getPublicKey() throws KeyStoreException{
        this.cert = (java.security.cert.Certificate) ks.getCertificate(alias); 
        return cert.getPublicKey();
    }
    
    public byte[] Sign(byte[] data, PrivateKey ksPrivateKey) throws InvalidKeyException, SignatureException{
        this.generadorFirma.initSign(ksPrivateKey);
        this.generadorFirma.update(data);
        return generadorFirma.sign();
    }
    
    public boolean Verify(byte[] firmaDigitalBytes, byte[] data, PublicKey ksPublicKey) throws InvalidKeyException, SignatureException{
        generadorFirma.initVerify(ksPublicKey);
        generadorFirma.update(data);
        return  generadorFirma.verify(firmaDigitalBytes);
    }
    
}
