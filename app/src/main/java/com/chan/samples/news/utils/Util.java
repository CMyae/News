package com.chan.samples.news.utils;

import android.util.Log;

import com.chan.samples.news.data.models.Source;
import com.chan.samples.news.data.models.SourceResponse;

import java.util.List;

/**
 * Created by chan on 2/3/18.
 */

public class Util {

    public static int calculatePageCount(int totalResult){
        int pageSize = 10;
        int totalRes = totalResult;
        if(totalRes == 0){
            return 0;
        }
        int result = totalRes/pageSize;
        int remainder = totalRes%pageSize;

        if(result == 0){
            return 1;
        }else{
            if(remainder != 0){

                return result+1;

            }else{
                return result;
            }
        }
    }


    public static String getSourcesName(SourceResponse response){
        if(response == null) return "";

        List<Source> sources = response.getSources();
        if(sources == null) return "";

        String name = "";
        for(int i=0; i<sources.size(); i++){
            if(i == 20){
                break;
            }

            if(i < 20){
                Source source = sources.get(i);
                if(source != null){
                    name += source.getId() + ",";
                }
            }

        }
        return name;
    }



    public static String getCapitalizeName(String name){
        if(name.length() == 1){
            return name.toUpperCase();
        }
        String[] words = name.split("-");
        String result = "";
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if(i == words.length - 1) {
                result += word.substring(0, 1).toUpperCase() + word.substring(1);
            }else{
                result += word.substring(0, 1).toUpperCase() + word.substring(1) + " ";
            }
        }
        return result;
    }

}
