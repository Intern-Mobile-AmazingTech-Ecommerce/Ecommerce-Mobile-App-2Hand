package com.example.ecommercemobileapp2hand.Views.Checkout;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommercemobileapp2hand.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckoutPaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckoutPaymentFragment extends Fragment {
    private ImageView img_payment_arrowright;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private TextView tv_Payment;

    public CheckoutPaymentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment CheckoutPaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckoutPaymentFragment newInstance(String param1) {
        CheckoutPaymentFragment fragment = new CheckoutPaymentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_payment, container, false);
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
        tv_Payment=view.findViewById(R.id.tvPayment);
    }
    private  void addEvents(){
        if (mParam1!=null){
            tv_Payment.setText(mParam1);
        }
        img_payment_arrowright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(),ChooseCardActivity.class);
                startActivity(intent);
            }
        });
    }
}