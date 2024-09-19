package com.example.ecommercemobileapp2hand.Views.Checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecommercemobileapp2hand.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckoutPaymentOnFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckoutPaymentOnFragment extends Fragment {
    private ImageView img_payment_arrowright;
    private TextView tvPayment;
    private Switch paymentSwitch;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CheckoutPaymentOnFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckoutPaymentOnFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckoutPaymentOnFragment newInstance(String param1, String param2) {
        CheckoutPaymentOnFragment fragment = new CheckoutPaymentOnFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checkout_payment_on, container, false);
        addControls(view);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();

        addEvents();
    }
    private void addControls(View view){
        img_payment_arrowright = view.findViewById(R.id.img_payment_arrowright);
        tvPayment = view.findViewById(R.id.tvPayment);
        paymentSwitch = view.findViewById(R.id.paymentSwitch);
        tvPayment.setText("**** 1234");
    }
    private  void addEvents(){

        img_payment_arrowright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment newFragment = new CheckoutPaymentFragment();
//                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_Payment, newFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
            }
        });
        paymentSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tvPayment.setText("1234 5678 9012 3456");
            } else {
                tvPayment.setText("**** 1234");
            }
        });
    }
}