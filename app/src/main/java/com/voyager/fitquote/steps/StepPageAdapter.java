package com.voyager.fitquote.steps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.button.MaterialButton;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;
import com.voyager.fitquote.R;

public class StepPageAdapter extends PagerAdapter {
    private static final String TAG = "StepPageAdapter";

    int[] layouts;
    Activity activity;
    private LayoutInflater layoutInflater;
    private MaterialButton btnBack, btnNext;

    public StepPageAdapter(Activity context, int[] layouts) {
        this.activity = context;
        this.layouts = layouts;

        btnBack = (MaterialButton) activity.findViewById(R.id.btn_back);
        btnNext = (MaterialButton) activity.findViewById(R.id.btn_next);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(layouts[position], container, false);
        container.addView(view);

        Log.d(TAG, "instantiateItem: " + position);


        return view;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
