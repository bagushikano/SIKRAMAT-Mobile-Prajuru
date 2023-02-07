package com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.campurankeluar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.campuranmasuk.inputdatapradana.PerkawinanCampuranMasukDataLainnyaActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.satubanjar.PerkawinanSatuBanjarPurusaSelectActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.satubanjar.PerkawinanSatuBanjarStatusKekeluargaanActivity;
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
import com.bagushikano.sikedatmobileadmin.model.perkawinan.Perkawinan;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerkawinanCampuranKeluarPasanganActivity extends AppCompatActivity {

    private Button perkawinanNextButton;

    private Button perkawinanSelectPradanaButton;
    private Boolean isPradanaSelected = false, isPurusaSelected = false;

    private CacahKramaMipil pradanaKrama;

    ActivityResultLauncher<Intent> startActivityIntent;

    private TextView kramaPradanaName, kramaPradanaNik, noKramaMipilPradana;
    private Button kramaMipilPradanaDetailButton;
    private LinearLayout kramaPradanaImageLoadingContainer;
    private MaterialCardView kramaPradanaCard;
    private ImageView kramaPradanaImage;

    private final String KRAMA_SELECT_KEY = "KRAMA_SELECT_KEY";
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    private final String PRADANA_KEY = "PRADANA_SELECT_KEY";

    private final String PERKAWINAN_KEY = "PERKAWINAN_KEY";
    private final String DESA_DINAS_KEY = "DESA_DINAS_KEY";

    Gson gson = new Gson();


    private AutoCompleteTextView perkawinanKabupatenAutoComplete, perkawinanKecamatanAutoComplete,
            perkawinanDesaDinasAutoComplete, perkawinanProvinsiAutoComplete, perkawinanAgamaAsalAutoComplete;

    private TextInputEditText perkawinanAlamatAsalField;

    private TextInputLayout perkawinanKabupatenLayout, perkawinanKecamatanLayout,
            perkawinanDesaDinasLayout, perkawinanProvinsiLayout, perkawinanAlamatAsalLayout,
            perkawinanBuktiUpacaraSudhiWadhaniLayout, perkawinanAgamaAsalLayout, nikLayout, namaLayout, alamatLayout;

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

    private TextInputEditText nikPasanganField, namaPasanganField;

    private Perkawinan perkawinan = new Perkawinan();

    private Boolean isNamaValid = false, isNikValid = false, isAlamatValid = false, isAgamaValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perkawinan_campuran_keluar_pasangan);

        perkawinanPasanganLoadingLayout = findViewById(R.id.perkawinan_campuran_asal_progress_layout);

        nikPasanganField = findViewById(R.id.perkawinan_campuran_keluar_pasangan_nik_field);
        namaPasanganField = findViewById(R.id.perkawinan_campuran_keluar_pasangan_nama_field);
        namaLayout = findViewById(R.id.perkawinan_campuran_keluar_pasangan_nama_layout);
        nikLayout = findViewById(R.id.perkawinan_campuran_keluar_pasangan_nik_layout);

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

        nikPasanganField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (nikPasanganField.getText().toString().length() < 16) {
                    nikLayout.setError("NIK tidak boleh kurang dari 16 karakter");
                    isNikValid = false;
                }
                else if (nikPasanganField.getText().toString().length() > 16) {
                    nikLayout.setError("NIK tidak boleh lebih dari 16 karakter");
                    isNikValid = false;
                }
                else {
                    nikLayout.setError(null);
                    isNikValid = true;
                }
            }
        });

        namaPasanganField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(namaPasanganField.getText().toString().length() == 0) {
                    namaLayout.setError("Nama tidak boleh kosong");
                    namaLayout.setErrorEnabled(true);
                    isNamaValid = false;
                }
                else {
                    if (namaPasanganField.getText().toString().length() < 2) {
                        namaLayout.setError("Nama tidak boleh kurang dari 2 karakter");
                        namaLayout.setErrorEnabled(true);
                        isNamaValid = false;
                    }
                    else {
                        namaLayout.setError(null);
                        namaLayout.setErrorEnabled(false);
                        isNamaValid = true;
                    }
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


        kramaPradanaName = findViewById(R.id.krama_nama_text);
        kramaPradanaNik = findViewById(R.id.krama_nik_text);
        noKramaMipilPradana = findViewById(R.id.krama_no_mipil_text);
        kramaMipilPradanaDetailButton = findViewById(R.id.krama_pasangan_detail_button);
        kramaPradanaImageLoadingContainer = findViewById(R.id.krama_image_loading_container);
        kramaPradanaImage = findViewById(R.id.krama_image);
        kramaPradanaCard = findViewById(R.id.krama_pasangan_card);

        perkawinanNextButton = findViewById(R.id.perkawinan_pasangan_next_button);
        perkawinanNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNamaValid && isNikValid && isAlamatValid && isDesaDinasSelected && isAgamaValid)  {
                    Intent perkawinanNextIntent = new Intent(getApplicationContext(), PerkawinanCampuranKeluarDataPerkawinanActivity.class);
                    perkawinan.setNamaPasangan(namaPasanganField.getText().toString());
                    perkawinan.setNikPasangan(nikPasanganField.getText().toString());
                    perkawinan.setAgamaPasangan(perkawinanAgamaAsalAutoComplete.getText().toString());
                    perkawinan.setAlamatAsalPasangan(perkawinanAlamatAsalField.getText().toString());
                    perkawinan.setDesaAsalPasanganId(idDesaDinas);
                    perkawinan.setPradanaId(pradanaKrama.getId());

                    perkawinanNextIntent.putExtra(PERKAWINAN_KEY, gson.toJson(perkawinan));
                    perkawinanNextIntent.putExtra(DESA_DINAS_KEY, gson.toJson(desaDinas));
                    perkawinanNextIntent.putExtra(PRADANA_KEY, gson.toJson(pradanaKrama));
                    startActivityIntent.launch(perkawinanNextIntent);
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Terdapat data yang belum valid. Silahkan periksa kembali", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        perkawinanSelectPradanaButton = findViewById(R.id.perkawinan_pasangan_button);
        perkawinanSelectPradanaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent perkawinanPradanaIntent = new Intent(getApplicationContext(), PerkawinanCampuranKeluarPradanaSelectActivity.class);
                startActivityIntent.launch(perkawinanPradanaIntent);
            }
        });

        startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 100) {
                            // pradana
                            pradanaKrama = gson.fromJson(result.getData().getStringExtra(KRAMA_SELECT_KEY), CacahKramaMipil.class);
                            kramaPradanaName.setText(StringFormatter.formatNamaWithGelar(
                                    pradanaKrama.getPenduduk().getNama(),
                                    pradanaKrama.getPenduduk().getGelarDepan(),
                                    pradanaKrama.getPenduduk().getGelarBelakang()
                            ));
                            kramaPradanaNik.setText(pradanaKrama.getPenduduk().getNik());
                            noKramaMipilPradana.setText(pradanaKrama.getNomorCacahKramaMipil());
                            kramaMipilPradanaDetailButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                                    kramaDetail.putExtra(KRAMA_DETAIL_KEY, pradanaKrama.getId());
                                    startActivity(kramaDetail);
                                }
                            });
                            if (pradanaKrama.getPenduduk().getFoto() != null) {
                                kramaPradanaImageLoadingContainer.setVisibility(View.VISIBLE);
                                Picasso.get()
                                        .load(getResources().getString(R.string.image_endpoint) +
                                                pradanaKrama.getPenduduk().getFoto())
                                        .fit().centerCrop()
                                        .into(kramaPradanaImage, new com.squareup.picasso.Callback() {
                                            @Override
                                            public void onSuccess() {
                                                kramaPradanaImageLoadingContainer.setVisibility(View.GONE);
                                                kramaPradanaImage.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                kramaPradanaImageLoadingContainer.setVisibility(View.GONE);
                                                Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaPradanaImage);
                                            }
                                        });
                            }
                            else {
                                Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaPradanaImage);
                                kramaPradanaImage.setVisibility(View.VISIBLE);
                            }
                            kramaPradanaCard.setVisibility(View.VISIBLE);
                            isPradanaSelected = true;
                            perkawinanSelectPradanaButton.setText("Ganti Cacah Krama");
                            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                    "Cacah Krama berhasil dipilih.", Snackbar.LENGTH_SHORT).show();

                        } else if (result.getResultCode() == 1) {
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