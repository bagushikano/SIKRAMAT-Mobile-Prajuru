package com.bagushikano.sikedatmobileadmin.repository;

import androidx.lifecycle.MutableLiveData;

import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.DashboardDataKramaResponse;
import com.bagushikano.sikedatmobileadmin.model.ProfileGetResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    private static ProfileRepository profileRepository;

    public static ProfileRepository getInstance() {
        if (profileRepository == null) {
            profileRepository = new ProfileRepository();
        }
        return profileRepository;
    }

    private ApiRoute getData;

    public ProfileRepository(){
        getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
    }

    public MutableLiveData<ProfileGetResponse> getProfile(String token) {
        MutableLiveData<ProfileGetResponse> profileGetResponseMutableLiveData = new MutableLiveData<>();
        Call<ProfileGetResponse> dashboardDataKramaResponseCall = getData.getProfile("Bearer " + token);
        dashboardDataKramaResponseCall.enqueue(new Callback<ProfileGetResponse>() {
            @Override
            public void onResponse(Call<ProfileGetResponse> call, Response<ProfileGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    profileGetResponseMutableLiveData.setValue(response.body());
                } else {
                    profileGetResponseMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ProfileGetResponse> call, Throwable t) {
                profileGetResponseMutableLiveData.setValue(null);
            }
        });
        return profileGetResponseMutableLiveData;
    }
}
