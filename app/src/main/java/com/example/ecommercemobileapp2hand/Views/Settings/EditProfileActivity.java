package com.example.ecommercemobileapp2hand.Views.Settings;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.config.CloudinaryConfig;
import com.example.ecommercemobileapp2hand.R;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditProfileActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 100;
    EditText edtFirstName, edtLastName, edtPhoneNumber;
    String imageUrl;
    TextView tvSave, tvEdit;
    ImageButton btnBack;
    ImageView imageUser;
    String email;
    UserAccount userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addControl();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }
        addEvents();
    }

    private void addControl() {
        edtFirstName = findViewById(R.id.firstname);
        edtLastName = findViewById(R.id.lastname);
        edtPhoneNumber = findViewById(R.id.phonenumber);
        tvSave = findViewById(R.id.tvSave);
        tvEdit = findViewById(R.id.edit);
        btnBack = findViewById(R.id.btn_back);
        imageUser = findViewById(R.id.imgUser);
    }

    private void addEvents() {
        Intent intent = getIntent();
        edtFirstName.setText(intent.getStringExtra("firstName"));
        edtLastName.setText(intent.getStringExtra("lastName"));
        edtPhoneNumber.setText(intent.getStringExtra("phoneNumber"));

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
            String firstName = edtFirstName.getText().toString().trim();
            String lastName = edtLastName.getText().toString().trim();
            String phoneNumber = edtPhoneNumber.getText().toString().trim();

            boolean isFirstNameChanged = !firstName.equals(intent.getStringExtra("firstName"));
            boolean isLastNameChanged = !lastName.equals(intent.getStringExtra("lastName"));
            boolean isPhoneNumberChanged = !phoneNumber.equals(intent.getStringExtra("phoneNumber"));

            if (firstName.isEmpty()) {
                edtFirstName.setError("Tên không được để trống");
                return;
            }
            if (lastName.isEmpty()) {
                edtLastName.setError("Họ không được để trống");
                return;
            }
            if (phoneNumber.isEmpty()) {
                edtPhoneNumber.setError("Số điện thoại không được để trống");
                return;
            }

            if (isFirstNameChanged && !isValidName(firstName)) {
                edtFirstName.setError("Tên phải có ít nhất 2 ký tự và chỉ chứa chữ cái");
                return;
            }
            if (isLastNameChanged && !isValidName(lastName)) {
                edtLastName.setError("Họ phải có ít nhất 2 ký tự và chỉ chứa chữ cái");
                return;
            }
            if (isPhoneNumberChanged && !isValidPhoneNumber(phoneNumber)) {
                edtPhoneNumber.setError("Số điện thoại không hợp lệ");
                return;
            }


            boolean isImageUrlChanged = imageUrl != null && !imageUrl.equals(intent.getStringExtra("imgUrl"));

            if (isFirstNameChanged || isLastNameChanged || isPhoneNumberChanged || isImageUrlChanged) {
                if (isImageUrlChanged) {
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> uploadImage(firstName, lastName, phoneNumber, isFirstNameChanged, isLastNameChanged, isPhoneNumberChanged));
                    finish();
                } else {
                    updateUserDetails(firstName, lastName, phoneNumber, intent.getStringExtra("imgUrl"), isFirstNameChanged, isLastNameChanged, isPhoneNumberChanged);
                    finish();
                }

            } else {
                Toast.makeText(this, "Không có thay đổi nào để lưu", Toast.LENGTH_SHORT).show();
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
        return phoneNumber.matches("^\\+?\\d{1,3}[\\s-]?\\d{1,4}[\\s-]?\\d{4,10}$");
    }

    private void uploadImage(String firstName, String lastName, String phoneNumber, boolean isFirstNameChanged, boolean isLastNameChanged, boolean isPhoneNumberChanged) {
        try {
            Uri uri = Uri.parse(imageUrl);
            InputStream inputStream = getContentResolver().openInputStream(uri);

            if (inputStream != null) {
                Cloudinary cloudinary = CloudinaryConfig.getCloudinary();

                String publicId = "EcommerceApp/" + System.currentTimeMillis();

                Map<String, Object> uploadParams = ObjectUtils.asMap("public_id", publicId, "overwrite", true);

                Map<String, Object> uploadResult = cloudinary.uploader().upload(inputStream, uploadParams);

                String uploadedImageUrl = (String) uploadResult.get("url");
                String fileName = uploadedImageUrl.substring(uploadedImageUrl.lastIndexOf("/") + 1);

                String formattedFileName = "EcommerceApp/" + fileName;

                runOnUiThread(() -> updateUserDetails(firstName, lastName, phoneNumber, formattedFileName, isFirstNameChanged, isLastNameChanged, isPhoneNumberChanged));
            } else {
                runOnUiThread(() -> Toast.makeText(EditProfileActivity.this, "Lỗi", Toast.LENGTH_SHORT).show());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(EditProfileActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
        }
    }

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    handleSelectedImage(uri);
                }
            });

    private void updateUserDetails(String firstName, String lastName, String phoneNumber, String uploadedImageUrl, boolean isFirstNameChanged, boolean isLastNameChanged, boolean isPhoneNumberChanged) {
        UserAccountHandler.updateUserAccountDetails(firstName, lastName, phoneNumber, uploadedImageUrl, email);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedFirstName", firstName);
        resultIntent.putExtra("updatedLastName", lastName);
        resultIntent.putExtra("updatedPhoneNumber", phoneNumber);
        resultIntent.putExtra("updatedImageUrl", uploadedImageUrl);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
        Toast.makeText(EditProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
    }

    private void handleSelectedImage(Uri uri) {
        try {
            InputStream imageStream = getContentResolver().openInputStream(uri);
            if (imageStream != null) {
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageUser.setImageBitmap(selectedImage);
                imageUrl = uri.toString();
            } else {
                Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
