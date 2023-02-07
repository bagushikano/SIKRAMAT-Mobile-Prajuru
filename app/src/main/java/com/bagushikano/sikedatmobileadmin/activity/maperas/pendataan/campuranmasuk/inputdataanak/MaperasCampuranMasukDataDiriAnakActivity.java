package com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.campuranmasuk.inputdataanak;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.adapter.master.PekerjaanSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.PendidikanSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.master.Pekerjaan;
import com.bagushikano.sikedatmobileadmin.model.master.PekerjaanGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Pendidikan;
import com.bagushikano.sikedatmobileadmin.model.master.PendidikanGetResponse;
import com.bagushikano.sikedatmobileadmin.util.ChangeDateTimeFormat;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaperasCampuranMasukDataDiriAnakActivity extends AppCompatActivity {


    private Button maperasAnakNext;
    private TextInputEditText maperasAnakTempatLahirField, maperasAnakTanggalLahirField,
            maperasAnakNotelpField;

    private TextInputLayout maperasAnakTempatLahirLayout, maperasAnakTanggalLahirLayout, maperasAnakNotelpLayout,
            maperasAnakJenisKelaminLayout, maperasAnakGoldarLayout, maperasAnakPekerjaanLayout, maperasAnakPendidikanLayout;

    private AutoCompleteTextView jkAutoComplete, goldarAutoComplete, pendidikanAutoComplete, pekerjaanAutoComplete;

    private String jenisKelamin;

    private Boolean isTempatLahirValid = false, isTanggalLahirValid = false,
            isGoldarValid = false, isPendidikanValid = false, isPekerjaanValid = false, isJenisKelaminValid = false;

    private PekerjaanSelectionAdapter pekerjaanSelectionAdapter;
    private PendidikanSelectionAdapter pendidikanSelectionAdapter;
    private ArrayList<Pendidikan> pendidikanArrayList = new ArrayList<>();
    private ArrayList<Pekerjaan> pekerjaanArrayList = new ArrayList<>();

    private Pendidikan pendidikan = null;
    private Pekerjaan pekerjaan = null;

    private final String ANAK_CACAH_KEY = "ANAK_CACAH_KEY";
    private final String ANAK_CACAH_FOTO_KEY = "ANAK_CACAH_FOTO_KEY";

    Gson gson = new Gson();

    Uri fotoUri;

    CacahKramaMipil cacahKramaAnak = new CacahKramaMipil();

    ActivityResultLauncher<Intent> startActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maperas_campuran_masuk_data_diri_anak);

        if (getIntent().hasExtra(ANAK_CACAH_FOTO_KEY)) {
            fotoUri = Uri.parse(getIntent().getStringExtra(ANAK_CACAH_FOTO_KEY));
        }
        cacahKramaAnak = gson.fromJson(getIntent().getStringExtra(ANAK_CACAH_KEY), CacahKramaMipil.class);

        maperasAnakNext = findViewById(R.id.maperas_campuran_masuk_anak_next_button);
        maperasAnakTempatLahirField = findViewById(R.id.maperas_campuran_masuk_anak_tempat_lahir_field);
        maperasAnakTanggalLahirField = findViewById(R.id.maperas_campuran_masuk_anak_tanggal_lahir_Field);
        maperasAnakNotelpField = findViewById(R.id.maperas_campuran_masuk_anak_notelp_field);

        maperasAnakTempatLahirLayout = findViewById(R.id.maperas_campuran_masuk_anak_tempat_lahir_layout);
        maperasAnakTanggalLahirLayout = findViewById(R.id.maperas_campuran_masuk_anak_tanggal_lahir_layout);
        maperasAnakNotelpLayout = findViewById(R.id.maperas_campuran_masuk_anak_notelp_layout);
        maperasAnakJenisKelaminLayout = findViewById(R.id.maperas_campuran_masuk_anak_jk_layout);
        maperasAnakGoldarLayout = findViewById(R.id.maperas_campuran_masuk_anak_goldar_layout);
        maperasAnakPekerjaanLayout = findViewById(R.id.maperas_campuran_masuk_anak_pekerjaan_layout);
        maperasAnakPendidikanLayout = findViewById(R.id.maperas_campuran_masuk_anak_pendidikan_tertinggi_layout);

        jkAutoComplete = findViewById(R.id.maperas_campuran_masuk_anak_jk_field);
        goldarAutoComplete = findViewById(R.id.maperas_campuran_masuk_anak_goldar_field);
        pendidikanAutoComplete = findViewById(R.id.maperas_campuran_masuk_anak_pendidikan_tertinggi_field);
        pekerjaanAutoComplete = findViewById(R.id.maperas_campuran_masuk_anak_pekerjaan_field);

        maperasAnakTempatLahirField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(maperasAnakTempatLahirField.getText().toString().length() == 0) {
                    maperasAnakTempatLahirLayout.setError("Tempat lahir tidak boleh kosong");
                    maperasAnakTempatLahirLayout.setErrorEnabled(true);
                    isTempatLahirValid = false;
                }
                else {
                    maperasAnakTempatLahirLayout.setError(null);
                    maperasAnakTempatLahirLayout.setErrorEnabled(false);
                    isTempatLahirValid = true;
                }
            }
        });

        String[] jkAnak = new String[] {"Laki-Laki", "Perempuan"};
        ArrayAdapter adapterJk = new ArrayAdapter<>(this, R.layout.dropdown_menu_item, jkAnak);
        jkAutoComplete.setAdapter(adapterJk);
        jkAutoComplete.setInputType(0);
        jkAutoComplete.setKeyListener(null);

        String[] goldarAnak = new String[] {"A", "B", "AB", "O", "-"};
        ArrayAdapter adapterGoldar = new ArrayAdapter<>(this, R.layout.dropdown_menu_item, goldarAnak);
        goldarAutoComplete.setAdapter(adapterGoldar);
        goldarAutoComplete.setInputType(0);
        goldarAutoComplete.setKeyListener(null);

        goldarAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isGoldarValid = true;
            }
        });

        jkAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    jenisKelamin = "laki-laki";
                }
                else {
                    jenisKelamin = "perempuan";
                }
                isJenisKelaminValid = true;
            }
        });

        MaterialDatePicker.Builder<Long> datePickerBuilderAppointmentDate = MaterialDatePicker.Builder.datePicker();
        datePickerBuilderAppointmentDate.setTitleText("Pilih tanggal lahir");
        datePickerBuilderAppointmentDate.setCalendarConstraints(
                new CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.before(
                        new Date().getTime())).build()
        );
        final MaterialDatePicker<Long> datePickerAppointmentDate = datePickerBuilderAppointmentDate.build();
        datePickerAppointmentDate.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selectedDate) {
                // link: https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
                TimeZone timeZoneUTC = TimeZone.getDefault();
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                SimpleDateFormat simpleFormat = new SimpleDateFormat("EEE, dd-MM-yyyy", Locale.US);
                Date date = new Date(selectedDate + offsetFromUTC);
                maperasAnakTanggalLahirField.setText(simpleFormat.format(date));
                isTanggalLahirValid = true;
            }
        });

        maperasAnakTanggalLahirField.setShowSoftInputOnFocus(false);
        maperasAnakTanggalLahirField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(datePickerAppointmentDate.isVisible())) {
                    datePickerAppointmentDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                }
            }
        });

        maperasAnakTanggalLahirField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    if (!(datePickerAppointmentDate.isVisible())) {
                        datePickerAppointmentDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                }
            }
        });

        pendidikanSelectionAdapter = new PendidikanSelectionAdapter(this, pendidikanArrayList);
        pendidikanAutoComplete.setAdapter(pendidikanSelectionAdapter);
        pendidikanAutoComplete.setInputType(0);
        pendidikanAutoComplete.setKeyListener(null);

        pendidikanAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pendidikan = (Pendidikan) adapterView.getItemAtPosition(i);
                pendidikanAutoComplete.setText(pendidikan.getJenjangPendidikan());
                isPendidikanValid = true;
            }
        });

        pekerjaanSelectionAdapter = new PekerjaanSelectionAdapter(this, pekerjaanArrayList);
        pekerjaanAutoComplete.setAdapter(pekerjaanSelectionAdapter);
        pekerjaanAutoComplete.setInputType(0);
        pekerjaanAutoComplete.setKeyListener(null);

        pekerjaanAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pekerjaan = (Pekerjaan) adapterView.getItemAtPosition(i);
                pekerjaanAutoComplete.setText(pekerjaan.getProfesi());
                isPekerjaanValid = true;
            }
        });

        maperasAnakNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTempatLahirValid && isTanggalLahirValid && isGoldarValid && isPendidikanValid
                        && isPekerjaanValid && isJenisKelaminValid ) {
                    cacahKramaAnak.penduduk.setPekerjaan(pekerjaan);
                    cacahKramaAnak.penduduk.setPendidikan(pendidikan);
                    cacahKramaAnak.penduduk.setTempatLahir(maperasAnakTempatLahirField.getText().toString());
                    cacahKramaAnak.penduduk.setTanggalLahir(
                            ChangeDateTimeFormat.changeDateFormatForForm(maperasAnakTanggalLahirField.getText().toString()));
                    cacahKramaAnak.penduduk.setJenisKelamin(jenisKelamin);
                    cacahKramaAnak.penduduk.setGolonganDarah(goldarAutoComplete.getText().toString());
                    if (maperasAnakNotelpField.getText().toString().length() != 0) {
                        cacahKramaAnak.penduduk.setTelepon(maperasAnakNotelpField.getText().toString());
                    }

                    Intent maperasNextIntent = new Intent(getApplicationContext(), MaperasCampuranMasukDataOrangTuaAnakActivity.class);
                    maperasNextIntent.putExtra(ANAK_CACAH_KEY, gson.toJson(cacahKramaAnak));
                    if (fotoUri != null) {
                        maperasNextIntent.putExtra(ANAK_CACAH_FOTO_KEY, fotoUri.toString());
                    }
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

        getPendidikan();
        getPekerjaan();
    }


    public void getPendidikan() {
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<PendidikanGetResponse> pendidikanGetResponseCall
                = getData.getMasterPendidikan();
        pendidikanGetResponseCall.enqueue(new Callback<PendidikanGetResponse>() {
            @Override
            public void onResponse(Call<PendidikanGetResponse> call, Response<PendidikanGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    pendidikanArrayList.clear();
                    pendidikanArrayList.addAll(response.body().getData());
                    pendidikanSelectionAdapter.notifyDataSetChanged();
                }
                else {

                }

            }
            @Override
            public void onFailure(Call<PendidikanGetResponse> call, Throwable t) {

            }
        });
    }

    public void getPekerjaan() {
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<PekerjaanGetResponse> pekerjaanGetResponseCall
                = getData.getMasterPekerjaan();
        pekerjaanGetResponseCall.enqueue(new Callback<PekerjaanGetResponse>() {
            @Override
            public void onResponse(Call<PekerjaanGetResponse> call, Response<PekerjaanGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    pekerjaanArrayList.clear();
                    pekerjaanArrayList.addAll(response.body().getData());
                    pekerjaanSelectionAdapter.notifyDataSetChanged();
                }
                else {

                }

            }
            @Override
            public void onFailure(Call<PekerjaanGetResponse> call, Throwable t) {

            }
        });
    }
}