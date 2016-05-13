package com.iia.amasafeguard.entity;

import android.content.Context;
import android.graphics.Path;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.io.ToNetASCIIInputStream;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static android.os.Environment.getDataDirectory;

/**
 * Manage encrypted data
 * Created by antoine on 20/01/2016.
 */
public class Utils{

    public static final byte[] CONST_KC = new byte[16];
    public static final String CONST_AMASAFEGUARD = "Amasafeguard";
    public static final String CONST_FILETEMP = "temp";


    /**
     * Generate encrypted key
     * @param client
     * @param file
     * @param uuid
     * @return file encrypted
     */
    public static File protectSymetricFile(FTPClient client, File file, String uuid)
    {
        SecureRandom sr = new SecureRandom();

        // 01. Deriv password : generate salt
        byte[] salt = new byte[8];
        sr.nextBytes(salt);

         //03. cipher data
        // 03-a. generate iv
        byte[] iv = new byte[16];
        sr.nextBytes(iv);

        // 03-b. read file
        byte[] plainData;
        plainData = ReadSettings(file).getBytes();

        // 03-c. compute encryption
        final byte[] encryptedData = encrypt(plainData, CONST_KC, iv);

        //04- Create encrypted temp file
        File fileCypher = CreateFileTemp(encryptedData,client, uuid, file.getName());

        return fileCypher;
    }

    /**
     * Read file
     * @return content
     */
    public static String ReadSettings(File confFile) {
        String content = "";
        String line = "";

        try {
            InputStream ips = new FileInputStream(confFile);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);

            while ((line = br.readLine()) != null) {
                content += line;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * Encrypt file
     * @param input
     * @param key
     * @param iv
     * @return byte[]
     */
    public static final byte[] encrypt(final byte[] input, final byte[] key, final byte[] iv)
    {
        try
        {
            // Cipher
            Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            // Key
            SecretKey aesKey = new SecretKeySpec(key, "AES");
            // Initialization Vector Required for CBC
            IvParameterSpec ips = new IvParameterSpec(iv);
            // Initialize the Cipher with key and parameters
            encryptCipher.init(Cipher.ENCRYPT_MODE, aesKey, ips);

            // Encrypt !
            final byte[] cipherText = encryptCipher.doFinal(input);

            return cipherText;
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Create temporary file
     * @param encryptedContent
     * @param client
     * @param uuid
     * @param fileName
     * @return file
     */
    public static File CreateFileTemp(byte[] encryptedContent, FTPClient client, String uuid, String fileName){
        FileOutputStream output;
        String pathFileAmasafeguard = Environment.getExternalStorageDirectory() + File.separator + CONST_AMASAFEGUARD + File.separator + CONST_FILETEMP;

        File file = new File(pathFileAmasafeguard,fileName);

        try {
            output = new FileOutputStream(file,true);
            output.write(encryptedContent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
