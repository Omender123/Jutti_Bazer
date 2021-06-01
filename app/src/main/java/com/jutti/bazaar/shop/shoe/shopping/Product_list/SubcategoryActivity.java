package com.jutti.bazaar.shop.shoe.shopping.Product_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jutti.bazaar.shop.shoe.shopping.R;

public class SubcategoryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);
        noActionbar();
    }

    public void open_brands(View view) {
        startActivity(new Intent(getApplicationContext(), Product_list.class));
    }


    public void goBack(View view){
        onBackPressed();
    }
    private void noActionbar() {
        getSupportActionBar().hide();
    }


}
