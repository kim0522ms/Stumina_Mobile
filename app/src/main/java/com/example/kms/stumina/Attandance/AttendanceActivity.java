package com.example.kms.stumina.Attandance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.kms.stumina.ConnectTask;
import com.example.kms.stumina.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AttendanceActivity extends AppCompatActivity {

    private CustomAdapterAttendance adapter;
    private ListView listView;
    private String rsch_idx;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_attendance);
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

        System.out.println("출석 테이블 찾을 스케쥴 번호 : " + rsch_idx);
        adapter = new CustomAdapterAttendance(this);
        listView = (ListView) findViewById(R.id.listview_check);

        setData();

        listView.setAdapter(adapter);
    }

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
            returnFromServer = new ConnectTask().execute("stumina.azurewebsites.net/mobile/getScheduleAttendance","rsch_idx="+rsch_idx).get();
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
