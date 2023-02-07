package com.bagushikano.sikedatmobileadmin.activity.kelahiran;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.kelahiran.KelahiranAjuanDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.kelahiran.KelahiranApproveResponse;
import com.bagushikano.sikedatmobileadmin.model.kelahiran.KelahiranDetailResponse;
import com.bagushikano.sikedatmobileadmin.util.ChangeDateTimeFormat;
import com.bagushikano.sikedatmobileadmin.util.DownloadUtil;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KelahiranDetailActivity extends AppCompatActivity {


    TextView statusAjuanKelahiran, tanggalAjuanKelahiran,
            noAktaKelahiran, namaAnak, tanggalLahirAnak, kelahiranTolakText, kelahiranKeteranganText;
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
        setContentView(R.layout.activity_kelahiran_detail);

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
        Call<KelahiranDetailResponse> kelahiranDetailResponseCall = getData.getKelahiranDetail("Bearer " + token, id);
        kelahiranDetailResponseCall.enqueue(new Callback<KelahiranDetailResponse>() {
            @Override
            public void onResponse(Call<KelahiranDetailResponse> call, Response<KelahiranDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    if (response.body().getKelahiran().getStatus() == 1) {
                        statusAjuanKelahiran.setText("Sah");
                        approveKelahiran.setVisibility(GONE);
                    } else if (response.body().getKelahiran().getStatus() == 0) {
                        statusAjuanKelahiran.setText("Draft");
                    }

                    if (response.body().getKelahiran().getKeterangan() != null) {
                        kelahiranKeteranganText.setText(response.body().getKelahiran().getKeterangan());
                    }

                    namaAnak.setText(response.body().getKelahiran().getCacahKramaMipil().getPenduduk().getNama());
                    if (response.body().getKelahiran().getNomorAktaKelahiran() != null) {
                        noAktaKelahiran.setText(response.body().getKelahiran().getNomorAktaKelahiran());
                    }
                    else {
                        noAktaKelahiran.setText("-");
                    }
                    if (response.body().getKelahiran().getFileAktaKelahiran() != null ) {
                        showAktaKelahiranButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DownloadUtil downloadUtil = new DownloadUtil(getApplicationContext());
                                Long download =   downloadUtil.downloadFile(
                                        getResources().getString(R.string.api_endpoint) + response.body().getKelahiran().getFileAktaKelahiran());
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
                            response.body().getKelahiran().getCacahKramaMipil().getPenduduk().getTanggalLahir())
                    );

                    tanggalAjuanKelahiran.setText(ChangeDateTimeFormat.changeDateTimeFormatForCreatedAt(
                            response.body().getKelahiran().getCreatedAt())
                    );


                    showDetailAnakButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                            kramaDetail.putExtra(KRAMA_DETAIL_KEY, response.body().getKelahiran().getCacahKramaMipil().getId());
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
            public void onFailure(Call<KelahiranDetailResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void approveKelahiran(String token, int id) {

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