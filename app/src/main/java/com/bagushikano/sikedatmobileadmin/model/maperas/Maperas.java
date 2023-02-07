package com.bagushikano.sikedatmobileadmin.model.maperas;

import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.bagushikano.sikedatmobileadmin.model.master.DesaDinas;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Maperas {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("nomor_maperas")
    @Expose
    public String nomorMaperas;
    @SerializedName("nomor_akta_pengangkatan_anak")
    @Expose
    public String nomorAktaPengangkatanAnak;
    @SerializedName("jenis_maperas")
    @Expose
    public String jenisMaperas;
    @SerializedName("krama_mipil_lama_id")
    @Expose
    public Integer kramaMipilLamaId;
    @SerializedName("krama_mipil_baru_id")
    @Expose
    public Integer kramaMipilBaruId;
    @SerializedName("cacah_krama_mipil_lama_id")
    @Expose
    public Integer cacahKramaMipilLamaId;
    @SerializedName("cacah_krama_mipil_baru_id")
    @Expose
    public Integer cacahKramaMipilBaruId;
    @SerializedName("banjar_adat_lama_id")
    @Expose
    public Integer banjarAdatLamaId;
    @SerializedName("banjar_adat_baru_id")
    @Expose
    public Integer banjarAdatBaruId;
    @SerializedName("desa_adat_lama_id")
    @Expose
    public Integer desaAdatLamaId;
    @SerializedName("desa_adat_baru_id")
    @Expose
    public Integer desaAdatBaruId;
    @SerializedName("ayah_lama_id")
    @Expose
    public Integer ayahLamaId;
    @SerializedName("ayah_baru_id")
    @Expose
    public Integer ayahBaruId;
    @SerializedName("ibu_lama_id")
    @Expose
    public Integer ibuLamaId;
    @SerializedName("ibu_baru_id")
    @Expose
    public Integer ibuBaruId;
    @SerializedName("tanggal_maperas")
    @Expose
    public String tanggalMaperas;
    @SerializedName("nama_pemuput")
    @Expose
    public String namaPemuput;
    @SerializedName("status_maperas")
    @Expose
    public Integer statusMaperas;
    @SerializedName("file_bukti_maperas")
    @Expose
    public String fileBuktiMaperas;
    @SerializedName("file_akta_pengangkatan_anak")
    @Expose
    public String fileAktaPengangkatanAnak;
    @SerializedName("alasan_penolakan")
    @Expose
    public Object alasanPenolakan;
    @SerializedName("nik_ayah_lama")
    @Expose
    public Object nikAyahLama;
    @SerializedName("nik_ibu_lama")
    @Expose
    public Object nikIbuLama;
    @SerializedName("nama_ayah_lama")
    @Expose
    public Object namaAyahLama;
    @SerializedName("nama_ibu_lama")
    @Expose
    public Object namaIbuLama;
    @SerializedName("agama_lama")
    @Expose
    public Object agamaLama;
    @SerializedName("alamat_asal")
    @Expose
    public Object alamatAsal;
    @SerializedName("desa_asal_id")
    @Expose
    public Object desaAsalId;
    @SerializedName("file_sudhi_wadhani")
    @Expose
    public Object fileSudhiWadhani;
    @SerializedName("nik_ayah_baru")
    @Expose
    public Object nikAyahBaru;
    @SerializedName("nik_ibu_baru")
    @Expose
    public Object nikIbuBaru;
    @SerializedName("nama_ayah_baru")
    @Expose
    public Object namaAyahBaru;
    @SerializedName("nama_ibu_baru")
    @Expose
    public Object namaIbuBaru;
    @SerializedName("agama_baru")
    @Expose
    public Object agamaBaru;
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
    @SerializedName("cacah_krama_mipil_lama")
    @Expose
    public CacahKramaMipil cacahKramaMipilLama;
    @SerializedName("cacah_krama_mipil_baru")
    @Expose
    public CacahKramaMipil cacahKramaMipilBaru;
    @SerializedName("krama_mipil_lama")
    @Expose
    public KramaMipil kramaMipilLama;
    @SerializedName("krama_mipil_baru")
    @Expose
    public KramaMipil kramaMipilBaru;
    @SerializedName("ayah_lama")
    @Expose
    public CacahKramaMipil ayahLama;
    @SerializedName("ayah_baru")
    @Expose
    public CacahKramaMipil ayahBaru;
    @SerializedName("ibu_lama")
    @Expose
    public CacahKramaMipil ibuLama;
    @SerializedName("ibu_baru")
    @Expose
    public CacahKramaMipil ibuBaru;
    @SerializedName("desa_dinas_asal")
    @Expose
    public DesaDinas desaDinasAsal;
}
