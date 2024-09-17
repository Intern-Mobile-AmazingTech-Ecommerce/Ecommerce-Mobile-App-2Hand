package com.example.ecommercemobileapp2hand.Views.Coupon;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommercemobileapp2hand.Controllers.CouponHandler;
import com.example.ecommercemobileapp2hand.Models.Coupon;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.CouponAdapter;
import java.util.ArrayList;
import java.util.List;

public class CouponDialog extends Dialog {
    private static final String TAG = "CouponDialog";
    private List<Coupon> coupons;
    private Context context;
    private OnCouponSelectedListener onCouponSelectedListener;

    public CouponDialog(@NonNull Context context, OnCouponSelectedListener onCouponSelectedListener) {
        super(context);
        this.context = context;
        this.coupons = new ArrayList<>();
        this.onCouponSelectedListener = onCouponSelectedListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Dialog created");
        fetchCoupons();
    }

    private void fetchCoupons() {
        Log.d(TAG, "fetchCoupons: Fetching coupons");
        CouponHandler.getCoupons(new CouponHandler.Callback<ArrayList<Coupon>>() {
            @Override
            public void onResult(ArrayList<Coupon> result) {
                Log.d(TAG, "fetchCoupons: Coupons fetched, size: " + result.size());
                ((Activity) context).runOnUiThread(() -> {
                    coupons.addAll(result);
                    setupDialog();
                });
            }
        });
    }

    private void setupDialog() {
        Log.d(TAG, "setupDialog: Setting up dialog");
        if (coupons.isEmpty()) {
            Log.d(TAG, "setupDialog: No coupons available, showing empty dialog");
            setContentView(R.layout.empty_coupon_dialog);
            Button btnClose = findViewById(R.id.btn_close_coupon_dialog);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "setupDialog: Close button clicked");
                    dismiss();
                }
            });
        } else {
            Log.d(TAG, "setupDialog: Coupons available, showing coupon dialog");
            setContentView(R.layout.coupon_dialog);
            RecyclerView recyclerView = findViewById(R.id.recycler_view_coupons);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            CouponAdapter adapter = new CouponAdapter(coupons, coupon -> {
                onCouponSelectedListener.onCouponSelected(coupon);
                dismiss();
            });
            recyclerView.setAdapter(adapter);

            ImageButton btnClose = findViewById(R.id.btn_close_coupon_dialog);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "setupDialog: Close button clicked");
                    dismiss();
                }
            });
        }

        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.BOTTOM;
            window.setAttributes(layoutParams);
            Log.d(TAG, "setupDialog: Window attributes set");
        }
    }

    public interface OnCouponSelectedListener {
        void onCouponSelected(Coupon coupon);
    }
}