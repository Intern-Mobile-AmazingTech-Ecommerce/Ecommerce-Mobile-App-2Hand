package com.example.ecommercemobileapp2hand.Views.Settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommercemobileapp2hand.Controllers.UserAccountHandler;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.config.DBConnect;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Login.SignInActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsFragment extends Fragment {
    private TextView tvUserName, tvEmail, tvPhoneNumber, tvSignOut, tvEdit;
    private TextView tvAddress, tvWishlist, tvPayment, tvHelp, tvSupport;
    private static final String TAG = "SettingsFragment";
    private FirebaseAuth firebaseAuth;
    private static final String PREFS_NAME = "user_prefs";

    private UserAccount userAccount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        if (getArguments() != null) {
            userAccount = (UserAccount) getArguments().getSerializable("UserAccount");
        }

        addControls(view);

        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (googleAccount != null) {
            saveUserDataToSharedPreferences(googleAccount.getDisplayName(), googleAccount.getEmail(), null);
            tvUserName.setText(googleAccount.getDisplayName());
            tvEmail.setText(googleAccount.getEmail());
        } else {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                fetchUserDataFromFirebase();
            }
        }

        //fetchUserDataFromSharedPreferences();
        fetchUserData();

        addEvent();
        return view;
    }


    private void addControls(View view) {
        tvUserName = view.findViewById(R.id.tvUserName);
        tvEmail = view.findViewById(R.id.tvUserEmail);
        tvPhoneNumber = view.findViewById(R.id.tvUserPhoneNumber);
        tvSignOut = view.findViewById(R.id.tvSignOut);
        tvAddress = view.findViewById(R.id.Address);
        tvWishlist = view.findViewById(R.id.Wishlist);
        tvPayment = view.findViewById(R.id.Payment);
        tvHelp = view.findViewById(R.id.Help);
        tvSupport = view.findViewById(R.id.Support);
        tvEdit = view.findViewById(R.id.edit);
    }

    private void fetchUserDataFromFirebase() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            String userName = user.getDisplayName();
            String userEmail = user.getEmail();
            String userPhone = user.getPhoneNumber();

            saveUserDataToSharedPreferences(userName, userEmail, userPhone);
        }
    }

    private void saveUserDataToSharedPreferences(String userName, String userEmail, String userPhone) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("userName", userName);
        editor.putString("userEmail", userEmail);
        editor.putString("userPhone", userPhone);
        editor.apply();
    }

//    private void fetchUserDataFromSharedPreferences() {
//        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
//        String userName = sharedPreferences.getString("userName", "Null");
//        String userEmail = sharedPreferences.getString("userEmail", "Null");
//        String userPhone = sharedPreferences.getString("userPhone", "Null");
//
//        tvUserName.setText(userName);
//        tvEmail.setText(userEmail);
//        tvPhoneNumber.setText(userPhone);
//    }

    private void fetchUserData() {
        String email = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getString("userEmail", null);

        if (email != null) {
            UserAccount userAccount = UserAccountHandler.getUserAccountByEmail(email);

            if (userAccount != null) {
                email = userAccount.getEmail();
                String firstName = userAccount.getFirstName();
                String lastName = userAccount.getLastName();
                String phoneNumber = userAccount.getPhoneNumber();

                tvEmail.setText(email);
                String fullName = firstName + " " + lastName;
                tvUserName.setText(fullName);
                tvPhoneNumber.setText(phoneNumber != null ? phoneNumber : "Null");
            } else {
                Log.e(TAG, "Không tìm thấy người dùng với email: " + email);
            }
        } else {
            Log.e(TAG, "Email không hợp lệ.");
        }
    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            LoginManager.getInstance().logOut();
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(getActivity(), SignInActivity.class));
            requireActivity().finish();
            Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        });
    }

    private void addEvent() {
        tvSignOut.setOnClickListener(v -> signOut());
        tvAddress.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListAddressActivity.class);
            startActivity(intent);
        });
        tvWishlist.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WishlistActivity.class);
            intent.putExtra("UserAccount", userAccount.getUserId());
            startActivity(intent);
        });
        tvPayment.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            startActivity(intent);
        });

        tvEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.putExtra("firstName", tvUserName.getText().toString().split(" ")[0]);
            intent.putExtra("lastName", tvUserName.getText().toString().split(" ")[1]);
            intent.putExtra("phoneNumber", tvPhoneNumber.getText().toString());
            intent.putExtra("email", tvEmail.getText().toString());
            startActivityForResult(intent, 1);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            String updatedFirstName = data.getStringExtra("updatedFirstName");
            String updatedLastName = data.getStringExtra("updatedLastName");
            String updatedPhoneNumber = data.getStringExtra("updatedPhoneNumber");

            String fullName = updatedFirstName + " " + updatedLastName;
            tvUserName.setText(fullName);
            tvPhoneNumber.setText(updatedPhoneNumber);
        }
    }

}
