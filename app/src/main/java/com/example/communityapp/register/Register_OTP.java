package com.example.communityapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.communityapp.R;
import com.example.communityapp.login.MainActivity;

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

public class Register_OTP extends AppCompatActivity {

    Button btnOK;
    EditText OTPNumber;

    String url = "http://1cdd787fb2da.ngrok.io/users/register/verify";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        OTPNumber = findViewById(R.id.otp_no);
        btnOK = findViewById(R.id.OK);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject jsonObject = new JSONObject();

                Intent intent = getIntent();
                String Name = intent.getStringExtra(Register_password.EXTRA_NAME);
                String date = intent.getStringExtra(Register_password.EXTRA_DATE);
                String MNumber = intent.getStringExtra(Register_password.EXTRA_NUMBER);
                String password = intent.getStringExtra(Register_password.EXTRA_PASSWORD);
                String number = OTPNumber.getText().toString();

                try{
                    jsonObject.put("otp",number);
                    jsonObject.put("mobileNo",MNumber);
                    jsonObject.put("name",Name);
                    jsonObject.put("dob",date);
                    jsonObject.put("password",password);
                }catch(JSONException e){
                    Toast.makeText(Register_OTP.this,"Error",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                final OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
                final Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                final Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                client.newCall(request).enqueue(new Callback(){
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        int responseCode = response.code();
                        if (responseCode == 201) {
                            Register_OTP.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    success();
                                    startActivity(myIntent);
                                }
                            });
                        } else if (responseCode == 401) {
                            Register_OTP.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    failed();
                                }
                            });
                        } else if(responseCode == 500){
                            Register_OTP.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    error();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                });


            }
        });

    }


    public void success(){
        Toast.makeText(this,"Registration successful",Toast.LENGTH_LONG).show();
    }

    public void failed(){
        Toast.makeText(this,"Wrong OTP",Toast.LENGTH_LONG).show();
    }

    public void error(){
        Toast.makeText(this,"Server Error",Toast.LENGTH_LONG).show();
    }

}