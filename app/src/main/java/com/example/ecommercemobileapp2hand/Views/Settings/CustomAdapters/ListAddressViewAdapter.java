package com.example.ecommercemobileapp2hand.Views.Settings.CustomAdapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Settings.AddAddressActivity;

import java.util.ArrayList;

public class ListAddressViewAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<String> listAddress=new ArrayList<>();
    private int resourceLayout;
    public ListAddressViewAdapter(Context context,int resourceLayout,ArrayList<String> listAddress){
        super(context,resourceLayout,listAddress);
        this.context=context;
        this.resourceLayout=resourceLayout;
        this.listAddress=listAddress;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String address=listAddress.get(position);
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(resourceLayout,null);
        }
        TextView tvAddress=convertView.findViewById(R.id.txtAddress);
        tvAddress.setText(address);
        TextView tvEdit=convertView.findViewById(R.id.tvEdit);
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(getContext(), AddAddressActivity.class));
            }
        });
        return convertView;
    }


}
