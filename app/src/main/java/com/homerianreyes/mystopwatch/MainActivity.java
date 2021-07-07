package com.homerianreyes.mystopwatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;

import com.homerianreyes.mystopwatch.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean isRunning = false;
    private long pauseOffset;
    private ActivityMainBinding binding;
    private long mLastClickTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(binding.getRoot());

        binding.chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String t = (h < 10 ? "0"+h: h)+":"+(m < 10 ? "0"+m: m)+":"+ (s < 10 ? "0"+s: s);
                chronometer.setText(t);
            }
        });
        binding.chronometer.setBase(SystemClock.elapsedRealtime());
        binding.chronometer.setText("00:00:00");

        binding.startButton.setOnClickListener(this);
        binding.pauseButton.setOnClickListener(this);
        binding.resetButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

       if (binding.startButton.equals(view)) {
           if (!isRunning) {
               binding.chronometer.setBase(SystemClock.elapsedRealtime() + pauseOffset);
               binding.chronometer.start();
               isRunning = true;
           }
       } else if (binding.pauseButton.equals(view)) {
           if (isRunning) {
               binding.chronometer.stop();
               pauseOffset = binding.chronometer.getBase() - SystemClock.elapsedRealtime();
               isRunning = false;
           }
       } else if (binding.resetButton.equals(view)) {
            binding.chronometer.setBase(SystemClock.elapsedRealtime());
           binding.chronometer.setText("00:00:00");
            pauseOffset = 0;
       }
    }

}