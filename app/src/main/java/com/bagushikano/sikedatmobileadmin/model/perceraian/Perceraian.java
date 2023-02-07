package com.bagushikano.sikedatmobileadmin.model.perceraian;


import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Perceraian {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("nomor_perceraian")
    @Expose
    public String nomorPerceraian;
    @SerializedName("nomor_akta_perceraian")
    @Expose
    public String nomorAktaPerceraian;
    @SerializedName("krama_mipil_id")
    @Expose
    public Integer kramaMipilId;
    @SerializedName("banjar_adat_purusa_id")
    @Expose
    public Integer banjarAdatPurusaId;
    @SerializedName("banjar_adat_pradana_id")
    @Expose
    public Integer banjarAdatPradanaId;
    @SerializedName("desa_adat_purusa_id")
    @Expose
    public Integer desaAdatPurusaId;
    @SerializedName("desa_adat_pradana_id")
    @Expose
    public Integer desaAdatPradanaId;
    @SerializedName("purusa_id")
    @Expose
    public Integer purusaId;
    @SerializedName("pradana_id")
    @Expose
    public Integer pradanaId;
    @SerializedName("status_purusa")
    @Expose
    public String statusPurusa;
    @SerializedName("status_pradana")
    @Expose
    public String statusPradana;
    @SerializedName("status_anggota")
    @Expose
    public String statusAnggota;
    @SerializedName("krama_mipil_baru_purusa_id")
    @Expose
    public Integer kramaMipilBaruPurusaId;
    @SerializedName("krama_mipil_baru_pradana_id")
    @Expose
    public Integer kramaMipilBaruPradanaId;
    @SerializedName("status_hubungan_baru_purusa")
    @Expose
    public String statusHubunganBaruPurusa;
    @SerializedName("status_hubungan_baru_pradana")
    @Expose
    public String statusHubunganBaruPradana;
    @SerializedName("desa_baru_pradana_id")
    @Expose
    public String desaBaruPradanaId;
    @SerializedName("tanggal_perceraian")
    @Expose
    public String tanggalPerceraian;
    @SerializedName("nama_pemuput")
    @Expose
    public String namaPemuput;
    @SerializedName("file_bukti_perceraian")
    @Expose
    public String fileBuktiPerceraian;
    @SerializedName("file_akta_perceraian")
    @Expose
    public String fileAktaPerceraian;
    @SerializedName("status_perceraian")
    @Expose
    public Integer statusPerceraian;
    @SerializedName("keterangan")
    @Expose
    public String keterangan;
    @SerializedName("alasan_penolakan")
    @Expose
    public String alasanPenolakan;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    public String deletedAt;
    @SerializedName("purusa")
    @Expose
    public CacahKramaMipil purusa;
    @SerializedName("pradana")
    @Expose
    public CacahKramaMipil pradana;
}

