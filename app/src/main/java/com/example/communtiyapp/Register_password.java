package com.example.communtiyapp;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class Register_password extends AppCompatActivity {

    EditText etPassword;
    EditText newPassword;
    Button btnRegister;
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        btnRegister = (Button) findViewById(R.id.button);
        etPassword = findViewById(R.id.confirm_password);
        newPassword = findViewById(R.id.password_text);
        tv = findViewById(R.id.tv);

        final String password = newPassword.getText().toString();
        final String secret = etPassword.getText().toString();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //final String LOG_TAG = Register_password.class.getSimpleName();

                if (password.equals(secret)) {
                    check();
                } else {
                    reEnterPassword();
                }
            }
        });
    }

    public void check(){

        Intent intent = getIntent();
        final String Name = intent.getStringExtra(Register_MobileNo.EXTRA_FNAME);
        final String date = intent.getStringExtra(Register_MobileNo.EXTRA_DATE);
        final String number = intent.getStringExtra(Register_MobileNo.EXTRA_Mobile);


        String secret = etPassword.getText().toString();
        JSONObject jsonObject = new JSONObject();
        String url = "http://0a5f9a327ab4.ngrok.io/users/reg";

        try{
            jsonObject.put("name",Name);
            jsonObject.put("mobileNo",number);
            jsonObject.put("dob",date);
            jsonObject.put("password",secret);
        }catch(JSONException e){
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        final OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(jsonObject.toString(),JSON);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int responseCode = response.code();
                if (responseCode == 201) {
                    Register_password.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            success();
                        }
                    });
                } else if (responseCode == 409) {
                    Register_password.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            failed();
                        }
                    });
                } else{
                    Register_password.this.runOnUiThread(new Runnable() {
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

    public void success(){
        Toast.makeText(this,"Registration successful",Toast.LENGTH_LONG).show();
    }

    public void failed(){
        Toast.makeText(this,"Registration Failed",Toast.LENGTH_LONG).show();
    }

    public void error(){
        Toast.makeText(this,"Server Error",Toast.LENGTH_LONG).show();
    }

    public void reEnterPassword(){
        Toast.makeText(this,"Incorrect Password.Re-Enter Password",Toast.LENGTH_LONG).show();
    }
}