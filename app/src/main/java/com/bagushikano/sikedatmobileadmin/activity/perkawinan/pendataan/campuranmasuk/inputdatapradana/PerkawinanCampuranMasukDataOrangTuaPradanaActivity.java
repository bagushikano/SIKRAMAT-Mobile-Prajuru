package com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.campuranmasuk.inputdatapradana;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.Perkawinan;
import com.bagushikano.sikedatmobileadmin.util.ChangeDateTimeFormat;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

public class PerkawinanCampuranMasukDataOrangTuaPradanaActivity extends AppCompatActivity {

    private TextInputEditText perkawinanPradanaNamaAyahField, perkawinanPradanaNikAyahField,
            perkawinanPradanaNamaIbuField, perkawinanPradanaNikIbuField;

    private TextInputLayout perkawinanPradanaNamaAyahLayout, perkawinanPradanaNikAyahLayout,
            perkawinanPradanaNamaIbuLayout, perkawinanPradanaNikIbuLayout;
    private Button perkawinanPradanaNext;

    private Boolean isNamaAyahValid = true, isNikAyahValid = true, isNamaIbuValid = true, isNikIbuValid = true;

    Perkawinan perkawinanCampuranMasuk = new Perkawinan();

    private final String PRADANA_CACAH_KEY = "PRADANA_CACAH_KEY";
    private final String PRADANA_CACAH_FOTO_KEY = "PRADANA_CACAH_FOTO_KEY";
    private final String CAMPURAN_KEY = "CAMPURAN_KEY";
    Uri fotoUri;
    Gson gson = new Gson();
    CacahKramaMipil cacahKramaPradana = new CacahKramaMipil();
    ActivityResultLauncher<Intent> startActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perkawinan_campuran_masuk_data_orang_tua_pradana);

        if (getIntent().hasExtra(PRADANA_CACAH_FOTO_KEY)) {
            fotoUri = Uri.parse(getIntent().getStringExtra(PRADANA_CACAH_FOTO_KEY));
        }
        cacahKramaPradana = gson.fromJson(getIntent().getStringExtra(PRADANA_CACAH_KEY), CacahKramaMipil.class);

        perkawinanPradanaNext = findViewById(R.id.perkawinan_campuran_masuk_pradana_next_button);

        perkawinanPradanaNamaAyahField = findViewById(R.id.perkawinan_campuran_masuk_pradana_nama_ayah_field);
        perkawinanPradanaNikAyahField = findViewById(R.id.perkawinan_campuran_masuk_pradana_nik_ayah_field);
        perkawinanPradanaNamaIbuField = findViewById(R.id.perkawinan_campuran_masuk_pradana_nama_ibu_field);
        perkawinanPradanaNikIbuField = findViewById(R.id.perkawinan_campuran_masuk_pradana_nik_ibu_field);

        perkawinanPradanaNamaAyahLayout = findViewById(R.id.perkawinan_campuran_masuk_pradana_nama_ayah_layout);
        perkawinanPradanaNikAyahLayout = findViewById(R.id.perkawinan_campuran_masuk_pradana_nik_ayah_layout);
        perkawinanPradanaNamaIbuLayout = findViewById(R.id.perkawinan_campuran_masuk_pradana_nama_ibu_layout);
        perkawinanPradanaNikIbuLayout = findViewById(R.id.perkawinan_campuran_masuk_pradana_nik_ibu_layout);

        perkawinanPradanaNikAyahField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(perkawinanPradanaNikAyahField.getText().toString().length() == 0) {
                    perkawinanPradanaNikAyahLayout.setError(null);
                    isNikAyahValid = true;
                }
                else {
                    if (perkawinanPradanaNikAyahField.getText().toString().length() < 16) {
                        perkawinanPradanaNikAyahLayout.setError("NIK tidak boleh kurang dari 16 karakter");
                        isNikAyahValid = false;
                    }
                    else if (perkawinanPradanaNikAyahField.getText().toString().length() > 16) {
                        perkawinanPradanaNikAyahLayout.setError("NIK tidak boleh lebih dari 16 karakter");
                        isNikAyahValid = false;
                    }
                    else {
                        perkawinanPradanaNikAyahLayout.setError(null);
                        isNikAyahValid = true;
                    }
                }
            }
        });

        perkawinanPradanaNamaAyahField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(perkawinanPradanaNamaAyahField.getText().toString().length() == 0) {
                    perkawinanPradanaNamaAyahLayout.setError(null);
                    perkawinanPradanaNamaAyahLayout.setErrorEnabled(false);
                    isNamaAyahValid = true;
                }
                else {
                    if (perkawinanPradanaNamaAyahField.getText().toString().length() < 2) {
                        perkawinanPradanaNamaAyahLayout.setError("Nama tidak boleh kurang dari 2 karakter");
                        perkawinanPradanaNamaAyahLayout.setErrorEnabled(true);
                        isNamaAyahValid = false;
                    }
                    else {
                        perkawinanPradanaNamaAyahField.setError(null);
                        perkawinanPradanaNamaAyahLayout.setErrorEnabled(false);
                        isNamaAyahValid = true;
                    }
                }
            }
        });

        perkawinanPradanaNikIbuField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(perkawinanPradanaNikIbuField.getText().toString().length() == 0) {
                    perkawinanPradanaNikIbuLayout.setError(null);
                    isNikIbuValid = true;
                }
                else {
                    if (perkawinanPradanaNikIbuField.getText().toString().length() < 16) {
                        perkawinanPradanaNikIbuLayout.setError("NIK tidak boleh kurang dari 16 karakter");
                        isNikIbuValid = false;
                    }
                    else if (perkawinanPradanaNikIbuField.getText().toString().length() > 16) {
                        perkawinanPradanaNikIbuLayout.setError("NIK tidak boleh lebih dari 16 karakter");
                        isNikIbuValid = false;
                    }
                    else {
                        perkawinanPradanaNikIbuLayout.setError(null);
                        isNikIbuValid = true;
                    }
                }
            }
        });

        perkawinanPradanaNamaIbuField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(perkawinanPradanaNamaIbuField.getText().toString().length() == 0) {
                    perkawinanPradanaNamaIbuLayout.setError(null);
                    perkawinanPradanaNamaIbuLayout.setErrorEnabled(false);
                    isNamaIbuValid = true;
                }
                else {
                    if (perkawinanPradanaNamaIbuField.getText().toString().length() < 2) {
                        perkawinanPradanaNamaIbuLayout.setError("Nama tidak boleh kurang dari 2 karakter");
                        perkawinanPradanaNamaIbuLayout.setErrorEnabled(true);
                        isNamaIbuValid = false;
                    }
                    else {
                        perkawinanPradanaNamaIbuField.setError(null);
                        perkawinanPradanaNamaIbuLayout.setErrorEnabled(false);
                        isNamaIbuValid = true;
                    }
                }
            }
        });

        perkawinanPradanaNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNamaAyahValid && isNikAyahValid && isNamaIbuValid && isNikIbuValid) {
                    if (perkawinanPradanaNamaAyahField.getText().toString().length() != 0) {
                        perkawinanCampuranMasuk.setNamaAyahPradana(perkawinanPradanaNamaAyahField.getText().toString());
                    }
                    if (perkawinanPradanaNikAyahField.getText().toString().length() != 0) {
                        perkawinanCampuranMasuk.setNikAyahPradana(perkawinanPradanaNikAyahField.getText().toString());
                    }
                    if (perkawinanPradanaNamaIbuField.getText().toString().length() != 0) {
                        perkawinanCampuranMasuk.setNamaIbuPradana(perkawinanPradanaNamaIbuField.getText().toString());
                    }
                    if (perkawinanPradanaNikIbuField.getText().toString().length() != 0 ) {
                        perkawinanCampuranMasuk.setNikIbuPradana(perkawinanPradanaNikIbuField.getText().toString());
                    }

                    Intent perkawinanNextIntent = new Intent(getApplicationContext(), PerkawinanCampuranMasukDataLainnyaActivity.class);
                    perkawinanNextIntent.putExtra(PRADANA_CACAH_KEY, gson.toJson(cacahKramaPradana));
                    if (fotoUri != null) {
                        perkawinanNextIntent.putExtra(PRADANA_CACAH_FOTO_KEY, fotoUri.toString());
                    }
                    perkawinanNextIntent.putExtra(CAMPURAN_KEY, gson.toJson(perkawinanCampuranMasuk));
                    startActivityIntent.launch(perkawinanNextIntent);

                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Terdapat data yang belum valid. Silahkan periksa kembali", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 700) {
                            setResult(700, result.getData());
                            finish();
                        }
                    }
                });
    }
}