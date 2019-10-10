package com.voyager.fitquote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.voyager.fitquote.steps.StepPageAdapter;

public class StartWizardActivity extends AppCompatActivity {

    private static final String TAG = "StartWizardActivity";

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private MaterialButton btnBack, btnNext;

    private StepPageAdapter stepPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_wizard);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primaryDarkColor));
        }

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnBack = (MaterialButton) findViewById(R.id.btn_back);
        btnNext = (MaterialButton) findViewById(R.id.btn_next);

        layouts = new int[] {
                R.layout.activity_wizard_screen_1,
                R.layout.activity_wizard_screen_2,
                R.layout.activity_wizard_screen_3
        };
        addBottomDots(0);
        updatePageElements();

        stepPageAdapter = new StepPageAdapter(this, layouts);
        viewPager.setAdapter(stepPageAdapter);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                    addBottomDots(current);
                } else {
                    Snackbar.make(v, "Your application has been submitted", Snackbar.LENGTH_LONG)
                            .show();
                }

                updatePageElements();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnBack.onClick: " + viewPager.getCurrentItem());
                int backLocation = viewPager.getCurrentItem() - 1;
                if (backLocation >= 0) {
                    // move to next screen
                    viewPager.setCurrentItem(backLocation);
                    addBottomDots(backLocation);
                }

                updatePageElements();
            }
        });
    }

    private void updatePageElements() {
        int position = viewPager.getCurrentItem();

        if(position == 0) {
            btnBack.setVisibility(View.GONE);
        } else {
            btnBack.setVisibility(View.VISIBLE);
        }

        if(position >= layouts.length -1) {
            btnNext.setText("Finish");
            btnNext.setIcon(null);
        } else {
            btnNext.setText("Next");
            btnNext.setIcon(getDrawable(R.drawable.ic_chevron_right_black_24dp));
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this, null, 0, R.style.FitQuoteDotShadow);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.primaryTextColor));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.secondaryColor));
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }
}
