package com.example.ecommercemobileapp2hand.Views.Checkout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.ecommercemobileapp2hand.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckoutAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckoutAddressFragment extends Fragment {
    private ImageView img_address_arrowright;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CheckoutAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckoutAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckoutAddressFragment newInstance(String param1, String param2) {
        CheckoutAddressFragment fragment = new CheckoutAddressFragment();
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

        View view = inflater.inflate(R.layout.fragment_checkout_address, container, false);

        addControls(view);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        addEvents();
    }
    private void addControls(View view){
        img_address_arrowright = view.findViewById(R.id.img_address_arrowright);
    }
    private  void addEvents(){
        // Set a click listener on the ImageView
        img_address_arrowright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the new fragment
                Fragment newFragment = new CheckoutAddressOnFragment();

                // Replace the current fragment with the new fragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_Address, newFragment); // R.id.fragment_container is the ID of your container in the activity
                transaction.addToBackStack(null); // Add this transaction to the back stack
                transaction.commit();
            }
        });
    }
}