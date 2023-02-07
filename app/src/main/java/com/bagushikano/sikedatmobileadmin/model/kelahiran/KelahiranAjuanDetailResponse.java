package com.bagushikano.sikedatmobileadmin.model.kelahiran;

import com.bagushikano.sikedatmobileadmin.model.master.Penduduk;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KelahiranAjuanDetailResponse {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private KelahiranAjuan kelahiranAjuan;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user")
    @Expose
    private Penduduk penduduk;
}
