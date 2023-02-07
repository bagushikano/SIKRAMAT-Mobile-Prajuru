package com.bagushikano.sikedatmobileadmin.model.krama;

import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahTamiu;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarAdat;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tamiu {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nomor_tamiu")
    @Expose
    private String nomorTamiu;
    @SerializedName("banjar_adat_id")
    @Expose
    private Integer banjarAdatId;
    @SerializedName("cacah_tamiu_id")
    @Expose
    private Integer cacahTamiuId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("alasan_perubahan")
    @Expose
    private String alasanPerubahan;
    @SerializedName("tanggal_registrasi")
    @Expose
    private String tanggalRegistrasi;
    @SerializedName("tanggal_nonaktif")
    @Expose
    private Object tanggalNonaktif;
    @SerializedName("alasan_keluar")
    @Expose
    private Object alasanKeluar;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("cacah_tamiu")
    @Expose
    private CacahTamiu cacahTamiu;
    @SerializedName("banjar_adat")
    @Expose
    private BanjarAdat banjarAdat;
}
