package com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.satubanjar;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.kematian.KematianAjuanDetailActivity;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.kematian.KematianAjuanDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.PerkawinanDetailResponse;
import com.bagushikano.sikedatmobileadmin.util.ChangeDateTimeFormat;
import com.bagushikano.sikedatmobileadmin.util.DownloadUtil;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerkawinanSatuBanjarDetailActivity extends AppCompatActivity {

    private Toolbar homeToolbar;
    SharedPreferences loginPreferences;

    LinearLayout loadingContainer, failedContainer;
    SwipeRefreshLayout perkawinanContainer;
    Button refreshPerkawinan;

    TextView tanggalPerkawinanText, namaPemuputText, noBuktiSerahTerimaText, noAktaText,
            keteranganText, statusKekeluargaanText, calonKramaText, perkawinanStatusText;
    Button aktaFileButton, serahTerimaFileButton, saveSahButton;
    LinearLayout perkawinanProgressLayout, perkawinanCalonKramaLayout;
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

    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";
    private final String PERKAWINAN_DETAIL_KEY = "PERKAWINAN_DETAIL_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perkawinan_satu_banjar_detail);

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        loadingContainer = findViewById(R.id.perkawinan_detail_loading_container);
        failedContainer = findViewById(R.id.perkawinan_detail_failed_container);
        perkawinanContainer = findViewById(R.id.perkawinan_detail_container);
        refreshPerkawinan = findViewById(R.id.perkawinan_detail_refresh);

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

        tanggalPerkawinanText = findViewById(R.id.perkawinan_tanggal_text);
        namaPemuputText = findViewById(R.id.perkawinan_nama_pemuput_text);
        noBuktiSerahTerimaText = findViewById(R.id.perkawinan_no_serah_terima_text);
        noAktaText = findViewById(R.id.perkawinan_no_akta_text);
        keteranganText = findViewById(R.id.perkawinan_keterangan_text);
        aktaFileButton = findViewById(R.id.perkawinan_berkas_akta_show);
        serahTerimaFileButton = findViewById(R.id.perkawinan_berkas_serah_terima_show);
        perkawinanProgressLayout = findViewById(R.id.perkawinan_pendataan_progress_layout);
        saveSahButton = findViewById(R.id.perkawinan_store_sah_button);
        calonKramaText = findViewById(R.id.perkawinan_calon_krama_text);
        perkawinanCalonKramaLayout = findViewById(R.id.perkawinan_calon_krama_layout);
        statusKekeluargaanText = findViewById(R.id.perkawinan_status_kekeluargaan_text);
        perkawinanStatusText = findViewById(R.id.perkawinan_detail_status_text);

        saveSahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sahPerkawinan(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(PERKAWINAN_DETAIL_KEY, -1));
            }
        });

        perkawinanContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(PERKAWINAN_DETAIL_KEY, -1));
            }
        });

        getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(PERKAWINAN_DETAIL_KEY, -1));
    }

    public void getDetail(String token, Integer id) {
        setLoadingContainerVisible();
        perkawinanContainer.setRefreshing(true);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<PerkawinanDetailResponse> perkawinanDetailResponseCall = getData.getPerkawinanDetail("Bearer " + token, id);
        perkawinanDetailResponseCall.enqueue(new Callback<PerkawinanDetailResponse>() {
            @Override
            public void onResponse(Call<PerkawinanDetailResponse> call, Response<PerkawinanDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    perkawinanContainer.setRefreshing(false);
                    if (response.body().getPerkawinan().getStatusPerkawinan() == 0) {
                        perkawinanStatusText.setText("Draft");
                        perkawinanStatusText.setTextColor(getApplicationContext().getColor(R.color.yellow));
                    } else if (response.body().getPerkawinan().getStatusPerkawinan() == 3) {
                        perkawinanStatusText.setText("Sah");
                        perkawinanStatusText.setTextColor(getApplicationContext().getColor(R.color.green));
                        saveSahButton.setVisibility(GONE);
                    }

                    if (response.body().getPerkawinan().getNomorAktaPerkawinan() != null) {
                        noAktaText.setText(response.body().getPerkawinan().getNomorAktaPerkawinan());
                        aktaFileButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DownloadUtil downloadUtil = new DownloadUtil(getApplicationContext());
                                Long download =   downloadUtil.downloadFile(
                                        getResources().getString(R.string.api_endpoint) + response.body().getPerkawinan().getFileAktaPerkawinan());
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
                    if (response.body().getPerkawinan().getNomorPerkawinan() != null) {
                        noBuktiSerahTerimaText.setText(response.body().getPerkawinan().getNomorPerkawinan());
                        serahTerimaFileButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DownloadUtil downloadUtil = new DownloadUtil(getApplicationContext());
                                Long download =   downloadUtil.downloadFile(
                                        getResources().getString(R.string.api_endpoint) + response.body().getPerkawinan().getFileBuktiSerahTerimaPerkawinan());
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
                        serahTerimaFileButton.setVisibility(GONE);
                    }
                    tanggalPerkawinanText.setText(ChangeDateTimeFormat.changeDateFormat(response.body().getPerkawinan().getTanggalPerkawinan()));
                    namaPemuputText.setText(response.body().getPerkawinan().getNamaPemuput());

                    if (response.body().getPerkawinan().getKeterangan() != null) {
                        keteranganText.setText(response.body().getPerkawinan().getKeterangan());
                    }
                    if (response.body().getPerkawinan().getStatusKekeluargaan().equals("baru")) {
                        statusKekeluargaanText.setText("Pembentukan Krama Mipil (Kepala Keluarga) Baru");
                        perkawinanCalonKramaLayout.setVisibility(View.VISIBLE);
                        if (response.body().getPerkawinan().getCalonKramaId().equals(response.body().getPerkawinan().getPradana().getId())) {
                            calonKramaText.setText(response.body().getPerkawinan().getPradana().getPenduduk().getNama());
                        } else {
                            calonKramaText.setText(response.body().getPerkawinan().getPurusa().getPenduduk().getNama());
                        }
                    } else {
                        statusKekeluargaanText.setText("Tetap di Krama Mipil (Kepala Keluarga) Lama");
                    }

                    // purusa
                    String namaFormated = response.body().getPerkawinan().getPurusa().getPenduduk().getNama();
                    if (response.body().getPerkawinan().getPurusa().getPenduduk().getGelarDepan() != null) {
                        namaFormated = String.format("%s %s",
                                response.body().getPerkawinan().getPurusa().getPenduduk().getGelarDepan(),
                                response.body().getPerkawinan().getPurusa().getPenduduk().getNama()
                        );
                    }


                    if (response.body().getPerkawinan().getPurusa().getPenduduk().getGelarBelakang() != null) {
                        namaFormated = String.format("%s %s",
                                namaFormated,
                                response.body().getPerkawinan().getPurusa().getPenduduk().getGelarBelakang()
                        );
                    }
                    kramaPurusaName.setText(namaFormated);
                    kramaPurusaNik.setText(response.body().getPerkawinan().getPurusa().getPenduduk().getNik());
                    noKramaMipilPurusa.setText(response.body().getPerkawinan().getPurusa().getNomorCacahKramaMipil());
                    kramaMipilPurusaDetailButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                            kramaDetail.putExtra(KRAMA_DETAIL_KEY, response.body().getPerkawinan().getPurusa().getId());
                            startActivity(kramaDetail);
                        }
                    });
                    if (response.body().getPerkawinan().getPurusa().getPenduduk().getFoto() != null) {
                        kramaPurusaImageLoadingContainer.setVisibility(View.VISIBLE);
                        Picasso.get()
                                .load(getResources().getString(R.string.image_endpoint) +
                                        response.body().getPerkawinan().getPurusa().getPenduduk().getFoto())
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

                    //pradana
                    namaFormated = response.body().getPerkawinan().getPradana().getPenduduk().getNama();
                    if (response.body().getPerkawinan().getPradana().getPenduduk().getGelarDepan() != null) {
                        namaFormated = String.format("%s %s",
                                response.body().getPerkawinan().getPradana().getPenduduk().getGelarDepan(),
                                response.body().getPerkawinan().getPradana().getPenduduk().getNama()
                        );
                    }


                    if (response.body().getPerkawinan().getPradana().getPenduduk().getGelarBelakang() != null) {
                        namaFormated = String.format("%s %s",
                                namaFormated,
                                response.body().getPerkawinan().getPradana().getPenduduk().getGelarBelakang()
                        );
                    }
                    kramaPradanaName.setText(namaFormated);
                    kramaPradanaNik.setText(response.body().getPerkawinan().getPradana().getPenduduk().getNik());
                    noKramaMipilPradana.setText(response.body().getPerkawinan().getPradana().getNomorCacahKramaMipil());
                    kramaMipilPradanaDetailButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                            kramaDetail.putExtra(KRAMA_DETAIL_KEY, response.body().getPerkawinan().getPradana().getId());
                            startActivity(kramaDetail);
                        }
                    });
                    if (response.body().getPerkawinan().getPradana().getPenduduk().getFoto() != null) {
                        kramaPradanaImageLoadingContainer.setVisibility(View.VISIBLE);
                        Picasso.get()
                                .load(getResources().getString(R.string.image_endpoint) +
                                        response.body().getPerkawinan().getPradana().getPenduduk().getFoto())
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
                    setKramaContainerVisible();
                }
                else {
                    perkawinanContainer.setRefreshing(false);
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                perkawinanContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<PerkawinanDetailResponse> call, Throwable t) {
                perkawinanContainer.setRefreshing(false);
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    public void sahPerkawinan(String token, int id) {
        perkawinanProgressLayout.setVisibility(View.VISIBLE);
        saveSahButton.setVisibility(GONE);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<PerkawinanDetailResponse> perkawinanDetailResponseCall = getData.perkawinanSahSatuBanjarAdat("Bearer " + token, id);
        perkawinanDetailResponseCall.enqueue(new Callback<PerkawinanDetailResponse>() {
            @Override
            public void onResponse(Call<PerkawinanDetailResponse> call, Response<PerkawinanDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Data Perkawinan berhasil disahkan.", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                perkawinanProgressLayout.setVisibility(View.GONE);
                getDetail(loginPreferences.getString("token", "empty"), id);
            }

            @Override
            public void onFailure(Call<PerkawinanDetailResponse> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void setFailedContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(View.VISIBLE);
        perkawinanContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        loadingContainer.setVisibility(View.VISIBLE);
        failedContainer.setVisibility(GONE);
        perkawinanContainer.setVisibility(GONE);
    }

    public void setKramaContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(GONE);
        perkawinanContainer.setVisibility(View.VISIBLE);
    }
}