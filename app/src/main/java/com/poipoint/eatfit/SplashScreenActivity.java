package com.poipoint.eatfit;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * Created by Tanmay on 2/6/2016.
 */

//this class just displays and animates the splash screen
public class SplashScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        ImageView splashImage=(ImageView)findViewById(R.id.splash_image);
        ScaleAnimation scaleAnimation=new ScaleAnimation(1,1.1f,1,1.1f);
        scaleAnimation.setDuration(3000);
        scaleAnimation.setFillEnabled(true);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent i=new Intent(getApplicationContext(),CategoreyActivity.class);
                startActivity(i);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splashImage.startAnimation(scaleAnimation);

    }
}
