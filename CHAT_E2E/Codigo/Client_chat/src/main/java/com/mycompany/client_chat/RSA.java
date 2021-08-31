package com.mycompany.client_chat;

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

    String sha_type;
    int tamPrimo;           //numero de bits de las claves privadas RSA
    BigInteger n, q, p;     // n = p*q, note que 'n' es mucho más grande que p y q (la suma de los tamaños de cada uno).
    BigInteger totient;
    BigInteger pub, priv;        //llaves pública y privada

    /**
     * Constructor de la clase RSA
     */
    public RSA(int tamPrimo) {
        this.tamPrimo = tamPrimo;
        generaPrimos();             //Genera p y q
        KeyGen();                   //Genera e y d
        switch (this.tamPrimo) {
            case 2048:
                this.sha_type = "SHA-224";
                break;
            case 3072:
                this.sha_type = "SHA-256";
                break;
            case 7680:
                this.sha_type = "SHA-384";
                break;
            case 15360:
                this.sha_type = "SHA-512";
                break;
            case 1024:
                this.sha_type = "SHA-1";
                break;
            default:
                this.sha_type = "SHA-1";
                break;
        }
    }

    public void generaPrimos() {
        p = new BigInteger(tamPrimo, 10, new Random());
        do {
            q = new BigInteger(tamPrimo, 10, new Random());
        } while (q.compareTo(p) == 0);
    }

    public void KeyGen() {
        // n = p * q
        n = p.multiply(q);
        // toltient = (p-1)*(q-1)
        totient = p.subtract(BigInteger.valueOf(1));
        totient = totient.multiply(q.subtract(BigInteger.valueOf(1)));
        // Elegimos un e coprimo de y menor que n
        do {
            pub = new BigInteger(2 * tamPrimo, new Random());
        } while ((pub.compareTo(totient) != -1)
                || (pub.gcd(totient).compareTo(BigInteger.valueOf(1)) != 0));
        // d = e^1 mod totient
        priv = pub.modInverse(totient);
    }

    /**
     * Cifra el texto usando la clave pública
     *
     * @param mensaje Ristra que contiene el mensaje a cifrar
     * @return El mensaje cifrado como un BigInteger
     */
    public BigInteger cifrar(byte[] mensaje, BigInteger e, BigInteger n) {
        int i;
        BigInteger pt = new BigInteger(mensaje);
        BigInteger ctNumber = pt.modPow(e, n);

        return ctNumber;
    }

    /**
     * Descifra el texto cifrado usando la clave privada
     *
     * @param cifrado BigInteger que contiene el texto cifrado que será
     * descifrado
     * @return The decrypted String plaintext
     */
    public byte[] descifrar(BigInteger ct, BigInteger d, BigInteger n) {

        BigInteger pt = ct.modPow(d, n);

        return (pt.toByteArray());
    }

    public BigInteger sign(byte[] mensaje, BigInteger d, BigInteger n) throws NoSuchAlgorithmException, IOException {
        MessageDigest shaDigest = MessageDigest.getInstance(this.sha_type);
        byte[] sha = shaDigest.digest(mensaje);
        BigInteger m = new BigInteger(1,sha);
        BigInteger s = m.modPow(d, n);
        return s;
    }

    public boolean verify(byte[] mensaje, BigInteger sign, BigInteger e, BigInteger n) throws NoSuchAlgorithmException, IOException {
        MessageDigest shaDigest = MessageDigest.getInstance(this.sha_type);
        byte[] sha = shaDigest.digest(mensaje);
        BigInteger h = new BigInteger(1,sha);
        BigInteger s_p = sign;
        BigInteger h_p = s_p.modPow(e, n);
        if (h_p.equals(h)) {
            return true;
        } else {
            return false;
        }
    }

    public BigInteger getp() {
        return (p);
    }

    public BigInteger getq() {
        return (q);
    }

    public BigInteger gettotient() {
        return (totient);
    }

    public BigInteger getn() {
        return (n);
    }

    public BigInteger getPub() {
        return (pub);
    }

    public BigInteger getPriv() {
        return (priv);
    }

}
