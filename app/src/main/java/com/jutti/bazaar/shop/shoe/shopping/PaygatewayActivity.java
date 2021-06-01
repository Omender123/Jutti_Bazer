package com.jutti.bazaar.shop.shoe.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jutti.bazaar.shop.shoe.shopping.Activities.OrdersList;

public class PaygatewayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paygateway);
        getSupportActionBar().hide();

        Toast.makeText(this, "Payment Gateway here", Toast.LENGTH_SHORT).show();

        init_handler();
        nostatusbar();
      //  noActionbar();
    }

    private void nostatusbar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void init_handler() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Your Order has been Placed Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), OrdersList.class));
                        finish();
                    }
                }, 2000);

    }

}
