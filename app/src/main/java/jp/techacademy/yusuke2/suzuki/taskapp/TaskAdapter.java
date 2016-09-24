package jp.techacademy.yusuke2.suzuki.taskapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TaskAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private ArrayList<Task> mTaskArrayList;

    public TaskAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setTaskArrayList(ArrayList<Task> taskArrayList) {
        mTaskArrayList = taskArrayList;
    }

    //アイテム（データ）の数を返す
    @Override
    public int getCount() {
        return mTaskArrayList.size();
    }

    //アイテム（データ）を返す
    @Override
    public Object getItem(int position) {
        return mTaskArrayList.get(position);
    }

    //アイテム（データ）のIDを返す
    @Override
    public long getItemId(int position) {
        return mTaskArrayList.get(position).getId();
    }

    //Viewを返す baseadapterの継承
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(android.R.layout.simple_list_item_2, null);
        }

        TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
        TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);
        //カテゴリの追加
//        TextView textView3 = (TextView) convertView.findViewById(android.R.id.text3);

        textView1.setText(mTaskArrayList.get(position).getTitle());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPANESE);
        Date date = mTaskArrayList.get(position).getDate();
        textView2.setText(simpleDateFormat.format(date));
//        textView3.setText(simpleDateFormat.format(date));

        return convertView;
    }
}