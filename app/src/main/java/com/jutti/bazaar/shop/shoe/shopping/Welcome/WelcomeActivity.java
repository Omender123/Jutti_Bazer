package com.jutti.bazaar.shop.shoe.shopping.Welcome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.jutti.bazaar.shop.shoe.shopping.Login_Register.LoginActivity;
import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.Typewriter;

public class WelcomeActivity extends AppCompatActivity {

    Typewriter tv_welcome1, tv_welcome2, tv_welcome3, tv_welcome4;
    TextView tv_getstarted;
    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WelcomeActivity.this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        nostatusbar();
        inits();
        init_handlers();
        onClicks();

    }


    @Override
    protected void onResume() {
        setup_video();

        super.onResume();
    }

    private void setup_video() {
        videoBG = (VideoView) findViewById(R.id.videoView);

        // Build your video Uri
        Uri uri = Uri.parse("android.resource://" // First start with this,
                + getPackageName() // then retrieve your package name,
                + "/" // add a slash,
                + R.raw.bg_video); // and then finally add your video resource. Make sure it is stored
        // in the raw folder.
        // Set the new Uri to our VideoView
        videoBG.setVideoURI(uri);
        // Start the VideoView
        videoBG.start();
        // Set an OnPreparedListener for our VideoView. For more information about VideoViews,
        // check out the Android Docs: https://developer.android.com/reference/android/widget/VideoView.html
        videoBG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer = mediaPlayer;
                // We want our video to play over and over so we set looping to true.
                mMediaPlayer.setLooping(true);
                // We then seek to the current posistion if it has been set and play the video.
                if (mCurrentVideoPosition != 0) {
                    mMediaPlayer.seekTo(mCurrentVideoPosition);
                    mMediaPlayer.start();
                }
            }
        });
    }
    private void nostatusbar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void onClicks() {
        tv_getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("tut_played", "true");

                editor.commit();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    private void init_handlers() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_welcome1.setVisibility(View.VISIBLE);
                tv_welcome1.setCharacterDelay(50);
                tv_welcome1.animateText("Premium Quality Assured");
            }
        }, 500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_welcome2.setVisibility(View.VISIBLE);
                tv_welcome2.setCharacterDelay(50);
                tv_welcome2.animateText("Hassle Free Delivery");
            }
        }, 1500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_welcome3.setVisibility(View.VISIBLE);
                tv_welcome3.setCharacterDelay(50);
                tv_welcome3.animateText("100% Trusted Products");
            }
        }, 2500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_getstarted.setVisibility(View.VISIBLE);
            }
        }, 4500);
    }

    private void inits() {
        tv_welcome1 = findViewById(R.id.tv_welcome1);
        tv_welcome2 = findViewById(R.id.tv_welcome2);
        tv_welcome3 = findViewById(R.id.tv_welcome3);
        tv_welcome4 = findViewById(R.id.tv_welcome4);
        tv_getstarted = findViewById(R.id.tv_getstarted);

    }


}
