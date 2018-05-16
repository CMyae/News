package com.chan.samples.news.data.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.chan.samples.news.utils.Constants;
import com.google.gson.annotations.Expose;

/**
 * Created by chan on 1/18/18.
 */

@Entity(tableName = "bookmark")
public class Bookmark implements Parcelable{

    @Ignore public static final int TYPE_CATEGORY = 0;
    @Ignore public static final int TYPE_SOURCE = 1;

    @Ignore public static final int BOOKMARKED = 100;
    @Ignore public static final int CHECKED = 200;
    @Ignore public static final int UNCHECKED = 300;


    @Expose @PrimaryKey private long bookmark_id;

    @Expose private String name;
    @Expose private int type;
    @Ignore private int status = UNCHECKED;


    public Bookmark(long bookmark_id,String name, int type) {
        this.bookmark_id = bookmark_id;
        this.name = name;
        this.type = type;
    }

    @Ignore
    public Bookmark(){

    }

    protected Bookmark(Parcel in) {
        bookmark_id = in.readLong();
        name = in.readString();
        type = in.readInt();
    }

    public static final Creator<Bookmark> CREATOR = new Creator<Bookmark>() {
        @Override
        public Bookmark createFromParcel(Parcel in) {
            return new Bookmark(in);
        }

        @Override
        public Bookmark[] newArray(int size) {
            return new Bookmark[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getBookmark_id() {
        return bookmark_id;
    }

    public void setBookmark_id(long bookmark_id) {
        this.bookmark_id = bookmark_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(bookmark_id);
        dest.writeString(name);
        dest.writeInt(type);
    }

    @Override
    public String toString() {
        return getBookmark_id() + "  " + getName() + "  " + getType();
    }
}
