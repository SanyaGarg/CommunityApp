package com.example.communityapp.wall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.communityapp.Globals.Global;
import com.example.communityapp.R;
import com.example.communityapp.community.CommunityFragment;
import com.example.communityapp.login.MainActivity;
import com.example.communityapp.meetings.MeetingsFragment;
import com.example.communityapp.profile.ProfileFragment;
import com.example.communityapp.register.Register_password;
import com.example.communityapp.share.Share;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Wall extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private BottomNavigationView setMainNav;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //recyclerView.setLayoutManager( new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //   recyclerView.setItemAnimator(new DefaultItemAnimator());
        //  recyclerView.setNestedScrollingEnabled(false);

        Loadjson();


        setMainNav = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        Log.d(TAG, "onCreate: starting.");

        setMainNav.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {

                    case R.id.share:
                        startActivity(new Intent(Wall.this,
                                Share.class));
                        overridePendingTransition(0,0);
                        return ;
                    case R.id.community:
                        selectedFragment = new CommunityFragment();

                        break;
                    case R.id.meetings:
                        selectedFragment = new MeetingsFragment();
                        break ;
                    case R.id.profile:
                        selectedFragment = new ProfileFragment();
                        break ;
                }

                assert selectedFragment != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            }

    });
}

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");

    }

    public void Loadjson() {

        Wall_API apiInterface = ApiClient.getApiClient().create(Wall_API.class);

        Intent intent = getIntent();
        String token = intent.getStringExtra(MainActivity.EXTRA_TOKEN);
      //  Log.e("tag1",token);
        //final String token = ;
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization",token);


        Call<List<Post>> call;
        call = apiInterface.getPosts(headers);

        call.enqueue(new Callback<List<Post>>() {

            @Override
            public void onResponse(@NotNull Call<List<Post>> call, @NotNull Response<List<Post>> response) {
                int responseCode = response.code();

                //if(response.isSuccessful()){
                //  Toast.makeText(MainActivity.this, "Success"+response.body(), Toast.LENGTH_LONG).show();
                if (responseCode == 200) {
                    Log.i("TAG","helloooo");
                    List<Post> posts = response.body();

                    recyclerView.setAdapter(new Adapter(posts,Wall.this));
                    // adapter = new Adapter(posts, MainActivity.this);
                    //recyclerView.setAdapter(adapter);
                    // adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure(@NotNull Call<List<Post>> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
