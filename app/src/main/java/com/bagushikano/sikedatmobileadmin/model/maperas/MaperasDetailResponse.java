package com.bagushikano.sikedatmobileadmin.model.maperas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaperasDetailResponse {
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public Maperas maperas;
    @SerializedName("sisi_banjar_adat_keluar")
    @Expose
    public Boolean sisiBanjarAdatKeluar;
    @SerializedName("message")
    @Expose
    public String message;
}
