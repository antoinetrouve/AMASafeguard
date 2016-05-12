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
import com.iia.amasafeguard.entity.Utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SynchronizeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronize);

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        Bundle b = this.getIntent().getExtras();
        String uuid = b.getString("UUID");

        new myAsyncTask(uuid).execute();
    }

    class myAsyncTask extends AsyncTask<String, Void, Boolean> {

        private FTPClient client;
        private String uuid;

        //Constructor to get uuid user
        public myAsyncTask(String uuid) {
            this.uuid = uuid;
        }

        protected Boolean doInBackground(String... urls) {
            try {
                String filename = "/Test/test.txt";
                String filepath = Environment.getDataDirectory().getPath() + filename;
                File file = new File(filepath);

                //CONNECTION with login and password
                client = Ftp.FtpConnection();

                boolean success = false;

                //Verify FTP connection
                int returnCode = client.getReplyCode();
                if(!FTPReply.isPositiveCompletion(returnCode)){
                    System.out.println("Connect failed");
                    return false;
                }

                //if directory doesn't exist make it
                if(!client.changeWorkingDirectory("/Amasafeguard/" + this.uuid)) {
                    client.makeDirectory("/Amasafeguard/" + this.uuid);
                }

                Utils.protectSymetricFile(client);

                client.logout();
                client.disconnect();

                return true;
            } catch (IOException e) {
                //Log.e("FTP", e.toString());
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean b) {
            // TODO: check this.exception
            // TODO: do something with the feed
            if(b){
                Toast.makeText(SynchronizeActivity.this,"Dossier crée antoine est chaud !!",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(SynchronizeActivity.this,"Antoine a fait de la merde!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
