package com.example.ecommercemobileapp2hand.Views.Settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommercemobileapp2hand.Controllers.UserAccountHandler;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class SettingsFragment extends Fragment {
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private TextView tvUserName, tvEmail, tvPhoneNumber, tvSignOut, tvEdit;
    private TextView tvAddress, tvWishlist, tvPayment, tvHelp, tvSupport;
    private static final String TAG = "SettingsFragment";
    private FirebaseAuth firebaseAuth;
    private ImageView imageUser;
    private UserAccount userAccount;
    String fullImageUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        addControls(view);

        addEvent();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

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
        imageUser = view.findViewById(R.id.imgUser);
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
            intent.putExtra("imgUrl", fullImageUrl);
            startActivityForResult(intent, 1);
        });
    }

    private void fetchUserData(String email) {
        if (email != null) {
            userAccount = UserAccountHandler.getUserAccountByEmail(email);

            if (userAccount != null) {
                tvEmail.setText(userAccount.getEmail());

                String fullName = (userAccount.getFirstName() != null ? userAccount.getFirstName() : "") + " " +
                        (userAccount.getLastName() != null ? userAccount.getLastName() : "");
                tvUserName.setText(fullName);

                tvPhoneNumber.setText(userAccount.getPhoneNumber() != null ? userAccount.getPhoneNumber() : "Null");

                if (userAccount.getImgUrl() != null && !userAccount.getImgUrl().isEmpty()) {
                    String baseUrl = "https://res.cloudinary.com/dr0xghsna/image/upload/";
                    fullImageUrl = baseUrl + userAccount.getImgUrl();

                    Picasso.get()
                            .load(fullImageUrl)
                            .placeholder(R.drawable.user)
                            .error(R.drawable.user)
                            .into(imageUser);
                    Log.d("Image", "Full Image URL: " + fullImageUrl);

                } else {
                    imageUser.setImageResource(R.drawable.user);
                }
            } else {
                Log.e(TAG, "Không tìm thấy: " + email);
            }
        } else {
            Log.e(TAG, "Không tìm thấy email");
        }
    }


    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
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

            String fullName = updatedFirstName + " " + updatedLastName;
            tvUserName.setText(fullName);

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

}
