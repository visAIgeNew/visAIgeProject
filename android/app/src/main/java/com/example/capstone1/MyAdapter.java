package com.example.capstone1;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintSet;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private ArrayList<MyItem> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public MyItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        Context context = parent.getContext();
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.log_item_layout, parent, false);
        }

        TextView tvUserId = (TextView) view.findViewById(R.id.tv_item);
        TextView tvDate = (TextView) view.findViewById(R.id.tv_date);

        MyItem myItem = getItem(i);

        tvUserId.setText(myItem.getLog_user_id());
        tvDate.setText(myItem.getLog_date());

        return view;
    }

    public void addItem(String userId, String log_date) {
        MyItem mItem = new MyItem();

        mItem.setLog_user_id(userId);
        mItem.setLog_date(log_date);

        mItems.add(mItem);
    }

    public void deleteItem() {
        mItems.clear();
    }
}
