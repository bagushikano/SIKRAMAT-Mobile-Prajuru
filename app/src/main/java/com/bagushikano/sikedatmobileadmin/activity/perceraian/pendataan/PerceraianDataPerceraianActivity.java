package com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.documentfile.provider.DocumentFile;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.satubanjar.MaperasSatuBanjarCompleteActivity;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.bagushikano.sikedatmobileadmin.model.maperas.MaperasDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.perceraian.Perceraian;
import com.bagushikano.sikedatmobileadmin.model.perceraian.PerceraianDetailResponse;
import com.bagushikano.sikedatmobileadmin.util.ChangeDateTimeFormat;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerceraianDataPerceraianActivity extends AppCompatActivity {

    private final String KRAMA_CERAI_KEY = "KRAMA_CERAI_KEY";
    private final String PASANGAN_CERAI_KEY = "PASANGAN_CERAI_KEY";
    private final String ANGGOTA_CERAI_KEY = "ANGGOTA_CERAI_KEY";
    private final String PERCERAIAN_KEY = "PERCERAIAN_KEY";

    private Button perceraianNextButton;

    private TextInputLayout namaPemuputLayout, tanggalPerceraianLayout,
            noBuktiSerahTerimaLayout, berkasBuktiSerahTerimaLayout, noAktaPerceraianLayout,
            berkasAktaPerceraianLayout, keteranganLayout;

    private TextInputEditText namaPemuputField, tanggalPerceraianField, noBuktiSerahTerimaField,
            berkasBuktiSerahTerimaField, noAktaPerceraianField, berkasAktaPerceraianField, keteranganField;

    Gson gson = new Gson();

    private Uri uriAktaPerceraian, uriBuktiSerahTerimaPerceraian;
    private final String AKTA_FILE_KEY = "BERKAS_AKTA_PERCERAIAN_KEY",
            SERAH_TERIMA_FILE_KEY = "BERKAS_SERAH_TERIMA_PERCERAIAN_KEY";

    private Toolbar homeToolbar;

    private Boolean isTanggalPerceraianValid = false, isNamaPemuputValid = false,
            isNoBuktiSerahTerimaValid = false, isBerkasBuktiSerahTerima = false;

    ActivityResultLauncher<Intent> startActivityIntent;
    SharedPreferences loginPreferences;

    ApiRoute retro;


    KramaMipil kramaMipilSelected;
    CacahKramaMipil pasanganKrama;
    ArrayList<AnggotaKramaMipil> anggotaKramaMipilArrayList = new ArrayList<>();
    Perceraian perceraian;

    Button saveDraftButton;
    LinearLayout maperasProgressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perceraian_data_perceraian);

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        kramaMipilSelected = gson.fromJson(getIntent().getStringExtra(KRAMA_CERAI_KEY), KramaMipil.class);
        pasanganKrama = gson.fromJson(getIntent().getStringExtra(PASANGAN_CERAI_KEY), CacahKramaMipil.class);
        if (getIntent().hasExtra(ANGGOTA_CERAI_KEY)) {
            anggotaKramaMipilArrayList.addAll(
                    gson.fromJson(getIntent().getStringExtra(ANGGOTA_CERAI_KEY),
                            new TypeToken<ArrayList<AnggotaKramaMipil>>(){}.getType()
                    ));
        }
        perceraian = gson.fromJson(getIntent().getStringExtra(PERCERAIAN_KEY), Perceraian.class);

        maperasProgressLayout = findViewById(R.id.maperas_pendataan_progress_layout);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        namaPemuputLayout = findViewById(R.id.perceraian_nama_pemuput_form);
        namaPemuputField = findViewById(R.id.perceraian_nama_pemuput_field);

        tanggalPerceraianLayout = findViewById(R.id.perceraian_tanggal_form);
        tanggalPerceraianField = findViewById(R.id.perceraian_tanggal_field);

        noBuktiSerahTerimaLayout = findViewById(R.id.perceraian_no_bukti_form);
        noBuktiSerahTerimaField = findViewById(R.id.perceraian_no_bukti_field);
        berkasBuktiSerahTerimaLayout = findViewById(R.id.perceraian_berkas_bukti_form);
        berkasBuktiSerahTerimaField = findViewById(R.id.perceraian_berkas_bukti_field);

        noAktaPerceraianLayout = findViewById(R.id.perceraian_no_akta_perceraian_form);
        noAktaPerceraianField = findViewById(R.id.perceraian_no_akta_perceraian_field);
        berkasAktaPerceraianLayout = findViewById(R.id.perceraian_berkas_akta_perceraian_form);
        berkasAktaPerceraianField = findViewById(R.id.perceraian_berkas_akta_perceraian_field);

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


        berkasAktaPerceraianField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFile(2);
            }
        });

        berkasAktaPerceraianField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    pickFile(2);
                }
            }
        });


        keteranganLayout = findViewById(R.id.perceraian_keterangan_form);
        keteranganField = findViewById(R.id.perceraian_keterangan_field);

        MaterialDatePicker.Builder<Long> datePickerBuilderAppointmentDate = MaterialDatePicker.Builder.datePicker();
        datePickerBuilderAppointmentDate.setTitleText("Pilih tanggal perceraian");
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
                tanggalPerceraianField.setText(simpleFormat.format(date));
                isTanggalPerceraianValid = true;
            }
        });

        tanggalPerceraianField.setShowSoftInputOnFocus(false);
        tanggalPerceraianField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(datePickerAppointmentDate.isVisible())) {
                    datePickerAppointmentDate.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                }
            }
        });

        tanggalPerceraianField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        perceraianNextButton = findViewById(R.id.perceraian_next_summary_button);
        perceraianNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNoBuktiSerahTerimaValid && isNamaPemuputValid && isBerkasBuktiSerahTerima && isTanggalPerceraianValid) {
                    submitData();
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Periksa data kembali.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void submitData() {
        retro = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        RequestBody perceraianJsonData, anggotaKeluargaJsonData, fileBuktiSerahTerimaData, fileAktaData;
        Call<PerceraianDetailResponse> maperasDetailResponseCall = null;

        perceraian.setTanggalPerceraian(ChangeDateTimeFormat.changeDateFormatForForm(tanggalPerceraianField.getText().toString()));
        perceraian.setNamaPemuput(namaPemuputField.getText().toString());
        perceraian.setNomorPerceraian(noBuktiSerahTerimaField.getText().toString());
        perceraian.setNomorAktaPerceraian(noAktaPerceraianField.getText().toString());
        perceraian.setKeterangan(keteranganField.getText().toString());

        perceraianJsonData = RequestBody.create(MediaType.parse("text/plain"), gson.toJson(perceraian));
        if (anggotaKramaMipilArrayList.size() != 0) {
            anggotaKeluargaJsonData = RequestBody.create(MediaType.parse("text/plain"), gson.toJson(anggotaKramaMipilArrayList));
        } else {
            anggotaKeluargaJsonData = null;
        }

        MultipartBody.Part buktiSerahTerimaFile = null;
        MultipartBody.Part aktaFile = null;

        /**
         * multi part akta
         */

        if (uriAktaPerceraian != null) {
            File filesDir = getApplicationContext().getCacheDir();
            File file = new File(filesDir, DocumentFile.fromSingleUri(this, uriAktaPerceraian).getName());
            OutputStream os;
            ContentResolver cr = getApplicationContext().getContentResolver();
            InputStream inputStream = null;
            try {
                inputStream = cr.openInputStream(uriAktaPerceraian);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                os = new FileOutputStream(file);
                int length;
                byte[] bytes = new byte[1024];
                while ((length = inputStream.read(bytes)) != -1) {
                    os.write(bytes, 0, length);
                }
                os.flush();
                os.close();
                inputStream.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing temp file", e);
            }
            fileAktaData = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            aktaFile = MultipartBody.Part.createFormData("file_akta_perceraian", file.getName(), fileAktaData);
        }

        /**
         * multi part serah terima
         */

        if (uriBuktiSerahTerimaPerceraian != null) {
            File filesDir = getApplicationContext().getCacheDir();
            File file = new File(filesDir, DocumentFile.fromSingleUri(this, uriBuktiSerahTerimaPerceraian).getName());
            OutputStream os;
            ContentResolver cr = getApplicationContext().getContentResolver();
            InputStream inputStream = null;
            try {
                inputStream = cr.openInputStream(uriBuktiSerahTerimaPerceraian);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                os = new FileOutputStream(file);
                int length;
                byte[] bytes = new byte[1024];
                while ((length = inputStream.read(bytes)) != -1) {
                    os.write(bytes, 0, length);
                }
                os.flush();
                os.close();
                inputStream.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing temp file", e);
            }
            fileBuktiSerahTerimaData = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            buktiSerahTerimaFile = MultipartBody.Part.createFormData("file_bukti_perceraian", file.getName(), fileBuktiSerahTerimaData);
        }

        maperasDetailResponseCall = retro.storePerceraian(
                "Bearer " + loginPreferences.getString("token", "empty"),
                buktiSerahTerimaFile,
                aktaFile,
                perceraianJsonData,
                anggotaKeluargaJsonData
        );

        perceraianNextButton.setVisibility(View.GONE);
        maperasProgressLayout.setVisibility(View.VISIBLE);

        maperasDetailResponseCall.enqueue(new Callback<PerceraianDetailResponse>() {
            @Override
            public void onResponse(Call<PerceraianDetailResponse> call, Response<PerceraianDetailResponse> response) {
                perceraianNextButton.setVisibility(View.VISIBLE);
                maperasProgressLayout.setVisibility(View.GONE);
                if (response.code() == 200 && response.body().getStatusCode() == 200 && response.body().getMessage().equals("data perceraian sukses")) {
                    Intent perkawinanPendataanCompleteIntent = new Intent(getApplicationContext(), PerceraianCompleteActivity.class);
                    Gson gson = new Gson();
                    String perkawinanJson = gson.toJson(response.body().getPerceraian());
                    perkawinanPendataanCompleteIntent.putExtra("PERCERAIAN_DETAIL_KEY", perkawinanJson);
                    startActivity(perkawinanPendataanCompleteIntent);
                    setResult(1);
                    finish();
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PerceraianDetailResponse> call, Throwable t) {
                perceraianNextButton.setVisibility(View.VISIBLE);
                maperasProgressLayout.setVisibility(View.GONE);
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
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
            uriBuktiSerahTerimaPerceraian = data.getData();
            berkasBuktiSerahTerimaField.setText("");
            if (DocumentFile.fromSingleUri(this, uriBuktiSerahTerimaPerceraian).length() > 2000000) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas terlalu besar", Snackbar.LENGTH_SHORT).show();
                berkasBuktiSerahTerimaLayout.setError("Berkas terlalu besar");
            } else {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas Bukti Serah Terima Perceraian berhasil di pilih", Snackbar.LENGTH_SHORT).show();
                berkasBuktiSerahTerimaField.setText(DocumentFile.fromSingleUri(this, uriBuktiSerahTerimaPerceraian).getName());
                berkasBuktiSerahTerimaLayout.setError(null);
                isBerkasBuktiSerahTerima = true;
            }
        }
        else if (resultCode == RESULT_OK && requestCode == 2) {
            uriAktaPerceraian = data.getData();
            berkasAktaPerceraianField.setText("");
            if (DocumentFile.fromSingleUri(this, uriAktaPerceraian).length() > 2000000) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas terlalu besar", Snackbar.LENGTH_SHORT).show();
                berkasAktaPerceraianLayout.setError("Berkas terlalu besar");
            } else {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas Akta Perceraian berhasil di pilih", Snackbar.LENGTH_SHORT).show();
                berkasAktaPerceraianField.setText(DocumentFile.fromSingleUri(this, uriAktaPerceraian).getName());
                berkasAktaPerceraianLayout.setError(null);
            }
        }
    }
}