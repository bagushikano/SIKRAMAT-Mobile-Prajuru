package com.bagushikano.sikedatmobileadmin.activity.perkawinan.pendataan.bedabanjar;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
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
import com.bagushikano.sikedatmobileadmin.adapter.perkawinan.PerkawinanListMempelaiAdapter;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipilGetResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerkawinanBedaBanjarPurusaSelectActivity extends AppCompatActivity {

    RecyclerView kramaMipilList;
    ArrayList<CacahKramaMipil> cacahKramaMipilArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    PerkawinanListMempelaiAdapter perkawinanListMempelaiAdapter;

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

    LinearLayout kramaLoadProgressContainer;
    NestedScrollView kramaNestedScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perkawinan_beda_banjar_purusa_select);

        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

        loadingContainer = findViewById(R.id.cacah_krama_loading_container);
        failedContainer = findViewById(R.id.cacah_krama_failed_container);
        kramaContainer = findViewById(R.id.cacah_krama_container);
        refreshKrama = findViewById(R.id.cacah_krama_refresh);
        kramaMipilList = findViewById(R.id.cacah_krama_list);
        searchKramaField = findViewById(R.id.cacah_krama_search_field);
        kramaAllDataLoadedTextView = findViewById(R.id.all_data_loaded_cacah_krama_text);
        kramaEmptyContainer = findViewById(R.id.cacah_krama_empty_container);
        kramaLoadProgressContainer = findViewById(R.id.krama_load_progress);
        kramaNestedScroll = findViewById(R.id.cacah_krama_nested_scroll);

        linearLayoutManager = new LinearLayoutManager(this);
        perkawinanListMempelaiAdapter = new PerkawinanListMempelaiAdapter(this, cacahKramaMipilArrayList, 0);
        kramaMipilList.setLayoutManager(linearLayoutManager);
        kramaMipilList.setAdapter(perkawinanListMempelaiAdapter);
        kramaMipilList.setNestedScrollingEnabled(true);


        refreshKrama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(loginPreferences.getString("token", "empty"), searchKramaField.getText().toString());
            }
        });

        kramaContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(loginPreferences.getString("token", "empty"), searchKramaField.getText().toString());
            }
        });

        searchKramaField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getData(loginPreferences.getString("token", "empty"), searchKramaField.getText().toString());
                    return true;
                }
                return false;
            }
        });

        getData(loginPreferences.getString("token", "empty"), searchKramaField.getText().toString());
    }

    public void getData(String token, String nama) {
        setLoadingContainerVisible();
        kramaContainer.setRefreshing(true);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<CacahKramaMipilGetResponse> cacahKramaMipilGetResponseCall = getData.getPurusa("Bearer " + token, nama);
        cacahKramaMipilGetResponseCall.enqueue(new Callback<CacahKramaMipilGetResponse>() {
            @Override
            public void onResponse(Call<CacahKramaMipilGetResponse> call, Response<CacahKramaMipilGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    cacahKramaMipilArrayList.clear();
                    cacahKramaMipilArrayList.addAll(response.body().getCacahKramaMipilPaginate().getCacahKramaMipilList());
                    perkawinanListMempelaiAdapter.notifyDataSetChanged();
                    if (cacahKramaMipilArrayList.size() == 0) {
                        kramaEmptyContainer.setVisibility(View.VISIBLE);
                    }
                    else {
                        kramaEmptyContainer.setVisibility(GONE);
                        currentPage = response.body().getCacahKramaMipilPaginate().getCurrentPage();
                        lastPage = response.body().getCacahKramaMipilPaginate().getLastPage();
                        if (currentPage != lastPage) {
                            nextPage = currentPage+1;
                            kramaNestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                                @Override
                                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                    if (!kramaNestedScroll.canScrollVertically(1)) {
                                        getNextData(token, nextPage, nama);
                                    }
                                }
                            });
                        }
                        else {
                            kramaAllDataLoadedTextView.setVisibility(View.VISIBLE); kramaNestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                                @Override
                                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                                }
                            });
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
            public void onFailure(Call<CacahKramaMipilGetResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void getNextData(String token, int page, String nama) {
        kramaLoadProgressContainer.setVisibility(View.VISIBLE);
        kramaContainer.setRefreshing(true);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<CacahKramaMipilGetResponse> cacahKramaMipilGetResponseCall = getData.getPurusaNextPage("Bearer " + token, page, nama);
        cacahKramaMipilGetResponseCall.enqueue(new Callback<CacahKramaMipilGetResponse>() {
            @Override
            public void onResponse(Call<CacahKramaMipilGetResponse> call, Response<CacahKramaMipilGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    cacahKramaMipilArrayList.addAll(response.body().getCacahKramaMipilPaginate().getCacahKramaMipilList());
                    currentPage = response.body().getCacahKramaMipilPaginate().getCurrentPage();
                    perkawinanListMempelaiAdapter.notifyItemInserted(cacahKramaMipilArrayList.size()-1);
                    kramaLoadProgressContainer.setVisibility(GONE);
                    if (currentPage != lastPage) {
                        nextPage++;
                    }
                    else {
                        kramaAllDataLoadedTextView.setVisibility(View.VISIBLE);
                        kramaNestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                            @Override
                            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                            }
                        });
                    }
                    kramaContainer.setRefreshing(false);
                }
                else {
                    kramaLoadProgressContainer.setVisibility(GONE);
                    kramaContainer.setRefreshing(false);
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CacahKramaMipilGetResponse> call, Throwable t) {
                setFailedContainerVisible();
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(-1);
        finish();
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