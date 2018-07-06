package com.example.kms.stumina.Attandance;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kms.stumina.R;

import java.util.ArrayList;

public class CustomAdapterAttendance extends BaseAdapter {

    private ArrayList<CustomAttendanceDTO> listCustom = new ArrayList<>();
    private Context context;
    private String mode;
    private String rsch_idx;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getRsch_idx() {
        return rsch_idx;
    }

    public void setRsch_idx(String rsch_idx) {
        this.rsch_idx = rsch_idx;
    }


    public CustomAdapterAttendance(Context context) {
        this.context = context;
        this.mode = "";
    }

    @Override
    public int getCount() {
        return listCustom.size();
    }

    // 하나의 Item(ImageView 1, TextView 2)
    @Override
    public Object getItem(int position) {
        return listCustom.get(position);
    }

    // Item의 id : Item을 구별하기 위한 것으로 position 사용
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 실제로 Item이 보여지는 부분
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_attendance, null, false);

            holder = new CustomViewHolder();

            holder.text_checkAttName = (TextView) convertView.findViewById(R.id.text_checkAttName);
            holder.cb_attendance = (CheckBox)convertView.findViewById(R.id.cb_attendance);

            convertView.setTag(holder);
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        final CustomAttendanceDTO dto = listCustom.get(position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("선택된 회원명 : ", "" + dto.getUser_name());
                holder.cb_attendance.setChecked(!(holder.cb_attendance.isChecked()));
                dto.setAtt_value(holder.cb_attendance.isChecked());
                listCustom.set(position,dto);
            }
        });

        (convertView.findViewById(R.id.cb_attendance)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dto.setAtt_value(holder.cb_attendance.isChecked());
                listCustom.set(position,dto);
            }
        });


        holder.text_checkAttName.setText(dto.getUser_name());
        holder.cb_attendance.setChecked(dto.isAtt_value());

        return convertView;
    }

    class CustomViewHolder {
        TextView text_checkAttName;
        CheckBox cb_attendance;
    }

    // MainActivity에서 Adapter에있는 ArrayList에 data를 추가시켜주는 함수
    public void addItem(CustomAttendanceDTO dto) {
        listCustom.add(dto);
    }
}
