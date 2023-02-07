package com.bagushikano.sikedatmobileadmin.model.perceraian;

import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerceraianGetKramaCerai {

    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public KramaMipil kramaMipil;
    @SerializedName("pasangan")
    @Expose
    public List<AnggotaKramaMipil> pasangan = null;
    @SerializedName("anggota_krama_mipil")
    @Expose
    public List<AnggotaKramaMipil> anggotaKramaMipil = null;
    @SerializedName("message")
    @Expose
    public String message;
}
