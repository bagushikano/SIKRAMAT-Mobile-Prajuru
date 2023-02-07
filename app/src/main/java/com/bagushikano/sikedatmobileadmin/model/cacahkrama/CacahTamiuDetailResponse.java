package com.bagushikano.sikedatmobileadmin.model.cacahkrama;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CacahTamiuDetailResponse {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private CacahTamiu cacahTamiu;
    @SerializedName("message")
    @Expose
    private String message;
}
