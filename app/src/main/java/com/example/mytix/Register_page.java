package com.example.mytix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mytix.Login_page;
import com.example.mytix.R;

public class Register_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
    }

    public void btn_back(View view) {
        startActivity(new Intent(getApplicationContext(), Login_page.class));
    }

}
