package com.versatilemobitech.bhattivikramarka.models;

/**
 * Created by Excentd11 on 10/17/2017.
 */

public class Gallery {
    public String id, image,foldername,folderid;

    public Gallery(String id, String image, String foldername,String folderid) {
        this.id = id;
        this.image = image;
        this.foldername = foldername;
        this.folderid=folderid;
    }

   /*public int image;

    public Gallery(int image) {
        this.image = image;
    }*/
}
