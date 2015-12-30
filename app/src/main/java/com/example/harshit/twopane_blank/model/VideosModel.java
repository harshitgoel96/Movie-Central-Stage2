
package com.example.harshit.twopane_blank.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VideosModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Video> results = new ArrayList<Video>();

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The videos
     */
    public List<Video> getResults() {
        return results;
    }

    /**
     * 
     * @param videos
     *     The videos
     */
    public void setResults(List<Video> videos) {
        this.results = videos;
    }

}
