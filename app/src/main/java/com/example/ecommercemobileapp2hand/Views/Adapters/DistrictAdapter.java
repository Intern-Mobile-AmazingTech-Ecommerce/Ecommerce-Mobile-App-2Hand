package com.example.ecommercemobileapp2hand.Views.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ecommercemobileapp2hand.Models.District;

import java.util.List;

public class DistrictAdapter extends ArrayAdapter<District> {
    public DistrictAdapter(Context context, List<District> districts) {
        super(context, 0, districts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        TextView textView = convertView.findViewById(android.R.id.text1);
        District district = getItem(position);
        if (district != null) {
            textView.setText(district.getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
