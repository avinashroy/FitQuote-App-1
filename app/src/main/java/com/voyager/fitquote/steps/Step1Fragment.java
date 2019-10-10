package com.voyager.fitquote.steps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.voyager.fitquote.R;
import com.voyager.fitquote.common.LoadingDialog;
import com.voyager.fitquote.task.Step1AsyncTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Step1Fragment extends Fragment implements Step {

    private View view;
    private TextInputEditText ageTextEdit;
    private MaterialSpinner coverOptionSpinner;
    private FrameLayout progressSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_wizard_screen_1, container, false);

        coverOptionSpinner = (MaterialSpinner) view.findViewById(R.id.cover_option_spnr);
        coverOptionSpinner.setItems(getResources().getStringArray(R.array.cover_options));
        coverOptionSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (!item.equalsIgnoreCase("Myself")) {
                    Snackbar.make(view, item + " is not supported yet", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        ageTextEdit = (TextInputEditText) view.findViewById(R.id.age_text_edit);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Runnable r = () -> {
            Step1AsyncTask startApplicationTask = new Step1AsyncTask();
            try {
                String applicationNo = startApplicationTask.execute(this).get();
//            Snackbar.make(view, applicationNo, Snackbar.LENGTH_LONG).show();
                Log.d("s1", applicationNo);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        new Thread(r).start();
    }


    @Override
    public VerificationError verifyStep() {
        if(coverOptionSpinner.getSelectedIndex() != 0) {
            Snackbar.make(view, "Only Myself is supported right now", Snackbar.LENGTH_LONG).show();
            return new VerificationError("Only Myself is supported right now");
        } else if ( ageTextEdit.getText() == null || ageTextEdit.getText().toString().equals("")) {
            Snackbar.make(view, "Please enter Age", Snackbar.LENGTH_LONG).show();
            return new VerificationError("Age is not entered");
        }
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
}
