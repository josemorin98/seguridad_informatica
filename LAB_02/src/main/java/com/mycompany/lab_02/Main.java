/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lab_02;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author morin
 */
public class Main {

    private static byte[] readFileToBytes(String Path) throws IOException {

        String filePath = Path;

        // file to byte[], Path
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));

        // file to byte[], File -> Path
        File file = new File(filePath);
        byte[] bytes2 = Files.readAllBytes(file.toPath());
        return bytes;

    }

    public static void archivoCifrado(byte[] datos, String nameArchivo) throws FileNotFoundException, IOException {
        FileOutputStream fileOutput = new FileOutputStream(nameArchivo);
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
        bufferedOutput.write(datos);
        bufferedOutput.flush();
        bufferedOutput.close();
        fileOutput.close();
    }

    public static void send(byte[] data) {
        System.out.println("Se cifrado: \n" + new String(data));
    }

    public static void show(byte[] data) {
        System.out.println("\nSe descifrado: \n" + new String(data));
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, KeyStoreException, FileNotFoundException, CertificateException, UnrecoverableEntryException, SignatureException {
        /*String dir_file = "/home/morin/Escritorio/Certificado/Plan-Nutricional-José-Morinm1.pdf";
        byte[] data = readFileToBytes(dir_file);
        data = "hola".getBytes();
        AES aesEmisor = new AES();*/

 /*byte[] k = aesEmisor.generateKey(128);
        byte[] cif_file = aesEmisor.encrypt(data, k);
        send(cif_file);*/
        //Generacion de llaves Publicas y Pribadas para Emisor y Receptor
        KeyPairGenerator generadorLlaves = KeyPairGenerator.getInstance("RSA");
        generadorLlaves.initialize(2048);

        //Llaves Receptor
        KeyPair parLlavesReceptor = generadorLlaves.genKeyPair();

        //Llaves Emisor
        KeyPair parLlavesEmisor = generadorLlaves.genKeyPair();

        //Lectura de archivos
        String dirFolder = "/home/morin/Escritorio/Seguridad Informatica/LAB_02/corpuesD";
        File folder = new File(dirFolder);
        if (folder.isDirectory()) {
            String[] list_files = folder.list();
            System.out.println("N.File\tSize\tCifrado\tFirma\tDescifrado\tVerificacion\tK_Cifrado\tK_Descifrado");

            File archivo;
            byte[] data;
            AES aesEmisor = new AES();
            byte[] k = aesEmisor.generateKey(256);
            byte[] cif_file;
            RSA rsaEmisor = new RSA();;
            byte[] cif_k;
            KeyStoreCert ksEmisor;
            byte[] firmado_data;
            RSA rsaReceptor = new RSA();;
            byte[] des_k;
            AES aesReceptor = new AES();;
            byte[] des;
            //Laves Ksign y Kver - Emisor
            //Password del jks y alias
            String password = "jcmg42988";
            String alias = "server";
            //Lectura del archivo jks
            ksEmisor = new KeyStoreCert(alias, password);
            String dir = "/home/morin/Escritorio/Seguridad Informatica/LAB_02/Files_cert/15360/keystore.jks";
            ksEmisor.setJKS(dir);
            for (int g = 0; g < 4; g++) {

                for (int i = 0; i < list_files.length; i++) {
                    //System.out.println("------ "+list_files[i]+" ------");
                    archivo = new File(dirFolder + "/" + list_files[i]);

                    data = readFileToBytes(dirFolder + "/" + list_files[i]);
                    //AES Emisor
                    //aesEmisor = new AES();
                    //Generacion de llave K
                    //k = aesEmisor.generateKey(256);
                    //Cifrado de Archivo
                    double inicioAES = System.currentTimeMillis();
                    cif_file = aesEmisor.encrypt(data, k);
                    //System.out.println("----Archivo Cifrado");
                    double finAES = System.currentTimeMillis();
                    //send(cif_file); //Muestra de archivo

                    //System.out.println("Tiempo de generacion de cifrado: " + (finAES - inicioAES) + " ms");
                    //RSA Emisor
                    //rsaEmisor = new RSA();
                    double inicioK = System.nanoTime();
                    //Cifrar k con llave publica del receptor
                    cif_k = rsaEmisor.Encrypt(k, parLlavesReceptor.getPublic());
                    //System.out.println("\n Llave k cifrada");
                    //send(k);
                    double finK = System.nanoTime();

                    //Firmado digital
                    double inicioFirma = System.currentTimeMillis();
                    firmado_data = ksEmisor.Sign(data, ksEmisor.getPrivateKey());
                    double finFirma = System.currentTimeMillis();

                    //System.out.println("Tiempo de generacion de firma: " + (finFirma - inicioFirma) + " ms");
                    //RECEPTOR
                    //RSA Receptor
                    //rsaReceptor = new RSA();
                    //Descifrado de la llave K
                    double inicioKD = System.nanoTime();
                    des_k = rsaReceptor.Decrypt(cif_k, parLlavesReceptor.getPrivate());
                    double finKD = System.nanoTime();
                    //Muestra la llave k descifrada
                    //System.out.println("----Llave k desifrada");
                    //show(des_k);
                    //AES Receptor
                    //aesReceptor = new AES();
                    //Descifrado a paritr de la llave kdescifrada con la llave privada del receptor
                    double inicioAESRec = System.currentTimeMillis();
                    des = aesReceptor.decrypt(cif_file, des_k);
                    //show(des);
                    double finAESRec = System.currentTimeMillis();

                    //System.out.println("Tiempo de generacion de descifrado: " + (finAESRec - inicioAESRec) + " ms");
                    //Verificación de la firma generada por el Emisor
                    double inicioVer = System.currentTimeMillis();
                    boolean verificar = ksEmisor.Verify(firmado_data, data, ksEmisor.getPublicKey());
                    if (verificar == true) {
                        //System.out.println("----El archivo es autentico, llego correctamente.");
                    } else {
                        System.out.println("El archivo no es autentico, hubo un cambio duarante su trslado.");
                    }
                    double finVer = System.currentTimeMillis();
                    //System.out.println("Tiempo de generacion de verficado: " + (finVer - inicioVer) + " ms");
                    //(i) + "\t" + archivo.length() + "\t" + 
                    System.out.println((finAES - inicioAES) + "\t"
                            + (finFirma - inicioFirma) + "\t" + (finAESRec - inicioAESRec) + "\t" + (finVer - inicioVer)
                            + "\t" + (finK - inicioK) + "\t" + (finKD - inicioKD));
                }
            }
        }

    }
}
