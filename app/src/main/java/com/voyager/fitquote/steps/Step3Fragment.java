package com.voyager.fitquote.steps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.voyager.fitquote.R;
import com.voyager.fitquote.model.SingletonDataHolder;
import com.voyager.fitquote.task.Step2AsyncTask;
import com.voyager.fitquote.task.Step3AsyncTask;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getTimeInstance;

public class Step3Fragment extends Fragment implements Step, View.OnClickListener {

    final String TAG = "Step3Fragment";
    final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
    View view;
    MaterialButton googleFitBtn;

    TextView tvAge;
    TextView tvSteps;
    TextView tvStatus;
    TextView tvDiscount;
    TextView tvFinalAmount;



    private int totalSteps = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_wizard_screen_3, container, false);
        //initialize your UI

        googleFitBtn = (MaterialButton) view.findViewById(R.id.google_fit_btn);
        googleFitBtn.setOnClickListener(this);



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tvAge = (TextView) view.findViewById(R.id.tv_s3_age);
        tvSteps = (TextView) view.findViewById(R.id.tv_s3_steps);
        tvStatus = (TextView) view.findViewById(R.id.tv_s3_status);
        tvDiscount = (TextView) view.findViewById(R.id.tv_s3_discount);
        tvFinalAmount = (TextView) view.findViewById(R.id.tv_s3_finalAmt);

        tvAge.setText(SingletonDataHolder.getInstance().getAge());
        tvFinalAmount.setText(SingletonDataHolder.getInstance().getSelectedQuotation().getQuotationAmount());
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
    public void onClick(View v) {
        Log.d(TAG, "onClick: google fitbutton clicked");
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(view.getContext()), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(view.getContext()),
                    fitnessOptions);
        } else {
            accessGoogleFit();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                accessGoogleFit();
            }
        }
    }

    private void accessGoogleFit() {
        Log.d("GSI", "accessGoogleFit()");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.YEAR, -1);
        long startTime = cal.getTimeInMillis();


        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(180, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();



        Fitness.getHistoryClient(view.getContext(), GoogleSignIn.getLastSignedInAccount(view.getContext()))
                .readData(readRequest)
                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        aggregateData(dataReadResponse);
//                        Snackbar.make(view, "Total Steps : " + totalSteps, Snackbar.LENGTH_LONG).show();
                        Log.d(TAG, "onSuccess: totalSteps : "  + totalSteps);
                        // fixing bug in lambda
                        Step3Fragment.this.tvSteps.setText(totalSteps + "");
                        SingletonDataHolder.getInstance().setTotalSteps(totalSteps + "");

                        // customer id generation TPDO - get customer id from google
                        Random random = new Random();
                        SingletonDataHolder.getInstance().setCustomerId(String.format("%04d", random.nextInt(10000)));

                        // Call AWS Lambda
                        Runnable r = () -> {
                            Step3AsyncTask task = new Step3AsyncTask();
                            try {
                                task.execute(Step3Fragment.this).get();

                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(() -> {
                                    Log.d(TAG, "onSuccess: quotation " + SingletonDataHolder.getInstance().getSelectedQuotation());
                                    Log.d(TAG, "onSuccess: discount amount " + SingletonDataHolder.getInstance().getDiscount());
                                    double discountAmount = (Double.parseDouble(SingletonDataHolder.getInstance().getDiscount()) / 100) *
                                            Double.parseDouble(SingletonDataHolder.getInstance().getSelectedQuotation().getQuotationAmount());

                                    DecimalFormat df = new DecimalFormat("0.00");
                                    String discountAmountStr = df.format(discountAmount);

                                    tvDiscount.setText(SingletonDataHolder.getInstance().getDiscount() + " % = HKD " + discountAmountStr);
                                    tvStatus.setText(SingletonDataHolder.getInstance().getLifestyle());
                                    tvFinalAmount.setText("HKD " + (Double.parseDouble(SingletonDataHolder.getInstance().getSelectedQuotation().getQuotationAmount()) - discountAmount));
                                });

                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        };

                        new Thread(r).start();

                        totalSteps = 0;
                        Log.d("GSI", "onSuccess()");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("GSI", "onFailure()", e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DataReadResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<DataReadResponse> task) {
                        Log.d("GSI", "onComplete()");
                    }
                });
    }


    public void aggregateData(DataReadResponse dataReadResult) {
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    aggregateDataSet(dataSet);
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: " + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                aggregateDataSet(dataSet);
            }
        }
    }

    private void aggregateDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                if (field.getName().equalsIgnoreCase("steps")) {
                    totalSteps += dp.getValue(field).asInt();
                }
            }
        }


    }
}
