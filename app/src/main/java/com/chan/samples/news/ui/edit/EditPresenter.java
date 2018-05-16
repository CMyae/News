package com.chan.samples.news.ui.edit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.chan.samples.news.R;
import com.chan.samples.news.data.DataManager;
import com.chan.samples.news.data.models.Bookmark;
import com.chan.samples.news.data.models.Source;
import com.chan.samples.news.data.models.SourceResponse;
import com.chan.samples.news.di.ActivityContext;
import com.chan.samples.news.di.View;
import com.chan.samples.news.utils.NetworkUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chan on 2/20/18.
 */

public class EditPresenter implements EditContract.EditMvpPresenter {

    private EditContract.EditView view;
    private DataManager dataManager;
    private Context context;

    @Inject
    public EditPresenter(@ActivityContext Context context,@View Object view, DataManager dataManager) {
        this.context = context;
        this.view = (EditContract.EditView) view;
        this.dataManager = dataManager;
    }


    @Override
    public void loadSources() {
        Call<SourceResponse> call = dataManager.getRemoteController().getRemoteApi()
                .getAllSources();

        call.enqueue(new Callback<SourceResponse>() {
            @Override
            public void onResponse(Call<SourceResponse> call, Response<SourceResponse> response) {
                List<Source> sources = response.body().getSources();
                List<Bookmark> bookmarks = new ArrayList<>();

                for(Source source: sources){
                    Bookmark b = new Bookmark(0,source.getId(),Bookmark.TYPE_SOURCE);
                    bookmarks.add(b);
                }
                dataManager.getPrefHelper().saveDefaultChannels(bookmarks);

                view.onSourceLoaded();
            }

            @Override
            public void onFailure(Call<SourceResponse> call, Throwable t) {
                view.showMessage(R.string.message_error_connection);
            }
        });
    }


    @Override
    public void editBookmarks(String id,List<Bookmark> categories, List<Bookmark> channel) {
        //no item selected
        if(categories.size() == 0 && channel.size() ==0){
            view.showMessage(R.string.message_no_item_selected);
            return;
        }

        if(!NetworkUtils.isNetworkConnected(context)){
            view.showMessage(R.string.message_error_connection);
            return;
        }

        final List<Bookmark> bookmarks = new ArrayList<>();
        bookmarks.addAll(categories);
        bookmarks.addAll(channel);

        view.showDialog();

        dataManager.storeBookmarkInNetwork(id,bookmarks).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    dataManager.getPrefHelper().saveUserBookmark(bookmarks);
                    view.onEditCompleted();
                }else{
                    view.showMessage(R.string.message_error_connection);
                }
            }
        });
    }
}
