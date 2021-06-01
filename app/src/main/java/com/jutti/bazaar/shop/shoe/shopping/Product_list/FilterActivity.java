package com.jutti.bazaar.shop.shoe.shopping.Product_list;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jutti.bazaar.shop.shoe.shopping.R;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_filter);
        noActionbar();



    }

    public void goBack(View view){
        onBackPressed();
    }
    private void noActionbar() {
        getSupportActionBar().hide();
    }
}
