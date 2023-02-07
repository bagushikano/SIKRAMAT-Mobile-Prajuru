package com.bagushikano.sikedatmobileadmin.model.krama;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TamiuGetResponse {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private TamiuPaginate tamiuPaginate;
    @SerializedName("message")
    @Expose
    private String message;
}
