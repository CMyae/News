package com.chan.samples.news.ui.edit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chan.samples.news.R;
import com.chan.samples.news.data.models.Bookmark;
import com.chan.samples.news.ui.edit.holders.CategoryViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chan on 2/20/18.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder>{

    private Context context;
    private List<Bookmark> categories;
    private boolean isEditable;

    public CategoryAdapter(Context context, List<Bookmark> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_row_category,parent,false);
        return new CategoryViewHolder(v,categories);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.bindView(categories.get(position),isEditable);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public void setEditable(boolean edit){
        isEditable = edit;
        notifyDataSetChanged();
    }

    public boolean isEditable() {
        return isEditable;
    }


    public List<Bookmark> getSelectedCategories(){
        List<Bookmark> bookmarks = new ArrayList<>();
        for(Bookmark b : categories){
            if(b.getStatus() != Bookmark.UNCHECKED){
                bookmarks.add(b);
            }
        }
        return bookmarks;
    }
}
