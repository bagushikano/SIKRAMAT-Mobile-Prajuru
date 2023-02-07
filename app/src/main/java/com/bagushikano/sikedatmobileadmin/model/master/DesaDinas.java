package com.bagushikano.sikedatmobileadmin.model.master;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DesaDinas {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("kecamatan_id")
    @Expose
    public String kecamatanId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("created_at")
    @Expose
    public Object createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    public Object deletedAt;
    @SerializedName("kecamatan")
    @Expose
    public Kecamatan kecamatan;
}
