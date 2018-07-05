package com.example.jaipurguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ConnectionLost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connectionlost);
    }

    public void retry(View v)
    {
        finish();

        startActivity(new Intent(this,MainActivity.class));
    }
}
