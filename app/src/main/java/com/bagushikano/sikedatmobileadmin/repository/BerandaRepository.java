package com.bagushikano.sikedatmobileadmin.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.DashboardDataResponse;
import com.bagushikano.sikedatmobileadmin.model.notifikasi.NotifikasiGetResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BerandaRepository {
    private static BerandaRepository berandaRepository;

    public static BerandaRepository getInstance() {
        if (berandaRepository == null) {
            berandaRepository = new BerandaRepository();
        }
        return berandaRepository;
    }

    private ApiRoute getData;

    public BerandaRepository(){
        getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
    }

    public MutableLiveData<DashboardDataResponse> getDashboardData(String token) {
        MutableLiveData<DashboardDataResponse> dashboardDataResponseMutableLiveData = new MutableLiveData<>();
        Call<DashboardDataResponse> dashboardDataResponseCall = getData.getDashboardData("Bearer " + token);
        dashboardDataResponseCall.enqueue(new Callback<DashboardDataResponse>() {
            @Override
            public void onResponse(Call<DashboardDataResponse> call, Response<DashboardDataResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    dashboardDataResponseMutableLiveData.setValue(response.body());
                } else {
                    dashboardDataResponseMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<DashboardDataResponse> call, Throwable t) {
                dashboardDataResponseMutableLiveData.setValue(null);
            }
        });
        return dashboardDataResponseMutableLiveData;
    }

    public MutableLiveData<NotifikasiGetResponse> getNotifikasi(String token){
        MutableLiveData<NotifikasiGetResponse> notifikasiGetResponseMutableLiveData = new MutableLiveData<>();
        Call<NotifikasiGetResponse> notifikasiGetResponseCall = getData.getNotifikasi("Bearer " + token);
        notifikasiGetResponseCall.enqueue(new Callback<NotifikasiGetResponse>() {
            @Override
            public void onResponse(Call<NotifikasiGetResponse> call, Response<NotifikasiGetResponse> response) {
                Log.d("duar", String.valueOf(response.code()));
                if (response.code() == 200 && response.body().getStatusCode() == 200 && response.body().getMessage().equals("data notifikasi sukses")) {
                    notifikasiGetResponseMutableLiveData.setValue(response.body());
                }
                else {
                    notifikasiGetResponseMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<NotifikasiGetResponse> call, Throwable t) {
                notifikasiGetResponseMutableLiveData.setValue(null);
            }
        });
        return notifikasiGetResponseMutableLiveData;
    }
}
