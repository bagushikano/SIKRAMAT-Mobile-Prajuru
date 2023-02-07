package com.bagushikano.sikedatmobileadmin.model.maperas;

import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaperasOrtuBaruGetResponse {
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("ayah")
    @Expose
    public List<CacahKramaMipil> ayahList = null;
    @SerializedName("ibu")
    @Expose
    public List<CacahKramaMipil> ibuList = null;
    @SerializedName("message")
    @Expose
    public String message;
}
