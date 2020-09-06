package com.firebase.barqrcodegenerator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private final int SPALSH_DELAY=3000;

    private ImageView imageView;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setBackgroundDrawable(null);

        initializeView();
        animateLogo();
        goToMain();
    }



    private void initializeView() {
        imageView=findViewById(R.id.imageView);
        text=findViewById(R.id.text);
    }

    private void animateLogo() {
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.fade_in_without_duration);
        animation.setDuration(SPALSH_DELAY);

        imageView.startAnimation(animation);
        text.startAnimation(animation);
    }

    private void goToMain() {

        new Handler().postDelayed(()->{
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        },SPALSH_DELAY);
    }

}