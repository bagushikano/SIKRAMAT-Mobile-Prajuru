package com.bagushikano.sikedatmobileadmin.model;

import com.bagushikano.sikedatmobileadmin.model.master.DesaAdat;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarAdat;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardDataResponse {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("kramaMipil")
    @Expose
    private Integer kramaMipil;
    @SerializedName("kramaTamiu")
    @Expose
    private Integer kramaTamiu;
    @SerializedName("cacahMipil")
    @Expose
    private Integer cacahMipil;
    @SerializedName("cacahTamiu")
    @Expose
    private Integer cacahTamiu;

    @SerializedName("cacahTamiu2")
    @Expose
    private Integer cacahTamiu2;

    @SerializedName("tamiu")
    @Expose
    private Integer tamiu;

    @SerializedName("kelahiran")
    @Expose
    private Integer kelahiran;

    @SerializedName("kematian")
    @Expose
    private Integer kematian;

    @SerializedName("perkawinan")
    @Expose
    private Integer perkawinan;

    @SerializedName("perceraian")
    @Expose
    private Integer perceraian;

    @SerializedName("maperas")
    @Expose
    private Integer maperas;

    @SerializedName("banjarAdat")
    @Expose
    private BanjarAdat banjarAdat;
    @SerializedName("desaAdat")
    @Expose
    private DesaAdat desaAdat;
    @SerializedName("message")
    @Expose
    private String message;
}
