package com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.campuranmasuk.inputdatapradana;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.adapter.master.BanjarAdatSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.DesaAdatSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.DesaDinasSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.KabupatenSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.KecamatanSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.ProvinsiSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.master.BanjarAdatGetNoPaginationResponse;
import com.bagushikano.sikedatmobileadmin.model.master.DesaAdat;
import com.bagushikano.sikedatmobileadmin.model.master.DesaAdatGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.DesaDinas;
import com.bagushikano.sikedatmobileadmin.model.master.DesaDinasGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Kabupaten;
import com.bagushikano.sikedatmobileadmin.model.master.KabupatenGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Kecamatan;
import com.bagushikano.sikedatmobileadmin.model.master.KecamatanGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Provinsi;
import com.bagushikano.sikedatmobileadmin.model.master.ProvinsiGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarAdat;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.Perkawinan;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerkawinanCampuranMasukDataLainnyaActivity extends AppCompatActivity {

    private Button perkawinanPradanaNext;

    private Boolean isAgamaSebelumnyaSelected = false, isSudhiWadhaniSelected = false, isAlamatAsalValid = false;

    Perkawinan perkawinanCampuranMasuk = new Perkawinan();

    private final String PRADANA_CACAH_KEY = "PRADANA_CACAH_KEY";
    private final String PRADANA_CACAH_FOTO_KEY = "PRADANA_CACAH_FOTO_KEY";
    private final String CAMPURAN_KEY = "CAMPURAN_KEY";
    private final String SUDHI_WADHANI_KEY = "SUDHI_WADHANI_KEY";
    Uri fotoUri;
    Gson gson = new Gson();
    CacahKramaMipil cacahKramaPradana = new CacahKramaMipil();
    ActivityResultLauncher<Intent> startActivityIntent;

    private Uri uriFile;

    private LinearLayout perkawinanPasanganLoadingLayout;

    private AutoCompleteTextView perkawinanKabupatenAutoComplete, perkawinanKecamatanAutoComplete,
            perkawinanDesaDinasAutoComplete, perkawinanProvinsiAutoComplete, perkawinanAgamaAsalAutoComplete;

    private TextInputEditText perkawinanAlamatAsalField, perkawinanBuktiUpcaraSudhiWadhaniField;

    private TextInputLayout perkawinanKabupatenLayout, perkawinanKecamatanLayout,
            perkawinanDesaDinasLayout, perkawinanProvinsiLayout, perkawinanAlamatAsalLayout,
            perkawinanBuktiUpacaraSudhiWadhaniLayout, perkawinanAgamaAsalLayout;

    private KabupatenSelectionAdapter kabupatenSelectionAdapter;
    private KecamatanSelectionAdapter kecamatanSelectionAdapter;
    private DesaDinasSelectionAdapter desaDinasSelectionAdapter;
    private ProvinsiSelectionAdapter provinsiSelectionAdapter;

    private Integer idKabupaten = null, idKecamatan = null, idProvinsi = null;
    private String idDesaDinas = null;
    private Boolean isDesaDinasSelected = false;
    private Kabupaten kabupaten = null;
    private Kecamatan kecamatan = null;
    private DesaDinas desaDinas = null;
    private Provinsi provinsi = null;

    private ArrayList<Kabupaten> kabupatens = new ArrayList<>();
    private ArrayList<Kecamatan> kecamatans = new ArrayList<>();
    private ArrayList<DesaDinas> desaDinasArrayList = new ArrayList<>();
    private ArrayList<Provinsi> provinsis = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perkawinan_campuran_masuk_data_lainnya);

        if (getIntent().hasExtra(PRADANA_CACAH_FOTO_KEY)) {
            fotoUri = Uri.parse(getIntent().getStringExtra(PRADANA_CACAH_FOTO_KEY));
        }
        cacahKramaPradana = gson.fromJson(getIntent().getStringExtra(PRADANA_CACAH_KEY), CacahKramaMipil.class);
        perkawinanCampuranMasuk = gson.fromJson(getIntent().getStringExtra(CAMPURAN_KEY), Perkawinan.class);

        perkawinanPradanaNext = findViewById(R.id.perkawinan_campuran_masuk_pradana_next_button);

        perkawinanPasanganLoadingLayout = findViewById(R.id.perkawinan_campuran_asal_progress_layout);

        perkawinanAlamatAsalField = findViewById(R.id.perkawinan_campuran_masuk_alamat_asal_field);
        perkawinanBuktiUpcaraSudhiWadhaniField = findViewById(R.id.perkawinan_campuran_masuk_bukti_sudhi_wadhani_field);
        perkawinanAgamaAsalAutoComplete = findViewById(R.id.perkawinan_campuran_masuk_agama_sebelumnya_field);

        perkawinanAlamatAsalLayout = findViewById(R.id.perkawinan_campuran_masuk_alamat_asal_layout);
        perkawinanBuktiUpacaraSudhiWadhaniLayout = findViewById(R.id.perkawinan_campuran_masuk_bukti_sudhi_wadhani_layout);
        perkawinanAgamaAsalLayout = findViewById(R.id.perkawinan_campuran_masuk_pradana_agama_sebelumnya_layout);

        perkawinanBuktiUpcaraSudhiWadhaniField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFile();
            }
        });

        perkawinanBuktiUpcaraSudhiWadhaniField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    pickFile();
                }
            }
        });

        String[] agamaArray = new String[] {"Islam", "Protestan", "Katolik", "Hindu", "Buddha", "Khonghucu"};
        ArrayAdapter adapterAgama = new ArrayAdapter<>(this, R.layout.dropdown_menu_item, agamaArray);
        perkawinanAgamaAsalAutoComplete.setAdapter(adapterAgama);
        perkawinanAgamaAsalAutoComplete.setInputType(0);
        perkawinanAgamaAsalAutoComplete.setKeyListener(null);

        perkawinanAgamaAsalAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isAgamaSebelumnyaSelected = true;
            }
        });

        perkawinanAlamatAsalField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (perkawinanAlamatAsalField.getText().toString().length() == 0) {
                    perkawinanAlamatAsalLayout.setError("Alamat asal tidak boleh kosong");
                    perkawinanAlamatAsalLayout.setErrorEnabled(true);
                    isAlamatAsalValid = false;
                }
                else {
                    perkawinanAlamatAsalLayout.setError(null);
                    perkawinanAlamatAsalLayout.setErrorEnabled(false);
                    isAlamatAsalValid = true;
                }
            }
        });

        perkawinanProvinsiAutoComplete = findViewById(R.id.perkawinan_provinsi_field);
        perkawinanKabupatenAutoComplete = findViewById(R.id.perkawinan_kabupaten_field);
        perkawinanKecamatanAutoComplete = findViewById(R.id.perkawinan_kecamatan_field);
        perkawinanDesaDinasAutoComplete = findViewById(R.id.perkawinan_desa_kelurahan_field);

        perkawinanKabupatenLayout = findViewById(R.id.perkawinan_kabupaten_form);
        perkawinanKecamatanLayout = findViewById(R.id.perkawinan_kecamatan_form);
        perkawinanProvinsiLayout = findViewById(R.id.perkawinan_provinsi_form);
        perkawinanDesaDinasLayout = findViewById(R.id.perkawinan_desa_kelurahan_form);

        provinsiSelectionAdapter = new ProvinsiSelectionAdapter(this, provinsis);
        perkawinanProvinsiAutoComplete.setAdapter(provinsiSelectionAdapter);
        perkawinanProvinsiAutoComplete.setInputType(0);
        perkawinanProvinsiAutoComplete.setKeyListener(null);

        perkawinanProvinsiAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                provinsi = (Provinsi) adapterView.getItemAtPosition(i);
                perkawinanProvinsiAutoComplete.setText(provinsi.getName());
                idProvinsi = provinsi.getId();

                kabupaten = null;
                kecamatan = null;
                desaDinas = null;

                idKabupaten = null;
                idKecamatan = null;
                idDesaDinas = null;

                isDesaDinasSelected = false;

                perkawinanKabupatenLayout.setVisibility(View.VISIBLE);
                perkawinanKecamatanLayout.setVisibility(View.GONE);
                perkawinanDesaDinasLayout.setVisibility(View.GONE);

                perkawinanKabupatenAutoComplete.setText(null);
                perkawinanKecamatanAutoComplete.setText(null);
                perkawinanDesaDinasAutoComplete.setText(null);

                getKabupaten();
            }
        });

        kabupatenSelectionAdapter = new KabupatenSelectionAdapter(this, kabupatens);
        perkawinanKabupatenAutoComplete.setAdapter(kabupatenSelectionAdapter);
        perkawinanKabupatenAutoComplete.setInputType(0);
        perkawinanKabupatenAutoComplete.setKeyListener(null);

        perkawinanKabupatenAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                kabupaten = (Kabupaten) adapterView.getItemAtPosition(i);
                perkawinanKabupatenAutoComplete.setText(kabupaten.getName());
                idKabupaten = kabupaten.getId();

                kecamatan = null;
                desaDinas = null;

                idKecamatan = null;
                idDesaDinas = null;

                isDesaDinasSelected = false;

                perkawinanKecamatanLayout.setVisibility(View.VISIBLE);
                perkawinanDesaDinasLayout.setVisibility(View.GONE);

                perkawinanKecamatanAutoComplete.setText(null);
                perkawinanDesaDinasAutoComplete.setText(null);

                getKecamatan();
            }
        });

        kecamatanSelectionAdapter = new KecamatanSelectionAdapter(this, kecamatans);
        perkawinanKecamatanAutoComplete.setAdapter(kecamatanSelectionAdapter);
        perkawinanKecamatanAutoComplete.setThreshold(100);
        perkawinanKecamatanAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                kecamatan = (Kecamatan) adapterView.getItemAtPosition(i);
                perkawinanKecamatanAutoComplete.setText(kecamatan.getName());
                idKecamatan = kecamatan.getId();

                desaDinas = null;
                idDesaDinas = null;
                isDesaDinasSelected = false;
                perkawinanDesaDinasLayout.setVisibility(View.VISIBLE);
                perkawinanDesaDinasAutoComplete.setText(null);
                getDesaDinas();
            }
        });

        desaDinasSelectionAdapter = new DesaDinasSelectionAdapter(this, desaDinasArrayList);
        perkawinanDesaDinasAutoComplete.setAdapter(desaDinasSelectionAdapter);
        perkawinanDesaDinasAutoComplete.setThreshold(100);
        perkawinanDesaDinasAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                desaDinas = (DesaDinas) adapterView.getItemAtPosition(i);
                perkawinanDesaDinasAutoComplete.setText(desaDinas.getName());
                idDesaDinas = desaDinas.getId();
                isDesaDinasSelected = true;
            }
        });

        perkawinanPradanaNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAgamaSebelumnyaSelected && isAlamatAsalValid && isDesaDinasSelected) {
                    Intent perkawinanDataPradanaFinishIntent = new Intent();
                    perkawinanCampuranMasuk.setAgamaAsalPradana(perkawinanAgamaAsalAutoComplete.getText().toString().toLowerCase(Locale.ROOT));
                    perkawinanCampuranMasuk.setAlamatAsalPradana(perkawinanAlamatAsalField.getText().toString());
                    perkawinanCampuranMasuk.setDesaAsalPradanaId(String.valueOf(idDesaDinas));
                    if (isSudhiWadhaniSelected) {
                        perkawinanCampuranMasuk.setFileSudhiWadhani(DocumentFile.fromSingleUri(
                                PerkawinanCampuranMasukDataLainnyaActivity.this, uriFile).getName());
                    }
                    perkawinanDataPradanaFinishIntent.putExtra(PRADANA_CACAH_KEY, gson.toJson(cacahKramaPradana));
                    if (fotoUri != null) {
                        perkawinanDataPradanaFinishIntent.putExtra(PRADANA_CACAH_FOTO_KEY, fotoUri.toString());
                    }
                    perkawinanDataPradanaFinishIntent.putExtra(CAMPURAN_KEY, gson.toJson(perkawinanCampuranMasuk));
                    setResult(700, perkawinanDataPradanaFinishIntent);
                    finish();
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Terdapat data yang belum valid. Silahkan periksa kembali", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        getProvinsi();
    }

    public void getProvinsi() {
        disableForm();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<ProvinsiGetResponse> provinsiGetResponseCall = getData.getMasterProvinsi();
        provinsiGetResponseCall.enqueue(new Callback<ProvinsiGetResponse>() {
            @Override
            public void onResponse(Call<ProvinsiGetResponse> call, Response<ProvinsiGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berhasil memuat data Provinsi.", Snackbar.LENGTH_LONG).show();
                    provinsis.clear();
                    provinsis.addAll(response.body().getData());
                    provinsiSelectionAdapter.notifyDataSetChanged();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Gagal memuat data Provinsi. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
                }
                enableForm();
            }

            @Override
            public void onFailure(Call<ProvinsiGetResponse> call, Throwable t) {
                enableForm();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "Gagal memuat data Provinsi. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
            }
        });
    }


    public void pickFile() {
        Intent chooseFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        String[] mimeTypes = {"image/jpg", "image/jpeg", "application/pdf"};
        chooseFile.setType("*/*");
        chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            uriFile = data.getData();
            perkawinanBuktiUpcaraSudhiWadhaniField.setText("");
            isSudhiWadhaniSelected = false;
            if (DocumentFile.fromSingleUri(this, uriFile).length() > 2000000) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas terlalu besar", Snackbar.LENGTH_SHORT).show();
                perkawinanBuktiUpacaraSudhiWadhaniLayout.setError("Berkas terlalu besar");
            } else {
                File filesDir = getApplicationContext().getCacheDir();
                File file = new File(filesDir, DocumentFile.fromSingleUri(this, uriFile).getName());
                OutputStream os;
                ContentResolver cr = getApplicationContext().getContentResolver();
                InputStream inputStream = null;
                try {
                    inputStream = cr.openInputStream(uriFile);
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
                perkawinanBuktiUpcaraSudhiWadhaniField.setText(DocumentFile.fromSingleUri(this, uriFile).getName());
                isSudhiWadhaniSelected = true;
                perkawinanBuktiUpacaraSudhiWadhaniLayout.setError(null);
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas berhasil di pilih", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    public void getKabupaten() {
        disableForm();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KabupatenGetResponse> kabupatenGetResponseCall = getData.getMasterKabupatenProv(String.valueOf(idProvinsi));
        kabupatenGetResponseCall.enqueue(new Callback<KabupatenGetResponse>() {
            @Override
            public void onResponse(Call<KabupatenGetResponse> call, Response<KabupatenGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berhasil memuat data Kabupaten.", Snackbar.LENGTH_LONG).show();
                    kabupatens.clear();
                    kabupatens.addAll(response.body().getData());
                    kabupatenSelectionAdapter.notifyDataSetChanged();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Gagal memuat data Kabupaten. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
                }
                enableForm();
            }

            @Override
            public void onFailure(Call<KabupatenGetResponse> call, Throwable t) {
                enableForm();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "Gagal memuat data Kabupaten. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void getKecamatan() {
        disableForm();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KecamatanGetResponse> kecamatanGetResponseCall = getData.getMasterKecamatan(String.valueOf(idKabupaten));
        kecamatanGetResponseCall.enqueue(new Callback<KecamatanGetResponse>() {
            @Override
            public void onResponse(Call<KecamatanGetResponse> call, Response<KecamatanGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Berhasil memuat data Kecamatan. Silahkan pilih Kecamatan.", Snackbar.LENGTH_LONG).show();
                    kecamatans.clear();
                    kecamatans.addAll(response.body().getData());
                    kecamatanSelectionAdapter.notifyDataSetChanged();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Gagal memuat data Kecamatan. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
                }
                enableForm();
            }

            @Override
            public void onFailure(Call<KecamatanGetResponse> call, Throwable t) {
                enableForm();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "Gagal memuat data Kecamatan. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void getDesaDinas() {
        disableForm();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<DesaDinasGetResponse> desaDinasGetResponseCall = getData.getMasterDesaDinas(String.valueOf(idKecamatan));
        desaDinasGetResponseCall.enqueue(new Callback<DesaDinasGetResponse>() {
            @Override
            public void onResponse(Call<DesaDinasGetResponse> call, Response<DesaDinasGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Berhasil memuat data Desa/Kelurahan. Silahkan pilih Desa/Kelurahan.", Snackbar.LENGTH_LONG).show();
                    desaDinasArrayList.clear();
                    desaDinasArrayList.addAll(response.body().getData());
                    desaDinasSelectionAdapter.notifyDataSetChanged();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Gagal memuat data Desa/Kelurahan. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
                }
                enableForm();
            }

            @Override
            public void onFailure(Call<DesaDinasGetResponse> call, Throwable t) {
                enableForm();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "Gagal memuat data Desa/Kelurahan. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void disableForm() {
        perkawinanPasanganLoadingLayout.setVisibility(View.VISIBLE);
        perkawinanKabupatenLayout.setEnabled(false);
        perkawinanKecamatanLayout.setEnabled(false);
        perkawinanDesaDinasLayout.setEnabled(false);
        perkawinanProvinsiLayout.setEnabled(false);
    }

    public void enableForm() {
        perkawinanPasanganLoadingLayout.setVisibility(View.GONE);
        perkawinanKabupatenLayout.setEnabled(true);
        perkawinanKecamatanLayout.setEnabled(true);
        perkawinanDesaDinasLayout.setEnabled(true);
        perkawinanProvinsiLayout.setEnabled(true);
    }
}