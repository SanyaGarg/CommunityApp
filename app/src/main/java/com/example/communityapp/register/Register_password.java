package com.example.communityapp.register;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.communityapp.R;

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

    public static final String EXTRA_NUMBER= "com.example.communityapp.EXTRA_NUMBER";
    public static final String EXTRA_PASSWORD= "com.example.communityapp.EXTRA_PASSWORD";
    public static final String EXTRA_DATE= "com.example.communityapp.EXTRA_DATE";
    public static final String EXTRA_NAME= "com.example.communityapp.EXTRA_NAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        btnRegister = (Button) findViewById(R.id.button);
        etPassword = findViewById(R.id.confirm_password);
        newPassword = findViewById(R.id.password_text);
        tv = findViewById(R.id.tv);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = newPassword.getText().toString();
                String pass = etPassword.getText().toString();
                if (password.equals(pass)) {
                    Intent myIntent = new Intent(view.getContext(), Register_OTP.class);
                    Intent intent = getIntent();
                    String Name = intent.getStringExtra(Register_MobileNo.EXTRA_FNAME);
                    String date = intent.getStringExtra(Register_MobileNo.EXTRA_DATE);
                    String number = intent.getStringExtra(Register_MobileNo.EXTRA_Mobile);
                    check(myIntent,pass,number,Name,date);
                } else {
                    reEnterPassword();
                }
            }
        });
    }

    public void check(final Intent myIntent, final String pass,final String number,final String Name,final String date){

        //String secret = etPassword.getText().toString();
        JSONObject jsonObject = new JSONObject();
        String url = "http://1cdd787fb2da.ngrok.io/users/register";

        try{
            jsonObject.put("mobileNo",number);
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
                if (responseCode == 200) {
                    Register_password.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            success();

                            myIntent.putExtra(EXTRA_NUMBER,number);
                            myIntent.putExtra(EXTRA_DATE,date);
                            myIntent.putExtra(EXTRA_NAME,Name);
                            myIntent.putExtra(EXTRA_PASSWORD,pass);
                            startActivity(myIntent);

                        }
                    });
                } else if (responseCode == 409) {
                    Register_password.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alreadyRegistered();
                        }
                    });
                } else if(responseCode == 500){
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
        Toast.makeText(this,"OTP Sent",Toast.LENGTH_LONG).show();
    }

    public void alreadyRegistered(){
        Toast.makeText(this,"Already Registered",Toast.LENGTH_LONG).show();
    }


    public void error(){
        Toast.makeText(this,"Server Error",Toast.LENGTH_LONG).show();
    }

    public void reEnterPassword(){
        Toast.makeText(this,"Incorrect Password.Re-Enter Password",Toast.LENGTH_LONG).show();
    }
}