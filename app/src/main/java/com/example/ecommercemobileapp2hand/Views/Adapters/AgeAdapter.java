package com.example.ecommercemobileapp2hand.Views.Login.Onboarding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecommercemobileapp2hand.R;

import java.util.List;

public class AgeAdapter extends ArrayAdapter<Age> {
    public AgeAdapter(@NonNull Context context, int resource, @NonNull List<Age> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.age_selected,parent,false);
        TextView tvSelec=convertView.findViewById(R.id.tv_selected);

        Age category=this.getItem(position);
        if( category !=null)
        {
            tvSelec.setText(category.getAge());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.age_category,parent,false);
        TextView tvCate=convertView.findViewById(R.id.tv_category);

        Age category=this.getItem(position);
        if( category !=null)
        {
            tvCate.setText(category.getAge());
        }
        return convertView;
    }
}
