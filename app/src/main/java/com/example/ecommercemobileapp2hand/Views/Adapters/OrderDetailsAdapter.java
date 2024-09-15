package com.example.ecommercemobileapp2hand.Views.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Controllers.ProductReviewHandler;
import com.example.ecommercemobileapp2hand.Controllers.UserOrderProductsHandler;
import com.example.ecommercemobileapp2hand.Models.ProductReview;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.UserOrderProducts;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.ProductPage.ProductPage;
import com.example.ecommercemobileapp2hand.Views.Utils.Util;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {
    private Future<?> currentTask;
    ArrayList<UserOrderProducts> lstOrderDetails;
    Context context;
    boolean checkDeliverd;

    public OrderDetailsAdapter(ArrayList<UserOrderProducts> lstOrderDetails, Context context, boolean checkDeliverd) {
        this.lstOrderDetails = lstOrderDetails;
        this.context = context;
        this.checkDeliverd = checkDeliverd;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewholder = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_orderdetails, parent, false);
        return new OrderDetailsAdapter.MyViewHolder(viewholder);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserOrderProducts details = lstOrderDetails.get(position);
        Util.getCloudinaryImageUrl(context, details.getThumbnail(), -1, -1, new Util.Callback<String>() {
            @Override
            public void onResult(String result) {
                String url = result;
                ((android.app.Activity) context).runOnUiThread(() -> {
                    Picasso.get().load(url).into(holder.imgPro);
                });
            }
        });
        holder.tv_ProductName.setText(details.getProduct_name());
        holder.tv_SizeColor.setText(details.getProduct_color_name() + ", Size " + details.getSize_name());

        holder.tvAmount.setText("x" + String.valueOf(details.getAmount()));
        if (details.getSale_price() != null && details.getSale_price().compareTo(BigDecimal.ZERO) > 0) {
            holder.tv_BasePrice.setText("$" + String.valueOf(details.getBase_price()));
            holder.tv_SalePrice.setText("$" + String.valueOf(details.getSale_price()));

            // Gạch ngang giá gốc
            holder.tv_BasePrice.setPaintFlags(holder.tv_BasePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tv_BasePrice.setVisibility(View.GONE);
            holder.tv_SalePrice.setText("$" + String.valueOf(details.getSale_price()));
        }
        BigDecimal totalPrice = details.getSale_price().multiply(BigDecimal.valueOf(details.getAmount()));
        holder.tvTotalPricePro.setText("Subtotal: $" + totalPrice);

        if (checkDeliverd) {
            if (details.isReviewed() == true) {
                holder.btnReview.setText("Reviewed");
                holder.btnReview.setEnabled(false);
            }
            else
            {
                holder.btnReview.setVisibility(View.VISIBLE);
                holder.btnReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserOrderProductsHandler.getProductDetailsID(details.getProduct_id(), details.getProduct_color_name(), details.getSize_name(), new UserOrderProductsHandler.Callback<Integer>() {
                            @Override
                            public void onResult(Integer result) {
                                ((android.app.Activity) context).runOnUiThread(() -> {
                                    showReviewOverlay(result, details.getUser_order_id());
                                });
                            }
                        });

                    }
                });
            }
        } else {
            holder.btnReview.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);
        if (currentTask != null) {
            currentTask.cancel(true);
        }
    }

    @Override
    public int getItemCount() {
        return lstOrderDetails != null ? lstOrderDetails.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPro;
        private TextView tv_ProductName;
        private TextView tv_SizeColor;
        private TextView tvAmount;
        private TextView tv_BasePrice;
        private TextView tv_SalePrice;
        private TextView tvTotalPricePro;
        private Button btnReview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgPro = itemView.findViewById(R.id.imgPro);
            this.tv_ProductName = itemView.findViewById(R.id.tv_ProductName);
            this.tv_SizeColor = itemView.findViewById(R.id.tv_SizeColor);
            this.tvAmount = itemView.findViewById(R.id.tvAmount);
            this.tv_BasePrice = itemView.findViewById(R.id.tv_BasePrice);
            this.tv_SalePrice = itemView.findViewById(R.id.tv_SalePrice);
            this.tvTotalPricePro = itemView.findViewById(R.id.tvTotalPricePro);
            this.btnReview = itemView.findViewById(R.id.btnReview);
        }
    }

    private void showReviewOverlay(int product_detail_id, int user_order_id) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_overlay, null);

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowattri = window.getAttributes();
        windowattri.gravity = Gravity.BOTTOM;
        window.setAttributes(windowattri);

        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        EditText edt_review = dialog.findViewById(R.id.edt_review);

        ImageButton btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserAccount userAccount = UserAccountManager.getInstance().getCurrentUserAccount();
                if (!edt_review.getText().toString().isEmpty() && ratingBar.getRating() != 0) {
                    LocalDateTime now = LocalDateTime.now();
                    ProductReview review = new ProductReview(-1, userAccount.getUserId(), product_detail_id, edt_review.getText().toString(), now, (int) ratingBar.getRating());
                    ProductReviewHandler.insertReview(review, new ProductReviewHandler.Callback<Boolean>() {
                        @Override
                        public void onResult(Boolean result) {
                            ((android.app.Activity) context).runOnUiThread(() -> {
                                if (result) {
                                    UserOrderProductsHandler.updateIsReviewed(user_order_id, product_detail_id, true, new UserOrderProductsHandler.Callback<Boolean>() {
                                        @Override
                                        public void onResult(Boolean result) {
                                            if (result) {
                                                ((android.app.Activity)context).runOnUiThread(() -> {
                                                    Toast.makeText(context, "Review submitted", Toast.LENGTH_SHORT).show();
                                                    notifyDataSetChanged();
                                                });
                                            } else {
                                                ((android.app.Activity)context).runOnUiThread(() -> {
                                                    Toast.makeText(context, "Failed to submit review", Toast.LENGTH_SHORT).show();
                                                });
                                            }
                                        }
                                    });
                                } else
                                {
                                    Toast.makeText(context, "Failed to submit review", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                    dialog.dismiss();
                } else {
                    if (edt_review.getText().toString().isEmpty()) {
                        edt_review.setError("Review is required");
                    }
                    if (ratingBar.getRating() == 0) {
                        Toast.makeText(context, "Please provide a rating", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
