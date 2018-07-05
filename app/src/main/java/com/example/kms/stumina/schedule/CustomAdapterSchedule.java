package com.example.kms.stumina.schedule;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kms.stumina.Attandance.AttendanceActivity;
import com.example.kms.stumina.CustomDTO;
import com.example.kms.stumina.R;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapterSchedule  extends BaseAdapter {

    private ArrayList<CustomScheduleDTO> listCustom = new ArrayList<>();
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


    public CustomAdapterSchedule(Context context) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_schedules, null, false);

            holder = new CustomViewHolder();

            holder.text_rsch_name = (TextView) convertView.findViewById(R.id.text_rsch_name);
            holder.text_sr_name = (TextView) convertView.findViewById(R.id.text_sr_name);
            holder.text_sr_location = (TextView) convertView.findViewById(R.id.text_sr_location);
            holder.text_date_yyyy = (TextView) convertView.findViewById(R.id.text_date_yyyy);
            holder.text_date_mmdd = (TextView) convertView.findViewById(R.id.text_date_mmdd);

            convertView.setTag(holder);
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        final CustomScheduleDTO dto = listCustom.get(position);


        if (mode.equals("attendance")) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("찾을 스케쥴 번호 : ", "" + dto.getRsch_idx());
                    Intent intent = new Intent(context, AttendanceActivity.class);
                    intent.putExtra("rsch_idx", dto.getRsch_idx());
                    context.startActivity(intent);
                }
            });
        }

        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        SimpleDateFormat yyyyFormat = new SimpleDateFormat("yyyy년");
        SimpleDateFormat mmddFormat = new SimpleDateFormat("MM월 dd일");

        Date date = null;
        try {
            date = oldDateFormat.parse(dto.getRsch_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String yyyy = yyyyFormat.format(date);
        String mmdd = mmddFormat.format(date);

        holder.text_rsch_name.setText(dto.getRsch_name());
        holder.text_sr_name.setText(dto.getSr_name());
        holder.text_sr_location.setText(dto.getSr_location());
        holder.text_date_yyyy.setText(yyyy);
        holder.text_date_mmdd.setText(mmdd);

        return convertView;
    }

    class CustomViewHolder {
        TextView text_rsch_name;
        TextView text_sr_name;
        TextView text_sr_location;
        TextView text_date_yyyy;
        TextView text_date_mmdd;
    }

    // MainActivity에서 Adapter에있는 ArrayList에 data를 추가시켜주는 함수
    public void addItem(CustomScheduleDTO dto) {
        listCustom.add(dto);
    }
}
