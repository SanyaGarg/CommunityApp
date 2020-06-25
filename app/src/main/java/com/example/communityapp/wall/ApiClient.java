package com.example.communityapp.wall;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String URL = "https://community-ebh.herokuapp.com/";
    //public static final String token = Global.getGlobal();
    public static Retrofit retrofit;

    public static Retrofit getApiClient(){
        if(retrofit == null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}


