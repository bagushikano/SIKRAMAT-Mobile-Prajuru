package com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.bedabanjar;

import static android.view.View.GONE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.satubanjar.PerkawinanSatuBanjarPradanaSelectActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.satubanjar.PerkawinanSatuBanjarPurusaSelectActivity;
import com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.satubanjar.PerkawinanSatuBanjarStatusKekeluargaanActivity;
import com.bagushikano.sikedatmobileadmin.adapter.master.BanjarAdatSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.DesaAdatSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.KabupatenSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.master.KecamatanSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.perkawinan.PerkawinanMempelaiSelectionAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.kelahiran.KelahiranDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.master.BanjarAdatGetNoPaginationResponse;
import com.bagushikano.sikedatmobileadmin.model.master.DesaAdat;
import com.bagushikano.sikedatmobileadmin.model.master.DesaAdatGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Kabupaten;
import com.bagushikano.sikedatmobileadmin.model.master.KabupatenGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.Kecamatan;
import com.bagushikano.sikedatmobileadmin.model.master.KecamatanGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarAdat;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarAdatGetResponse;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.Perkawinan;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerkawinanBedaBanjarPasanganActivity extends AppCompatActivity {

    private Button perkawinanNextButton;

    private Button perkawinanSelectPurusaButton, perkawinanSelectPradanaButton;
    private Boolean isPradanaSelected = false, isPurusaSelected = false;

    private CacahKramaMipil purusaKrama, pradanaKrama;

    ActivityResultLauncher<Intent> startActivityIntent;

    private TextView kramaPurusaName, kramaPurusaNik, noKramaMipilPurusa;
    private Button kramaMipilPurusaDetailButton;
    private LinearLayout kramaPurusaImageLoadingContainer;
    private MaterialCardView kramaPurusaCard;
    private ImageView kramaPurusaImage;

    private TextView kramaPradanaName, kramaPradanaNik, noKramaMipilPradana;
    private Button kramaMipilPradanaDetailButton;
    private LinearLayout kramaPradanaImageLoadingContainer;
    private MaterialCardView kramaPradanaCard;
    private ImageView kramaPradanaImage;

    private final String KRAMA_SELECT_KEY = "KRAMA_SELECT_KEY";
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    private final String PURUSA_KEY = "PURUSA_SELECT_KEY";
    private final String PRADANA_KEY = "PRADANA_SELECT_KEY";

    Gson gson = new Gson();

    private LinearLayout perkawinanPasanganLoadingLayout;

    private AutoCompleteTextView perkawinanKabupatenAutoComplete, perkawinanKecamatanAutoComplete,
            perkawinanDesaAdatAutoComplete, perkawinanBanjarAdatAutoComplete;

    private TextInputLayout perkawinanKabupatenLayout, perkawinanKecamatanLayout,
            perkawinanDesaAdatLayout, perkawinanBanjarAdatLayout;

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

    private Perkawinan perkawinan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perkawinan_beda_banjar_pasangan);

        perkawinanPasanganLoadingLayout = findViewById(R.id.perkawinan_pasangan_progress_layout);

        perkawinanBanjarAdatAutoComplete = findViewById(R.id.perkawinan_banjar_adat_field);
        perkawinanKabupatenAutoComplete = findViewById(R.id.perkawinan_kabupaten_field);
        perkawinanKecamatanAutoComplete = findViewById(R.id.perkawinan_kecamatan_field);
        perkawinanDesaAdatAutoComplete = findViewById(R.id.perkawinan_desa_adat_field);

        perkawinanKabupatenLayout = findViewById(R.id.perkawinan_kabupaten_form);
        perkawinanKecamatanLayout = findViewById(R.id.perkawinan_kecamatan_form);
        perkawinanDesaAdatLayout = findViewById(R.id.perkawinan_desa_adat_form);
        perkawinanBanjarAdatLayout = findViewById(R.id.perkawinan_banjar_adat_form);

        kabupatenSelectionAdapter = new KabupatenSelectionAdapter(this, kabupatens);
        perkawinanKabupatenAutoComplete.setAdapter(kabupatenSelectionAdapter);
