package com.example.communityapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.communityapp.R;

public class Register_MobileNo extends AppCompatActivity {

    String mobile_no;

    public static final String EXTRA_Mobile= "com.example.communityapp.register.EXTRA_Mobile";
    public static final String EXTRA_DATE= "com.example.communityapp.register.EXTRA_DATE";
    public static final String EXTRA_FNAME= "com.example.communityapp.register.EXTRA_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);

        Intent intent = getIntent();
        final String Name = intent.getStringExtra(Register_DOB.EXTRA_NAME);
        final String dob = intent.getStringExtra(Register_DOB.EXTRA_DOB);

        Button btnNext = (Button) findViewById(R.id.button);
        final EditText no = findViewById(R.id.mobile_text);

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mobile_no = no.getText().toString();

                Intent myIntent = new Intent(view.getContext(), Register_password.class);
                myIntent.putExtra(EXTRA_Mobile,mobile_no);
                myIntent.putExtra(EXTRA_DATE,dob);
                myIntent.putExtra(EXTRA_FNAME,Name);
                startActivity(myIntent);

            }
        });
    }

    public void error(){
        Toast.makeText(this,"Please enter your Mobile Number.",Toast.LENGTH_LONG).show();
    }
}