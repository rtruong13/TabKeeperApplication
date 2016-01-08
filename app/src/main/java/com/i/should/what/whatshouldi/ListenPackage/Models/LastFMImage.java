package com.i.should.what.whatshouldi.ListenPackage.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ryan on 7/28/2015.
 */
public class LastFMImage implements Serializable {

    @SerializedName("#text")
    @Expose
    private String Text;
    @SerializedName("size")
    @Expose
    private String size;

    /**
     *
     * @return
     * The Text
     */
    public String getPath() {
        return Text;
    }

    /**
     *
     * @param Text
     * The #text
     */
    public void setText(String Text) {
        this.Text = Text;
    }

    /**
     *
     * @return
     * The size
     */
    public String getSize() {
        return size;
    }

    /**
     *
     * @param size
     * The size
     */
    public void setSize(String size) {
        this.size = size;
    }

}