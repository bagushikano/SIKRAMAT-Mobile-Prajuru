package com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan.kramapurusa;

import static android.view.View.GONE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan.kramaselect.PerceraianSelectKramaTujuanActivity;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.bagushikano.sikedatmobileadmin.model.perceraian.Perceraian;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class PerceraianKramaPurusaTetapActivity extends AppCompatActivity {

    TextView kramaNamaText, pasanganNamaText, kramaKedudukanText, pasanganKedudukanText;
    Button kramaSelectButton, peceraianNextButton;

    ActivityResultLauncher<Intent> startActivityIntent;
    SharedPreferences loginPreferences;

    //di pake untuk krama mipilnya
    private final String KRAMA_MIPIL_SELECT_KEY = "KRAMA_MIPIL_SELECT_KEY";
    private final String KRAMA_MIPIL_DETAIL_KEY = "KRAMA_MIPIL_DETAIL_KEY";
    Gson gson = new Gson();

    KramaMipil kramaMipilSelected;
    KramaMipil kramaBaru;
    AnggotaKramaMipil pasanganKrama;
    ArrayList<AnggotaKramaMipil> anggotaKramaMipilArrayList = new ArrayList<>();
    Perceraian perceraian;

    private final String KRAMA_CERAI_KEY = "KRAMA_CERAI_KEY";
    private final String PASANGAN_CERAI_KEY = "PASANGAN_CERAI_KEY";
    private final String ANGGOTA_CERAI_KEY = "ANGGOTA_CERAI_KEY";
    private final String PERCERAIAN_KEY = "PERCERAIAN_KEY";

    private RadioGroup perceraianKramaMipilPurusaStatusKeluargaRadioGroup;
    private RadioButton perceraianKramaMipilPurusaStayRadio, perceraiankramaMipilPurusaPindahKramaRadio;
    private MaterialCardView perceraianKramaPindahKramaLayout;
    private TextInputLayout perceraianKramaStatusHubungan;
    private AutoCompleteTextView perceraianKramaStatusHubunganField;
    private Button perceraianSelectNewKramaButton;
    private TextView kramaBaruNameText;

    Boolean isStatusKeluargaValid = false, isKramaSelected = false, isStatusBaruValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perceraian_krama_purusa_tetap);

        kramaMipilSelected = gson.fromJson(getIntent().getStringExtra(KRAMA_CERAI_KEY), KramaMipil.class);
        pasanganKrama = gson.fromJson(getIntent().getStringExtra(PASANGAN_CERAI_KEY), AnggotaKramaMipil.class);
        if (getIntent().hasExtra(ANGGOTA_CERAI_KEY)) {
            anggotaKramaMipilArrayList.addAll(
                    gson.fromJson(getIntent().getStringExtra(ANGGOTA_CERAI_KEY),
                    new TypeToken<ArrayList<AnggotaKramaMipil>>(){}.getType()
            ));
        }
        perceraian = gson.fromJson(getIntent().getStringExtra(PERCERAIAN_KEY), Perceraian.class);

        kramaNamaText = findViewById(R.id.perceraian_krama_mipil_nama_text);
        kramaKedudukanText = findViewById(R.id.perceraian_krama_mipil_kedudukan_text);
        peceraianNextButton = findViewById(R.id.perceraian_next_button);

        kramaNamaText.setText(StringFormatter.formatNamaWithGelar(
                kramaMipilSelected.getCacahKramaMipil().getPenduduk().getNama(),
                kramaMipilSelected.getCacahKramaMipil().getPenduduk().getGelarDepan(),
                kramaMipilSelected.getCacahKramaMipil().getPenduduk().getGelarBelakang()
        ));
        kramaKedudukanText.setText(kramaMipilSelected.getKedudukanKramaMipil());

        perceraianKramaMipilPurusaStatusKeluargaRadioGroup = findViewById(R.id.perceraian_krama_mipil_status_kekeluargaan);
        perceraianKramaMipilPurusaStayRadio = findViewById(R.id.perceraian_krama_mipil_stay);
        perceraiankramaMipilPurusaPindahKramaRadio = findViewById(R.id.perceraian_krama_mipil_pindah_keluarga);

        perceraianKramaPindahKramaLayout = findViewById(R.id.perceraian_krama_mipil_krama_tujuan_card);
        perceraianKramaStatusHubungan = findViewById(R.id.perceraian_krama_mipil_new_krama_status_hubungan_layout);
        perceraianKramaStatusHubunganField = findViewById(R.id.perceraian_krama_mipil_new_krama_status_hubungan_field);
        perceraianSelectNewKramaButton = findViewById(R.id.perceraian_krama_mipil_select_new_krama);
        kramaBaruNameText = findViewById(R.id.krama_tujuan_nama_text);


        String[] jkAnak = new String[] {"Anak", "Cucu", "Ayah", "Ibu", "Famili Lain"};
        ArrayAdapter adapterJk = new ArrayAdapter<>(this, R.layout.dropdown_menu_item, jkAnak);
        perceraianKramaStatusHubunganField.setAdapter(adapterJk);
        perceraianKramaStatusHubunganField.setInputType(0);
        perceraianKramaStatusHubunganField.setKeyListener(null);

        perceraianKramaStatusHubunganField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Anak")) {
                    perceraian.setStatusHubunganBaruPurusa("anak");
                } else if (adapterView.getItemAtPosition(i).equals("Cucu")) {
                    perceraian.setStatusHubunganBaruPurusa("cucu");
                } else if (adapterView.getItemAtPosition(i).equals("Ayah")) {
                    perceraian.setStatusHubunganBaruPurusa("ayah");
                } else if (adapterView.getItemAtPosition(i).equals("Ibu")) {
                    perceraian.setStatusHubunganBaruPurusa("ibu");
                } else if (adapterView.getItemAtPosition(i).equals("Famili Lain")) {
                    perceraian.setStatusHubunganBaruPurusa("famili_lain");
                }
                isStatusBaruValid = true;
            }
        });

        perceraianKramaMipilPurusaStatusKeluargaRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.perceraian_krama_mipil_stay) {
                    perceraian.setStatusPurusa("tetap_di_banjar_dan_kk_lama");
                    perceraianKramaPindahKramaLayout.setVisibility(GONE);
                    perceraian.setStatusHubunganBaruPurusa(null);
                    perceraian.setKramaMipilBaruPurusaId(null);
                } else if (i == R.id.perceraian_krama_mipil_pindah_keluarga) {
                    perceraian.setStatusPurusa("tetap_di_banjar_dan_kk_baru");
                    perceraianKramaPindahKramaLayout.setVisibility(View.VISIBLE);
                }
                isStatusKeluargaValid = true;
            }
        });

        peceraianNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStatusKeluargaValid) {
                    if (perceraian.getStatusPurusa().equals("tetap_di_banjar_dan_kk_baru")) {
                        if (isKramaSelected && isStatusBaruValid) {
                            Intent perceraianNext = new Intent(getApplicationContext(), PerceraianKramaPurusaPasanganActivity.class);
                            perceraianNext.putExtra( KRAMA_CERAI_KEY ,gson.toJson(kramaMipilSelected));
                            perceraianNext.putExtra( PASANGAN_CERAI_KEY, gson.toJson(pasanganKrama));
                            perceraianNext.putExtra( PERCERAIAN_KEY, gson.toJson(perceraian));
                            if (anggotaKramaMipilArrayList.size() != 0) {
                                perceraianNext.putExtra( ANGGOTA_CERAI_KEY, gson.toJson(anggotaKramaMipilArrayList));
                            }
                            startActivityIntent.launch(perceraianNext);
                        } else {
                            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                    "Periksa data kembali", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Intent perceraianNext = new Intent(getApplicationContext(), PerceraianKramaPurusaPasanganActivity.class);
                        perceraianNext.putExtra( KRAMA_CERAI_KEY ,gson.toJson(kramaMipilSelected));
                        perceraianNext.putExtra( PASANGAN_CERAI_KEY, gson.toJson(pasanganKrama));
                        perceraianNext.putExtra( PERCERAIAN_KEY, gson.toJson(perceraian));
                        if (anggotaKramaMipilArrayList.size() != 0) {
                            perceraianNext.putExtra( ANGGOTA_CERAI_KEY, gson.toJson(anggotaKramaMipilArrayList));
                        }
                        startActivityIntent.launch(perceraianNext);
                    }
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Periksa data kembali", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        perceraianSelectNewKramaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pass KRAMA untuk idkrama, pass BANJAR untuk id bajnar
                Intent maperasSelectKramaIntent = new Intent(getApplicationContext() , PerceraianSelectKramaTujuanActivity.class);
                maperasSelectKramaIntent.putExtra("KRAMA", kramaMipilSelected.getId());
                startActivityIntent.launch(maperasSelectKramaIntent);
            }
        });

        startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 200) {
                            kramaBaru = gson.fromJson(result.getData().getStringExtra(KRAMA_MIPIL_SELECT_KEY), KramaMipil.class);
                            kramaBaruNameText.setText(
                                    StringFormatter.formatNamaWithGelar(
                                            kramaBaru.getCacahKramaMipil().getPenduduk().getNama(),
                                            kramaBaru.getCacahKramaMipil().getPenduduk().getGelarDepan(),
                                            kramaBaru.getCacahKramaMipil().getPenduduk().getGelarBelakang()
                                    )
                            );
                            perceraian.setKramaMipilBaruPurusaId(kramaBaru.getId());
                            isKramaSelected = true;
                        } else if (result.getResultCode() == 1) {
                            setResult(1);
                            finish();
                        }
                    }
                });
    }
}