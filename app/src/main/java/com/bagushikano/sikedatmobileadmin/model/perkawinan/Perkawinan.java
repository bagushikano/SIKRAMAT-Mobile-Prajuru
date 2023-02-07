package com.bagushikano.sikedatmobileadmin.model.perkawinan;

import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.master.DesaAdat;
import com.bagushikano.sikedatmobileadmin.model.master.DesaDinas;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarAdat;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Perkawinan {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("nomor_perkawinan")
    @Expose
    public String nomorPerkawinan;
    @SerializedName("nomor_akta_perkawinan")
    @Expose
    public String nomorAktaPerkawinan;
    @SerializedName("jenis_perkawinan")
    @Expose
    public String jenisPerkawinan;
    @SerializedName("purusa_id")
    @Expose
    public Integer purusaId;
    @SerializedName("pradana_id")
    @Expose
    public Integer pradanaId;
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
    @SerializedName("tanggal_perkawinan")
    @Expose
    public String tanggalPerkawinan;
    @SerializedName("status_kekeluargaan")
    @Expose
    public String statusKekeluargaan;
    @SerializedName("calon_krama_id")
    @Expose
    public Integer calonKramaId;
    @SerializedName("nama_pemuput")
    @Expose
    public String namaPemuput;
    @SerializedName("status_perkawinan")
    @Expose
    public Integer statusPerkawinan;
    @SerializedName("file_bukti_serah_terima_perkawinan")
    @Expose
    public String fileBuktiSerahTerimaPerkawinan;
    @SerializedName("file_akta_perkawinan")
    @Expose
    public String fileAktaPerkawinan;
    @SerializedName("alasan_penolakan")
    @Expose
    public Object alasanPenolakan;
    @SerializedName("keterangan")
    @Expose
    public String keterangan;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
    @SerializedName("nik_ayah_pradana")
    @Expose
    public Object nikAyahPradana;
    @SerializedName("nik_ibu_pradana")
    @Expose
    public Object nikIbuPradana;
    @SerializedName("nama_ayah_pradana")
    @Expose
    public Object namaAyahPradana;
    @SerializedName("nama_ibu_pradana")
    @Expose
    public Object namaIbuPradana;
    @SerializedName("agama_asal_pradana")
    @Expose
    public Object agamaAsalPradana;
    @SerializedName("alamat_asal_pradana")
    @Expose
    public Object alamatAsalPradana;
    @SerializedName("desa_asal_pradana_id")
    @Expose
    public Object desaAsalPradanaId;
    @SerializedName("file_sudhi_wadhani")
    @Expose
    public Object fileSudhiWadhani;
    @SerializedName("nama_pasangan")
    @Expose
    public Object namaPasangan;
    @SerializedName("nik_pasangan")
    @Expose
    public Object nikPasangan;
    @SerializedName("agama_pasangan")
    @Expose
    public Object agamaPasangan;
    @SerializedName("alamat_asal_pasangan")
    @Expose
    public Object alamatAsalPasangan;
    @SerializedName("desa_asal_pasangan_id")
    @Expose
    public Object desaAsalPasanganId;
    @SerializedName("purusa")
    @Expose
    public CacahKramaMipil purusa;
    @SerializedName("pradana")
    @Expose
    public CacahKramaMipil pradana;
    @SerializedName("desa_asal_pradana")
    @Expose
    public DesaDinas desaDinasPradana;
    @SerializedName("desa_asal_pasangan")
    @Expose
    public DesaDinas desaDinasPasangan;
    @SerializedName("banjar_adat_purusa")
    @Expose
    public BanjarAdat banjarAdatPurusa;
    @SerializedName("banjar_adat_pradana")
    @Expose
    public BanjarAdat banjarAdatPradana;
    @SerializedName("desa_adat_pradana")
    @Expose
    public DesaAdat desaAdatPradana;
    @SerializedName("desa_adat_purusa")
    @Expose
    public DesaAdat desaAdatPurusa;
}
