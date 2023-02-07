package com.bagushikano.sikedatmobileadmin.model.master.banjar;

import com.bagushikano.sikedatmobileadmin.model.master.DesaAdat;
import com.bagushikano.sikedatmobileadmin.model.master.Kecamatan;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BanjarAdat {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("desa_adat_id")
    @Expose
    public Integer desaAdatId;
    @SerializedName("kode_banjar_adat")
    @Expose
    public String kodeBanjarAdat;
    @SerializedName("nama_banjar_adat")
    @Expose
    public String namaBanjarAdat;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public Object updatedAt;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
    @SerializedName("desa_adat")
    @Expose
    public DesaAdat desaAdat;
}
