package com.example.ecommercemobileapp2hand.Views.Login;

import android.content.Intent;
import android.content.IntentSender;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.MainActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class SignInActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    Button btnSignInGoogle;
    Button btnSignInFacebook;
    Button btnContinue;
    TextView txtCreateAccount;
    private static final int REQ_GOOGLE_SIGN_IN = 1;
    private static final int REQ_FACEBOOK_LOGIN = 2;

    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        continueSignIn();
        createAccount();
        signInGoogle();
        signInFacebook();

    }

    private void signInGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

        btnSignInGoogle = findViewById(R.id.btnSignInGoogle);
        btnSignInGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, REQ_GOOGLE_SIGN_IN);
        });
    }

    //Lỗi ở đây khi chạy app
    private void signInFacebook() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onCancel() {
                //khi người dùng hủy đăng nhập
                Toast.makeText(SignInActivity.this, "Facebook login canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                //lỗi đăng nhập
                Toast.makeText(SignInActivity.this, "Facebook login failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        btnSignInFacebook = findViewById(R.id.btnSignInFacebook);
        btnSignInFacebook.setOnClickListener(v -> {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        });
    }

    private void continueSignIn(){
        btnContinue=(Button) findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,SignInPasswordActivity.class));
            }
        });
    }

    private void createAccount(){
        txtCreateAccount=(TextView) findViewById(R.id.dont_have_a);
        txtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_GOOGLE_SIGN_IN) { // Đăng nhập Google
            if (resultCode == RESULT_OK) {
                GoogleSignIn.getSignedInAccountFromIntent(data)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                GoogleSignInAccount account = task.getResult();
                                if (account != null) {
                                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                    finish(); //không quay lại SignInActivity khi nhấn nút back
                                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.w(TAG, "Google sign in failed", task.getException());
                            }
                        });
            }
        } else if (requestCode == REQ_FACEBOOK_LOGIN) { // Đăng nhập Facebook
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
