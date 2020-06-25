package com.example.communityapp.wall;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;

public interface Wall_API {

    public static final String BASE_URL = "https://community-ebh.herokuapp.com/";

    @GET("posts")
    Call<List<Post>> getPosts(
            @HeaderMap Map<String, String> headers
    );

//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();
}
