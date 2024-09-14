package com.example.ecommercemobileapp2hand.Views.Adapters;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommercemobileapp2hand.Controllers.ProductHandler;
import com.example.ecommercemobileapp2hand.Controllers.WishlistHandler;
import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.Models.ProductDetails;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.Wishlist;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Homepage.HomeFragment;
import com.example.ecommercemobileapp2hand.Views.ProductPage.ProductPage;
import com.example.ecommercemobileapp2hand.Views.Utils.ProductDiffCallBack;
import com.example.ecommercemobileapp2hand.Views.Utils.Util;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProductCardAdapter extends RecyclerView.Adapter<ProductCardAdapter.MyViewHolder> {
    ArrayList<Product> lstPro;
    Context context;
    ArrayList<ProductDetails> lstDetails;
    private FavoriteClickedListener listener;
    private ExecutorService service;
    private int currentWishListID =-1;
    public ProductCardAdapter(ArrayList<Product> lstPro, Context context) {
        this.lstPro = lstPro;
        this.context = context;
    }
    public ProductCardAdapter(ArrayList<Product> lstPro, Context context, FavoriteClickedListener listener) {
        this.lstPro = lstPro;
        this.context = context;
        this.listener = listener;
    }
    public ProductCardAdapter(ArrayList<Product> lstPro, Context context, FavoriteClickedListener listener, int currentWishListID) {
        this.lstPro = lstPro;
        this.context = context;
        this.listener = listener;
        this.currentWishListID = currentWishListID;
    }

    public void setFilteredList(ArrayList<Product> newProductList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ProductDiffCallBack(this.lstPro, newProductList));
        this.lstPro.clear();
        this.lstPro.addAll(newProductList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateProduct(int position, Product updatedProduct) {
        lstPro.set(position, updatedProduct);
        notifyItemChanged(position);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewholder = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_productcard, parent, false);
        return new MyViewHolder(viewholder);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(service == null || service.isShutdown()){
            service = Executors.newCachedThreadPool();
        }
        Product pro = lstPro.get(position);
        //First Sale Details
        ProductDetails details = new ProductDetails();
        Optional<ProductDetails> firstSaleDetails = pro.getProductDetailsArrayList().stream()
                .filter(detail -> detail.getSale_price() != null && detail.getSale_price().compareTo(BigDecimal.ZERO) > 0)
                .findFirst();
        String url = "";


        if (firstSaleDetails.isPresent()) {
            details = firstSaleDetails.get();
            Util.getCloudinaryImageUrl(context, details.getImgDetailsArrayList().get(0).getImg_url(), 159, 220, new Util.Callback<String>() {
                @Override
                public void onResult(String result) {
                    String imgUrl = result;
                    ((android.app.Activity)context).runOnUiThread(()->{
                        Glide.with(context).load(imgUrl).override(159,220).into(holder.img_Product);
                    });
                }
            });
        } else {
            Util.getCloudinaryImageUrl(context, pro.getThumbnail(), 159, 220, new Util.Callback<String>() {
                @Override
                public void onResult(String result) {
                    String imgUrl = result;
                    ((android.app.Activity)context).runOnUiThread(()->{
                        Glide.with(context).load(imgUrl).override(159,220).into(holder.img_Product);
                    });
                }
            });
        }
        ProductDetails finalDetails = details;
        holder.tvProductName.setText(pro.getProduct_name());
        holder.tvSalePrice.setVisibility(View.VISIBLE);

        if (firstSaleDetails.isPresent()) {
            Log.d("IMG_URL DETAILS" + finalDetails.getProduct_details_id(), finalDetails.getImgDetailsArrayList().get(0).getImg_url());
            holder.tvSalePrice.setText("$" + String.valueOf(finalDetails.getSale_price()));
            holder.tvPrice.setText("$" + String.valueOf(pro.getBase_price()));
            // Gạch ngang giá gốc
            holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvPrice.setVisibility(View.GONE);
            holder.tvSalePrice.setText("$" + String.valueOf(pro.getBase_price()));
        }


        holder.productCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductPage.class);
                Bundle bundle = new Bundle();

                if(currentWishListID != -1){
                   ProductHandler.getDataByProductID(pro.getProduct_id(), new ProductHandler.Callback<Product>() {
                        @Override
                        public void onResult(Product result) {
                            Product ProDetails = result;
                            ((android.app.Activity)context).runOnUiThread(()->{
                                bundle.putParcelable("lstDetails", ProDetails);
                                if (firstSaleDetails.isPresent()) {
                                    bundle.putParcelable("currentSale", finalDetails);
                                }
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            });
                        }
                    });


                }else {
                    bundle.putParcelable("lstDetails", pro);
                    if (firstSaleDetails.isPresent()) {
                        bundle.putParcelable("currentSale", finalDetails);
                    }
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }


            }
        });
        boolean result = WishlistHandler.checkProductDetailsExistsInWishlistByUserID(finalDetails.getProduct_details_id(), UserAccountManager.getInstance().getCurrentUserAccount().getUserId());
        if(result){
            holder.img_Heart.setIconResource(R.drawable.red_heart);
            holder.img_Heart.setIconTint(ColorStateList.valueOf(Color.RED));
        }else {
            holder.img_Heart.setIconResource(R.drawable.black_heart);
            holder.img_Heart.setIconTint(ColorStateList.valueOf(Color.BLACK));
        }
        if(currentWishListID != -1){
            holder.img_Heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(result){
                        service.submit(()->{
                            boolean isRemoved = WishlistHandler.removeFromWishlist(currentWishListID,finalDetails.getProduct_details_id());
                            ((android.app.Activity)context).runOnUiThread(()->{
                                if(isRemoved){
                                    notifyDataSetChanged();
                                    Toast.makeText(context,"Product Removed From Wishlist Successfully",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(context,"Product Removed From Wishlist Failed",Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                        service.shutdown();
                    }else {
                        service.submit(()->{
                            boolean isInserted = WishlistHandler.insertToWishlist(currentWishListID,finalDetails.getProduct_details_id());
                            ((android.app.Activity)context).runOnUiThread(()->{
                                if(isInserted){
                                    notifyDataSetChanged();
                                    Toast.makeText(context,"Product inserted into Wishlist Successfully",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(context,"Product inserted into Wishlist Failed",Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                        service.shutdown();
                    }

                }
            });
        }else {
            holder.img_Heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddToWLOverlay(finalDetails,holder.getAdapterPosition());
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return lstPro != null ? lstPro.size():0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
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

    private void showAddToWLOverlay(ProductDetails currentDetails,int position) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.wishlist_overlay, null);
        bottomSheetDialog.setContentView(dialogView);

        View parentLayout = (View) dialogView.getParent();
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parentLayout);
        behavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO, true);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        TextView tv_cancel;
        Button btnNewWL, btnDone;
        RecyclerView recyWL;

        tv_cancel = dialogView.findViewById(R.id.tv_cancel);
        btnNewWL = dialogView.findViewById(R.id.btnNewWL);
        btnDone = dialogView.findViewById(R.id.btnDone);
        recyWL = dialogView.findViewById(R.id.recy_wl);
        ExecutorService loadingWishlist = Executors.newCachedThreadPool();
        loadingWishlist.execute(()->{
            ArrayList<Wishlist> wishlists = WishlistHandler.getWishListByUserID(UserAccountManager.getInstance().getCurrentUserAccount().getUserId());
            ((android.app.Activity)context).runOnUiThread(()->{
                WishListAdapter wishListAdapter = new WishListAdapter(context, wishlists, currentDetails.getProduct_details_id());
                recyWL.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
                recyWL.setAdapter(wishListAdapter);
            });
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                loadingWishlist.submit(()->{
//                    boolean result = WishlistHandler.checkProductDetailsExistsInWishlistByUserID(currentDetails.getProduct_details_id(), UserAccountManager.getInstance().getCurrentUserAccount().getUserId());
                    ((android.app.Activity)context).runOnUiThread(()->{
                        if(listener != null){
                            listener.onDoneClicked();
                        }

                    });
                    Product updatedProduct = lstPro.get(position);

                    updateProduct(position, updatedProduct);


                });

            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingWishlist.shutdown();
                bottomSheetDialog.dismiss();
            }
        });
        btnNewWL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddWLOverlay(()->{
                    loadingWishlist.execute(()->{
                        ArrayList<Wishlist> updatedWishlists = WishlistHandler.getWishListByUserID(UserAccountManager.getInstance().getCurrentUserAccount().getUserId());
                        ((android.app.Activity)context).runOnUiThread(() -> {
                            WishListAdapter updatedWishListAdapter = new WishListAdapter(context, updatedWishlists, currentDetails.getProduct_details_id() );
                            recyWL.setAdapter(updatedWishListAdapter);
                        });
                    });
                });
            }
        });
        bottomSheetDialog.show();

    }

    private void showAddWLOverlay(Runnable onDismissCallback) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.addwishlist_overlay, null);
        bottomSheetDialog.setContentView(dialogView);

        ImageButton btn_close;
        EditText edtNameWL;
        Button btnCreate;

        btn_close = dialogView.findViewById(R.id.btn_close);
        edtNameWL = dialogView.findViewById(R.id.edtNameWL);
        btnCreate = dialogView.findViewById(R.id.btnCreate);
        ExecutorService insertWishlist = Executors.newSingleThreadExecutor();
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtNameWL.getText().length() > 0){

                    insertWishlist.execute(()->{
                        boolean result = WishlistHandler.addNewWishlist(UserAccountManager.getInstance().getCurrentUserAccount().getUserId(),edtNameWL.getText().toString());
                        ((android.app.Activity)context).runOnUiThread(()->{
                            if(result){
                                Toast.makeText(context,"New Wishlist added Successfully",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(context,"New Wishlist added Failed",Toast.LENGTH_SHORT).show();
                            }
                            if (onDismissCallback != null) {
                                onDismissCallback.run();
                            }
                        });
                    });

                }else {
                    if (onDismissCallback != null) {
                        onDismissCallback.run();
                    }
                }
                insertWishlist.shutdown();
                bottomSheetDialog.dismiss();

            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertWishlist.shutdown();
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }
    public interface FavoriteClickedListener{
        void onDoneClicked();
    }
}
