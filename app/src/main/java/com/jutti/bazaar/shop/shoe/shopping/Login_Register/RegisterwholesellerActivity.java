package com.jutti.bazaar.shop.shoe.shopping.Login_Register;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jutti.bazaar.shop.shoe.shopping.R;

public class RegisterwholesellerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerwholeseller);
        noActionbar();
    }
    public void goBack(View view){
        onBackPressed();
    }

    private void noActionbar() {
        getSupportActionBar().hide();
    }
    public void FindNOpenCBS(View view){
        Intent i;
        PackageManager manager = getPackageManager();
        try {
            i = manager.getLaunchIntentForPackage("com.kitchen.utensils.shopping.cbs");
            if (i == null)
                throw new PackageManager.NameNotFoundException();
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {

//if not found in device then will come here
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.kitchen.utensils.shopping.cbs"));
            startActivity(intent);
        }
    }
}
