package com.chan.samples.news.data.remote;

import com.chan.samples.news.data.models.Bookmark;

import java.util.List;

/**
 * Created by chan on 2/15/18.
 */

public interface BookmarkCallback {
    void fetchedBookmark(List<Bookmark> bookmarks);
    void loadedError();
}
