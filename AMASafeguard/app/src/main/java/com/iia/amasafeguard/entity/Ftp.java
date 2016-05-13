package com.iia.amasafeguard.entity;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by Antoine Trouvé on 20/01/2016.
 */
public class Ftp extends Activity implements OnClickListener {

    private static final String FTP_HOST = "192.168.100.58";
    private static final String FTP_USER = "amasafeguard";
    private static final String FTP_PWD = "amasafeguard";


    @Override
    public void onClick(View v) {
        // check changement between mobile and database local
        // if changement detected
        // begin LoadTask
    }

    /**
     * Connection to ftp server
     * @throws IOException
     */
    public static FTPClient FtpConnection() throws IOException {
        FTPClient ftpclient = new FTPClient();
        try {
            ftpclient.connect(FTP_HOST);
            ftpclient.login(FTP_USER,FTP_PWD);
            return ftpclient;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}



