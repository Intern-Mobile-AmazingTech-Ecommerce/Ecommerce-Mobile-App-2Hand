package com.example.ecommercemobileapp2hand.Views.Cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.ecommercemobileapp2hand.Controllers.BagHandler;
import com.example.ecommercemobileapp2hand.Controllers.CouponHandler;
import com.example.ecommercemobileapp2hand.Models.Bag;
import com.example.ecommercemobileapp2hand.Models.Coupon;
import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.Adapter_Cart;
import com.example.ecommercemobileapp2hand.Views.Checkout.Checkout;
import com.example.ecommercemobileapp2hand.Views.Coupon.CouponDialog;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Cart extends AppCompatActivity {
    ExecutorService service = Executors.newSingleThreadExecutor();
    ImageButton back;
    TextView removeAll, txtSubtotal, txtTax, txtTotal, txtShippingCost;
    Button btncheckout;
    ArrayList<Bag> mylist;
    Adapter_Cart myadapter;
    ListView lv;
    EditText couponCode;
    ImageButton applyCouponButton;
    TextView selectCoupon;
    TextView txtDiscount;

    private static final String TAG = "Cart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadUserBag();
        back = findViewById(R.id.btnBack2);
        back.setOnClickListener(view -> finish());

        btncheckout = findViewById(R.id.btnCheckout);
        btncheckout.setOnClickListener(view -> {
            Intent myintent = new Intent(Cart.this, Checkout.class);
            myintent.putExtra("subtotal", txtSubtotal.getText().toString());
            myintent.putExtra("tax", txtTax.getText().toString());
            myintent.putExtra("shippingCost", txtShippingCost.getText().toString());
            myintent.putExtra("total", txtTotal.getText().toString());
            myintent.putExtra("listOrder", mylist);
            startActivity(myintent);
        });

        removeAll = findViewById(R.id.removeAll);
        removeAll.setOnClickListener(view -> {
            BagHandler.deleteUserBag(UserAccountManager.getInstance().getCurrentUserAccount().getUserId());
            loadUserBag();
        });

        addConTrols();
        addEvents();
    }

    private void addConTrols() {
        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtTax = findViewById(R.id.txtTax);
        txtTotal = findViewById(R.id.txtTotalPrice);
        txtShippingCost = findViewById(R.id.txtShippingCost);
        lv = findViewById(R.id.lv_cart);
        couponCode = findViewById(R.id.coupon);
        applyCouponButton = findViewById(R.id.applyCouponButton);
        selectCoupon = findViewById(R.id.select_coupon);
        txtDiscount = findViewById(R.id.txtDiscount);
    }

    private void applyCoupon(Coupon coupon) {
        try {
            String subtotalStr = txtSubtotal.getText().toString().replace("$", "");
            if (subtotalStr == null || subtotalStr.isEmpty()) {
                subtotalStr = "0";
            }
            BigDecimal subtotal = new BigDecimal(subtotalStr);
            BigDecimal discountAmount = BigDecimal.ZERO;

            if (!coupon.isActive()) {
                Toast.makeText(this, "Coupon không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            Date currentDate = new Date(System.currentTimeMillis());
            if (currentDate.before(coupon.getStartDate()) || currentDate.after(coupon.getEndDate())) {
                Toast.makeText(this, "Coupon đã hết hạn", Toast.LENGTH_SHORT).show();
                return;
            }

            // giảm giá theo tổng đơn hàng
            if (coupon.getType().equals("ORDER")) {
                discountAmount = subtotal.multiply(coupon.getDiscountValue()).divide(new BigDecimal("100"));
            }
            // giảm giá với giá trị đơn hàng tối thiểu
            else if (coupon.getType().equals("MINORDER")) {
                BigDecimal minOrderValue = new BigDecimal("1000");
                if (subtotal.compareTo(minOrderValue) >= 0) {
                    discountAmount = subtotal.multiply(coupon.getDiscountValue()).divide(new BigDecimal("100"));
                } else {
                    Toast.makeText(this, "Giá trị đơn hàng phải trên $1000", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            // giảm giá cho sản phẩm cụ thể
            else if (coupon.getType().equals("PRODUCT")) {
                discountAmount = applyProductDiscount(coupon);
            }

            BigDecimal newTotal = subtotal.subtract(discountAmount);
            DecimalFormat df = new DecimalFormat("#.00");
            txtDiscount.setText("$" + df.format(discountAmount));
            txtTotal.setText("$" + df.format(newTotal));

            if (myadapter != null) {
                myadapter.setDiscountAmount(discountAmount);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi định dạng số", Toast.LENGTH_SHORT).show();
        }
    }


    private void addEvents() {
        selectCoupon.setOnClickListener(view -> {
            CouponDialog couponDialog = new CouponDialog(this, coupon -> {
                couponCode.setText(coupon.getCode());
                checkCoupon(coupon.getCode());
            });
            couponDialog.show();
        });

        applyCouponButton.setOnClickListener(view -> {
            String enteredCouponCode = couponCode.getText().toString();
            if (!enteredCouponCode.isEmpty()) {
                checkCoupon(enteredCouponCode);
            } else {
                Toast.makeText(this, "Vui lòng nhập mã coupon", Toast.LENGTH_SHORT).show();
            }
        });

        EditText coupon = findViewById(R.id.coupon);
        coupon.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(coupon, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    private void checkCoupon(String couponCode) {
        service.execute(() -> {
            try {
                Coupon coupon = CouponHandler.getDiscountValueFromDatabase(couponCode);

                runOnUiThread(() -> {
                    if (coupon != null) {
                        applyCoupon(coupon);
                        Log.d("Coupon", "Coupon applied: " + couponCode);
                    } else {
                        Toast.makeText(this, "Không tìm thấy coupon", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error checking coupon: " + e.getMessage(), e);
                runOnUiThread(() -> Toast.makeText(this, "Lỗi khi kiểm tra coupon", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private BigDecimal applyProductDiscount(Coupon coupon) {
        BigDecimal discountAmount = BigDecimal.ZERO;
        boolean hasNonDiscountedProducts = false;

        for (Bag item : mylist) {
            Product product = item.getProduct();
            if (product != null && product.getCoupon_id() == coupon.getCouponId()) {
                // Nếu sản phẩm có coupon tương ứng, áp dụng giảm giá
                BigDecimal productPrice = product.getPrice();
                BigDecimal productDiscount = productPrice.multiply(coupon.getDiscountValue()).divide(new BigDecimal("100"));
                discountAmount = discountAmount.add(productDiscount);
            } else {
                hasNonDiscountedProducts = true;
            }
        }

        if (hasNonDiscountedProducts) {
            runOnUiThread(() -> Toast.makeText(this, "Một số sản phẩm trong giỏ hàng không áp dụng được coupon", Toast.LENGTH_SHORT).show());
        }

        return discountAmount;
    }


    private void loadUserBag() {
        service.execute(() -> {
            UserAccount user = UserAccountManager.getInstance().getCurrentUserAccount();
            if (user != null) {
                mylist = BagHandler.getData(user.getUserId());
                if (mylist != null && !mylist.isEmpty()) {
                    runOnUiThread(() -> {
                        myadapter = new Adapter_Cart(Cart.this, R.layout.layout_cart, mylist);
                        lv.setAdapter(myadapter);
                    });
                } else {
                    Intent intent = new Intent(Cart.this, EmptyCart.class);
                    startActivity(intent);
                }
            } else {
                runOnUiThread(() -> Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show());
            }
        });
    }
}