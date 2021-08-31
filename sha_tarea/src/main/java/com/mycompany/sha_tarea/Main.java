/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sha_tarea;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author morin
 */
public class Main {

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        //Creamos un inputFile para poder leer el archivo del directorio
        FileInputStream fis = new FileInputStream(file);

        //Ccreamos un vetor de bytes para leer el archivo
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //leemos el archivo y acutalizamos el hash con los bytes del archivo
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        //cerramos la lectura
        fis.close();

        //Obtenemos el sha de los bytes actualizados
        byte[] bytes = digest.digest();

        //Se convierte en un string de hexadeciamles
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //retornamos el resutado
        return sb.toString();
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        // Tipo de SHA's a utilizar y almacenar el tiempo
        String[] shas = {"SHA-1", "SHA-224", "SHA-256", "SHA-512"};
        double[] shasT = {0.0, 0.0, 0.0, 0.0};

        // Seleccionamos la direccion de la carpeta
        String dir = "/home/morin/Escritorio/Pruebas-sha";
        dir = args[0];

        // Creamos un File para ubicarnos en la carpeta
        // Tomamos la lista de los elementos que estan en la carpeta
        File carpeta = new File(dir);
        if (carpeta.isDirectory()) {
            String[] listado = carpeta.list();

            double tiempoTotal = 0.0; // Variable para el tiempo total

            // Condicionamos si tiene valores o no
            if (listado == null || listado.length == 0) {
                    System.out.println(" No hay elementos dentro de la carpeta actual");
                    return;
            } else {
                // Si tiene valores entonces se itera el array en donde estan los diferentes sha
                System.out.println("\tDirectorio: " + dir);
                for (int j = 0; j < shas.length; j++) {
                    // Contavilizamos el tiempo de ejecucion
                    long inicio = System.currentTimeMillis();

                    // Creamos un MessageDigest para poder cifrar
                    MessageDigest shaDigest = MessageDigest.getInstance(shas[j]);

                    // Iteramos lo archivos del directporio dado
                    for (int i = 0; i < listado.length; i++) {
                        //System.out.println(listado[i]);
                        // Creamos un File del archivo iterado
                        File arch = new File(dir + "/" + listado[i]);

                        // Llamamos la funcion de cifrado y la almacenamos
                        String shaChecksum = getFileChecksum(shaDigest, arch);
                        // Desplegamos el resultado
                        //System.out.println(shaChecksum);
                    }
                    System.out.println("------ " + shas[j] + " REALIZADO ------");
                    // Tomamos el tiempo de fin de la ejecucion
                    long fin = System.currentTimeMillis();

                    // calculamos el tiempo de ejecucion
                    double tiempo = (double) ((fin - inicio) / 1000);
                    shasT[j] = tiempo;

                    // almacenamos el tiempo
                    tiempoTotal = tiempoTotal + tiempo;
                }

                //Desplegmos los tiempos individuales y el total
                for (int i = 0; i < shas.length; i++) {
                    System.out.println("\t" + shas[i] + ": " + shasT[i] + " seg.");
                }
                System.out.println("\tTotal: " + tiempoTotal + " seg.");
            }
        }else{
            System.out.println("No es un directorio.");
        }
    }

}
