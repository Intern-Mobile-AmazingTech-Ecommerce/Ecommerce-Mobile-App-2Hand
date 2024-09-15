package com.example.ecommercemobileapp2hand.Views.Adapters;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SymbolTable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
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
    private int selectedPosition = -1;
    private ArrayList<UserAddress> userAddressList ;
    private Context context;
    private int layout;
    private int userAddressID=-1;

    public AddressAdapter(ArrayList<UserAddress> userAddressList, Context context,int layout) {
        this.userAddressList = userAddressList;
        this.context = context;
        this.layout =layout;
    }

    @NonNull
    @Override
    public AdressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new AdressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdressViewHolder holder,int position) {
        UserAddress address = userAddressList.get(position);
        holder.textViewAdress.setText(address.getStringAddress());
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
                context.startActivity(intent);
            }
        });
        if (layout==R.layout.custom_item_choose_address){
            if(position == selectedPosition){
                holder.rdbtnChoose.setText("Active");
            }
            else{
                holder.rdbtnChoose.setText("Inactive");
            }
            holder.rdbtnChoose.setChecked(position== selectedPosition);
            holder.rdbtnChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.rdbtnChoose.setTag("Active");
                    selectedPosition = holder.getAdapterPosition();
                    notifyDataSetChanged();
                    userAddressID=address.getUser_address_id();

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userAddressList != null ? userAddressList.size() : 0;
    }

    public static class AdressViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewAdress;
        RadioButton rdbtnChoose;
        public AdressViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAdress = itemView.findViewById(R.id.textViewAddress);
            rdbtnChoose=itemView.findViewById(R.id.rdbtnChoose);
        }
    }
    public int getSelectedUserAddressID(){
        return userAddressID;
    }

}
