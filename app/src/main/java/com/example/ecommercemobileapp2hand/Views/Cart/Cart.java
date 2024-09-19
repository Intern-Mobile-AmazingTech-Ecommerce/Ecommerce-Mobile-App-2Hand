package com.example.ecommercemobileapp2hand.Views.Cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.ecommercemobileapp2hand.Controllers.ProductHandler;
import com.example.ecommercemobileapp2hand.Controllers.UserAddressHandler;
import com.example.ecommercemobileapp2hand.Controllers.UserCardsHandler;
import com.example.ecommercemobileapp2hand.Models.Bag;
import com.example.ecommercemobileapp2hand.Models.Coupon;
import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.UserAddress;
import com.example.ecommercemobileapp2hand.Models.UserCards;
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
    UserAccount user;
    UserAddress userAddress;
    BigDecimal discountAmount = BigDecimal.ZERO;
    Product product = new Product();
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
        user=UserAccountManager.getInstance().getCurrentUserAccount();
        loadUserBag();
        back = findViewById(R.id.btnBack2);
        back.setOnClickListener(view -> finish());

        btncheckout = findViewById(R.id.btnCheckout);
        btncheckout.setOnClickListener(view -> {
            Intent myintent = new Intent(Cart.this, Checkout.class);
            Bundle bundle =new Bundle();
            bundle.putParcelableArrayList("listOrder",mylist);
            myintent.putExtras(bundle);
            myintent.putExtra("discount",String.valueOf(discountAmount));
            startActivity(myintent);
        });

        removeAll = findViewById(R.id.removeAll);
        removeAll.setOnClickListener(view -> {
            AlertDialog.Builder builder=new AlertDialog.Builder(Cart.this);
            builder.setTitle("Thông báo")
                    .setMessage("Bạn có chắc rằng muốn xóa tất cả sản phẩm khỏi giỏ hàng không ?")
                    .setPositiveButton("OK",((dialogInterface, i) -> {
                        BagHandler.deleteUserBag(UserAccountManager.getInstance().getCurrentUserAccount().getUserId());
                        loadUserBag();
                    })).setNegativeButton("Cancel",(dialogInterface, i) -> {}).show();
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

            if (!coupon.isActive()) {
                discountAmount = BigDecimal.ZERO;
                updateDiscountAndTotal(subtotal);
                Toast.makeText(this, "Coupon is not valid", Toast.LENGTH_SHORT).show();
                return;
            }

            Date currentDate = new Date(System.currentTimeMillis());
            if (currentDate.before(coupon.getStartDate()) || currentDate.after(coupon.getEndDate())) {
                discountAmount = BigDecimal.ZERO;
                updateDiscountAndTotal(subtotal);
                Toast.makeText(this, "Coupon has expired", Toast.LENGTH_SHORT).show();
                return;
            }

            if (coupon.getType().equals("ORDER")) {
                discountAmount = subtotal.multiply(coupon.getDiscountValue()).divide(new BigDecimal("100"));
            } else if (coupon.getType().equals("MINORDER")) {
                BigDecimal minOrderValue = new BigDecimal("1000");
                if (subtotal.compareTo(minOrderValue) >= 0) {
                    discountAmount = subtotal.multiply(coupon.getDiscountValue()).divide(new BigDecimal("100"));
                } else {
                    discountAmount = BigDecimal.ZERO;
                    updateDiscountAndTotal(subtotal);
                    Toast.makeText(this, "Order must be over $1000", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (coupon.getType().equals("PRODUCT")) {
                boolean hasNonDiscountedProducts = false;

                for (Bag item : mylist) {
                    Product product = item.getProduct();
                    if (product != null && product.getCoupon_id() == coupon.getCouponId()) {
                        discountAmount = subtotal.multiply(coupon.getDiscountValue()).divide(new BigDecimal("100"));
                    } else {
                        hasNonDiscountedProducts = true;
                    }
                }
                if (hasNonDiscountedProducts) {
                    runOnUiThread(() -> Toast.makeText(this, "Some products in the cart do not apply for the coupon", Toast.LENGTH_SHORT).show());
                    txtDiscount.setText("0");
                    txtTotal.setText(txtSubtotal.getText());
                    return;
                }
            }

            updateDiscountAndTotal(subtotal);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Number format error", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDiscountAndTotal(BigDecimal subtotal) {
        BigDecimal newTotal = subtotal.subtract(discountAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal formattedDiscountAmount = discountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        DecimalFormat df = new DecimalFormat("#.00");
        txtDiscount.setText("$" + df.format(formattedDiscountAmount));
        txtTotal.setText("$" + df.format(newTotal));

        if (myadapter != null) {
            myadapter.setDiscountAmount(formattedDiscountAmount);
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
                        Toast.makeText(this, "Coupon not found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error checking coupon", Toast.LENGTH_SHORT).show());
            }
        });
    }


    private void loadUserBag() {
        service.execute(() -> {
            user = UserAccountManager.getInstance().getCurrentUserAccount();
            if (user != null) {
                mylist = BagHandler.getData(user.getUserId());
                if (mylist != null && !mylist.isEmpty()) {
                    for (Bag item : mylist) {
                        ProductHandler.getDataByProductID(item.getProduct_id(), new ProductHandler.Callback<Product>() {
                        @Override
                        public void onResult(Product product) {
                            item.setProduct(product);
                        }
                    });
                        item.setProduct(product);
                    }
                    runOnUiThread(() -> {
                        myadapter = new Adapter_Cart(Cart.this, R.layout.layout_cart, mylist);
                        lv.setAdapter(myadapter);
                    });
                } else {
                    Intent intent = new Intent(Cart.this, EmptyCart.class);
                    startActivity(intent);
                }
            }
        });
    }
}