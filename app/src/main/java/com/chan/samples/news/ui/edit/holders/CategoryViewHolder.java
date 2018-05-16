package com.chan.samples.news.ui.edit.holders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chan.samples.news.R;
import com.chan.samples.news.data.models.Bookmark;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chan on 2/21/18.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvCategoryName) TextView tvCategoryName;
    @BindView(R.id.chkCategory) CheckBox chkCategory;

    private List<Bookmark> categories;

    public CategoryViewHolder(View itemView, final List<Bookmark> bookmarks) {
        super(itemView);
        ButterKnife.bind(this,itemView);

        this.categories = bookmarks;

        chkCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bookmark bookmark = categories.get(getAdapterPosition());
                if(bookmark == null) return;
                boolean isChecked = chkCategory.isChecked();
                if(isChecked){
                    bookmark.setStatus(Bookmark.CHECKED);
                }else{
                    bookmark.setStatus(Bookmark.UNCHECKED);
                }
            }
        });
    }


    public void bindView(Bookmark category,boolean isEditable){

        if(isEditable){
            chkCategory.setEnabled(true);
        }else{
            chkCategory.setEnabled(false);
        }

        tvCategoryName.setText(category.getName());

        if(category.getStatus() == Bookmark.BOOKMARKED){
            chkCategory.setVisibility(View.VISIBLE);
            chkCategory.setChecked(true);
        }else{
            if (isEditable) {
                chkCategory.setVisibility(View.VISIBLE);
                chkCategory.setChecked(false);
            } else {
                chkCategory.setVisibility(View.GONE);
            }
        }

    }
}
