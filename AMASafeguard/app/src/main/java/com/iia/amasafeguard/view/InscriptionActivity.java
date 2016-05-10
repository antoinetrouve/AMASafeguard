package com.iia.amasafeguard.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iia.amasafeguard.R;
import com.iia.amasafeguard.data.UserSQLiteAdapter;
import com.iia.amasafeguard.entity.Generator;
import com.iia.amasafeguard.entity.User;

import java.security.SecureRandom;

public class InscriptionActivity extends AppCompatActivity {

    private String password;
    private byte[] km;
    private byte[] ka;
    private int count = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Button btInscription = (Button) this.findViewById(R.id.btInscriptionPage);
        final EditText etLoginInscription = (EditText) this.findViewById(R.id.etLoginInscription);
        final EditText etPasswordInscription = (EditText) this.findViewById(R.id.etPasswordInscription);

        final Context context = this;

        final byte[] salt = new byte[8];

        btInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etLoginInscription.getText().toString() != null && etPasswordInscription.getText().toString() != null) {
                    UserSQLiteAdapter userAdapter = new UserSQLiteAdapter(context);
                    userAdapter.open();
                    User user = new User();
                    user.setLogin(etLoginInscription.getText().toString());

                    //MANAGE PASSWORD
                    //get password
                    password = etLoginInscription.getText().toString();
                    //hashing password and conv ert hash code to string
                    password = Generator.toHexString(Generator.sha256(password));
                    //Generates cryptographically secure pseudo-random numbers
                    SecureRandom rs = new SecureRandom();
                    //Generates and stores random bytes
                    rs.nextBytes(salt);
                    //Derivation hashed password
                    km = Generator.derivKm(password, count, salt);
                    //Derivation hashed password into 1 keys (ka)
                    ka = Generator.derivKa(km);
                    //encode password to base64 URL_SAFE to save in database
                    user.setMdp(Base64.encodeToString(ka,Base64.URL_SAFE));

                    user.setCreated_at();
                    user.setUuid();
                    user.setIs_connected(0);
                    user.setId(userAdapter.insert(user));

                    Toast.makeText(context, "Votre compte à bien été créer.", Toast.LENGTH_LONG).show();

                    userAdapter.close();
                }
            }
        });
    }
}
