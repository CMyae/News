package com.chan.samples.news.ui.edit;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chan.samples.news.NewsApp;
import com.chan.samples.news.R;
import com.chan.samples.news.data.DataManager;
import com.chan.samples.news.data.models.Bookmark;
import com.chan.samples.news.di.components.DaggerActivityComponent;
import com.chan.samples.news.di.modules.ActivityModule;
import com.chan.samples.news.ui.edit.adapters.CategoryAdapter;
import com.chan.samples.news.ui.edit.adapters.ChannelAdapter;
import com.chan.samples.news.ui.main.MainActivity;
import com.facebook.Profile;
import com.google.android.gms.common.api.BooleanResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


//TODO: webview progress hide
public class EditBookmarkFragment extends Fragment implements EditContract.EditView{

    private static final String TAG = "EditBookmarkFragment";

    @BindView(R.id.progressContainer) FrameLayout progressContainer;
    @BindView(R.id.rvCategory) RecyclerView rvCategory;
    @BindView(R.id.rvChannel) RecyclerView rvChannel;
    @BindView(R.id.tvEdit) TextView tvEdit;


    @Inject EditContract.EditMvpPresenter presenter;
    @Inject DataManager dataManager;
    private FirebaseUser user;

    private List<Bookmark> categoryBookmark = new ArrayList<>();
    private List<Bookmark> channelBookmark = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private ChannelAdapter channelAdapter;

    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_bookmark, container, false);

        ButterKnife.bind(this,view);
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(getContext(),this))
                .applicationComponent(NewsApp.getApp().getComponent())
                .build()
                .inject(this);

        user = FirebaseAuth.getInstance().getCurrentUser();

        initView();

        //no channel data load yet
        if(!dataManager.getPrefHelper().existChannel()){
            presenter.loadSources();
        }else{
            onSourceLoaded();
        }


        return view;
    }


    /**
     * Initialize all views
     */
    private void initView(){
        rvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCategory.setHasFixedSize(true);
        rvCategory.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        categoryAdapter = new CategoryAdapter(getContext(),categoryBookmark);
        rvCategory.setAdapter(categoryAdapter);


        LinearLayoutManager horizontalLayout = new LinearLayoutManager(
                getContext(),LinearLayoutManager.HORIZONTAL,false
        );
        rvChannel.setLayoutManager(horizontalLayout);
        rvChannel.setHasFixedSize(true);
        channelAdapter = new ChannelAdapter(getContext(),channelBookmark);
        rvChannel.setAdapter(channelAdapter);


    }



    /**
     * Initialize bookmark data
     */
    private void initData(){
        categoryBookmark.clear();
        channelBookmark.clear();
        Bookmark[] userBookmarks = dataManager.getPrefHelper().getUserBookmark();
        Bookmark[] allBookmarks = dataManager.getPrefHelper().getAllBookmarks();

        //find already bookmarked item
        for(Bookmark uBookmark : userBookmarks){

            for(Bookmark aBookmark: allBookmarks){
                if(uBookmark.getBookmark_id() == aBookmark.getBookmark_id()){
                    aBookmark.setStatus(Bookmark.BOOKMARKED);
                    break;
                }
            }
        }

        for(Bookmark b: allBookmarks){
            if(b.getType() == Bookmark.TYPE_CATEGORY){
                categoryBookmark.add(b);
            }else{
                channelBookmark.add(b);
            }
        }
    }



    @OnClick(R.id.tvCancel)
    public void onCancelClick(View v){
        if(isEditMode()){
            hideEditableBookmarks();
            tvEdit.setText("Edit");
        }else {
            ((MainActivity) getActivity()).hideBottomSheet();
        }
    }


    @OnClick(R.id.tvEdit)
    public void onEditClick(View v){

        if(user == null){
            showMessage(R.string.message_login);
            return;
        }

        if(!isEditMode()) {
            tvEdit.setText(getString(R.string.confirm));
            showEditableBookmarks();
        }else{
            //user click ok
            List<Bookmark> categories = categoryAdapter.getSelectedCategories();
            List<Bookmark> channel = channelAdapter.getSelectedChannel();

            presenter.editBookmarks(Profile.getCurrentProfile().getId(),categories,channel);

        }
    }

    public boolean isEditMode(){
        return categoryAdapter.isEditable();
    }

    @Override
    public void showLoadingProgress() {
        progressContainer.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideLoadingProgress() {
        progressContainer.setVisibility(View.GONE);
    }


    @Override
    public void showMessage(int resId) {
        Toast.makeText(getContext(), getString(resId), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSourceLoaded() {
        initData();
        hideLoadingProgress();
        showAllBookmarks();
    }


    @Override
    public void showAllBookmarks() {
        categoryAdapter.notifyDataSetChanged();
        channelAdapter.notifyDataSetChanged();
    }


    @Override
    public void showEditableBookmarks() {
        categoryAdapter.setEditable(true);
        channelAdapter.setEditable(true);
    }

    @Override
    public void hideEditableBookmarks() {
        initData(); //reset bookmark data again
        categoryAdapter.setEditable(false);
        channelAdapter.setEditable(false);
    }


    @Override
    public void onEditCompleted() {
        showMessage(R.string.message_edited);
        hideDialog();
        ((MainActivity)getActivity()).hideBottomSheet();
        ((MainActivity)getActivity()).onBookmarkEdited();
    }


    @Override
    public void showDialog() {
        if(dialog == null) {
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("Updating Bookmark");
            dialog.setCancelable(false);
            dialog.setIndeterminateDrawable(getContext().getDrawable(R.drawable.custom_progress_bar));
        }
        dialog.show();
    }

    @Override
    public void hideDialog() {
        dialog.dismiss();
    }


}
