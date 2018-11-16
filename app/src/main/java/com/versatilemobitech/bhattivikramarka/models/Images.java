package com.versatilemobitech.bhattivikramarka.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Excentd11 on 10/17/2017.
 */

public class Images implements Parcelable {
    public String  id,imageUrl,folder_id;

    public Images( String id,String imageInner,String folder_id) {
        this.imageUrl = imageInner;
        this.folder_id=folder_id;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(String folder_id) {
        this.folder_id = folder_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.imageUrl);
        dest.writeString(this.folder_id);
    }

    protected Images(Parcel in) {
        this.id = in.readString();
        this.imageUrl = in.readString();
        this.folder_id = in.readString();
    }

    public static final Parcelable.Creator<Images> CREATOR = new Parcelable.Creator<Images>() {
        @Override
        public Images createFromParcel(Parcel source) {
            return new Images(source);
        }

        @Override
        public Images[] newArray(int size) {
            return new Images[size];
        }
    };
}
