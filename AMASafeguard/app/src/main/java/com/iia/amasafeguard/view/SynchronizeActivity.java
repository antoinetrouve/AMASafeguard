package com.iia.amasafeguard.view;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.iia.amasafeguard.R;
import com.iia.amasafeguard.entity.Ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SynchronizeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronize);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        new myAsyncTask().execute();
    }

    class myAsyncTask extends AsyncTask<String, Void, Boolean> {

        private FTPClient client;

        protected Boolean doInBackground(String... urls) {
            try {
                Log.d("PASSE", "ICI");

                String filename = "/Test/test.txt";
                String filepath = Environment.getDataDirectory().getPath() + filename;
                File file = new File(filepath);

                client = Ftp.FtpConnection();
                boolean success = false;
                client.changeWorkingDirectory("test");
                int returnCode = client.getReplyCode();

                if(returnCode == 500){
                    success = client.makeDirectory("test");
                }

                client.logout();

                if(success){
                    return true;
                }
                client.disconnect();
            } catch (IOException e) {
                //Log.e("FTP", e.toString());
                e.printStackTrace();
            } catch (Throwable e) {
                Log.d("PASSE", "CATCH");
            }
            return false;
        }

        protected void onPostExecute(Boolean b) {
            // TODO: check this.exception
            // TODO: do something with the feed
            if(b){
                Toast.makeText(SynchronizeActivity.this,"Dossier crée !!",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(SynchronizeActivity.this,"Antoine a fait de la merde!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
