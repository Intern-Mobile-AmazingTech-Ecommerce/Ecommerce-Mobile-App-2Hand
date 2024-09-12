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
import com.example.ecommercemobileapp2hand.Models.OrderStatus;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.UserOrder;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Homepage.CategoriesActivity;
import com.example.ecommercemobileapp2hand.Views.Adapters.OrderCardAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class OrdersFragment extends Fragment {
    private ExecutorService service;
    private Future<?> task;
    private ChipGroup chipGroup;
    private RecyclerView recyOrders;
    private ArrayList<UserOrder> lstorders;
    private OrderCardAdapter orderCardAdapter;
    private LinearLayout linear_order1, linear_order2;
    private ImageView img_empty_order;
    private Button btn_explore;
    private String checkstatus;
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
    public void onStart() {
        super.onStart();
        // Nhận tt user từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            UserAccount userAccount = (UserAccount) bundle.getSerializable("UserAccount");
            if (userAccount != null) {
                String email = userAccount.getEmail();
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        addControl(view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public void onResume() {
        super.onResume();
        userAccount = UserAccountManager.getInstance().getCurrentUserAccount();
        if(service == null){
            service = Executors.newCachedThreadPool();
        }
        bgTask();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (service != null && !service.isShutdown()) {
            service.shutdown();
            try {
                if (!service.awaitTermination(60, TimeUnit.SECONDS)) {
                    service.shutdownNow();
                }
            } catch (InterruptedException e) {
                service.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    private void bgTask(){
        getActivity().runOnUiThread(()->{
            lstorders = userAccount.getLstOrder() != null ? userAccount.getLstOrder() : new ArrayList<>();
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
                loadOrderStatus();
            }
        });


    }
    private void addControl(View view){
        chipGroup = view.findViewById(R.id.chipgroup);
        recyOrders = view.findViewById(R.id.recyOrders);
        img_empty_order = view.findViewById(R.id.img_empty_order);
        linear_order1 = view.findViewById(R.id.linear_order1);
        linear_order2 = view.findViewById(R.id.linear_order2);
        btn_explore = view.findViewById(R.id.btn_explore);
    }
    private void loadOrderStatus(){
       task = service.submit(()->{
            oderstatus = OrderStatusHandler.getData();
            getActivity().runOnUiThread(()->{
                if(!oderstatus.isEmpty() && oderstatus != null)
                {
                    createChips(oderstatus);
                }
            });
        });
    }
    private void createChips(ArrayList<OrderStatus> statuses)
    {
        chipGroup.removeAllViews();

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

                orderCardAdapter = new OrderCardAdapter(filter, getContext(), userAccount);
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
                        orderCardAdapter = new OrderCardAdapter(filter, getContext(), userAccount);
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