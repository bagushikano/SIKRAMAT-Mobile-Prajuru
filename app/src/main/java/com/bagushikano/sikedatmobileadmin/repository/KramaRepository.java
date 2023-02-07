package com.bagushikano.sikedatmobileadmin.repository;

import androidx.lifecycle.MutableLiveData;

import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.DashboardDataKramaResponse;
import com.bagushikano.sikedatmobileadmin.model.DashboardDataResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KramaRepository {
    private static KramaRepository kramaRepository;

    public static KramaRepository getInstance() {
        if (kramaRepository == null) {
            kramaRepository = new KramaRepository();
        }
        return kramaRepository;
    }

    private ApiRoute getData;

    public KramaRepository(){
        getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
    }

    public MutableLiveData<DashboardDataKramaResponse> getDashboardKramaData(String token) {
        MutableLiveData<DashboardDataKramaResponse> dashboardDataKramaResponseMutableLiveData = new MutableLiveData<>();
        Call<DashboardDataKramaResponse> dashboardDataKramaResponseCall = getData.getDashboardKramaData("Bearer " + token);
        dashboardDataKramaResponseCall.enqueue(new Callback<DashboardDataKramaResponse>() {
            @Override
            public void onResponse(Call<DashboardDataKramaResponse> call, Response<DashboardDataKramaResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    dashboardDataKramaResponseMutableLiveData.setValue(response.body());
                } else {
                    dashboardDataKramaResponseMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<DashboardDataKramaResponse> call, Throwable t) {
                dashboardDataKramaResponseMutableLiveData.setValue(null);
            }
        });
        return dashboardDataKramaResponseMutableLiveData;
    }
}
