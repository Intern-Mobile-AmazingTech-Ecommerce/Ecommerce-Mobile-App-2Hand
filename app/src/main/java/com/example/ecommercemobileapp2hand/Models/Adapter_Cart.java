package com.example.ecommercemobileapp2hand.Models;

import android.app.Activity;
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

public class Adapter_Cart extends ArrayAdapter<Cart> {
    Activity context;
    int IdLayout;
    ArrayList<Cart> myList;

    public Adapter_Cart(Activity context, int idLayout,ArrayList<Cart> myList) {
        super(context,idLayout, myList);
        this.context = context;
        IdLayout = idLayout;
        this.myList = myList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //
        LayoutInflater myflacter = context.getLayoutInflater();
        //
        convertView = myflacter.inflate(IdLayout, null);
        //
        Cart myCart = myList.get(position);
        //
        ImageView imgCart = convertView.findViewById(R.id.img_cart);
        imgCart.setImageResource(myCart.getImage());
        //
        TextView txt_NameCart = convertView.findViewById(R.id.name_cart);
        txt_NameCart.setText(myCart.getName());
        //
        TextView txt_SizeCart = convertView.findViewById(R.id.size_cart);
        txt_SizeCart.setText(myCart.getSize());
        //
        TextView txt_ColorCart = convertView.findViewById(R.id.color_cart);
        txt_ColorCart.setText(myCart.getColor());
        //
        TextView txt_PriceCart = convertView.findViewById(R.id.price_cart);
        txt_PriceCart.setText(myCart.getPrice());

        return convertView;
    }
}
