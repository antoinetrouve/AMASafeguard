package com.iia.amasafeguard.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.iia.amasafeguard.R;

import com.iia.amasafeguard.data.DataSQLiteAdapter;

public class ConnexionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        DataSQLiteAdapter dataSqliteAdapter = new DataSQLiteAdapter(this);
        dataSqliteAdapter.open();

        Button btConnexion = (Button)this.findViewById(R.id.btConnexion);
        Button btInscription = (Button)this.findViewById(R.id.btInscription);

        btConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //UserSQLiteAdapter userSqlAdapter = new UserSQLiteAdapter(ConnexionActivity.this);
                //userSqlAdapter
                Intent intent = new Intent(ConnexionActivity.this, SynchronizeActivity.class);
                startActivity(intent);
            }
        });

        btInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnexionActivity.this, InscriptionActivity.class);
                startActivity(intent);
            }
        });
    }
}
