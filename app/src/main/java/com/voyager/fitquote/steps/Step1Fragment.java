package com.voyager.fitquote.steps;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.voyager.fitquote.R;
import com.voyager.fitquote.common.LoadingDialog;
import com.voyager.fitquote.model.SingletonDataHolder;
import com.voyager.fitquote.task.Step1AsyncTask;

import java.util.concurrent.ExecutionException;

public class Step1Fragment extends Fragment implements Step {

    private View view;
    private TextInputEditText ageTextEdit;
    private MaterialSpinner coverOptionSpinner;
    private FrameLayout progressSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_wizard_screen_1, container, false);

        //coverOptionSpinner = (MaterialSpinner) view.findViewById(R.id.cover_option_spnr);

        /*coverOptionSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (!item.equalsIgnoreCase("Myself")) {
                    Snackbar.make(view, item + " is not supported yet", Snackbar.LENGTH_LONG).show();
                }
            }
        });*/

        ageTextEdit = (TextInputEditText) view.findViewById(R.id.age_text_edit);
       // ageTextEdit.setFilters(new InputFilter[]{ new MinMaxFilter("18", "80")});
       // ageTextEdit.setFilters( new InputFilter[]{ new MinMaxFilter( "18" , "80" )}) ;


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Runnable r = () -> {
            Step1AsyncTask startApplicationTask = new Step1AsyncTask();
            try {
                String applicationNo = startApplicationTask.execute(this).get();
                Log.d("s1", applicationNo);
                SingletonDataHolder dataHolder = SingletonDataHolder.getInstance();
                dataHolder.setApplicationNo(applicationNo);
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
        if( ageTextEdit.getText() == null || ageTextEdit.getText().toString().equals("")) {
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
