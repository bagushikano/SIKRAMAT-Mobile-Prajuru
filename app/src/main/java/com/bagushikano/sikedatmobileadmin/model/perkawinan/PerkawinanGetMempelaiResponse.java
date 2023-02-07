package com.bagushikano.sikedatmobileadmin.model.perkawinan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PerkawinanGetMempelaiResponse {
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public PerkawinanGetMempelaiPaginate perkawinanGetMempelaiPaginate;
    @SerializedName("message")
    @Expose
    public String message;

}
