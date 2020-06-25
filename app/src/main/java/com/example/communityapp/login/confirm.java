package com.example.communityapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class confirm extends AppCompatActivity {

    Button next;
    EditText phone;
    String token;
    public static final String EXTRA_TEXT= "com.example.communityapp.login.EXTRA_TEXT";
    public static final String EXTRA_TOKEN= "com.example.communityapp.login.EXTRA_TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        next = findViewById(R.id.next);
        phone = findViewById(R.id.phone);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(v);
            }
        });
    }


    public void validate(final View v)
    {
        String num = phone.getText().toString().trim();
        if(num.length()==0)
        {
            Toast.makeText(this,"Phone No. field is empty",Toast.LENGTH_LONG).show();
            return;
        }

        int flag=0;
        for(int i=0;i<num.length();i++)
        {
            if(!(num.charAt(i)<='9' && num.charAt(i)>='0'))
            {
                flag=1;
            }
        }
        if(flag==1)
        {
            Toast.makeText(this,"Enter a valid Phone No.!",Toast.LENGTH_LONG).show();
        }
      else
        {

            JSONObject jsonObject = new JSONObject();
            String url = "https://community-ebh.herokuapp.com/users/loginotp";
            //Toast.makeText(this,"yo",Toast.LENGTH_LONG).show();
            try {
                jsonObject.put("mobileNo", num);

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
                        confirm.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sent();
                                }
                            });
                    }

                    else if(responseCode==401)
                    {
                        confirm.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                invalid();
                            }
                        });
                    }

                    else
                    {
                        confirm.this.runOnUiThread(new Runnable() {
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

    public void sent()
    {
        Toast.makeText(this,"OTP sent!",Toast.LENGTH_LONG).show();
        OTP();
    }

    public void invalid()
    {
        Toast.makeText(this,"Phone no. not registered",Toast.LENGTH_LONG).show();
    }

    public void error()
    {
        Toast.makeText(this,"server error!",Toast.LENGTH_LONG).show();
    }

    public void OTP()
    {
        String num = phone.getText().toString().trim();
        Intent intent = new Intent(this, otp_screen.class);
        intent.putExtra(EXTRA_TEXT, num);

        startActivity(intent);
    }


}