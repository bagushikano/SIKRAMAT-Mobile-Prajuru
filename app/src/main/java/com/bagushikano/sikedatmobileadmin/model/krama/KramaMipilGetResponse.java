package com.bagushikano.sikedatmobileadmin.model.krama;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KramaMipilGetResponse {
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public KramaMipilPaginate kramaMipilPaginate;
    @SerializedName("message")
    @Expose
    public String message;
}
