package com.bagushikano.sikedatmobileadmin.model.perceraian;

import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaMipil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerceraianDetailResponse {
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public Perceraian perceraian;
    @SerializedName("anggota_krama_mipil")
    @Expose
    public List<AnggotaKramaMipil> anggotaKramaMipil = null;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("sisi_pradana")
    @Expose
    public Boolean sisiPradana;
}
