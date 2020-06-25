package com.example.communityapp.share;

import android.content.SharedPreferences;

import java.util.List;
import java.util.Map;

import com.example.communityapp.Globals.Global;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

    String BASE_URL = "https://community-ebh.herokuapp.com/";

    @Multipart
    //@Headers("authorization: " + token)
    @POST("posts")
    Call<MyResponse> uploadImage(@Part List<MultipartBody.Part> file, @Part("text") RequestBody post_text,@HeaderMap Map<String,String> header);

}
