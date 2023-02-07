package com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan.kramapurusa;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan.PerceraianDataAnggotaKeluargaActivity;
import com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan.PerceraianDataPerceraianActivity;
import com.bagushikano.sikedatmobileadmin.adapter.master.DesaDinasSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.KabupatenSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.KecamatanSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.ProvinsiSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.bagushikano.sikedatmobileadmin.model.master.DesaDinas;
import com.bagushikano.sikedatmobileadmin.model.master.DesaDinasGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Kabupaten;
import com.bagushikano.sikedatmobileadmin.model.master.KabupatenGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Kecamatan;
import com.bagushikano.sikedatmobileadmin.model.master.KecamatanGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Provinsi;
import com.bagushikano.sikedatmobileadmin.model.master.ProvinsiGetResponse;
import com.bagushikano.sikedatmobileadmin.model.perceraian.Perceraian;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerceraianKramaPurusaPasanganKeluarBaliActivity extends AppCompatActivity {

    private KabupatenSelectionAdapter kabupatenSelectionAdapter;
    private KecamatanSelectionAdapter kecamatanSelectionAdapter;
    private DesaDinasSelectionAdapter desaDinasSelectionAdapter;
    private ProvinsiSelectionAdapter provinsiSelectionAdapter;

    KramaMipil kramaMipilSelected;
    KramaMipil kramaBaru;
    AnggotaKramaMipil pasanganKrama;
    ArrayList<AnggotaKramaMipil> anggotaKramaMipilArrayList = new ArrayList<>();
    Perceraian perceraian;

    private final String KRAMA_CERAI_KEY = "KRAMA_CERAI_KEY";
    private final String PASANGAN_CERAI_KEY = "PASANGAN_CERAI_KEY";
    private final String ANGGOTA_CERAI_KEY = "ANGGOTA_CERAI_KEY";
    private final String PERCERAIAN_KEY = "PERCERAIAN_KEY";

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
            perkawinanDesaDinasAutoComplete, perkawinanProvinsiAutoComplete;


    private TextInputLayout perkawinanKabupatenLayout, perkawinanKecamatanLayout,
            perkawinanDesaDinasLayout, perkawinanProvinsiLayout;

    Gson gson = new Gson();

    SharedPreferences loginPreferences;
    ActivityResultLauncher<Intent> startActivityIntent;

    Button peceraianNextButton;
    TextView kramaNamaText, pasanganNamaText, kramaKedudukanText, pasanganKedudukanText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perceraian_krama_purusa_pasangan_keluar_bali);

        perkawinanPasanganLoadingLayout = findViewById(R.id.perkawinan_campuran_asal_progress_layout);

        kramaNamaText = findViewById(R.id.perceraian_krama_mipil_nama_text);
        kramaKedudukanText = findViewById(R.id.perceraian_krama_mipil_kedudukan_text);
        peceraianNextButton = findViewById(R.id.perceraian_next_button);

        kramaMipilSelected = gson.fromJson(getIntent().getStringExtra(KRAMA_CERAI_KEY), KramaMipil.class);
        pasanganKrama = gson.fromJson(getIntent().getStringExtra(PASANGAN_CERAI_KEY), AnggotaKramaMipil.class);
        if (getIntent().hasExtra(ANGGOTA_CERAI_KEY)) {
            anggotaKramaMipilArrayList.addAll(
                    gson.fromJson(getIntent().getStringExtra(ANGGOTA_CERAI_KEY),
                            new TypeToken<ArrayList<AnggotaKramaMipil>>(){}.getType()
                    ));
        }
        perceraian = gson.fromJson(getIntent().getStringExtra(PERCERAIAN_KEY), Perceraian.class);

        kramaNamaText.setText(StringFormatter.formatNamaWithGelar(
                pasanganKrama.getCacahKramaMipil().getPenduduk().getNama(),
                pasanganKrama.getCacahKramaMipil().getPenduduk().getGelarDepan(),
                pasanganKrama.getCacahKramaMipil().getPenduduk().getGelarBelakang()
        ));
        kramaKedudukanText.setText("Pradana");

        peceraianNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDesaDinasSelected) {
                    // arahin ke data anggota langsung
                    Intent perceraianNext = null;
                    if (anggotaKramaMipilArrayList.size() != 0) {
                        perceraianNext = new Intent(getApplicationContext(), PerceraianDataAnggotaKeluargaActivity.class);
                        perceraianNext.putExtra( ANGGOTA_CERAI_KEY, gson.toJson(anggotaKramaMipilArrayList));
                    } else {
                        perceraianNext = new Intent(getApplicationContext(), PerceraianDataPerceraianActivity.class);
                    }
                    perceraianNext.putExtra( KRAMA_CERAI_KEY ,gson.toJson(kramaMipilSelected));
                    perceraianNext.putExtra( PASANGAN_CERAI_KEY, gson.toJson(pasanganKrama));
                    perceraianNext.putExtra( PERCERAIAN_KEY, gson.toJson(perceraian));
                    startActivityIntent.launch(perceraianNext);
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Periksa data kembali.", Snackbar.LENGTH_SHORT).show();
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
                perceraian.setDesaBaruPradanaId(idDesaDinas);
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