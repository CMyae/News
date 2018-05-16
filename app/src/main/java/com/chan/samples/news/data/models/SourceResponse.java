package com.chan.samples.news.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chan on 2/19/18.
 */

public class SourceResponse {

    @Expose @SerializedName("status") private String status;
    @Expose @SerializedName("sources") private List<Source> sources;

    public SourceResponse() {
    }

    public SourceResponse(String status, List<Source> sources) {
        this.status = status;
        this.sources = sources;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }
}
