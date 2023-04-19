package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class CustomAdapterList extends BaseAdapter {
    Context context;
    ListView countryList;
    ListView flags;
    LayoutInflater inflter;

    public CustomAdapterList(Context applicationContext, ListView countryList, ListView flags) {
        this.context = context;
        this.countryList = countryList;
        this.flags = flags;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryList.getNumbersImageId();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_listof_food, null);
        TextView food = (TextView)  view.findViewById(R.id.title);
        TextView kcal = (TextView) view.findViewById(R.id.subtitle);
        ImageView icon = (ImageView) view.findViewById(R.id.ListImage);
        food.setText((CharSequence) countryList);
        icon.setImageResource(i);
        return view;
    }
}