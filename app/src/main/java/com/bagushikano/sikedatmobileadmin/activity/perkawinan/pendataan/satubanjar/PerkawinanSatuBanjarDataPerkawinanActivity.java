package com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.satubanjar;

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
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.Perkawinan;
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

public class PerkawinanSatuBanjarDataPerkawinanActivity extends AppCompatActivity {

    private Button perkawinanNextButton;

    private TextInputLayout namaPemuputLayout, tanggalPerkawinanLayout,
            noBuktiSerahTerimaLayout, berkasBuktiSerahTerimaLayout, noAktaPerkawinanLayout,
            berkasAktaPerkawinanLayout, keteranganLayout;

    private TextInputEditText namaPemuputField, tanggalPerkawinanField, noBuktiSerahTerimaField,
            berkasBuktiSerahTerimaField, noAktaPerkawinanField, berkasAktaPerkawinanField, keteranganField;

    Gson gson = new Gson();
    private final String PURUSA_KEY = "PURUSA_SELECT_KEY";
    private final String PRADANA_KEY = "PRADANA_SELECT_KEY";
    private final String PERKAWINAN_KEY = "PERKAWINAN_KEY";
    private String purusaJson, pradanaJson;
    Perkawinan perkawinan;

    private Uri uriAktaPerkawinan, uriBuktiSerahTerimaPerkawinan;
    private final String AKTA_FILE_KEY = "BERKAS_AKTA_PERKAWINAN_KEY",
            SERAH_TERIMA_FILE_KEY = "BERKAS_SERAH_TERIMA_PERKAWINAN_KEY";

    private Toolbar homeToolbar;

    private Boolean isTanggalPerkawinanValid = false, isNamaPemuputValid = false,
            isNoBuktiSerahTerimaValid = false, isBerkasBuktiSerahTerima = false;

