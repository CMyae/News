package com.chan.samples.news.di.modules;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;

import com.chan.samples.news.data.DataManager;
import com.chan.samples.news.data.local.NewsDatabase;
import com.chan.samples.news.data.local.PrefHelper;
import com.chan.samples.news.data.remote.APIEndpoint;
import com.chan.samples.news.data.remote.RemoteController;
import com.chan.samples.news.di.ApplicationContext;
import com.chan.samples.news.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chan on 1/17/18.
 */

@Module
public class ApplicationModule {

    private Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    @ApplicationContext
    public Context provideAppContext(){
        return context;
    }

    @Singleton
    @Provides
    public OkHttpClient provideClient(){

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header(Constants.HEADER_KEY,Constants.API_KEY);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };

        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build();
    }



    @Singleton
    @Provides
    public APIEndpoint provideApiEndpoint(OkHttpClient client){

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        return new Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(APIEndpoint.class);

    }


    @Named(Constants.PREF_BOOKMARK)
    @Singleton
    @Provides
    public SharedPreferences providePrefBookmark(@ApplicationContext Context context){
        return context.getSharedPreferences(Constants.PREF_BOOKMARK,Context.MODE_PRIVATE);
    }


    @Named(Constants.PREF_DEF)
    @Singleton
    @Provides
    public SharedPreferences providePrefDefault(@ApplicationContext Context context){
        return context.getSharedPreferences(Constants.PREF_DEF,Context.MODE_PRIVATE);
    }


    @Named(Constants.PREF_CONFIG)
    @Singleton
    @Provides
    public SharedPreferences providePrefConfig(@ApplicationContext Context context){
        return context.getSharedPreferences(Constants.PREF_CONFIG,Context.MODE_PRIVATE);
    }


    @Singleton
    @Provides
    public Gson provideGson(){
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }


    @Singleton
    @Provides
    public RemoteController provideRemoteController(APIEndpoint apiEndpoint){
        return new RemoteController(apiEndpoint);
    }


    @Singleton
    @Provides
    public DataManager provideDataManager(PrefHelper prefHelper,RemoteController controller, NewsDatabase db){
        return new DataManager(prefHelper,controller,db);
    }


    @Singleton
    @Provides
    public NewsDatabase provideDb(@ApplicationContext Context context){
        return Room.databaseBuilder(context,
                NewsDatabase.class, Constants.DB_NAME).build();
    }

}
