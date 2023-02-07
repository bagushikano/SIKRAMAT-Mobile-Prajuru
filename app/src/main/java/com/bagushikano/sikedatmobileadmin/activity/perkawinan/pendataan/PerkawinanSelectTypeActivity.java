package com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan;

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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.bedabanjar.PerkawinanBedaBanjarPasanganActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.campurankeluar.PerkawinanCampuranKeluarPasanganActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.campuranmasuk.PerkawinanCampuranMasukPasanganActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.satubanjar.PerkawinanSatuBanjarPasanganActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class PerkawinanSelectTypeActivity extends AppCompatActivity {

    private Button perkawinanNextButton;
    private AutoCompleteTextView perkawinanJenisAutoComplete;
    private TextInputLayout perkawinanJenisForm;
    private TextView perkawinanJenisKeteranganText;
    private Boolean isJenisPerkawinanSelected = false;
    private Integer jenisPerkawinan;
    private MaterialCardView perkawinanJenisCard;
    private Toolbar homeToolbar;
    private final String PERKAWINAN_TYPE_FLAG = "PERKAWINAN_TYPE";

    ActivityResultLauncher<Intent> startActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perkawinan_select_type);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        perkawinanNextButton = findViewById(R.id.perkawinan_type_next_button);
        perkawinanJenisAutoComplete = findViewById(R.id.perkawinan_jenis_field);
        perkawinanJenisForm = findViewById(R.id.perkawinan_jenis_form);
        perkawinanJenisKeteranganText = findViewById(R.id.perkawinan_jenis_keterangan_text);
        perkawinanJenisCard = findViewById(R.id.perkawinan_jenis_keterangan_card);

        String[] jenisPerkawinanArr = new String[] {"Satu Banjar Adat", "Beda Banjar Adat", "Campuran Masuk", "Campuran Keluar"};
        ArrayAdapter jenisPerkawinanAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_item, jenisPerkawinanArr);

        perkawinanJenisAutoComplete.setAdapter(jenisPerkawinanAdapter);
        perkawinanJenisAutoComplete.setInputType(0);
        perkawinanJenisAutoComplete.setKeyListener(null);

        perkawinanNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isJenisPerkawinanSelected) {
                    Intent perkawinanNextIntent = null;
                    if (jenisPerkawinan == 0) {
                        perkawinanNextIntent = new Intent(getApplicationContext(), PerkawinanSatuBanjarPasanganActivity.class);
                        perkawinanNextIntent.putExtra(PERKAWINAN_TYPE_FLAG, 0);
                    } else if (jenisPerkawinan == 1) {
                        perkawinanNextIntent = new Intent(getApplicationContext(), PerkawinanBedaBanjarPasanganActivity.class);
                        perkawinanNextIntent.putExtra(PERKAWINAN_TYPE_FLAG, 1);
                    } else if (jenisPerkawinan == 2 ) {
                        perkawinanNextIntent = new Intent(getApplicationContext(), PerkawinanCampuranMasukPasanganActivity.class);
                        perkawinanNextIntent.putExtra(PERKAWINAN_TYPE_FLAG, 2);
                    } else if (jenisPerkawinan == 3) {
                        perkawinanNextIntent = new Intent(getApplicationContext(), PerkawinanCampuranKeluarPasanganActivity.class);
                        perkawinanNextIntent.putExtra(PERKAWINAN_TYPE_FLAG, 3);
                    }
                    startActivityIntent.launch(perkawinanNextIntent);

                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Pilih jenis perkawinan sebelum melanjutkan.", Snackbar.LENGTH_LONG)
//                        .setAnchorView(perkawinanNextButton)
                            .show();
                }
            }
        });

        perkawinanJenisAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Satu Banjar Adat")) {
                    jenisPerkawinan = 0;
                    setJenisPerkawinanKeteranganText("Perkawinan Satu Banjar Adat ",
                            "merupakan perkawinan yang terjadi ketika Purusa dan Pradana berasal dari satu Desa Adat dan Banjar Adat yang sama.");

                } else if (adapterView.getItemAtPosition(i).equals("Beda Banjar Adat")) {
                    jenisPerkawinan = 1;
                    setJenisPerkawinanKeteranganText("Perkawinan Beda Banjar Adat ",
                            "merupakan perkawinan yang terjadi ketika Purusa dan Pradana berasal dari Banjar Adat yang berbeda.");

                } else if (adapterView.getItemAtPosition(i).equals("Campuran Masuk")) {
                    jenisPerkawinan = 2;
                    setJenisPerkawinanKeteranganText("Perkawinan Campuran Masuk ",
                            "merupakan perkawinan yang terjadi ketika Pradana bukan merupakan warga Desa Adat di Bali.");

                } else if (adapterView.getItemAtPosition(i).equals("Campuran Keluar")) {
                    jenisPerkawinan = 3;
                    setJenisPerkawinanKeteranganText("Perkawinan Campuran Keluar ",
                            "merupakan perkawinan yang terjadi ketika Penduduk kawin keluar dengan orang yang bukan merupakan warga Desa Adat di Bali.");
                }
                isJenisPerkawinanSelected = true;
                perkawinanJenisCard.setVisibility(View.VISIBLE);
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

    public void setJenisPerkawinanKeteranganText(String jenis, String keterangan) {
        String jenisPerkawinanTitle = jenis;
        String jenisPerkawinanPenjelasan = keterangan;
        SpannableString jenisPerkawinanKeteranganFormatted = new SpannableString(jenisPerkawinanTitle + jenisPerkawinanPenjelasan);
        jenisPerkawinanKeteranganFormatted.setSpan(new StyleSpan(Typeface.BOLD), 0, jenisPerkawinanTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        perkawinanJenisKeteranganText.setText(jenisPerkawinanKeteranganFormatted);
    }
}