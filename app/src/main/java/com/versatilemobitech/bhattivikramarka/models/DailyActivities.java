package com.versatilemobitech.bhattivikramarka.models;

/**
 * Created by Excentd11 on 10/17/2017.
 */

public class DailyActivities {
    public String id,image,title,description,posted_on;

    public DailyActivities(String id,  String image,  String title, String description, String posted_on) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.description = description;
        this.posted_on = posted_on;
    }
  /*  public int image;

    public DailyActivities(int image) {
        this.image = image;
    }*/
}
