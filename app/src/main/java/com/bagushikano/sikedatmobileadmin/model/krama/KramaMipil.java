package com.bagushikano.sikedatmobileadmin.model.krama;

import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarAdat;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KramaMipil {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("nomor_krama_mipil")
    @Expose
    public String nomorKramaMipil;
    @SerializedName("banjar_adat_id")
    @Expose
    public Integer banjarAdatId;
    @SerializedName("cacah_krama_mipil_id")
    @Expose
    public Integer cacahKramaMipilId;
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("alasan_perubahan")
    @Expose
    public String alasanPerubahan;
    @SerializedName("tanggal_registrasi")
    @Expose
    public String tanggalRegistrasi;
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
    @SerializedName("banjar_adat")
    @Expose
    public BanjarAdat banjarAdat;
    @SerializedName("jenis_krama_mipil")
    @Expose
    public String jenisKramaMipil;
    @SerializedName("kedudukan_krama_mipil")
    @Expose
    public String kedudukanKramaMipil;

}
