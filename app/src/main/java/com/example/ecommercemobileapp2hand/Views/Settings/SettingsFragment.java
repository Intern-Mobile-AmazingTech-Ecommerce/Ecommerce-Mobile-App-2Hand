package com.example.ecommercemobileapp2hand.Views.Settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Login.SignInActivity;
import com.facebook.AccessToken;
import com.facebook.AuthenticationTokenClaims;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SettingsFragment extends Fragment {

    private TextView tvUserName, tvEmail, tvSignOut;
    private static final String TAG = "SettingsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        tvUserName = view.findViewById(R.id.tvUserName);
        tvEmail = view.findViewById(R.id.tvUserEmail);
        tvSignOut = view.findViewById(R.id.tvSignOut);

        // Google account is signed in
        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (googleAccount != null) {
            tvUserName.setText(googleAccount.getDisplayName());
            tvEmail.setText(googleAccount.getEmail());
        } else {
            //Facebook account is signed in
        }
        tvSignOut.setOnClickListener(v -> signOut());

        return view;
    }

    private void signOut() {
        // Sign out Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            // Sign out Facebook
            LoginManager.getInstance().logOut();
            // Clear user data
            startActivity(new Intent(getActivity(), SignInActivity.class));
            requireActivity().finish();
            Toast.makeText(getActivity(), "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();
        });
    }
}
