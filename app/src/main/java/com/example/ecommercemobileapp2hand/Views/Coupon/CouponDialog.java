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
import com.example.ecommercemobileapp2hand.Models.Bag;
import com.example.ecommercemobileapp2hand.Models.Coupon;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.CouponAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CouponDialog extends Dialog {
    private static final String TAG = "CouponDialog";
    private List<Coupon> coupons;
    private List<Bag> mylist; // Add this line
    private Context context;
    private OnCouponSelectedListener onCouponSelectedListener;

    public CouponDialog(@NonNull Context context, List<Bag> mylist, OnCouponSelectedListener onCouponSelectedListener) { // Update constructor
        super(context);
        this.context = context;
        this.coupons = new ArrayList<>();
        this.mylist = mylist; // Add this line
        this.onCouponSelectedListener = onCouponSelectedListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchCoupons();
    }

    private void fetchCoupons() {
        CouponHandler.getCoupons(new CouponHandler.Callback<ArrayList<Coupon>>() {
            @Override
            public void onResult(ArrayList<Coupon> result) {
                ((Activity) context).runOnUiThread(() -> {
                    BigDecimal subtotal = calculateSubtotal();
                    for (Coupon coupon : result) {
                        if (coupon.getType().equals("MINORDER") && subtotal.compareTo(new BigDecimal("1000")) < 0) {
                            continue; // Ẩn coupon MINORDER nếu tổng giá trị đơn hàng dưới 1000$
                        }

                        if (coupon.getType().equals("PRODUCT")) {
                            boolean hasMatchingProduct = false;
                            for (Bag item : mylist) {
                                if (item.getProduct() != null && item.getProduct().getCoupon_id() == coupon.getCouponId()) {
                                    hasMatchingProduct = true;
                                    break;
                                }
                            }
                            if (!hasMatchingProduct) {
                                continue; // Ẩn coupon PRODUCT nếu không có sản phẩm nào có coupon_id trùng
                            }
                        }

                        coupons.add(coupon); // Thêm coupon hợp lệ vào danh sách
                    }
                    setupDialog();
                });
            }
        });
    }

    private BigDecimal calculateSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (Bag item : mylist) {
            subtotal = subtotal.add(item.getSalePrice().compareTo(BigDecimal.ZERO) != 0 ? item.getSalePrice().multiply(BigDecimal.valueOf(item.getAmount())) : item.getBasePrice().multiply(BigDecimal.valueOf(item.getAmount())) );
        }
        return subtotal;
    }

    private void setupDialog() {
        if (coupons.isEmpty()) {
            setContentView(R.layout.empty_coupon_dialog);
            Button btnClose = findViewById(R.id.btn_close_coupon_dialog);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
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
        }
    }

    public interface OnCouponSelectedListener {
        void onCouponSelected(Coupon coupon);
    }
}