package com.example.ecommercemobileapp2hand.Views.Cart;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    BigDecimal discountAmount = BigDecimal.ZERO;
    Product product = new Product();
    private static final String TAG = "Cart";
    private String currentCouponCode;
    private final BroadcastReceiver closeActivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };
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

        IntentFilter filter = new IntentFilter("CLOSE_ACTIVITY");
        registerReceiver(closeActivity, filter, Context.RECEIVER_NOT_EXPORTED);

        user = UserAccountManager.getInstance().getCurrentUserAccount();
        loadUserBag();
        addConTrols();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(closeActivity);
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
        back = findViewById(R.id.btnBack2);
        btncheckout = findViewById(R.id.btnCheckout);
        removeAll = findViewById(R.id.removeAll);
    }

    private void applyCoupon(Coupon coupon) {
        try {
            // Lấy subtotal và shipping cost
            String subtotalStr = txtSubtotal.getText().toString().replace("$", "");
            String shippingCostStr = txtShippingCost.getText().toString().replace("$", "");

            // kt và gán 0 nếu chuỗi rỗng
            if (subtotalStr == null || subtotalStr.isEmpty()) {
                subtotalStr = "0";
            }
            if (shippingCostStr == null || shippingCostStr.isEmpty()) {
                shippingCostStr = "0";
            }

            // Chuyển thành BigDecimal
            BigDecimal subtotal = new BigDecimal(subtotalStr);
            BigDecimal shippingCost = new BigDecimal(shippingCostStr);
//            BigDecimal totalAmount = subtotal.add(shippingCost);

            //check coupon
            if (!coupon.isActive()) {
                discountAmount = BigDecimal.ZERO;
                BigDecimal totalAmount = subtotal.add(shippingCost);
                updateDiscountAndTotal(totalAmount);
                Toast.makeText(this, "Coupon is not valid", Toast.LENGTH_SHORT).show();
                return;
            }

            //ktra ngày hiện tại có hợp lệ không
            Date currentDate = new Date(System.currentTimeMillis());
            if (currentDate.before(coupon.getStartDate()) || currentDate.after(coupon.getEndDate())) {
                discountAmount = BigDecimal.ZERO;
                BigDecimal totalAmount = subtotal.add(shippingCost);
                updateDiscountAndTotal(totalAmount);
                Toast.makeText(this, "Coupon has expired", Toast.LENGTH_SHORT).show();
                return;
            }

            discountAmount = BigDecimal.ZERO;
            List<String> discountedProducts = new ArrayList<>();

            //ORDER, áp dụng giảm giá cho toàn bộ đơn hàng
            if (coupon.getType().equals("ORDER")) {

                BigDecimal totalAmount = subtotal.add(shippingCost);
                discountAmount = totalAmount.multiply(coupon.getDiscountValue()).divide(new BigDecimal("100"));
                Log.d(TAG, "Order Discount Amount: " + discountAmount);
                updateDiscountAndTotal(totalAmount);
                Toast.makeText(this, "Coupon applied: " + coupon.getDiscountValue() + "% off on the entire order", Toast.LENGTH_LONG).show();
            }
            //PRODUCT, tính tổng giá trị sản phẩm có coupon_id tương ứng
            else if (coupon.getType().equals("PRODUCT")) {
                BigDecimal productTotal = BigDecimal.ZERO;
                for (Bag item : mylist) {
                    Product product = item.getProduct();
                    if (product != null && product.getCoupon_id() == coupon.getCouponId()) {
                        discountedProducts.add(product.getProduct_name());
                        productTotal = productTotal.add(item.getSalePrice().compareTo(BigDecimal.ZERO) != 0 ? item.getSalePrice().multiply(BigDecimal.valueOf(item.getAmount())) : item.getBasePrice().multiply(BigDecimal.valueOf(item.getAmount())));
                    }
                }
                if (discountedProducts.isEmpty()) {
                    Toast.makeText(this, "No products in the cart apply for the coupon", Toast.LENGTH_SHORT).show();
                    BigDecimal totalWithShipping = subtotal.add(shippingCost);
                    updateDiscountAndTotal(totalWithShipping);
                    txtDiscount.setText("$0.00");
                    txtTotal.setText("$"+totalWithShipping.toString());
                    return;
                }

                // tính thêm shipping cost

                //áp dụng discount
                discountAmount = productTotal.multiply(coupon.getDiscountValue()).divide(new BigDecimal("100"));
                BigDecimal totalWithShipping = discountAmount.add(shippingCost);
                updateDiscountAndTotal(totalWithShipping);

                //thông báo
                StringBuilder message = new StringBuilder("Discount applied to: ");
                for (String productName : discountedProducts) {
                    message.append(productName).append(", ");
                }
                message.setLength(message.length() - 2);
                Toast.makeText(this, message.toString(), Toast.LENGTH_LONG).show();
            }
            // MINORDER, áp dụng giảm giá nếu tổng đơn hàng đạt mức 1000$
            else if (coupon.getType().equals("MINORDER")) {
                BigDecimal minOrderAmount = new BigDecimal("1000");
                if (subtotal.compareTo(minOrderAmount) >= 0) {
                    BigDecimal totalAmount = subtotal.add(shippingCost);
                    discountAmount = totalAmount.multiply(coupon.getDiscountValue()).divide(new BigDecimal("100"));
                    Log.d(TAG, "MinOrder Discount Amount: " + discountAmount);
                    updateDiscountAndTotal(totalAmount);
                    Toast.makeText(this, "Coupon applied: " + coupon.getDiscountValue() + "% off on orders over " + minOrderAmount, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Order total must be at least " + minOrderAmount + " to apply this coupon", Toast.LENGTH_SHORT).show();
                }
            } else {
                discountAmount = BigDecimal.ZERO;
                BigDecimal totalAmount = subtotal.add(shippingCost);
                updateDiscountAndTotal(totalAmount);
                Toast.makeText(this, "Coupon type not valid", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Number format error", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDiscountAndTotal(BigDecimal subtotal) {
        BigDecimal newTotal = subtotal.subtract(discountAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal formattedDiscountAmount = discountAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        DecimalFormat df = new DecimalFormat("#.00");
        txtDiscount.setText("-$" + df.format(formattedDiscountAmount));
        txtTotal.setText("$" + df.format(newTotal));

        if (myadapter != null) {
            myadapter.setDiscountAmount(formattedDiscountAmount);
        }
    }


    private void addEvents() {
        back.setOnClickListener(view -> finish());

        btncheckout.setOnClickListener(view -> {
            Intent myintent = new Intent(Cart.this, Checkout.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("listOrder", mylist);
            myintent.putExtras(bundle);
            myintent.putExtra("discount", String.valueOf(discountAmount));
            startActivity(myintent);
        });

        removeAll.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
            builder.setTitle("Question")
                    .setMessage("Are you sure you want to delete all the product from cart ?")
                    .setPositiveButton("OK", ((dialogInterface, i) -> {
                        BagHandler.deleteUserBag(UserAccountManager.getInstance().getCurrentUserAccount().getUserId());
                        Intent intent = new Intent(Cart.this, EmptyCart.class);
                        startActivity(intent);
                        finish();
                    })).setNegativeButton("Cancel", (dialogInterface, i) -> {
                    }).show();
        });

        selectCoupon.setOnClickListener(view -> {
            CouponDialog couponDialog = new CouponDialog(this, mylist, coupon -> {
                couponCode.setText(coupon.getCode());
                currentCouponCode = coupon.getCode();
                checkCoupon(coupon.getCode());
            });
            couponDialog.show();
        });

        applyCouponButton.setOnClickListener(view -> {
            String enteredCouponCode = couponCode.getText().toString();
            if (!enteredCouponCode.isEmpty()) {
                currentCouponCode = enteredCouponCode;
                checkCoupon(enteredCouponCode);
            } else {
                Toast.makeText(this, "Please enter the coupon code", Toast.LENGTH_SHORT).show();
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
//                        Log.d("Coupon", "Coupon applied: " + couponCode);
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
                        myadapter = new Adapter_Cart(Cart.this, R.layout.layout_cart, mylist, new Adapter_Cart.OnItemBagClickListener() {
                            @Override
                            public void onItemBagClick(ArrayList<Bag> myList) {
                                mylist = myList;
                                if (currentCouponCode != null && !currentCouponCode.isEmpty()) {
                                    checkCoupon(currentCouponCode);
                                }
                            }
                        });
                        lv.setAdapter(myadapter);
                    });
                }
        });

    }
}