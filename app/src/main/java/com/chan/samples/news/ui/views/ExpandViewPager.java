package com.chan.samples.news.ui.views;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.chan.samples.news.utils.ViewUtils;

/**
 * Created by chan on 1/14/18.
 */

public class ExpandViewPager extends ViewPager{

    private static final int duration = 4000;

    private Handler handler;
    private Runnable runnable;
    private int currentPage;
    private int MAX_COUNT;

    private int scrollState;


    public ExpandViewPager(Context context) {
        super(context);
        init();
    }

    public ExpandViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){

        handler = new Handler(Looper.getMainLooper());

        runnable = new Runnable() {
            @Override
            public void run() {
                if(scrollState == SCROLL_STATE_IDLE){
                    setCurrentItem(currentPage,true);
                }
            }
        };

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                if(currentPage >= MAX_COUNT){
                    currentPage = 0;
                }else{
                    currentPage++;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                scrollState = state;
                if(scrollState == SCROLL_STATE_SETTLING){
                    //remove previous callback
                    handler.removeCallbacks(runnable);

                    handler.postDelayed(runnable,duration);
                }
            }
        });
    }


    public void playCarouselAnimation(){
        MAX_COUNT = getAdapter().getCount()-1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setCurrentItem(++currentPage,true);
            }
        },duration);
        //if there is previous handler postDelay working,
        //remove it and restart again

//        handler.removeCallbacks(runnable);
//
//        if(runnable == null){
//
//            MAX_COUNT = getAdapter().getCount();
//
//            runnable = new Runnable() {
//                @Override
//                public void run() {
//
//                    //if page is currently dragging,remove callback to avoid animating and restart again
//                    if(scrollState == SCROLL_STATE_DRAGGING){
//                        handler.removeCallbacks(runnable);
//                        handler.postDelayed(this,duration);
//                        return;
//                    }
//
//                    //no items to animate
//                    if(MAX_COUNT == 0) return;
//
//                    if(currentPage >= MAX_COUNT){
//                        currentPage = 0;
//                    }else{
//                        currentPage++;
//                    }
//                    setCurrentItem(currentPage,true);
//                    handler.postDelayed(this,duration);
//                }
//            };
//        }
//
//        this.post(new Runnable() {
//            @Override
//            public void run() {
//                handler.postDelayed(runnable,duration);
//            }
//        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);

        if(mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            int height = 0;

            for(int i=0;i<getChildCount(); i++){
                View child = getChildAt(i);
                child.measure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));

                int h = child.getMeasuredHeight();
                if(h > height) height = h;
            }

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

    }
}
