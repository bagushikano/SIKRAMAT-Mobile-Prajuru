package com.bagushikano.sikedatmobileadmin.activity;

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
import com.bagushikano.sikedatmobileadmin.adapter.banjar.BanjarAdatListAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarAdat;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarAdatGetResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BanjarAdatDaftarActivity extends AppCompatActivity {
    RecyclerView banjarAdatList;
    ArrayList<BanjarAdat> banjarAdatArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    BanjarAdatListAdapter banjarAdatListAdapter;

    LinearLayout loadingContainer, failedContainer, banjarAdatEmptyContainer;
    SwipeRefreshLayout banjarAdatContainer;
    Button refreshBanjarAdat;
    SharedPreferences loginPreferences;
    private Toolbar homeToolbar;

    TextInputEditText searchBanjarAdatField;

    TextView banjarAdatAllDataLoadedTextView;
    int currentPage;
    int nextPage;
    int lastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banjar_adat_list);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        loadingContainer = findViewById(R.id.banjar_adat_loading_container);
        failedContainer = findViewById(R.id.banjar_adat_failed_container);
        banjarAdatContainer = findViewById(R.id.banjar_adat_container);
        refreshBanjarAdat = findViewById(R.id.banjar_adat_refresh);
        banjarAdatList = findViewById(R.id.banjar_adat_list);
        searchBanjarAdatField = findViewById(R.id.banjar_adat_search_field);
        banjarAdatAllDataLoadedTextView = findViewById(R.id.all_data_loaded_banjar_adat_text);
        banjarAdatEmptyContainer = findViewById(R.id.banjar_adat_empty_container);

        linearLayoutManager = new LinearLayoutManager(this);
        banjarAdatListAdapter = new BanjarAdatListAdapter(this, banjarAdatArrayList);
        banjarAdatList.setLayoutManager(linearLayoutManager);
        banjarAdatList.setAdapter(banjarAdatListAdapter);


        refreshBanjarAdat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(loginPreferences.getString("token", "empty"));
            }
        });

        banjarAdatContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(loginPreferences.getString("token", "empty"));
            }
        });

        searchBanjarAdatField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    searchKrama(searchBanjarAdatField.getText().toString(), loginPreferences.getString("token", "empty"));
                    return true;
                }
                return false;
            }
        });

        getData(loginPreferences.getString("token", "empty"));
    }
    public void getData(String token) {
        setLoadingContainerVisible();
        banjarAdatContainer.setRefreshing(true);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<BanjarAdatGetResponse> banjarAdatGetResponseCall = getData.getBanjarAdat("Bearer " + token);
        banjarAdatGetResponseCall.enqueue(new Callback<BanjarAdatGetResponse>() {
            @Override
            public void onResponse(Call<BanjarAdatGetResponse> call, Response<BanjarAdatGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    banjarAdatArrayList.clear();
                    banjarAdatArrayList.addAll(response.body().getBanjarAdatPaginate().getBanjarAdatList());
                    banjarAdatListAdapter.notifyDataSetChanged();
                    if (banjarAdatArrayList.size() == 0) {
                        banjarAdatEmptyContainer.setVisibility(View.VISIBLE);
                    }
                    else {
                        banjarAdatEmptyContainer.setVisibility(GONE);
                        currentPage = response.body().getBanjarAdatPaginate().getCurrentPage();
                        lastPage = response.body().getBanjarAdatPaginate().getLastPage();
                        if (currentPage != lastPage) {
                            //TODO add automatic handler if data is need to loaded
                            nextPage = currentPage+1;
                        }
                        else {
                            banjarAdatAllDataLoadedTextView.setVisibility(View.VISIBLE);
                        }
                    }
                    setBanjarAdatContainerVisible();
                }
                else {
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                banjarAdatContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<BanjarAdatGetResponse> call, Throwable t) {
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
        banjarAdatContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        loadingContainer.setVisibility(View.VISIBLE);
        failedContainer.setVisibility(GONE);
        banjarAdatContainer.setVisibility(GONE);
    }

    public void setBanjarAdatContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(GONE);
        banjarAdatContainer.setVisibility(View.VISIBLE);
    }
}