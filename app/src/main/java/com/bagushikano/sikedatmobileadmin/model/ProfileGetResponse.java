package com.bagushikano.sikedatmobileadmin.model;

import com.bagushikano.sikedatmobileadmin.model.krama.KramaMipil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileGetResponse {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private KramaMipil kramaMipil;
    @SerializedName("jabatan")
    @Expose
    private String jabatan;
    @SerializedName("message")
    @Expose
    private String message;
}
