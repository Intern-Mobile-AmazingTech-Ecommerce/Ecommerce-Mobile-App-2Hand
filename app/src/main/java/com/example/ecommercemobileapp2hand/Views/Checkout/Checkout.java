package com.example.ecommercemobileapp2hand.Views.Checkout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.Controllers.UserAddressHandler;
import com.example.ecommercemobileapp2hand.Controllers.UserCardsHandler;
import com.example.ecommercemobileapp2hand.Controllers.UserOrderHandler;
import com.example.ecommercemobileapp2hand.Controllers.UserOrderProductsHandler;
import com.example.ecommercemobileapp2hand.Models.Bag;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.UserAddress;
import com.example.ecommercemobileapp2hand.Models.UserCards;
import com.example.ecommercemobileapp2hand.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Checkout extends AppCompatActivity {
    private static final int REQUEST_CODE_ACTIVITY_CHOOSE_ADDRESS = 1;
    private static final int REQUEST_CODE_ACTIVITY_CHOOSE_CARD = 2;

    private ImageView imgBack;
    private RelativeLayout btnPlaceOrder;
    private TextView txtSubtotal,txtTotal,txtTax,txtShippingCost,txtPrice,txtDiscount,txtShippingAddress,txtPayment;
    private ArrayList<Bag> listOrder=new ArrayList<Bag>();
    private UserAccount userAccount;
    private UserAddress userAddress;
    private UserCards userCard;
    private LinearLayout addressLayout;
    private LinearLayout paymentLayout;
    private BigDecimal discount=BigDecimal.ZERO;
    private BigDecimal total;
    private int userAddressID;
    private int userCardID;
    private ExecutorService service = Executors.newCachedThreadPool();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);
        addControl();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getIT();
        setAddressAndCard();
    }
    private void addControl(){
        imgBack = findViewById(R.id.imgBack);
        txtShippingCost=findViewById(R.id.txtShippingCost);
        txtSubtotal=findViewById(R.id.txtSubtotal);
        txtTax=findViewById(R.id.txtTax);
        txtTotal=findViewById(R.id.txtTotal);
        txtPrice=findViewById(R.id.txtPrice);
        txtDiscount=findViewById(R.id.txtDiscount);
        txtShippingAddress=findViewById(R.id.txtShippingAddress);
        txtPayment=findViewById(R.id.tvPayment);
        btnPlaceOrder= findViewById(R.id.btnAddToBag);
        addressLayout=findViewById(R.id.addressLayout);
        paymentLayout=findViewById(R.id.paymentLayout);

    }
    @Override
    protected void onResume() {
        super.onResume();
        addEvent();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && data!=null){
            int resultData=data.getIntExtra("selected_item",0);
            switch (requestCode){
                case REQUEST_CODE_ACTIVITY_CHOOSE_ADDRESS:
                    userAddressID=resultData;
                    break;
                case REQUEST_CODE_ACTIVITY_CHOOSE_CARD:
                    userCardID=resultData;
                    break;
                default:
                    break;
            }
            setAddressAndCard();
        }
    }
    private void addEvent(){
        // Set the back button to handle click event
        imgBack.setOnClickListener(v -> onBackPressed());
        setFee();

        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Checkout.this, ChooseAddressActivity.class);
                int AddressID= (userAddress==null) ? -1 :userAddress.getUser_address_id();
                intent.putExtra("addressID", AddressID);
                startActivityForResult(intent,REQUEST_CODE_ACTIVITY_CHOOSE_ADDRESS);
            }
        });
        paymentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Checkout.this, ChooseCardActivity.class);
                int CardID= (userCard==null) ? -1 :userCard.getUser_cards_id();
                intent.putExtra("cardID", CardID);
                startActivityForResult(intent,REQUEST_CODE_ACTIVITY_CHOOSE_CARD);
            }
        });
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userAddress!=null){
                    if (userCard!=null){
                        service.execute(() -> {
                            int userOrderID;
                            if (userAddressID>0){
                                userOrderID= UserOrderHandler.createUserOrder(userAccount.getUserId(),userAddressID,total,discount);
                            }
                            else{
                                userOrderID= UserOrderHandler.createUserOrder(userAccount.getUserId(),userAddress.getUser_address_id(),total,discount);
                            }
                            if (userOrderID>0){
                                for(Bag bag : listOrder){
                                    UserOrderProductsHandler.createUserOrderProduct(userOrderID,bag.getProduct_details_size_id(),bag.getAmount());
                                }
                                Intent orderSuccess=new Intent(Checkout.this, OrderPlaceSuccessfullyActivity.class);
                                startActivity(orderSuccess);
                            }
                        });
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Bạn chưa có thông tin thẻ ngân hàng",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Bạn chưa có thông tin địa chỉ",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setFee(){
        BigDecimal subtotal=BigDecimal.ZERO;
        BigDecimal tax=BigDecimal.ZERO;
        BigDecimal shippingCost=BigDecimal.ZERO;
        BigDecimal price;
        for (Bag bag : listOrder){
            if (bag.getSalePrice().compareTo(BigDecimal.ZERO)>0){
                price=bag.getSalePrice().multiply(BigDecimal.valueOf(bag.getAmount()));
            }
            else{
                price =bag.getBasePrice().multiply(BigDecimal.valueOf(bag.getAmount()));
            }
            subtotal = subtotal.add(price);
        }
        total=subtotal.add(tax).add(shippingCost.subtract(discount)).setScale(2, BigDecimal.ROUND_HALF_UP);
        txtSubtotal.setText("$"+String.valueOf(subtotal));
        txtTax.setText("$"+String.valueOf(tax));
        txtShippingCost.setText("$"+String.valueOf(shippingCost));
        txtTotal.setText("$"+String.valueOf(total));
        txtPrice.setText("$"+String.valueOf(total));
        txtDiscount.setText("$"+String.valueOf(discount));
    }
    private void getIT(){
        Intent intent = getIntent();
        if (intent!=null){
            listOrder=intent.getParcelableArrayListExtra("listOrder");
            discount=new BigDecimal(intent.getStringExtra("discount")).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
    }
    private void setAddressAndCard(){
        userAccount=UserAccountManager.getInstance().getCurrentUserAccount();
        service.execute(() -> {
            if (userAddressID>0){
                userAddress=UserAddressHandler.GetUserAddressByAddressID(userAddressID);
            }
            else{
                UserAddressHandler.getListAdressByUserId(userAccount.getUserId(), new UserAddressHandler.Callback<ArrayList<UserAddress>>() {
                    @Override
                    public void onResult(ArrayList<UserAddress> result) {
                        if (!result.isEmpty()){
                            userAddress=result.get(0);
                        }
                    }
                });
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (userAddress!=null){
                        txtShippingAddress.setText(userAddress.getUser_address_street());
                    }
                }
            });
        });
        service.execute(() -> {
            if (userCardID>0){
                userCard=UserCardsHandler.GetUserCardByCardID(userCardID);
            }
            else{
                UserCardsHandler.getListCardByUserId(userAccount.getUserId(), new UserCardsHandler.Callback<ArrayList<UserCards>>() {
                    @Override
                    public void onResult(ArrayList<UserCards> result) {
                        if (!result.isEmpty()){
                            userCard=result.get(0);
                        }
                    }
                });
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (userCard!=null){
                        txtPayment.setText(userCard.getUser_card_number());
                    }
                }
            });
        });
    }
}