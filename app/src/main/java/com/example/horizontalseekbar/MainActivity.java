package com.example.horizontalseekbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private HorizontalSeekBar horizontalSeekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        horizontalSeekBar=(HorizontalSeekBar)findViewById(R.id.sb_horizontal);
        horizontalSeekBar.setProgressLeft(90);
        horizontalSeekBar.setProgressRight(80);
        horizontalSeekBar.setOnSlideListener(new HorizontalSeekBar.OnSlideListener() {
            @Override
            public void onSlidingProgress(int progressLeft,int progressRight) {

            }

            @Override
            public void onSlidProgress(int progressLeft,int progressRight) {
                Log.v("test","progressLeft="+progressLeft);
                Log.v("test","progressRight="+progressRight);
            }
        });
    }
}
