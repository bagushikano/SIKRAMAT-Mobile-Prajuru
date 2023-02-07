package com.bagushikano.sikedatmobileadmin.model.cacahkrama;

import com.bagushikano.sikedatmobileadmin.model.master.Penduduk;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarAdat;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarDinas;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CacahTamiu {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("banjar_adat_id")
    @Expose
    private Integer banjarAdatId;
    @SerializedName("banjar_dinas_id")
    @Expose
    private Integer banjarDinasId;
    @SerializedName("nomor_cacah_tamiu")
    @Expose
    private String nomorCacahTamiu;
    @SerializedName("tanggal_masuk")
    @Expose
    private String tanggalMasuk;
    @SerializedName("tanggal_keluar")
    @Expose
    private Object tanggalKeluar;
    @SerializedName("alasan_keluar")
    @Expose
    private Object alasanKeluar;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("penduduk_id")
    @Expose
    private Integer pendudukId;
    @SerializedName("wna_id")
    @Expose
    private Object wnaId;
    @SerializedName("alamat_asal")
    @Expose
    private String alamatAsal;
    @SerializedName("desa_asal_id")
    @Expose
    private String desaAsalId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("penduduk")
    @Expose
    private Penduduk penduduk;
    @SerializedName("banjar_dinas")
    @Expose
    private BanjarDinas banjarDinas;
    @SerializedName("banjar_adat")
    @Expose
    private BanjarAdat banjarAdat;
}
