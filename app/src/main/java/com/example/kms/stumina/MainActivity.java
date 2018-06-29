package com.example.kms.stumina;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kms.stumina.LeaderStudy.LeaderStudyDTO;
import com.example.kms.stumina.Notification.CustomAdapterNotification;
import com.example.kms.stumina.Notification.NotificationDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CustomAdapter adapter;
    private CustomAdapter adapter_leader;
    private CustomAdapterNotification adapter_notification;
    public static ListView listView;
    private ArrayList<String> imageUrl;
    private TextView text_nodata, text_toptext;

    public static String returnFromServer;
    public static ArrayList<ImageView> imagesID = new ArrayList<>();
    public static String recievedJson = "";
    public static Context mainContext;

    private static final int REQUEST_LOGIN = 1;
    private static final int LOGIN_SUCCESS = 2;
    private static final int LOGIN_FAILED = 3;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    text_toptext.setText("참여 중인 스터디 목록");
                    if (adapter == null || adapter.getCount() == 0)
                    {
                        text_nodata.setText("참여 중인 스터디가 없습니다 !");
                        listView.setVisibility(View.INVISIBLE);
                        text_nodata.setVisibility(View.VISIBLE);
                    }
                    else {
                        setData("home");
                        listView.setAdapter(adapter);
                        endLoadData();
                        text_nodata.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.VISIBLE);
                    }
                    return true;
                case R.id.navigation_dashboard:
                    text_toptext.setText("출석 체크할 스터디를 선택해 주세요.");
                    if (adapter_leader == null || adapter_leader.getCount() == 0)
                    {
                        text_nodata.setText("직접 개설한 스터디가 없습니다 !");
                        listView.setVisibility(View.INVISIBLE);
                        text_nodata.setVisibility(View.VISIBLE);
                    }
                    else {
                        setData("leader");
                        listView.setAdapter(adapter_leader);
                        text_nodata.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.VISIBLE);
                    }
                    return true;
                case R.id.navigation_notifications:
                    text_toptext.setText("받은 알림 확인");
                    if (adapter_notification == null || adapter_notification.getCount() == 0)
                    {
                        text_nodata.setText("알림이 없습니다 !");
                        listView.setVisibility(View.INVISIBLE);
                        text_nodata.setVisibility(View.VISIBLE);
                    }
                    else {
                        listView = (ListView) findViewById(R.id.listView);
                        setData("notification");
                        listView.setAdapter(adapter_notification);
                        text_nodata.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.VISIBLE);
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainContext = this;

        Intent intent = new Intent(this, LoginActivity.class);
        //startActivity(intent);

        startActivityForResult(intent, REQUEST_LOGIN);

        text_nodata = (TextView)findViewById(R.id.text_nodata);
        text_toptext = (TextView)findViewById(R.id.text_toptext);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        adapter_notification = new CustomAdapterNotification( this);

        listView = (ListView) findViewById(R.id.listView);

        imageUrl = new ArrayList<>();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == LOGIN_SUCCESS) {
            String user_idx = data.getStringExtra("user_idx");

            Intent intent = new Intent(this, LoadingActivity.class);
            intent.putExtra("user_idx", user_idx);
            startActivity(intent);
            return;
        }
    }

    public void endLoadData(){
        listView.setAdapter(adapter);

        System.out.println("이미지 사이즈 : " +imageUrl.size());
        System.out.println("리스트뷰 이미지 갯수 : " + imagesID.size());

        setListImages();
    }

    public void setListImages(){
        new Thread()
        {
            @Override
            public void run()
            {
                for (int i = 0; i < imageUrl.size() ; i++) {
                    System.out.println(imageUrl.get(i));
                    try {
                        URL url = new URL(imageUrl.get(i));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        final Bitmap  bitmap = BitmapFactory.decodeStream(is);

                        final int imageViewIdx = i;

                        Log.d("imageViewIdx", "" + imageViewIdx);
                        Log.d("imagesID", "" + imagesID.get(imageViewIdx));
                        Log.d("Bitmap","" + bitmap.getHeight());

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        imagesID.get(imageViewIdx).setImageBitmap(bitmap);
                                    }
                                });
                            }
                        }).start();

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void setData(String mode) {
        System.out.println(returnFromServer);

        if (returnFromServer != null) {
            System.out.println(returnFromServer);

            if (mode.equals("home")) {
                try {
                    JSONObject returnedJobj = new JSONObject(returnFromServer);

                    Object temp = returnedJobj.get("return");

                    if (temp != null && !(temp.toString().equals("NODATA"))) {
                        System.out.println(temp.toString());

                        adapter = new CustomAdapter(this);
                        imagesID = new ArrayList<>();
                        imageUrl = new ArrayList<>();

                        JSONArray jArr = (JSONArray) temp;

                        Log.d("테스트", "" + jArr.length());

                        for (int i = 0; i < jArr.length(); i++) {
                            CustomDTO dto = new CustomDTO();

                            //dto.setImage(bitmap);
                            imageUrl.add(jArr.getJSONObject(i).get("std_image").toString());

                            //dto.setResId(arrResId.getResourceId(i, 0));
                            dto.setTitle(jArr.getJSONObject(i).get("std_name").toString());
                            dto.setContent(jArr.getJSONObject(i).get("std_contents").toString());
                            dto.setStd_no(jArr.getJSONObject(i).get("std_no").toString());
                            adapter.addItem(dto);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (mode.equals("leader")) {

                try {
                    JSONObject returnedJobj = new JSONObject(returnFromServer);

                    Object temp = returnedJobj.get("leaderStudy");

                    Log.d("테스트", temp.toString());

                    if (temp != null && !(temp.toString().equals("NODATA"))) {
                        System.out.println(temp.toString());

                        adapter = new CustomAdapter(this);
                        imagesID = new ArrayList<>();
                        imageUrl = new ArrayList<>();

                        JSONArray jArr = (JSONArray) temp;

                        for (int i = 0; i < jArr.length(); i++) {
                            CustomDTO dto = new CustomDTO();

                            //dto.setImage(bitmap);
                            imageUrl.add(jArr.getJSONObject(i).get("std_image").toString());

                            //dto.setResId(arrResId.getResourceId(i, 0));
                            dto.setTitle(jArr.getJSONObject(i).get("std_name").toString());
                            dto.setContent(jArr.getJSONObject(i).get("std_contents").toString());
                            dto.setStd_no(jArr.getJSONObject(i).get("std_no").toString());
                            adapter_leader.addItem(dto);
                        }
                    }
                } catch (JSONException e) {
                    Log.d("경고", "LeaderStudy is Null");
                }
            }
            else if (mode.equals("notification")) {
                try {
                    Log.d("테스트", "ㅠㅠ");
                    JSONObject returnedJobj = new JSONObject(returnFromServer);

                    Object temp = returnedJobj.get("notification");

                    Log.d("테스트", temp.toString());

                    if (temp != null && !(temp.toString().equals("NODATA"))) {
                        System.out.println(temp.toString());

                        adapter_leader = new CustomAdapter(this);

                        JSONArray jArr = (JSONArray) temp;

                        for (int i = 0; i < jArr.length(); i++) {
                            NotificationDTO dto = new NotificationDTO();

                            //dto.setResId(arrResId.getResourceId(i, 0));

                            dto.setNoti_idx(jArr.getJSONObject(i).get("noti_idx").toString());
                            dto.setNoti_date(jArr.getJSONObject(i).get("noti_date").toString());
                            dto.setNoti_text(jArr.getJSONObject(i).get("noti_text").toString());
                            dto.setNoti_read(jArr.getJSONObject(i).get("noti_read").toString());
                            adapter_notification.addItem(dto);

                            Log.d("테스트", "알림 추가됨");
                        }
                    }
                } catch (JSONException e) {
                    Log.d("경고", "LeaderStudy is Null");
                }
            }
        }
    }
}