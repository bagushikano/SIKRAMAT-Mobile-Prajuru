package com.bagushikano.sikedatmobileadmin.activity.maperas.pendataan.campuranmasuk;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.maperas.MaperasDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.PerkawinanDetailResponse;
import com.bagushikano.sikedatmobileadmin.util.ChangeDateTimeFormat;
import com.bagushikano.sikedatmobileadmin.util.DownloadUtil;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaperasCampuranMasukDetailActivity extends AppCompatActivity {


    private Toolbar homeToolbar;
    SharedPreferences loginPreferences;

    LinearLayout loadingContainer, failedContainer;
    SwipeRefreshLayout maperasContainer;
    Button refreshPerkawinan;
    LinearLayout perkawinanProgressLayout;

    private final String MAPERAS_DETAIL_KEY = "MAPERAS_DETAIL_KEY";
    private final String KRAMA_MIPIL_DETAIL_KEY = "KRAMA_MIPIL_DETAIL_KEY";
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    TextView tanggalMaperasText, namaPemuputText, noBuktiMaperas, noAktaText,
            keteranganText, maperasStatusText;
    Button aktaFileButton, buktiMaperasFileButton, saveDraftButton, saveSahButton;

    LinearLayout maperasProgressLayout;

    private TextView kramaAnakName, kramaAnakNik, noKramaMipilAnak;
    private Button kramaMipilAnakDetailButton;
    private LinearLayout kramaAnakImageLoadingContainer;
    private MaterialCardView kramaAnakCard;
    private ImageView kramaAnakImage;

    private TextView kramaAyahBaruName, kramaAyahBaruNik, noKramaMipilAyahBaru;
    private Button kramaMipilAyahBaruDetailButton;
    private LinearLayout kramaAyahBaruImageLoadingContainer;
    private MaterialCardView kramaAyahBaruCard;
    private ImageView kramaAyahBaruImage;

    private TextView kramaIbuBaruName, kramaIbuBaruNik, noKramaMipilIbuBaru;
    private Button kramaMipilIbuBaruDetailButton;
    private LinearLayout kramaIbuBaruImageLoadingContainer;
    private MaterialCardView kramaIbuBaruCard;
    private ImageView kramaIbuBaruImage;

    private TextView alamatAsal, provinsiAsal, kabkotAsal, kecAsal, desDinasAsal, namaAyah, namaIbu;
    private Button sudhiWadhani;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maperas_campuran_masuk_detail);

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        alamatAsal = findViewById(R.id.alamat_asal_pradana_text);
        provinsiAsal = findViewById(R.id.provinsi_asal_pradana_text);
        kabkotAsal = findViewById(R.id.kabkot_asal_pradana_text);
        kecAsal = findViewById(R.id.kec_asal_pradana_text);
        desDinasAsal = findViewById(R.id.desdinas_asal_pradana_text);
        namaAyah = findViewById(R.id.nama_ayah_pradana_text);
        namaIbu = findViewById(R.id.nama_ibu_pradana_text);
        sudhiWadhani = findViewById(R.id.perkawinan_berkas_sudhi_wadhani_show);

        loadingContainer = findViewById(R.id.maperas_loading_container);
        failedContainer = findViewById(R.id.maperas_failed_container);
        maperasContainer = findViewById(R.id.maperas_container);
        refreshPerkawinan = findViewById(R.id.maperas_refresh);

        kramaAnakName = findViewById(R.id.anak_maperas_nama_text);
        kramaAnakNik = findViewById(R.id.anak_maperas_nik_text);
        noKramaMipilAnak = findViewById(R.id.anak_maperas_no_mipil_text);
        kramaMipilAnakDetailButton = findViewById(R.id.anak_maperas_detail_button);
        kramaAnakImageLoadingContainer = findViewById(R.id.anak_maperas_image_loading_container);
        kramaAnakImage = findViewById(R.id.anak_maperas_image);
        kramaAnakCard = findViewById(R.id.anak_maperas_card);


        kramaAyahBaruName = findViewById(R.id.ayah_baru_maperas_nama_text);
        kramaAyahBaruNik = findViewById(R.id.ayah_baru_maperas_nik_text);
        noKramaMipilAyahBaru = findViewById(R.id.ayah_baru_maperas_no_mipil_text);
        kramaMipilAyahBaruDetailButton = findViewById(R.id.ayah_baru_maperas_detail_button);
        kramaAyahBaruImageLoadingContainer = findViewById(R.id.ayah_baru_maperas_image_loading_container);
        kramaAyahBaruImage = findViewById(R.id.ayah_baru_maperas_image);
        kramaAyahBaruCard = findViewById(R.id.ayah_baru_maperas_card);

        kramaIbuBaruName = findViewById(R.id.ibu_baru_maperas_nama_text);
        kramaIbuBaruNik = findViewById(R.id.ibu_baru_maperas_nik_text);
        noKramaMipilIbuBaru = findViewById(R.id.ibu_baru_maperas_no_mipil_text);
        kramaMipilIbuBaruDetailButton = findViewById(R.id.ibu_baru_maperas_detail_button);
        kramaIbuBaruImageLoadingContainer = findViewById(R.id.ibu_baru_maperas_image_loading_container);
        kramaIbuBaruImage = findViewById(R.id.ibu_baru_maperas_image);
        kramaIbuBaruCard = findViewById(R.id.ibu_baru_maperas_card);

        tanggalMaperasText = findViewById(R.id.maperas_tanggal_text);
        namaPemuputText = findViewById(R.id.maperas_nama_pemuput_text);
        noBuktiMaperas = findViewById(R.id.maperas_no_text);
        noAktaText = findViewById(R.id.maperas_no_akta_text);
        keteranganText = findViewById(R.id.maperas_keterangan_text);
        aktaFileButton = findViewById(R.id.maperas_berkas_akta_show);
        buktiMaperasFileButton = findViewById(R.id.maperas_berkas_show);
        maperasProgressLayout = findViewById(R.id.maperas_pendataan_progress_layout);
        saveSahButton = findViewById(R.id.maperas_store_sah_button);
        saveDraftButton = findViewById(R.id.maperas_store_draft_button);
        maperasStatusText = findViewById(R.id.maperas_detail_status_text);

        maperasContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(MAPERAS_DETAIL_KEY, -1));
            }
        });

        saveSahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sahMaperas(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(MAPERAS_DETAIL_KEY, -1));
            }
        });

        getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(MAPERAS_DETAIL_KEY, -1));
    }

    public void getDetail(String token, Integer id) {
        setLoadingContainerVisible();
        maperasContainer.setRefreshing(false);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<MaperasDetailResponse> maperasDetailResponseCall = getData.getDetailMaperas("Bearer " + token, id);
        maperasDetailResponseCall.enqueue(new Callback<MaperasDetailResponse>() {
            @Override
            public void onResponse(Call<MaperasDetailResponse> call, Response<MaperasDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    if (response.body().getMaperas().getStatusMaperas() == 0) {
                        maperasStatusText.setText("Draft");
                        maperasStatusText.setTextColor(getApplicationContext().getColor(R.color.yellow));
                    } else if (response.body().getMaperas().getStatusMaperas() == 3) {
                        maperasStatusText.setText("Sah");
                        maperasStatusText.setTextColor(getApplicationContext().getColor(R.color.green));
                        saveSahButton.setVisibility(GONE);
                    }

                    if (response.body().getMaperas().getAlamatAsal() != null) {
                        alamatAsal.setText(response.body().getMaperas().getAlamatAsal().toString());
                    }
                    provinsiAsal.setText(
                            response.body().getMaperas().getDesaDinasAsal().getKecamatan().getKabupaten().getProvinsi().getName());
                    kabkotAsal.setText(response.body().getMaperas().getDesaDinasAsal().getKecamatan().getKabupaten().getName());
                    kecAsal.setText(response.body().getMaperas().getDesaDinasAsal().getKecamatan().getName());
                    desDinasAsal.setText(response.body().getMaperas().getDesaDinasAsal().getName());
                    if (response.body().getMaperas().getNamaAyahLama() != null) {
                        namaAyah.setText(response.body().getMaperas().getNamaAyahLama().toString());
                    }
                    if (response.body().getMaperas().getNamaIbuLama() != null) {
                        namaIbu.setText(response.body().getMaperas().getNamaIbuLama().toString());
                    }

                    if (response.body().getMaperas().getFileSudhiWadhani() != null) {
                        sudhiWadhani.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DownloadUtil downloadUtil = new DownloadUtil(getApplicationContext());
                                Long download =   downloadUtil.downloadFile(
                                        getResources().getString(R.string.api_endpoint) + response.body().getMaperas().getFileSudhiWadhani());
                                if (download != 0) {
                                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                            "Berkas sedang diunduh", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                            "Berkas gagal diunduh", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        sudhiWadhani.setVisibility(GONE);
                    }

                    if (response.body().getMaperas().getNomorAktaPengangkatanAnak() != null) {
                        noAktaText.setText(response.body().getMaperas().getNomorAktaPengangkatanAnak());
                        aktaFileButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DownloadUtil downloadUtil = new DownloadUtil(getApplicationContext());
                                Long download =   downloadUtil.downloadFile(
                                        getResources().getString(R.string.api_endpoint) + response.body().getMaperas().getFileAktaPengangkatanAnak());
                                if (download != 0) {
                                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                            "Berkas sedang diunduh", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                            "Berkas gagal diunduh", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        aktaFileButton.setVisibility(View.GONE);
                    }
                    if (response.body().getMaperas().getNomorMaperas() != null) {
                        noBuktiMaperas.setText(response.body().getMaperas().getNomorMaperas());
                        buktiMaperasFileButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DownloadUtil downloadUtil = new DownloadUtil(getApplicationContext());
                                Long download =   downloadUtil.downloadFile(
                                        getResources().getString(R.string.api_endpoint) + response.body().getMaperas().getFileBuktiMaperas());
                                if (download != 0) {
                                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                            "Berkas sedang diunduh", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                            "Berkas gagal diunduh", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        buktiMaperasFileButton.setVisibility(GONE);
                    }
                    tanggalMaperasText.setText(ChangeDateTimeFormat.changeDateFormat(response.body().getMaperas().getTanggalMaperas()));
                    namaPemuputText.setText(response.body().getMaperas().getNamaPemuput());

                    if (response.body().getMaperas().getKeterangan() != null) {
                        keteranganText.setText(response.body().getMaperas().getKeterangan());
                    }

                    // anak
                    kramaAnakName.setText(StringFormatter.formatNamaWithGelar(
                            response.body().getMaperas().getCacahKramaMipilBaru().getPenduduk().getNama(),
                            response.body().getMaperas().getCacahKramaMipilBaru().getPenduduk().getGelarDepan(),
                            response.body().getMaperas().getCacahKramaMipilBaru().getPenduduk().getGelarBelakang()
                    ));
                    kramaAnakNik.setText(response.body().getMaperas().getCacahKramaMipilBaru().getPenduduk().getNik());
                    noKramaMipilAnak.setText(response.body().getMaperas().getCacahKramaMipilBaru().getNomorCacahKramaMipil());
                    kramaMipilAnakDetailButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                            kramaDetail.putExtra(KRAMA_DETAIL_KEY, response.body().getMaperas().getCacahKramaMipilBaru().getId());
                            startActivity(kramaDetail);
                        }
                    });
                    if (response.body().getMaperas().getCacahKramaMipilBaru().getPenduduk().getFoto() != null) {
                        kramaAnakImageLoadingContainer.setVisibility(View.VISIBLE);
                        Picasso.get()
                                .load(getResources().getString(R.string.image_endpoint) +
                                        response.body().getMaperas().getCacahKramaMipilBaru().getPenduduk().getFoto())
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

                    // ayah baru
                    kramaAyahBaruCard.setVisibility(View.VISIBLE);
                    kramaAyahBaruName.setText(StringFormatter.formatNamaWithGelar(
                            response.body().getMaperas().getAyahBaru().getPenduduk().getNama(),
                            response.body().getMaperas().getAyahBaru().getPenduduk().getGelarDepan(),
                            response.body().getMaperas().getAyahBaru().getPenduduk().getGelarBelakang()
                    ));
                    kramaAyahBaruNik.setText(response.body().getMaperas().getAyahBaru().getPenduduk().getNik());
                    noKramaMipilAyahBaru.setText(response.body().getMaperas().getAyahBaru().getNomorCacahKramaMipil());
                    kramaMipilAyahBaruDetailButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                            kramaDetail.putExtra(KRAMA_DETAIL_KEY, response.body().getMaperas().getAyahBaru().getId());
                            startActivity(kramaDetail);
                        }
                    });
                    if (response.body().getMaperas().getAyahBaru().getPenduduk().getFoto() != null) {
                        kramaAyahBaruImageLoadingContainer.setVisibility(View.VISIBLE);
                        Picasso.get()
                                .load(getResources().getString(R.string.image_endpoint) +
                                        response.body().getMaperas().getAyahBaru().getPenduduk().getFoto())
                                .fit().centerCrop()
                                .into(kramaAyahBaruImage, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                        kramaAyahBaruImageLoadingContainer.setVisibility(View.GONE);
                                        kramaAyahBaruImage.setVisibility(View.VISIBLE);
                                    }
                                    @Override
                                    public void onError(Exception e) {
                                        kramaAyahBaruImageLoadingContainer.setVisibility(View.GONE);
                                        Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaAyahBaruImage);
                                    }
                                });
                    }
                    else {
                        Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaAyahBaruImage);
                        kramaAyahBaruImage.setVisibility(View.VISIBLE);
                    }

                    // ibu baru
                    kramaIbuBaruName.setText(StringFormatter.formatNamaWithGelar(
                            response.body().getMaperas().getIbuBaru().getPenduduk().getNama(),
                            response.body().getMaperas().getIbuBaru().getPenduduk().getGelarDepan(),
                            response.body().getMaperas().getIbuBaru().getPenduduk().getGelarBelakang()
                    ));
                    kramaIbuBaruNik.setText(response.body().getMaperas().getIbuBaru().getPenduduk().getNik());
                    noKramaMipilIbuBaru.setText(response.body().getMaperas().getIbuBaru().getNomorCacahKramaMipil());
                    kramaMipilIbuBaruDetailButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                            kramaDetail.putExtra(KRAMA_DETAIL_KEY, response.body().getMaperas().getIbuBaru().getId());
                            startActivity(kramaDetail);
                        }
                    });
                    if (response.body().getMaperas().getIbuBaru().getPenduduk().getFoto() != null) {
                        kramaIbuBaruImageLoadingContainer.setVisibility(View.VISIBLE);
                        Picasso.get()
                                .load(getResources().getString(R.string.image_endpoint) +
                                        response.body().getMaperas().getIbuBaru().getPenduduk().getFoto())
                                .fit().centerCrop()
                                .into(kramaIbuBaruImage, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                        kramaIbuBaruImageLoadingContainer.setVisibility(View.GONE);
                                        kramaIbuBaruImage.setVisibility(View.VISIBLE);
                                    }
                                    @Override
                                    public void onError(Exception e) {
                                        kramaIbuBaruImageLoadingContainer.setVisibility(View.GONE);
                                        Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaIbuBaruImage);
                                    }
                                });
                    }
                    else {
                        Picasso.get().load(R.drawable.paceholder_krama_pict).into(kramaIbuBaruImage);
                        kramaIbuBaruImage.setVisibility(View.VISIBLE);
                    }

                    setKramaContainerVisible();
                }
                else {
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                maperasContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<MaperasDetailResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void sahMaperas(String token, int id) {
        maperasProgressLayout.setVisibility(View.VISIBLE);
        saveSahButton.setVisibility(GONE);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<MaperasDetailResponse> perkawinanDetailResponseCall = getData.maperasCampuranMasukSah("Bearer " + token, id);
        perkawinanDetailResponseCall.enqueue(new Callback<MaperasDetailResponse>() {
            @Override
            public void onResponse(Call<MaperasDetailResponse> call, Response<MaperasDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Data Maperas berhasil disahkan.", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                maperasProgressLayout.setVisibility(View.GONE);
                getDetail(loginPreferences.getString("token", "empty"), id);
            }

            @Override
            public void onFailure(Call<MaperasDetailResponse> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    public void setFailedContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(View.VISIBLE);
        maperasContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        loadingContainer.setVisibility(View.VISIBLE);
        failedContainer.setVisibility(GONE);
        maperasContainer.setVisibility(GONE);
    }

    public void setKramaContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(GONE);
        maperasContainer.setVisibility(View.VISIBLE);
    }
}