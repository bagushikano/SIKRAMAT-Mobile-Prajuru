package com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.satubanjar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.documentfile.provider.DocumentFile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.satubanjar.MaperasSatuBanjarSummaryActivity;
import com.bagushikano.sikedatmobileadmin.model.maperas.Maperas;
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
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MaperasSatuBanjarDataMaperasActivity extends AppCompatActivity {

    private Button maperasNextButton;

    private TextInputLayout namaPemuputLayout, tanggalMaperasLayout,
            noBuktiSerahTerimaLayout, berkasBuktiSerahTerimaLayout, noAktaMaperasLayout,
            berkasAktaMaperasLayout, keteranganLayout;

    private TextInputEditText namaPemuputField, tanggalMaperasField, noBuktiSerahTerimaField,
            berkasBuktiSerahTerimaField, noAktaMaperasField, berkasAktaMaperasField, keteranganField;

    Gson gson = new Gson();
    private final String ANAK_KEY = "ANAK_KEY";
    private final String KRAMA_LAMA_KEY = "KRAMA_LAMA_KEY";
    private final String KRAMA_BARU_KEY = "KRAMA_BARU_KEY";
    private final String AYAH_KEY = "AYAH_KEY";
    private final String IBU_KEY = "IBU_KEY";
    private final String MAPERAS_KEY = "MAPERAS_KEY";

    private String anakJson, kramaLamaJson, kramaBaruJson, ayahJson, ibuJson;
    Maperas maperas;

    private Uri uriAktaMaperas, uriBuktiSerahTerimaMaperas;
    private final String AKTA_FILE_KEY = "BERKAS_AKTA_MAPERAS_KEY",
            SERAH_TERIMA_FILE_KEY = "BERKAS_SERAH_TERIMA_MAPERAS_KEY";

    private Toolbar homeToolbar;

    private Boolean isTanggalMaperasValid = false, isNamaPemuputValid = false,
            isNoBuktiSerahTerimaValid = false, isBerkasBuktiSerahTerima = false;

    ActivityResultLauncher<Intent> startActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maperas_satu_banjar_data_maperas);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        namaPemuputLayout = findViewById(R.id.maperas_nama_pemuput_form);
        namaPemuputField = findViewById(R.id.maperas_nama_pemuput_field);

        tanggalMaperasLayout = findViewById(R.id.maperas_tanggal_form);
        tanggalMaperasField = findViewById(R.id.maperas_tanggal_field);

        noBuktiSerahTerimaLayout = findViewById(R.id.maperas_no_bukti_form);
        noBuktiSerahTerimaField = findViewById(R.id.maperas_no_bukti_field);
        berkasBuktiSerahTerimaLayout = findViewById(R.id.maperas_berkas_bukti_form);
        berkasBuktiSerahTerimaField = findViewById(R.id.maperas_berkas_bukti_field);

        noAktaMaperasLayout = findViewById(R.id.maperas_no_akta_maperas_form);
        noAktaMaperasField = findViewById(R.id.maperas_no_akta_maperas_field);
        berkasAktaMaperasLayout = findViewById(R.id.maperas_berkas_akta_maperas_form);
        berkasAktaMaperasField = findViewById(R.id.maperas_berkas_akta_maperas_field);

        namaPemuputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (namaPemuputField.getText().toString().length() == 0) {
                    namaPemuputLayout.setErrorEnabled(true);
                    namaPemuputLayout.setError("Nama Pemuput tidak boleh kosong");
                } else if (namaPemuputField.getText().toString().length() < 3) {
                    namaPemuputLayout.setErrorEnabled(true);
                    namaPemuputLayout.setError("Nama Pemuput tidak boleh kurang dari 3 karakter");
                } else {
                    namaPemuputLayout.setErrorEnabled(false);
                    namaPemuputLayout.setError(null);
                    isNamaPemuputValid = true;
                }
            }
        });

        noBuktiSerahTerimaField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (noBuktiSerahTerimaField.getText().toString().length() == 0) {
                    noBuktiSerahTerimaLayout.setErrorEnabled(true);
                    noBuktiSerahTerimaLayout.setError("Nomor Bukti Maperas tidak boleh kosong");
                } else {
                    noBuktiSerahTerimaLayout.setErrorEnabled(false);
                    noBuktiSerahTerimaLayout.setError(null);
                    isNoBuktiSerahTerimaValid = true;
                }
            }
        });

        berkasBuktiSerahTerimaField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFile(1);
            }
        });

        berkasBuktiSerahTerimaField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    pickFile(1);
                }
            }
        });


        berkasAktaMaperasField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFile(2);
            }
        });

        berkasAktaMaperasField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    pickFile(2);
                }
            }
        });


        keteranganLayout = findViewById(R.id.maperas_keterangan_form);
        keteranganField = findViewById(R.id.maperas_keterangan_field);

        MaterialDatePicker.Builder<Long> datePickerBuilderAppointmentDate = MaterialDatePicker.Builder.datePicker();
        datePickerBuilderAppointmentDate.setTitleText("Pilih tanggal maperas");
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
                tanggalMaperasField.setText(simpleFormat.format(date));
                isTanggalMaperasValid = true;
            }
        });

        tanggalMaperasField.setShowSoftInputOnFocus(false);
        tanggalMaperasField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(datePickerAppointmentDate.isVisible())) {
                    datePickerAppointmentDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                }
            }
        });

        tanggalMaperasField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    if (!(datePickerAppointmentDate.isVisible())) {
                        datePickerAppointmentDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                }
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

        maperasNextButton = findViewById(R.id.maperas_next_summary_button);
        maperasNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNamaPemuputValid && isBerkasBuktiSerahTerima && isNoBuktiSerahTerimaValid && isTanggalMaperasValid) {
                    Intent maperasNextIntent = new Intent(getApplicationContext(), MaperasSatuBanjarSummaryActivity.class);
                    maperas = gson.fromJson(getIntent().getStringExtra(MAPERAS_KEY), Maperas.class);
                    maperas.setNamaPemuput(namaPemuputField.getText().toString());
                    maperas.setTanggalMaperas(ChangeDateTimeFormat.changeDateFormatForForm(
                            tanggalMaperasField.getText().toString()));
                    maperas.setNomorAktaPengangkatanAnak(noAktaMaperasField.getText().toString());
                    maperas.setNomorMaperas(noBuktiSerahTerimaField.getText().toString());
                    maperas.setJenisMaperas("satu_banjar_adat");
                    maperas.setKeterangan(keteranganField.getText().toString());
                    anakJson = getIntent().getStringExtra(ANAK_KEY);
                    kramaBaruJson = getIntent().getStringExtra(KRAMA_BARU_KEY);
                    kramaLamaJson = getIntent().getStringExtra(KRAMA_LAMA_KEY);
                    ayahJson = getIntent().getStringExtra(AYAH_KEY);
                    ibuJson = getIntent().getStringExtra(IBU_KEY);
                    if (uriAktaMaperas != null) {
                        maperasNextIntent.putExtra(AKTA_FILE_KEY, uriAktaMaperas.toString());
                    }
                    maperasNextIntent.putExtra(SERAH_TERIMA_FILE_KEY, uriBuktiSerahTerimaMaperas.toString());
                    maperasNextIntent.putExtra(MAPERAS_KEY, gson.toJson(maperas));
                    maperasNextIntent.putExtra(ANAK_KEY, anakJson);
                    maperasNextIntent.putExtra(KRAMA_LAMA_KEY, kramaLamaJson);
                    maperasNextIntent.putExtra(KRAMA_BARU_KEY, kramaBaruJson);
                    maperasNextIntent.putExtra(AYAH_KEY, ayahJson);
                    maperasNextIntent.putExtra(IBU_KEY, ibuJson);
                    startActivityIntent.launch(maperasNextIntent);
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Terdapat data yang belum valid. Silahkan periksa kembali.", Snackbar.LENGTH_SHORT).show();

                }
            }
        });
    }

    // 1 berkas serah terima, 2 berkas akta
    public void pickFile(int flag) {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        String[] mimeTypes = {"image/jpg", "image/jpeg", "application/pdf"};
        chooseFile.setType("*/*");
        chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, flag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            uriBuktiSerahTerimaMaperas = data.getData();
            berkasBuktiSerahTerimaField.setText("");
            if (DocumentFile.fromSingleUri(this, uriBuktiSerahTerimaMaperas).length() > 2000000) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas terlalu besar", Snackbar.LENGTH_SHORT).show();
                berkasBuktiSerahTerimaLayout.setError("Berkas terlalu besar");
            } else {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas Bukti Maperas berhasil dipilih", Snackbar.LENGTH_SHORT).show();
                berkasBuktiSerahTerimaField.setText(DocumentFile.fromSingleUri(this, uriBuktiSerahTerimaMaperas).getName());
                berkasBuktiSerahTerimaLayout.setError(null);
                isBerkasBuktiSerahTerima = true;
            }
        }
        else if (resultCode == RESULT_OK && requestCode == 2) {
            uriAktaMaperas = data.getData();
            berkasAktaMaperasField.setText("");
            if (DocumentFile.fromSingleUri(this, uriAktaMaperas).length() > 2000000) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas terlalu besar", Snackbar.LENGTH_SHORT).show();
                berkasAktaMaperasLayout.setError("Berkas terlalu besar");
            } else {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas Akta Maperas berhasil dipilih", Snackbar.LENGTH_SHORT).show();
                berkasAktaMaperasField.setText(DocumentFile.fromSingleUri(this, uriAktaMaperas).getName());
                berkasAktaMaperasLayout.setError(null);
            }
        }
    }
}