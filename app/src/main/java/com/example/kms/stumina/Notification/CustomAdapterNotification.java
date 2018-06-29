package com.example.kms.stumina.Notification;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kms.stumina.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapterNotification extends BaseAdapter {

    private ArrayList<NotificationDTO> listCustom = new ArrayList<>();
    private Context context;

    public CustomAdapterNotification(Context context) {
        this.context = context;
    }

    // ListView에 보여질 Item 수
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
        CustomAdapterNotification.CustomViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_notification, null, false);

            holder = new CustomAdapterNotification.CustomViewHolder();
            holder.text_date_yyyy = (TextView) convertView.findViewById(R.id.text_notidate_yyyy);
            holder.text_date_mmdd = (TextView) convertView.findViewById(R.id.text_notidate_mmdd);
            holder.text_noti_time = (TextView) convertView.findViewById(R.id.text_noti_time);
            holder.text_noti_text = (TextView) convertView.findViewById(R.id.text_noti_text);

            convertView.setTag(holder);

            Log.d("테스트","뭐지?");

        } else {
            holder = (CustomAdapterNotification.CustomViewHolder) convertView.getTag();
        }

        final NotificationDTO dto = listCustom.get(position);

        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        SimpleDateFormat yyyyFormat = new SimpleDateFormat("yyyy년");
        SimpleDateFormat mmddFormat = new SimpleDateFormat("MM월 dd일");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh시 mm분 ss초");

        Date date = null;
        try {
            date = oldDateFormat.parse(dto.getNoti_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String yyyy = yyyyFormat.format(date);
        String mmdd = mmddFormat.format(date);
        String time = timeFormat.format(date);

        Log.d("테스트",dto.getNoti_text());

        holder.text_noti_text.setText(dto.getNoti_text());
        holder.text_date_yyyy.setText(yyyy);
        holder.text_date_mmdd.setText(mmdd);
        holder.text_noti_time.setText(time);

        return convertView;
    }

    class CustomViewHolder {
        TextView text_noti_text;
        TextView text_noti_time;
        TextView text_date_yyyy;
        TextView text_date_mmdd;
    }

    // MainActivity에서 Adapter에있는 ArrayList에 data를 추가시켜주는 함수
    public void addItem(NotificationDTO dto) {
        listCustom.add(dto);
    }
}
