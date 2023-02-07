package com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.campuranmasuk.inputdataanak;

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
import com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.campuranmasuk.inputdataanak.MaperasCampuranMasukDataLainnyaActivity;
import com.bagushikano.sikedatmobileadmin.adapter.master.DesaDinasSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.KabupatenSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.KecamatanSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.ProvinsiSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.master.DesaDinas;
import com.bagushikano.sikedatmobileadmin.model.master.DesaDinasGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Kabupaten;
import com.bagushikano.sikedatmobileadmin.model.master.KabupatenGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Kecamatan;
import com.bagushikano.sikedatmobileadmin.model.master.KecamatanGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Provinsi;
import com.bagushikano.sikedatmobileadmin.model.master.ProvinsiGetResponse;
import com.bagushikano.sikedatmobileadmin.model.maperas.Maperas;
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

public class MaperasCampuranMasukDataLainnyaActivity extends AppCompatActivity {

    private Button maperasAnakNext;

    private Boolean isAgamaSebelumnyaSelected = false, isSudhiWadhaniSelected = false, isAlamatAsalValid = false;

    Maperas maperasCampuranMasuk = new Maperas();

    private final String ANAK_CACAH_KEY = "ANAK_CACAH_KEY";
    private final String ANAK_CACAH_FOTO_KEY = "ANAK_CACAH_FOTO_KEY";
    private final String CAMPURAN_KEY = "CAMPURAN_KEY";
    private final String SUDHI_WADHANI_KEY = "SUDHI_WADHANI_KEY";
    Uri fotoUri;
    Gson gson = new Gson();
    CacahKramaMipil cacahKramaAnak = new CacahKramaMipil();
    ActivityResultLauncher<Intent> startActivityIntent;

    private Uri uriFile;

    private LinearLayout maperasPasanganLoadingLayout;

    private AutoCompleteTextView maperasKabupatenAutoComplete, maperasKecamatanAutoComplete,
            maperasDesaDinasAutoComplete, maperasProvinsiAutoComplete, maperasAgamaAsalAutoComplete;

    private TextInputEditText maperasAlamatAsalField, maperasBuktiUpcaraSudhiWadhaniField;

    private TextInputLayout maperasKabupatenLayout, maperasKecamatanLayout,
            maperasDesaDinasLayout, maperasProvinsiLayout, maperasAlamatAsalLayout,
            maperasBuktiUpacaraSudhiWadhaniLayout, maperasAgamaAsalLayout;

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
        setContentView(R.layout.activity_maperas_campuran_masuk_data_lainnya);

        if (getIntent().hasExtra(ANAK_CACAH_FOTO_KEY)) {
            fotoUri = Uri.parse(getIntent().getStringExtra(ANAK_CACAH_FOTO_KEY));
        }
        cacahKramaAnak = gson.fromJson(getIntent().getStringExtra(ANAK_CACAH_KEY), CacahKramaMipil.class);
        maperasCampuranMasuk = gson.fromJson(getIntent().getStringExtra(CAMPURAN_KEY), Maperas.class);

        maperasAnakNext = findViewById(R.id.maperas_campuran_masuk_anak_next_button);

        maperasPasanganLoadingLayout = findViewById(R.id.maperas_campuran_asal_progress_layout);

        maperasAlamatAsalField = findViewById(R.id.maperas_campuran_masuk_alamat_asal_field);
        maperasBuktiUpcaraSudhiWadhaniField = findViewById(R.id.maperas_campuran_masuk_bukti_sudhi_wadhani_field);
        maperasAgamaAsalAutoComplete = findViewById(R.id.maperas_campuran_masuk_agama_sebelumnya_field);

        maperasAlamatAsalLayout = findViewById(R.id.maperas_campuran_masuk_alamat_asal_layout);
        maperasBuktiUpacaraSudhiWadhaniLayout = findViewById(R.id.maperas_campuran_masuk_bukti_sudhi_wadhani_layout);
        maperasAgamaAsalLayout = findViewById(R.id.maperas_campuran_masuk_anak_agama_sebelumnya_layout);

