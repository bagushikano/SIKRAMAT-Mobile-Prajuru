package com.bagushikano.sikedatmobileadmin.model.cacahkrama;

import com.bagushikano.sikedatmobileadmin.model.master.Penduduk;
import com.bagushikano.sikedatmobileadmin.model.master.Tempekan;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarAdat;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarDinas;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CacahKramaMipil {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("nomor_cacah_krama_mipil")
    @Expose
    public String nomorCacahKramaMipil;
    @SerializedName("banjar_dinas_id")
    @Expose
    public Integer banjarDinasId;
    @SerializedName("banjar_adat_id")
    @Expose
    public Integer banjarAdatId;
    @SerializedName("penduduk_id")
    @Expose
    public Integer pendudukId;
    @SerializedName("jenis_kependudukan")
    @Expose
    public String jenisKependudukan;
    @SerializedName("status")
    @Expose
    public Object status;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
    @SerializedName("penduduk")
    @Expose
    public Penduduk penduduk;
    @SerializedName("banjar_adat")
    @Expose
    public BanjarAdat banjarAdat;
    @SerializedName("banjar_dinas")
    @Expose
    public BanjarDinas banjarDinas;
    @SerializedName("tempekan")
    @Expose
    public Tempekan tempekan;
}
