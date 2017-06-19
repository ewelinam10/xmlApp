package com.ewelinam.xmlapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button przycisk;
    private EditText et_link;
    private String link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        przycisk = (Button)findViewById(R.id.przycisk);
        et_link = (EditText) findViewById(R.id.link);
    }

    public void wczytaj(View view) {
        link = String.valueOf(et_link.getText());
        Intent nowyEkran = new Intent(getApplicationContext(),SecondActivity.class);
        nowyEkran.putExtra("link",link);
        startActivity(nowyEkran);
    }
}