//        perkawinanKabupatenAutoComplete.setThreshold(100);
        perkawinanKabupatenAutoComplete.setInputType(0);
        perkawinanKabupatenAutoComplete.setKeyListener(null);

        perkawinanKabupatenAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                kabupaten = (Kabupaten) adapterView.getItemAtPosition(i);
                perkawinanKabupatenAutoComplete.setText(kabupaten.getName());
                idKabupaten = kabupaten.getId();

                kecamatan = null;
                desaAdat = null;
                banjarAdat = null;

                idBanjarAdat = null;
                idKecamatan = null;
                idDesaAdat = null;

                isBanjarAdatSelected = false;

                perkawinanKecamatanLayout.setVisibility(View.VISIBLE);
                perkawinanDesaAdatLayout.setVisibility(View.GONE);
                perkawinanBanjarAdatLayout.setVisibility(View.GONE);

                perkawinanKecamatanAutoComplete.setText(null);
                perkawinanDesaAdatAutoComplete.setText(null);
                perkawinanBanjarAdatAutoComplete.setText(null);

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

                desaAdat = null;
                banjarAdat = null;

                idDesaAdat = null;
                idBanjarAdat = null;

                isBanjarAdatSelected = false;

                perkawinanKecamatanLayout.setVisibility(View.VISIBLE);
                perkawinanDesaAdatLayout.setVisibility(View.VISIBLE);
                perkawinanBanjarAdatLayout.setVisibility(View.GONE);


                perkawinanDesaAdatAutoComplete.setText(null);
                perkawinanBanjarAdatAutoComplete.setText(null);

                getDesaAdat();
            }
        });

        desaAdatSelectionAdapter = new DesaAdatSelectionAdapter(this, desaAdats);
        perkawinanDesaAdatAutoComplete.setAdapter(desaAdatSelectionAdapter);
        perkawinanDesaAdatAutoComplete.setThreshold(100);
        perkawinanDesaAdatAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                desaAdat = (DesaAdat) adapterView.getItemAtPosition(i);
                perkawinanDesaAdatAutoComplete.setText(desaAdat.getDesadatNama());
                idDesaAdat = desaAdat.getId();

                banjarAdat = null;
                idBanjarAdat = null;

                isBanjarAdatSelected = false;

                perkawinanKecamatanLayout.setVisibility(View.VISIBLE);
                perkawinanDesaAdatLayout.setVisibility(View.VISIBLE);
                perkawinanBanjarAdatLayout.setVisibility(View.VISIBLE);

                perkawinanBanjarAdatAutoComplete.setText(null);

                getBanjarAdat();
            }
        });

        banjarAdatSelectionAdapter = new BanjarAdatSelectionAdapter(this, banjarAdats);
        perkawinanBanjarAdatAutoComplete.setAdapter(banjarAdatSelectionAdapter);
        perkawinanBanjarAdatAutoComplete.setThreshold(100);
        perkawinanBanjarAdatAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                banjarAdat = (BanjarAdat) adapterView.getItemAtPosition(i);
                perkawinanBanjarAdatAutoComplete.setText(banjarAdat.getNamaBanjarAdat());
                idBanjarAdat = banjarAdat.getId();

                isBanjarAdatSelected = true;
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "Berhasil memilih Banjar Adat Asal Pradana. Silahkan pilih Pradana.", Snackbar.LENGTH_SHORT).show();
            }
        });


        kramaPurusaName = findViewById(R.id.krama_nama_text);
        kramaPurusaNik = findViewById(R.id.krama_nik_text);
        noKramaMipilPurusa = findViewById(R.id.krama_no_mipil_text);
        kramaMipilPurusaDetailButton = findViewById(R.id.krama_purusa_detail_button);
        kramaPurusaImageLoadingContainer = findViewById(R.id.krama_image_loading_container);
        kramaPurusaImage = findViewById(R.id.krama_image);
        kramaPurusaCard = findViewById(R.id.krama_purusa_card);

        kramaPradanaName = findViewById(R.id.krama_pradana_nama_text);
        kramaPradanaNik = findViewById(R.id.krama_pradana_nik_text);
        noKramaMipilPradana = findViewById(R.id.krama_pradana_no_mipil_text);
        kramaMipilPradanaDetailButton = findViewById(R.id.krama_pradana_detail_button);
        kramaPradanaImageLoadingContainer = findViewById(R.id.krama_pradana_image_loading_container);
        kramaPradanaImage = findViewById(R.id.krama_pradana_image);
        kramaPradanaCard = findViewById(R.id.krama_pradana_card);

        perkawinanNextButton = findViewById(R.id.perkawinan_pasangan_next_button);
        perkawinanNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPradanaSelected && isPurusaSelected) {
                    Intent perkawinanNextIntent = new Intent(getApplicationContext(), PerkawinanBedaBanjarStatusKekeluargaanActivity.class);
                    perkawinanNextIntent.putExtra(PURUSA_KEY, gson.toJson(purusaKrama));
                    perkawinanNextIntent.putExtra(PRADANA_KEY, gson.toJson(pradanaKrama));
                    perkawinanNextIntent.putExtra("BANJAR_KEY", idBanjarAdat);
                    if (getIntent().hasExtra("EDIT_PERKAWINAN")) {
                        perkawinanNextIntent.putExtra("EDIT_PERKAWINAN", gson.toJson(perkawinan));
                    }
                    startActivityIntent.launch(perkawinanNextIntent);
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Periksa data pasangan kembali.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        perkawinanSelectPradanaButton = findViewById(R.id.perkawinan_pasangan_pradana_button);
        perkawinanSelectPurusaButton = findViewById(R.id.perkawinan_pasangan_purusa_button);

        perkawinanSelectPradanaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPurusaSelected && isBanjarAdatSelected) {
                    Intent perkawinanPradanaIntent = new Intent(getApplicationContext() , PerkawinanBedaBanjarPradanaSelectActivity.class);
                    perkawinanPradanaIntent.putExtra("PURUSA_ID", purusaKrama.getId());
                    perkawinanPradanaIntent.putExtra("BANJAR_ID", idBanjarAdat);
                    startActivityIntent.launch(perkawinanPradanaIntent);
                } else if (!(isBanjarAdatSelected)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Pilih asal Pradana terebih dahulu", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Pilih Purusa terebih dahulu", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        perkawinanSelectPurusaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent perkawinanPurusaIntent = new Intent(getApplicationContext() , PerkawinanBedaBanjarPurusaSelectActivity.class);
                startActivityIntent.launch(perkawinanPurusaIntent);
            }
        });

        startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 200) {
                            // purusa
                            purusaKrama = gson.fromJson(result.getData().getStringExtra(KRAMA_SELECT_KEY), CacahKramaMipil.class);
                            String namaFormated = purusaKrama.getPenduduk().getNama();
                            if (purusaKrama.getPenduduk().getGelarDepan() != null) {
                                namaFormated = String.format("%s %s",
                                        purusaKrama.getPenduduk().getGelarDepan(),
                                        purusaKrama.getPenduduk().getNama()
                                );
                            }


                            if (purusaKrama.getPenduduk().getGelarBelakang() != null) {
                                namaFormated = String.format("%s %s",
                                        namaFormated,
                                        purusaKrama.getPenduduk().getGelarBelakang()
                                );
                            }
                            kramaPurusaName.setText(namaFormated);
                            kramaPurusaNik.setText(purusaKrama.getPenduduk().getNik());
                            noKramaMipilPurusa.setText(purusaKrama.getNomorCacahKramaMipil());
                            kramaMipilPurusaDetailButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                                    kramaDetail.putExtra(KRAMA_DETAIL_KEY, purusaKrama.getId());
                                    startActivity(kramaDetail);
                                }
                            });
                            if (purusaKrama.getPenduduk().getFoto() != null) {
                                kramaPurusaImageLoadingContainer.setVisibility(View.VISIBLE);
                                Picasso.get()
                                        .load(getResources().getString(R.string.image_endpoint) +
                                                purusaKrama.getPenduduk().getFoto())
                                        .fit().centerCrop()
                                        .into(kramaPurusaImage, new com.squareup.picasso.Callback() {
                                            @Override
                                            public void onSuccess() {
                                                kramaPurusaImageLoadingContainer.setVisibility(View.GONE);
                                                kramaPurusaImage.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                kramaPurusaImageLoadingContainer.setVisibility(View.GONE);
                                                Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaPurusaImage);
                                            }
                                        });
                            }
                            else {
                                Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaPurusaImage);
                                kramaPurusaImage.setVisibility(View.VISIBLE);
                            }
                            kramaPurusaCard.setVisibility(View.VISIBLE);
                            isPurusaSelected = true;
                            perkawinanSelectPurusaButton.setText("Ganti Purusa");
                            perkawinanSelectPradanaButton.setText("Pilih Pradana");

                            if (isPradanaSelected) {
                                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                        "Purusa telah diganti. Silahkan pilih Pradana.", Snackbar.LENGTH_SHORT).show();
                                isPradanaSelected = false;
                                kramaPradanaCard.setVisibility(View.GONE);
                                pradanaKrama = null;
                            } else {
                                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                        "Purusa berhasil dipilih. Silahkan pilih Pradana.", Snackbar.LENGTH_SHORT).show();
                            }
                        } else if (result.getResultCode() == 100) {
                            // pradana
                            pradanaKrama = gson.fromJson(result.getData().getStringExtra(KRAMA_SELECT_KEY), CacahKramaMipil.class);
                            String namaFormated = pradanaKrama.getPenduduk().getNama();
                            if (pradanaKrama.getPenduduk().getGelarDepan() != null) {
                                namaFormated = String.format("%s %s",
                                        pradanaKrama.getPenduduk().getGelarDepan(),
                                        pradanaKrama.getPenduduk().getNama()
                                );
                            }


                            if (pradanaKrama.getPenduduk().getGelarBelakang() != null) {
                                namaFormated = String.format("%s %s",
                                        namaFormated,
                                        pradanaKrama.getPenduduk().getGelarBelakang()
                                );
                            }
                            kramaPradanaName.setText(namaFormated);
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
                            perkawinanSelectPradanaButton.setText("Ganti Pradana");
                            Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                    "Pradana berhasil dipilih.", Snackbar.LENGTH_SHORT).show();

                        } else if (result.getResultCode() == 1) {
                            setResult(1);
                            finish();
                        }
                    }
                });
        getKabupaten();
        if (getIntent().hasExtra("EDIT_PERKAWINAN")) {
            perkawinan = gson.fromJson(getIntent().getStringExtra("EDIT_PERKAWINAN"), Perkawinan.class);
            setEdit();
        }
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
        perkawinanPasanganLoadingLayout.setVisibility(View.VISIBLE);
        perkawinanKabupatenLayout.setEnabled(false);
        perkawinanKecamatanLayout.setEnabled(false);
        perkawinanDesaAdatLayout.setEnabled(false);
        perkawinanBanjarAdatLayout.setEnabled(false);
    }

    public void enableForm() {
        perkawinanPasanganLoadingLayout.setVisibility(View.GONE);
        perkawinanKabupatenLayout.setEnabled(true);
        perkawinanKecamatanLayout.setEnabled(true);
        perkawinanDesaAdatLayout.setEnabled(true);
        perkawinanBanjarAdatLayout.setEnabled(true);
    }

    public void setEdit() {
        isPradanaSelected = true;
        isBanjarAdatSelected = true;
        isPurusaSelected = true;
        perkawinanKabupatenAutoComplete.setThreshold(100);
        perkawinanKecamatanAutoComplete.setThreshold(100);
        perkawinanDesaAdatAutoComplete.setThreshold(100);
        perkawinanBanjarAdatAutoComplete.setThreshold(100);

        perkawinanKabupatenAutoComplete.setText(perkawinan.getBanjarAdatPradana().getDesaAdat().getKecamatan().getKabupaten().getName());
        perkawinanKecamatanAutoComplete.setText(perkawinan.getBanjarAdatPradana().getDesaAdat().getKecamatan().getName());
        perkawinanDesaAdatAutoComplete.setText(perkawinan.getBanjarAdatPradana().getDesaAdat().getDesadatNama());
        perkawinanBanjarAdatAutoComplete.setText(perkawinan.getBanjarAdatPradana().getNamaBanjarAdat());

        kabupaten = perkawinan.getBanjarAdatPradana().getDesaAdat().getKecamatan().getKabupaten();
        kecamatan = perkawinan.getBanjarAdatPradana().getDesaAdat().getKecamatan();
        desaAdat = perkawinan.getBanjarAdatPradana().getDesaAdat();
        banjarAdat = perkawinan.getBanjarAdatPradana();

        idKabupaten = kabupaten.getId();
        idBanjarAdat = banjarAdat.getId();
        idKecamatan = kecamatan.getId();
        idDesaAdat = desaAdat.getId();
        isBanjarAdatSelected = true;
        getKabupaten();
        getKecamatan();
        getDesaAdat();
        getBanjarAdat();

        perkawinanKecamatanLayout.setVisibility(View.VISIBLE);
        perkawinanDesaAdatLayout.setVisibility(View.VISIBLE);
        perkawinanBanjarAdatLayout.setVisibility(View.VISIBLE);

        purusaKrama = perkawinan.getPurusa();
        kramaPurusaName.setText(StringFormatter.formatNamaWithGelar(
                purusaKrama.getPenduduk().getNama(),
                purusaKrama.getPenduduk().getGelarDepan(),
                purusaKrama.getPenduduk().getGelarBelakang()));
        kramaPurusaNik.setText(purusaKrama.getPenduduk().getNik());
        noKramaMipilPurusa.setText(purusaKrama.getNomorCacahKramaMipil());
        kramaMipilPurusaDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                kramaDetail.putExtra(KRAMA_DETAIL_KEY, purusaKrama.getId());
                startActivity(kramaDetail);
            }
        });
        if (purusaKrama.getPenduduk().getFoto() != null) {
            kramaPurusaImageLoadingContainer.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(getResources().getString(R.string.image_endpoint) +
                            purusaKrama.getPenduduk().getFoto())
                    .fit().centerCrop()
                    .into(kramaPurusaImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            kramaPurusaImageLoadingContainer.setVisibility(View.GONE);
                            kramaPurusaImage.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            kramaPurusaImageLoadingContainer.setVisibility(View.GONE);
                            Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaPurusaImage);
                        }
                    });
        }
        else {
            Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaPurusaImage);
            kramaPurusaImage.setVisibility(View.VISIBLE);
        }
        kramaPurusaCard.setVisibility(View.VISIBLE);
        isPurusaSelected = true;
        perkawinanSelectPurusaButton.setText("Ganti Purusa");

        // pradana
        pradanaKrama = perkawinan.getPradana();

        kramaPradanaName.setText(StringFormatter.formatNamaWithGelar(
                pradanaKrama.getPenduduk().getNama(),
                pradanaKrama.getPenduduk().getGelarDepan(),
                pradanaKrama.getPenduduk().getGelarBelakang())
        );
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
        perkawinanSelectPradanaButton.setText("Ganti Pradana");
    }
}