package com.example.ecommercemobileapp2hand.Views.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ecommercemobileapp2hand.Models.Province;

import java.util.List;
public class ProvinceAdapter extends ArrayAdapter<Province> {

    public ProvinceAdapter(Context context, List<Province> provinces) {
        super(context, 0, provinces);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Province province = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        // Lookup view for data population
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        // Populate the data into the template view using the data object
        textView.setText(province.getName());
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Province province = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        // Lookup view for data population
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        // Populate the data into the template view using the data object
        textView.setText(province.getName());
        // Return the completed view to render on screen
        return convertView;
    }
}