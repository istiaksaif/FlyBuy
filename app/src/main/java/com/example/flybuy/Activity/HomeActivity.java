package com.example.flybuy.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.flybuy.Adaptar.SliderAdapterimage;
import com.example.flybuy.Fragment.CartFragment;
import com.example.flybuy.Fragment.HomeFragment;
import com.example.flybuy.Fragment.ProfileFragment;
import com.example.flybuy.Fragment.SearchFragment;
import com.example.flybuy.Model.SliderImageModel;
import com.example.flybuy.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class HomeActivity extends AppCompatActivity {

    private long backPressedTime;
    private Fragment selectedFragment = null;
    private BottomNavigationView bottomNavigationView;
    private ViewPager sliderViewPager;
    private LinearLayout sliderDots;
    private int dotsCount;
    private ImageView[] dots;
    private ImageView imageView;
    private int currentPosition=0;
    private SliderAdapterimage sliderAdapterimage;
    private List<SliderImageModel> sliderList = new ArrayList<>();

    private Timer timer;
    private String receiveemail;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        receiveemail = getIntent().getStringExtra("email");

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.ichome);
        bottomNavigationView.setItemIconTintList(null);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.ichome:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.icseach:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.iccart:
                    selectedFragment = new CartFragment();
                    break;
                case R.id.icprofile:
                    selectedFragment = new ProfileFragment();
                    break;
            }
            if(selectedFragment!=null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                return true;
            }
            return false;
        }
    };



//    private void MyTimerTask(){
//
//        final Handler handler = new Handler();
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                if(currentPosition==sliderList.size())
//                    currentPosition=0;
//                sliderViewPager.setCurrentItem(currentPosition++,true);
//
//            }
//        };
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(runnable);
//            }
//        },2000,4000);
//    }

    public void onBackPressed(){
        if(backPressedTime + 2000>System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else{
            Toast.makeText(getBaseContext(),"Press Back Again to Exit",Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}