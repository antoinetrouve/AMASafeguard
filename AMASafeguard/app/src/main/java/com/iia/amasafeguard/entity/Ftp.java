package com.iia.amasafeguard.entity;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;

/**
 * Created by antoine on 20/01/2016.
 */
public class Ftp extends Activity implements OnClickListener {

    private static final String FTP_HOST = "172.20.10.4";
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

    /**
     * Synchronize data if change detected
     * @param ftpclient
     * @return null
     */
    public static void Synchronize(FTPClient ftpclient){
        //Send file modified (or created) to ftp
        File downloadfile = new File();
        String filepath = "chemin de destination sur srv ftp";
        File.Uploadfile(ftpclient,downloadfile,filepath);

        //File deleted into device must be deleted into ftp server
        try {
            FTPFile deletefile = new FTPFile();
            ftpclient.deleteFile(deletefile.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Asynchrone task to manage synchronization
     */
    /*public class LoadTask extends AsyncTask<Void, Void, Boolean>{

        private Context ctx;
        private Boolean is_connected;*/

        /**
         *
         * @param context
         */
        /*public LoadTask(Context context) {
            this.ctx = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                FTPClient ftpclient = Ftp.FtpConnection();
                if(ftpclient.isConnected()){
                    Ftp.Synchronize(ftpclient);
                    this.is_connected = true;
                }
                else {
                    this.is_connected = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return this.is_connected;
        }

        @Override
        protected void onPostExecute(Boolean is_connected) {
            if (!is_connected){
                Toast.makeText(this.ctx, "Connection to server impossible !", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(is_connected);
        }
    }*/
}



