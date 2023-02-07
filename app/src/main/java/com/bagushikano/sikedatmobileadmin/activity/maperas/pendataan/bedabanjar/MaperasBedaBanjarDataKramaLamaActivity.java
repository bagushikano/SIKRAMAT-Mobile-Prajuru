package com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.bedabanjar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.krama.KramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.adapter.master.BanjarAdatSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.DesaAdatSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.KabupatenSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.KecamatanSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipilDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.bagushikano.sikedatmobileadmin.model.maperas.Maperas;
import com.bagushikano.sikedatmobileadmin.model.master.BanjarAdatGetNoPaginationResponse;
import com.bagushikano.sikedatmobileadmin.model.master.DesaAdat;
import com.bagushikano.sikedatmobileadmin.model.master.DesaAdatGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Kabupaten;
import com.bagushikano.sikedatmobileadmin.model.master.KabupatenGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Kecamatan;
import com.bagushikano.sikedatmobileadmin.model.master.KecamatanGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarAdat;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.Perkawinan;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaperasBedaBanjarDataKramaLamaActivity extends AppCompatActivity {

    private Button maperasNextButton;

    private Button maperasSelectKramaButton, maperasSelectAnakButton;
    private Boolean isKramaSelected = false, isAnakSelected = false;

    private CacahKramaMipil anakMaperas;
    private KramaMipil kramaMipilLama;
    private Maperas maperas;

    ActivityResultLauncher<Intent> startActivityIntent;

    private MaterialCardView kramaMipilCard;
    private TextView kramaMipilName, kramaMipilNo, kramaMipilBanjarAdat;
    private Button kramaMipilDetailButton;

    private TextView kramaAnakName, kramaAnakNik, noKramaMipilAnak;
    private Button kramaMipilAnakDetailButton;
    private LinearLayout kramaAnakImageLoadingContainer;
    private MaterialCardView kramaAnakCard;
    private ImageView kramaAnakImage;

    private TextInputEditText ayahLamaField, ibuLamaField;

    // di pake untuk anak
    private final String KRAMA_SELECT_KEY = "KRAMA_SELECT_KEY";
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    //di pake untuk krama mipilnya
    private final String KRAMA_MIPIL_SELECT_KEY = "KRAMA_MIPIL_SELECT_KEY";
    private final String KRAMA_MIPIL_DETAIL_KEY = "KRAMA_MIPIL_DETAIL_KEY";

    // di pake tukar data antar activity nya
    private final String ANAK_KEY = "ANAK_KEY";
    private final String KRAMA_LAMA_KEY = "KRAMA_LAMA_KEY";

    Gson gson = new Gson();

    SharedPreferences loginPreferences;

    private LinearLayout maperasPasanganLoadingLayout;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maperas_beda_banjar_data_krama_lama);

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

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

        kramaMipilName = findViewById(R.id.krama_nama_text);
        kramaMipilNo = findViewById(R.id.krama_no_mipil_text);
        kramaMipilBanjarAdat = findViewById(R.id.krama_banjar_adat_text);
        kramaMipilDetailButton = findViewById(R.id.krama_detail_button);
        kramaMipilCard = findViewById(R.id.krama_mipil_lama_card);

        ayahLamaField = findViewById(R.id.maperas_ayah_lama_field);
        ibuLamaField = findViewById(R.id.maperas_ibu_lama_field);

        kramaAnakName = findViewById(R.id.anak_maperas_nama_text);
        kramaAnakNik = findViewById(R.id.anak_maperas_nik_text);
        noKramaMipilAnak = findViewById(R.id.anak_maperas_no_mipil_text);
        kramaMipilAnakDetailButton = findViewById(R.id.anak_maperas_detail_button);
        kramaAnakImageLoadingContainer = findViewById(R.id.anak_maperas_image_loading_container);
        kramaAnakImage = findViewById(R.id.anak_maperas_image);
        kramaAnakCard = findViewById(R.id.anak_maperas_card);

        maperasNextButton = findViewById(R.id.maperas_krama_lama_next_button);
        maperasNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAnakSelected && isKramaSelected) {
                    Intent maperasNextIntent = new Intent(getApplicationContext(), MaperasBedaBanjarDataKramaBaruActivity.class);
                    maperasNextIntent.putExtra(ANAK_KEY, gson.toJson(anakMaperas));
                    maperasNextIntent.putExtra(KRAMA_LAMA_KEY, gson.toJson(kramaMipilLama));
                    if (getIntent().hasExtra("EDIT_MAPERAS")) {
                        maperasNextIntent.putExtra("EDIT_MAPERAS", gson.toJson(maperas));
                    }
                    startActivityIntent.launch(maperasNextIntent);
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Periksa data kembali.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        maperasSelectKramaButton = findViewById(R.id.maperas_krama_mipil_lama_button);
        maperasSelectAnakButton = findViewById(R.id.maperas_anak_button);
        maperasSelectAnakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isKramaSelected) {
                    Intent maperasAnakIntent = new Intent(getApplicationContext() , MaperasBedaBanjarAnakSelectActivity.class);
                    maperasAnakIntent.putExtra("KRAMA_ID", kramaMipilLama.getId());
                    startActivityIntent.launch(maperasAnakIntent);
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Pilih Krama Mipil Lama terebih dahulu", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        maperasSelectKramaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent maperasSelectKramaIntent = new Intent(getApplicationContext() , MaperasBedaBanjarKramaMipilLamaSelectActivity.class);
                if (isBanjarAdatSelected) {
                    maperasSelectKramaIntent.putExtra("BANJAR_KEY", idBanjarAdat);
                    startActivityIntent.launch(maperasSelectKramaIntent);
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Pilih asal Anak terebih dahulu", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 200) {
                            // krama mipil
                            kramaMipilLama = gson.fromJson(result.getData().getStringExtra(KRAMA_MIPIL_SELECT_KEY), KramaMipil.class);
                            String namaFormated = kramaMipilLama.getCacahKramaMipil().getPenduduk().getNama();
                            if (kramaMipilLama.getCacahKramaMipil().getPenduduk().getGelarDepan() != null) {
                                namaFormated = String.format("%s %s",
                                        kramaMipilLama.getCacahKramaMipil().getPenduduk().getGelarDepan(),
                                        kramaMipilLama.getCacahKramaMipil().getPenduduk().getNama()
                                );
                            }
                            if (kramaMipilLama.getCacahKramaMipil().getPenduduk().getGelarBelakang() != null) {
                                namaFormated = String.format("%s %s",
                                        namaFormated,
                                        kramaMipilLama.getCacahKramaMipil().getPenduduk().getGelarBelakang()
                                );
                            }
                            kramaMipilName.setText(namaFormated);
                            kramaMipilNo.setText(kramaMipilLama.getNomorKramaMipil());
                            kramaMipilBanjarAdat.setText(kramaMipilLama.getBanjarAdat().getNamaBanjarAdat());
                            kramaMipilDetailButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent kramaDetail = new Intent(getApplicationContext(), KramaMipilDetailActivity.class);
                                    kramaDetail.putExtra(KRAMA_MIPIL_DETAIL_KEY, kramaMipilLama.getId());
                                    startActivity(kramaDetail);
                                }
                            });
                            kramaMipilCard.setVisibility(View.VISIBLE);
                            isKramaSelected = true;
                            maperasSelectKramaButton.setText("Ganti Krama Mipil");
                            maperasSelectAnakButton.setText("Pilih Anak");

                            if (isAnakSelected) {
                                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                        "Krama Mipil telah diganti. Silahkan pilih Anak.", Snackbar.LENGTH_SHORT).show();
                                isAnakSelected = false;
                                kramaAnakCard.setVisibility(View.GONE);
                                anakMaperas = null;
                            } else {
                                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                        "Krama Mipil berhasil dipilih. Silahkan pilih Anak.", Snackbar.LENGTH_SHORT).show();
                            }
                        } else if (result.getResultCode() == 100) {
                            // anak
                            anakMaperas = gson.fromJson(result.getData().getStringExtra(KRAMA_SELECT_KEY), CacahKramaMipil.class);
                            String namaFormated = anakMaperas.getPenduduk().getNama();
                            if (anakMaperas.getPenduduk().getGelarDepan() != null) {
                                namaFormated = String.format("%s %s",
                                        anakMaperas.getPenduduk().getGelarDepan(),
                                        anakMaperas.getPenduduk().getNama()
                                );
                            }

                            if (anakMaperas.getPenduduk().getGelarBelakang() != null) {
                                namaFormated = String.format("%s %s",
                                        namaFormated,
                                        anakMaperas.getPenduduk().getGelarBelakang()
                                );
                            }
                            kramaAnakName.setText(namaFormated);
                            kramaAnakNik.setText(anakMaperas.getPenduduk().getNik());
                            noKramaMipilAnak.setText(anakMaperas.getNomorCacahKramaMipil());
                            kramaMipilAnakDetailButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                                    kramaDetail.putExtra(KRAMA_DETAIL_KEY, anakMaperas.getId());
                                    startActivity(kramaDetail);
                                }
                            });
                            if (anakMaperas.getPenduduk().getFoto() != null) {
                                kramaAnakImageLoadingContainer.setVisibility(View.VISIBLE);
                                Picasso.get()
                                        .load(getResources().getString(R.string.image_endpoint) +
                                                anakMaperas.getPenduduk().getFoto())
                                        .fit().centerCrop()
                                        .into(kramaAnakImage, new com.squareup.picasso.Callback() {
                                            @Override
                                            public void onSuccess() {
                                                kramaAnakImageLoadingContainer.setVisibility(View.GONE);
                                                kramaAnakImage.setVisibility(View.VISIBLE);
                                            }
                                            @Override
                                            public void onError(Exception e) {
                                                kramaAnakImageLoadingContainer.setVisibility(View.GONE);
                                                Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaAnakImage);
                                            }
                                        });
                            }
                            else {
                                Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaAnakImage);
                                kramaAnakImage.setVisibility(View.VISIBLE);
                            }
                            getOrtuLama(loginPreferences.getString("token", "empty"), anakMaperas.getId());
                            kramaAnakCard.setVisibility(View.VISIBLE);
                            isAnakSelected = true;
                            maperasSelectAnakButton.setText("Ganti Anak");
                            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                    "Anak berhasil dipilih.", Snackbar.LENGTH_SHORT).show();

                        } else if (result.getResultCode() == 1) {
                            setResult(1);
                            finish();
                        }
                    }
                });
        getKabupaten();
        if (getIntent().hasExtra("EDIT_MAPERAS")) {
            maperas = gson.fromJson(getIntent().getStringExtra("EDIT_MAPERAS"), Maperas.class);
            setEdit();
        }
    }

    public void getOrtuLama(String token, Integer idCacahAnak) {
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<CacahKramaMipilDetailResponse> kramaMipilGetResponseCall = getData.getOrtuLamaMaperas(
                "Bearer " + token, idCacahAnak);
        kramaMipilGetResponseCall.enqueue(new Callback<CacahKramaMipilDetailResponse>() {
            @Override
            public void onResponse(Call<CacahKramaMipilDetailResponse> call, Response<CacahKramaMipilDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    if (response.body().getCacahKramaMipil().getPenduduk().getIbu() != null) {
                        ibuLamaField.setText(response.body().getCacahKramaMipil().getPenduduk().getIbu().getNama());
                        anakMaperas.getPenduduk().setIbu(response.body().getCacahKramaMipil().getPenduduk().getIbu());
                    }
                    if (response.body().getCacahKramaMipil().getPenduduk().getAyah() != null) {
                        ayahLamaField.setText(response.body().getCacahKramaMipil().getPenduduk().getAyah().getNama());
                        anakMaperas.getPenduduk().setAyah(response.body().getCacahKramaMipil().getPenduduk().getAyah());
                    }
                }
            }

            @Override
            public void onFailure(Call<CacahKramaMipilDetailResponse> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
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

    public void setEdit() {
        isAnakSelected = true;
        isBanjarAdatSelected = true;
        isKramaSelected = true;

        maperasKabupatenAutoComplete.setThreshold(100);
        maperasKecamatanAutoComplete.setThreshold(100);
        maperasDesaAdatAutoComplete.setThreshold(100);
        maperasBanjarAdatAutoComplete.setThreshold(100);

        maperasKabupatenAutoComplete.setText(maperas.getKramaMipilLama().getBanjarAdat().getDesaAdat().getKecamatan().getKabupaten().getName());
        maperasKecamatanAutoComplete.setText(maperas.getKramaMipilLama().getBanjarAdat().getDesaAdat().getKecamatan().getName());
        maperasDesaAdatAutoComplete.setText(maperas.getKramaMipilLama().getBanjarAdat().getDesaAdat().getDesadatNama());
        maperasBanjarAdatAutoComplete.setText(maperas.getKramaMipilLama().getBanjarAdat().getNamaBanjarAdat());

        kabupaten = maperas.getKramaMipilLama().getBanjarAdat().getDesaAdat().getKecamatan().getKabupaten();
        kecamatan = maperas.getKramaMipilLama().getBanjarAdat().getDesaAdat().getKecamatan();
        desaAdat = maperas.getKramaMipilLama().getBanjarAdat().getDesaAdat();
        banjarAdat = maperas.getKramaMipilLama().getBanjarAdat();

        idKabupaten = kabupaten.getId();
        idBanjarAdat = banjarAdat.getId();
        idKecamatan = kecamatan.getId();
        idDesaAdat = desaAdat.getId();
        isBanjarAdatSelected = true;
        getKabupaten();
        getKecamatan();
        getDesaAdat();
        getBanjarAdat();

        maperasKecamatanLayout.setVisibility(View.VISIBLE);
        maperasDesaAdatLayout.setVisibility(View.VISIBLE);
        maperasBanjarAdatLayout.setVisibility(View.VISIBLE);

        // krama mipil
        kramaMipilLama = maperas.getKramaMipilLama();
        kramaMipilName.setText(StringFormatter.formatNamaWithGelar(
                kramaMipilLama.getCacahKramaMipil().getPenduduk().getNama(),
                kramaMipilLama.getCacahKramaMipil().getPenduduk().getGelarDepan(), kramaMipilLama.getCacahKramaMipil().getPenduduk().getGelarBelakang())
        );
        kramaMipilNo.setText(kramaMipilLama.getNomorKramaMipil());
        kramaMipilBanjarAdat.setText(kramaMipilLama.getBanjarAdat().getNamaBanjarAdat());
        kramaMipilDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kramaDetail = new Intent(getApplicationContext(), KramaMipilDetailActivity.class);
                kramaDetail.putExtra(KRAMA_MIPIL_DETAIL_KEY, kramaMipilLama.getId());
                startActivity(kramaDetail);
            }
        });
        kramaMipilCard.setVisibility(View.VISIBLE);
        isKramaSelected = true;
        maperasSelectKramaButton.setText("Ganti Krama Mipil");

        // anak
        anakMaperas = maperas.getCacahKramaMipilLama();
        kramaAnakName.setText(StringFormatter.formatNamaWithGelar(
                anakMaperas.getPenduduk().getNama(),
                anakMaperas.getPenduduk().getGelarDepan(),
                anakMaperas.getPenduduk().getGelarBelakang())
        );
        kramaAnakNik.setText(anakMaperas.getPenduduk().getNik());
        noKramaMipilAnak.setText(anakMaperas.getNomorCacahKramaMipil());
        kramaMipilAnakDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                kramaDetail.putExtra(KRAMA_DETAIL_KEY, anakMaperas.getId());
                startActivity(kramaDetail);
            }
        });
        if (anakMaperas.getPenduduk().getFoto() != null) {
            kramaAnakImageLoadingContainer.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(getResources().getString(R.string.image_endpoint) +
                            anakMaperas.getPenduduk().getFoto())
                    .fit().centerCrop()
                    .into(kramaAnakImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            kramaAnakImageLoadingContainer.setVisibility(View.GONE);
                            kramaAnakImage.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onError(Exception e) {
                            kramaAnakImageLoadingContainer.setVisibility(View.GONE);
                            Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaAnakImage);
                        }
                    });
        }
        else {
            Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaAnakImage);
            kramaAnakImage.setVisibility(View.VISIBLE);
        }
        getOrtuLama(loginPreferences.getString("token", "empty"), anakMaperas.getId());
        kramaAnakCard.setVisibility(View.VISIBLE);
        isAnakSelected = true;
        maperasSelectAnakButton.setText("Ganti Anak");
    }
}