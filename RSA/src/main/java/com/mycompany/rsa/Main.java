/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rsa;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
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
    

    public static void main(String args[]) throws Exception {

        //BufferedReader f = new BufferedReader(new InputStreamReader(System.in));
        RSA rsa = new RSA(3072);

        System.out.println("\n n = " + rsa.getn());
        System.out.println("\n e = " + rsa.getPub());
        System.out.println("\n d = " + rsa.getPriv());

        /*System.out.println("Enter the plain text: ");
      String s = f.readLine();*/
 /*String s = "Hola como estas quiero decrite que esta es un prueba de rsa con diferentes niveles de seguirdad para ver el rendimiento del algormito asi como los tiempos que genrra los niveles de seguiridad uno dos tres cuatro cinco seis siete ocho nueve diez once doce t";
        
        System.out.println("Encryption started, please wait...");
        long inicio = System.currentTimeMillis();

        BigInteger cifradoNumber = rsa.cifrar(s.getBytes(), rsa.gete(), rsa.getn());
        //System.out.println(cifradoNumber);
        System.out.println("Done!");
        long finCifrado = System.currentTimeMillis();
        long totalCifrado = (finCifrado - inicio);
        System.out.println("Tiempo de cifrado = " + totalCifrado);

        inicio = System.currentTimeMillis();
        System.out.print("\nDecryption started, please wait...");
        String descifrado = new String(rsa.descifrar(cifradoNumber, rsa.getd(), rsa.getn()));
        System.out.println("Done!\n");
        long finDescifrado = System.currentTimeMillis();
        long totalDescifrado = finDescifrado - inicio;
        System.out.println("Tiempo de cifrado = " + totalDescifrado);
        System.out.println("Decrypted: " + descifrado);
        
         */
        File file = new File("/home/morin/Descargas/1998smcGeneralizedDunnIndices.pdf");
        //File file = new File("/home/morin/Documentos/Trabajos/Shapes/shapefiles_service-master/app/shapefiles/ecorregiones/ecort08gw.shp");
        //File file = new File("/home/morin/Vídeos/vokoscreenNG-2021-02-08_12-15-17.mkv");
        long inicio = System.currentTimeMillis();
        byte[] s = rsa.sign(file, rsa.getPriv(), rsa.getn());
        System.out.println("Firma");
        //System.out.println(new String(s));
        long finH = System.currentTimeMillis();
        System.out.println("Sign - " + (inicio - finH));
        
        byte[] archByte = readFileToBytes("/home/morin/Descargas/1998smcGeneralizedDunnIndices.pdf");
        System.out.println("Encryption started, please wait...");
        inicio = System.currentTimeMillis();
        String men = file.toString();
        BigInteger cifradoFile = rsa.cifrar(archByte, rsa.getPub(), rsa.getn());
        System.out.println("Done!");
        long finCifrado = System.currentTimeMillis();
        long totalCifrado = (finCifrado - inicio);
        System.out.println("Tiempo de cifrado = " + totalCifrado);
        
        inicio = System.currentTimeMillis();
        System.out.println("\n Verificar");
        String verificar = rsa.verify(file, s, rsa.getPub(), rsa.getn());
        System.out.println(verificar);
        long finV = System.currentTimeMillis();
        System.out.println("Verificar - " + (inicio - finH));
        
        String descifrado = new String(rsa.descifrar(cifradoFile, rsa.getPriv(), rsa.getn()));
        if(men.equals(descifrado)){
            System.out.println("Si es");
        }else{
            System.out.println("Noes :c");
        }
        
    }
}
