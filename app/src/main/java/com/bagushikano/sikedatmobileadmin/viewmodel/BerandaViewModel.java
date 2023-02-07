package com.bagushikano.sikedatmobileadmin.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bagushikano.sikedatmobileadmin.model.DashboardDataResponse;
import com.bagushikano.sikedatmobileadmin.model.notifikasi.NotifikasiGetResponse;
import com.bagushikano.sikedatmobileadmin.repository.BerandaRepository;

public class BerandaViewModel extends ViewModel {
    private MutableLiveData<DashboardDataResponse> dashboardDataResponseMutableLiveData;
    private MutableLiveData<NotifikasiGetResponse> notifikasiGetResponseMutableLiveData;
    private BerandaRepository berandaRepository;

    public void init(String token){
        if (dashboardDataResponseMutableLiveData != null &&
                notifikasiGetResponseMutableLiveData != null){
            return;
        }
        berandaRepository = berandaRepository.getInstance();
        dashboardDataResponseMutableLiveData = berandaRepository.getDashboardData(token);
        notifikasiGetResponseMutableLiveData = berandaRepository.getNotifikasi(token);
    }
    public void getData(String token) {
        dashboardDataResponseMutableLiveData = berandaRepository.getDashboardData(token);
    }

    public LiveData<DashboardDataResponse> getDashboardData() {
        return dashboardDataResponseMutableLiveData;
    }

    public void getNotifikasi(String token) {
        notifikasiGetResponseMutableLiveData = berandaRepository.getNotifikasi(token);
    }

    public LiveData<NotifikasiGetResponse> getNotifikasiData() {
        return notifikasiGetResponseMutableLiveData;
    }
}
