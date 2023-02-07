package com.bagushikano.sikedatmobileadmin.model.master;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tempekan {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("banjar_adat_id")
    @Expose
    public Integer banjarAdatId;
    @SerializedName("kode_tempekan")
    @Expose
    public String kodeTempekan;
    @SerializedName("nama_tempekan")
    @Expose
    public String namaTempekan;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
}
