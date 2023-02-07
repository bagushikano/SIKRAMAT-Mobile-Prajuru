package com.bagushikano.sikedatmobileadmin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bagushikano.sikedatmobileadmin.R;

public class DesaAdatSummaryActivity extends AppCompatActivity {

    Button banjarAdatListButton, banjarDinasListButton;
    private Toolbar homeToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desa_adat_summary);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        banjarAdatListButton = findViewById(R.id.banjar_adat_list_button);
        banjarDinasListButton = findViewById(R.id.banjar_dinas_list_button);

        banjarAdatListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent banjarListIntent = new Intent(getApplicationContext(), BanjarAdatDaftarActivity.class);
                startActivity(banjarListIntent);
            }
        });

        banjarDinasListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent banjarListIntent = new Intent(getApplicationContext(), BanjarDinasDaftarActivity.class);
                startActivity(banjarListIntent);
            }
        });
    }
}