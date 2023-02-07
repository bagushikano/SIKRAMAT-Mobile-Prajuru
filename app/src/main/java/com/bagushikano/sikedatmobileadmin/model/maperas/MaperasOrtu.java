package com.bagushikano.sikedatmobileadmin.model.maperas;

import com.bagushikano.sikedatmobileadmin.model.krama.AnggotaKramaMipil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaperasOrtu {
    @SerializedName("ibu")
    @Expose
    public List<AnggotaKramaMipil> ibuList = null;
    @SerializedName("ayah")
    @Expose
    public List<AnggotaKramaMipil> ayahList = null;
}
