package com.github.zieiony.guide;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.github.zieiony.guide.progresstextview.ProgressTextView_Bad;
import com.github.zieiony.guide.progresstextview.ProgressTextView_Good;
import com.github.zieiony.guide.progresstextview.ProgressTextView_Medium;

@ActivityAnnotation(layout = R.layout.activity_progresstextview, title = "ProgressTextView")
public class ProgressTextViewActivity extends SampleActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ProgressTextView_Bad progress = findViewById(R.id.prgressTextView_bad);
        progress.setOnTouchListener((v, event) -> {
            progress.setProgress(event.getX() / v.getWidth());
            return true;
        });

        final ProgressTextView_Medium progress2 = findViewById(R.id.prgressTextView_medium);
        progress2.setOnTouchListener((v, event) -> {
            progress2.setProgress(event.getX() / v.getWidth());
            return true;
        });

        final ProgressTextView_Good progress3 = findViewById(R.id.prgressTextView_good);
        progress3.setOnTouchListener((v, event) -> {
            progress3.setProgress(event.getX() / v.getWidth());
            return true;
        });
    }
}
