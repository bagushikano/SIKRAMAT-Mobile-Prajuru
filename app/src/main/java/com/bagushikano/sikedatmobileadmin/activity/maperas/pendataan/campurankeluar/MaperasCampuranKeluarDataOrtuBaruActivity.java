package com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.campurankeluar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.adapter.master.DesaDinasSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.KabupatenSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.KecamatanSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.ProvinsiSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.maperas.Maperas;
import com.bagushikano.sikedatmobileadmin.model.master.DesaDinas;
import com.bagushikano.sikedatmobileadmin.model.master.DesaDinasGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Kabupaten;
import com.bagushikano.sikedatmobileadmin.model.master.KabupatenGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Kecamatan;
import com.bagushikano.sikedatmobileadmin.model.master.KecamatanGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Provinsi;
import com.bagushikano.sikedatmobileadmin.model.master.ProvinsiGetResponse;
import com.bagushikano.sikedatmobileadmin.util.ChangeDateTimeFormat;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaperasCampuranKeluarDataOrtuBaruActivity extends AppCompatActivity {

    //data ortu
    private TextInputEditText perkawinanPradanaNamaAyahField, perkawinanPradanaNikAyahField,
            perkawinanPradanaNamaIbuField, perkawinanPradanaNikIbuField;

    private TextInputLayout perkawinanPradanaNamaAyahLayout, perkawinanPradanaNikAyahLayout,
            perkawinanPradanaNamaIbuLayout, perkawinanPradanaNikIbuLayout;
    private Button maperasNextButton;

    private Boolean isNamaAyahValid = false, isNikAyahValid = false, isNamaIbuValid = false,
            isNikIbuValid = false, isAlamatValid = false, isAgamaValid;

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

    private LinearLayout perkawinanPasanganLoadingLayout;

    private AutoCompleteTextView perkawinanKabupatenAutoComplete, perkawinanKecamatanAutoComplete,
            perkawinanDesaDinasAutoComplete, perkawinanProvinsiAutoComplete, perkawinanAgamaAsalAutoComplete;

    private TextInputEditText perkawinanAlamatAsalField;

    private TextInputLayout perkawinanKabupatenLayout, perkawinanKecamatanLayout,
            perkawinanDesaDinasLayout, perkawinanProvinsiLayout, perkawinanAlamatAsalLayout,
            perkawinanBuktiUpacaraSudhiWadhaniLayout, perkawinanAgamaAsalLayout;

    private Maperas maperas = new Maperas();


    // di pake tukar data antar activity nya
    private final String ANAK_KEY = "ANAK_KEY";
    private final String KRAMA_LAMA_KEY = "KRAMA_LAMA_KEY";
    private final String MAPERAS_KEY = "MAPERAS_KEY";

    Gson gson = new Gson();

    SharedPreferences loginPreferences;
    ActivityResultLauncher<Intent> startActivityIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maperas_campuran_keluar_data_ortu_baru);

        perkawinanPasanganLoadingLayout = findViewById(R.id.perkawinan_campuran_asal_progress_layout);
        perkawinanAlamatAsalField = findViewById(R.id.perkawinan_campuran_keluar_pasangan_alamat_asal_field);
        perkawinanAgamaAsalAutoComplete = findViewById(R.id.perkawinan_campuran_keluar_pasangan_agama_field);

        perkawinanAlamatAsalLayout = findViewById(R.id.perkawinan_campuran_keluar_pasangan_alamat_asal_layout);
        perkawinanAgamaAsalLayout = findViewById(R.id.perkawinan_campuran_keluar_pasangan_agama_layout);

        String[] agamaArray = new String[] {"Islam", "Protestan", "Katolik", "Hindu", "Buddha", "Khonghucu"};
        ArrayAdapter adapterAgama = new ArrayAdapter<>(this, R.layout.dropdown_menu_item, agamaArray);
        perkawinanAgamaAsalAutoComplete.setAdapter(adapterAgama);
        perkawinanAgamaAsalAutoComplete.setInputType(0);
        perkawinanAgamaAsalAutoComplete.setKeyListener(null);

        perkawinanAgamaAsalAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isAgamaValid = true;
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
                    isAlamatValid = false;
                }
                else {
                    perkawinanAlamatAsalLayout.setError(null);
                    perkawinanAlamatAsalLayout.setErrorEnabled(false);
                    isAlamatValid = true;
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

        getProvinsi();

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
                    isNikAyahValid = false;
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
                    isNamaAyahValid = false;
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
                    isNikIbuValid = false;
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
                    isNamaIbuValid = false;
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

        maperasNextButton = findViewById(R.id.maperas_next_button);
        maperasNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDesaDinasSelected && isAlamatValid && isAgamaValid
                        && isNikIbuValid && isNikAyahValid && isNamaIbuValid && isNamaAyahValid) {
                    Intent maperasNextIntent = new Intent(getApplicationContext(), MaperasCampuranKeluarDataMaperasActivity.class);

                    maperas.setNamaAyahBaru(perkawinanPradanaNamaAyahField.getText().toString());
                    maperas.setNikAyahBaru(perkawinanPradanaNikAyahField.getText().toString());
                    maperas.setNamaIbuBaru(perkawinanPradanaNamaIbuField.getText().toString());
                    maperas.setNikIbuBaru(perkawinanPradanaNamaIbuField.getText().toString());
                    maperas.setDesaAsalId(idDesaDinas);
                    maperas.setAgamaBaru(perkawinanAgamaAsalAutoComplete.getText().toString());
                    maperas.setAlamatAsal(perkawinanAlamatAsalField.getText().toString());
                    maperas.setJenisMaperas("campuran_keluar");

                    maperasNextIntent.putExtra("BANJAR_KEY", gson.toJson(desaDinas));
                    maperasNextIntent.putExtra(MAPERAS_KEY, gson.toJson(maperas));
                    maperasNextIntent.putExtra(ANAK_KEY, getIntent().getStringExtra(ANAK_KEY));
                    maperasNextIntent.putExtra(KRAMA_LAMA_KEY, getIntent().getStringExtra(KRAMA_LAMA_KEY));
                    startActivityIntent.launch(maperasNextIntent);
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Periksa data kembali.", Snackbar.LENGTH_SHORT).show();
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