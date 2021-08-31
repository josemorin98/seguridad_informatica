/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.biginteger;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author morin
 */
public class Main {
    public static void main(String[] args) {
     // BigInteger object to store result 
        BigInteger sum; 
        
        // Convert the string input to BigInteger 
        BigInteger p 
            = new BigInteger("0100000000000000000001F4C8F927AED3CA752257",16); 
        BigInteger r 
            = new BigInteger("1176954224688105769566774212902092897866168635793"); 
        BigInteger s
            = new BigInteger("299742580584132926933316745664091704165278518100");
        // Using add() method 
        
        System.out.println("p - " + p);
        System.out.println("r - " + r);
        System.out.println("s - " + s);
        sum = r.multiply(s.modInverse(p)).mod(p);
        
  
        // Display the result in BigInteger 
        System.out.println(
                sum + "\n"); 
        
    }
}
