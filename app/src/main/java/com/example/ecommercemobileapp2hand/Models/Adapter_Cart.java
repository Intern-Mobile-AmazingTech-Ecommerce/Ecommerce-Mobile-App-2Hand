package com.example.ecommercemobileapp2hand.Models;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecommercemobileapp2hand.R;

import java.util.ArrayList;

public class Adapter_Cart extends ArrayAdapter<Cartt> {
    Activity context;
    int IdLayout;
    ArrayList<Cartt> myList;
    //Tạo Constructor để gọi và truyền tham so

    public Adapter_Cart(Activity context, int idLayout,ArrayList<Cartt> myList) {
        super(context,idLayout, myList);
        this.context = context;
        IdLayout = idLayout;
        this.myList = myList;
    }
    //getW

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //
        LayoutInflater myflacter = context.getLayoutInflater();
        //
        convertView = myflacter.inflate(IdLayout, null);
        //
        Cartt myCart = myList.get(position);
        //
        ImageView imgCart = convertView.findViewById(R.id.imgCart);
        imgCart.setImageResource(myCart.getImage());
        //
        TextView txt_NameCart = convertView.findViewById(R.id.txt_NameCart);
        txt_NameCart.setText(myCart.getName());

        TextView txt_SizeCart = convertView.findViewById(R.id.txt_SizeCart);
        txt_SizeCart.setText(myCart.getSize());

        TextView txt_PriceCart = convertView.findViewById(R.id.txt_PriceCart);
        txt_PriceCart.setText(myCart.getPrice());

        TextView txt_ColerCart = convertView.findViewById(R.id.txt_ColorCart);
        txt_ColerCart.setText(myCart.getColor());
        return convertView;
    }
}
