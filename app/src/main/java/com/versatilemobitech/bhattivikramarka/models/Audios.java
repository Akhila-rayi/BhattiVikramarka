package com.versatilemobitech.bhattivikramarka.models;

/**
 * Created by user on 27-12-17.
 */

public class Audios {
    public String id, image,title,description, audiourl;

    public Audios(String id,String image, String title, String description,String audiourl) {
        this.id=id;
        this.image = image;
        this.title = title;
        this.description=description;
        this.audiourl = audiourl;
    }
}
