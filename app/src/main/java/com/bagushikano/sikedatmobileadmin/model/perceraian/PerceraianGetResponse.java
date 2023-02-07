package com.bagushikano.sikedatmobileadmin.model.perceraian;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerceraianGetResponse {
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public PerceraianPaginate perceraianPaginate;
    @SerializedName("message")
    @Expose
    public String message;
}
