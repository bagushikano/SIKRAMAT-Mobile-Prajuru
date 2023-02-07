package com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.satubanjar;

import static android.view.View.GONE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.adapter.perkawinan.PerkawinanMempelaiSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.Perkawinan;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;

public class PerkawinanSatuBanjarStatusKekeluargaanActivity extends AppCompatActivity {

    private RadioGroup perkawinanStatusKekeluargaanRadioGroup;
    private RadioButton perkawinanKeluargaBaruRadio, perkawinanKeluargaLamaRadio;
    private MaterialCardView perkawinanKekeluargaanKepalaKeluargaCard;
    private CacahKramaMipil pradanaKrama, purusaKrama;

    private AutoCompleteTextView perkawinanKekeluargaanKepalaKeluargaAutoComplete;
    private TextInputLayout perkawinanKekeluargaanKepalaKeluargaLayout;

    private ArrayList<CacahKramaMipil> cacahKramaMipilArrayList = new ArrayList<>();
    private PerkawinanMempelaiSelectionAdapter perkawinanMempelaiSelectionAdapter;

    private Button perkawinanNextButton;

    // 0 = tetap, 1 = baru
    private String statusKekeluargaan;
    private Boolean isStatusKeluargaSelected = false;
    private Integer idCalonKrama;
    private CacahKramaMipil cacahKramaMipilSelected;

    private final String PURUSA_KEY = "PURUSA_SELECT_KEY";
    private final String PRADANA_KEY = "PRADANA_SELECT_KEY";
    private final String PERKAWINAN_KEY = "PERKAWINAN_KEY";

    Gson gson = new Gson();

    Perkawinan perkawinan = new Perkawinan();

    ActivityResultLauncher<Intent> startActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perkawinan_satu_banjar_status_kekeluargaan);

        perkawinanKekeluargaanKepalaKeluargaCard = findViewById(R.id.perkawinan_kepala_keluarga_card);
        perkawinanKekeluargaanKepalaKeluargaAutoComplete = findViewById(R.id.perkawinan_kepala_keluarga_field);
        perkawinanKekeluargaanKepalaKeluargaLayout = findViewById(R.id.perkawinan_kepala_keluarga_form);

        pradanaKrama = gson.fromJson(getIntent().getStringExtra(PRADANA_KEY), CacahKramaMipil.class);
        purusaKrama = gson.fromJson(getIntent().getStringExtra(PURUSA_KEY), CacahKramaMipil.class);
        cacahKramaMipilArrayList.add(pradanaKrama);
        cacahKramaMipilArrayList.add(purusaKrama);

        perkawinanMempelaiSelectionAdapter = new PerkawinanMempelaiSelectionAdapter(this, cacahKramaMipilArrayList);
        perkawinanKekeluargaanKepalaKeluargaAutoComplete.setAdapter(perkawinanMempelaiSelectionAdapter);
        perkawinanKekeluargaanKepalaKeluargaAutoComplete.setThreshold(100);
        perkawinanKekeluargaanKepalaKeluargaAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cacahKramaMipilSelected = (CacahKramaMipil) adapterView.getItemAtPosition(i);
                perkawinanKekeluargaanKepalaKeluargaAutoComplete.setText(cacahKramaMipilSelected.getPenduduk().getNama());
                perkawinan.setCalonKramaId(cacahKramaMipilSelected.getId());
            }
        });

        perkawinanStatusKekeluargaanRadioGroup = findViewById(R.id.perkawinan_status_kekeluargaan_radio_group);
        perkawinanStatusKekeluargaanRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.perkawinan_kekeluargaan_stay_radio) {
                    statusKekeluargaan = "tetap";
                    perkawinanKekeluargaanKepalaKeluargaCard.setVisibility(GONE);
                } else if (i == R.id.perkawinan_kekeluargaan_new_radio) {
                    statusKekeluargaan = "baru";
                    perkawinanKekeluargaanKepalaKeluargaCard.setVisibility(View.VISIBLE);
                }
                perkawinan.setStatusKekeluargaan(statusKekeluargaan);
                isStatusKeluargaSelected = true;
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

        perkawinanNextButton = findViewById(R.id.perkawinan_status_kekeluargaan_next_button);
        perkawinanNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent perkawinanNext = new Intent(getApplicationContext(), PerkawinanSatuBanjarDataPerkawinanActivity.class);
                perkawinanNext.putExtra(PURUSA_KEY, gson.toJson(purusaKrama));
                perkawinanNext.putExtra(PRADANA_KEY, gson.toJson(pradanaKrama));
                perkawinanNext.putExtra(PERKAWINAN_KEY, gson.toJson(perkawinan));
                startActivityIntent.launch(perkawinanNext);
            }
        });
    }
}