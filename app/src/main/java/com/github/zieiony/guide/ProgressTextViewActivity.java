package com.github.zieiony.guide;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.zieiony.guide.progresstextview.ProgressTextView_Bad;
import com.github.zieiony.guide.progresstextview.ProgressTextView_Good;
import com.github.zieiony.guide.progresstextview.ProgressTextView_Medium;

@ActivityAnnotation(title = "ProgressTextView")
public class ProgressTextViewActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progresstextview);

        final ProgressTextView_Bad progress = findViewById(R.id.prgressTextView_bad);
        progress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                progress.setProgress(event.getX() / v.getWidth());
                return true;
            }
        });

        final ProgressTextView_Medium progress2 = findViewById(R.id.prgressTextView_medium);
        progress2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                progress2.setProgress(event.getX() / v.getWidth());
                return true;
            }
        });

        final ProgressTextView_Good progress3 = findViewById(R.id.prgressTextView_good);
        progress3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                progress3.setProgress(event.getX() / v.getWidth());
                return true;
            }
        });
    }
}
