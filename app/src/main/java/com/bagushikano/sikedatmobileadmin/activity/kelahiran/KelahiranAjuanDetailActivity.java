package com.bagushikano.sikedatmobileadmin.activity.kelahiran;

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
import com.bagushikano.sikedatmobileadmin.activity.kematian.KematianAjuanDetailActivity;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.kelahiran.KelahiranAjuanDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.kelahiran.KelahiranApproveResponse;
import com.bagushikano.sikedatmobileadmin.model.kelahiran.KelahiranDetailResponse;
import com.bagushikano.sikedatmobileadmin.util.ChangeDateTimeFormat;
import com.bagushikano.sikedatmobileadmin.util.DownloadUtil;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KelahiranAjuanDetailActivity extends AppCompatActivity {

    TextView statusAjuanKelahiran, tanggalAjuanKelahiran,
            noAktaKelahiran, namaAnak, tanggalLahirAnak, kelahiranTolakText, kelahiranKeteranganText, kematianPengajuText;
    Button showAktaKelahiranButton, showDetailAnakButton, showTolakAjuanCardButton,
            tolakAjuanCancelButton, submitTolakButton;
    MaterialCardView tolakAjuanCard, tolakAlasanCard;

    TextInputEditText alasanTolakField;

    private final String KELAHIRAN_DETAIL_KEY = "KELAHIRAN_DETAIL_KEY";
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    LinearLayout loadingContainer, failedContainer, kelahiranEmptyContainer;
    SwipeRefreshLayout kelahiranContainer;
    Button refreshKelahiran;
    SharedPreferences loginPreferences;
    private Toolbar homeToolbar;

    private Button approveKelahiran;
    LinearLayout kelahiranApproveProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelahiran_ajuan_detail);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        loadingContainer = findViewById(R.id.kelahiran_loading_container);
        failedContainer = findViewById(R.id.kelahiran_failed_container);
        kelahiranContainer = findViewById(R.id.kelahiran_container);
        refreshKelahiran = findViewById(R.id.kelahiran_refresh);

        approveKelahiran = findViewById(R.id.approve_kelahiran_button);
        kelahiranApproveProgress = findViewById(R.id.approve_progress_kelahiran);
        showTolakAjuanCardButton = findViewById(R.id.tolak_ajuan_kelahiran_show_button);
        tolakAjuanCard = findViewById(R.id.tolak_ajuan_kelahiran_card);
        tolakAjuanCancelButton = findViewById(R.id.kelahiran_ajuan_tolak_cancel_button);

        statusAjuanKelahiran = findViewById(R.id.kelahiran_ajuan_status);
        tanggalAjuanKelahiran = findViewById(R.id.kelahiran_ajuan_tanggal);
        noAktaKelahiran = findViewById(R.id.kelahiran_ajuan_no_akta_kelahiran);
        namaAnak = findViewById(R.id.kelahiran_ajuan_nama_anak);
        tanggalLahirAnak = findViewById(R.id.kelahiran_ajuan_tanggal_lahir_anak);
        showAktaKelahiranButton = findViewById(R.id.kelahiran_ajuan_akta_kelahiran_show);
        showDetailAnakButton = findViewById(R.id.kelahiran_ajuan_detail_anak_button);
        kelahiranKeteranganText = findViewById(R.id.kelahiran_ajuan_keterangan);
        kematianPengajuText = findViewById(R.id.kematian_ajuan_pengaju);

        kelahiranTolakText = findViewById(R.id.kelahiran_ajuan_tolak_text);
        tolakAlasanCard = findViewById(R.id.kelahiran_ajuan_alasan_tolak_card);
        alasanTolakField = findViewById(R.id.kelahiran_ajuan_tolak_field);
        submitTolakButton = findViewById(R.id.submit_kelahiran_ajuan_tolak_button);
        submitTolakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alasanTolakField.getText().toString().length() == 0) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Alasan tolak ajuan tidak boleh kosong.", Snackbar.LENGTH_LONG).show();
                }
                else {
                    new MaterialAlertDialogBuilder(KelahiranAjuanDetailActivity.this)
                            .setTitle("Penolakan Ajuan")
                            .setMessage("Yakin ingin menolak ajuan kelahiran?")
                            .setPositiveButton("Tolak Ajuan", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    tolakKelahiran(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KELAHIRAN_DETAIL_KEY, -1));
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

        showTolakAjuanCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tolakAjuanCard.setVisibility(View.VISIBLE);
                approveKelahiran.setVisibility(GONE);
                showTolakAjuanCardButton.setVisibility(GONE);
            }
        });

        tolakAjuanCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tolakAjuanCard.setVisibility(GONE);
                approveKelahiran.setVisibility(View.VISIBLE);
                showTolakAjuanCardButton.setVisibility(View.VISIBLE);
            }
        });

        approveKelahiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approveKelahiran(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KELAHIRAN_DETAIL_KEY, -1));
            }
        });

        refreshKelahiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KELAHIRAN_DETAIL_KEY, -1));
            }
        });

        kelahiranContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KELAHIRAN_DETAIL_KEY, -1));
            }
        });
        getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KELAHIRAN_DETAIL_KEY, -1));

    }

    public void getDetail(String token, int id) {
        setLoadingContainerVisible();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KelahiranAjuanDetailResponse> kelahiranDetailResponseCall = getData.getKelahiranAjuanDetail("Bearer " + token, id);
        kelahiranDetailResponseCall.enqueue(new Callback<KelahiranAjuanDetailResponse>() {
            @Override
            public void onResponse(Call<KelahiranAjuanDetailResponse> call, Response<KelahiranAjuanDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    if (response.body().getKelahiranAjuan().getStatus() == 3) {
                        statusAjuanKelahiran.setText("Sah");
                        approveKelahiran.setVisibility(GONE);
                        showTolakAjuanCardButton.setVisibility(GONE);

                    } else if (response.body().getKelahiranAjuan().getStatus() == 1) {
                        statusAjuanKelahiran.setText("Ajuan sedang dalam proses");
                        approveKelahiran.setVisibility(View.VISIBLE);
                        showTolakAjuanCardButton.setVisibility(View.VISIBLE);
                        approveKelahiran.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new MaterialAlertDialogBuilder(KelahiranAjuanDetailActivity.this)
                                        .setTitle("Pengesahan Ajuan")
                                        .setMessage("Yakin ingin mengesahkan ajuan kelahiran?")
                                        .setPositiveButton("Sahkan Ajuan", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                approveKelahiran(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KELAHIRAN_DETAIL_KEY, -1));
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

                    } else if (response.body().getKelahiranAjuan().getStatus() == 2) {
                        statusAjuanKelahiran.setText("Ajuan ditolak");
                        approveKelahiran.setVisibility(GONE);
                        showTolakAjuanCardButton.setVisibility(GONE);
                        tolakAlasanCard.setVisibility(View.VISIBLE);
                        kelahiranTolakText.setText(response.body().getKelahiranAjuan().getAlasanTolakAjuan().toString());
                    } else if (response.body().getKelahiranAjuan().getStatus() == 0) {
                        statusAjuanKelahiran.setText("Menunggu untuk diproses");
                        showTolakAjuanCardButton.setVisibility(GONE);
                        approveKelahiran.setVisibility(View.VISIBLE);
                        showTolakAjuanCardButton.setVisibility(GONE);

                        approveKelahiran.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                prosesKelahiran(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KELAHIRAN_DETAIL_KEY, -1));
                            }
                        });
                        approveKelahiran.setText("Terima ajuan");
                    }


                    if (response.body().getKelahiranAjuan().getKeterangan() != null) {
                        kelahiranKeteranganText.setText(response.body().getKelahiranAjuan().getKeterangan());
                    }

                    kematianPengajuText.setText(response.body().getPenduduk().getNama());

                namaAnak.setText(response.body().getKelahiranAjuan().getCacahKramaMipil().getPenduduk().getNama());
                    if (response.body().getKelahiranAjuan().getNomorAktaKelahiran() != null) {
                        noAktaKelahiran.setText(response.body().getKelahiranAjuan().getNomorAktaKelahiran());
                    }
                    else {
                        noAktaKelahiran.setText("-");
                    }
                    if (response.body().getKelahiranAjuan().getFileAktaKelahiran() != null ) {
                        showAktaKelahiranButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DownloadUtil downloadUtil = new DownloadUtil(getApplicationContext());
                                Long download =   downloadUtil.downloadFile(
                                        getResources().getString(R.string.api_endpoint) + response.body().getKelahiranAjuan().getFileAktaKelahiran());
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
                        showAktaKelahiranButton.setVisibility(GONE);
                    }

                    tanggalLahirAnak.setText(ChangeDateTimeFormat.changeDateFormat(
                            response.body().getKelahiranAjuan().getCacahKramaMipil().getPenduduk().getTanggalLahir())
                    );

                    tanggalAjuanKelahiran.setText(ChangeDateTimeFormat.changeDateTimeFormatForCreatedAt(
                            response.body().getKelahiranAjuan().getCreatedAt())
                    );


                    showDetailAnakButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                            kramaDetail.putExtra(KRAMA_DETAIL_KEY, response.body().getKelahiranAjuan().getCacahKramaMipil().getId());
                            startActivity(kramaDetail);
                        }
                    });

                    setKramaContainerVisible();
                }
                else {
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                kelahiranContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<KelahiranAjuanDetailResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void approveKelahiran(String token, int id) {
        kelahiranApproveProgress.setVisibility(View.VISIBLE);
        approveKelahiran.setVisibility(View.GONE);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KelahiranApproveResponse> kelahiranApproveResponseCall = getData.kelahiranApprove("Bearer " + token, id);
        kelahiranApproveResponseCall.enqueue(new Callback<KelahiranApproveResponse>() {
            @Override
            public void onResponse(Call<KelahiranApproveResponse> call, Response<KelahiranApproveResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Ajuan kelahiran telah disahkan", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_LONG).show();
                }
                kelahiranApproveProgress.setVisibility(View.GONE);
                approveKelahiran.setVisibility(GONE);
                showTolakAjuanCardButton.setVisibility(GONE);
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KELAHIRAN_DETAIL_KEY, -1));
            }

            @Override
            public void onFailure(Call<KelahiranApproveResponse> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void tolakKelahiran(String token, int id) {
        kelahiranApproveProgress.setVisibility(View.VISIBLE);
        tolakAjuanCard.setVisibility(GONE);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KelahiranDetailResponse> kelahiranTolakCall = getData.kelahiranTolak("Bearer " + token, id, alasanTolakField.getText().toString());
        kelahiranTolakCall.enqueue(new Callback<KelahiranDetailResponse>() {
            @Override
            public void onResponse(Call<KelahiranDetailResponse> call, Response<KelahiranDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Ajuan kelahiran berhasil ditolak.", Snackbar.LENGTH_LONG).show();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_LONG).show();
                }
                kelahiranApproveProgress.setVisibility(View.GONE);
                approveKelahiran.setVisibility(GONE);
                showTolakAjuanCardButton.setVisibility(GONE);
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KELAHIRAN_DETAIL_KEY, -1));
            }

            @Override
            public void onFailure(Call<KelahiranDetailResponse> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void prosesKelahiran(String token, int id) {
        kelahiranApproveProgress.setVisibility(View.VISIBLE);
        tolakAjuanCard.setVisibility(GONE);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KelahiranDetailResponse> kelahiranTolakCall = getData.kelahiranProses("Bearer " + token, id);
        kelahiranTolakCall.enqueue(new Callback<KelahiranDetailResponse>() {
            @Override
            public void onResponse(Call<KelahiranDetailResponse> call, Response<KelahiranDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Ajuan telah diterima.", Snackbar.LENGTH_LONG).show();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_LONG).show();
                }

                approveKelahiran.setVisibility(View.VISIBLE);
                showTolakAjuanCardButton.setVisibility(View.VISIBLE);
                kelahiranApproveProgress.setVisibility(View.GONE);
                approveKelahiran.setText("Sahkan");
                approveKelahiran.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MaterialAlertDialogBuilder(KelahiranAjuanDetailActivity.this)
                                .setTitle("Pengesahan Ajuan")
                                .setMessage("Yakin ingin mengesahkan ajuan kelahiran?")
                                .setPositiveButton("Sahkan Ajuan", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        approveKelahiran(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KELAHIRAN_DETAIL_KEY, -1));
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
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KELAHIRAN_DETAIL_KEY, -1));
            }

            @Override
            public void onFailure(Call<KelahiranDetailResponse> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void setFailedContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(View.VISIBLE);
        kelahiranContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        loadingContainer.setVisibility(View.VISIBLE);
        failedContainer.setVisibility(GONE);
        kelahiranContainer.setVisibility(GONE);
    }

    public void setKramaContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(GONE);
        kelahiranContainer.setVisibility(View.VISIBLE);
    }
}