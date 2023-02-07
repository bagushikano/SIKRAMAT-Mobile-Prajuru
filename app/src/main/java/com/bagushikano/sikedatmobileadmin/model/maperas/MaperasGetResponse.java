package com.bagushikano.sikedatmobileadmin.model.maperas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaperasGetResponse {
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public MaperasPaginate maperasPaginate;
    @SerializedName("message")
    @Expose
    public String message;
}
