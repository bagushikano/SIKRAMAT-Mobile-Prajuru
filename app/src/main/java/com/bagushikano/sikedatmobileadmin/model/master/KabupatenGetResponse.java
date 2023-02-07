package com.bagushikano.sikedatmobileadmin.model.master;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KabupatenGetResponse {
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public List<Kabupaten> data = null;
    @SerializedName("message")
    @Expose
    public String message;
}
