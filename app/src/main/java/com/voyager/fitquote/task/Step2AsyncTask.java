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
import com.voyager.fitquote.steps.Step1Fragment;
import com.voyager.fitquote.steps.Step2Fragment;

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

public class Step2AsyncTask extends AsyncTask<Step2Fragment, String, String> {
    private static final String TAG = "Step2AsyncTask";
    Dialog loadingDialog;

    @Override
    protected String doInBackground(Step2Fragment... params) {
        Step2Fragment step2Fragment = params[0];
        SingletonDataHolder.getInstance().setQuotations(new ArrayList<>());

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            loadingDialog = LoadingDialog.show(step2Fragment.getContext());
        });

        QuotationsRequest quotationsRequest = new QuotationsRequest();
        InsuredPerson insuredPerson = new InsuredPerson();
        insuredPerson.setAge(SingletonDataHolder.getInstance().getAge());
        insuredPerson.setRelationshipWithApplicant("SELF");
        quotationsRequest.setInsuredPerson(new InsuredPerson[]{insuredPerson});
        quotationsRequest.setApplicationReferenceNumber(SingletonDataHolder.getInstance().getApplicationNo());
        quotationsRequest.setPaymentCurrency("HKD");
        quotationsRequest.setPaymentFrequency("M");
        quotationsRequest.setChildOnlyIndicator("NO");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        quotationsRequest.setPolicyStartDate(sdf.format(new Date()));
        quotationsRequest.setQuotationType("I");

        Log.d(TAG, "doInBackground: request body " + new Gson().toJson(quotationsRequest));
        OkHttpClient httpClient = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(new Gson().toJson(quotationsRequest), JSON);
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("X-HSBC-Channel-Id", "OHI");
        headersMap.put("X-HSBC-Chnl-CountryCode", "HK");
        headersMap.put("X-HSBC-Chnl-Group-Member", "HBAP");
        headersMap.put("X-HSBC-Locale", "en_HK");
        Headers headers = Headers.of(headersMap);
        Request request = new Request.Builder()
                .url("https://rbwm-api.hsbc.com.hk/dwi-firstcare-quotations-hk-ea-prod-proxy/v1/firstcare/quotations")
                .headers(headers)
                .post(body)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            Log.d(TAG, "response status : " + response.code());
            JsonObject jobj = new Gson().fromJson(response.body().string(), JsonObject.class);
            JsonArray jsonArray = jobj.get("quotations").getAsJsonArray();
            for (int i = 0; i<jsonArray.size(); i++) {
                JsonElement element = jsonArray.get(i);
                JsonObject currentObject = element.getAsJsonObject();
                Quotation quotation = new Quotation();
                quotation.setId(i + 1);
                quotation.setQuotationExternalReferenceNumber(currentObject.get("quotationInternalReferenceNumber").getAsString());
                quotation.setQuotationInternalReferenceNumber(currentObject.get("quotationExternalReferenceNumber").getAsString());

                JsonObject joAmountDetails = currentObject.get("quotationDetails").getAsJsonArray()
                                                            .get(0).getAsJsonObject()
                                                            .get("quotationDetailsAmount").getAsJsonArray()
                                                            .get(0).getAsJsonObject();
                quotation.setQuotationAmountTypeCode(joAmountDetails.get("quotationAmountTypeCode").getAsString());
                quotation.setQuotationAmount(joAmountDetails.get("quotationAmount").getAsJsonObject().get("amount").getAsString());
                quotation.setQuotationAmountCurrency(joAmountDetails.get("quotationAmount").getAsJsonObject().get("currency").getAsString());
                quotation.setSelected(false);

                Log.d(TAG, "doInBackground: " + quotation);
                SingletonDataHolder.getInstance().getQuotations().add(quotation);
            }

            LoadingDialog.hide(loadingDialog);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
