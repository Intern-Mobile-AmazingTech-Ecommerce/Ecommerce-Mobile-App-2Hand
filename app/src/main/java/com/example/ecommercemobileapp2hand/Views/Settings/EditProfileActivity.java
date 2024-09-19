package com.example.ecommercemobileapp2hand.Views.Settings;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ecommercemobileapp2hand.Controllers.UserAccountHandler;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.config.CloudinaryConfig;
import com.example.ecommercemobileapp2hand.R;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditProfileActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 100;

    EditText firstNameEditText, lastNameEditText, phoneNumberEditText;
    String imageUrl;
    TextView tvSave, tvEdit;
    ImageButton btnBack;
    ImageView imageUser;
    String email;
    UserAccount userAccount;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    handleSelectedImage(uri);
                } else {
                    Toast.makeText(EditProfileActivity.this, "No media selected", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControl();
        addEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }
    }

    private void addControl() {
        firstNameEditText = findViewById(R.id.firstname);
        lastNameEditText = findViewById(R.id.lastname);
        phoneNumberEditText = findViewById(R.id.phonenumber);
        tvSave = findViewById(R.id.tvSave);
        tvEdit = findViewById(R.id.edit);
        btnBack = findViewById(R.id.btn_back);
        imageUser = findViewById(R.id.imgUser);
    }

    private void addEvents() {
        Intent intent = getIntent();
        firstNameEditText.setText(intent.getStringExtra("firstName"));
        lastNameEditText.setText(intent.getStringExtra("lastName"));
        phoneNumberEditText.setText(intent.getStringExtra("phoneNumber"));

        email = intent.getStringExtra("email");

        imageUrl = getIntent().getStringExtra("imgUrl");
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl)
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(imageUser);
        }

        btnBack.setOnClickListener(view -> finish());

        tvSave.setOnClickListener(view -> {
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String phoneNumber = phoneNumberEditText.getText().toString().trim();

            if (!isValidName(firstName)) {
                firstNameEditText.setError("Must have at least 2 characters");
                return;
            }

            if (!isValidName(lastName)) {
                lastNameEditText.setError("Must have at least 2 characters");
                return;
            }

            if (!isValidPhoneNumber(phoneNumber)) {
                phoneNumberEditText.setError("Invalid phone number");
                return;
            }

            if (imageUrl != null && !imageUrl.isEmpty()) {
                uploadImage(firstName, lastName, phoneNumber);
            } else {
                updateUserDetails(firstName, lastName, phoneNumber, null);
            }
        });

        tvEdit.setOnClickListener(view -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
    }

    private boolean isValidName(String text) {
        return text.length() >= 2;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^0\\d{9}$");
    }

    private void uploadImage(String firstName, String lastName, String phoneNumber) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                InputStream inputStream;
                if (imageUrl.startsWith("content://")) {
                    Uri uri = Uri.parse(imageUrl);
                    inputStream = getContentResolver().openInputStream(uri);
                } else {
                    URL url = new URL(imageUrl);
                    inputStream = url.openStream();
                }

                if (inputStream != null) {
                    Cloudinary cloudinary = CloudinaryConfig.getCloudinary();

                    String publicId = "EcommerceApp/" + System.currentTimeMillis();

                    Map<String, Object> uploadParams = ObjectUtils.asMap("public_id", publicId, "overwrite", true);

                    Map<String, Object> uploadResult = cloudinary.uploader().upload(inputStream, uploadParams);

                    String uploadedImageUrl = (String) uploadResult.get("url");
                    String fileName = uploadedImageUrl.substring(uploadedImageUrl.lastIndexOf("/") + 1);

                    String formattedFileName = "EcommerceApp/" + fileName;

                    runOnUiThread(() -> updateUserDetails(firstName, lastName, phoneNumber, formattedFileName));
                } else {
                    runOnUiThread(() -> Toast.makeText(EditProfileActivity.this, "Cannot open image", Toast.LENGTH_SHORT).show());
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(EditProfileActivity.this, "Error uploading image", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void updateUserDetails(String firstName, String lastName, String phoneNumber, String uploadedImageUrl) {
        UserAccountHandler.updateUserAccountDetails(firstName, lastName, phoneNumber, uploadedImageUrl, email, new UserAccountHandler.Callback<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                if(result){
                    runOnUiThread(()->{
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updatedFirstName", firstName);
                        resultIntent.putExtra("updatedLastName", lastName);
                        resultIntent.putExtra("updatedPhoneNumber", phoneNumber);
                        resultIntent.putExtra("updatedImageUrl", uploadedImageUrl);
                        UserAccountManager.getInstance().getCurrentUserAccount().setImgUrl(uploadedImageUrl);
                        UserAccountManager.getInstance().getCurrentUserAccount().setFirstName(firstName);
                        UserAccountManager.getInstance().getCurrentUserAccount().setLastName(lastName);
                        UserAccountManager.getInstance().getCurrentUserAccount().setPhoneNumber(phoneNumber);
                        setResult(Activity.RESULT_OK, resultIntent);
                        Toast.makeText(EditProfileActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                    });

                }else {
                    runOnUiThread(()->{
                        Toast.makeText(EditProfileActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

    }

    private void handleSelectedImage(Uri uri) {
        try {
            InputStream imageStream = getContentResolver().openInputStream(uri);
            if (imageStream != null) {
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageUser.setImageBitmap(selectedImage);
                imageUrl = uri.toString();
            } else {
                Toast.makeText(this, "Cannot open image stream", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error selecting image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}