package com.bagushikano.sikedatmobileadmin.activity.perceraian.pendataan.kramaselect;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.adapter.maperas.MaperasKramaMipilListAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipilGetResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerceraianSelectKramaTujuanActivity extends AppCompatActivity {


    RecyclerView kramaMipilList;
    ArrayList<KramaMipil> kramaMipilArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    MaperasKramaMipilListAdapter kramaMipilListAdapter;

    LinearLayout loadingContainer, failedContainer, kramaEmptyContainer;
    SwipeRefreshLayout kramaContainer;
    Button refreshKrama;
    SharedPreferences loginPreferences;
    private Toolbar homeToolbar;

    TextInputEditText searchKramaField;

    TextView kramaAllDataLoadedTextView;
    int currentPage;
    int nextPage;
    int lastPage;


    Integer idKrama = null, idBanjar = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perceraian_select_krama_tujuan);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        loadingContainer = findViewById(R.id.krama_loading_container);
        failedContainer = findViewById(R.id.krama_failed_container);
        kramaContainer = findViewById(R.id.krama_container);
        refreshKrama = findViewById(R.id.krama_refresh);
        kramaMipilList = findViewById(R.id.krama_list);
        searchKramaField = findViewById(R.id.krama_search_field);
        kramaAllDataLoadedTextView = findViewById(R.id.all_data_loaded_krama_text);
        kramaEmptyContainer = findViewById(R.id.krama_empty_container);

        linearLayoutManager = new LinearLayoutManager(this);
        kramaMipilListAdapter = new MaperasKramaMipilListAdapter(this, kramaMipilArrayList);
        kramaMipilList.setLayoutManager(linearLayoutManager);
        kramaMipilList.setAdapter(kramaMipilListAdapter);



        if (getIntent().hasExtra("KRAMA")) {
            idKrama = getIntent().getIntExtra("KRAMA", -1);
        }
        if (getIntent().hasExtra("BANJAR")) {
            idBanjar = getIntent().getIntExtra("BANJAR", -1);
        }

        refreshKrama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(loginPreferences.getString("token", "empty"), idKrama, idBanjar);
            }
        });

        kramaContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(loginPreferences.getString("token", "empty"), idKrama, idBanjar);
            }
        });

        searchKramaField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    searchKrama(searchKramaField.getText().toString(), loginPreferences.getString("token", "empty"));
                    return true;
                }
                return false;
            }
        });


        getData(loginPreferences.getString("token", "empty"), idKrama, idBanjar);
    }

    public void getData(String token, Integer kramaId, Integer banjarId) {
        setLoadingContainerVisible();
        kramaContainer.setRefreshing(true);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KramaMipilGetResponse> kramaMipilGetResponseCall = getData.perceraianGetListKramaTujuan("Bearer " + token, kramaId, banjarId);
        kramaMipilGetResponseCall.enqueue(new Callback<KramaMipilGetResponse>() {
            @Override
            public void onResponse(Call<KramaMipilGetResponse> call, Response<KramaMipilGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    kramaMipilArrayList.clear();
                    kramaMipilArrayList.addAll(response.body().getKramaMipilPaginate().getKramaMipilList());
                    kramaMipilListAdapter.notifyDataSetChanged();
                    if (kramaMipilArrayList.size() == 0) {
                        kramaEmptyContainer.setVisibility(View.VISIBLE);
                    }
                    else {
                        kramaEmptyContainer.setVisibility(GONE);
                        currentPage = response.body().getKramaMipilPaginate().getCurrentPage();
                        lastPage = response.body().getKramaMipilPaginate().getLastPage();
                        if (currentPage != lastPage) {
                            //TODO add automatic handler if data is need to loaded
                            nextPage = currentPage+1;
                        }
                        else {
                            kramaAllDataLoadedTextView.setVisibility(View.VISIBLE);
                        }
                    }
                    setKramaContainerVisible();
                }
                else {
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                kramaContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<KramaMipilGetResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void searchKrama(String q, String token) {
//        setLoadingContainerVisible();
//        ApiInterface getData = RetrofitClient.buildRetrofit().create(ApiInterface.class);
//        Call<MemberResponse> memberResponseCall = getData.searchMember("Bearer " + token, q);
//        memberResponseCall.enqueue(new Callback<MemberResponse>() {
//            @Override
//            public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {
//                if (response.code() == 200 && response.body().getStatus().equals("success")) {
//                    memberArrayList.clear();
//                    memberArrayList.addAll(response.body().getMemberList());
//                    memberListAdapter.notifyDataSetChanged();
//                    setMemberContainerVisible();
//                }
//                else {
//                    setFailedContainerVisible();
//                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MemberResponse> call, Throwable t) {
//                setFailedContainerVisible();
//                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
//            }
//        });
    }


    public void setFailedContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(View.VISIBLE);
        kramaContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        loadingContainer.setVisibility(View.VISIBLE);
        failedContainer.setVisibility(GONE);
        kramaContainer.setVisibility(GONE);
    }

    public void setKramaContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(GONE);
        kramaContainer.setVisibility(View.VISIBLE);
    }
}