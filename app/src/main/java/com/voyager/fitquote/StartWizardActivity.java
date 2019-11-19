package com.voyager.fitquote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.voyager.fitquote.model.Quotation;
import com.voyager.fitquote.model.SingletonDataHolder;
import com.voyager.fitquote.steps.Step1Fragment;
import com.voyager.fitquote.steps.Step2Fragment;
import com.voyager.fitquote.steps.Step3Fragment;
import com.voyager.fitquote.steps.StepPageAdapter;

public class StartWizardActivity extends AppCompatActivity {

    private static final String TAG = "StartWizardActivity";

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private MaterialButton btnBack, btnNext;
    private TextInputEditText tiAge;

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
        tiAge = (TextInputEditText) findViewById(R.id.age_text_edit);

        layouts = new int[] {
                R.layout.activity_wizard_screen_1,
                R.layout.activity_wizard_screen_2,
                R.layout.activity_wizard_screen_3
        };
        addBottomDots(0);
        updatePageElements();

//        stepPageAdapter = new StepPageAdapter(getFragmentManager(), layouts);
        stepPageAdapter = new StepPageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        stepPageAdapter.addFrag(new Step1Fragment(), "Step1Fragment");
        stepPageAdapter.addFrag(new Step2Fragment(), "Step2Fragment");
        stepPageAdapter.addFrag(new Step3Fragment(), "Step3Fragment");

        viewPager.setAdapter(stepPageAdapter);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                Log.d(TAG, "btnNext.onClick: " + viewPager.getCurrentItem());
                int current = getItem(+1);
                if (current < layouts.length) {
                    Log.d(TAG, "Current Item is " + current);
                    // Validation on next button click at first screen
                    if (current == 1) {
                        tiAge = (TextInputEditText) findViewById(R.id.age_text_edit);
                        Log.d(TAG, "onClick: value inthe textbox is " + tiAge.getText().toString());
                        /*if(tiAge.getText().toString() == null && tiAge.getText().toString().equals("")) {*/
                        if(tiAge.getText().toString().equals("")) {
                            Snackbar.make(v, "Please enter your age", Snackbar.LENGTH_LONG)
                                    .show();
                        } else if(Integer.parseInt(tiAge.getText().toString()) < 18  || Integer.parseInt(tiAge.getText().toString()) > 80) {
                            Snackbar.make(v, "Please note, to apply you need to be aged between 18 to 80 years old", Snackbar.LENGTH_LONG)
                                    .show();
                        } else {
                            SingletonDataHolder.getInstance().setAge(tiAge.getText().toString());
                            viewPager.setCurrentItem(current);
                            addBottomDots(current);
                        }
                    } else if (current == 2) {
                        RadioGroup rg = (RadioGroup) findViewById(R.id.rg_plan_options);
                        int selectedId = rg.getCheckedRadioButtonId();
                        Log.d(TAG, "selected radio " + selectedId);

                        if (selectedId > 0 ) {
                            for(Quotation q : SingletonDataHolder.getInstance().getQuotations()) {
                                if(q.getId() == selectedId) {
                                    SingletonDataHolder.getInstance().setSelectedQuotation(q);
                                    Log.d(TAG, "onClick: selected quotation " + q);
                                }
                            }

                            viewPager.setCurrentItem(current);
                            addBottomDots(current);
                        } else {
                            Snackbar.make(v, "Please choose a plan to proceed", Snackbar.LENGTH_LONG)
                                    .show();
                        }

                    }

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
            dots[i].setTextColor(getResources().getColor(R.color.secondaryColor));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.primaryTextColor));
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }
}
