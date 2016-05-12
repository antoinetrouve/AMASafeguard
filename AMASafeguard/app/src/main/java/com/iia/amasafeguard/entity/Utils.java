package com.iia.amasafeguard.entity;

import android.graphics.Path;
import android.os.Environment;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

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
 * Created by antoine on 20/01/2016.
 */
public class Utils{

    public static final byte[] CONST_KC = new byte[16];

    /**
     *
     * @param ftpClient
     * @param uploadFile (local file which need to be uploaded).
     * @param tempFileName
     */
    public static void Uploadfile(FTPClient ftpClient,File uploadFile,String tempFileName){
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            InputStream inputStream = new FileInputStream(uploadFile);
            boolean done = ftpClient.storeFile(tempFileName, inputStream);
            inputStream.close();
            if (done){
                System.out.println("Succès de UploadFile");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean protectSymetricFile(FTPClient client, File file)
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
        CreateFileTemp(encryptedData,client);

        return true;
    }

    /**
     * Read file
     * @return content
     */
    public static String ReadSettings(File confFile) {
        String content = "";
        ArrayList<File> arrayFile = new ArrayList();

        try {
            InputStream ips = new FileInputStream(confFile);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;

            while ((ligne = br.readLine()) != null) {
                content += ligne;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

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

    public static final byte[] mac_protect(final byte[] input, final byte[] key)
    {
        try
        {
            // Mac
            Mac hMac = Mac.getInstance("HmacSHA256");
            // key
            SecretKey macKey = new SecretKeySpec(key, "HMACSHA256");
            // Initialize the Mac with key
            hMac.init(macKey);

            // Compute HMAC value
            final byte[] macDigest = hMac.doFinal(input);

            return macDigest;
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static void CreateFileTemp(byte[] encryptedContent, FTPClient client){
        FileOutputStream output;
        boolean success = true;

        File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Amasafeguard");
        if(!myDir.exists()){
            success = myDir.mkdir();
            myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Amasafeguard" + File.separator + "conf");
            if (!myDir.exists()){
                success = myDir.mkdir();
            }
            myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Amasafeguard" + File.separator + "temp");
            if (!myDir.exists()){
                success = myDir.mkdir();
            }
        }

        String pathFileAmasafeguard = Environment.getExternalStorageDirectory() + File.separator + "Amasafeguard" + File.separator + "temp";
        String tempFileName = "TempFiles.txt";
        File file = new File(pathFileAmasafeguard,tempFileName);

        if(success){
            try {
                output = new FileOutputStream(file,true);
                output.write(encryptedContent);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Uploadfile(client,file,tempFileName);
        }
    }
}
