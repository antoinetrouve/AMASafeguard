package com.iia.amasafeguard.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iia.amasafeguard.R;

import com.iia.amasafeguard.data.UserSQLiteAdapter;
import com.iia.amasafeguard.entity.Generator;
import com.iia.amasafeguard.entity.User;
import com.iia.amasafeguard.entity.Utils;

public class ConnexionActivity extends AppCompatActivity {

    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        final EditText etLogin = (EditText) this.findViewById(R.id.etLogin);
        final EditText etPassword = (EditText) this.findViewById(R.id.etPassword);
        Button btConnexion = (Button)this.findViewById(R.id.btConnexion);
        Button btInscription = (Button)this.findViewById(R.id.btInscription);

        btConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserSQLiteAdapter userSqlAdapter = new UserSQLiteAdapter(ConnexionActivity.this);
                userSqlAdapter.open();
                // GET PASSWORD AND COMPARE WITH DATABASE
                //get password
                password = etPassword.getText().toString();
                //hashing password and convert hash code to string
                password = Generator.toHexString(Generator.sha256(password));
                User result = userSqlAdapter.getUserWithLoginPassword(etLogin.getText().toString(), password);

                if (result != null){
                    result.setIs_connected(1);
                    userSqlAdapter.update(result);
                    userSqlAdapter.close();
                    Intent intent = new Intent(ConnexionActivity.this, SynchronizeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("UUID", result.getUuid());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else {
                    Toast.makeText(ConnexionActivity.this, "Votre Login ou Password est incorrecte !", Toast.LENGTH_LONG).show();
                }
                userSqlAdapter.close();
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
