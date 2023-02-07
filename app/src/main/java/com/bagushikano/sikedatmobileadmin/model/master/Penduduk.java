package com.bagushikano.sikedatmobileadmin.model.master;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Penduduk {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("desa_id")
    @Expose
    public String desaId;
    @SerializedName("nomor_induk_cacah_krama")
    @Expose
    public String nomorIndukCacahKrama;
    @SerializedName("profesi_id")
    @Expose
    public Integer profesiId;
    @SerializedName("pendidikan_id")
    @Expose
    public Integer pendidikanId;
    @SerializedName("agama")
    @Expose
    public String agama;
    @SerializedName("nik")
    @Expose
    public String nik;
    @SerializedName("gelar_depan")
    @Expose
    public String gelarDepan;
    @SerializedName("nama")
    @Expose
    public String nama;
    @SerializedName("gelar_belakang")
    @Expose
    public String gelarBelakang;
    @SerializedName("nama_alias")
    @Expose
    public String namaAlias;
    @SerializedName("tempat_lahir")
    @Expose
    public String tempatLahir;
    @SerializedName("tanggal_lahir")
    @Expose
    public String tanggalLahir;
    @SerializedName("jenis_kelamin")
    @Expose
    public String jenisKelamin;
    @SerializedName("golongan_darah")
    @Expose
    public String golonganDarah;
    @SerializedName("alamat")
    @Expose
    public String alamat;
    @SerializedName("telepon")
    @Expose
    public String telepon;
    @SerializedName("tanggal_kematian")
    @Expose
    public Object tanggalKematian;
    @SerializedName("status_perkawinan")
    @Expose
    public Object statusPerkawinan;
    @SerializedName("foto")
    @Expose
    public String foto;
    @SerializedName("ayah_kandung_id")
    @Expose
    public Object ayahKandungId;
    @SerializedName("ibu_kandung_id")
    @Expose
    public Object ibuKandungId;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
    @SerializedName("pekerjaan")
    @Expose
    public Pekerjaan pekerjaan;
    @SerializedName("pendidikan")
    @Expose
    public Pendidikan pendidikan;
    @SerializedName("ayah")
    @Expose
    public Penduduk ayah;
    @SerializedName("ibu")
    @Expose
    public Penduduk ibu;
}
