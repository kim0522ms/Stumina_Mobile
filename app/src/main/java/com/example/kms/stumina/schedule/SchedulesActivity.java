package com.example.kms.stumina.schedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kms.stumina.ConnectTask;
import com.example.kms.stumina.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SchedulesActivity extends Activity {

    private CustomAdapterSchedule adapter;
    private ListView listView;
    private String std_no;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedules);

        Intent intent=new Intent(this.getIntent());
        std_no = intent.getStringExtra("text");

        System.out.println("스케쥴 찾을 스터디 번호 : " + std_no);
        adapter = new CustomAdapterSchedule(this);
        listView = (ListView) findViewById(R.id.listview_schedule);

        setData();

        listView.setAdapter(adapter);
    }

    void errorAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("아직 설정된 스케쥴이 없습니다!");
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
            returnFromServer = new ConnectTask().execute("stumina.azurewebsites.net/mobile/getStudySchedule","std_no="+std_no).get();
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
                        CustomScheduleDTO dto = new CustomScheduleDTO();

                        dto.setRsch_date(jArr.getJSONObject(i).get("rsch_date").toString());
                        dto.setRsch_name(jArr.getJSONObject(i).get("rsch_name").toString());
                        dto.setSr_name(jArr.getJSONObject(i).get("sr_name").toString());
                        dto.setSr_location(jArr.getJSONObject(i).get("sr_location").toString());
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
