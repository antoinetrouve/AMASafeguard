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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SynchronizeActivity extends AppCompatActivity {

    private static final String PATH_CONF_FILE = "/conf.txt";
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
            return true;
        }

        protected void onPostExecute(Boolean b) {

            btSynchronize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Récupération du fichier de conf
                    File confFile = new File(Environment.getDataDirectory().getPath() + PATH_CONF_FILE);
                    String content ="";
                    ArrayList<File> arrayFile = new ArrayList();

                    Boolean exist = confFile.exists();

                    try{
                        InputStream ips = new FileInputStream(confFile);
                        InputStreamReader ipsr = new InputStreamReader(ips);
                        BufferedReader br = new BufferedReader(ipsr);
                        String ligne;

                        while ((ligne=br.readLine())!=null){
                            File file = new File(Environment.getDataDirectory().getPath() + ligne);
                            arrayFile.add(file);
                        }
                        br.close();
                    }
                    catch (Exception e){
                        System.out.println(e.toString());
                    }

                    DataSQLiteAdapter dataSQLiteAdapter = new DataSQLiteAdapter(SynchronizeActivity.this);
                    dataSQLiteAdapter.open();

                    //Boucle foreach sur le tableau de File
                    for (File file: arrayFile) {
                        Data fileDb = dataSQLiteAdapter.getDataByPath(file.getPath());
                        Date d = new Date(file.lastModified());
                        
                        if ((file.exists() && fileDb == null) || (file.exists() && !d.equals(fileDb.getUpdated_at()))){
                            Data data = new Data();
                            data.setName(file.getName());
                            data.setPath(file.getPath());
                            data.setCreated_at(String.valueOf(d));
                            data.setUpdated_at(String.valueOf(d));

                            if(fileDb == null){
                                //Insert
                                dataSQLiteAdapter.insert(data);
                            }else{
                                //Update
                                dataSQLiteAdapter.update(data);
                            }
                        }
                        if(file.exists() == false) {
                            Toast.makeText(SynchronizeActivity.this, "Le dossier " + file.getName() + " n'éxiste pas !", Toast.LENGTH_LONG).show();
                        }
                    }

                    dataSQLiteAdapter.close();

                    Toast.makeText(SynchronizeActivity.this, "Dossier synchronisé avec la DB !", Toast.LENGTH_LONG).show();
                }
            });


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
