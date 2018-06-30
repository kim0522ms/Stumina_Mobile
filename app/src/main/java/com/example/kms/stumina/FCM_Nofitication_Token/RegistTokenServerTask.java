package com.example.kms.stumina.FCM_Nofitication_Token;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RegistTokenServerTask extends AsyncTask<String,Void,Void> {
    @Override
    protected Void doInBackground(String... strings) {
        String token = strings[0];
        String user_idx = strings[1];

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("token", token)
                .add("user_idx", user_idx)
                .build();

        Request request = new Request.Builder()
                .url("https://stumina.azurewebsites.net/mobile/registToken")
                .post(body)
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
