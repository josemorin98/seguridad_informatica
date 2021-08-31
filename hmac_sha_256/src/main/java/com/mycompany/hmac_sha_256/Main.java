/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hmac_sha_256;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    public static void send(byte[] data) {
        System.out.println("Enviando a red publica: " + new String(data));
    }

    public static void show(byte[] data) {
        System.out.println("Mostrando en dispositivo receptor : " + new String(data));
    }

    public static void archivoCifrado(byte[] datos, String nameArchivo) throws FileNotFoundException, IOException {
        FileOutputStream fileOutput = new FileOutputStream(nameArchivo);
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
        bufferedOutput.write(datos);
        bufferedOutput.flush();
        bufferedOutput.close();
        fileOutput.close();
    }

    public static void main(String[] args) throws Exception {
        String dir = "/home/morin/Vídeos";
        //dir = "/home/morin/Escritorio/pruebas_cipher";
        dir = "/home/morin/Escritorio/Ejercicio-3C";
        dir = args[0];

        File carpeta = new File(dir);
        if (carpeta.isDirectory()) {
            if (carpeta.isDirectory()) {
                String[] listado = carpeta.list();
                if (listado == null || listado.length == 0) {
                    System.out.println(" No hay elementos dentro de la carpeta actual");
                    return;
                } else {
                    System.out.println("\tDirectorio: " + dir);
                    int enc = 0;
                    String[] ECNR = {"DES", "3DES", "AES-128", "AES-192", "AES-256"};
                    double[] Tiempos = {0.0, 0.0, 0.0, 0.0, 0.0};
                    double tiempoTotal = 0, tiempo = 0;
                    long inicio, fin;
                    long iniciodes, findes;
                    long iniciocif, fincif;
                    File p = new File(dir + "/Pruebas");
                    FileWriter fw = null;
                    if (p.mkdir()) {
                        fw = new FileWriter(dir + "/Pruebas/tiempos.csv");
                    }
                    fw.append("Cifrador").append(",");
                    fw.append("Archvivo").append(",");
                    fw.append("Tamaño").append(",");
                    fw.append("T_cifrado").append(",");
                    fw.append("T_descifrado").append("\n");
                    while (enc <= 4) {
                        switch (enc) {
                            //DES
                            case 0:
                                System.out.println("-----------------------");
                                //Inicio de tiempo general
                                inicio = System.currentTimeMillis();
                                File DES = new File(dir + "/Pruebas/DES");
                                for (int i = 0; i < listado.length; i++) {

                                    if (new File(dir + "/" + listado[i]).isFile()) {
                                        //Inicio de tiempo de cifrado
                                        iniciocif = System.currentTimeMillis();
                                        //Cifrado
                                        byte[] archByte = readFileToBytes(dir + "/" + listado[i]);
                                        String secretKey = "secret123";
                                        Ciphers cipherDES = new MyDES();
                                        byte[] secretKeyDES = cipherDES.keyGen(64, secretKey);
                                        byte[] myCipherDES = cipherDES.encrypt(archByte, secretKeyDES);
                                        System.out.println(ECNR[enc] + " - " + listado[i]);
                                        //Fin de tiempo de cifrado
                                        fincif = System.currentTimeMillis();
                                        //Inicio de tiempo de descifrado
                                        iniciodes = System.currentTimeMillis();
                                        //Descifrado
                                        byte[] miDescipherDES = cipherDES.decrypt(myCipherDES, secretKeyDES);
                                        if (!DES.exists()) {
                                            if (DES.mkdirs()) {
                                                archivoCifrado(miDescipherDES, dir + "/Pruebas/DES/DES-" + listado[i]);
                                            }
                                        } else {
                                            archivoCifrado(miDescipherDES, dir + "/Pruebas/DES/DES-" + listado[i]);
                                        }
                                        //Fin de inicio de descrifrado
                                        findes = System.currentTimeMillis();
                                        System.out.println("Cifrado: " + (fincif - iniciocif) + "\t| Descifrado: " + (findes - iniciodes) + "\n");
                                        //Tiempos en el csv
                                        long cif = (fincif - iniciocif);
                                        long desc = (findes - iniciodes);
                                        fw.append(ECNR[enc]).append(",");
                                        fw.append(listado[i]).append(",");
                                        fw.append(""+new File(dir + "/" + listado[i]).length()).append(",");
                                        fw.append("" + cif).append(",");
                                        fw.append("" + desc).append("\n");
                                    }

                                }
                                // Tomamos el tiempo de fin de la ejecucion
                                fin = System.currentTimeMillis();

                                // calculamos el tiempo de ejecucion
                                tiempo = (double) ((fin - inicio) / 1000);
                                tiempo = (double) ((fin - inicio));
                                System.out.println(" " + tiempo);
                                Tiempos[enc] = tiempo;

                                break;

                            case 1:
                                System.out.println("-----------------------");
                                inicio = System.currentTimeMillis();
                                File tDES = new File(dir + "/Pruebas/3DES");
                                for (int i = 0; i < listado.length; i++) {
                                    if (new File(dir + "/" + listado[i]).isFile()) {
                                        //Inicio de tiempo de cifrado
                                        iniciocif = System.currentTimeMillis();
                                        //Cifrado
                                        byte[] archByte = readFileToBytes(dir + "/" + listado[i]);
                                        String secretKey = "secret123";
                                        Ciphers cipher3DES = new My3DES();
                                        byte[] secretKey3DES = cipher3DES.keyGen(192, secretKey);
                                        byte[] myCipher3DES = cipher3DES.encrypt(archByte, secretKey3DES);
                                        System.out.println(ECNR[enc] + " - " + listado[i]);
                                        //Fin de tiempo de cifrado
                                        fincif = System.currentTimeMillis();
                                        //Inicio de tiempo de descifrado
                                        iniciodes = System.currentTimeMillis();
                                        //Descifrado
                                        byte[] miDescipher3DES = cipher3DES.decrypt(myCipher3DES, secretKey3DES);
                                        if (!tDES.exists()) {
                                            if (tDES.mkdirs()) {
                                                archivoCifrado(miDescipher3DES, dir + "/Pruebas/3DES/3DES-" + listado[i]);
                                            }
                                        } else {
                                            archivoCifrado(miDescipher3DES, dir + "/Pruebas/3DES/3DES-" + listado[i]);
                                        }
                                        //Fin de inicio de descrifrado
                                        findes = System.currentTimeMillis();
                                        System.out.println("Cifrado: " + (fincif - iniciocif) + "\t| Descifrado: " + (findes - iniciodes) + "\n");
                                        //Tiempos en el csv
                                        long cif = (fincif - iniciocif);
                                        long desc = (findes - iniciodes);
                                        fw.append(ECNR[enc]).append(",");
                                        fw.append(listado[i]).append(",");
                                        fw.append(""+new File(dir + "/" + listado[i]).length()).append(",");
                                        fw.append("" + cif).append(",");
                                        fw.append("" + desc).append("\n");
                                    }
                                }
                                // Tomamos el tiempo de fin de la ejecucion
                                fin = System.currentTimeMillis();

                                // calculamos el tiempo de ejecucion
                                tiempo = (double) ((fin - inicio) / 1000);
                                tiempo = (double) ((fin - inicio));
                                System.out.println(" " + tiempo);
                                Tiempos[enc] = tiempo;
                                break;
                            case 2:
                                System.out.println("-----------------------");
                                inicio = System.currentTimeMillis();
                                File AES128 = new File(dir + "/Pruebas/AES128");
                                for (int i = 0; i < listado.length; i++) {
                                    if (new File(dir + "/" + listado[i]).isFile()) {
                                        //Inicio de tiempo de cifrado
                                        iniciocif = System.currentTimeMillis();
                                        //Cifrado
                                        byte[] archByte = readFileToBytes(dir + "/" + listado[i]);
                                        String secretKey = "secret123";
                                        Ciphers cipherAES = new MyAES();
                                        byte[] secretKeyAES = cipherAES.keyGen(128, secretKey);
                                        byte[] myCipherAES = cipherAES.encrypt(archByte, secretKeyAES);
                                        System.out.println(ECNR[enc] + " - " + listado[i]);
                                        //Fin de tiempo de cifrado
                                        fincif = System.currentTimeMillis();
                                        //Inicio de tiempo de descifrado
                                        iniciodes = System.currentTimeMillis();
                                        //Descifrado
                                        byte[] myDEs = cipherAES.decrypt(myCipherAES, secretKeyAES);
                                        if (!AES128.exists()) {
                                            if (AES128.mkdirs()) {
                                                archivoCifrado(myDEs, dir + "/Pruebas/AES128/AES128-" + listado[i]);
                                            }
                                        } else {
                                            archivoCifrado(myDEs, dir + "/Pruebas/AES128/AES128-" + listado[i]);
                                        }
                                        //Fin de inicio de descrifrado
                                        findes = System.currentTimeMillis();
                                        System.out.println("Cifrado: " + (fincif - iniciocif) + "\t| Descifrado: " + (findes - iniciodes) + "\n");
                                        //Tiempos en el csv
                                        long cif = (fincif - iniciocif);
                                        long desc = (findes - iniciodes);
                                        fw.append(ECNR[enc]).append(",");
                                        fw.append(listado[i]).append(",");
                                        fw.append(""+new File(dir + "/" + listado[i]).length()).append(",");
                                        fw.append("" + cif).append(",");
                                        fw.append("" + desc).append("\n");
                                    }
                                }
                                // Tomamos el tiempo de fin de la ejecucion
                                fin = System.currentTimeMillis();

                                // calculamos el tiempo de ejecucion
                                tiempo = (double) ((fin - inicio) / 1000);
                                tiempo = (double) ((fin - inicio));
                                System.out.println(" " + tiempo);
                                Tiempos[enc] = tiempo;
                                break;
                            case 3:
                                System.out.println("-----------------------");
                                inicio = System.currentTimeMillis();
                                File AES192 = new File(dir + "/Pruebas/AES192");
                                for (int i = 0; i < listado.length; i++) {
                                    if (new File(dir + "/" + listado[i]).isFile()) {
                                        //Inicio de tiempo de cifrado
                                        iniciocif = System.currentTimeMillis();
                                        //Cifrado
                                        byte[] archByte = readFileToBytes(dir + "/" + listado[i]);
                                        String secretKey = "secret123";
                                        Ciphers cipherAES = new MyAES();
                                        byte[] secretKeyAES = cipherAES.keyGen(192, secretKey);
                                        byte[] myCipherAES = cipherAES.encrypt(archByte, secretKeyAES);
                                        System.out.println(ECNR[enc] + " - " + listado[i]);
                                        //Fin de tiempo de cifrado
                                        fincif = System.currentTimeMillis();
                                        //Inicio de tiempo de descifrado
                                        iniciodes = System.currentTimeMillis();
                                        //Descifrado
                                        byte[] myAES = cipherAES.decrypt(myCipherAES, secretKeyAES);
                                        if (!AES192.exists()) {
                                            if (AES192.mkdirs()) {
                                                archivoCifrado(myAES, dir + "/Pruebas/AES192/AES192-" + listado[i]);
                                            }
                                        } else {
                                            archivoCifrado(myAES, dir + "/Pruebas/AES192/AES192-" + listado[i]);
                                        }
                                        //Fin de inicio de descrifrado
                                        findes = System.currentTimeMillis();
                                        System.out.println("Cifrado: " + (fincif - iniciocif) + "\t| Descifrado: " + (findes - iniciodes) + "\n");
                                        //Tiempos en el csv
                                        long cif = (fincif - iniciocif);
                                        long desc = (findes - iniciodes);
                                        fw.append(ECNR[enc]).append(",");
                                        fw.append(listado[i]).append(",");
                                        fw.append(""+new File(dir + "/" + listado[i]).length()).append(",");
                                        fw.append("" + cif).append(",");
                                        fw.append("" + desc).append("\n");
                                    }
                                }
                                // Tomamos el tiempo de fin de la ejecucion
                                fin = System.currentTimeMillis();

                                // calculamos el tiempo de ejecucion
                                tiempo = (double) ((fin - inicio) / 1000);
                                tiempo = (double) ((fin - inicio));
                                System.out.println(" " + tiempo);
                                Tiempos[enc] = tiempo;
                                break;
                            case 4:
                                System.out.println("-----------------------");
                                inicio = System.currentTimeMillis();
                                File AES256 = new File(dir + "/Pruebas/AES256");
                                for (int i = 0; i < listado.length; i++) {
                                    if (new File(dir + "/" + listado[i]).isFile()) {
                                        //Inicio de tiempo de cifrado
                                        iniciocif = System.currentTimeMillis();
                                        //Cifrado
                                        byte[] archByte = readFileToBytes(dir + "/" + listado[i]);
                                        String secretKey = "secret123";
                                        Ciphers cipherAES = new MyAES();
                                        byte[] secretKeyAES = cipherAES.keyGen(256, secretKey);
                                        byte[] myCipherAES = cipherAES.encrypt(archByte, secretKeyAES);
                                        System.out.println(ECNR[enc] + " - " + listado[i]);
                                        //Fin de tiempo de cifrado
                                        fincif = System.currentTimeMillis();
                                        //Inicio de tiempo de descifrado
                                        iniciodes = System.currentTimeMillis();
                                        //Descifrado
                                        byte[] myDEs = cipherAES.decrypt(myCipherAES, secretKeyAES);
                                        if (!AES256.exists()) {
                                            if (AES256.mkdirs()) {
                                                archivoCifrado(myDEs, dir + "/Pruebas/AES256/AES256-" + listado[i]);
                                            }
                                        } else {
                                            archivoCifrado(myDEs, dir + "/Pruebas/AES256/AES256-" + listado[i]);
                                        }
                                        //Fin de inicio de descrifrado
                                        findes = System.currentTimeMillis();
                                        System.out.println("Cifrado: " + (fincif - iniciocif) + "\t| Descifrado: " + (findes - iniciodes) + "\n");
                                        //Tiempos en el csv
                                        long cif = (fincif - iniciocif);
                                        long desc = (findes - iniciodes);
                                        fw.append(ECNR[enc]).append(",");
                                        fw.append(listado[i]).append(",");
                                        fw.append(""+new File(dir + "/" + listado[i]).length()).append(",");
                                        fw.append("" + cif).append(",");
                                        fw.append("" + desc).append("\n");
                                    }
                                }
                                // Tomamos el tiempo de fin de la ejecucion
                                fin = System.currentTimeMillis();

                                // calculamos el tiempo de ejecucion
                                tiempo = (double) ((fin - inicio) / 1000);
                                tiempo = (double) ((fin - inicio));
                                System.out.println(" " + tiempo);
                                Tiempos[enc] = tiempo;
                                break;
                            default:
                                break;
                            // Declaraciones
                        }
                        // almacenamos el tiempo
                        enc++;
                    };
                    fw.flush();
                    fw.close();
                }
            }
            /*
        //Pruebas ----
        byte[] archByte = "Hola_como_estas".getBytes();
        String secretKey = "secret123";/*
        
        Ciphers cipherDES = new MyDES();
        byte[] secretKeyDES = cipherDES.keyGen(64, secretKey);
        byte[] myCipherDES = cipherDES.encrypt(archByte, secretKeyDES);
        send(myCipherDES);
        byte[] miDescipherDES = cipherDES.decrypt(myCipherDES, secretKeyDES);
        show(miDescipherDES);
        // 3DES
        Ciphers cipher3DES = new My3DES();
        byte[] secretKey3DES = cipher3DES.keyGen(192, secretKey);
        byte[] myCipher3DES = cipher3DES.encrypt(archByte, secretKey3DES);
        send(myCipher3DES);
        byte[] miDescipher3DES = cipher3DES.decrypt(myCipher3DES, secretKey3DES);
        show(miDescipher3DES);
        
        Ciphers cipherAES = new MyAES();
        byte[] secretKeyAES = cipherAES.keyGen(128, secretKey);
        byte[] myCipherAES = cipherAES.encrypt(archByte, secretKeyAES);
        send(myCipherAES);
        byte[] myDEs = cipherAES.decrypt(myCipherAES, secretKeyAES);
        show(myDEs);*/

        }
    }
}
