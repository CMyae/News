package com.chan.samples.news.ui.edit;

import com.chan.samples.news.data.models.Bookmark;

import java.util.List;

/**
 * Created by chan on 2/20/18.
 */

public interface EditContract {

    interface EditView{
        void showLoadingProgress();
        void hideLoadingProgress();
        void showMessage(int resId);
        void onSourceLoaded();
        void showAllBookmarks();
        void showEditableBookmarks();
        void hideEditableBookmarks();
        void showDialog();
        void hideDialog();
        void onEditCompleted();
    }

    interface EditMvpPresenter{
        void loadSources();
        void editBookmarks(String id,List<Bookmark> categories,List<Bookmark> channel);
    }
}
