package com.example.kms.stumina;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CustomAdapter adapter;
    private ListView listView;
    private ArrayList<String> imageUrl;
    public static ArrayList<ImageView> imagesID = new ArrayList<>();
    public static String recievedJson = "";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    listView.setAdapter(adapter);
                    return true;
                case R.id.navigation_dashboard:
                    listView.setAdapter(null);
                    return true;
                case R.id.navigation_notifications:
                    listView.setAdapter(null);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        adapter = new CustomAdapter(this);
        listView = (ListView) findViewById(R.id.listView);

        imageUrl = new ArrayList<>();

        setData();

        listView.setAdapter(adapter);

        System.out.println("이미지 사이즈 : " +imageUrl.size());
        System.out.println("리스트뷰 이미지 갯수 : " + imagesID.size());

        setListImages();
    }

    private void setListImages(){
        Thread thread = new Thread()
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
        };
        thread.start();
    }

    // 보통 ListView는 통신을 통해 가져온 데이터를 보여줍니다.
    // arrResId, titles, contents를 서버에서 가져온 데이터라고 생각하시면 됩니다.
    private void setData() {
        /*TypedArray arrResId = getResources().obtainTypedArray(R.array.resId);
        String[] titles = getResources().getStringArray(R.array.title);
        String[] contents = getResources().getStringArray(R.array.content);*/

        String returnFromServer = null;
        try {
             returnFromServer = new ConnectTask().execute("stumina.azurewebsites.net/mobile/getAllMyStudies","user_idx=1").get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(returnFromServer);
        System.out.println(returnFromServer);
        System.out.println(returnFromServer);

        if (returnFromServer != null) {
            System.out.println(returnFromServer);

            try {
                JSONObject returnedJobj = new JSONObject(returnFromServer);

                Object temp = returnedJobj.get("return");

                if (temp != null) {
                    JSONArray jArr = (JSONArray) temp;

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
    }
}