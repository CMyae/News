package com.chan.samples.news.ui.views;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by chan on 1/17/18.
 */

public class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;
    private OnLoadMoreListener listener;

    private boolean isLoading;

    public EndlessScrollListener(LinearLayoutManager layoutManager){
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        int totalItemCount = layoutManager.getItemCount()-1;

        if(!isLoading && totalItemCount <= lastVisibleItem){
            if(listener != null){
                isLoading = true;
                listener.onLoadMore();
            }
        }

    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.listener = listener;
    }

    public void setLoading() {
        isLoading = false;
        Log.i("TAG",isLoading + " is loading status");
    }

    public void stopLoading(){
        isLoading = true;
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }

}
