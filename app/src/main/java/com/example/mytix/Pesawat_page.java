package com.example.mytix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Pesawat_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesawat_page);
    }

    public void btn_garuda(View view) {
        startActivity(new Intent(getApplicationContext(), Pembayaran.class));
    }
}
