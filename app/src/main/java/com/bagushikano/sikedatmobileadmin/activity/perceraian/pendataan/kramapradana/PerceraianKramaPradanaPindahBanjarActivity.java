package com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan.kramapradana;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan.PerceraianDataAnggotaKeluargaActivity;
import com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan.PerceraianDataPerceraianActivity;
import com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan.kramaselect.PerceraianSelectKramaTujuanActivity;
import com.bagushikano.sikedatmobileadmin.adapter.master.BanjarAdatSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.DesaAdatSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.KabupatenSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.KecamatanSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.bagushikano.sikedatmobileadmin.model.master.BanjarAdatGetNoPaginationResponse;
import com.bagushikano.sikedatmobileadmin.model.master.DesaAdat;
import com.bagushikano.sikedatmobileadmin.model.master.DesaAdatGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Kabupaten;
import com.bagushikano.sikedatmobileadmin.model.master.KabupatenGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Kecamatan;
import com.bagushikano.sikedatmobileadmin.model.master.KecamatanGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarAdat;
import com.bagushikano.sikedatmobileadmin.model.perceraian.Perceraian;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerceraianKramaPradanaPindahBanjarActivity extends AppCompatActivity {

    TextView kramaNamaText, pasanganNamaText, kramaKedudukanText, pasanganKedudukanText;
    Button kramaSelectButton, peceraianNextButton;

    ActivityResultLauncher<Intent> startActivityIntent;
    SharedPreferences loginPreferences;

    //di pake untuk krama mipilnya
    private final String KRAMA_MIPIL_SELECT_KEY = "KRAMA_MIPIL_SELECT_KEY";
    private final String KRAMA_MIPIL_DETAIL_KEY = "KRAMA_MIPIL_DETAIL_KEY";
    Gson gson = new Gson();

    KramaMipil kramaMipilSelected;
    KramaMipil kramaBaru;
    AnggotaKramaMipil pasanganKrama;
    ArrayList<AnggotaKramaMipil> anggotaKramaMipilArrayList = new ArrayList<>();
    Perceraian perceraian = new Perceraian();

    private final String KRAMA_CERAI_KEY = "KRAMA_CERAI_KEY";
    private final String PASANGAN_CERAI_KEY = "PASANGAN_CERAI_KEY";
    private final String ANGGOTA_CERAI_KEY = "ANGGOTA_CERAI_KEY";
    private final String PERCERAIAN_KEY = "PERCERAIAN_KEY";

    private RadioGroup perceraianKramaMipilPurusaStatusKeluargaRadioGroup;
    private RadioButton perceraianKramaMipilPurusaStayRadio, perceraiankramaMipilPurusaPindahKramaRadio;
    private LinearLayout perceraianKramaPindahKramaLayout;
    private TextInputLayout perceraianKramaStatusHubungan;
    private AutoCompleteTextView perceraianKramaStatusHubunganField;
    private Button perceraianSelectNewKramaButton;
    private TextView kramaBaruNameText;

    private AutoCompleteTextView maperasKabupatenAutoComplete, maperasKecamatanAutoComplete,
            maperasDesaAdatAutoComplete, maperasBanjarAdatAutoComplete;

    private TextInputLayout maperasKabupatenLayout, maperasKecamatanLayout,
            maperasDesaAdatLayout, maperasBanjarAdatLayout;

    private ArrayList<Kabupaten> kabupatens = new ArrayList<>();
    private ArrayList<Kecamatan> kecamatans = new ArrayList<>();
    private ArrayList<DesaAdat> desaAdats = new ArrayList<>();
    private ArrayList<BanjarAdat> banjarAdats = new ArrayList<>();

    private KabupatenSelectionAdapter kabupatenSelectionAdapter;
    private KecamatanSelectionAdapter kecamatanSelectionAdapter;
    private DesaAdatSelectionAdapter desaAdatSelectionAdapter;
    private BanjarAdatSelectionAdapter banjarAdatSelectionAdapter;

    private Integer idBanjarAdat = null, idKabupaten = null, idKecamatan = null, idDesaAdat = null;
    private Boolean isBanjarAdatSelected = false;
    private Kabupaten kabupaten = null;
    private Kecamatan kecamatan = null;
    private DesaAdat desaAdat = null;
    private BanjarAdat banjarAdat = null;
    private LinearLayout maperasPasanganLoadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perceraian_krama_pradana_pindah_banjar);

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

        maperasPasanganLoadingLayout = findViewById(R.id.maperas_beda_banjar_progress_layout);

        maperasBanjarAdatAutoComplete = findViewById(R.id.maperas_banjar_adat_field);
        maperasKabupatenAutoComplete = findViewById(R.id.maperas_kabupaten_field);
        maperasKecamatanAutoComplete = findViewById(R.id.maperas_kecamatan_field);
        maperasDesaAdatAutoComplete = findViewById(R.id.maperas_desa_adat_field);

        maperasKabupatenLayout = findViewById(R.id.maperas_kabupaten_form);
        maperasKecamatanLayout = findViewById(R.id.maperas_kecamatan_form);
        maperasDesaAdatLayout = findViewById(R.id.maperas_desa_adat_form);
        maperasBanjarAdatLayout = findViewById(R.id.maperas_banjar_adat_form);

        kabupatenSelectionAdapter = new KabupatenSelectionAdapter(this, kabupatens);
        maperasKabupatenAutoComplete.setAdapter(kabupatenSelectionAdapter);
