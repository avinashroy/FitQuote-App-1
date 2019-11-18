package com.voyager.fitquote.task;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.voyager.fitquote.common.LoadingDialog;
import com.voyager.fitquote.model.InsuredPerson;
import com.voyager.fitquote.model.Quotation;
import com.voyager.fitquote.model.QuotationsRequest;
import com.voyager.fitquote.model.SingletonDataHolder;
import com.voyager.fitquote.model.UserLifestyleRequest;
import com.voyager.fitquote.steps.Step3Fragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Step3AsyncTask extends AsyncTask<Step3Fragment, Object, Object> {
    private static final String TAG = "Step3AsyncTask";
    Dialog loadingDialog;

    @Override
    protected Object doInBackground(Step3Fragment... params) {
        Step3Fragment step3Fragment = params[0];
        SingletonDataHolder.getInstance().setQuotations(new ArrayList<>());

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            loadingDialog = LoadingDialog.show(step3Fragment.getContext());
        });

        OkHttpClient httpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        SingletonDataHolder dataHolder = SingletonDataHolder.getInstance();;
        UserLifestyleRequest userLifestyleRequest = new UserLifestyleRequest();
        userLifestyleRequest.setSteps(Integer.parseInt(dataHolder.getTotalSteps()));
        userLifestyleRequest.setAge(Integer.parseInt(dataHolder.getAge()));
        userLifestyleRequest.setCustomerId(Integer.parseInt(dataHolder.getCustomerId()));
        Log.d(TAG, "doInBackground: request body " + new Gson().toJson(userLifestyleRequest));
        RequestBody body = RequestBody.create(new Gson().toJson(userLifestyleRequest), JSON);
        Request request = new Request.Builder()
                .url("https://826j7jcu7k.execute-api.us-east-1.amazonaws.com/dev/user-lifestyle")
                .post(body)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            Log.d(TAG, "response status : " + response.code());
            JsonObject jobj = new Gson().fromJson(response.body().string(), JsonObject.class);

            Log.d(TAG, "doInBackground: response json " + jobj.toString());
            String discountStr = jobj.get("discount").getAsString();
            Log.d(TAG, "doInBackground: discountStr : " + discountStr);
            String discount = "0";

            if(!discountStr.equals("Nil")) {;
                discountStr = discountStr.substring(0, 1);
                discount = discountStr;
            }
            dataHolder.setDiscount(discount);
            dataHolder.setLifestyle(jobj.get("lifestyle").getAsString());

            LoadingDialog.hide(loadingDialog);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
