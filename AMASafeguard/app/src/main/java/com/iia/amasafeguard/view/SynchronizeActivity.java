package com.iia.amasafeguard.view;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.iia.amasafeguard.R;
import com.iia.amasafeguard.data.DataSQLiteAdapter;
import com.iia.amasafeguard.entity.Data;
import com.iia.amasafeguard.entity.Ftp;
import com.iia.amasafeguard.entity.Utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SynchronizeActivity extends AppCompatActivity {

    private static final String PATH_CONF_FILE = "/Amasafeguard/conf/conf.txt";
    Button btSynchronize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronize);

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        btSynchronize = (Button)this.findViewById(R.id.btSynchronize);

        Bundle b = this.getIntent().getExtras();
        String uuid = b.getString("UUID");

        new myAsyncTask(uuid).execute();
    }

    class myAsyncTask extends AsyncTask<String, Void, Boolean> {

        private FTPClient client;
        private String uuid;
        FileOutputStream output;

        //Constructor to get uuid user
        public myAsyncTask(String uuid) {
            this.uuid = uuid;
        }

        protected Boolean doInBackground(String... urls) {
           try {

               String pathFileAmasafeguard = Environment.getExternalStorageDirectory() + File.separator + "Amasafeguard" + File.separator + "conf";

               File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Amasafeguard");
               if(!myDir.exists()){
                   myDir.mkdir();
                   myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Amasafeguard" + File.separator + "conf");
                   if (!myDir.exists()){
                       myDir.mkdir();
                   }
                   myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Amasafeguard" + File.separator + "temp");
                   if (!myDir.exists()){
                       myDir.mkdir();
                   }
               }

               File file = new File(pathFileAmasafeguard, "conf.txt");
               if (!file.exists()){
                   byte[] confText = file.getPath().getBytes();
                   output = new FileOutputStream(file,true);
                   output.write(confText);
                   output.close();
               }

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

                return true;
            } catch (IOException e) {
                //Log.e("FTP", e.toString());
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return true;
        }

        protected void onPostExecute(Boolean b) {

            btSynchronize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Récupération du fichier de conf
                    File confFile = new File(Environment.getExternalStorageDirectory().getPath() + PATH_CONF_FILE);
                    String content ="";
                    ArrayList<File> arrayFile = new ArrayList();

                    try{
                        InputStream ips = new FileInputStream(confFile);
                        InputStreamReader ipsr = new InputStreamReader(ips);
                        BufferedReader br = new BufferedReader(ipsr);
                        String ligne;

                        while ((ligne=br.readLine())!=null){
                            File file = new File(ligne);
                            if (file.exists()){
                                arrayFile.add(file);
                            }else{
                                Toast.makeText(SynchronizeActivity.this, "Le dossier " + file.getName() + " n'éxiste pas !", Toast.LENGTH_LONG).show();
                            }
                        }
                        br.close();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    DataSQLiteAdapter dataSQLiteAdapter = new DataSQLiteAdapter(SynchronizeActivity.this);
                    dataSQLiteAdapter.open();

                    //Boucle foreach sur le tableau de File
                    for (File file: arrayFile) {

                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        Data fileDb = dataSQLiteAdapter.getDataByPath(file.getPath());

                        if (fileDb == null){
                            fileDb = new Data();
                            fileDb.setName(file.getName());
                            fileDb.setPath(file.getPath());
                            fileDb.setCreated_at(sdf.format(file.lastModified()));
                            fileDb.setUpdated_at(sdf.format(file.lastModified()));

                            dataSQLiteAdapter.insert(fileDb);
                            Utils.protectSymetricFile(client, file);
                        }

                        if (!fileDb.getUpdated_at().equals(sdf.format(file.lastModified()))){
                            dataSQLiteAdapter.update(fileDb);
                            Utils.protectSymetricFile(client, file);
                        }

                        try {
                            client.logout();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            client.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    dataSQLiteAdapter.close();

                    Toast.makeText(SynchronizeActivity.this, "Dossier synchronisé avec la DB !", Toast.LENGTH_LONG).show();
                }
            });


            // TODO: check this.exception
            // TODO: do something with the feed
            if(b){
                Toast.makeText(SynchronizeActivity.this,"Succès de l'envoie de fichier au FTP",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(SynchronizeActivity.this,"Echec de l'envoie de fichier au FTP",Toast.LENGTH_LONG).show();
            }
        }
    }
}
