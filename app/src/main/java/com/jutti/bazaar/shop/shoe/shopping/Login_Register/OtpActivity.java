package com.jutti.bazaar.shop.shoe.shopping.Login_Register;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jutti.bazaar.shop.shoe.shopping.Activities.Home;
import com.jutti.bazaar.shop.shoe.shopping.R;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class OtpActivity extends AppCompatActivity {
    LinearLayout ll_success,ll_mobileotp;
    TextView tv_sec;
    OtpTextView otp_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ll_success=findViewById(R.id.ll_success);
        ll_mobileotp=findViewById(R.id.ll_mobileotp);
        tv_sec=findViewById(R.id.tv_sec);
        otp_view=findViewById(R.id.otp_view);
        otp_view.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                //   Toast.makeText(MainActivity.this, "gege", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOTPComplete(String otp) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ll_success.setVisibility(View.VISIBLE);
                        ll_mobileotp.setVisibility(View.GONE);
                    }
                }, 800);

            }
        });
        noActionbar();

        countdown();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                otp_view.setOTP("1234");
            }
        }, 1500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        }, 3900);


    }

    private void countdown() {
        new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                tv_sec.setText(millisUntilFinished / 1000+"s");
            }

            public void onFinish() {
                tv_sec.setText("");
            }
        }.start();
    }

    private void noActionbar() {
        getSupportActionBar().hide();
    }
}
