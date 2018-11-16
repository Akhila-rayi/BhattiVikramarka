package com.versatilemobitech.bhattivikramarka.models;

/**
 * Created by Excentd11 on 10/17/2017.
 */

public class Videos {
    public String id,videoname,youtubeurl,youtubeid,image;

    public Videos(String id, String videoname, String youtubeurl, String youtubeid, String image) {
        this.id = id;
        this.videoname = videoname;
        this.youtubeurl = youtubeurl;
        this.youtubeid = youtubeid;
        this.image = image;
    }
  /*public int image;

    public Videos(int image) {
        this.image = image;
    }*/
}
