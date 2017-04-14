package com.example.liaohaicongsx.lhc_refreshlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by liaohaicongsx on 2017/04/14.
 */
public class lvAdapter extends BaseAdapter {

    private final int size = 25;
    private String [] str = new String[size];
    private Context context;


    public lvAdapter(Context context){
        this.context = context;
        for(int i = 0;i<size;i++){
            str[i] = String.valueOf(i+1);
        }
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return str[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHodler holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_layout,null);
            holder = new MyViewHodler(convertView);
            convertView.setTag(holder);
        }else{
            holder = (MyViewHodler) convertView.getTag();
        }
        holder.textView.setText(str[position]);
        return convertView;
    }

    class MyViewHodler{
        TextView textView;

        MyViewHodler(View itemView){
            textView  = (TextView) itemView.findViewById(R.id.tv_item_num);
        }
    }

}
