package com.jutti.bazaar.shop.shoe.shopping.Product_details;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;


import com.jutti.bazaar.shop.shoe.shopping.R;
import com.jutti.bazaar.shop.shoe.shopping.databinding.ZoomSliderActivityBinding;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;


public class ZoomSliderImageActivivty extends AppCompatActivity {
    ZoomSliderActivityBinding binding;
    Activity context;
    //    private ArrayList<ImageData> images;
    ArrayList<String> imageListType = Product_Details.al_banners;
    int currentPage = 0;
    private static int NUM_Page = 0;
    Timer timer;
    final long DELAY_MS = 8000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 8000; //
    private Handler mHandler = new Handler();
    private Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {
            if (currentPage == NUM_Page) {
                currentPage = 0;
            }
            binding.viewPager.setCurrentItem(currentPage++, true);
            mHandler.postDelayed(mHandlerTask, 5000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.zoom_slider_activity);
        context = ZoomSliderImageActivivty.this;
//                timerOnViewPager();

        Product_Details.al_banners = this.getIntent().getStringArrayListExtra("tutorialData");
        Log.e("TAG", "onCreate: " + Product_Details.al_banners);
        binding.ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        timerOnViewPager();
    }


    private void timerOnViewPager() {

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            private static final int NUM_PAGES = 5;

            public void run() {

                if (currentPage == NUM_PAGES - 1) {
                    currentPage = 0;
                }
                binding.viewPager.setCurrentItem(currentPage++, false);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);


//            images.get(i).getImagePath();
        binding.viewPager.setAdapter(new ViewpagerAdapterZoomSlider(context, imageListType));
         binding.indicator.setViewPager(binding.viewPager);
        final float density = context.getResources().getDisplayMetrics().density;
        // binding.indicator.setRadius(5 * density);
        NUM_Page = imageListType.size();
        new Handler().
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPage == NUM_Page) {
                            currentPage = 0;
                        }
                        binding.viewPager.setCurrentItem(currentPage++, true);
                    }
                }, 2000);

        binding.indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }
    }


