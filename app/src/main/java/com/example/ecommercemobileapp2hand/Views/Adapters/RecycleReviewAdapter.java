package com.example.ecommercemobileapp2hand.Views.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommercemobileapp2hand.Controllers.UserAccountHandler;
import com.example.ecommercemobileapp2hand.Models.FakeModels.Reviews;
import com.example.ecommercemobileapp2hand.Models.ProductReview;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Utils.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecycleReviewAdapter extends RecyclerView.Adapter<RecycleReviewAdapter.MyViewHolder> {

    private ArrayList<ProductReview> reviewsList;
    private Context context;
    private ExecutorService service = Executors.newCachedThreadPool();

    public RecycleReviewAdapter(ArrayList<ProductReview> reviewsList, Context context) {
        this.context = context;
        if (reviewsList == null) {
            this.reviewsList = new ArrayList<>();
        } else {
            this.reviewsList = reviewsList;
        }

        Collections.sort(this.reviewsList, new Comparator<ProductReview>() {
            @Override
            public int compare(ProductReview review1, ProductReview review2) {
                return review2.getCreated_at().compareTo(review1.getCreated_at());
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_review, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductReview reviews = reviewsList.get(position);

        UserAccount userAccount = UserAccountManager.getInstance().getCurrentUserAccount();
        if (userAccount.getImgUrl() != null) {
            Util.getCloudinaryImageUrl(context, userAccount.getImgUrl(), -1, -1, new Util.Callback<String>() {
                @Override
                public void onResult(String result) {
                    String imgUrl = result;
                    ((android.app.Activity) context).runOnUiThread(() -> {
                        Glide.with(context).load(imgUrl).into(holder.imgAvt);
                    });
                }
            });

        } else {
            Bitmap bitmap = Util.convertStringToBitmapFromAccess(context.getApplicationContext(), "avt.png");
            holder.imgAvt.setImageBitmap(bitmap);
        }
        holder.tvCustomerName.setText(userAccount.getFullName());
        holder.tvDays.setText(getDayDifference(reviews.getCreated_at()));
        holder.ratingBar.setRating(reviews.getRating());
        holder.tvReviewContent.setText(reviews.getReview_content());


    }

    public static String getDayDifference(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();

        long daysBetween = ChronoUnit.DAYS.between(date, now);
        long hoursBetween = ChronoUnit.HOURS.between(date, now);
        long minutesBetween = ChronoUnit.MINUTES.between(date, now);

        if (daysBetween == 0) {
            if (hoursBetween == 0) {
                if (minutesBetween < 1) {
                    return "Just now";
                } else {
                    return minutesBetween + " minutes ago";
                }
            } else {
                return hoursBetween + " hours ago";
            }
        } else if (daysBetween == 1) {
            return "1 day ago";
        } else {
            return daysBetween + " days ago";
        }
    }

    @Override
    public int getItemCount() {
        return reviewsList != null ? reviewsList.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvt;
        RatingBar ratingBar;
        TextView tvCustomerName, tvReviewContent, tvDays;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvt = itemView.findViewById(R.id.img_Avatar);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvReviewContent = itemView.findViewById(R.id.tv_review);
            tvDays = itemView.findViewById(R.id.tvDays);
        }
    }
}
