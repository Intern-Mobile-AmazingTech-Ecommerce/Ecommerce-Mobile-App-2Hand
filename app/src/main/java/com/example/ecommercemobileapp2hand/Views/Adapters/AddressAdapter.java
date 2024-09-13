package com.example.ecommercemobileapp2hand.Views.Adapters;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SymbolTable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Controllers.WishlistHandler;
import com.example.ecommercemobileapp2hand.Models.UserAddress;
import com.example.ecommercemobileapp2hand.Models.Wishlist;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Settings.UpdateAdressActivity;
import com.example.ecommercemobileapp2hand.Views.Settings.WishlistDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AdressViewHolder>
{
    private ArrayList<UserAddress> userAddressList ;
    private Context context;

    public AddressAdapter(ArrayList<UserAddress> userAddressList, Context context) {
        this.userAddressList = userAddressList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address,parent,false);
        return new AdressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdressViewHolder holder, int position) {
        UserAddress address = userAddressList.get(position);
        holder.textViewAdress.setText(address.getUser_address_street());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateAdressActivity.class) ;
                intent.putExtra("id",address.getUser_address_id());
                intent.putExtra("street",address.getUser_address_street());
                intent.putExtra("city",address.getUser_address_city());
                intent.putExtra("phone",address.getUser_address_phone());
                intent.putExtra("state",address.getUser_address_state());
                intent.putExtra("zip",address.getUser_address_zipcode());;
                System.out.println("iddddd"+address.getUser_address_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userAddressList != null ? userAddressList.size() : 0;
    }

    public static class AdressViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewAdress;
        public AdressViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAdress = itemView.findViewById(R.id.textViewAddress);
        }
    }

}
