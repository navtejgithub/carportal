package com.carportal;

import java.security.SecureRandom;
import java.util.HexFormat;

public class Test {

    public static void main(String [] args){
        SecureRandom secureRandom=new SecureRandom();
        byte[] key=new byte[32];
        secureRandom.nextBytes(key);
        System.out.println(HexFormat.of().formatHex(key));


    }
}
