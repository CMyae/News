package com.chan.samples.news.data.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.chan.samples.news.utils.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chan on 1/13/18.
 */

@Entity(tableName = "article_response")
public class ArticleResponse implements Parcelable{

    @Ignore public static final int TYPE_HEADLINE = 100;
    @Ignore public static final int TYPE_LATEST = 200;

    @PrimaryKey private long id;

    @Expose @SerializedName("status") private String status;

    @Expose @SerializedName("totalResults") private int totalResult;

    @Ignore
    @Expose
    @SerializedName("articles")
    private List<Article> articles;

    private long bookmark_id;
    private int type;

    public ArticleResponse() {
    }

    @Ignore
    public ArticleResponse(List<Article> articles) {
        this.articles = articles;
    }

    protected ArticleResponse(Parcel in) {
        id = in.readLong();
        status = in.readString();
        totalResult = in.readInt();
        articles = in.createTypedArrayList(Article.CREATOR);
        bookmark_id = in.readLong();
        type = in.readInt();
    }

    public static final Creator<ArticleResponse> CREATOR = new Creator<ArticleResponse>() {
        @Override
        public ArticleResponse createFromParcel(Parcel in) {
            return new ArticleResponse(in);
        }

        @Override
        public ArticleResponse[] newArray(int size) {
            return new ArticleResponse[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBookmark_id() {
        return bookmark_id;
    }

    public void setBookmark_id(long bookmark_id) {
        this.bookmark_id = bookmark_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public void setArticleResponseId(){
        for(Article article: articles){
            article.setArticle_response_id(id);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(status);
        dest.writeInt(totalResult);
        dest.writeTypedList(articles);
        dest.writeLong(bookmark_id);
        dest.writeInt(type);
    }
}
