package com.example.ecommercemobileapp2hand.Views.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommercemobileapp2hand.Controllers.UserAccountHandler;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.MainActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignInActivity extends AppCompatActivity {

    private static final int REQ_GOOGLE_SIGN_IN = 1;
    private static final String TAG = "SignInActivity";

    private CallbackManager callbackManager;
    private Button btnSignInGoogle;
    private Button btnSignInFacebook;
    private Button btnContinue;
    private TextView txtCreateAccount;
    private EditText edtEmail;
    private Switch switcher;
    private SharedPreferences sharedPreferences;
    private boolean nightMode;
    private FirebaseAuth firebaseAuth;
    ExecutorService service =  Executors.newCachedThreadPool();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onStart() {
        super.onStart();
        addControls();
        addEvents();
        facebookLoginMethod();
        googleLoginMethod();
        nightModeSwitch();
    }

    private void addControls() {
        btnContinue = findViewById(R.id.btnContinue);
        txtCreateAccount = findViewById(R.id.dont_have_a);
        btnSignInGoogle = findViewById(R.id.btnSignInGoogle);
        btnSignInFacebook = findViewById(R.id.btnSignInFacebook);
        edtEmail = findViewById(R.id.email_address);
    }

    private void addEvents() {
        btnContinue.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            if (!isValidEmail(email)) {
                edtEmail.setError("Email không hợp lệ");
                return;
            }
            Intent intent = new Intent(SignInActivity.this, SignInPasswordActivity.class);
            intent.putExtra("Email", email);
            startActivity(intent);
        });

        txtCreateAccount.setOnClickListener(v -> startActivity(new Intent(SignInActivity.this, SignUpActivity.class)));
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void facebookLoginMethod() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Đăng nhập bị huỷ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Facebook login error", error);
            }
        });

        btnSignInFacebook.setOnClickListener(v ->
                LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("email", "public_profile")));
    }

    private void googleLoginMethod() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

        btnSignInGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, REQ_GOOGLE_SIGN_IN);
        });
    }

    private void nightModeSwitch() {
        switcher = findViewById(R.id.switcher);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);

        if (nightMode) {
            switcher.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        switcher.setOnClickListener(v -> {
            nightMode = !nightMode;
            AppCompatDelegate.setDefaultNightMode(nightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            sharedPreferences.edit().putBoolean("night", nightMode).apply();
        });
    }

    private void handleSignInResult(FirebaseUser user) {
        if (user != null) {
            String email = user.getEmail();
            if (email != null) {
                UserAccount userAccount = UserAccountHandler.getUserAccount(email);
                UserAccountManager.getInstance().setCurrentUserAccount(userAccount);
                UserAccountHandler userAccountHandler = new UserAccountHandler();

                service.execute(()->{
                    boolean emailExists = userAccountHandler.checkEmailExists(email);
                    Intent intent = emailExists ? new Intent(SignInActivity.this, MainActivity.class) : new Intent(SignInActivity.this, OnboardingActivity.class);
                    intent.putExtra("UserAccount", userAccount);
                    intent.putExtra("email", email);
                    intent.putExtra("displayName", user.getDisplayName());
                    startActivity(intent);
                });


                finish();
            } else {
                Toast.makeText(this, "Email is null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "User is null", Toast.LENGTH_SHORT).show();
        }
    }


    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String email = user.getEmail();
                    String displayName = user.getDisplayName();
                    service.execute(()->{
                        UserAccountHandler.saveUserAccount(email, displayName, "Facebook");
                    });
                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    handleSignInResult(user);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Facebook login error", task.getException());
            }
        });
    }


    private void handleGoogleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, authTask -> {
                    if (authTask.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        handleSignInResult(user);
                    } else {
                        Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Google sign-in error", authTask.getException());
                    }
                });
            }
        } catch (ApiException e) {
            Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Google sign-in failed", e);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        } else if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
