package com.example.ecommercemobileapp2hand.Views.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.Models.ProductDetails;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.ProductPage.ProductPage;
import com.example.ecommercemobileapp2hand.Views.Utils.ProductDiffCallBack;
import com.example.ecommercemobileapp2hand.Views.Utils.Util;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

public class ProductCardAdapter extends RecyclerView.Adapter<ProductCardAdapter.MyViewHolder> {
    ArrayList<Product> lstPro;
    Context context;

    public ProductCardAdapter(ArrayList<Product> lstPro, Context context) {
        this.lstPro = lstPro;
        this.context = context;
    }
    public void setFilteredList(ArrayList<Product> newProductList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ProductDiffCallBack(this.lstPro,newProductList));
        this.lstPro.clear();
        this.lstPro.addAll(newProductList);
        diffResult.dispatchUpdatesTo(this);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewholder = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_productcard, parent, false);
        return new MyViewHolder(viewholder);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product pro = lstPro.get(position);
        ProductDetails details = new ProductDetails();
        Optional<ProductDetails> firstSaleDetails = pro.getProductDetailsArrayList().stream()
                .filter(detail -> detail.getSale_price() != null && detail.getSale_price().compareTo(BigDecimal.ZERO) > 0)
                .findFirst();

        String url = "";
        holder.tvProductName.setText(pro.getProduct_name());
        holder.tvSalePrice.setVisibility(View.VISIBLE);
        if(firstSaleDetails.isPresent()){
            details = firstSaleDetails.get();
            Log.d("IMG_URL DETAILS"+details.getProduct_details_id(),details.getImgDetailsArrayList().get(0).getImg_url());
            url = Util.getCloudinaryImageUrl(context,details.getImgDetailsArrayList().get(0).getImg_url(),159,220);
            holder.tvSalePrice.setText("$" + String.valueOf(details.getSale_price()));
            holder.tvPrice.setText("$" + String.valueOf(pro.getBase_price()));
            // Gạch ngang giá gốc
            holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            url = Util.getCloudinaryImageUrl(context, pro.getThumbnail(),159,220);
            holder.tvPrice.setVisibility(View.GONE);
            holder.tvSalePrice.setText("$" + String.valueOf(pro.getBase_price()));
        }

        Picasso.get().load(url).into(holder.img_Product);


        ProductDetails finalDetails = details;
        holder.productCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductPage.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("lstDetails",pro);
                if(firstSaleDetails.isPresent()){
                    bundle.putParcelable("currentSale", finalDetails);
                }
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstPro.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private CardView productCard;
        private ImageView img_Product;
        private TextView tvProductName, tvSalePrice, tvPrice;
        private MaterialButton img_Heart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.productCard = itemView.findViewById(R.id.cardProduct);
            this.img_Product = itemView.findViewById(R.id.img_Product);
            this.img_Heart = itemView.findViewById(R.id.img_Heart);
            this.tvProductName = itemView.findViewById(R.id.tvProductName);
            this.tvSalePrice = itemView.findViewById(R.id.tvSalePrice);
            this.tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
