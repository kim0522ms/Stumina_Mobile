package com.example.kms.stumina.Attandance;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.kms.stumina.MainActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class UpdateAttendanceTask extends AsyncTask<String, Void, Void> {

    private JSONObject json;
    private String rsch_idx;

    UpdateAttendanceTask(JSONObject json, String rsch_idx) {
        // list all the parameters like in normal class define
        this.json = json;
        this.rsch_idx = rsch_idx;
    }

    @Override
    protected Void doInBackground(String... strings) {

        Log.d("뭘까 대체", json.toString());

        /*String replacedJson = json.toString().replace('\\', '&');
        replacedJson = replacedJson.replaceAll("&","");*/

        HttpURLConnection urlConn = null;

        URL url = null;
        try {
            url = new URL("http://"+ MainActivity.server_url + "/mobile/updateAbsent");

            urlConn = (HttpURLConnection) url.openConnection();

            // [2-1]. urlConn 설정.
            urlConn.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : POST.
            urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
            urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8");

            String parameters = "json="+json.toString()+"&rsch_idx="+rsch_idx;

            OutputStream os = urlConn.getOutputStream();
            os.write(parameters.getBytes("UTF-8")); // 출력 스트림에 출력.
            os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
            os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.

            // 실패 시 null을 리턴하고 메서드를 종료.
            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            String line = "";
            String page = "";
            String receiveMsg = "";

            while ((line = reader.readLine()) != null){
                receiveMsg += line;
                receiveMsg += System.lineSeparator();
            }

            System.out.println("테스트 : " + receiveMsg);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        urlConn.disconnect();

/*
        OkHttpClient client = new OkHttpClient();
        System.out.println(json.toString());
        System.out.println(rsch_idx);

        RequestBody body = new FormBody.Builder()
                .add("json", json.toString())
                .add("rsch_idx",rsch_idx)
                .build();

        Request request = new Request.Builder()
                .url("https://stumina.azurewebsites.net/mobile/updateAbsent")
                .post(body)
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
        }*/
        return null;
    }
}
