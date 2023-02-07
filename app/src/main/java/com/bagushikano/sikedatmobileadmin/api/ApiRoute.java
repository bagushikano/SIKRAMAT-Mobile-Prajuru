package com.bagushikano.sikedatmobileadmin.api;

import com.bagushikano.sikedatmobileadmin.model.perceraian.PerceraianDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.perceraian.PerceraianGetKramaCerai;
import com.bagushikano.sikedatmobileadmin.model.perceraian.PerceraianGetResponse;
import com.bagushikano.sikedatmobileadmin.model.AuthResponse;
import com.bagushikano.sikedatmobileadmin.model.DashboardDataKramaResponse;
import com.bagushikano.sikedatmobileadmin.model.DashboardDataResponse;
import com.bagushikano.sikedatmobileadmin.model.ProfileGetResponse;
import com.bagushikano.sikedatmobileadmin.model.ResponseGeneral;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipilDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipilGetResponse;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaTamiuDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaTamiuGetResponse;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahTamiuDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahTamiuGetResponse;
import com.bagushikano.sikedatmobileadmin.model.kelahiran.KelahiranAjuanDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.kelahiran.KelahiranAjuanGetResponse;
import com.bagushikano.sikedatmobileadmin.model.kelahiran.KelahiranApproveResponse;
import com.bagushikano.sikedatmobileadmin.model.kelahiran.KelahiranDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.kelahiran.KelahiranGetResponse;
import com.bagushikano.sikedatmobileadmin.model.kematian.KematianAjuanDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.kematian.KematianAjuanGetResponse;
import com.bagushikano.sikedatmobileadmin.model.kematian.KematianApproveResponse;
import com.bagushikano.sikedatmobileadmin.model.kematian.KematianDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.kematian.KematianGetResponse;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaMipilGetResponse;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaTamiuGetResponse;
import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaTamiuGetResponse;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipilDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipilGetResponse;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaTamiuDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.krama.KramaTamiuGetResponse;
import com.bagushikano.sikedatmobileadmin.model.krama.TamiuDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.krama.TamiuGetResponse;
import com.bagushikano.sikedatmobileadmin.model.maperas.MaperasDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.maperas.MaperasGetResponse;
import com.bagushikano.sikedatmobileadmin.model.maperas.MaperasOrtuBaruGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.BanjarAdatGetNoPaginationResponse;
import com.bagushikano.sikedatmobileadmin.model.master.DesaAdatGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.DesaDinasGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.KabupatenGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.KecamatanGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.PekerjaanGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.PendidikanGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.ProvinsiGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarAdatGetResponse;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarDinasGetResponse;
import com.bagushikano.sikedatmobileadmin.model.notifikasi.NotifikasiGetResponse;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.PerkawinanDetailResponse;
import com.bagushikano.sikedatmobileadmin.model.perkawinan.PerkawinanGetResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiRoute {

    /**
     * Master public things here
     */

    @Headers({"Accept: application/json"})
    @GET("public/get-provinsi")
    Call<ProvinsiGetResponse> getMasterProvinsi();

    @Headers({"Accept: application/json"})
    @GET("public/get-kabupaten")
    Call<KabupatenGetResponse> getMasterKabupaten();

    @Headers({"Accept: application/json"})
    @GET("public/get-kabupaten-provinsi")
    Call<KabupatenGetResponse> getMasterKabupatenProv(
            @Query("prov") String provId
    );

    @Headers({"Accept: application/json"})
    @GET("public/get-kecamatan")
    Call<KecamatanGetResponse> getMasterKecamatan(
            @Query("kab") String kabId
    );

    @Headers({"Accept: application/json"})
    @GET("public/get-desa-adat")
    Call<DesaAdatGetResponse> getMasterDesaAdat(
            @Query("kec") String kecId
    );

    @Headers({"Accept: application/json"})
    @GET("public/get-banjar-adat")
    Call<BanjarAdatGetNoPaginationResponse> getMasterBanjarAdat(
            @Query("desa_adat") String desId
    );

    @Headers({"Accept: application/json"})
    @GET("public/get-desa-dinas")
    Call<DesaDinasGetResponse> getMasterDesaDinas(
            @Query("kec") String kec
    );

    @Headers({"Accept: application/json"})
    @GET("public/get-pendidikan")
    Call<PendidikanGetResponse> getMasterPendidikan();

    @Headers({"Accept: application/json"})
    @GET("public/get-pekerjaan")
    Call<PekerjaanGetResponse> getMasterPekerjaan();


    /**
     * Auth related thing start here
     * */
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("login")
    Call<AuthResponse> loginUser(
            @Field("email") String username,
            @Field("password") String password
    );

    /**
     *  Desa adat related start here
     */

    @Headers({"Accept: application/json"})
    @GET("admin/banjar/get-banjar-dinas")
    Call<BanjarDinasGetResponse> getBanjarDinas(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar/get-banjar-adat")
    Call<BanjarAdatGetResponse> getBanjarAdat(
            @Header("Authorization") String authHeader
    );

    /**
     *  Admin related start here
     */

    @Headers({"Accept: application/json"})
    @GET("admin/dashboard")
    Call<DashboardDataResponse> getDashboardData(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("admin/dashboard/krama")
    Call<DashboardDataKramaResponse> getDashboardKramaData(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/profile/get")
    Call<ProfileGetResponse> getProfile(
            @Header("Authorization") String authHeader
    );


    /**
     * Cacah Krama Mipil related start here
     */

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/cacah-krama/get-mipil")
    Call<CacahKramaMipilGetResponse> getCacahKramaMipil(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/cacah-krama/get-mipil")
    Call<CacahKramaMipilGetResponse> getCacahKramaMipilNextPage(
            @Header("Authorization") String authHeader,
            @Query("page") int page
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/cacah-krama/get-mipil-detail/{id}")
    Call<CacahKramaMipilDetailResponse> getCacahKramaMipilDetail(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );

    /**
     * Cacah Krama tamiu related start here
     */

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/cacah-krama/get-cacah-krama-tamiu")
    Call<CacahKramaTamiuGetResponse> getCacahKramaTamiu(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/cacah-krama/get-cacah-krama-tamiu-detail/{id}")
    Call<CacahKramaTamiuDetailResponse> getCacahKramaTamiulDetail(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );

    /**
     * Cacah Tamiu related start here
     */

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/cacah-krama/get-cacah-tamiu")
    Call<CacahTamiuGetResponse> getCacahTamiu(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/cacah-krama/get-cacah-tamiu-detail/{id}")
    Call<CacahTamiuDetailResponse> getCacahTamiulDetail(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );


    /**
     *  Krama Mipil related start here
     */

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/krama/get-mipil")
    Call<KramaMipilGetResponse> getKramaMipil(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/krama/get-mipil/{id}")
    Call<KramaMipilDetailResponse> getDetailKramaMipil(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/krama/get-mipil-detail/{id}")
    Call<AnggotaKramaMipilGetResponse> getKramaMipilDetail(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );

    /**
     * Krama tamiu related start here
     */

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/krama/get-krama-tamiu")
    Call<KramaTamiuGetResponse> getKramaTamiu(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/krama/get-krama-tamiu/{id}")
    Call<KramaTamiuDetailResponse> getDetailKramaTamiu(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/krama/get-krama-tamiu-detail/{id}")
    Call<AnggotaKramaTamiuGetResponse> getKramaTamiuDetail(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );

    /**
     * Tamiu related thing start here
     */

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/krama/get-tamiu")
    Call<TamiuGetResponse> getTamiu(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/krama/get-tamiu/{id}")
    Call<TamiuDetailResponse> getDetailTamiu(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/krama/get-tamiu-detail/{id}")
    Call<AnggotaTamiuGetResponse> getTamiuDetail(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );

    /**
     * Kematian related thing start here
     */

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/kematian/get")
    Call<KematianGetResponse> getKematian(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/kematian/detail/{id}")
    Call<KematianDetailResponse> getKematianDetail(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/kematian/get-ajuan")
    Call<KematianAjuanGetResponse> getKematianAjuan(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/kematian/detail-ajuan/{id}")
    Call<KematianAjuanDetailResponse> getKematianAjuanDetail(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );


    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/kematian/approve/{id}")
    Call<KematianApproveResponse> kematianApprove(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("admin/banjar-adat/kematian/tolak")
    Call<KematianDetailResponse> kematianTolak(
            @Header("Authorization") String authHeader,
            @Field("id") Integer id,
            @Field("alasan") String alasan
    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("admin/banjar-adat/kematian/proses")
    Call<KematianDetailResponse> kematianProses(
            @Header("Authorization") String authHeader,
            @Field("id") Integer id
    );


    /**
     * Kelahiran related thing start here
     */


    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/kelahiran/get")
    Call<KelahiranGetResponse> getKelahiran(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/kelahiran/detail/{id}")
    Call<KelahiranDetailResponse> getKelahiranDetail(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/kelahiran/approve/{id}")
    Call<KelahiranApproveResponse> kelahiranApprove(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("admin/banjar-adat/kelahiran/tolak")
    Call<KelahiranDetailResponse> kelahiranTolak(
            @Header("Authorization") String authHeader,
            @Field("id") Integer id,
            @Field("alasan") String alasan
    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("admin/banjar-adat/kelahiran/proses")
    Call<KelahiranDetailResponse> kelahiranProses(
            @Header("Authorization") String authHeader,
            @Field("id") Integer id
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/kelahiran/get-ajuan")
    Call<KelahiranAjuanGetResponse> getKelahiranAjuan(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/kelahiran/detail-ajuan/{id}")
    Call<KelahiranAjuanDetailResponse> getKelahiranAjuanDetail(
            @Header("Authorization") String authHeader,
            @Path("id") Integer id
    );

    /**
     * Perkawinan things
     */

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/perkawinan/get")
    Call<PerkawinanGetResponse> getPerkawinan(
            @Header("Authorization") String authHeader,
            @Query("status") String status,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate,
            @Query("type") String type
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/perkawinan/get")
    Call<PerkawinanGetResponse> getPerkawinanNextPage(
            @Header("Authorization") String authHeader,
            @Query("status") String status,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate,
            @Query("type") String type,
            @Query("page") int page
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/perkawinan/detail")
    Call<PerkawinanDetailResponse> getPerkawinanDetail(
            @Header("Authorization") String authHeader,
            @Query("perkawinan") Integer idPerkawinan
    );


    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/perkawinan/get-pradana")
    Call<CacahKramaMipilGetResponse> getPradana(
            @Header("Authorization") String authHeader,
            @Query("purusa_id") Integer purusaId,
            @Query("jenis_perkawinan") Integer jenisPerkawinan,
            @Query("banjar_adat_id") Integer banjarAdatId,
            @Query("nama") String nama
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/perkawinan/get-pradana")
    Call<CacahKramaMipilGetResponse> getPradanaNextPage(
            @Header("Authorization") String authHeader,
            @Query("purusa_id") Integer purusaId,
            @Query("page") int page,
            @Query("jenis_perkawinan") Integer jenisPerkawinan,
            @Query("banjar_adat_id") Integer banjarAdatId,
            @Query("nama") String nama
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/perkawinan/get-purusa")
    Call<CacahKramaMipilGetResponse> getPurusa(
            @Header("Authorization") String authHeader,
            @Query("nama") String nama
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/perkawinan/get-purusa")
    Call<CacahKramaMipilGetResponse> getPurusaNextPage(
            @Header("Authorization") String authHeader,
            @Query("page") int page,
            @Query("nama") String nama
    );


    @Headers({"Accept: application/json"})
    @Multipart
    @POST("admin/banjar-adat/perkawinan/store-satu-banjar")
    Call<PerkawinanDetailResponse> perkawinanStoreSatuBanjarAdat(
            @Header("Authorization") String authHeader,
            @Part("purusa") RequestBody purusaId,
            @Part("pradana") RequestBody pradanaId,
            @Part("nomor_bukti_serah_terima_perkawinan") RequestBody noBuktiSerahTerima,
            @Part("nomor_akta_perkawinan") RequestBody noAkta,
            @Part("tanggal_perkawinan") RequestBody tanggalPerkawinan,
            @Part("keterangan") RequestBody keterangan,
            @Part("status_kekeluargaan") RequestBody statusKekeluargaan,
            @Part("calon_kepala_keluarga") RequestBody calonKepalaKeluargaId,
            @Part("nama_pemuput") RequestBody namaPemuput,
            @Part MultipartBody.Part file_bukti_serah_terima_perkawinan,
            @Part MultipartBody.Part file_akta_perkawinan
    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("admin/banjar-adat/perkawinan/sah-satu-banjar")
    Call<PerkawinanDetailResponse> perkawinanSahSatuBanjarAdat(
            @Header("Authorization") String authHeader,
            @Field("id_perkawinan") Integer idPerkawinan
    );

    @Headers({"Accept: application/json"})
    @Multipart
    @POST("admin/banjar-adat/perkawinan/store-beda-banjar-adat")
    Call<PerkawinanDetailResponse> perkawinanStoreBedaBanjarAdat(
            @Header("Authorization") String authHeader,
            @Part("purusa") RequestBody purusaId,
            @Part("pradana") RequestBody pradanaId,
            @Part("banjar_adat_pradana") RequestBody banjarAdatPradanaId,
            @Part("nomor_bukti_serah_terima_perkawinan") RequestBody noBuktiSerahTerima,
            @Part("nomor_akta_perkawinan") RequestBody noAkta,
            @Part("tanggal_perkawinan") RequestBody tanggalPerkawinan,
            @Part("keterangan") RequestBody keterangan,
            @Part("status_kekeluargaan") RequestBody statusKekeluargaan,
            @Part("calon_kepala_keluarga") RequestBody calonKepalaKeluargaId,
            @Part("nama_pemuput") RequestBody namaPemuput,
            @Part MultipartBody.Part file_bukti_serah_terima_perkawinan,
            @Part MultipartBody.Part file_akta_perkawinan
    );

    @Headers({"Accept: application/json"})
    @Multipart
    @POST("admin/banjar-adat/perkawinan/edit-beda-banjar-adat")
    Call<PerkawinanDetailResponse> perkawinanEditBedaBanjarAdat(
            @Header("Authorization") String authHeader,
            @Part("purusa") RequestBody purusaId,
            @Part("pradana") RequestBody pradanaId,
            @Part("banjar_adat_pradana") RequestBody banjarAdatPradanaId,
            @Part("nomor_bukti_serah_terima_perkawinan") RequestBody noBuktiSerahTerima,
            @Part("nomor_akta_perkawinan") RequestBody noAkta,
            @Part("tanggal_perkawinan") RequestBody tanggalPerkawinan,
            @Part("keterangan") RequestBody keterangan,
            @Part("status_kekeluargaan") RequestBody statusKekeluargaan,
            @Part("calon_kepala_keluarga") RequestBody calonKepalaKeluargaId,
            @Part("nama_pemuput") RequestBody namaPemuput,
            @Part MultipartBody.Part file_bukti_serah_terima_perkawinan,
            @Part MultipartBody.Part file_akta_perkawinan,
            @Part("id") RequestBody id
    );


    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("admin/banjar-adat/perkawinan/sah-beda-banjar-adat")
    Call<PerkawinanDetailResponse> perkawinanBedaBanjarSah(
            @Header("Authorization") String authHeader,
            @Field("id_perkawinan") Integer id
    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("admin/banjar-adat/perkawinan/konfirmasi-beda-banjar-adat")
    Call<PerkawinanDetailResponse> perkawinanBedaBanjarKonfirmasi(
            @Header("Authorization") String authHeader,
            @Field("id_perkawinan") Integer id
    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("admin/banjar-adat/perkawinan/tolak-beda-banjar-adat")
    Call<PerkawinanDetailResponse> perkawinanBedaBanjarTolak(
            @Header("Authorization") String authHeader,
            @Field("id_perkawinan") Integer id,
            @Field("alasan_penolakan") String alasanPenolakan
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/perkawinan/delete-beda-banjar")
    Call<PerkawinanDetailResponse> perkawinanBedaBanjarDelete(
            @Header("Authorization") String authHeader,
            @Query("perkawinan") Integer id
    );

    @Headers({"Accept: application/json"})
    @Multipart
    @POST("admin/banjar-adat/perkawinan/store-campuran-masuk")
    Call<PerkawinanDetailResponse> perkawinanStoreCampuranMasuk(
            @Header("Authorization") String authHeader,
            @Part("purusa") RequestBody purusaId,
            @Part("nomor_bukti_serah_terima_perkawinan") RequestBody noBuktiSerahTerima,
            @Part("nomor_akta_perkawinan") RequestBody noAkta,
            @Part("tanggal_perkawinan") RequestBody tanggalPerkawinan,
            @Part("keterangan") RequestBody keterangan,
            @Part("status_kekeluargaan") RequestBody statusKekeluargaan,
            @Part("calon_kepala_keluarga") RequestBody calonKepalaKeluargaId,
            @Part("nama_pemuput") RequestBody namaPemuput,
            @Part MultipartBody.Part file_bukti_serah_terima_perkawinan,
            @Part MultipartBody.Part file_akta_perkawinan,
//            @Part("nik") RequestBody nikPradana,
//            @Part("gelar_depan") RequestBody gelarDepanPradana,
//            @Part("nama") RequestBody namaPradana,
//            @Part("gelar_belakang") RequestBody gelarBelakangPradana,
//            @Part("nama_alias") RequestBody namaAliasPradana,
//            @Part("tempat_lahir") RequestBody tempatLahirPradana,
//            @Part("tanggal_lahir") RequestBody tanggalLahirPradana,
//            @Part("jenis_kelamin") RequestBody jenisKelaminPradana,
//            @Part("golongan_darah") RequestBody golonganDarahPradana,
//            @Part("pekerjaan") RequestBody pekerjaanPradanaId,
//            @Part("pendidikan") RequestBody pendidikanPradanaId,
//            @Part("telepon") RequestBody teleponPradana,
            @Part MultipartBody.Part foto,
            @Part MultipartBody.Part file_sudhi_wadhani,
            @Part("perkawinan_json") RequestBody perkawinan,
            @Part("pradana_json") RequestBody pradanaJson
//            @Part("nik_ayah_pradana") RequestBody nikAyahPradana,
//            @Part("nama_ayah_pradana") RequestBody namaAyahPradana,
//            @Part("nik_ibu_pradana") RequestBody nikIbuPradana,
//            @Part("nama_ibu_pradana") RequestBody namaIbuPradana,
//            @Part("agama_asal_pradana") RequestBody agamaAsalPradana,
//            @Part("alamat_asal_pradana") RequestBody alamatAsalPradana,
//            @Part("desa_asal_pradana_id") RequestBody desaAsalPradanaId
    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("admin/banjar-adat/perkawinan/sah-campuran-masuk")
    Call<PerkawinanDetailResponse> perkawinanCampuranMasukSah(
            @Header("Authorization") String authHeader,
            @Field("id_perkawinan") Integer id
    );

    @Headers({"Accept: application/json"})
    @Multipart
    @POST("admin/banjar-adat/perkawinan/store-campuran-keluar")
    Call<PerkawinanDetailResponse> perkawinanStoreCampuranKeluar(
            @Header("Authorization") String authHeader,
            @Part MultipartBody.Part file_bukti_serah_terima_perkawinan,
            @Part MultipartBody.Part file_akta_perkawinan,
            @Part("perkawinan_json") RequestBody perkawinan
    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("admin/banjar-adat/perkawinan/sah-campuran-keluar")
    Call<PerkawinanDetailResponse> perkawinanCampuranKeluarSah(
            @Header("Authorization") String authHeader,
            @Field("id_perkawinan") Integer id
    );


//    @Headers({"Accept: application/json"})
//    @GET("admin/banjar-adat/perkawinan/get-sah")
//    Call<PerkawinanGetResponse> getPerkawinanSah(
//            @Header("Authorization") String authHeader
//    );
//
//    @Headers({"Accept: application/json"})
//    @GET("admin/banjar-adat/perkawinan/get-not-sah")
//    Call<PerkawinanGetResponse> getPerkawinanBelumSah(
//            @Header("Authorization") String authHeader
//    );
//
//    @Headers({"Accept: application/json"})
//    @GET("admin/banjar-adat/perkawinan/get-sah")
//    Call<PerkawinanGetResponse> getPerkawinanSahNextPage(
//            @Header("Authorization") String authHeader,
//            @Query("page") int page
//    );
//
//    @Headers({"Accept: application/json"})
//    @GET("admin/banjar-adat/perkawinan/get-not-sah")
//    Call<PerkawinanGetResponse> getPerkawinanBelumSahNextPage(
//            @Header("Authorization") String authHeader,
//            @Query("page") int page
//    );

    /**
     * Maperas things
     */

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/maperas/get")
    Call<MaperasGetResponse> getMaperas(
            @Header("Authorization") String authHeader,
            @Query("status") String status,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate,
            @Query("type") String type
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/maperas/get")
    Call<MaperasGetResponse> getMaperasNextPage(
            @Header("Authorization") String authHeader,
            @Query("status") String status,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate,
            @Query("type") String type,
            @Query("page") int page
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/maperas/detail")
    Call<MaperasDetailResponse> getDetailMaperas(
            @Header("Authorization") String authHeader,
            @Query("maperas") Integer idMaperas
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/maperas/get-daftar-anak")
    Call<AnggotaKramaMipilGetResponse> getDaftarAnak(
            @Header("Authorization") String authHeader,
            @Query("krama_mipil_id") Integer krama_mipil_id
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/maperas/get-krama-mipil-lama")
    Call<KramaMipilGetResponse> getKramaMipilLamaMaperas(
            @Header("Authorization") String authHeader,
            @Query("banjar_adat_id") Integer banjarAdatId
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/maperas/get-krama-mipil-baru")
    Call<KramaMipilGetResponse> getKramaMipilBaruMaperas(
            @Header("Authorization") String authHeader,
            @Query("krama_mipil_lama_id") Integer kramaLamaId,
            @Query("banjar_adat_id") Integer banjarAdatId
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/maperas/get-ortu-lama")
    Call<CacahKramaMipilDetailResponse> getOrtuLamaMaperas(
            @Header("Authorization") String authHeader,
            @Query("cacah_anak_id") Integer cacahAnakId
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/maperas/get-ortu-baru")
    Call<MaperasOrtuBaruGetResponse> getOrtuBatuMaperas(
            @Header("Authorization") String authHeader,
            @Query("krama_baru_id") Integer banjarAdatId
    );

    @Headers({"Accept: application/json"})
    @Multipart
    @POST("admin/banjar-adat/maperas/store-satu-banjar")
    Call<MaperasDetailResponse> storeMaperasSatuBanjar(
            @Header("Authorization") String authHeader,
            @Part MultipartBody.Part file_bukti_maperas,
            @Part MultipartBody.Part file_akta_pengangkatan_anak,
            @Part("maperas_json") RequestBody perkawinan
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/maperas/delete-beda-banjar")
    Call<MaperasDetailResponse> maperasBedaBanjarDelete(
            @Header("Authorization") String authHeader,
            @Query("maperas") Integer id
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/maperas/sah-satu-banjar")
    Call<MaperasDetailResponse> maperasSatuBanjarSah(
            @Header("Authorization") String authHeader,
            @Query("maperas") Integer id
    );

    @Headers({"Accept: application/json"})
    @Multipart
    @POST("admin/banjar-adat/maperas/store-beda-banjar-adat")
    Call<MaperasDetailResponse> storeMaperasBedaBanjar(
            @Header("Authorization") String authHeader,
            @Part MultipartBody.Part file_bukti_maperas,
            @Part MultipartBody.Part file_akta_pengangkatan_anak,
            @Part("maperas_json") RequestBody perkawinan
    );

    @Headers({"Accept: application/json"})
    @Multipart
    @POST("admin/banjar-adat/maperas/edit-beda-banjar-adat")
    Call<MaperasDetailResponse> editMaperasBedaBanjar(
            @Header("Authorization") String authHeader,
            @Part MultipartBody.Part file_bukti_maperas,
            @Part MultipartBody.Part file_akta_pengangkatan_anak,
            @Part("maperas_json") RequestBody perkawinan
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/maperas/sah-beda-banjar-adat")
    Call<MaperasDetailResponse> maperasBedaBanjarSah(
            @Header("Authorization") String authHeader,
            @Query("maperas") Integer id
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/maperas/konfirmasi-beda-banjar-adat")
    Call<MaperasDetailResponse> maperasBedaBanjarKonfirmasi(
            @Header("Authorization") String authHeader,
            @Query("maperas") Integer id
    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("admin/banjar-adat/maperas/tolak-beda-banjar-adat")
    Call<MaperasDetailResponse> maperasBedaBanjarTolak(
            @Header("Authorization") String authHeader,
            @Query("maperas") Integer id,
            @Field("alasan_penolakan") String alasanPenolakan
    );

    @Headers({"Accept: application/json"})
    @Multipart
    @POST("admin/banjar-adat/maperas/store-campuran-masuk")
    Call<MaperasDetailResponse> storeMaperasCampuranMasuk(
            @Header("Authorization") String authHeader,
            @Part MultipartBody.Part file_bukti_maperas,
            @Part MultipartBody.Part file_akta_pengangkatan_anak,
            @Part("maperas_json") RequestBody maperas,
            @Part("anak_json") RequestBody anak,
            @Part MultipartBody.Part foto,
            @Part MultipartBody.Part file_sudhi_wadhani
    );

    @Headers({"Accept: application/json"})
    @Multipart
    @POST("admin/banjar-adat/maperas/store-campuran-keluar")
    Call<MaperasDetailResponse> storeMaperasCampuranKeluar(
            @Header("Authorization") String authHeader,
            @Part MultipartBody.Part file_bukti_maperas,
            @Part MultipartBody.Part file_akta_pengangkatan_anak,
            @Part("maperas_json") RequestBody perkawinan
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/maperas/sah-campuran-masuk")
    Call<MaperasDetailResponse> maperasCampuranMasukSah(
            @Header("Authorization") String authHeader,
            @Query("maperas") Integer id
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/maperas/sah-campuran-keluar")
    Call<MaperasDetailResponse> maperasCampuranKeluarSah(
            @Header("Authorization") String authHeader,
            @Query("maperas") Integer id
    );


    /**
     * Perceraian things
     */

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/perceraian/get")
    Call<PerceraianGetResponse> getPerceraian(
            @Header("Authorization") String authHeader,
            @Query("status") String status,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/perceraian/get")
    Call<PerceraianGetResponse> getPerceraianNextPage(
            @Header("Authorization") String authHeader,
            @Query("status") String status,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate,
            @Query("page") int page
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/perceraian/detail")
    Call<PerceraianDetailResponse> getDetailPerceraian(
            @Header("Authorization") String authHeader,
            @Query("perceraian") Integer perceraian
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/perceraian/get-list-krama-mipil")
    Call<KramaMipilGetResponse> perceraianGetListKrama(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("admin/banjar-adat/perceraian/get-list-krama-tujuan")
    Call<KramaMipilGetResponse> perceraianGetListKramaTujuan(
            @Header("Authorization") String authHeader,
            @Field("krama_mipil_saat_ini") Integer kramaMipilSaatIni,
            @Field("banjar_adat_krama_mipil") Integer banjarAdatBaru
    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("admin/banjar-adat/perceraian/get-list-krama-pasangan")
    Call<KramaMipilGetResponse> perceraianGetListKramaTujuanPasangan(
            @Header("Authorization") String authHeader,
            @Field("krama_mipil_saat_ini") Integer kramaMipilSaatIni,
            @Field("banjar_adat_pasangan") Integer banjarAdatBaru,
            @Field("krama_mipil_krama_mipil") Integer kramaTujuanPasangan
    );


    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/perceraian/get-krama-mipil-selected")
    Call<PerceraianGetKramaCerai> perceraianGetKramaCerai(
            @Header("Authorization") String authHeader,
            @Query("krama") Integer id
    );

    @Headers({"Accept: application/json"})
    @Multipart
    @POST("admin/banjar-adat/perceraian/store")
    Call<PerceraianDetailResponse> storePerceraian(
            @Header("Authorization") String authHeader,
            @Part MultipartBody.Part file_bukti_perceraian,
            @Part MultipartBody.Part file_akta_perceraian,
            @Part("perceraian_json") RequestBody perceraian_json,
            @Part("anggota_keluarga_json") RequestBody anggota_keluarga_json
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/perceraian/sah")
    Call<PerceraianDetailResponse> perceraianSah(
            @Header("Authorization") String authHeader,
            @Query("perceraian") Integer id
    );

    @Headers({"Accept: application/json"})
    @GET("admin/banjar-adat/perceraian/konfirmasi")
    Call<PerceraianDetailResponse> perceraianKonfirmasi(
            @Header("Authorization") String authHeader,
            @Query("perceraian") Integer id
    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("admin/banjar-adat/perceraian/tolak")
    Call<PerceraianDetailResponse> perceraianTolak(
            @Header("Authorization") String authHeader,
            @Query("perceraian") Integer id,
            @Field("alasan_penolakan") String alasanPenolakan
    );


    /**
     * Notifikasi things
     */

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("notifikasi/store-firebase-token")
    Call<ResponseGeneral> sendFcmToken(
            @Header("Authorization") String authHeader,
            @Field("firebase_token") String fcmToken
    );

    @Headers({"Accept: application/json"})
    @GET("notifikasi/get-notifikasi/kelihan_adat")
    Call<NotifikasiGetResponse> getNotifikasi(
            @Header("Authorization") String authHeader
    );

    @Headers({"Accept: application/json"})
    @GET("notifikasi/read-all-notifikasi/kelihan_adat")
    Call<NotifikasiGetResponse> readAllNotifikasi(
            @Header("Authorization") String authHeader
    );
}
