package com.example.communtiyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class Register_DOB extends AppCompatActivity {


    String date;

    public static final String EXTRA_DOB= "com.example.communtiyapp.EXTRA_DOB";
    public static final String EXTRA_NAME= "com.example.communtiyapp.EXTRA_NAME";

    private EditText etDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Button btnNext;

    public String getDate() {
        return date;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dob);

        Intent intent = getIntent();
        final String Name = intent.getStringExtra(MainActivity.EXTRA_TEXT);

        etDate = findViewById(R.id.birth_date);
        btnNext = (Button) findViewById(R.id.button);

        Calendar cal = Calendar.getInstance();
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DAY_OF_MONTH);

        etDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Register_DOB.this,
                        android.R.style.Theme_Material_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month += 1;
                date = year + "-" + month + "-" + day;
                etDate.setText(date);
            }

        };

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(date==null){
                    error();
                }else{

                    Intent myIntent = new Intent(view.getContext(), Register_MobileNo.class);
                    myIntent.putExtra(EXTRA_DOB,date);
                    myIntent.putExtra(EXTRA_NAME,Name);
                    startActivity(myIntent);
                }
            }
        });

    }

    public void error(){
        Toast.makeText(this,"Please enter your Birthday",Toast.LENGTH_LONG).show();

    }
}