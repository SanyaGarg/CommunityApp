package com.example.communityapp.share;

import android.net.Uri;

public class UriHelper {
    private Uri uri = null;
    public static UriHelper instance = new UriHelper();

    public UriHelper() {

    }

    public static UriHelper getInstance() {
        return instance;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
