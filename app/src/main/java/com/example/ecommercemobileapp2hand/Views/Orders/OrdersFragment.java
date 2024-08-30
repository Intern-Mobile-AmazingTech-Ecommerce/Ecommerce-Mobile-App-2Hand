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

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ecommercemobileapp2hand.Controllers.OrderStatusHandler;
import com.example.ecommercemobileapp2hand.Controllers.UserAccountHandler;
import com.example.ecommercemobileapp2hand.Controllers.UserOrderHandler;
import com.example.ecommercemobileapp2hand.Models.FakeModels.Order;
import com.example.ecommercemobileapp2hand.Models.OrderStatus;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.UserOrder;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Homepage.CategoriesActivity;
import com.example.ecommercemobileapp2hand.Views.Adapters.OrderCardAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Random;

public class OrdersFragment extends Fragment {
    ChipGroup chipGroup;
    RecyclerView recyOrders;
    ArrayList<UserOrder> lstorders;
    OrderCardAdapter orderCardAdapter;
    LinearLayout linear_order1, linear_order2;
    ImageView img_empty_order;
    Button btn_explore;
    String checkstatus;
    private UserAccount userAccount;
    private ArrayList<OrderStatus> oderstatus;
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

        if (getArguments() != null)
        {
            userAccount = (UserAccount) getArguments().getSerializable("UserAccount");
        }
        oderstatus = OrderStatusHandler.getData();

        chipGroup = view.findViewById(R.id.chipgroup);
        recyOrders = view.findViewById(R.id.recyOrders);
        img_empty_order = view.findViewById(R.id.img_empty_order);
        linear_order1 = view.findViewById(R.id.linear_order1);
        linear_order2 = view.findViewById(R.id.linear_order2);
        btn_explore = view.findViewById(R.id.btn_explore);

//        lstorders = new ArrayList<>();
//        lstorders = Order.initOrder();
        lstorders = UserOrderHandler.getOrder(userAccount.getUser_id());

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
            ArrayList<OrderStatus> oderstatus = OrderStatusHandler.getData();

            createChips(oderstatus);
        }
    }
    private void createChips(ArrayList<OrderStatus> statuses)
    {
        Random random = new Random();

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isDarkMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES;

        for (OrderStatus status : statuses)
        {
            Chip chip = (Chip) LayoutInflater.from(getContext()).inflate(R.layout.custom_chip_item, chipGroup, false);
            chip.setText(status.getOrder_status_name());
            chip.setId(random.nextInt());
            chipGroup.addView(chip);

            if (chip.getText().equals("Processing"))
            {
                chip.setChecked(true);
                chip.setTextColor(Color.parseColor("#FFFFFF"));
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#8E6CEF")));
                checkstatus = chip.getText().toString();

                ArrayList<UserOrder> filter = new ArrayList<>();
                for (OrderStatus ordstt : oderstatus)
                {
                    if (ordstt.getOrder_status_name().equals(checkstatus))
                    {
                        filter = filterOrder(ordstt);
                        break;
                    }
                }

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

                        ArrayList<UserOrder> filter = new ArrayList<>();
                        for (OrderStatus ordstt : oderstatus)
                        {
                            if (ordstt.getOrder_status_name().equals(checkstatus))
                            {
                                filter = filterOrder(ordstt);
                                break;
                            }
                        }
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

    private ArrayList<UserOrder> filterOrder(OrderStatus ord)
    {
        ArrayList<UserOrder> filter = new ArrayList<>();
        for (UserOrder order : lstorders)
        {
            if (ord.getOrder_status_name().equals("Processing"))
            {
                if (!OrderStatusHandler.CheckStatus(order.getOrder_status_id(), "Delivered"))
                {
                    filter.add(order);
                }
            }
            if (ord.getOrder_status_name().equals("Shipped"))
            {
                if (OrderStatusHandler.CheckStatus(order.getOrder_status_id(), ord.getOrder_status_name()))
                {
                    filter.add(order);
                }
            }
            if (ord.getOrder_status_name().equals("Delivered"))
            {
                if (OrderStatusHandler.CheckStatus(order.getOrder_status_id(), ord.getOrder_status_name()))
                {
                    filter.add(order);
                }
            }
        }
        return filter;
    }
}