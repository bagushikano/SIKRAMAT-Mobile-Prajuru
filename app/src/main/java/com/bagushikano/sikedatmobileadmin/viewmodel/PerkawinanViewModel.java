package com.bagushikano.sikedatmobileadmin.viewmodel;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.bagushikano.sikedatmobileadmin.model.kematian.KematianAjuanGetResponse;
import com.bagushikano.sikedatmobileadmin.model.kematian.KematianGetResponse;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.Perkawinan;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.PerkawinanGetResponse;
import com.bagushikano.sikedatmobileadmin.repository.KematianRepository;
import com.bagushikano.sikedatmobileadmin.repository.PerkawinanRepository;

public class PerkawinanViewModel extends ViewModel {
//    private MutableLiveData<PerkawinanGetResponse> perkawinanBelumSahGetResponseMutableLiveData;
//    private MutableLiveData<PerkawinanGetResponse> perkawinanTelahSahGetResponseMutableLiveData;
//
//    private MediatorLiveData<PerkawinanGetResponse> perkawinanTelahSahGetResponseMediatorLiveData;
//
//
//    private PerkawinanRepository perkawinanRepository;
//
//    public void init(String token) {
//        if (perkawinanBelumSahGetResponseMutableLiveData != null
//                || perkawinanTelahSahGetResponseMutableLiveData != null) {
//            return;
//        }
//
//        perkawinanRepository = perkawinanRepository.getInstance();
//        perkawinanBelumSahGetResponseMutableLiveData = perkawinanRepository.getPerkawinanBelumSah(token);
//        perkawinanTelahSahGetResponseMutableLiveData = perkawinanRepository.getPerkawinanTelahSah(token);
//
//        perkawinanTelahSahGetResponseMediatorLiveData.addSource(perkawinanTelahSahGetResponseMutableLiveData, new Observer<PerkawinanGetResponse>() {
//            @Override
//            public void onChanged(@Nullable PerkawinanGetResponse perkawinanGetResponse) {
//                PerkawinanGetResponse current = perkawinanTelahSahGetResponseMediatorLiveData.getValue();
//                current. = querySnapshot;
//                mediatorLiveData.setValue(current);
//            }
//        });
//
//        perkawinanTelahSahGetResponseMediatorLiveData.addSource(perkawinanTelahSahGetResponseMutableLiveData);
//    }
//    public void getDataPerkawinanSah(String token) {
//        perkawinanTelahSahGetResponseMutableLiveData = perkawinanRepository.getPerkawinanTelahSah(token);
//    }
//
//    public void getDataPerkawinanSahNextPage(String token, int page) {
//        perkawinanTelahSahGetResponseMutableLiveData.();
//        perkawinanTelahSahGetResponseMutableLiveData. perkawinanRepository.getPerkawinanSahNextPage(token, page);
//    }
//
//    public LiveData<PerkawinanGetResponse> getPerkawinanSah() {
//        return perkawinanTelahSahGetResponseMutableLiveData;
//    }
//
//    public void getDataAjuan(String token) {
//        kematianAjuanGetResponseMutableLiveData = kematianRepository.getKematianAjuanData(token);
//    }
//
//
//    public LiveData<KematianAjuanGetResponse> getKematianAjuan() {
//        return kematianAjuanGetResponseMutableLiveData;
//    }
}
