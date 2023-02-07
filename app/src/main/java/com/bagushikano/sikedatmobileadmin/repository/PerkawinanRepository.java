package com.bagushikano.sikedatmobileadmin.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.kematian.KematianGetResponse;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.PerkawinanGetResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerkawinanRepository {
//    private static PerkawinanRepository perkawinanRepository;
//
//    public static PerkawinanRepository getInstance() {
//        if (perkawinanRepository == null) {
//            perkawinanRepository = new PerkawinanRepository();
//        }
//        return perkawinanRepository;
//    }
//
//    private ApiRoute getData;
//
//    public PerkawinanRepository(){
//        getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
//    }
//
//    public MutableLiveData<PerkawinanGetResponse> getPerkawinanBelumSah(String token){
//        MutableLiveData<PerkawinanGetResponse> perkawinanGetResponseMutableLiveData = new MutableLiveData<>();
//        Call<PerkawinanGetResponse> perkawinanGetResponseCall = getData.getPerkawinanBelumSah("Bearer " + token);
//        perkawinanGetResponseCall.enqueue(new Callback<PerkawinanGetResponse>() {
//            @Override
//            public void onResponse(Call<PerkawinanGetResponse> call, Response<PerkawinanGetResponse> response) {
//                Log.d("duar", String.valueOf(response.code()));
//                if (response.code() == 200 && response.body().getStatus().equals(true)) {
//                    perkawinanGetResponseMutableLiveData.setValue(response.body());
//                }
//                else {
//                    perkawinanGetResponseMutableLiveData.setValue(null);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PerkawinanGetResponse> call, Throwable t) {
//                perkawinanGetResponseMutableLiveData.setValue(null);
//            }
//        });
//        return perkawinanGetResponseMutableLiveData;
//    }
//
//    public MutableLiveData<PerkawinanGetResponse> getPerkawinanTelahSah(String token){
//        MutableLiveData<PerkawinanGetResponse> perkawinanGetResponseMutableLiveData = new MutableLiveData<>();
//        Call<PerkawinanGetResponse> perkawinanGetResponseCall = getData.getPerkawinanSah("Bearer " + token);
//        perkawinanGetResponseCall.enqueue(new Callback<PerkawinanGetResponse>() {
//            @Override
//            public void onResponse(Call<PerkawinanGetResponse> call, Response<PerkawinanGetResponse> response) {
//                Log.d("duar", String.valueOf(response.code()));
//                if (response.code() == 200 && response.body().getStatus().equals(true)) {
//                    perkawinanGetResponseMutableLiveData.setValue(response.body());
//                }
//                else {
//                    perkawinanGetResponseMutableLiveData.setValue(null);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PerkawinanGetResponse> call, Throwable t) {
//                perkawinanGetResponseMutableLiveData.setValue(null);
//            }
//        });
//        return perkawinanGetResponseMutableLiveData;
//    }
//
//    public MutableLiveData<PerkawinanGetResponse> getPerkawinanSahNextPage(String token, int page){
//        MutableLiveData<PerkawinanGetResponse> perkawinanGetResponseMutableLiveData = new MutableLiveData<>();
//        Call<PerkawinanGetResponse> perkawinanGetResponseCall = getData.getPerkawinanSahNextPage("Bearer " + token, page);
//        perkawinanGetResponseCall.enqueue(new Callback<PerkawinanGetResponse>() {
//            @Override
//            public void onResponse(Call<PerkawinanGetResponse> call, Response<PerkawinanGetResponse> response) {
//                Log.d("duar", String.valueOf(response.code()));
//                if (response.code() == 200 && response.body().getStatus().equals(true)) {
//                    perkawinanGetResponseMutableLiveData.setValue(response.body());
//                }
//                else {
//                    perkawinanGetResponseMutableLiveData.setValue(null);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PerkawinanGetResponse> call, Throwable t) {
//                perkawinanGetResponseMutableLiveData.setValue(null);
//            }
//        });
//        return perkawinanGetResponseMutableLiveData;
//    }
//
//    public MutableLiveData<PerkawinanGetResponse> getPerkawinanBelumSahNextPage(String token, int page){
//        MutableLiveData<PerkawinanGetResponse> perkawinanGetResponseMutableLiveData = new MutableLiveData<>();
//        Call<PerkawinanGetResponse> perkawinanGetResponseCall = getData.getPerkawinanBelumSahNextPage("Bearer " + token, page);
//        perkawinanGetResponseCall.enqueue(new Callback<PerkawinanGetResponse>() {
//            @Override
//            public void onResponse(Call<PerkawinanGetResponse> call, Response<PerkawinanGetResponse> response) {
//                Log.d("duar", String.valueOf(response.code()));
//                if (response.code() == 200 && response.body().getStatus().equals(true)) {
//                    perkawinanGetResponseMutableLiveData.setValue(response.body());
//                }
//                else {
//                    perkawinanGetResponseMutableLiveData.setValue(null);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PerkawinanGetResponse> call, Throwable t) {
//                perkawinanGetResponseMutableLiveData.setValue(null);
//            }
//        });
//        return perkawinanGetResponseMutableLiveData;
//    }
}
