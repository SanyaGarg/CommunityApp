package com.example.communityapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.communityapp.R;
import com.example.communityapp.wall.Wall;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.communityapp.login.MainActivity.EXTRA_TOKEN;

public class otp_screen extends AppCompatActivity {

    EditText otp;
    Button verify;
    String phone_num;
    String token;

//    public  SharedPreferences mPreferences ;
//    public static final String sharedPrefFile = "com.example.communityapp.*";

    //public static final String EXTRA_TOKEN= "com.example.communityapp.login.EXTRA_TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen);

        Intent intent = getIntent();
        phone_num = intent.getStringExtra(confirm.EXTRA_TEXT);


        otp = findViewById(R.id.phone);
        verify = findViewById(R.id.verify);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify_otp(v);
            }
        });
    }


    public void verify_otp(final View v)
    {
        String num = otp.getText().toString().trim();

//        mPreferences = getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
//        final SharedPreferences.Editor editor = mPreferences.edit();

        if(num.length()!=4)
        {
            Toast.makeText(this,"OTP must be of 4 digits",Toast.LENGTH_LONG).show();
        }

        else
        {

            JSONObject jsonObject = new JSONObject();
            String url = "https://community-ebh.herokuapp.com/users/loginotp/verify";
            //Toast.makeText(this,"yo",Toast.LENGTH_LONG).show();
            try {
                jsonObject.put("mobileNo", phone_num);
                jsonObject.put("otp", num);

            } catch (JSONException e) {
                Toast.makeText(this,"1st",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


            final OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            // put your json here
            RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
            final Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();


            //Toast.makeText(this,"success",Toast.LENGTH_LONG).show();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                    int responseCode = response.code();
                    if(responseCode==200)
                    {
                        otp_screen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                verified();
                                try {
                                    JSONObject reader = new JSONObject(response.body().toString());
                                    //editor.putString(EXTRA_TOKEN,reader.getString("token"));
                                    token = (reader.getString("token"));
                                    Intent intent = new Intent(v.getContext(), Wall.class);
                                    intent.putExtra(EXTRA_TOKEN,token);
                                    startActivity(intent);
                                    Log.i("TOKEN",token);

                                } catch (JSONException e) {

                                }

                                Intent myIntent = new Intent(v.getContext(), Wall.class);
                                //myIntent.putExtra(EXTRA_TOKEN,getGlobal());
                                startActivity(myIntent);

                            }
                        });

                    }

                    else if(responseCode==401)
                    {
                        otp_screen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wrong();
                            }
                        });
                    } else
                    {
                        otp_screen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                error();
                            }
                        });
                    }

                }

            });

        }
    }

    public void verified()
    {
        Toast.makeText(this,"OTP verified successfully",Toast.LENGTH_LONG).show();
    }

    public void wrong()
    {
        Toast.makeText(this,"OTP entered is wrong",Toast.LENGTH_LONG).show();
    }

    public void error()
    {
        Toast.makeText(this,"server error!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, confirm.class);
        startActivity(intent);
    }
    }
