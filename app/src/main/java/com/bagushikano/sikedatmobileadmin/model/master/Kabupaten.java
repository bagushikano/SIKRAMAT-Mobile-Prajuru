package com.bagushikano.sikedatmobileadmin.model.master;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Kabupaten {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("provinsi_id")
    @Expose
    public String provinsiId;
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
    @SerializedName("provinsi")
    @Expose
    public Provinsi provinsi;
}
