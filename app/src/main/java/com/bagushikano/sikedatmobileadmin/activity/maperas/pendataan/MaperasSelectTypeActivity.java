package com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.bedabanjar.MaperasBedaBanjarDataKramaLamaActivity;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.campurankeluar.MaperasCampuranKeluarDataKramaLamaActivity;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.campuranmasuk.MaperasCampuranMasukDataAnakOrtuLamaActivity;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.satubanjar.MaperasSatuBanjarDataKramaLamaActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class MaperasSelectTypeActivity extends AppCompatActivity {


    private Button maperasNextButton;
    private AutoCompleteTextView maperasJenisAutoComplete;
    private TextInputLayout maperasJenisForm;
    private TextView maperasJenisKeteranganText;
    private Boolean isJenisMaperasSelected = false;
    private Integer jenisMaperas;
    private MaterialCardView maperasJenisCard;
    private Toolbar homeToolbar;
    private final String PERKAWINAN_TYPE_FLAG = "PERKAWINAN_TYPE";

    ActivityResultLauncher<Intent> startActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maperas_select_type);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        maperasNextButton = findViewById(R.id.maperas_type_next_button);
        maperasJenisAutoComplete = findViewById(R.id.maperas_jenis_field);
        maperasJenisForm = findViewById(R.id.maperas_jenis_form);
        maperasJenisKeteranganText = findViewById(R.id.maperas_jenis_keterangan_text);
        maperasJenisCard = findViewById(R.id.maperas_jenis_keterangan_card);

        String[] jenisMaperasArr = new String[] {"Satu Banjar Adat", "Beda Banjar Adat", "Campuran Masuk", "Campuran Keluar"};
        ArrayAdapter jenisMaperasAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_item, jenisMaperasArr);

        maperasJenisAutoComplete.setAdapter(jenisMaperasAdapter);
        maperasJenisAutoComplete.setInputType(0);
        maperasJenisAutoComplete.setKeyListener(null);

        maperasNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isJenisMaperasSelected) {
                    Intent maperasNextIntent = null;
                    if (jenisMaperas == 0) {
                        maperasNextIntent = new Intent(getApplicationContext(), MaperasSatuBanjarDataKramaLamaActivity.class);
                        maperasNextIntent.putExtra(PERKAWINAN_TYPE_FLAG, 0);
                    } else if (jenisMaperas == 1) {
                        maperasNextIntent = new Intent(getApplicationContext(), MaperasBedaBanjarDataKramaLamaActivity.class);
                        maperasNextIntent.putExtra(PERKAWINAN_TYPE_FLAG, 1);
                    } else if (jenisMaperas == 2 ) {
                        maperasNextIntent = new Intent(getApplicationContext(), MaperasCampuranMasukDataAnakOrtuLamaActivity.class);
                        maperasNextIntent.putExtra(PERKAWINAN_TYPE_FLAG, 2);
                    } else if (jenisMaperas == 3) {
                        maperasNextIntent = new Intent(getApplicationContext(), MaperasCampuranKeluarDataKramaLamaActivity.class);
                        maperasNextIntent.putExtra(PERKAWINAN_TYPE_FLAG, 3);
                    }
                    startActivityIntent.launch(maperasNextIntent);

                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Pilih jenis maperas sebelum melanjutkan.", Snackbar.LENGTH_LONG)
//                        .setAnchorView(maperasNextButton)
                            .show();
                }
            }
        });

        maperasJenisAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Satu Banjar Adat")) {
                    jenisMaperas = 0;
                    setJenisMaperasKeteranganText("Maperas Satu Banjar Adat ",
                            "merupakan maperas yang terjadi ketika orang tua lama dan orang tau baru berasal dari satu Desa Adat dan Banjar Adat yang sama.");

                } else if (adapterView.getItemAtPosition(i).equals("Beda Banjar Adat")) {
                    jenisMaperas = 1;
                    setJenisMaperasKeteranganText("Maperas Beda Banjar Adat ",
                            "merupakan maperas yang terjadi ketika orang tua lama dan orang tua baru berasal dari Banjar Adat yang berbeda.");

                } else if (adapterView.getItemAtPosition(i).equals("Campuran Masuk")) {
                    jenisMaperas = 2;
                    setJenisMaperasKeteranganText("Maperas Campuran Masuk ",
                            "merupakan maperas yang terjadi ketika calon anak bukan merupakan warga Desa Adat di Bali.");

                } else if (adapterView.getItemAtPosition(i).equals("Campuran Keluar")) {
                    jenisMaperas = 3;
                    setJenisMaperasKeteranganText("Maperas Campuran Keluar ",
                            "merupakan maperas yang terjadi ketika seorang anak diangkat oleh orang yang bukan merupakan warga Desa Adat di Bali.");
                }
                isJenisMaperasSelected = true;
                maperasJenisCard.setVisibility(View.VISIBLE);
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

    public void setJenisMaperasKeteranganText(String jenis, String keterangan) {
        String jenisMaperasTitle = jenis;
        String jenisMaperasPenjelasan = keterangan;
        SpannableString jenisMaperasKeteranganFormatted = new SpannableString(jenisMaperasTitle + jenisMaperasPenjelasan);
        jenisMaperasKeteranganFormatted.setSpan(new StyleSpan(Typeface.BOLD), 0, jenisMaperasTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        maperasJenisKeteranganText.setText(jenisMaperasKeteranganFormatted);
    }
}