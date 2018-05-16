package com.chan.samples.news.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by chan on 1/10/18.
 */

public class CustomGridView extends GridView {

    public CustomGridView(Context context) {
        super(context);
    }

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec,expandSpec);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();

//        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
//
//        if(getAdapter() == null){
//            return;
//        }
//
//        int count = getAdapter().getCount();
//
//        for(int i=0; i<count; i++){
//            View view = getAdapter().getView(0,null,this);
//            if(view != null){
//                int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,MeasureSpec.AT_MOST);
//                view.measure(0,expandSpec);
//                Log.i("TAG",view.getMeasuredHeight()+" measure height");
//            }
//        }

    }

}
