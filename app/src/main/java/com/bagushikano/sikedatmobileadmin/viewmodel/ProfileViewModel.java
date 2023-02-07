package com.bagushikano.sikedatmobileadmin.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bagushikano.sikedatmobileadmin.model.DashboardDataKramaResponse;
import com.bagushikano.sikedatmobileadmin.model.ProfileGetResponse;
import com.bagushikano.sikedatmobileadmin.repository.KramaRepository;
import com.bagushikano.sikedatmobileadmin.repository.ProfileRepository;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<ProfileGetResponse> profileGetResponseMutableLiveData;
    private ProfileRepository profileRepository;

    public void init(String token){
        if (profileGetResponseMutableLiveData != null){
            return;
        }
        profileRepository = profileRepository.getInstance();
        profileGetResponseMutableLiveData = profileRepository.getProfile(token);
    }
    public void getData(String token) {
        profileGetResponseMutableLiveData = profileRepository.getProfile(token);
    }

    public LiveData<ProfileGetResponse> getProfileData() {
        return profileGetResponseMutableLiveData;
    }
}