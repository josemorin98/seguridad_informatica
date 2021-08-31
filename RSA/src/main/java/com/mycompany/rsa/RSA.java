package com.mycompany.rsa;

/*
 * RSA.java
 *
 * Creado 24 de octubre de 2007, 12:02 PM
 *
 */
import java.math.BigInteger;
import java.util.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Algunas personas
 */
public class RSA {

   int tamPrimo;           //numero de bits de las claves privadas RSA
	
   BigInteger n, q, p;     // n = p*q, note que 'n' es mucho más grande que p y q (la suma de los tamaños de cada uno).
   BigInteger totient;
   BigInteger pub, priv;        //llaves pública y privada

 /** Constructor de la clase RSA */
   public RSA(int tamPrimo) {
      this.tamPrimo = tamPrimo;
      generaPrimos();             //Genera p y q
      KeyGen();                   //Genera e y d
   }
 
   public void generaPrimos()
   {
      p = new BigInteger(tamPrimo, 10, new Random());
      do q = new BigInteger(tamPrimo, 10, new Random());
         while(q.compareTo(p)==0);
   }
 
   public void KeyGen()
   {
     // n = p * q
      n = p.multiply(q);
     // toltient = (p-1)*(q-1)
      totient = p.subtract(BigInteger.valueOf(1));
      totient = totient.multiply(q.subtract(BigInteger.valueOf(1)));
     // Elegimos un e coprimo de y menor que n
      do pub = new BigInteger(2 * tamPrimo, new Random());
         while((pub.compareTo(totient) != -1) ||
      (pub.gcd(totient).compareTo(BigInteger.valueOf(1)) != 0));
     // d = e^1 mod totient
      priv = pub.modInverse(totient);
   }
 
 /**
  * Cifra el texto usando la clave pública
  *
  * @param   mensaje     Ristra que contiene el mensaje a cifrar
  * @return   El mensaje cifrado como un BigInteger
       */
  
  
   public BigInteger cifrar(byte[] mensaje, BigInteger e, BigInteger n)
   {
      int i;
      BigInteger pt = new BigInteger(mensaje);
      BigInteger ctNumber = pt.modPow(e,n);
     
      return ctNumber;
   }
 
 /**
  * Descifra el texto cifrado usando la clave privada
  *
  * @param   cifrado       BigInteger que contiene el texto cifrado
  *                           que será descifrado
  * @return  The decrypted String plaintext
  */
  
   public byte[] descifrar(BigInteger ct, BigInteger d, BigInteger n) {
       
      BigInteger pt = ct.modPow(d,n);
     
      return(pt.toByteArray());
   }
   
   public byte[] sign(File mensaje, BigInteger d, BigInteger n) throws NoSuchAlgorithmException, IOException{
       long inicio = System.currentTimeMillis();
       MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
       String shaChecksum = getFileChecksum(shaDigest,mensaje);
       BigInteger m = new BigInteger(shaChecksum.getBytes());
       
       BigInteger s = m.modPow(d, n);
       return(s.toByteArray());
   }
   
   public String verify(File mensaje, byte[] s, BigInteger e, BigInteger n) throws NoSuchAlgorithmException, IOException{
       MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
       String shaChecksum = getFileChecksum(shaDigest,mensaje);
       BigInteger h = new BigInteger(shaChecksum.getBytes());
       BigInteger s_p = new BigInteger(s);
       BigInteger h_p = s_p.modPow(e, n);
       if (h_p.equals(h)) {
           return "Valid";
       }else{
           return "Invalid";
       }
   }
 
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

   public BigInteger getp() {
      return(p);}
   public BigInteger getq() {
      return(q);}
   public BigInteger gettotient() {
      return(totient);}
   public BigInteger getn() {
      return(n);}
   public BigInteger getPub() {
      return(pub);}
   public BigInteger getPriv() {
      return(priv);}

}
