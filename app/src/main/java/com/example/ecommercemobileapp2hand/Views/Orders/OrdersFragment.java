package com.example.ecommercemobileapp2hand.Views.Orders;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ecommercemobileapp2hand.Models.Order;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Homepage.CategoriesActivity;
import com.example.ecommercemobileapp2hand.Views.Orders.CustomAdapter.OrderCardAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrdersFragment extends Fragment {
    ChipGroup chipGroup;
    RecyclerView recyOrders;
    ArrayList<Order> lstorders;
    OrderCardAdapter orderCardAdapter;
    LinearLayout linear_order1, linear_order2;
    ImageView img_empty_order;
    Button btn_explore;
    String checkstatus;
    public OrdersFragment() {
    }

    public static OrdersFragment newInstance() {
        OrdersFragment fragment = new OrdersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chipGroup = view.findViewById(R.id.chipgroup);
        recyOrders = view.findViewById(R.id.recyOrders);
        img_empty_order = view.findViewById(R.id.img_empty_order);
        linear_order1 = view.findViewById(R.id.linear_order1);
        linear_order2 = view.findViewById(R.id.linear_order2);
        btn_explore = view.findViewById(R.id.btn_explore);

        lstorders = Order.initOrder();

        if (lstorders.isEmpty())
        {
            linear_order1.setGravity(Gravity.CENTER);
            chipGroup.setVisibility(View.GONE);
            recyOrders.setVisibility(View.GONE);
            linear_order2.setVisibility(View.VISIBLE);

            btn_explore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), CategoriesActivity.class);
                    startActivity(intent);
                }
            });
        }
        else
        {
            ArrayList<String> oderstatus = new ArrayList<>();
            oderstatus.add("Processing");
            oderstatus.add("Shipped");
            oderstatus.add("Delivered");
            oderstatus.add("Returned");
            oderstatus.add("Canceled");

            createChips(oderstatus);
        }
    }
    private void createChips(ArrayList<String> statuses)
    {
        Random random = new Random();

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isDarkMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES;

        for (String status : statuses)
        {
            Chip chip = (Chip) LayoutInflater.from(getContext()).inflate(R.layout.custom_chip_item, chipGroup, false);
            chip.setText(status);
            chip.setId(random.nextInt());
            chipGroup.addView(chip);

            if (chip.getText().equals("Processing"))
            {
                chip.setChecked(true);
                chip.setTextColor(Color.parseColor("#FFFFFF"));
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#8E6CEF")));
                checkstatus = chip.getText().toString();

                ArrayList<Order> filter = filterOrder(checkstatus);
                orderCardAdapter = new OrderCardAdapter(filter, getContext(), checkstatus);
                recyOrders.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                recyOrders.setItemAnimator(new DefaultItemAnimator());

                recyOrders.setAdapter(orderCardAdapter);
            }

            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        chip.setTextColor(Color.parseColor("#FFFFFF"));
                        chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#8E6CEF")));
                        checkstatus = chip.getText().toString();

                        ArrayList<Order> filter = filterOrder(checkstatus);
                        orderCardAdapter = new OrderCardAdapter(filter, getContext(), checkstatus);
                        recyOrders.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        recyOrders.setItemAnimator(new DefaultItemAnimator());

                        recyOrders.setAdapter(orderCardAdapter);
                    }
                    else
                    {
                        if (isDarkMode)
                        {
                            chip.setTextColor(Color.parseColor("#FFFFFF"));
                            chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#342f3f")));
                        }
                        else
                        {
                            chip.setTextColor(Color.parseColor("#272727"));
                            chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#F4F4F4")));
                        }
                    }
                }
            });
        }
    }

    private ArrayList<Order> filterOrder(String stt)
    {
        ArrayList<Order> filter = new ArrayList<>();
        for (Order order : lstorders)
        {
            if (stt.equals("Processing"))
            {
                if (!order.getOrder_status_name().equals("Delivered"))
                {
                    filter.add(order);
                }
            }
            if (stt.equals("Shipped"))
            {
                if (order.getOrder_status_name().equals("Shipped"))
                {
                    filter.add(order);
                }
            }
            if (stt.equals("Delivered"))
            {
                if (order.getOrder_status_name().equals("Delivered"))
                {
                    filter.add(order);
                }
            }
        }
        return filter;
    }
}