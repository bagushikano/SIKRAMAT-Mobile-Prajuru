package com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.campuranmasuk.inputdataanak;

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
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.campuranmasuk.inputdataanak.MaperasCampuranMasukDataLainnyaActivity;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.maperas.Maperas;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

public class MaperasCampuranMasukDataOrangTuaAnakActivity extends AppCompatActivity {


    private TextInputEditText maperasAnakNamaAyahField, maperasAnakNikAyahField,
            maperasAnakNamaIbuField, maperasAnakNikIbuField;

    private TextInputLayout maperasAnakNamaAyahLayout, maperasAnakNikAyahLayout,
            maperasAnakNamaIbuLayout, maperasAnakNikIbuLayout;
    private Button maperasAnakNext;

    private Boolean isNamaAyahValid = true, isNikAyahValid = true, isNamaIbuValid = true, isNikIbuValid = true;

    Maperas maperasCampuranMasuk = new Maperas();

    private final String ANAK_CACAH_KEY = "ANAK_CACAH_KEY";
    private final String ANAK_CACAH_FOTO_KEY = "ANAK_CACAH_FOTO_KEY";
    private final String CAMPURAN_KEY = "CAMPURAN_KEY";
    Uri fotoUri;
    Gson gson = new Gson();
    CacahKramaMipil cacahKramaAnak = new CacahKramaMipil();
    ActivityResultLauncher<Intent> startActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maperas_campuran_masuk_data_orang_tua_anak);


        if (getIntent().hasExtra(ANAK_CACAH_FOTO_KEY)) {
            fotoUri = Uri.parse(getIntent().getStringExtra(ANAK_CACAH_FOTO_KEY));
        }
        cacahKramaAnak = gson.fromJson(getIntent().getStringExtra(ANAK_CACAH_KEY), CacahKramaMipil.class);

        maperasAnakNext = findViewById(R.id.maperas_campuran_masuk_anak_next_button);

        maperasAnakNamaAyahField = findViewById(R.id.maperas_campuran_masuk_anak_nama_ayah_field);
        maperasAnakNikAyahField = findViewById(R.id.maperas_campuran_masuk_anak_nik_ayah_field);
        maperasAnakNamaIbuField = findViewById(R.id.maperas_campuran_masuk_anak_nama_ibu_field);
        maperasAnakNikIbuField = findViewById(R.id.maperas_campuran_masuk_anak_nik_ibu_field);

        maperasAnakNamaAyahLayout = findViewById(R.id.maperas_campuran_masuk_anak_nama_ayah_layout);
        maperasAnakNikAyahLayout = findViewById(R.id.maperas_campuran_masuk_anak_nik_ayah_layout);
        maperasAnakNamaIbuLayout = findViewById(R.id.maperas_campuran_masuk_anak_nama_ibu_layout);
        maperasAnakNikIbuLayout = findViewById(R.id.maperas_campuran_masuk_anak_nik_ibu_layout);

        maperasAnakNikAyahField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(maperasAnakNikAyahField.getText().toString().length() == 0) {
                    maperasAnakNikAyahLayout.setError(null);
                    isNikAyahValid = true;
                }
                else {
                    if (maperasAnakNikAyahField.getText().toString().length() < 16) {
                        maperasAnakNikAyahLayout.setError("NIK tidak boleh kurang dari 16 karakter");
                        isNikAyahValid = false;
                    }
                    else if (maperasAnakNikAyahField.getText().toString().length() > 16) {
                        maperasAnakNikAyahLayout.setError("NIK tidak boleh lebih dari 16 karakter");
                        isNikAyahValid = false;
                    }
                    else {
                        maperasAnakNikAyahLayout.setError(null);
                        isNikAyahValid = true;
                    }
                }
            }
        });

        maperasAnakNamaAyahField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(maperasAnakNamaAyahField.getText().toString().length() == 0) {
                    maperasAnakNamaAyahLayout.setError(null);
                    maperasAnakNamaAyahLayout.setErrorEnabled(false);
                    isNamaAyahValid = true;
                }
                else {
                    if (maperasAnakNamaAyahField.getText().toString().length() < 2) {
                        maperasAnakNamaAyahLayout.setError("Nama tidak boleh kurang dari 2 karakter");
                        maperasAnakNamaAyahLayout.setErrorEnabled(true);
                        isNamaAyahValid = false;
                    }
                    else {
                        maperasAnakNamaAyahField.setError(null);
                        maperasAnakNamaAyahLayout.setErrorEnabled(false);
                        isNamaAyahValid = true;
                    }
                }
            }
        });

        maperasAnakNikIbuField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(maperasAnakNikIbuField.getText().toString().length() == 0) {
                    maperasAnakNikIbuLayout.setError(null);
                    isNikIbuValid = true;
                }
                else {
                    if (maperasAnakNikIbuField.getText().toString().length() < 16) {
                        maperasAnakNikIbuLayout.setError("NIK tidak boleh kurang dari 16 karakter");
                        isNikIbuValid = false;
                    }
                    else if (maperasAnakNikIbuField.getText().toString().length() > 16) {
                        maperasAnakNikIbuLayout.setError("NIK tidak boleh lebih dari 16 karakter");
                        isNikIbuValid = false;
                    }
                    else {
                        maperasAnakNikIbuLayout.setError(null);
                        isNikIbuValid = true;
                    }
                }
            }
        });

        maperasAnakNamaIbuField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(maperasAnakNamaIbuField.getText().toString().length() == 0) {
                    maperasAnakNamaIbuLayout.setError(null);
                    maperasAnakNamaIbuLayout.setErrorEnabled(false);
                    isNamaIbuValid = true;
                }
                else {
                    if (maperasAnakNamaIbuField.getText().toString().length() < 2) {
                        maperasAnakNamaIbuLayout.setError("Nama tidak boleh kurang dari 2 karakter");
                        maperasAnakNamaIbuLayout.setErrorEnabled(true);
                        isNamaIbuValid = false;
                    }
                    else {
                        maperasAnakNamaIbuField.setError(null);
                        maperasAnakNamaIbuLayout.setErrorEnabled(false);
                        isNamaIbuValid = true;
                    }
                }
            }
        });

        maperasAnakNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNamaAyahValid && isNikAyahValid && isNamaIbuValid && isNikIbuValid) {
                    if (maperasAnakNamaAyahField.getText().toString().length() != 0) {
                        maperasCampuranMasuk.setNamaAyahLama(maperasAnakNamaAyahField.getText().toString());
                    }
                    if (maperasAnakNikAyahField.getText().toString().length() != 0) {
                        maperasCampuranMasuk.setNikAyahLama(maperasAnakNikAyahField.getText().toString());
                    }
                    if (maperasAnakNamaIbuField.getText().toString().length() != 0) {
                        maperasCampuranMasuk.setNamaIbuLama(maperasAnakNamaIbuField.getText().toString());
                    }
                    if (maperasAnakNikIbuField.getText().toString().length() != 0 ) {
                        maperasCampuranMasuk.setNikIbuLama(maperasAnakNikIbuField.getText().toString());
                    }

                    Intent maperasNextIntent = new Intent(getApplicationContext(), MaperasCampuranMasukDataLainnyaActivity.class);
                    maperasNextIntent.putExtra(ANAK_CACAH_KEY, gson.toJson(cacahKramaAnak));
                    if (fotoUri != null) {
                        maperasNextIntent.putExtra(ANAK_CACAH_FOTO_KEY, fotoUri.toString());
                    }
                    maperasNextIntent.putExtra(CAMPURAN_KEY, gson.toJson(maperasCampuranMasuk));
                    startActivityIntent.launch(maperasNextIntent);

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