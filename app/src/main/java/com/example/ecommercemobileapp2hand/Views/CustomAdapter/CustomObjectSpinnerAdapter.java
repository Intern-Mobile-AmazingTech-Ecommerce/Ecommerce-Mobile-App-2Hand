package com.example.ecommercemobileapp2hand.Views.CustomAdapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ecommercemobileapp2hand.R;

import java.util.ArrayList;
import java.util.List;

public class CustomObjectSpinnerAdapter extends BaseAdapter {
    ArrayList<String> listObj;

    public CustomObjectSpinnerAdapter(ArrayList<String> listObj) {
        this.listObj = listObj;
    }

    @Override
    public int getCount() {
        return listObj.size();
    }

    @Override
    public Object getItem(int position) {
        return listObj.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = View.inflate(parent.getContext(), R.layout.custom_spinner_object,null);
        }
        String objectName = listObj.get(position);
        TextView tvObjectName = convertView.findViewById(R.id.tvObjectName);
        tvObjectName.setText(objectName);
        return convertView;
    }
}
