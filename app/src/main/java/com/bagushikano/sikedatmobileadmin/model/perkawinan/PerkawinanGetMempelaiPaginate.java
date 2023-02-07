package com.bagushikano.sikedatmobileadmin.model.perkawinan;

import com.bagushikano.sikedatmobileadmin.model.cacahkrama.CacahKramaMipil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PerkawinanGetMempelaiPaginate {
    @SerializedName("current_page")
    @Expose
    public Integer currentPage;
    @SerializedName("data")
    @Expose
    public List<CacahKramaMipil> cacahKramaMipilList = null;
    @SerializedName("first_page_url")
    @Expose
    public String firstPageUrl;
    @SerializedName("from")
    @Expose
    public Integer from;
    @SerializedName("last_page")
    @Expose
    public Integer lastPage;
    @SerializedName("last_page_url")
    @Expose
    public String lastPageUrl;
    @SerializedName("next_page_url")
    @Expose
    public String nextPageUrl;
    @SerializedName("path")
    @Expose
    public String path;
    @SerializedName("per_page")
    @Expose
    public Integer perPage;
    @SerializedName("prev_page_url")
    @Expose
    public Object prevPageUrl;
    @SerializedName("to")
    @Expose
    public Integer to;
    @SerializedName("total")
    @Expose
    public Integer total;

}
