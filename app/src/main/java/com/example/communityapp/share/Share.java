package com.example.communityapp.share;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.communityapp.Globals.Global;
import com.example.communityapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Share extends AppCompatActivity implements ListAdapterWithRecyclerView.image_listener, ListAdapterWithRecyclerView.video_listener{

    Button pick;
    Button post;
    RecyclerView recyclerView;
    EditText post_text;

    static String TAG = "yoooooooooooo";

    ListAdapterWithRecyclerView listAdapterWithRecycleView;


    List<Uri> uris = new ArrayList<>();
    List<String> filePaths = new ArrayList<>();

    public static final String EXTRA_TOKEN= "com.example.communityapp.login.EXTRA_TOKEN";
    public static final String SHARED_PREF = "com.example.communityapp.*";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        pick = findViewById(R.id.pick);
        recyclerView = findViewById(R.id.recycler_view);
        post_text = findViewById(R.id.post_text);
        post = findViewById(R.id.post);

        listAdapterWithRecycleView = new ListAdapterWithRecyclerView(uris,this, (ListAdapterWithRecyclerView.video_listener) this,this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listAdapterWithRecycleView);


        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(Share.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(Share.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/*", "video/*"});
                startActivityForResult(intent, 1);

            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_media();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {

            ClipData clipData = data.getClipData();
            // Log.e(TAG,"aa gaya");

            if (clipData != null)
            {

                //Toast.makeText(this,"set_images",Toast.LENGTH_LONG).show();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri dataUri = clipData.getItemAt(i).getUri();

                    uris.add(dataUri);

                }

                listAdapterWithRecycleView.notifyDataSetChanged();
                recyclerView.scrollToPosition(uris.size()-1);

                Toast.makeText(this,""+recyclerView.getAdapter().getItemCount(),Toast.LENGTH_LONG).show();

            }

            else
            {
                Toast.makeText(this,"Select at least one image or video!",Toast.LENGTH_LONG).show();
            }

            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }





    @Override
    public void image_click(int position) {
        UriHelper.getInstance().setUri(uris.get(position));
        Intent intent = new Intent(this,user_image_view.class);
        startActivity(intent);
    }

    @Override
    public void video_click(int position) {
        //Toast.makeText(this,"set_images",Toast.LENGTH_LONG).show();
        UriHelper.getInstance().setUri(uris.get(position));
        Intent intent = new Intent(this,user_video_view.class);
        startActivity(intent);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        Log.e("taggg2",result);
        return result;
    }


    public void upload_media()
    {
        /*if(uris.size()==0)
        {
            Toast.makeText(this," Add an images of videos first",Toast.LENGTH_LONG).show();
            return;
        }*/

        List<MultipartBody.Part> img_list = new ArrayList<>();

        for(int i=0;i<uris.size();i++)
        {
            File file = new File(getRealPathFromURI(uris.get(i)));
            //Log.e("xyz",uris.get(0).toString());
            RequestBody requestFile = RequestBody.create(file,MediaType.parse("multipart/form-data"));

            MultipartBody.Part body = MultipartBody.Part.createFormData("postMedia", file.getName(), requestFile);
            img_list.add(body);

        }


        RequestBody descBody = RequestBody.create(post_text.getText().toString(),MediaType.parse("text/plain"));


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String token = sharedPreferences.getString(EXTRA_TOKEN,"");

        Map<String,String> headers = new HashMap<>();
        headers.put("authorization", token);


        Call<MyResponse> call = api.uploadImage(img_list,descBody,headers);

        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                media_added();
                Log.e(TAG,response.toString());
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                media_failed();
                Log.e(TAG,t.toString());
            }
        });

    }

    public void media_added()
    {
        Toast.makeText(this,"post uploaded!",Toast.LENGTH_LONG).show();
    }

    public void media_failed()
    {
        Toast.makeText(this," Failed to add post!",Toast.LENGTH_LONG).show();
    }

}