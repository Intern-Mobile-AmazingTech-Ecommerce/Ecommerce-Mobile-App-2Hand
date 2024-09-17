package com.example.ecommercemobileapp2hand.Views.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecommercemobileapp2hand.Controllers.BagHandler;
import com.example.ecommercemobileapp2hand.Models.Bag;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Utils.Util;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Adapter_Cart extends ArrayAdapter<Bag> {
    Activity context;
    int IdLayout;
    ArrayList<Bag> myList;
    BigDecimal discountAmount = BigDecimal.ZERO; // Add this line

    public Adapter_Cart(Activity context, int idLayout, ArrayList<Bag> myList) {
        super(context, idLayout, myList);
        this.context = context;
        IdLayout = idLayout;
        this.myList = myList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myInflater = context.getLayoutInflater();
        convertView = myInflater.inflate(IdLayout, null);
        Bag myCart = myList.get(position);

        ImageView imgCart = convertView.findViewById(R.id.imgCart);
        Util.getCloudinaryImageUrl(context, myCart.getImage(), 64, 64, result -> {
            String url = result;
            context.runOnUiThread(() -> Picasso.get().load(url).into(imgCart));
        });

        TextView txt_NameCart = convertView.findViewById(R.id.txt_NameCart);
        txt_NameCart.setText(myCart.getProduct_name());

        TextView txt_SizeCart = convertView.findViewById(R.id.txt_SizeCart);
        txt_SizeCart.setText(txt_SizeCart.getText() + " " + myCart.getSize());

        BigDecimal price;
        if (myCart.getSalePrice().compareTo(BigDecimal.ZERO) > 0) {
            price = myCart.getSalePrice().multiply(BigDecimal.valueOf(myCart.getAmount()));
        } else {
            price = myCart.getBasePrice().multiply(BigDecimal.valueOf(myCart.getAmount()));
        }
        TextView txt_PriceCart = convertView.findViewById(R.id.txt_PriceCart);
        txt_PriceCart.setText("$" + price);

        TextView txt_ColorCart = convertView.findViewById(R.id.txt_ColorCart);
        txt_ColorCart.setText(txt_ColorCart.getText() + " " + myCart.getColor());

        TextView txt_Quantity = convertView.findViewById(R.id.txt_Quantity);
        txt_Quantity.setText(String.valueOf(myCart.getAmount()));
        setFee();

        ImageButton imageButtonAdd = convertView.findViewById(R.id.imageButtonAdd);
        imageButtonAdd.setOnClickListener(view -> {
            int quantity = Integer.parseInt(txt_Quantity.getText().toString());
            quantity += 1;
            boolean result = BagHandler.updateProductAmount(myCart.getUser_id(), myCart.getProduct_details_size_id(), quantity);
            if (result) {
                txt_Quantity.setText(String.valueOf(quantity));
                BigDecimal newPrice;
                if (myCart.getSalePrice().compareTo(BigDecimal.ZERO) > 0) {
                    newPrice = myCart.getSalePrice().multiply(BigDecimal.valueOf(quantity));
                } else {
                    newPrice = myCart.getBasePrice().multiply(BigDecimal.valueOf(quantity));
                }
                txt_PriceCart.setText("$" + newPrice);
                myList = BagHandler.getData(myCart.getUser_id());
                setFee();
            }
            showMessage(result);
        });

        ImageButton imageButtonMinus = convertView.findViewById(R.id.imageButtonMinus);
        imageButtonMinus.setOnClickListener(view -> {
            int quantity = Integer.parseInt(txt_Quantity.getText().toString());
            if (quantity > 1) {
                quantity -= 1;
                boolean result = BagHandler.updateProductAmount(myCart.getUser_id(), myCart.getProduct_details_size_id(), quantity);
                if (result) {
                    txt_Quantity.setText(String.valueOf(quantity));
                    BigDecimal newPrice;
                    if (myCart.getSalePrice().compareTo(BigDecimal.ZERO) > 0) {
                        newPrice = myCart.getSalePrice().multiply(BigDecimal.valueOf(quantity));
                    } else {
                        newPrice = myCart.getBasePrice().multiply(BigDecimal.valueOf(quantity));
                    }
                    txt_PriceCart.setText("$" + newPrice);
                    myList = BagHandler.getData(myCart.getUser_id());
                    setFee();
                }
                showMessage(result);
            }
        });
        return convertView;
    }

    private void setFee() {
        TextView txtSubtotal = context.findViewById(R.id.txtSubtotal);
        TextView txtTax = context.findViewById(R.id.txtTax);
        TextView txtTotal = context.findViewById(R.id.txtTotalPrice);
        TextView txtShippingCost = context.findViewById(R.id.txtShippingCost);
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;
        BigDecimal shippingCost = BigDecimal.ZERO;
        BigDecimal total;
        BigDecimal price;
        for (Bag bag : myList) {
            if (bag.getSalePrice().compareTo(BigDecimal.ZERO) > 0) {
                price = bag.getSalePrice().multiply(BigDecimal.valueOf(bag.getAmount()));
            } else {
                price = bag.getBasePrice().multiply(BigDecimal.valueOf(bag.getAmount()));
            }
            subtotal = subtotal.add(price);
        }
        total = subtotal.add(tax).add(shippingCost).subtract(discountAmount); // Apply discount here
        txtSubtotal.setText("$" + subtotal);
        txtTax.setText("$" + tax);
        txtShippingCost.setText("$" + shippingCost);
        txtTotal.setText("$" + total);
    }

    private void showMessage(boolean result) {
        if (!result) {
            Toast.makeText(context, "Đã đến giới hạn sản phẩm còn trong kho", Toast.LENGTH_SHORT).show();
        }
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        setFee(); // Recalculate fees with the new discount
    }
}