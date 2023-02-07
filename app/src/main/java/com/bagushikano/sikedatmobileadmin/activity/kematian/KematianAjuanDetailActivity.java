package com.bagushikano.sikedatmobileadmin.activity.kematian;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.kelahiran.KelahiranApproveResponse;
import com.bagushikano.sikedatmobileadmin.model.kelahiran.KelahiranDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.kematian.KematianAjuanDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.kematian.KematianApproveResponse;
import com.bagushikano.sikedatmobileadmin.model.kematian.KematianDetailResponse;
import com.bagushikano.sikedatmobileadmin.util.ChangeDateTimeFormat;
import com.bagushikano.sikedatmobileadmin.util.DownloadUtil;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KematianAjuanDetailActivity extends AppCompatActivity {

    TextView statusAjuanKematian, tanggalAjuanKematian, kematianTolak,
            noAktaKematian, namaCacahKematian, tanggalKematian, penyebabKematian, noSuketKematian,
            keteranganKematianText, kematianPengajuText;
    Button showAktaKematianButton, showSuketKematianButton, showDetailCacahMeninggal,
            showTolakAjuanCardButton, tolakAjuanCancelButton, submitTolakButton;
    MaterialCardView tolakAjuanCard, tolakAlasanCard;

    TextInputEditText alasanTolakField;

    private final String KEMATIAN_DETAIL_KEY = "KEMATIAN_DETAIL_KEY";
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    LinearLayout loadingContainer, failedContainer, kematianEmptyContainer;
    SwipeRefreshLayout kematianContainer;
    Button refreshKematian;
    SharedPreferences loginPreferences;
    private Toolbar homeToolbar;

    private Button approveKematian;
    LinearLayout kematianApproveProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kematian_ajuan_detail);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        loadingContainer = findViewById(R.id.kematian_loading_container);
        failedContainer = findViewById(R.id.kematian_failed_container);
        kematianContainer = findViewById(R.id.kematian_container);
        refreshKematian = findViewById(R.id.kematian_refresh);

        statusAjuanKematian = findViewById(R.id.kematian_ajuan_status);
        tanggalAjuanKematian = findViewById(R.id.kematian_ajuan_tanggal);
        noAktaKematian = findViewById(R.id.kematian_ajuan_no_akta_kematian);
        noSuketKematian = findViewById(R.id.kematian_ajuan_no_suket_kematian);
        namaCacahKematian = findViewById(R.id.kematian_ajuan_nama_meninggal);
        tanggalKematian = findViewById(R.id.kematian_ajuan_tanggal_kematian);
        penyebabKematian = findViewById(R.id.kematian_ajuan_penyebab_kematian);
        keteranganKematianText = findViewById(R.id.kematian_keterangan);
        kematianPengajuText = findViewById(R.id.kematian_ajuan_pengaju);

        showSuketKematianButton = findViewById(R.id.kematian_ajuan_suket_kematian_show);
        showAktaKematianButton = findViewById(R.id.kematian_ajuan_akta_kematian_show);
        showDetailCacahMeninggal = findViewById(R.id.kematian_ajuan_detail_cacah_mipil_button);

        approveKematian = findViewById(R.id.approve_kematian_button);
        kematianApproveProgress = findViewById(R.id.approve_progress_kematian);
        showTolakAjuanCardButton = findViewById(R.id.tolak_ajuan_kematian_show_button);
        tolakAjuanCard = findViewById(R.id.tolak_ajuan_kematian_card);
        tolakAjuanCancelButton = findViewById(R.id.kematian_ajuan_tolak_cancel_button);

        showTolakAjuanCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tolakAjuanCard.setVisibility(View.VISIBLE);
                approveKematian.setVisibility(GONE);
                showTolakAjuanCardButton.setVisibility(GONE);
            }
        });

        tolakAjuanCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tolakAjuanCard.setVisibility(GONE);
                approveKematian.setVisibility(View.VISIBLE);
                showTolakAjuanCardButton.setVisibility(View.VISIBLE);
            }
        });

        kematianTolak = findViewById(R.id.kematian_ajuan_tolak_text);
        tolakAlasanCard = findViewById(R.id.kematian_ajuan_alasan_tolak_card);
        alasanTolakField = findViewById(R.id.kematian_ajuan_tolak_field);
        submitTolakButton = findViewById(R.id.submit_kematian_ajuan_tolak_button);
        submitTolakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alasanTolakField.getText().toString().length() == 0) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Alasan tolak ajuan tidak boleh kosong.", Snackbar.LENGTH_LONG).show();
                }
                else {
                    new MaterialAlertDialogBuilder(KematianAjuanDetailActivity.this)
                            .setTitle("Penolakan Ajuan")
                            .setMessage("Yakin ingin menolak ajuan kematian?")
                            .setPositiveButton("Tolak Ajuan", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    tolakKematian(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KEMATIAN_DETAIL_KEY, -1));
                                }
                            })
                            .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                }
            }
        });

        approveKematian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approveKematian(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KEMATIAN_DETAIL_KEY, -1));
            }
        });

        refreshKematian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KEMATIAN_DETAIL_KEY, -1));
            }
        });

        kematianContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KEMATIAN_DETAIL_KEY, -1));
            }
        });
        getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KEMATIAN_DETAIL_KEY, -1));
    }

    public void getDetail(String token, int id) {
        setLoadingContainerVisible();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KematianAjuanDetailResponse> kematianDetailResponseCall = getData.getKematianAjuanDetail("Bearer " + token, id);
        kematianDetailResponseCall.enqueue(new Callback<KematianAjuanDetailResponse>() {
            @Override
            public void onResponse(Call<KematianAjuanDetailResponse> call, Response<KematianAjuanDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    if (response.body().getKematianAjuan().getStatus() == 3) {
                        statusAjuanKematian.setText("Sah");
                        approveKematian.setVisibility(GONE);
                        showTolakAjuanCardButton.setVisibility(GONE);

                    } else if (response.body().getKematianAjuan().getStatus() == 1) {
                        statusAjuanKematian.setText("Ajuan sedang dalam proses");
                        approveKematian.setVisibility(View.VISIBLE);
                        showTolakAjuanCardButton.setVisibility(View.VISIBLE);

                        approveKematian.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new MaterialAlertDialogBuilder(KematianAjuanDetailActivity.this)
                                        .setTitle("Pengesahan Ajuan")
                                        .setMessage("Yakin ingin mengesahkan ajuan kematian?")
                                        .setPositiveButton("Sahkan Ajuan", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                approveKematian(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KEMATIAN_DETAIL_KEY, -1));
                                            }
                                        })
                                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        });

                    } else if (response.body().getKematianAjuan().getStatus() == 2) {
                        statusAjuanKematian.setText("Ajuan ditolak");
                        approveKematian.setVisibility(GONE);
                        showTolakAjuanCardButton.setVisibility(GONE);
                        tolakAlasanCard.setVisibility(View.VISIBLE);
                        kematianTolak.setText(response.body().getKematianAjuan().getAlasanTolakAjuan());

                    } else if (response.body().getKematianAjuan().getStatus() == 0) {
                        statusAjuanKematian.setText("Menunggu untuk diproses");
                        showTolakAjuanCardButton.setVisibility(GONE);
                        approveKematian.setVisibility(View.VISIBLE);

                        approveKematian.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                prosesKematian(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KEMATIAN_DETAIL_KEY, -1));
                            }
                        });
                        approveKematian.setText("Terima ajuan");

                    }

                    if (response.body().getKematianAjuan().getKeterangan() != null) {
                        keteranganKematianText.setText(response.body().getKematianAjuan().getKeterangan());
                    }

                    kematianPengajuText.setText(response.body().getPenduduk().getNama());

                    namaCacahKematian.setText(response.body().getKematianAjuan().getCacahKramaMipil().getPenduduk().getNama());
                    if (response.body().getKematianAjuan().getNomorAktaKematian() != null) {
                        noAktaKematian.setText(response.body().getKematianAjuan().getNomorAktaKematian());
                    }
                    else {
                        noAktaKematian.setText("-");
                    }
                    if (response.body().getKematianAjuan().getFileAktaKematian() != null ) {
                        showAktaKematianButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DownloadUtil downloadUtil = new DownloadUtil(getApplicationContext());
                                Long download =   downloadUtil.downloadFile(
                                        getResources().getString(R.string.api_endpoint) + response.body().getKematianAjuan().getFileAktaKematian());
                                if (download != 0) {
                                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                            "Berkas sedang diunduh", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                            "Berkas gagal diunduh", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        showAktaKematianButton.setVisibility(GONE);
                    }

                    if (response.body().getKematianAjuan().getNomorSuketKematian() != null) {
                        noSuketKematian.setText(response.body().getKematianAjuan().getNomorSuketKematian());
                    }
                    else {
                        noSuketKematian.setText("-");
                    }
                    if (response.body().getKematianAjuan().getFileSuketKematian() != null ) {
                        showSuketKematianButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DownloadUtil downloadUtil = new DownloadUtil(getApplicationContext());
                                Long download =   downloadUtil.downloadFile(
                                        getResources().getString(R.string.api_endpoint) + response.body().getKematianAjuan().getFileSuketKematian());
                                if (download != 0) {
                                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                            "Berkas sedang diunduh", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                            "Berkas gagal diunduh", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        showSuketKematianButton.setVisibility(GONE);
                    }

                    tanggalKematian.setText(ChangeDateTimeFormat.changeDateFormat(
                            response.body().getKematianAjuan().getTanggalKematian())
                    );

                    tanggalAjuanKematian.setText(ChangeDateTimeFormat.changeDateTimeFormatForCreatedAt(
                            response.body().getKematianAjuan().getCreatedAt())
                    );

                    penyebabKematian.setText(response.body().getKematianAjuan().getPenyebabKematian());

                    showDetailCacahMeninggal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                            kramaDetail.putExtra(KRAMA_DETAIL_KEY, response.body().getKematianAjuan().getCacahKramaMipil().getId());
                            startActivity(kramaDetail);
                        }
                    });

                    setKramaContainerVisible();
                }
                else {
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                kematianContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<KematianAjuanDetailResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void approveKematian(String token, int id) {
        kematianApproveProgress.setVisibility(View.VISIBLE);
        approveKematian.setVisibility(View.GONE);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KematianApproveResponse> kematianApproveResponseCall = getData.kematianApprove("Bearer " + token, id);
        kematianApproveResponseCall.enqueue(new Callback<KematianApproveResponse>() {
            @Override
            public void onResponse(Call<KematianApproveResponse> call, Response<KematianApproveResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Ajuan kematian telah di sahkan", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                kematianApproveProgress.setVisibility(View.GONE);
                approveKematian.setVisibility(GONE);
                showTolakAjuanCardButton.setVisibility(GONE);
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KEMATIAN_DETAIL_KEY, -1));
            }

            @Override
            public void onFailure(Call<KematianApproveResponse> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void tolakKematian(String token, int id) {
        kematianApproveProgress.setVisibility(View.VISIBLE);
        tolakAjuanCard.setVisibility(GONE);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KematianDetailResponse> kelahiranTolakCall = getData.kematianTolak("Bearer " + token, id, alasanTolakField.getText().toString());
        kelahiranTolakCall.enqueue(new Callback<KematianDetailResponse>() {
            @Override
            public void onResponse(Call<KematianDetailResponse> call, Response<KematianDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Ajuan kematian berhasil ditolak.", Snackbar.LENGTH_LONG).show();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_LONG).show();
                }
                kematianApproveProgress.setVisibility(View.GONE);
                approveKematian.setVisibility(GONE);
                showTolakAjuanCardButton.setVisibility(GONE);
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KEMATIAN_DETAIL_KEY, -1));

            }

            @Override
            public void onFailure(Call<KematianDetailResponse> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void prosesKematian(String token, int id) {
        kematianApproveProgress.setVisibility(View.VISIBLE);
        tolakAjuanCard.setVisibility(GONE);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KematianDetailResponse> kelahiranTolakCall = getData.kematianProses("Bearer " + token, id);
        kelahiranTolakCall.enqueue(new Callback<KematianDetailResponse>() {
            @Override
            public void onResponse(Call<KematianDetailResponse> call, Response<KematianDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Ajuan telah diterima.", Snackbar.LENGTH_LONG).show();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_LONG).show();
                }
                kematianApproveProgress.setVisibility(View.GONE);
                approveKematian.setVisibility(View.VISIBLE);
                showTolakAjuanCardButton.setVisibility(View.VISIBLE);
                approveKematian.setText("Sahkan");
                approveKematian.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MaterialAlertDialogBuilder(KematianAjuanDetailActivity.this)
                                .setTitle("Pengesahan Ajuan")
                                .setMessage("Yakin ingin mengesahkan ajuan kematian?")
                                .setPositiveButton("Sahkan Ajuan", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        approveKematian(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KEMATIAN_DETAIL_KEY, -1));
                                    }
                                })
                                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .show();
                    }
                });
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KEMATIAN_DETAIL_KEY, -1));
            }

            @Override
            public void onFailure(Call<KematianDetailResponse> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void setFailedContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(View.VISIBLE);
        kematianContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        loadingContainer.setVisibility(View.VISIBLE);
        failedContainer.setVisibility(GONE);
        kematianContainer.setVisibility(GONE);
    }

    public void setKramaContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(GONE);
        kematianContainer.setVisibility(View.VISIBLE);
    }
}