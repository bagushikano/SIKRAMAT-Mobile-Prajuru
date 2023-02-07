package com.bagushikano.sikedatmobileadmin.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bagushikano.sikedatmobileadmin.model.kelahiran.KelahiranAjuanGetResponse;
import com.bagushikano.sikedatmobileadmin.model.kelahiran.KelahiranGetResponse;
import com.bagushikano.sikedatmobileadmin.repository.KelahiranRepository;

public class KelahiranViewModel extends ViewModel {
    private MutableLiveData<KelahiranGetResponse> kelahiranGetResponseMutableLiveData;
    private MutableLiveData<KelahiranAjuanGetResponse> kelahiranAjuanGetResponseMutableLiveData;

    private KelahiranRepository kelahiranRepository;

    public void init(String token) {
        if (kelahiranGetResponseMutableLiveData != null ||
                kelahiranAjuanGetResponseMutableLiveData != null) {
            return;
        }
        kelahiranRepository = kelahiranRepository.getInstance();
        kelahiranGetResponseMutableLiveData = kelahiranRepository.getKelahiranData(token);
        kelahiranAjuanGetResponseMutableLiveData = kelahiranRepository.getKelahiranAjuanData(token);
    }
    public void getData(String token) {
        kelahiranGetResponseMutableLiveData = kelahiranRepository.getKelahiranData(token);
    }

    public LiveData<KelahiranGetResponse> getKelahiran() {
        return kelahiranGetResponseMutableLiveData;
    }

    public void getDataAjuan(String token) {
        kelahiranAjuanGetResponseMutableLiveData = kelahiranRepository.getKelahiranAjuanData(token);
    }


    public LiveData<KelahiranAjuanGetResponse> getKelahiranAjuan() {
        return kelahiranAjuanGetResponseMutableLiveData;
    }
}
