package com.chan.samples.news.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.chan.samples.news.utils.Constants;
import com.chan.samples.news.utils.ViewUtils;

/**
 * Created by chan on 1/10/18.
 */

public class SquareImageView extends ImageView {

    public SquareImageView(Context context) {
        super(context);
        init();
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        setClipToOutline(true);
        setBackground(ViewUtils.getRoundRectDrawable(Constants.SMALL_CARD_RADIUS));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width,width);
    }
}
