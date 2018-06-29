package com.example.kms.stumina;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import static com.example.kms.stumina.MainActivity.mainContext;

public class LoadingActivity extends Activity {
    private TextView text_loading;
    private String user_idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Intent intent = getIntent();
        user_idx = intent.getExtras().getString("user_idx");

        startLoading();
    }
    private void startLoading() {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    MainActivity.returnFromServer = new ConnectTask().execute("stumina.azurewebsites.net/mobile/initApp","user_idx="+user_idx).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                ((MainActivity)MainActivity.mainContext).setData("home");
                ((MainActivity)MainActivity.mainContext).setData("leader");
                ((MainActivity)MainActivity.mainContext).setData("notification");
                ((MainActivity)MainActivity.mainContext).endLoadData();

                text_loading = (TextView)findViewById(R.id.text_loading);
                text_loading.setText("데이터 수신 완료");

                finish();
            }
        }, 1000);
    }
}
