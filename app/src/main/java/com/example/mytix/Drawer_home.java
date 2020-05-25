package com.example.mytix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

public class Drawer_home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    SliderLayout sliderLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_home);

        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderLayout.setScrollTimeInSec(1); //mengatur delay 1 detik

        setSliderViews();

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setSliderViews(){
        for(int i = 0; i<=3; i++){
            DefaultSliderView sliderView = new DefaultSliderView(this);

            switch (i){
                case 0:
                    //sliderView.setImageUrl("https://www.dakwatuna.com/wp-content/uploads/2018/04/bioskop-cnn-indonesia.jpg");
                    sliderView.setImageDrawable(R.drawable.bioskop);
                    break;
                case 1:
                    //sliderView.setImageUrl("https://www.infopenerbangan.com/wp-content/uploads/2018/06/penerbangan-780x400.jpg");
                    sliderView.setImageDrawable(R.drawable.penerbangan);
                    break;
                case 2:
                    //sliderView.setImageUrl("https://asset.kompas.com/crops/Sopi2ulRUWGp-jhrZDHqkLPvIDw=/156x0:1000x563/750x500/data/photo/2019/11/03/5dbe423d433c4.jpg");
                    sliderView.setImageDrawable(R.drawable.kereta);
                    break;
                case 3:
                    //sliderView.setImageUrl("https://www.dbs.com/iwov-resources/images/newsroom/indonesia/Blog/tempat%20wisata%20indonesia/Image%20Banner%20-%20Wisata%20Indonesia.jpg");
                    sliderView.setImageDrawable(R.drawable.liburan);
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderView.setDescription("Layanan MyTIX");
            final int finall = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener(){
                @Override
                public void onSliderClick (SliderView sliderView){
                    Toast.makeText(Drawer_home.this, "This is Slider " + (finall + 1), Toast.LENGTH_SHORT).show();
                }
            });

            sliderLayout.addSliderView(sliderView);
        }
        setupToolbar();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.profile:
                Toast.makeText(Drawer_home.this, "Profile Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(Drawer_home.this, "Setting Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.history:
                Toast.makeText(Drawer_home.this, "History Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.contact:
                Toast.makeText(Drawer_home.this, "Contact Us Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rate:
                Toast.makeText(Drawer_home.this, "Rate Us Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                Toast.makeText(Drawer_home.this, "About Us Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                Toast.makeText(Drawer_home.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    public void btn_pswt(View view) {
        startActivity(new Intent(getApplicationContext(), Pesawat_page.class));
    }

    public void btn_krt(View view) {
        startActivity(new Intent(getApplicationContext(), Kereta_page.class));
    }

    public void btn_bus(View view) {
    }

    public void btn_taxi(View view) {
    }

    public void btn_movie(View view) {
    }

    public void btn_holiday(View view) {
    }
}
