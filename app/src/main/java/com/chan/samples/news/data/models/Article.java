package com.chan.samples.news.data.models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by chan on 1/13/18.
 */

@Entity(tableName = "article")
public class Article implements Parcelable{

    @PrimaryKey(autoGenerate = true) private long id;

    @Embedded(prefix = "source_")
    @Expose
    @SerializedName("source")
    private Source source;

    @Expose @SerializedName("author") private String author;

    @Expose @SerializedName("title") private String title;

    @Expose @SerializedName("description") private String desc;

    @Expose @SerializedName("url") private String url;

    @Expose @SerializedName("urlToImage") private String urlToImage;

    @Expose @SerializedName("publishedAt") private String timeStamp;

    private long article_response_id;

    public Article(){

    }

    protected Article(Parcel in) {
        author = in.readString();
        title = in.readString();
        desc = in.readString();
        url = in.readString();
        urlToImage = in.readString();
        timeStamp = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeString(url);
        dest.writeString(urlToImage);
        dest.writeString(timeStamp);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getArticle_response_id() {
        return article_response_id;
    }

    public void setArticle_response_id(long article_response_id) {
        this.article_response_id = article_response_id;
    }
}
