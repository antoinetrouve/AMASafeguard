package com.iia.amasafeguard.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iia.amasafeguard.R;
import com.iia.amasafeguard.data.AmasafeguardSQLiteOpenHelper;
import com.iia.amasafeguard.data.DataSQLiteAdapter;

public class SynchronizeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronize);
    }
}
