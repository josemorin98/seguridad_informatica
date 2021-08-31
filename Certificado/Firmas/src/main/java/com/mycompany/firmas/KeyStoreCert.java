/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.firmas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
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

    public KeyStoreCert(String alias, String password) throws KeyStoreException {
        this.alias = alias;
        this.password = password.toCharArray();
        this.ks = KeyStore.getInstance(KeyStore.getDefaultType());
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
    
    public Certificate getPublicKey() throws KeyStoreException{
        this.cert = (java.security.cert.Certificate) ks.getCertificate(alias); 
        return cert;
    }
    
    
}
