/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.firmas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import java.util.Base64.Encoder;
import java.util.Base64;
import javax.crypto.SecretKey;

/**
 * En este ejemplo, los datos a firmar son el mensaje "test". Se deberá cambiar
 * para recibir cualquier arreglo de bytes (por ejemplo, de un archivo).
 * Internamente, se generan las llaves del usuario, pública y privada. Se deberá
 * modificar para que esas llaves se reciban como parámetros, es decir, se
 * deberán generar por fuera. Se realizan las dos operaciones de firma y
 * verificación consecutivamente. Se deberá modificar para que los procesos se
 * realicen en programas separados.
 */
public class FirmaDigital {

    private static byte[] readFileToBytes(String Path) throws IOException {

        String filePath = Path;

        // file to byte[], Path
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));

        // file to byte[], File -> Path
        File file = new File(filePath);
        byte[] bytes2 = Files.readAllBytes(file.toPath());
        return bytes;

    }

    public static void main(String[] args) throws Exception {

        //KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        // get user password and file input stream
        //char[] password = new String("keystore").toCharArray();
        String password = "keystore";
        String alias = "server";
        KeyStoreCert ks = new KeyStoreCert(alias, password);

        String dir = "/home/morin/Escritorio/Certificado/keystore.jks";
        ks.setJKS(dir);
        /*java.io.FileInputStream fis = null;
        try {
            fis = new java.io.FileInputStream("/home/morin/Escritorio/Certificado/keystore.jks");
            ks.load(fis, password);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }*/

        //var for passwords
        //KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password);
        // get my private key
        System.out.println("Private Key");
        //KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, protParam);
        //PrivateKey myPrivateKey = pkEntry.getPrivateKey();
        PrivateKey ksPrivateKey = ks.getPrivateKey();
        System.out.println(ksPrivateKey);

        System.out.println("\nPublic Key Cert");
        //java.security.cert.Certificate cert = (java.security.cert.Certificate) ks.getCertificate(alias); 
        //System.out.println(cert.getPublicKey());
        Certificate cert = ks.getPublicKey();
        System.out.println(cert.getPublicKey());

        //Leer archivo
        String dir_file = "/home/morin/Escritorio/Certificado/Plan-Nutricional-José-Morinm1.pdf";
        byte[] data = readFileToBytes(dir_file);

        //con nivel de seguridad de 1024-bit
        /* Generador de llaves publica y privada*/
 /*KeyPairGenerator generadorLlaves = KeyPairGenerator.getInstance("RSA");
            
        */
        //Los datos a firmar.
        //byte[] data = "test".getBytes("UTF8");
        //Algoritmo de firma, RSA y SHA1 como función hash.
        Signature generadorFirma = Signature.getInstance("Sha224WithRSA");
        //System.out.println("Llave privada: " + parLlaves.getPrivate());
        generadorFirma.initSign(ksPrivateKey);
        generadorFirma.update(data);

        byte[] firmaDigitalBytes = generadorFirma.sign();

        //La firma generada se codifica para que se muestre "amigablemente" al usuario.
        Encoder encoder = Base64.getEncoder();
        String firmaDigitaENC = encoder.encodeToString(firmaDigitalBytes);
        System.out.println("\nSello digital (codificacion Base64):\n" + firmaDigitaENC);

        //inicializacion con la llave publica
        generadorFirma.initVerify(cert.getPublicKey());
        generadorFirma.update(data);

        //Verificación de firma
        boolean resultadoVerificar = generadorFirma.verify(firmaDigitalBytes);

        //Creando cerficidao
        System.out.println("Exporting Certificate. ");
        byte[] buffer_out = cert.getEncoded();
        FileOutputStream os = new FileOutputStream(new File("/home/morin/Escritorio/Certificado/Plan-Nutricional-José-Morinm1.cer"));
        os.write(buffer_out);
        os.close();
        
        //Crando dat
        generadorFirma.initSign(ksPrivateKey);
        generadorFirma.update(data);
        byte[] buffer_out1 = generadorFirma.sign();
        FileOutputStream os1 = new FileOutputStream(new File("/home/morin/Escritorio/Certificado/Plan-Nutricional-José-Morinm1.dat"));
        os1.write(buffer_out1);
        os1.close();

        if (resultadoVerificar == true) {
            System.out.println("\nLa firma es CORRECTA");
        } else {
            System.out.println("\nLa firma es INCORRECTA");
        }
    }
}