    ActivityResultLauncher<Intent> startActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perkawinan_satu_banjar_data_perkawinan);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        namaPemuputLayout = findViewById(R.id.perkawinan_nama_pemuput_form);
        namaPemuputField = findViewById(R.id.perkawinan_nama_pemuput_field);

        tanggalPerkawinanLayout = findViewById(R.id.perkawinan_tanggal_form);
        tanggalPerkawinanField = findViewById(R.id.perkawinan_tanggal_field);

        noBuktiSerahTerimaLayout = findViewById(R.id.perkawinan_no_bukti_serah_terima_form);
        noBuktiSerahTerimaField = findViewById(R.id.perkawinan_no_bukti_serah_terima_field);
        berkasBuktiSerahTerimaLayout = findViewById(R.id.perkawinan_berkas_bukti_serah_terima_form);
        berkasBuktiSerahTerimaField = findViewById(R.id.perkawinan_berkas_bukti_serah_terima_field);

        noAktaPerkawinanLayout = findViewById(R.id.perkawinan_no_akta_perkawinan_form);
        noAktaPerkawinanField = findViewById(R.id.perkawinan_no_akta_perkawinan_field);
        berkasAktaPerkawinanLayout = findViewById(R.id.perkawinan_berkas_akta_perkawinan_form);
        berkasAktaPerkawinanField = findViewById(R.id.perkawinan_berkas_akta_perkawinan_field);

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
                    noBuktiSerahTerimaLayout.setError("Nomor Bukti Serah Terima tidak boleh kosong");
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


        berkasAktaPerkawinanField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFile(2);
            }
        });

        berkasAktaPerkawinanField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    pickFile(2);
                }
            }
        });


        keteranganLayout = findViewById(R.id.perkawinan_keterangan_form);
        keteranganField = findViewById(R.id.perkawinan_keterangan_field);

        MaterialDatePicker.Builder<Long> datePickerBuilderAppointmentDate = MaterialDatePicker.Builder.datePicker();
        datePickerBuilderAppointmentDate.setTitleText("Pilih tanggal perkawinan");
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
                tanggalPerkawinanField.setText(simpleFormat.format(date));
                isTanggalPerkawinanValid = true;
            }
        });

        tanggalPerkawinanField.setShowSoftInputOnFocus(false);
        tanggalPerkawinanField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(datePickerAppointmentDate.isVisible())) {
                    datePickerAppointmentDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                }
            }
        });

        tanggalPerkawinanField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        perkawinanNextButton = findViewById(R.id.perkawinan_next_summary_button);
        perkawinanNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNamaPemuputValid && isBerkasBuktiSerahTerima && isNoBuktiSerahTerimaValid && isTanggalPerkawinanValid) {
                    Intent perkawinanNextIntent = new Intent(getApplicationContext(), PerkawinanSatuBanjarSummaryActivity.class);
                    perkawinan = gson.fromJson(getIntent().getStringExtra(PERKAWINAN_KEY), Perkawinan.class);
                    perkawinan.setNamaPemuput(namaPemuputField.getText().toString());
                    perkawinan.setTanggalPerkawinan(ChangeDateTimeFormat.changeDateFormatForForm(
                            tanggalPerkawinanField.getText().toString()));
                    perkawinan.setNomorAktaPerkawinan(noAktaPerkawinanField.getText().toString());
                    perkawinan.setNomorPerkawinan(noBuktiSerahTerimaField.getText().toString());
                    perkawinan.setJenisPerkawinan("satu_banjar_adat");
                    perkawinan.setKeterangan(keteranganField.getText().toString());
                    pradanaJson = getIntent().getStringExtra(PRADANA_KEY);
                    purusaJson = getIntent().getStringExtra(PURUSA_KEY);
                    if (uriAktaPerkawinan != null) {
                        perkawinanNextIntent.putExtra(AKTA_FILE_KEY, uriAktaPerkawinan.toString());
                    }
                    perkawinanNextIntent.putExtra(SERAH_TERIMA_FILE_KEY, uriBuktiSerahTerimaPerkawinan.toString());
                    perkawinanNextIntent.putExtra(PURUSA_KEY, purusaJson);
                    perkawinanNextIntent.putExtra(PRADANA_KEY, pradanaJson);
                    perkawinanNextIntent.putExtra(PERKAWINAN_KEY, gson.toJson(perkawinan));
                    startActivityIntent.launch(perkawinanNextIntent);
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
            uriBuktiSerahTerimaPerkawinan = data.getData();
            berkasBuktiSerahTerimaField.setText("");
            if (DocumentFile.fromSingleUri(this, uriBuktiSerahTerimaPerkawinan).length() > 2000000) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas terlalu besar", Snackbar.LENGTH_SHORT).show();
                berkasBuktiSerahTerimaLayout.setError("Berkas terlalu besar");
            } else {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas Bukti Serah Terima Perkawinan berhasil dipilih", Snackbar.LENGTH_SHORT).show();
                berkasBuktiSerahTerimaField.setText(DocumentFile.fromSingleUri(this, uriBuktiSerahTerimaPerkawinan).getName());
                berkasBuktiSerahTerimaLayout.setError(null);
                isBerkasBuktiSerahTerima = true;
            }
        }
        else if (resultCode == RESULT_OK && requestCode == 2) {
            uriAktaPerkawinan = data.getData();
            berkasAktaPerkawinanField.setText("");
            if (DocumentFile.fromSingleUri(this, uriAktaPerkawinan).length() > 2000000) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas terlalu besar", Snackbar.LENGTH_SHORT).show();
                berkasAktaPerkawinanLayout.setError("Berkas terlalu besar");
            } else {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas Akta Perkawinan berhasil dipilih", Snackbar.LENGTH_SHORT).show();
                berkasAktaPerkawinanField.setText(DocumentFile.fromSingleUri(this, uriAktaPerkawinan).getName());
                berkasAktaPerkawinanLayout.setError(null);
            }
        }
    }
}