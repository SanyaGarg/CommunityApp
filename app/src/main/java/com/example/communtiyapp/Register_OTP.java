package com.example.communtiyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Register_OTP extends AppCompatActivity {

    Button btnOK;
    EditText number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        number = findViewById(R.id.otp_no);
        btnOK = findViewById(R.id.OK);


    }
}