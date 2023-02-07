package com.bagushikano.sikedatmobileadmin.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bagushikano.sikedatmobileadmin.model.DashboardDataKramaResponse;
import com.bagushikano.sikedatmobileadmin.model.DashboardDataResponse;
import com.bagushikano.sikedatmobileadmin.repository.BerandaRepository;
import com.bagushikano.sikedatmobileadmin.repository.KramaRepository;

public class KramaViewModel extends ViewModel {
    private MutableLiveData<DashboardDataKramaResponse> dashboardDataKramaResponseMutableLiveData;
    private KramaRepository kramaRepository;

    public void init(String token){
        if (dashboardDataKramaResponseMutableLiveData != null){
            return;
        }
        kramaRepository = kramaRepository.getInstance();
        dashboardDataKramaResponseMutableLiveData = kramaRepository.getDashboardKramaData(token);
    }
    public void getData(String token) {
        dashboardDataKramaResponseMutableLiveData = kramaRepository.getDashboardKramaData(token);
    }

    public LiveData<DashboardDataKramaResponse> getDashboardData() {
        return dashboardDataKramaResponseMutableLiveData;
    }
}
