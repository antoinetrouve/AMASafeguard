package com.iia.amasafeguard.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iia.amasafeguard.R;
import com.iia.amasafeguard.data.UserSQLiteAdapter;
import com.iia.amasafeguard.entity.User;

public class InscriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Button btInscription = (Button) this.findViewById(R.id.btInscriptionPage);
        final EditText etLoginInscription = (EditText) this.findViewById(R.id.etLoginInscription);
        final EditText etPasswordInscription = (EditText) this.findViewById(R.id.etPasswordInscription);

        final Context context = this;

        btInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etLoginInscription.getText().toString() != null && etPasswordInscription.getText().toString() != null){
                    UserSQLiteAdapter userAdapter = new UserSQLiteAdapter(context);
                    userAdapter.open();

                    User user = new User();
                    user.setLogin(etLoginInscription.getText().toString());
                    user.setMdp(etPasswordInscription.getText().toString());
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
