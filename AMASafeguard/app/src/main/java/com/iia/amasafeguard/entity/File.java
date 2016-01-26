package com.iia.amasafeguard.entity;

import org.apache.commons.net.ftp.FTPClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by antoine on 20/01/2016.
 */
public class File{

    /**
     *
     * @param ftpClient
     * @param downloadFile local file which need to be uploaded.
     * @param serverfilePath
     */
    public static void Uploadfile(FTPClient ftpClient,File downloadFile,String serverfilePath){
        try {
            FileInputStream srcFileStream = new FileInputStream(String.valueOf(downloadFile));
            ftpClient.storeFile(serverfilePath, srcFileStream);
            srcFileStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
