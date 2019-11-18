package com.voyager.fitquote.steps;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.voyager.fitquote.R;
import com.voyager.fitquote.StartWizardActivity;
import com.voyager.fitquote.common.LoadingDialog;
import com.voyager.fitquote.model.Quotation;
import com.voyager.fitquote.model.SingletonDataHolder;
import com.voyager.fitquote.task.Step1AsyncTask;
import com.voyager.fitquote.task.Step2AsyncTask;

import java.util.concurrent.ExecutionException;

public class Step2Fragment extends Fragment implements Step {
    private static final String TAG = "Step2Fragment";
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_wizard_screen_2, container, false);
        //initialize your UI

        return view;
    }

    @Override
    public VerificationError verifyStep() {
        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        return null;
    }

    @Override
    public void onSelected() {
        //update UI when selected
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
    }

    @Override
    public void onResume() {
        super.onResume();
        Runnable r = () -> {
            Step2AsyncTask task = new Step2AsyncTask();
            try {
                task.execute(this).get();

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    updateQuotationList();
                });

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        new Thread(r).start();
    }

    private void updateQuotationList() {
        RadioGroup rg = (RadioGroup) view.findViewById(R.id.rg_plan_options);
        int count = rg.getChildCount();
        if(count>0) {
            for (int i=count-1;i>=0;i--) {
                View o = rg.getChildAt(i);
                if (o instanceof RadioButton) {
                    rg.removeViewAt(i);
                }
            }
        }

        for (Quotation q : SingletonDataHolder.getInstance().getQuotations()) {
            Log.d(TAG, "updateQuotationList: " + q.getId());
            RadioButton rdbtn = new RadioButton(view.getContext());
            rdbtn.setId(q.getId());
            rdbtn.setText(q.getQuotationAmountTypeCode() + " - " + q.getQuotationAmountCurrency() + " " + q.getQuotationAmount());
            if (q.getId() == 1) rdbtn.setSelected(true);
            rg.addView(rdbtn);
        }

    }
}
