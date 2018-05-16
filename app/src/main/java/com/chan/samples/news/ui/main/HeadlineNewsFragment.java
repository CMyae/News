package com.chan.samples.news.ui.main;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chan.samples.news.R;
import com.chan.samples.news.data.models.Article;
import com.chan.samples.news.utils.Constants;
import com.chan.samples.news.utils.DateUtils;
import com.chan.samples.news.utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HeadlineNewsFragment extends Fragment {

    private static final String CURRENT_NEWS = "CURRENT_NEWS";


    @BindView(R.id.imgNewHeader) ImageView imgNewsHeader;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.tv_news_headline_title) TextView tvHeadlineNewsTitle;
    @BindView(R.id.tv_date) TextView tvDate;
    @BindView(R.id.tvChannel) TextView tvChannel;

    private Article currentArticle;


    public static HeadlineNewsFragment getInstance(Article latestNew){

        HeadlineNewsFragment fragment = new HeadlineNewsFragment();
        Bundle bdn = new Bundle();
        bdn.putParcelable(CURRENT_NEWS,latestNew);
        fragment.setArguments(bdn);
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bdn = getArguments();
        if(bdn != null){

            currentArticle = bdn.getParcelable(CURRENT_NEWS);
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_headline_news, container, false);
        ButterKnife.bind(this,view);

        imgNewsHeader.setClipToOutline(true);
        imgNewsHeader.setBackground(ViewUtils.getRoundRectDrawable(Constants.SMALL_CARD_RADIUS));

        if(currentArticle != null) {

            String title = currentArticle.getTitle();
            String timeStamp = currentArticle.getTimeStamp();
            String sourceName = "";

            if(currentArticle.getSource() != null){
                sourceName = currentArticle.getSource().getName();
            }


            if(title != null) tvHeadlineNewsTitle.setText(title);
            if(timeStamp != null){
                timeStamp = DateUtils.format(timeStamp);
                tvDate.setText(timeStamp);
            }
            if(sourceName != null) tvChannel.setText(sourceName);


            Glide.with(getContext())
                    .load(currentArticle.getUrlToImage())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.INVISIBLE);
                            //set holder image icon
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.INVISIBLE);
                            return false;
                        }
                    })
                    .into(imgNewsHeader);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)getActivity()).onArticleItemClick(currentArticle);
                }
            });
        }

        return view;


    }

}
