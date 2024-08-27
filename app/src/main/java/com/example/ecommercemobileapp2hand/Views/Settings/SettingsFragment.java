package com.example.ecommercemobileapp2hand.Views.Settings;

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
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Login.SignInActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {
    private TextView tvUserName, tvEmail, tvPhoneNumber, tvSignOut;
    private TextView tvAddress, tvWishlist, tvPayment, tvHelp, tvSupport;
    private static final String TAG = "SettingsFragment";
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        tvUserName = view.findViewById(R.id.tvUserName);
        tvEmail = view.findViewById(R.id.tvUserEmail);
        tvPhoneNumber = view.findViewById(R.id.tvUserPhoneNumber);
        tvSignOut = view.findViewById(R.id.tvSignOut);
        tvAddress = view.findViewById(R.id.Address);
        tvWishlist = view.findViewById(R.id.Wishlist);
        tvPayment = view.findViewById(R.id.Payment);
        tvHelp = view.findViewById(R.id.Help);
        tvSupport = view.findViewById(R.id.Support);

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
        fetchUserDataFromSharedPreferences();

        tvSignOut.setOnClickListener(v -> signOut());
        addressOnClick();
        return view;
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

    private void fetchUserDataFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Chưa có tên");
        String userEmail = sharedPreferences.getString("userEmail", "Chưa có email");
        String userPhone = sharedPreferences.getString("userPhone", "Chưa có số điện thoại");

        tvUserName.setText(userName);
        tvEmail.setText(userEmail);
        tvPhoneNumber.setText(userPhone);
    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            LoginManager.getInstance().logOut();
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(getActivity(), SignInActivity.class));
            requireActivity().finish();
            Toast.makeText(getActivity(), "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();
        });
    }

    private void addressOnClick() {
        tvAddress.setOnClickListener(view -> startActivity(new Intent(getActivity(), ListAddressActivity.class)));
        tvWishlist.setOnClickListener(v -> startActivity(new Intent(getActivity(), WishlistActivity.class)));
    }
}
