package com.voyager.fitquote.task;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.voyager.fitquote.R;
import com.voyager.fitquote.common.LoadingDialog;
import com.voyager.fitquote.steps.Step1Fragment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Step1AsyncTask extends AsyncTask<Step1Fragment, String, String> {
    Dialog loadingDialog;

    @Override
    protected String doInBackground(Step1Fragment... params) {
        Step1Fragment step1Fragment = params[0];

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            loadingDialog = LoadingDialog.show(step1Fragment.getContext());
        });

        OkHttpClient httpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create("{\"policyDetails\":{\"productCode\":\"PM4\"}}", JSON);
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("X-HSBC-Channel-Id", "OHI");
        headersMap.put("X-HSBC-Chnl-CountryCode", "HK");
        headersMap.put("X-HSBC-Chnl-Group-Member", "HBAP");
        headersMap.put("X-HSBC-Locale", "en_HK");
        Headers headers = Headers.of(headersMap);
        Request request = new Request.Builder()
                .url("https://rbwm-api.hsbc.com.hk/dwi-new-applications-hk-ea-prod-proxy/v1/new-applications/")
                .headers(headers)
                .post(body)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            JsonObject jobj = new Gson().fromJson(response.body().string(), JsonObject.class);
            LoadingDialog.hide(loadingDialog);
            return jobj.get("applicationReferenceNumber").getAsString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
