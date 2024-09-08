package com.example.ecommercemobileapp2hand.Views.Search;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigDecimal;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommercemobileapp2hand.Models.ProductDetails;
import com.example.ecommercemobileapp2hand.Controllers.CategoriesHandler;
import com.example.ecommercemobileapp2hand.Controllers.ProductHandler;
import com.example.ecommercemobileapp2hand.Models.FakeModels.Category;
import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.Models.ProductCategory;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.CategoriesAdapter;
import com.example.ecommercemobileapp2hand.Views.Adapters.GenderAdapter;
import com.example.ecommercemobileapp2hand.Views.Adapters.ProductCardAdapter;
import com.example.ecommercemobileapp2hand.Views.Adapters.SortByAdapter;
import com.example.ecommercemobileapp2hand.Views.App;
import com.example.ecommercemobileapp2hand.Views.Homepage.HomeFragment;
import com.example.ecommercemobileapp2hand.Views.MainActivity;
import com.example.ecommercemobileapp2hand.databinding.ActivitySearchBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity {
    private ExecutorService service = Executors.newCachedThreadPool();

    private ImageView imgBack;
    private SearchView searchView;
    private ArrayList<com.example.ecommercemobileapp2hand.Models.Product> lstPro;
    private ProductCardAdapter proAdapter;
    private RecyclerView recyViewSearchPro;
    private RecyclerView recyViewCateSearch;
    private LinearLayout linearLayoutSearch;
    private ScrollView scrollViewPro;
    private ArrayList<ProductCategory> categoryList;
    private CategoriesAdapter categoriesAdapter;
    private TextView textViewTitle, tvResult;
    private LinearLayout categoryContainer, productContainer, layoutFilter;
    private AppCompatButton filter, btnDeals, btnGender, btnSortBy, btnPrice;
    private String genderFilter = "";
    private ActivitySearchBinding binding;

    private LocalDateTime now = LocalDateTime.now();
    private Boolean sortByPriceAsc=null;
    private LocalDateTime  thirtyDaysAgo=LocalDateTime.MIN;

    private String onSale="";
    private String price="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addControls();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        addEvent();
        loadRecycleViewCategories();
        loadListPro();
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
    private void loadListPro() {
        service.execute(() -> {
            lstPro = ProductHandler.getData();
            runOnUiThread(()->{
                if(lstPro!=null && !lstPro.isEmpty()){
                    proAdapter = new ProductCardAdapter(lstPro, SearchActivity.this);
                    recyViewSearchPro.setLayoutManager(new GridLayoutManager(this, 2));
                    recyViewSearchPro.setItemAnimator(new DefaultItemAnimator());
                    recyViewSearchPro.setAdapter(proAdapter);
                }
            });
        });
    }

    void addControls() {
        imgBack = binding.btnBack;
        searchView = binding.searchView;
        searchView.clearFocus();
        layoutFilter = binding.layoutFilter;
        recyViewSearchPro = binding.recyProductSearch;

        recyViewCateSearch = binding.recyclerViewCategoriesSearch;
        linearLayoutSearch = binding.linearLayoutSearch;
        textViewTitle = binding.titleSearch;
        tvResult = binding.tvResult;
        //Container
        categoryContainer = binding.categoryContainer;
        productContainer = binding.productContainer;

        //filter btn
        filter = binding.filter;
        btnSortBy = binding.btnSortBy;
        btnPrice = binding.btnPrice;
        btnGender = binding.btnGender;
        btnDeals = binding.btnDeals;
    }

    private void loadRecycleViewCategories() {
        service.execute(() -> {
            categoryList = new ArrayList<>();
            categoryList = CategoriesHandler.getData();
            runOnUiThread(() -> {
                if (categoryList != null && !categoryList.isEmpty()) {
                    categoriesAdapter = new CategoriesAdapter(categoryList, this, R.layout.item_category);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                    recyViewCateSearch.setLayoutManager(layoutManager);
                    recyViewCateSearch.setAdapter(categoriesAdapter);
                }

            });
        });

    }

    void addEvent() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("SearchQuery", "Current query: " + newText);
                filterList(newText,"",null,LocalDateTime.MIN,"","");
                return false;
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortByOverlay("Sort by");
            }
        });
        btnGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortByOverlay("Gender");
            }
        });
        btnDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortByOverlay("Deals");
            }
        });
        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortByOverlay("Price");
            }
        });
    }

    void filterList(String text, String genderFilter, Boolean sortByPriceAsc, LocalDateTime thirtyDaysAgo, String onSale, String price) {
        ArrayList<Product> filterList = new ArrayList<>();
        filterList = lstPro.stream()
                .filter(product -> product.getProduct_name().toLowerCase().contains(text.toLowerCase()))
                .filter(product -> genderFilter.isEmpty() || product.getProductObject().getObject_name().equalsIgnoreCase(genderFilter))
                .filter(product -> onSale.isEmpty() || product.getProductDetailsArrayList()
                        .stream()
                        .anyMatch(productDetails -> productDetails.getSale_price().compareTo(BigDecimal.ZERO)!=0))
                .filter(product -> product.getCreated_at().isAfter(thirtyDaysAgo))
                .sorted(sortByPriceAsc != null
                        ? (sortByPriceAsc ? Comparator.comparing(Product::getBase_price)
                        : Comparator.comparing(Product::getBase_price).reversed())
                        : Comparator.comparingInt(Product::getProduct_id))

                .collect(Collectors.toCollection(ArrayList::new));


        updateUIWithFilterResults(text, filterList);

    }
    private void updateUIWithFilterResults(String text, ArrayList<Product> filterList) {
        runOnUiThread(() -> {
            ArrayList<Product> updatedFilterList = new ArrayList<>(filterList); // Create a new list to hold filtered results

            if (!price.isEmpty()) {
                if (price.equals("Min")) {
                    Optional<BigDecimal> minSale = updatedFilterList.stream()
                            .flatMap(product -> product.getProductDetailsArrayList().stream())
                            .map(ProductDetails::getSale_price)
                            .filter(Objects::nonNull)
                            .min(Comparator.naturalOrder());

                    Optional<BigDecimal> minNotSale = updatedFilterList.stream()
                            .filter(product -> product.getBase_price().compareTo(minSale.get()) <= 0)
                            .map(Product::getBase_price)
                            .filter(Objects::nonNull)
                            .min(Comparator.naturalOrder());

                    if (minNotSale.isPresent()) {
                        BigDecimal minValue = minNotSale.get();
                        updatedFilterList = updatedFilterList.stream()
                                .filter(product -> product.getBase_price().compareTo(minValue) <= 0)
                                .collect(Collectors.toCollection(ArrayList::new));
                    } else {
                        BigDecimal minValue = minSale.get();
                        updatedFilterList = updatedFilterList.stream()
                                .filter(product -> product.getProductDetailsArrayList()
                                        .stream()
                                        .anyMatch(productDetails -> productDetails.getSale_price() != null && productDetails.getSale_price().compareTo(minValue) <= 0))
                                .collect(Collectors.toCollection(ArrayList::new));
                    }
                } else if (price.equals("Max")) {
                    Optional<BigDecimal> maxSale = updatedFilterList.stream()
                            .flatMap(product -> product.getProductDetailsArrayList().stream())
                            .map(ProductDetails::getSale_price)
                            .filter(Objects::nonNull)
                            .max(Comparator.naturalOrder());

                    Optional<BigDecimal> maxNotSale = updatedFilterList.stream()
                            .filter(product -> product.getBase_price().compareTo(maxSale.get()) >= 0 && product.getProductDetailsArrayList().stream().anyMatch(productDetails -> productDetails.getSale_price() == null))
                            .map(Product::getBase_price)
                            .filter(Objects::nonNull)
                            .max(Comparator.naturalOrder());

                    if (maxNotSale.isPresent()) {
                        BigDecimal maxValue = maxNotSale.get();
                        updatedFilterList = updatedFilterList.stream()
                                .filter(product -> product.getBase_price().compareTo(maxValue) >= 0)
                                .collect(Collectors.toCollection(ArrayList::new));
                    } else {
                        BigDecimal maxValue = maxSale.get();
                        updatedFilterList = updatedFilterList.stream()
                                .filter(product -> product.getProductDetailsArrayList()
                                        .stream()
                                        .anyMatch(productDetails -> productDetails.getSale_price() != null && productDetails.getSale_price().compareTo(maxValue) >= 0))
                                .collect(Collectors.toCollection(ArrayList::new));
                    }
                }
            }

            // Update UI based on updatedFilterList
            if (text.isEmpty()) {
                categoryContainer.setVisibility(View.VISIBLE);
                linearLayoutSearch.setVisibility(View.GONE);
                productContainer.setVisibility(View.GONE);
            } else if (updatedFilterList.isEmpty()) {
                linearLayoutSearch.setVisibility(View.VISIBLE);
                categoryContainer.setVisibility(View.GONE);
                productContainer.setVisibility(View.GONE);
            } else {
                productContainer.setVisibility(View.VISIBLE);
                categoryContainer.setVisibility(View.GONE);
                linearLayoutSearch.setVisibility(View.GONE);
                if (proAdapter == null) {
                    proAdapter = new ProductCardAdapter(updatedFilterList, SearchActivity.this);
                    recyViewSearchPro.setLayoutManager(new GridLayoutManager(this, 2));
                    recyViewSearchPro.setItemAnimator(new DefaultItemAnimator());
                    recyViewSearchPro.setAdapter(proAdapter);
                } else {
                    proAdapter.setFilteredList(updatedFilterList);
                    proAdapter.notifyDataSetChanged();
                }
                tvResult.setText("" + updatedFilterList.size() + " Result Found");
            }
        });
    }
    private void showSortByOverlay(String type) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.sortby_overlay, null);
        bottomSheetDialog.setContentView(dialogView);

        TextView overlayTitle = dialogView.findViewById(R.id.overlay_title);
        overlayTitle.setText(type);

        ImageButton btnClose = dialogView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        RecyclerView recyleSortBy = dialogView.findViewById(R.id.recy_overlay);
        SortByAdapter sortByAdapter = getSortByAdapter(type, bottomSheetDialog);
        recyleSortBy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyleSortBy.setItemAnimator(new DefaultItemAnimator());
        recyleSortBy.setAdapter(sortByAdapter);
        bottomSheetDialog.show();
    }

    private @NonNull SortByAdapter getSortByAdapter(String type, BottomSheetDialog dialog) {
        ArrayList<String> sortByArr = getStrings(type);

        SortByAdapter sortByAdapter = new SortByAdapter(sortByArr, getApplicationContext(), new SortByAdapter.OnSortBySelectedListener() {
            @Override
            public void onSortBySelected(String selectedSortBy) {
                //Add filter function
                if (type.contains("Gender")) {
                    if (selectedSortBy.equalsIgnoreCase("Men")) {
                        genderFilter = "Men";
                    } else if (selectedSortBy.equalsIgnoreCase("Women")) {
                        genderFilter = "Women";
                    }
                    btnGender.setText(genderFilter);
                    dialog.dismiss();
                }
                else if(type.contains("Sort by")){
                    if (selectedSortBy.equalsIgnoreCase("Lowest-Highest Price")) {
                        Toast.makeText(getApplicationContext(),"low to high price", Toast.LENGTH_SHORT).show();
                        sortByPriceAsc = true;
                        btnSortBy.setText("Lowest-Highest Price");

                    } else if (selectedSortBy.equalsIgnoreCase("Highest-Lowest Price")) {
                        Toast.makeText(getApplicationContext(),"high to low price", Toast.LENGTH_SHORT).show();
                        sortByPriceAsc = false;

                        btnSortBy.setText("Highest-Lowest Price");
                    }
                    else if (selectedSortBy.equalsIgnoreCase("Newest")){
                        Toast.makeText(getApplicationContext(),"newest", Toast.LENGTH_SHORT).show();
                        sortByPriceAsc = null;
                        thirtyDaysAgo=now.minus(30, ChronoUnit.DAYS);
                        btnSortBy.setText("Newest");
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"rec", Toast.LENGTH_SHORT).show();
                        sortByPriceAsc = null;

                        btnSortBy.setText("Recommended");
                    }
                    dialog.dismiss();
                }else if (type.contains("Deals")) {
                    if (selectedSortBy.equalsIgnoreCase("On sale")){
                        onSale="On Sale";
                        btnDeals.setText(onSale);
                    }
                    dialog.dismiss();
                } else if (type.contains("Price")) {
                    if (selectedSortBy.equalsIgnoreCase("Min")){
                        price="Min";
                    }
                    else if (selectedSortBy.equalsIgnoreCase("Max")){
                        price="Max";
                    }
                    btnPrice.setText(price);
                    dialog.dismiss();
                }
                filterList(searchView.getQuery().toString(), genderFilter,sortByPriceAsc,thirtyDaysAgo,onSale,price);
            }
        }, genderFilter);
        dialog.findViewById(R.id.btn_clear_overlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.contains("Deals")){
                    onSale="";
                }else if (type.contains("Price")){
                    price="";
                }else if (type.contains("Gender")){
                    genderFilter="";
                }
                filterList(searchView.getQuery().toString(), genderFilter,sortByPriceAsc,thirtyDaysAgo,onSale,price);
            }
        });
        return sortByAdapter;
    }

    private static @NonNull ArrayList<String> getStrings(String type) {
        ArrayList<String> sortByArr = new ArrayList<>();
        if (type.contains("Sort by")) {
            sortByArr.add(0, "Recommended");
            sortByArr.add(1, "Newest");
            sortByArr.add(2, "Lowest-Highest Price");
            sortByArr.add(3, "Highest-Lowest Price");
        } else if (type.contains("Gender")) {
            sortByArr.add(0, "Men");
            sortByArr.add(1, "Women");
        } else if (type.contains("Deals")) {
            sortByArr.add(0, "On sale");
        } else {
            sortByArr.add(0, "Min");
            sortByArr.add(1, "Max");
        }
        return sortByArr;
    }
}