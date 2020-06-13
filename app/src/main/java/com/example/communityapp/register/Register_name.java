package com.example.communityapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.communityapp.R;

public class Register_name extends AppCompatActivity {

    String name;
    public static final String EXTRA_TEXT= "com.example.communityapp.EXTRA_TEXT";


    public String getName(){
        return name;
    }

    public void setName(String s){
        name = s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnNext = (Button) findViewById(R.id.button);
        final EditText etName = findViewById(R.id.et_name);
        final EditText lastName = findViewById(R.id.last_name);

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setName(etName.getText().toString()+lastName.getText().toString());

                if(name==null){
                    error();
                }else{
                    Intent myIntent = new Intent(view.getContext(), Register_DOB.class);
                    myIntent.putExtra(EXTRA_TEXT,name);
                    startActivity(myIntent);
                }
            }
        });
    }

    public void error(){
        Toast.makeText(this,"Please enter your name.",Toast.LENGTH_LONG).show();

    }
}