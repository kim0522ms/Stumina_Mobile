package com.example.kms.stumina.Attandance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kms.stumina.ConnectTask;
import com.example.kms.stumina.MainActivity;
import com.example.kms.stumina.R;
import com.example.kms.stumina.schedule.SchedulesActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AttendanceActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/{

    private CustomAdapterAttendance adapter;
    private ListView listView;
    private String rsch_idx;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_attendance);

        MenuInflater inflater = getMenuInflater();

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = new Intent(this.getIntent());

        rsch_idx = intent.getStringExtra("rsch_idx");

        /*Bundle bundle = intent.getExtras();

        std_no = bundle.getString("text");
        mode = bundle.getString("mode");*/
        /*
        std_no = intent.getStringExtra("text");

        mode = intent.getStringExtra("mode");

        if (mode.equals("attandance"))
        {
            adapter.setMode(mode);
        }
        */

        TextView submitButton = (TextView)findViewById(R.id.btn_submitAttendance);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("저장 버튼 눌림");

                CustomAttendanceDTO att_adapter;

                JSONObject jobj = new JSONObject();
                JSONArray jArr = new JSONArray();
                JSONObject tempObj;

                for (int i = 0; i < adapter.getCount(); i++)
                {
                    tempObj = new JSONObject();

                    att_adapter = (CustomAttendanceDTO)adapter.getItem(i);

                    try {
                        tempObj.put("user_idx", att_adapter.getUser_idx());
                        tempObj.put("absent", att_adapter.isAtt_value() ? "1" : "0");

                        jArr.put(tempObj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    jobj.put("return", jArr.toString());
                    jobj.put("rsch_idx",rsch_idx);

                    System.out.println("aaa");

                    new UpdateAttendanceTask(jobj, rsch_idx).execute("").get();

                    Toast.makeText(AttendanceActivity.this,"출석 정보가 갱신되었습니다",Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        System.out.println("출석 테이블 찾을 스케쥴 번호 : " + rsch_idx);
        adapter = new CustomAdapterAttendance(this);
        listView = (ListView) findViewById(R.id.listview_check);

        setData();

        listView.setAdapter(adapter);
    }
/*

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    void errorAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("서버와의 통신에 실패했습니다!");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        /*  builder.setNegativeButton("아니오",
        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
            }
        });*/
        builder.show();
    }

    private void setData() {
        /*TypedArray arrResId = getResources().obtainTypedArray(R.array.resId);
        String[] titles = getResources().getStringArray(R.array.title);
        String[] contents = getResources().getStringArray(R.array.content);*/

        String returnFromServer = null;
        try {
            returnFromServer = new ConnectTask().execute(MainActivity.server_url + "/mobile/getScheduleAttendance","rsch_idx="+rsch_idx).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (returnFromServer != null) {
            System.out.println(returnFromServer);

            try {
                JSONObject returnedJobj = new JSONObject(returnFromServer);

                Object temp = returnedJobj.get("return");

                if (temp != null) {
                    JSONArray jArr = (JSONArray) temp;

                    for (int i = jArr.length() - 1; i >= 0 ; i--) {
                        CustomAttendanceDTO dto = new CustomAttendanceDTO();

                        boolean absent = jArr.getJSONObject(i).get("absent").toString().equals("0") ? false : true;
                        dto.setUser_name(jArr.getJSONObject(i).get("user_name").toString());
                        dto.setUser_idx(jArr.getJSONObject(i).get("user_idx").toString());
                        dto.setAtt_value(absent);
                        adapter.addItem(dto);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                errorAlertDialog();
            }
        }
    }
}
