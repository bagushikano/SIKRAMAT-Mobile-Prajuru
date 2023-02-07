package com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.adapter.krama.AnggotaKramaListAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.perceraian.PerceraianAnggotaSelectStatusAdapter;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.bagushikano.sikedatmobileadmin.model.perceraian.Perceraian;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class PerceraianDataAnggotaKeluargaActivity extends AppCompatActivity {

    private final String KRAMA_CERAI_KEY = "KRAMA_CERAI_KEY";
    private final String PASANGAN_CERAI_KEY = "PASANGAN_CERAI_KEY";
    private final String ANGGOTA_CERAI_KEY = "ANGGOTA_CERAI_KEY";
    private final String PERCERAIAN_KEY = "PERCERAIAN_KEY";

    Gson gson = new Gson();

    SharedPreferences loginPreferences;
    ActivityResultLauncher<Intent> startActivityIntent;

    KramaMipil kramaMipilSelected;
    CacahKramaMipil pasanganKrama;
    ArrayList<AnggotaKramaMipil> anggotaKramaMipilArrayList = new ArrayList<>();
    Perceraian perceraian;

    RecyclerView kramaMipilAnggotaList;
    LinearLayoutManager linearLayoutManager;
    PerceraianAnggotaSelectStatusAdapter anggotaKramaListAdapter;

    Button peceraianNextButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perceraian_data_anggota_keluarga);

        kramaMipilSelected = gson.fromJson(getIntent().getStringExtra(KRAMA_CERAI_KEY), KramaMipil.class);
        pasanganKrama = gson.fromJson(getIntent().getStringExtra(PASANGAN_CERAI_KEY), CacahKramaMipil.class);
        if (getIntent().hasExtra(ANGGOTA_CERAI_KEY)) {
            anggotaKramaMipilArrayList.addAll(
                    gson.fromJson(getIntent().getStringExtra(ANGGOTA_CERAI_KEY),
                            new TypeToken<ArrayList<AnggotaKramaMipil>>(){}.getType()
                    ));
        }
        perceraian = gson.fromJson(getIntent().getStringExtra(PERCERAIAN_KEY), Perceraian.class);

        kramaMipilAnggotaList = findViewById(R.id.krama_mipil_anggota_list);
        linearLayoutManager = new LinearLayoutManager(this);
        anggotaKramaListAdapter = new PerceraianAnggotaSelectStatusAdapter(this, anggotaKramaMipilArrayList);
        kramaMipilAnggotaList.setLayoutManager(linearLayoutManager);
        kramaMipilAnggotaList.setAdapter(anggotaKramaListAdapter);

        peceraianNextButton = findViewById(R.id.perceraian_next_button);

        peceraianNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // arahin ke data anggota langsung
                Intent perceraianNext = null;
                perceraianNext = new Intent(getApplicationContext(), PerceraianDataPerceraianActivity.class);
                perceraianNext.putExtra(ANGGOTA_CERAI_KEY, gson.toJson(anggotaKramaMipilArrayList));
                perceraianNext.putExtra( KRAMA_CERAI_KEY ,gson.toJson(kramaMipilSelected));
                perceraianNext.putExtra( PASANGAN_CERAI_KEY, gson.toJson(pasanganKrama));
                perceraianNext.putExtra( PERCERAIAN_KEY, gson.toJson(perceraian));
                startActivityIntent.launch(perceraianNext);

            }
        });

        startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 1) {
                            setResult(1);
                            finish();
                        }
                    }
                });
    }
}