package com.iia.amasafeguard.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by Antoine Trouvé on 26/01/2016.
 */
public class Generator {

    //Hashing password
    //convert hashing password to string
    //derivation hashed password km
    //derivation hashed password into 1 keys (ka)

    /**
     * Hashing function
     * @param password
     * @return byte[] digest
     */
    public static final byte[] sha256(final String password){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            final byte[] digest = md.digest(password.getBytes());
            return digest;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert byte to string
     * @param data
     * @return String
     */
    public static String toHexString(byte[] data)
    {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < data.length; i++)
        {
            String hex = Integer.toHexString(0xff & data[i]);
            if(hex.length() == 1)
            {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * derivation hashed password function
     * @param password
     * @param count
     * @param salt
     * @return byte[] km
     */
    public static final byte[] derivKm(String password, int count, byte[] salt){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            //Initialization of H[0]
            md.update(salt);
            md.update(password.getBytes());
            byte[] h = md.digest();

            //H[i]
            for(int i = 1 ; i < count; i++){
                md.reset();
                md.update(salt);
                md.update(String.valueOf(i).getBytes());
                md.update(password.getBytes());
                md.update(h);
                h = md.digest();
            }
            return Arrays.copyOfRange(h, 0, 16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * derivation function km[] into ka[]
     * @param km
     * @return byte[] ka
     */
    public static final byte[] derivKa(byte[] km){
        byte[] ka;
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(km);
            md.update(String.valueOf(0).getBytes());
            ka = md.digest();
            return Arrays.copyOfRange(ka, 0, 16);

        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
    }

}
