package com.example.ecommercemobileapp2hand.Views.Settings;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommercemobileapp2hand.Controllers.UserAccountHandler;
import com.example.ecommercemobileapp2hand.R;

import java.io.InputStream;

public class EditProfileActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 1;

    EditText firstNameEditText, lastNameEditText, phoneNumberEditText;
    String imageUrl;
    TextView tvSave, tvEdit;
    ImageButton btnBack;
    ImageView imageView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        addControl();
        addEvents();
    }

    private void addControl() {
        firstNameEditText = findViewById(R.id.firstname);
        lastNameEditText = findViewById(R.id.lastname);
        phoneNumberEditText = findViewById(R.id.phonenumber);
        tvSave = findViewById(R.id.tvSave);
        tvEdit = findViewById(R.id.edit);
        btnBack = findViewById(R.id.btn_back);
        imageView = findViewById(R.id.img);
    }

    private void addEvents() {
        intent = getIntent();
        firstNameEditText.setText(intent.getStringExtra("firstName"));
        lastNameEditText.setText(intent.getStringExtra("lastName"));
        phoneNumberEditText.setText(intent.getStringExtra("phoneNumber"));

        String email = intent.getStringExtra("email");

        btnBack.setOnClickListener(view -> finish());

        tvSave.setOnClickListener(view -> {
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String phoneNumber = phoneNumberEditText.getText().toString().trim();

            if (!isValidName(firstName)) {
                firstNameEditText.setError("Tên phải có ít nhất 2 ký tự");
                return;
            }

            if (!isValidName(lastName)) {
                lastNameEditText.setError("Họ phải có ít nhất 2 ký tự");
                return;
            }

            if (!isValidPhoneNumber(phoneNumber)) {
                phoneNumberEditText.setError("Số điện thoại không hợp lệ");
                return;
            }

            UserAccountHandler.updateUserAccountDetails(firstName, lastName, phoneNumber, imageUrl, email);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedFirstName", firstName);
            resultIntent.putExtra("updatedLastName", lastName);
            resultIntent.putExtra("updatedPhoneNumber", phoneNumber);
            setResult(RESULT_OK, resultIntent);
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            finish();
        });

        tvEdit.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        });
    }

    private boolean isValidName(String text) {
        return text.length() >= 2;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\+?\\d{1,3}[\\s-]?\\d{1,4}[\\s-]?\\d{4,10}$");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageView.setImageBitmap(selectedImage);
                    imageUrl = selectedImageUri.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Lỗi khi chọn ảnh", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
