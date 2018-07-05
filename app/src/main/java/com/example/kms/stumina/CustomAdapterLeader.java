package com.example.kms.stumina;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kms.stumina.schedule.SchedulesActivity;

import java.util.ArrayList;

import static com.example.kms.stumina.MainActivity.imagesID;

public class CustomAdapterLeader extends BaseAdapter {

    private ArrayList<CustomDTO> listCustom = new ArrayList<>();
    private Context context;

    public CustomAdapterLeader(Context context) {
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
        CustomViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_leader, null, false);

            holder = new CustomViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imagesID.add(holder.imageView);

            holder.textTitle = (TextView) convertView.findViewById(R.id.text_title);
            holder.textContent = (TextView) convertView.findViewById(R.id.text_content);

            convertView.setTag(holder);
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        final CustomDTO dto = listCustom.get(position);

        // 해당 리스트 클릭 시 상세정보로 이동하기 위해 onClickListener 등록
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("클릭 테스트", dto.getStd_no());
                Intent intent=new Intent(context, SchedulesActivity.class);
/*
                Bundle bundle = new Bundle();
                bundle.putString("text",String.valueOf(dto.getStd_no()));
                bundle.putString("mode","attendance");
                intent.putExtra("bundle", bundle);*/

                String[] datas = new String[]{String.valueOf(dto.getStd_no()), "attendance"};
                //intent.putExtra("text",String.valueOf(dto.getStd_no()));
                intent.putExtra("datas",datas);
                context.startActivity(intent);
            }
        });

        //holder.imageView.setImageResource(dto.getResId());
        //holder.imageView.setImageBitmap(dto.getImage());
        holder.textTitle.setText(dto.getTitle());
        holder.textContent.setText(dto.getContent());

        return convertView;
    }

    class CustomViewHolder {
        ImageView imageView;
        TextView textTitle;
        TextView textContent;
    }

    // MainActivity에서 Adapter에있는 ArrayList에 data를 추가시켜주는 함수
    public void addItem(CustomDTO dto) {
        listCustom.add(dto);
    }
}