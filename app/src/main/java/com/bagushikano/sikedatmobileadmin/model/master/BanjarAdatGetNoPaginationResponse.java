package com.bagushikano.sikedatmobileadmin.model.master;

import com.bagushikano.sikedatmobileadmin.model.master.banjar.BanjarAdat;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BanjarAdatGetNoPaginationResponse {
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("data")
    @Expose
    public List<BanjarAdat> data = null;
    @SerializedName("message")
    @Expose
    public String message;
}
