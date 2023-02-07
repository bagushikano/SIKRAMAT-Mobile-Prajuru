package com.bagushikano.sikedatmobileadmin.model;


import com.bagushikano.sikedatmobileadmin.model.master.DesaAdat;
import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarAdat;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardDataKramaResponse {
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
    @SerializedName("cacahKramaTamiu")
    @Expose
    private Integer cacahKramaTamiu;

    @SerializedName("cacahTamiu")
    @Expose
    private Integer cacahTamiu;

    @SerializedName("tamiu")
    @Expose
    private Integer tamiu;

    @SerializedName("message")
    @Expose
    private String message;
}
