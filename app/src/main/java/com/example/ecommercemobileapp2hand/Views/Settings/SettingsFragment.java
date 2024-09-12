package com.example.ecommercemobileapp2hand.Views.Settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommercemobileapp2hand.Controllers.UserAccountHandler;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Login.SignInActivity;
import com.example.ecommercemobileapp2hand.Views.MainActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class SettingsFragment extends Fragment {
    private ExecutorService service;
    private TextView tvUserName, tvEmail, tvPhoneNumber, tvSignOut, tvEdit;
    private TextView tvAddress, tvWishlist, tvPayment, tvHelp, tvSupport;
    private static final String TAG = "SettingsFragment";
    private FirebaseAuth firebaseAuth;
    private ImageView imageUser;
    private UserAccount userAccount;
    private Switch switcher;
    private String fullImageUrl;
    private SharedPreferences sharedPreferences;
    private boolean nightMode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        UserAccountManager.getInstance();
        addControls(view);
        nightModeSwitch(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (service == null) {
            service = Executors.newSingleThreadExecutor();
        }
        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (googleAccount != null) {
            fetchUserData(googleAccount.getEmail());
        } else {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                fetchUserData(user.getEmail());
            }
        }
        addEvent();

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
        imageUser = view.findViewById(R.id.imgUser);
        switcher = view.findViewById(R.id.switcher);
    }

    private void addEvent() {
        tvSignOut.setOnClickListener(v -> signOut());
        tvAddress.setOnClickListener(v -> startActivity(new Intent(getActivity(), ListAddressActivity.class)));
        tvWishlist.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WishlistActivity.class);
            intent.putExtra("UserAccount", userAccount.getUserId());
            startActivity(intent);
        });
        tvPayment.setOnClickListener(v -> startActivity(new Intent(getActivity(), PaymentActivity.class)));
        tvEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.putExtra("firstName", tvUserName.getText().toString().split(" ")[0]);
            intent.putExtra("lastName", tvUserName.getText().toString().split(" ")[1]);
            intent.putExtra("phoneNumber", tvPhoneNumber.getText().toString());
            intent.putExtra("email", tvEmail.getText().toString());
            intent.putExtra("imgUrl", fullImageUrl);
            startActivityForResult(intent, 1);
        });
    }

    private void fetchUserData(String email) {
        if (email != null) {
            userAccount = UserAccountHandler.getUserAccountByEmail(email);
            UserAccountManager.getInstance().setCurrentUserAccount(userAccount);

            if (userAccount != null) {
                tvEmail.setText(userAccount.getEmail());
                tvUserName.setText(userAccount.getFirstName() + " " + userAccount.getLastName());
                tvPhoneNumber.setText(userAccount.getPhoneNumber() != null ? userAccount.getPhoneNumber() : "Null");

                if (userAccount.getImgUrl() != null && !userAccount.getImgUrl().isEmpty()) {
                    fullImageUrl = "https://res.cloudinary.com/dr0xghsna/image/upload/" + userAccount.getImgUrl();
                    Picasso.get()
                            .load(fullImageUrl)
                            .placeholder(R.drawable.user)
                            .error(R.drawable.user)
                            .into(imageUser);
                } else {
                    imageUser.setImageResource(R.drawable.user);
                }
            } else {
                Log.e(TAG, "Không tìm thấy thông tin người dùng.");
            }
        } else {
            Log.e(TAG, "Không tìm thấy email.");
        }
    }

    private void signOut() {
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireActivity(),
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build());

        googleSignInClient.signOut().addOnCompleteListener(task -> {
            LoginManager.getInstance().logOut();
            startActivity(new Intent(getActivity(), SignInActivity.class));
            requireActivity().finish();
            Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            String updatedFirstName = data.getStringExtra("updatedFirstName");
            String updatedLastName = data.getStringExtra("updatedLastName");
            String updatedPhoneNumber = data.getStringExtra("updatedPhoneNumber");
            String updatedImageUrl = data.getStringExtra("updatedImageUrl");

            tvUserName.setText(updatedFirstName + " " + updatedLastName);
            tvPhoneNumber.setText(updatedPhoneNumber);

            if (updatedImageUrl != null && !updatedImageUrl.isEmpty()) {
                Picasso.get()
                        .load(updatedImageUrl)
                        .placeholder(R.drawable.user)
                        .error(R.drawable.category_shorts)
                        .into(imageUser);
            }
        }
    }
    private void nightModeSwitch(View view) {
        switcher = view.findViewById(R.id.switcher);
        sharedPreferences = getActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);

        if (nightMode) {
            switcher.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        switcher.setOnClickListener(v -> {
            nightMode = !nightMode;
            AppCompatDelegate.setDefaultNightMode(nightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            sharedPreferences.edit().putBoolean("night", nightMode).apply();

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("navigateTo", "SettingsFragment");
            intent.putExtra("ActionBarOFF",true);
            intent.putExtra("hideActionBar", true);
            startActivity(intent);

            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }
}