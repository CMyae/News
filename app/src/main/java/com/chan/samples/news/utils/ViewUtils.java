package com.chan.samples.news.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

/**
 * Created by chan on 1/10/18.
 */

public class ViewUtils {

    public static int dpToPx(Context context,int dp){
        return (int)(dp * context.getResources().getDisplayMetrics().density);
    }

    public static int pxToDp(Context context,int px){
        return (int)(px / context.getResources().getDisplayMetrics().density);
    }

    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }



    public static Drawable getRoundRectDrawable(int radius){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radius);
        drawable.setColor(Color.parseColor("#eeeeee"));
        return drawable;
    }

}