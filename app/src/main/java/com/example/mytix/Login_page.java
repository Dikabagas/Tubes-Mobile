package com.example.mytix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    public void btn_signup(View view) {
        startActivity(new Intent(getApplicationContext(), Register_page.class));
    }

    public void btn_login(View view) {
        startActivity(new Intent(getApplicationContext(), Drawer_home.class));
    }
}