//        maperasKabupatenAutoComplete.setThreshold(100);
        maperasKabupatenAutoComplete.setInputType(0);
        maperasKabupatenAutoComplete.setKeyListener(null);

        maperasKabupatenAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                kabupaten = (Kabupaten) adapterView.getItemAtPosition(i);
                maperasKabupatenAutoComplete.setText(kabupaten.getName());
                idKabupaten = kabupaten.getId();

                kecamatan = null;
                desaAdat = null;
                banjarAdat = null;

                idBanjarAdat = null;
                idKecamatan = null;
                idDesaAdat = null;

                isBanjarAdatSelected = false;

                maperasKecamatanLayout.setVisibility(View.VISIBLE);
                maperasDesaAdatLayout.setVisibility(View.GONE);
                maperasBanjarAdatLayout.setVisibility(View.GONE);

                maperasKecamatanAutoComplete.setText(null);
                maperasDesaAdatAutoComplete.setText(null);
                maperasBanjarAdatAutoComplete.setText(null);

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

                desaAdat = null;
                banjarAdat = null;

                idDesaAdat = null;
                idBanjarAdat = null;

                isBanjarAdatSelected = false;

                maperasKecamatanLayout.setVisibility(View.VISIBLE);
                maperasDesaAdatLayout.setVisibility(View.VISIBLE);
                maperasBanjarAdatLayout.setVisibility(View.GONE);


                maperasDesaAdatAutoComplete.setText(null);
                maperasBanjarAdatAutoComplete.setText(null);

                getDesaAdat();
            }
        });

        desaAdatSelectionAdapter = new DesaAdatSelectionAdapter(this, desaAdats);
        maperasDesaAdatAutoComplete.setAdapter(desaAdatSelectionAdapter);
        maperasDesaAdatAutoComplete.setThreshold(100);
        maperasDesaAdatAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                desaAdat = (DesaAdat) adapterView.getItemAtPosition(i);
                maperasDesaAdatAutoComplete.setText(desaAdat.getDesadatNama());
                idDesaAdat = desaAdat.getId();

                banjarAdat = null;
                idBanjarAdat = null;

                isBanjarAdatSelected = false;

                maperasKecamatanLayout.setVisibility(View.VISIBLE);
                maperasDesaAdatLayout.setVisibility(View.VISIBLE);
                maperasBanjarAdatLayout.setVisibility(View.VISIBLE);

                maperasBanjarAdatAutoComplete.setText(null);

                getBanjarAdat();
            }
        });

        banjarAdatSelectionAdapter = new BanjarAdatSelectionAdapter(this, banjarAdats);
        maperasBanjarAdatAutoComplete.setAdapter(banjarAdatSelectionAdapter);
        maperasBanjarAdatAutoComplete.setThreshold(100);
        maperasBanjarAdatAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                banjarAdat = (BanjarAdat) adapterView.getItemAtPosition(i);
                maperasBanjarAdatAutoComplete.setText(banjarAdat.getNamaBanjarAdat());
                idBanjarAdat = banjarAdat.getId();

                isBanjarAdatSelected = true;
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "Berhasil memilih Banjar Adat Asal Anak. Silahkan pilih Krama/Keluarga Anak.", Snackbar.LENGTH_SHORT).show();
            }
        });

        perceraianKramaStatusHubungan = findViewById(R.id.perceraian_krama_mipil_new_krama_status_hubungan_layout);
        perceraianKramaStatusHubunganField = findViewById(R.id.perceraian_krama_mipil_new_krama_status_hubungan_field);
        perceraianSelectNewKramaButton = findViewById(R.id.perceraian_krama_mipil_select_new_krama);
        kramaBaruNameText = findViewById(R.id.krama_tujuan_nama_text);


        String[] jkAnak = new String[] {"Anak", "Cucu", "Ayah", "Ibu", "Famili Lain"};
        ArrayAdapter adapterJk = new ArrayAdapter<>(this, R.layout.dropdown_menu_item, jkAnak);
        perceraianKramaStatusHubunganField.setAdapter(adapterJk);
        perceraianKramaStatusHubunganField.setInputType(0);
        perceraianKramaStatusHubunganField.setKeyListener(null);

        perceraianKramaStatusHubunganField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Anak")) {
                    perceraian.setStatusHubunganBaruPradana("anak");
                } else if (adapterView.getItemAtPosition(i).equals("Cucu")) {
                    perceraian.setStatusHubunganBaruPradana("cucu");
                } else if (adapterView.getItemAtPosition(i).equals("Ayah")) {
                    perceraian.setStatusHubunganBaruPradana("ayah");
                } else if (adapterView.getItemAtPosition(i).equals("Ibu")) {
                    perceraian.setStatusHubunganBaruPradana("ibu");
                } else if (adapterView.getItemAtPosition(i).equals("Famili Lain")) {
                    perceraian.setStatusHubunganBaruPradana("famili_lain");
                }
            }
        });


        peceraianNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // arahin ke data anggota langsung
                Intent perceraianNext = null;
                perceraianNext = new Intent(getApplicationContext(), PerceraianKramaPradanaPasanganActivity.class);
                if (anggotaKramaMipilArrayList.size() != 0) {
                    perceraianNext.putExtra( ANGGOTA_CERAI_KEY, gson.toJson(anggotaKramaMipilArrayList));
                }
                perceraianNext.putExtra( KRAMA_CERAI_KEY ,gson.toJson(kramaMipilSelected));
                perceraianNext.putExtra( PASANGAN_CERAI_KEY, gson.toJson(pasanganKrama));
                perceraianNext.putExtra( PERCERAIAN_KEY, gson.toJson(perceraian));
                startActivityIntent.launch(perceraianNext);
            }
        });

        perceraianSelectNewKramaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pass KRAMA untuk idkrama, pass BANJAR untuk id bajnar
                Intent maperasSelectKramaIntent = new Intent(getApplicationContext() , PerceraianSelectKramaTujuanActivity.class);
                maperasSelectKramaIntent.putExtra("KRAMA", kramaMipilSelected.getId());
                maperasSelectKramaIntent.putExtra("BANJAR", idBanjarAdat);
                maperasSelectKramaIntent.putExtra("PASANGAN", (Integer) perceraian.getKramaMipilBaruPurusaId());
                startActivityIntent.launch(maperasSelectKramaIntent);
            }
        });

        getKabupaten();

        startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 200) {
                            kramaBaru = gson.fromJson(result.getData().getStringExtra(KRAMA_MIPIL_SELECT_KEY), KramaMipil.class);
                            kramaBaruNameText.setText(
                                    StringFormatter.formatNamaWithGelar(
                                            kramaBaru.getCacahKramaMipil().getPenduduk().getNama(),
                                            kramaBaru.getCacahKramaMipil().getPenduduk().getGelarDepan(),
                                            kramaBaru.getCacahKramaMipil().getPenduduk().getGelarBelakang()
                                    )
                            );
                            perceraian.setKramaMipilBaruPradanaId(kramaBaru.getId());
                            perceraian.setBanjarAdatPradanaId(idBanjarAdat);
                        } else if (result.getResultCode() == 1) {
                            setResult(1);
                            finish();
                        }
                    }
                });
    }

    public void getKabupaten() {
        disableForm();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KabupatenGetResponse> kabupatenGetResponseCall = getData.getMasterKabupaten();
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

    public void getDesaAdat() {
        disableForm();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<DesaAdatGetResponse> desaAdatGetResponseCall = getData.getMasterDesaAdat(String.valueOf(idKecamatan));
        desaAdatGetResponseCall.enqueue(new Callback<DesaAdatGetResponse>() {
            @Override
            public void onResponse(Call<DesaAdatGetResponse> call, Response<DesaAdatGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Berhasil memuat data Desa Adat. Silahkan pilih Desa Adat.", Snackbar.LENGTH_LONG).show();
                    desaAdats.clear();
                    desaAdats.addAll(response.body().getData());
                    desaAdatSelectionAdapter.notifyDataSetChanged();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Gagal memuat data Desa Adat. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
                }
                enableForm();
            }

            @Override
            public void onFailure(Call<DesaAdatGetResponse> call, Throwable t) {
                enableForm();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "Gagal memuat data Desa Adat. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void getBanjarAdat() {
        disableForm();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<BanjarAdatGetNoPaginationResponse> banjarAdatGetNoPaginationResponseCall
                = getData.getMasterBanjarAdat(String.valueOf(idDesaAdat));
        banjarAdatGetNoPaginationResponseCall.enqueue(new Callback<BanjarAdatGetNoPaginationResponse>() {
            @Override
            public void onResponse(Call<BanjarAdatGetNoPaginationResponse> call, Response<BanjarAdatGetNoPaginationResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Berhasil memuat data Banjar Adat. Silahkan pilih Banjar Adat.", Snackbar.LENGTH_LONG).show();
                    banjarAdats.clear();
                    banjarAdats.addAll(response.body().getData());
                    banjarAdatSelectionAdapter.notifyDataSetChanged();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Gagal memuat data Banjar Adat. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
                }
                enableForm();
            }

            @Override
            public void onFailure(Call<BanjarAdatGetNoPaginationResponse> call, Throwable t) {
                enableForm();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "Gagal memuat data Banjar Adat. Silahkan coba lagi nanti.", Snackbar.LENGTH_LONG).show();
            }
        });
    }


    public void disableForm() {
        maperasPasanganLoadingLayout.setVisibility(View.VISIBLE);
        maperasKabupatenLayout.setEnabled(false);
        maperasKecamatanLayout.setEnabled(false);
        maperasDesaAdatLayout.setEnabled(false);
        maperasBanjarAdatLayout.setEnabled(false);
    }

    public void enableForm() {
        maperasPasanganLoadingLayout.setVisibility(View.GONE);
        maperasKabupatenLayout.setEnabled(true);
        maperasKecamatanLayout.setEnabled(true);
        maperasDesaAdatLayout.setEnabled(true);
        maperasBanjarAdatLayout.setEnabled(true);
    }
}