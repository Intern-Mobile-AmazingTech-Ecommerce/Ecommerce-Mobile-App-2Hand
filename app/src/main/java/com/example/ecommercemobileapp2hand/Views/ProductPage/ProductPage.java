package com.example.ecommercemobileapp2hand.Views.ProductPage;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Controllers.BagHandler;
import com.example.ecommercemobileapp2hand.Controllers.UserAccountHandler;
import com.example.ecommercemobileapp2hand.Controllers.WishlistHandler;
import com.example.ecommercemobileapp2hand.Models.Bag;
import com.example.ecommercemobileapp2hand.Models.FakeModels.Reviews;
import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.Models.ProductColor;
import com.example.ecommercemobileapp2hand.Models.ProductDetails;
import com.example.ecommercemobileapp2hand.Models.ProductDetailsImg;
import com.example.ecommercemobileapp2hand.Models.ProductDetailsSize;
import com.example.ecommercemobileapp2hand.Models.ProductReview;
import com.example.ecommercemobileapp2hand.Models.Singleton.UserAccountManager;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.Models.Wishlist;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.GenderAdapter;
import com.example.ecommercemobileapp2hand.Views.Adapters.RecycleProductImageAdapter;
import com.example.ecommercemobileapp2hand.Views.Adapters.RecycleReviewAdapter;
import com.example.ecommercemobileapp2hand.Views.Adapters.RecycleSizeAdapter;
import com.example.ecommercemobileapp2hand.Views.Adapters.RecylerColorAdapter;
import com.example.ecommercemobileapp2hand.Views.Adapters.SortByAdapter;
import com.example.ecommercemobileapp2hand.Views.Adapters.WishListAdapter;
import com.example.ecommercemobileapp2hand.Views.Homepage.HomeFragment;
import com.example.ecommercemobileapp2hand.Views.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ProductPage extends AppCompatActivity {
    private ExecutorService service = Executors.newCachedThreadPool();
    private UserAccount userAccount;
    private Product product;
    //Current
    private ProductDetails currentDetails;
    private ProductDetailsSize currentSize;
    private ArrayList<ProductDetailsSize> currentSizes;
    //
    private RecyclerView recycleImgSlider;
    private RecycleProductImageAdapter imgSliderApdater;
    private ArrayList<ProductDetailsImg> imgList;
    private ArrayList<ProductReview> reviewsList;
    private ArrayList<ProductColor> colorList;
    private RecycleReviewAdapter reviewAdapter;
    private RecyclerView recycleReviews;
    private RelativeLayout btnColor, btnSize, btnQuantity;
    private ImageView imgBack, btnIncrease, btnDecrease, btnFavorite;
    private TextView tvProductName, tvPrice, tvOldPrice, tvDescription, tvSize, tvQuantity, tvTotalPrice,tvRatingPoints,tvTotalReviews;
    private View bgColor;
    private int quantity = 1;
    private BigDecimal totalPrice;
    private BigDecimal productPrice;
    private RelativeLayout btnAddToBag, btnOutOfStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControl();


    }

    private void getBundleIntent() {
        Intent intent = getIntent();
        if (intent.getParcelableExtra("lstDetails") != null) {
            product = intent.getParcelableExtra("lstDetails");
        }
        if (intent.getParcelableExtra("currentSale") != null) {
            currentDetails = intent.getParcelableExtra("currentSale");
        } else {
            currentDetails = product.getProductDetailsArrayList().get(0);
        }
        currentSize = currentDetails.getSizeArrayList() != null ? currentDetails.getSizeArrayList().get(0) : new ProductDetailsSize();
        colorList = product.getProductDetailsArrayList().stream().map(productDetails -> productDetails.getProductColor()).distinct().collect(Collectors.toCollection(ArrayList::new));

    }
    private void getUserAccount(){
        Future<?> task = service.submit(()->{
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String email = sharedPreferences.getString("userEmail","");
            userAccount = UserAccountHandler.getUserAccountByEmail(email);
        });
        try {
            task.get();

        }catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        getUserAccount();
        getBundleIntent();
        addEvent();
        bindingData(currentDetails);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (service != null && !service.isShutdown()) {
            service.shutdown();
            try {
                if (!service.awaitTermination(60, TimeUnit.SECONDS)) {
                    service.shutdownNow();
                }
            } catch (InterruptedException e) {
                service.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    @SuppressLint("ResourceType")
    private void bindingData(ProductDetails curr) {
        loadRecycleViewImgSlider(curr);
        loadListViewReviews(curr);
        tvProductName.setText(product.getProduct_name());

        if (curr.getSale_price().compareTo(BigDecimal.ZERO) != 0) {
            tvOldPrice.setVisibility(View.VISIBLE);
            tvOldPrice.setText("$" + product.getBase_price().toString());
            tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvPrice.setText("$" + curr.getSale_price().toString());
        } else {
            tvOldPrice.setVisibility(View.GONE);
            tvPrice.setText("$" + product.getBase_price().toString());

        }
        bgColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(curr.getProductColor().getProduct_color_value().trim())));
        tvDescription.setText(curr.getDescription());

        quantity = 1;
        tvQuantity.setText(String.valueOf(quantity));
        curr.setFavorite(false);
        if (curr.getSale_price().compareTo(BigDecimal.ZERO) != 0) {
            tvTotalPrice.setText("$" + String.valueOf(curr.getSale_price().toString()));
            productPrice = curr.getSale_price();
        } else {
            tvTotalPrice.setText("$" + String.valueOf(product.getBase_price().toString()));
            productPrice = product.getBase_price();
        }
        tvSize.setText(curr.getSizeArrayList() != null ? curr.getSizeArrayList().get(0).getSize().getSize_name() : "None");
        //Size
        int totalStockOfSize = curr.getSizeArrayList() != null ? curr.getSizeArrayList().stream().mapToInt(ProductDetailsSize::getStock).sum() : 0;
        if (totalStockOfSize == 0 || curr.getSizeArrayList() == null) {
            btnOutOfStock.setVisibility(View.VISIBLE);
            btnQuantity.setVisibility(View.GONE);
            btnSize.setVisibility(View.GONE);
        } else {
            btnOutOfStock.setVisibility(View.GONE);
            btnQuantity.setVisibility(View.VISIBLE);
            btnSize.setVisibility(View.VISIBLE);
        }

        tvTotalReviews.setText(String.valueOf(curr.getProductReviews() != null ? curr.getProductReviews().size() : 0) +" Reviews");
        tvRatingPoints.setText(curr.getAverageRatings().toString()+" Ratings");
        isFavorite(curr);
    }
    private void isFavorite(ProductDetails curr){
        service.submit(()->{
            boolean result = WishlistHandler.checkProductDetailsExistsInWishlistByUserID(curr.getProduct_details_id(),userAccount.getUserId());
            runOnUiThread(()->{
                if (result) {
                    btnFavorite.setImageResource(R.drawable.red_heart);
                    currentDetails.setFavorite(true);;
                }
                else
                {
                    int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                    int bgResource;
                    if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                        bgResource = R.drawable.white_heart;
                    } else {
                        bgResource = R.drawable.black_heart;
                    }

                    btnFavorite.setImageResource(bgResource);
                    currentDetails.setFavorite(false);

                }
            });

        });

    }
    private void addControl() {

        //TextView
        tvProductName = findViewById(R.id.tvProductName);
        tvPrice = findViewById(R.id.tvPrice);
        tvOldPrice = findViewById(R.id.tvOldPrice);
        tvDescription = findViewById(R.id.tvDescription);
        tvSize = findViewById(R.id.txtSize);
        tvTotalReviews = findViewById(R.id.tvTotalReviews);
        tvRatingPoints = findViewById(R.id.tvRatingPoints);

        //RecycleView
        recycleImgSlider = findViewById(R.id.recyclerProductImgSlider);
        recycleReviews = findViewById(R.id.recyclerRating);

        //Btn
        btnColor = findViewById(R.id.btnColor);
        btnSize = findViewById(R.id.btnSize);
        bgColor = findViewById(R.id.bgColor);
        if (btnColor != null) {
            btnColor.setOnClickListener(v -> showColorOverlay("Color"));
        }
        imgBack = findViewById(R.id.imgBack);
        if (btnSize != null) {
            btnSize.setOnClickListener(v -> showSizeOverlay("Size"));
        }


        //Quantity
        btnQuantity = findViewById(R.id.btnQuantity);
        btnIncrease = findViewById(R.id.btnIncreaseQuantity);
        btnDecrease = findViewById(R.id.btnDecreaseQuantity);
        tvQuantity = findViewById(R.id.txtQuantityValue);

        //btnAddToBag
        btnAddToBag = findViewById(R.id.btnAddToBag);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        //btnFavorite
        btnFavorite = findViewById(R.id.imgFavorite);

        //btnOutOfStock
        btnOutOfStock = findViewById(R.id.btnOutOfStock);
    }

    public void addEvent() {
        imgBack.setOnClickListener(v -> {
            finish();
        });

        btnIncrease.setOnClickListener(v -> {
            quantity += 1;
            tvQuantity.setText(String.valueOf(quantity));
            totalPrice = productPrice.multiply(BigDecimal.valueOf(quantity));
            tvTotalPrice.setText("$" + totalPrice.toString());
        });
        btnDecrease.setOnClickListener(v -> {
            if (quantity != 1) {
                quantity -= 1;
                tvQuantity.setText(String.valueOf(quantity));
                totalPrice = productPrice.multiply(BigDecimal.valueOf(quantity));
                tvTotalPrice.setText("$" + totalPrice.toString());

            }
        });

        btnFavorite.setOnClickListener(v -> {
            showAddToWLOverlay();
        });

        btnAddToBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSize == null) {
                    Toast.makeText(ProductPage.this, "Please select a size.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Kiểm tra số lượng còn lại
                int stock = currentSize.getStock();
                if (stock <= 0) {
                    Toast.makeText(ProductPage.this, "Product out of stock.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Kiểm tra số lượng yêu cầu có vượt quá số lượng còn lại không
                if (quantity > stock) {
                    Toast.makeText(ProductPage.this, "Not enough stock available.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bag bag = new Bag();
                bag.setUser_id(userAccount.getUserId());
                bag.setProduct_details_size_id(currentSize.getProductDetailsSizeID());
                bag.setAmount(quantity);
                try {
                    // Lưu vào cơ sở dữ liệu
                    boolean isSuccess = BagHandler.addBag(bag);
                    if (isSuccess) {
                        Toast.makeText(ProductPage.this, "Product added to bag successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Xử lý khi không thể thêm vào giỏ hàng (Ví dụ: do không đủ số lượng trong kho)
                        Toast.makeText(ProductPage.this, "Failed to add product to bag. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    // Xử lý lỗi ngoại lệ (Ví dụ: lỗi kết nối cơ sở dữ liệu)
                    Toast.makeText(ProductPage.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateStockStatus() {
        if (currentSize == null) {
            btnOutOfStock.setVisibility(View.VISIBLE);
            btnQuantity.setVisibility(View.GONE);
            btnAddToBag.setEnabled(false);
        } else {
            int stock = currentSize.getStock();
            if (stock <= 0) {
                btnOutOfStock.setVisibility(View.VISIBLE);
                btnQuantity.setVisibility(View.GONE);
                btnAddToBag.setEnabled(false);
            } else {
                btnOutOfStock.setVisibility(View.GONE);
                btnQuantity.setVisibility(View.VISIBLE);
                btnAddToBag.setEnabled(true);
            }
        }
    }
    private void loadRecycleViewImgSlider(ProductDetails productDetails) {
        runOnUiThread(() -> {
            imgList = productDetails.getImgDetailsArrayList();
            if (!imgList.isEmpty()) {
                imgSliderApdater = new RecycleProductImageAdapter(imgList, this);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                recycleImgSlider.setLayoutManager(layoutManager);
                recycleImgSlider.setAdapter(imgSliderApdater);
            }
        });


    }

    private void loadListViewReviews(ProductDetails productDetails) {
        reviewsList = productDetails.getProductReviews();
        reviewAdapter = new RecycleReviewAdapter(reviewsList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recycleReviews.setLayoutManager(layoutManager);
        recycleReviews.setAdapter(reviewAdapter);
    }

    private void showColorOverlay(String type) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.color_overlay, null);
        bottomSheetDialog.setContentView(dialogView);
        TextView overlayTitle = dialogView.findViewById(R.id.overlay_title);
        overlayTitle.setText(type);
        ImageButton btnClose = dialogView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
        RecyclerView recyclerColor = dialogView.findViewById(R.id.recyclerColor);
        RecylerColorAdapter recylerColorAdapter = new RecylerColorAdapter(colorList, ProductPage.this, currentDetails.getProductColor().getProduct_color_name(), new RecylerColorAdapter.OnColorsSelectedListener() {
            @Override
            public void onColorSelected(String colorName) {
                currentDetails = product.getProductDetailsArrayList().stream()
                        .filter(productDetails -> productDetails.getProductColor().getProduct_color_name().equalsIgnoreCase(colorName)) // Lọc theo tên màu
                        .findFirst()
                        .orElse(null);
                bindingData(currentDetails);
                updateStockStatus();
                bottomSheetDialog.dismiss();
            }
        });
        recyclerColor.setLayoutManager(new LinearLayoutManager(bottomSheetDialog.getContext(), RecyclerView.VERTICAL, false));
        recyclerColor.setAdapter(recylerColorAdapter);
        bottomSheetDialog.show();
    }

    private void showSizeOverlay(String type) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.size_overlay, null);
        bottomSheetDialog.setContentView(dialogView);

        TextView overlayTitle = dialogView.findViewById(R.id.overlay_title);
        overlayTitle.setText(type);

        ImageButton btnClose = dialogView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        RecyclerView recyclerSize = dialogView.findViewById(R.id.recy_size);
        RecycleSizeAdapter recycleSizeAdapter = new RecycleSizeAdapter(currentDetails.getSizeArrayList() != null ? currentDetails.getSizeArrayList() : new ArrayList<>(), ProductPage.this, new RecycleSizeAdapter.OnSizeSelectedListener() {
            public void onSizeSelected(ProductDetailsSize size) {
                currentSize = size;
                tvSize.setText(currentSize.getSize().getSize_name());
                int totalStockOfSize = currentSize.getStock();
                updateStockStatus(); // Cập nhật trạng thái còn hàng
                bottomSheetDialog.dismiss();
            }
        }, currentSize);

        recyclerSize.setLayoutManager(new LinearLayoutManager(bottomSheetDialog.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerSize.setAdapter(recycleSizeAdapter);
        bottomSheetDialog.show();

    }

    private void showReviewOverlay() {
        View view = LayoutInflater.from(ProductPage.this).inflate(R.layout.review_overlay, null);

        final Dialog dialog = new Dialog(ProductPage.this);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        if (window == null)
        {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowattri = window.getAttributes();
        windowattri.gravity = Gravity.BOTTOM;
        window.setAttributes(windowattri);

        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        EditText edt_review = dialog.findViewById(R.id.edt_review);

        ImageButton btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showAddToWLOverlay() {
    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this); // Use 'this' for context
    LayoutInflater inflater = this.getLayoutInflater(); // Use 'this' for context
    View dialogView = inflater.inflate(R.layout.wishlist_overlay, null);
    bottomSheetDialog.setContentView(dialogView);

    View parentLayout = (View) dialogView.getParent();
    BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parentLayout);
    behavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO, true);
    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    TextView tv_cancel;
    Button btnNewWL, btnDone;
    RecyclerView recyWL;

    tv_cancel = dialogView.findViewById(R.id.tv_cancel);
    btnNewWL = dialogView.findViewById(R.id.btnNewWL);
    btnDone = dialogView.findViewById(R.id.btnDone);
    recyWL = dialogView.findViewById(R.id.recy_wl);
    ExecutorService loadingWishlist = Executors.newSingleThreadExecutor();
    loadingWishlist.execute(() -> {
        UserAccount currentUser = UserAccountManager.getInstance().getCurrentUserAccount();
        if (currentUser != null) {
            ArrayList<Wishlist> wishlists = WishlistHandler.getWishListByUserID(currentUser.getUserId());
            runOnUiThread(() -> {
                WishListAdapter wishListAdapter = new WishListAdapter(this, wishlists, currentDetails.getProduct_details_id()); // Use 'this' for context
                recyWL.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)); // Use 'this' for context
                recyWL.setAdapter(wishListAdapter);
            });
        } else {
            runOnUiThread(() -> {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show(); // Use 'this' for context
            });
        }
    });
    btnDone.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            bottomSheetDialog.dismiss();
            isFavorite(currentDetails);
        }
    });

    tv_cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadingWishlist.shutdown();
            bottomSheetDialog.dismiss();
        }
    });
    btnNewWL.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showAddWLOverlay(() -> {
                loadingWishlist.execute(() -> {
                    UserAccount currentUser = UserAccountManager.getInstance().getCurrentUserAccount();
                    if (currentUser != null) {
                        ArrayList<Wishlist> updatedWishlists = WishlistHandler.getWishListByUserID(currentUser.getUserId());
                        runOnUiThread(() -> {
                            WishListAdapter updatedWishListAdapter = new WishListAdapter(ProductPage.this, updatedWishlists, currentDetails.getProduct_details_id());
                            recyWL.setAdapter(updatedWishListAdapter);                            recyWL.setAdapter(updatedWishListAdapter);
                        });
                    } else {
                        runOnUiThread(() -> {
                        });
                    }
                });
            });
        }
    });
    bottomSheetDialog.show();
}

    private void showAddWLOverlay(Runnable onDismissCallback) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.addwishlist_overlay, null);
        bottomSheetDialog.setContentView(dialogView);

        ImageButton btn_close;
        EditText edtNameWL;
        Button btnCreate;

        btn_close = dialogView.findViewById(R.id.btn_close);
        edtNameWL = dialogView.findViewById(R.id.edtNameWL);
        btnCreate = dialogView.findViewById(R.id.btnCreate);
        ExecutorService insertWishlist = Executors.newSingleThreadExecutor();
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtNameWL.getText().length() > 0){

                    insertWishlist.execute(()->{
                        boolean result = WishlistHandler.addNewWishlist(userAccount.getUserId(),edtNameWL.getText().toString());
                        runOnUiThread(()->{
                            if(result){
                                Toast.makeText(getApplicationContext(),"New Wishlist added Successfully",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(),"New Wishlist added Failed",Toast.LENGTH_SHORT).show();
                            }
                            if (onDismissCallback != null) {
                                onDismissCallback.run();
                            }
                        });
                    });

                }else {
                    if (onDismissCallback != null) {
                        onDismissCallback.run();
                    }
                }
                insertWishlist.shutdown();
                bottomSheetDialog.dismiss();

            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertWishlist.shutdown();
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

}