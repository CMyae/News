package com.chan.samples.news.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


/**
 * Created by chan on 1/27/18.
 */

public class KeyboardUtils {

    public static void showInputMethod(Context context,View v){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            //imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }
    }


    public static void hideInputMethod(Context context,View v){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
