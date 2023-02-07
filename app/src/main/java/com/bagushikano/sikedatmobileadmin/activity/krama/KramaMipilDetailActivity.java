package com.bagushikano.sikedatmobileadmin.activity.krama;

import static android.view.View.GONE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaMipilDetailActivity;
import com.bagushikano.sikedatmobileadmin.adapter.krama.AnggotaKramaListAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaMipilGetResponse;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipilDetailResponse;
import com.bagushikano.sikedatmobileadmin.util.ChangeDateTimeFormat;
import com.bagushikano.sikedatmobileadmin.util.StringFormatter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KramaMipilDetailActivity extends AppCompatActivity {

    LinearLayout kramaMipilDetailLoadingContainer, kramaMipilFailedContainer,
            kramaMipilAnggotaFailed, kramaMipilAnggotaLoadingContainer;

    SwipeRefreshLayout kramaMipilContainer;
    TextView kramaMipilNoText, kramaMipilNamaText, kramaMipilBanjarAdatText, kramaMipilRegistrasiDateText, kedudukanText, jenisText;
    RecyclerView kramaMipilAnggotaList;
    ArrayList<AnggotaKramaMipil> anggotaKramaMipilArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    AnggotaKramaListAdapter anggotaKramaListAdapter;

    private Toolbar homeToolbar;
    SharedPreferences loginPreferences;
    Button kramaMipilDetailRefresh, kramaMipilAnggotaRefresh;
    Button kramaMipilCacahDetailButton;
    private final String KRAMA_DETAIL_KEY = "KRAMA_MIPIL_DETAIL_KEY";

    private Button kartuKramaMipilButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_krama_mipil_detail);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        kramaMipilDetailLoadingContainer = findViewById(R.id.krama_mipil_detail_loading_container);
        kramaMipilFailedContainer = findViewById(R.id.krama_mipil_detail_failed_container);
        kramaMipilAnggotaFailed = findViewById(R.id.krama_mipil_anggota_failed_container);
        kramaMipilAnggotaLoadingContainer = findViewById(R.id.krama_mipil_angggota_loading_container);

        kramaMipilAnggotaList = findViewById(R.id.krama_mipil_anggota_list);
        linearLayoutManager = new LinearLayoutManager(this);
        anggotaKramaListAdapter = new AnggotaKramaListAdapter(this, anggotaKramaMipilArrayList);
        kramaMipilAnggotaList.setLayoutManager(linearLayoutManager);
        kramaMipilAnggotaList.setAdapter(anggotaKramaListAdapter);
        kartuKramaMipilButton = findViewById(R.id.kartu_krama_mipil_button);

        kramaMipilAnggotaRefresh = findViewById(R.id.krama_mipil_anggota_refresh);
        kramaMipilAnggotaRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAnggotaKrama(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KRAMA_DETAIL_KEY, -1));
            }
        });

        kramaMipilNoText = findViewById(R.id.krama_mipil_no_text);
        kramaMipilNamaText = findViewById(R.id.krama_mipil_name_text);
        kramaMipilBanjarAdatText = findViewById(R.id.krama_mipil_banjar_adat_text);
        kramaMipilRegistrasiDateText = findViewById(R.id.krama_mipil_registrasi_date_text);
        kedudukanText = findViewById(R.id.krama_mipil_kedudukan_text);
        jenisText = findViewById(R.id.krama_mipil_jenis_text);

        kramaMipilCacahDetailButton = findViewById(R.id.krama_mipil_cacah_detail_button);

        kramaMipilContainer = findViewById(R.id.krama_mipil_detail_container);
        kramaMipilDetailRefresh = findViewById(R.id.krama_mipil_detail_refresh);

        kramaMipilDetailRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KRAMA_DETAIL_KEY, -1));
                getAnggotaKrama(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KRAMA_DETAIL_KEY, -1));
            }
        });

        kramaMipilContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KRAMA_DETAIL_KEY, -1));
                getAnggotaKrama(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KRAMA_DETAIL_KEY, -1));
            }
        });
        getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KRAMA_DETAIL_KEY, -1));
        getAnggotaKrama(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KRAMA_DETAIL_KEY, -1));
    }

    public void getAnggotaKrama(String token, int id) {
        kramaMipilAnggotaLoadingContainer.setVisibility(View.VISIBLE);
        kramaMipilAnggotaFailed.setVisibility(GONE);
        kramaMipilAnggotaList.setVisibility(GONE);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<AnggotaKramaMipilGetResponse> anggotaKramaMipilGetResponseCall = getData.getKramaMipilDetail("Bearer " + token, id);
        anggotaKramaMipilGetResponseCall.enqueue(new Callback<AnggotaKramaMipilGetResponse>() {
            @Override
            public void onResponse(Call<AnggotaKramaMipilGetResponse> call, Response<AnggotaKramaMipilGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    anggotaKramaMipilArrayList.clear();
                    anggotaKramaMipilArrayList.addAll(response.body().getAnggotaKramaMipilList());
//                    anggotaKramaListAdapter.notifyDataSetChanged();
//                    if (kramaMipilArrayList.size() == 0) {
//                        kramaEmptyContainer.setVisibility(View.VISIBLE);
//                    }
                    kramaMipilAnggotaLoadingContainer.setVisibility(GONE);
                    kramaMipilAnggotaFailed.setVisibility(GONE);
                    kramaMipilAnggotaList.setVisibility(View.VISIBLE);
                }
                else {
                    kramaMipilAnggotaLoadingContainer.setVisibility(GONE);
                    kramaMipilAnggotaFailed.setVisibility(View.VISIBLE);
                    kramaMipilAnggotaList.setVisibility(GONE);
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AnggotaKramaMipilGetResponse> call, Throwable t) {
                kramaMipilAnggotaLoadingContainer.setVisibility(GONE);
                kramaMipilAnggotaFailed.setVisibility(View.VISIBLE);
                kramaMipilAnggotaList.setVisibility(GONE);
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void getDetail(String token, int id) {
        setLoadingContainerVisible();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KramaMipilDetailResponse> kramaMipilDetailResponseCall = getData.getDetailKramaMipil("Bearer " + token, id);
        kramaMipilDetailResponseCall.enqueue(new Callback<KramaMipilDetailResponse>() {
            @Override
            public void onResponse(Call<KramaMipilDetailResponse> call, Response<KramaMipilDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    if (response.body().getKramaMipil().getKedudukanKramaMipil().equals("purusa")) {
                        kedudukanText.setText("Purusa");
                    } else {
                        kedudukanText.setText("Pradana");
                    }
                    if (response.body().getKramaMipil().getJenisKramaMipil().equals("krama_penuh")) {
                        jenisText.setText("Krama Penuh");
                    } else {
                        jenisText.setText("Krama Balu");
                    }
                    kramaMipilNamaText.setText(StringFormatter.formatNamaWithGelar(response.body().getKramaMipil().getCacahKramaMipil().getPenduduk().getNama(),
                            response.body().getKramaMipil().getCacahKramaMipil().getPenduduk().getGelarDepan(), response.body().getKramaMipil().getCacahKramaMipil().getPenduduk().getGelarBelakang()));
                    kramaMipilNoText.setText(response.body().getKramaMipil().getNomorKramaMipil());
                    kramaMipilBanjarAdatText.setText(response.body().getKramaMipil().getBanjarAdat().getNamaBanjarAdat());
                    kramaMipilRegistrasiDateText.setText(ChangeDateTimeFormat.changeDateFormat(response.body().getKramaMipil().getTanggalRegistrasi()));

                    kramaMipilCacahDetailButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kramaDetail = new Intent(getApplicationContext(), CacahKramaMipilDetailActivity.class);
                            kramaDetail.putExtra("KRAMA_DETAIL_KEY", response.body().getKramaMipil().getCacahKramaMipil().getId());
                            startActivity(kramaDetail);
                        }
                    });

                    kartuKramaMipilButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kartuKramaMipilIntent = new Intent(getApplicationContext(), KramaMipilKartuActivity.class);
                            kartuKramaMipilIntent.putExtra("KRAMA_KEY", response.body().getKramaMipil().getId());
                            kartuKramaMipilIntent.putExtra("KRAMA_NO_KEY", response.body().getKramaMipil().getNomorKramaMipil());
                            startActivity(kartuKramaMipilIntent);
                        }
                    });

                    setKramaContainerVisible();
                }
                else {
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                kramaMipilContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<KramaMipilDetailResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void setFailedContainerVisible() {
        kramaMipilDetailLoadingContainer.setVisibility(GONE);
        kramaMipilFailedContainer.setVisibility(View.VISIBLE);
        kramaMipilContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        kramaMipilDetailLoadingContainer.setVisibility(View.VISIBLE);
        kramaMipilFailedContainer.setVisibility(GONE);
        kramaMipilContainer.setVisibility(GONE);
    }

    public void setKramaContainerVisible() {
        kramaMipilDetailLoadingContainer.setVisibility(GONE);
        kramaMipilFailedContainer.setVisibility(GONE);
        kramaMipilContainer.setVisibility(View.VISIBLE);
    }
}