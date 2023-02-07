package com.bagushikano.sikedatmobileadmin.model.krama;

import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AnggotaKramaMipil {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("krama_mipil_id")
    @Expose
    public Integer kramaMipilId;
    @SerializedName("cacah_krama_mipil_id")
    @Expose
    public Integer cacahKramaMipilId;
    @SerializedName("status_hubungan")
    @Expose
    public String statusHubungan;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
    @SerializedName("cacah_krama_mipil")
    @Expose
    public CacahKramaMipil cacahKramaMipil;
    @SerializedName("status_baru")
    @Expose
    public String statusBaru;
}