        maperasBuktiUpcaraSudhiWadhaniField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFile();
            }
        });

        maperasBuktiUpcaraSudhiWadhaniField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    pickFile();
                }
            }
        });

        String[] agamaArray = new String[] {"Islam", "Protestan", "Katolik", "Hindu", "Buddha", "Khonghucu"};
        ArrayAdapter adapterAgama = new ArrayAdapter<>(this, R.layout.dropdown_menu_item, agamaArray);
        maperasAgamaAsalAutoComplete.setAdapter(adapterAgama);
        maperasAgamaAsalAutoComplete.setInputType(0);
        maperasAgamaAsalAutoComplete.setKeyListener(null);

        maperasAgamaAsalAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isAgamaSebelumnyaSelected = true;
            }
        });

        maperasAlamatAsalField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (maperasAlamatAsalField.getText().toString().length() == 0) {
                    maperasAlamatAsalLayout.setError("Alamat asal tidak boleh kosong");
                    maperasAlamatAsalLayout.setErrorEnabled(true);
                    isAlamatAsalValid = false;
                }
                else {
                    maperasAlamatAsalLayout.setError(null);
                    maperasAlamatAsalLayout.setErrorEnabled(false);
                    isAlamatAsalValid = true;
                }
            }
        });

        maperasProvinsiAutoComplete = findViewById(R.id.maperas_provinsi_field);
        maperasKabupatenAutoComplete = findViewById(R.id.maperas_kabupaten_field);
        maperasKecamatanAutoComplete = findViewById(R.id.maperas_kecamatan_field);
        maperasDesaDinasAutoComplete = findViewById(R.id.maperas_desa_kelurahan_field);

        maperasKabupatenLayout = findViewById(R.id.maperas_kabupaten_form);
        maperasKecamatanLayout = findViewById(R.id.maperas_kecamatan_form);
        maperasProvinsiLayout = findViewById(R.id.maperas_provinsi_form);
        maperasDesaDinasLayout = findViewById(R.id.maperas_desa_kelurahan_form);

        provinsiSelectionAdapter = new ProvinsiSelectionAdapter(this, provinsis);
        maperasProvinsiAutoComplete.setAdapter(provinsiSelectionAdapter);
        maperasProvinsiAutoComplete.setInputType(0);
        maperasProvinsiAutoComplete.setKeyListener(null);

        maperasProvinsiAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                provinsi = (Provinsi) adapterView.getItemAtPosition(i);
                maperasProvinsiAutoComplete.setText(provinsi.getName());
                idProvinsi = provinsi.getId();

                kabupaten = null;
                kecamatan = null;
                desaDinas = null;

                idKabupaten = null;
                idKecamatan = null;
                idDesaDinas = null;

                isDesaDinasSelected = false;

                maperasKabupatenLayout.setVisibility(View.VISIBLE);
                maperasKecamatanLayout.setVisibility(View.GONE);
                maperasDesaDinasLayout.setVisibility(View.GONE);

                maperasKabupatenAutoComplete.setText(null);
                maperasKecamatanAutoComplete.setText(null);
                maperasDesaDinasAutoComplete.setText(null);

                getKabupaten();
            }
        });

        kabupatenSelectionAdapter = new KabupatenSelectionAdapter(this, kabupatens);
        maperasKabupatenAutoComplete.setAdapter(kabupatenSelectionAdapter);
        maperasKabupatenAutoComplete.setInputType(0);
        maperasKabupatenAutoComplete.setKeyListener(null);

        maperasKabupatenAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                kabupaten = (Kabupaten) adapterView.getItemAtPosition(i);
                maperasKabupatenAutoComplete.setText(kabupaten.getName());
                idKabupaten = kabupaten.getId();

                kecamatan = null;
                desaDinas = null;

                idKecamatan = null;
                idDesaDinas = null;

                isDesaDinasSelected = false;

                maperasKecamatanLayout.setVisibility(View.VISIBLE);
                maperasDesaDinasLayout.setVisibility(View.GONE);

                maperasKecamatanAutoComplete.setText(null);
                maperasDesaDinasAutoComplete.setText(null);

                getKecamatan();
            }
        });

        kecamatanSelectionAdapter = new KecamatanSelectionAdapter(this, kecamatans);
        maperasKecamatanAutoComplete.setAdapter(kecamatanSelectionAdapter);
        maperasKecamatanAutoComplete.setThreshold(100);
        maperasKecamatanAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                kecamatan = (Kecamatan) adapterView.getItemAtPosition(i);
                maperasKecamatanAutoComplete.setText(kecamatan.getName());
                idKecamatan = kecamatan.getId();

                desaDinas = null;
                idDesaDinas = null;
                isDesaDinasSelected = false;
                maperasDesaDinasLayout.setVisibility(View.VISIBLE);
                maperasDesaDinasAutoComplete.setText(null);
                getDesaDinas();
            }
        });

        desaDinasSelectionAdapter = new DesaDinasSelectionAdapter(this, desaDinasArrayList);
        maperasDesaDinasAutoComplete.setAdapter(desaDinasSelectionAdapter);
        maperasDesaDinasAutoComplete.setThreshold(100);
        maperasDesaDinasAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                desaDinas = (DesaDinas) adapterView.getItemAtPosition(i);
                maperasDesaDinasAutoComplete.setText(desaDinas.getName());
                idDesaDinas = desaDinas.getId();
                isDesaDinasSelected = true;
            }
        });

        maperasAnakNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAgamaSebelumnyaSelected && isAlamatAsalValid && isDesaDinasSelected) {
                    Intent maperasDataAnakFinishIntent = new Intent();
                    maperasCampuranMasuk.setAgamaLama(maperasAgamaAsalAutoComplete.getText().toString().toLowerCase(Locale.ROOT));
                    maperasCampuranMasuk.setAlamatAsal(maperasAlamatAsalField.getText().toString());
                    maperasCampuranMasuk.setDesaAsalId(String.valueOf(idDesaDinas));
                    if (isSudhiWadhaniSelected) {
                        maperasCampuranMasuk.setFileSudhiWadhani(DocumentFile.fromSingleUri(
                                MaperasCampuranMasukDataLainnyaActivity.this, uriFile).getName());
                    }
                    maperasDataAnakFinishIntent.putExtra(ANAK_CACAH_KEY, gson.toJson(cacahKramaAnak));
                    if (fotoUri != null) {
                        maperasDataAnakFinishIntent.putExtra(ANAK_CACAH_FOTO_KEY, fotoUri.toString());
                    }
                    maperasDataAnakFinishIntent.putExtra(CAMPURAN_KEY, gson.toJson(maperasCampuranMasuk));
                    setResult(700, maperasDataAnakFinishIntent);
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
            maperasBuktiUpcaraSudhiWadhaniField.setText("");
            isSudhiWadhaniSelected = false;
            if (DocumentFile.fromSingleUri(this, uriFile).length() > 2000000) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Berkas terlalu besar", Snackbar.LENGTH_SHORT).show();
                maperasBuktiUpacaraSudhiWadhaniLayout.setError("Berkas terlalu besar");
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
                maperasBuktiUpcaraSudhiWadhaniField.setText(DocumentFile.fromSingleUri(this, uriFile).getName());
                isSudhiWadhaniSelected = true;
                maperasBuktiUpacaraSudhiWadhaniLayout.setError(null);
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
        maperasPasanganLoadingLayout.setVisibility(View.VISIBLE);
        maperasKabupatenLayout.setEnabled(false);
        maperasKecamatanLayout.setEnabled(false);
        maperasDesaDinasLayout.setEnabled(false);
        maperasProvinsiLayout.setEnabled(false);
    }

    public void enableForm() {
        maperasPasanganLoadingLayout.setVisibility(View.GONE);
        maperasKabupatenLayout.setEnabled(true);
        maperasKecamatanLayout.setEnabled(true);
        maperasDesaDinasLayout.setEnabled(true);
        maperasProvinsiLayout.setEnabled(true);
    }
}