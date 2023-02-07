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
import com.bagushikano.sikedatmobileadmin.adapter.banjar.BanjarDinasListAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarDinas;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarDinasGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarDinas;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BanjarDinasDaftarActivity extends AppCompatActivity {

    RecyclerView banjarDinasList;
    ArrayList<BanjarDinas> banjarDinasArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    BanjarDinasListAdapter banjarDinasListAdapter;

    LinearLayout loadingContainer, failedContainer, banjarDinasEmptyContainer;
    SwipeRefreshLayout banjarDinasContainer;
    Button refreshBanjarDinas;
    SharedPreferences loginPreferences;
    private Toolbar homeToolbar;

    TextInputEditText searchBanjarDinasField;

    TextView banjarDinasAllDataLoadedTextView;
    int currentPage;
    int nextPage;
    int lastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banjar_dinas_list);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        loadingContainer = findViewById(R.id.banjar_dinas_loading_container);
        failedContainer = findViewById(R.id.banjar_dinas_failed_container);
        banjarDinasContainer = findViewById(R.id.banjar_dinas_container);
        refreshBanjarDinas = findViewById(R.id.banjar_dinas_refresh);
        banjarDinasList = findViewById(R.id.banjar_dinas_list);
        searchBanjarDinasField = findViewById(R.id.banjar_dinas_search_field);
        banjarDinasAllDataLoadedTextView = findViewById(R.id.all_data_loaded_banjar_dinas_text);
        banjarDinasEmptyContainer = findViewById(R.id.banjar_dinas_empty_container);

        linearLayoutManager = new LinearLayoutManager(this);
        banjarDinasListAdapter = new BanjarDinasListAdapter(this, banjarDinasArrayList);
        banjarDinasList.setLayoutManager(linearLayoutManager);
        banjarDinasList.setAdapter(banjarDinasListAdapter);


        refreshBanjarDinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(loginPreferences.getString("token", "empty"));
            }
        });

        banjarDinasContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(loginPreferences.getString("token", "empty"));
            }
        });

        searchBanjarDinasField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    searchKrama(searchBanjarDinasField.getText().toString(), loginPreferences.getString("token", "empty"));
                    return true;
                }
                return false;
            }
        });

        getData(loginPreferences.getString("token", "empty"));
    }
    public void getData(String token) {
        setLoadingContainerVisible();
        banjarDinasContainer.setRefreshing(true);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<BanjarDinasGetResponse> banjarDinasGetResponseCall = getData.getBanjarDinas("Bearer " + token);
        banjarDinasGetResponseCall.enqueue(new Callback<BanjarDinasGetResponse>() {
            @Override
            public void onResponse(Call<BanjarDinasGetResponse> call, Response<BanjarDinasGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    banjarDinasArrayList.clear();
                    banjarDinasArrayList.addAll(response.body().getBanjarDinasPaginate().getBanjarDinasList());
                    banjarDinasListAdapter.notifyDataSetChanged();
                    if (banjarDinasArrayList.size() == 0) {
                        banjarDinasEmptyContainer.setVisibility(View.VISIBLE);
                    }
                    else {
                        banjarDinasEmptyContainer.setVisibility(GONE);
                        currentPage = response.body().getBanjarDinasPaginate().getCurrentPage();
                        lastPage = response.body().getBanjarDinasPaginate().getLastPage();
                        if (currentPage != lastPage) {
                            //TODO add automatic handler if data is need to loaded
                            nextPage = currentPage+1;
                        }
                        else {
                            banjarDinasAllDataLoadedTextView.setVisibility(View.VISIBLE);
                        }
                    }
                    setBanjarDinasContainerVisible();
                }
                else {
                    setFailedContainerVisible();
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
                banjarDinasContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<BanjarDinasGetResponse> call, Throwable t) {
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
        banjarDinasContainer.setVisibility(GONE);
    }

    public void setLoadingContainerVisible() {
        loadingContainer.setVisibility(View.VISIBLE);
        failedContainer.setVisibility(GONE);
        banjarDinasContainer.setVisibility(GONE);
    }

    public void setBanjarDinasContainerVisible() {
        loadingContainer.setVisibility(GONE);
        failedContainer.setVisibility(GONE);
        banjarDinasContainer.setVisibility(View.VISIBLE);
    }
}