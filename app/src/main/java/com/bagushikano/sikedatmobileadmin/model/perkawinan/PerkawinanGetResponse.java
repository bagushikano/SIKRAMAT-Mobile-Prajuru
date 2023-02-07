package com.bagushikano.sikedatmobileadmin.model.perkawinan;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerkawinanGetResponse {
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public PerkawinanPaginate perkawinanPaginate;
    @SerializedName("message")
    @Expose
    public String message;
}
