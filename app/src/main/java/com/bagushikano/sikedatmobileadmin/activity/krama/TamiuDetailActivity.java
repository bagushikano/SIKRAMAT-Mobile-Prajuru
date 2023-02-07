package com.bagushikano.sikedatmobileadmin.activity.krama;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahKramaTamiuDetailActivity;
import com.bagushikano.sikedatmobileadmin.activity.cacahkrama.CacahTamiuDetailActivity;
import com.bagushikano.sikedatmobileadmin.adapter.krama.AnggotaKramaTamiuListAdapter;
import com.bagushikano.sikedatmobileadmin.adapter.krama.AnggotaTamiuListAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaTamiu;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaTamiuGetResponse;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaTamiu;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaTamiuGetResponse;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaTamiuDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.krama.TamiuDetailResponse;
import com.bagushikano.sikedatmobileadmin.util.ChangeDateTimeFormat;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TamiuDetailActivity extends AppCompatActivity {

    LinearLayout kramaTamiuDetailLoadingContainer, kramaTamiuFailedContainer,
            kramaTamiuAnggotaFailed, kramaTamiuAnggotaLoadingContainer;

    SwipeRefreshLayout kramaTamiuContainer;
    TextView kramaTamiuNoText, kramaTamiuNamaText, kramaTamiuBanjarAdatText, kramaTamiuRegistrasiDateText;
    RecyclerView kramaTamiuAnggotaList;
    ArrayList<AnggotaTamiu> anggotaTamiuArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    AnggotaTamiuListAdapter anggotaTamiuListAdapter;

    private Toolbar homeToolbar;
    SharedPreferences loginPreferences;
    Button kramaTamiuDetailRefresh, kramaTamiuAnggotaRefresh;
    MaterialCardView kramaTamiuCacahDetailButton;
    private final String KRAMA_DETAIL_KEY = "KRAMA_DETAIL_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tamiu_detail);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        kramaTamiuDetailLoadingContainer = findViewById(R.id.krama_tamiu_detail_loading_container);
        kramaTamiuFailedContainer = findViewById(R.id.krama_tamiu_detail_failed_container);
        kramaTamiuAnggotaFailed = findViewById(R.id.krama_tamiu_anggota_failed_container);
        kramaTamiuAnggotaLoadingContainer = findViewById(R.id.krama_tamiu_angggota_loading_container);

        kramaTamiuAnggotaList = findViewById(R.id.krama_tamiu_anggota_list);
        linearLayoutManager = new LinearLayoutManager(this);
        anggotaTamiuListAdapter = new AnggotaTamiuListAdapter(this, anggotaTamiuArrayList);
        kramaTamiuAnggotaList.setLayoutManager(linearLayoutManager);
        kramaTamiuAnggotaList.setAdapter(anggotaTamiuListAdapter);

        kramaTamiuAnggotaRefresh = findViewById(R.id.krama_tamiu_anggota_refresh);
        kramaTamiuAnggotaRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAnggotaKrama(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KRAMA_DETAIL_KEY, -1));
            }
        });

        kramaTamiuNoText = findViewById(R.id.krama_tamiu_no_text);
        kramaTamiuNamaText = findViewById(R.id.krama_tamiu_name_text);
        kramaTamiuBanjarAdatText = findViewById(R.id.krama_tamiu_banjar_adat_text);
        kramaTamiuRegistrasiDateText = findViewById(R.id.krama_tamiu_registrasi_date_text);

        kramaTamiuCacahDetailButton = findViewById(R.id.krama_tamiu_cacah_detail_button);

        kramaTamiuContainer = findViewById(R.id.krama_tamiu_detail_container);
        kramaTamiuDetailRefresh = findViewById(R.id.krama_tamiu_detail_refresh);

        kramaTamiuDetailRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDetail(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KRAMA_DETAIL_KEY, -1));
                getAnggotaKrama(loginPreferences.getString("token", "empty"), getIntent().getIntExtra(KRAMA_DETAIL_KEY, -1));
            }
        });

        kramaTamiuContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        kramaTamiuAnggotaLoadingContainer.setVisibility(View.VISIBLE);
        kramaTamiuAnggotaFailed.setVisibility(GONE);
        kramaTamiuAnggotaList.setVisibility(GONE);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<AnggotaTamiuGetResponse> anggotaTamiuGetResponseCall = getData.getTamiuDetail("Bearer " + token, id);
        anggotaTamiuGetResponseCall.enqueue(new Callback<AnggotaTamiuGetResponse>() {
            @Override
            public void onResponse(Call<AnggotaTamiuGetResponse> call, Response<AnggotaTamiuGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    anggotaTamiuArrayList.clear();
                    anggotaTamiuArrayList.addAll(response.body().getAnggotaTamiuList());
//                    anggotaKramaListAdapter.notifyDataSetChanged();
//                    if (kramaTamiuArrayList.size() == 0) {
//                        kramaEmptyContainer.setVisibility(View.VISIBLE);
//                    }
                    kramaTamiuAnggotaLoadingContainer.setVisibility(GONE);
                    kramaTamiuAnggotaFailed.setVisibility(GONE);
                    kramaTamiuAnggotaList.setVisibility(View.VISIBLE);
                }
                else {
                    kramaTamiuAnggotaLoadingContainer.setVisibility(GONE);
                    kramaTamiuAnggotaFailed.setVisibility(View.VISIBLE);
                    kramaTamiuAnggotaList.setVisibility(GONE);
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AnggotaTamiuGetResponse> call, Throwable t) {
                kramaTamiuAnggotaLoadingContainer.setVisibility(GONE);
                kramaTamiuAnggotaFailed.setVisibility(View.VISIBLE);
                kramaTamiuAnggotaList.setVisibility(GONE);
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void getDetail(String token, int id) {
        setLoadingContainerVisible();
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<TamiuDetailResponse> tamiuDetailResponseCall = getData.getDetailTamiu("Bearer " + token, id);
        tamiuDetailResponseCall.enqueue(new Callback<TamiuDetailResponse>() {
            @Override
            public void onResponse(Call<TamiuDetailResponse> call, Response<TamiuDetailResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    String namaFormated = response.body().getTamiu().getCacahTamiu().getPenduduk().getNama();
                    if (response.body().getTamiu().getCacahTamiu().getPenduduk().getGelarDepan() != null) {
                        namaFormated = String.format("%s %s",
                                response.body().getTamiu().getCacahTamiu().getPenduduk().getGelarDepan(),
                                response.body().getTamiu().getCacahTamiu().getPenduduk().getNama()
                        );
                    }
                    if (response.body().getTamiu().getCacahTamiu().getPenduduk().getGelarBelakang() != null) {
                        namaFormated = String.format("%s %s",
                                namaFormated,
                                response.body().getTamiu().getCacahTamiu().getPenduduk().getGelarBelakang()
                        );
                    }
                    kramaTamiuNamaText.setText(namaFormated);
                    kramaTamiuNoText.setText(response.body().getTamiu().getNomorTamiu());
                    kramaTamiuBanjarAdatText.setText(response.body().getTamiu().getBanjarAdat().getNamaBanjarAdat());
                    kramaTamiuRegistrasiDateText.setText(ChangeDateTimeFormat.changeDateFormat(response.body().getTamiu().getTanggalRegistrasi()));

                    kramaTamiuCacahDetailButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kramaDetail = new Intent(getApplicationContext(), CacahTamiuDetailActivity.class);
                            kramaDetail.putExtra(KRAMA_DETAIL_KEY, response.body().getTamiu().getCacahTamiu().getId());
                            startActivity(kramaDetail);
                        }
                    });

                    setKramaContainerVisible();
                }
                else {
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                kramaTamiuContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<TamiuDetailResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void setFailedContainerVisible() {
        kramaTamiuDetailLoadingContainer.setVisibility(GONE);
        kramaTamiuFailedContainer.setVisibility(View.VISIBLE);
        kramaTamiuContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        kramaTamiuDetailLoadingContainer.setVisibility(View.VISIBLE);
        kramaTamiuFailedContainer.setVisibility(GONE);
        kramaTamiuContainer.setVisibility(GONE);
    }

    public void setKramaContainerVisible() {
        kramaTamiuDetailLoadingContainer.setVisibility(GONE);
        kramaTamiuFailedContainer.setVisibility(GONE);
        kramaTamiuContainer.setVisibility(View.VISIBLE);
    }
}