package com.example.ecommercemobileapp2hand.Views.Search;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.ecommercemobileapp2hand.Controllers.ProductObjectHandler;
import com.example.ecommercemobileapp2hand.Models.ProductDetails;
import com.example.ecommercemobileapp2hand.Controllers.CategoriesHandler;
import com.example.ecommercemobileapp2hand.Controllers.ProductHandler;
import com.example.ecommercemobileapp2hand.Models.FakeModels.Category;
import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.Models.ProductCategory;
import com.example.ecommercemobileapp2hand.Models.ProductObject;
import com.example.ecommercemobileapp2hand.Models.Singleton.ProductManager;
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
    private Boolean sortByPriceAsc = null;
    private LocalDateTime thirtyDaysAgo = LocalDateTime.MIN;
    private ArrayList<Product> originalProductList;
    boolean filterChangedGender = false;
    boolean filterChangeSortBy = false;
    boolean filterChangedPrice = false;
    boolean filterChangeDeal = false;
    private String onSale = "";
    private String price = "";
    private TextView btn_clear_overlay;
    private int numberFilter = 0;
    private GenderAdapter genderAdapter;
    private Boolean isFreeShip=null;
    private static final long DEBOUNCE_DELAY = 300; // 300ms delay
    private Handler searchHandler = new Handler();
    private Runnable searchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_search), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addControls();


    }

    @Override
    protected void onResume() {
        super.onResume();
        addEvent();
        loadRecycleViewCategories();
        loadListPro();
    }

    private void loadListPro() {
        ProductHandler.getData(new ProductHandler.Callback<ArrayList<Product>>() {
            @Override
            public void onResult(ArrayList<Product> result) {
                lstPro = result;
                originalProductList = new ArrayList<>(lstPro);
                runOnUiThread(() -> {

                    if (lstPro != null && !lstPro.isEmpty()) {
                        proAdapter = new ProductCardAdapter(lstPro, SearchActivity.this);
                        recyViewSearchPro.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
                        recyViewSearchPro.setItemAnimator(new DefaultItemAnimator());
                        recyViewSearchPro.setAdapter(proAdapter);
                    }
                });
            }
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
        CategoriesHandler.getData(new CategoriesHandler.Callback<ArrayList<ProductCategory>>() {
            @Override
            public void onResult(ArrayList<ProductCategory> result) {
                categoryList = result;
                runOnUiThread(() -> {
                    if (categoryList != null && !categoryList.isEmpty()) {
                        categoriesAdapter = new CategoriesAdapter(categoryList, SearchActivity.this, R.layout.item_category);
                        recyViewCateSearch.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                        recyViewCateSearch.setAdapter(categoriesAdapter);
                    }
                });
            }
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

                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                searchRunnable = () -> filterList(newText, genderFilter, sortByPriceAsc, thirtyDaysAgo, onSale, price, isFreeShip);
                searchHandler.postDelayed(searchRunnable, DEBOUNCE_DELAY);
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
                showSortByOverlay();
            }
        });
        btnGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderOverlay();
            }
        });
        btnDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDealsOverlay();
            }
        });
        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPriceOverlay();
            }
        });
    }

    void filterList(String text, String genderFilter, Boolean sortByPriceAsc, LocalDateTime thirtyDaysAgo, String onSale, String price,Boolean isFreeshipFilter) {
        service.execute(()->{
            // Reset lstPro to the original list before filtering
            ArrayList<Product> filterList = new ArrayList<>(originalProductList);

            // Apply filtering logic as before
            filterList = filterList.stream().filter(product -> genderFilter.isEmpty() || product.getProductObject().getObject_name().equalsIgnoreCase(genderFilter))
                    .filter(product -> onSale.isEmpty() || product.getProductDetailsArrayList()
                            .stream()
                            .anyMatch(productDetails -> productDetails.getSale_price().compareTo(BigDecimal.ZERO) != 0))
                    .filter(product -> isFreeshipFilter == null || product.getIsFreeship() == isFreeshipFilter)
                    .filter(product -> product.getCreated_at().isAfter(thirtyDaysAgo))
                    .sorted((p1, p2) -> {
                        BigDecimal salePrice1 = getMinSalePrice(p1);
                        BigDecimal salePrice2 = getMinSalePrice(p2);

                        if (sortByPriceAsc != null) {
                            return sortByPriceAsc ? salePrice1.compareTo(salePrice2) : salePrice2.compareTo(salePrice1);
                        } else {
                            return Integer.compare(p1.getProduct_id(), p2.getProduct_id());
                        }
                    })
                    .collect(Collectors.toCollection(ArrayList::new));

            ArrayList<Product> finalFilterList = filterList;
            runOnUiThread(()->{
                updateUIWithFilterResults(text, finalFilterList);
            });

            // Clear any potential adapter caching (if applicable)
        });



    }
    private BigDecimal getMinSalePrice(Product product) {
        if (product.getProductDetailsArrayList() == null || product.getProductDetailsArrayList().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return product.getProductDetailsArrayList().stream()
                .filter(details -> details.getSale_price() != null)
                .map(ProductDetails::getSale_price)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }
    private void updateUIWithFilterResults(String text, ArrayList<Product> filterList) {
        ArrayList<Product> updatedFilterList = new ArrayList<>(filterList); // Create a new list to hold filtered results

        if (!price.isEmpty()) {
            if (price.equals("Min")) {
                Optional<BigDecimal> minSale = updatedFilterList.stream()
                        .flatMap(product -> product.getProductDetailsArrayList().stream())
                        .map(ProductDetails::getSale_price)
                        .filter(salePrice -> salePrice.compareTo(BigDecimal.ZERO) != 0)
                        .min(Comparator.naturalOrder());

                if (minSale.isPresent()) {
                    BigDecimal minValue = minSale.get();
                    Optional<BigDecimal> minNotSale = updatedFilterList.stream()
                            .filter(product -> product.getBase_price().compareTo(minValue) <= 0)
                            .map(Product::getBase_price)
                            .filter(Objects::nonNull)
                            .min(Comparator.naturalOrder());

                    if (minNotSale.isPresent()) {
                        BigDecimal minNotSaleValue = minNotSale.get();
                        updatedFilterList = updatedFilterList.stream()
                                .filter(product -> product.getBase_price().compareTo(minNotSaleValue) <= 0)
                                .collect(Collectors.toCollection(ArrayList::new));
                    } else {
                        updatedFilterList = updatedFilterList.stream()
                                .filter(product -> product.getProductDetailsArrayList()
                                        .stream()
                                        .anyMatch(productDetails -> productDetails.getSale_price().compareTo(minValue) <= 0))
                                .collect(Collectors.toCollection(ArrayList::new));
                    }
                }
            } else if (price.equals("Max")) {
                Optional<BigDecimal> maxSale = updatedFilterList.stream()
                        .flatMap(product -> product.getProductDetailsArrayList().stream())
                        .map(ProductDetails::getSale_price)
                        .filter(salePrice -> salePrice.compareTo(BigDecimal.ZERO) != 0)
                        .max(Comparator.naturalOrder());

                if (maxSale.isPresent()) {
                    BigDecimal maxValue = maxSale.get();
                    Optional<BigDecimal> maxNotSale = updatedFilterList.stream()
                            .filter(product -> product.getBase_price().compareTo(maxValue) >= 0 && product.getProductDetailsArrayList().stream().anyMatch(productDetails -> productDetails.getSale_price().compareTo(BigDecimal.ZERO) == 0))
                            .map(Product::getBase_price)
                            .filter(Objects::nonNull)
                            .max(Comparator.naturalOrder());

                    if (maxNotSale.isPresent()) {
                        BigDecimal maxNotSaleValue = maxNotSale.get();
                        updatedFilterList = updatedFilterList.stream()
                                .filter(product -> product.getBase_price().compareTo(maxNotSaleValue) >= 0)
                                .collect(Collectors.toCollection(ArrayList::new));
                    } else {
                        updatedFilterList = updatedFilterList.stream()
                                .filter(product -> product.getProductDetailsArrayList()
                                        .stream()
                                        .anyMatch(productDetails -> productDetails.getSale_price().compareTo(maxValue) >= 0))
                                .collect(Collectors.toCollection(ArrayList::new));
                    }
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
            tvResult.setText(updatedFilterList.size() + " Result Found");
        }
    }

//    private void showSortByOverlay(String type) {
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView =inflater.inflate(R.layout.sortby_overlay, null);
//
//        bottomSheetDialog.setContentView(dialogView);
//
//        TextView overlayTitle = dialogView.findViewById(R.id.overlay_title);
//        overlayTitle.setText(type);
//
//        ImageButton btnClose = dialogView.findViewById(R.id.btn_close);
//        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
//
//        RecyclerView recyleSortBy = dialogView.findViewById(R.id.recy_overlay);
//        SortByAdapter sortByAdapter = getSortByAdapter(type, bottomSheetDialog);
//        recyleSortBy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        recyleSortBy.setItemAnimator(new DefaultItemAnimator());
//        recyleSortBy.setAdapter(sortByAdapter);
//        bottomSheetDialog.show();
//    }

//    private @NonNull SortByAdapter getSortByAdapter(String type, BottomSheetDialog dialog) {
//        ArrayList<String> sortByArr=getStrings(type);
//        SortByAdapter sortByAdapter = new SortByAdapter(sortByArr, getApplicationContext(), new SortByAdapter.OnSortBySelectedListener() {
//            @Override
//            public void onSortBySelected(String selectedSortBy) {
//                //Add filter function
//                if (type.contains("Gender")) {
////                    if (!filterChangedGender) {
////                        numberFilter += 1;
////                        filter.setText(String.valueOf(numberFilter));
////                    }
////                    if (selectedSortBy.equalsIgnoreCase("Men")) {
////                        genderFilter = "Men";
////                        filterChangedGender = true;
////                    } else {
////                        genderFilter = "Women";
////                        filterChangedGender = true;
////                    }
////                    btnGender.setText(genderFilter);
////                    dialog.dismiss();
//                } else if (type.contains("Sort by")) {
//
//                    if (!filterChangeSortBy) {
//                        numberFilter += 1;
//                        filter.setText(String.valueOf(numberFilter));
//
//                    }
//                    if (selectedSortBy.equalsIgnoreCase("Lowest-Highest Price")) {
//                        //Toast.makeText(getApplicationContext(),"low to high price", Toast.LENGTH_SHORT).show();
//                        sortByPriceAsc = true;
//                        btnSortBy.setText("Lowest-Highest Price");
//                        filterChangeSortBy = true;
//                    } else if (selectedSortBy.equalsIgnoreCase("Highest-Lowest Price")) {
//                        //Toast.makeText(getApplicationContext(),"high to low price", Toast.LENGTH_SHORT).show();
//                        sortByPriceAsc = false;
//                        filterChangeSortBy = true;
//                        btnSortBy.setText("Highest-Lowest Price");
//                    } else if (selectedSortBy.equalsIgnoreCase("Newest")) {
//                        //Toast.makeText(getApplicationContext(),"newest", Toast.LENGTH_SHORT).show();
//                        sortByPriceAsc = null;
//                        thirtyDaysAgo = now.minus(30, ChronoUnit.DAYS);
//                        btnSortBy.setText("Newest");
//                        filterChangeSortBy = true;
//                    } else {
//                        //Toast.makeText(getApplicationContext(),"rec", Toast.LENGTH_SHORT).show();
//                        sortByPriceAsc = null;
//                        filterChangeSortBy = true;
//                        btnSortBy.setText("Recommended");
//                    }
//                    dialog.dismiss();
//                } else if (type.contains("Deals")) {
//
//                    if (!filterChangeDeal) {
//                        numberFilter += 1;
//                        filter.setText(String.valueOf(numberFilter));
//
//                    }
//                    if (selectedSortBy.equalsIgnoreCase("On sale")) {
//                        onSale = "On Sale";
//                        btnDeals.setText(onSale);
//                        filterChangeDeal = true;
//                    }
//                    dialog.dismiss();
//                } else if (type.contains("Price")) {
//                    if (!filterChangedPrice) {
//                        numberFilter += 1;
//                        filter.setText(String.valueOf(numberFilter));
//
//                    }
//
//                    if (selectedSortBy.equalsIgnoreCase("Min")) {
//                        price = "Min";
//                        filterChangedPrice = true;
//
//                    } else if (selectedSortBy.equalsIgnoreCase("Max")) {
//                        price = "Max";
//                        filterChangedPrice = true;
//                    }
//                    btnPrice.setText(price);
//                    dialog.dismiss();
//                }
//
//
//                filterList(searchView.getQuery().toString(), genderFilter, sortByPriceAsc, thirtyDaysAgo, onSale, price);
//            }
//        }, genderFilter);
//        dialog.findViewById(R.id.btn_clear_overlay).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (type.contains("Deals")) {
//                    onSale = "";
//                    btnDeals.setText("Deals");
//                    filterChangeDeal = false;
//
//                } else if (type.contains("Price")) {
//                    price = "";
//                    btnPrice.setText("Price");
//                    filterChangedPrice = false;
////                } else if (type.contains("Gender")) {
////                    genderFilter = ""; // Clear gender filter
////                    btnGender.setText("Gender");
////                    filterChangedGender = false;
////                } else if (type.contains("Sort by")) {
//                    sortByPriceAsc = null;
//                    btnSortBy.setText("Sort by");
//                    thirtyDaysAgo = LocalDateTime.MIN;
//                    filterChangeSortBy = false;
//                }
//                if (numberFilter > 0) {
//                    numberFilter -= 1;
//                    filter.setText(String.valueOf(numberFilter));
//                }
//                // Call filterList to update results based on cleared filters
//                filterList(searchView.getQuery().toString(), genderFilter, sortByPriceAsc, thirtyDaysAgo, onSale, price);
//                dialog.dismiss();
//            }
//        });
//        return sortByAdapter;
//    }


//    private static @NonNull ArrayList<String> getStrings(String type) {
//        ArrayList<String> sortByArr = new ArrayList<>();
//        if (type.contains("Sort by")) {
//            sortByArr.add(0, "Recommended");
//            sortByArr.add(1, "Newest");
//            sortByArr.add(2, "Lowest-Highest Price");
//            sortByArr.add(3, "Highest-Lowest Price");
////        } else if (type.contains("Gender")) {
////            sortByArr.add(0, "Men");
////            sortByArr.add(1, "Women");
////
////        } else if (type.contains("Deals")) {
//            sortByArr.add(0, "On sale");
//            sortByArr.add(0, "Free ship");
//        } else {
//            sortByArr.add(0, "Min");
//            sortByArr.add(1, "Max");
//        }
//        return sortByArr;
//    }

    //Tách overlay
    //overlay gender
    private void showGenderOverlay() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.gender_overlay, null);
        bottomSheetDialog.setContentView(dialogView);
        TextView overlayTitle = dialogView.findViewById(R.id.overlay_title);
        overlayTitle.setText("Gender");
        ImageButton btnClose = dialogView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
        RecyclerView recycleGender = dialogView.findViewById(R.id.recy_gender);
        recycleGender.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycleGender.setItemAnimator(new DefaultItemAnimator());
        ProductObjectHandler.getData(new ProductObjectHandler.Callback<ArrayList<ProductObject>>() {
            @Override
            public void onResult(ArrayList<ProductObject> result) {
                ArrayList<ProductObject> lstGender = result;

                // Set up the adapter once data is available
                runOnUiThread(() -> {
                    genderAdapter = new GenderAdapter(lstGender, getApplicationContext(), btnGender.getText().toString(), new GenderAdapter.OnGenderSelectedListener() {
                        @Override
                        public void onGenderSelected(String selectedGender) {
                            btnGender.setText(selectedGender);
                            if (!filterChangedGender) {
                                numberFilter += 1;
                                filter.setText(String.valueOf(numberFilter));
                            }
                            if (selectedGender.equalsIgnoreCase("Men")) {
                                genderFilter = "Men";
                            } else {
                                genderFilter = "Women";
                            }
                            filterChangedGender = true;
                            bottomSheetDialog.dismiss();

                            // Call the filter list with updated data
                            filterList(searchView.getQuery().toString(), genderFilter, sortByPriceAsc, thirtyDaysAgo, onSale, price, isFreeShip);
                        }
                    });
                    recycleGender.setAdapter(genderAdapter);
                    genderAdapter.notifyDataSetChanged();
                });
            }
        });

        bottomSheetDialog.findViewById(R.id.btn_clear_overlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderFilter = "";
                btnGender.setText("Gender");
                filterChangedGender = false;
                if (numberFilter > 0) {
                    numberFilter -= 1;
                    filter.setText(String.valueOf(numberFilter));
                }
                filterList(searchView.getQuery().toString(), genderFilter, sortByPriceAsc, thirtyDaysAgo, onSale, price, isFreeShip);
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    //overlay sortby
    private void showSortByOverlay() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView =inflater.inflate(R.layout.sortby_overlay, null);

        bottomSheetDialog.setContentView(dialogView);

        TextView overlayTitle = dialogView.findViewById(R.id.overlay_title);
        overlayTitle.setText("Sort by");

        ImageButton btnClose = dialogView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        RecyclerView recyleSortBy = dialogView.findViewById(R.id.recy_overlay);
        ArrayList<String> lstSortby=new ArrayList<>();
        lstSortby.add(0, "Recommended");
        lstSortby.add(1, "Newest");
        lstSortby.add(2, "Lowest-Highest Price");
        lstSortby.add(3, "Highest-Lowest Price");
        SortByAdapter sortByAdapter = new SortByAdapter(lstSortby, getApplicationContext(), new SortByAdapter.OnSortBySelectedListener() {
            @Override
            public void onSortBySelected(String selectedSortBy) {

                    btnSortBy.setText(selectedSortBy);
                    if (!filterChangeSortBy) {
                        numberFilter += 1;
                        filter.setText(String.valueOf(numberFilter));

                    }
                    if (selectedSortBy.equalsIgnoreCase("Lowest-Highest Price")) {
                        sortByPriceAsc = true;
                        btnSortBy.setText("Lowest-Highest Price");
                        filterChangeSortBy = true;
                    } else if (selectedSortBy.equalsIgnoreCase("Highest-Lowest Price")) {
                        sortByPriceAsc = false;
                        filterChangeSortBy = true;
                        btnSortBy.setText("Highest-Lowest Price");
                    } else if (selectedSortBy.equalsIgnoreCase("Newest")) {
                        //Toast.makeText(getApplicationContext(),"newest", Toast.LENGTH_SHORT).show();
                        sortByPriceAsc = null;
                        thirtyDaysAgo = now.minus(30, ChronoUnit.DAYS);
                        btnSortBy.setText("Newest");
                        filterChangeSortBy = true;
                    } else {
                        sortByPriceAsc = null;
                        filterChangeSortBy = true;
                        btnSortBy.setText("Recommended");
                    }
                    bottomSheetDialog.dismiss();
                    filterList(searchView.getQuery().toString(), genderFilter, sortByPriceAsc, thirtyDaysAgo, onSale, price,isFreeShip);

            }
        }, btnSortBy.getText().toString());


        bottomSheetDialog.findViewById(R.id.btn_clear_overlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortByPriceAsc = null;
                btnSortBy.setText("Sort by");
                thirtyDaysAgo = LocalDateTime.MIN;
                filterChangeSortBy = false;
                if (numberFilter > 0) {
                    numberFilter -= 1;
                    filter.setText(String.valueOf(numberFilter));
                }
                filterList(searchView.getQuery().toString(), genderFilter, sortByPriceAsc, thirtyDaysAgo, onSale, price,isFreeShip);
                bottomSheetDialog.dismiss();
            }
        });
        recyleSortBy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyleSortBy.setItemAnimator(new DefaultItemAnimator());
        recyleSortBy.setAdapter(sortByAdapter);
        bottomSheetDialog.show();
    }
    //overlay deals
    private void showDealsOverlay() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView =inflater.inflate(R.layout.deals_overlay, null);

        bottomSheetDialog.setContentView(dialogView);

        TextView overlayTitle = dialogView.findViewById(R.id.overlay_title);
        overlayTitle.setText("Deals");

        ImageButton btnClose = dialogView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        RecyclerView recyleDeals = dialogView.findViewById(R.id.recy_deals);
        ArrayList<String> lstDeals=new ArrayList<>();
        lstDeals.add(0, "On Sale");
        lstDeals.add(1, "Free Shipping Eligible");

        SortByAdapter dealsAdapter = new SortByAdapter(lstDeals, getApplicationContext(), new SortByAdapter.OnSortBySelectedListener() {
            @Override
            public void onSortBySelected(String selectedSortBy) {

                btnDeals.setText(selectedSortBy);
                if (!filterChangeDeal) {
                    numberFilter += 1;
                    filter.setText(String.valueOf(numberFilter));

                }
                if (selectedSortBy.equalsIgnoreCase("On sale")) {
                    onSale = "On Sale";
                    isFreeShip=null;
                    btnDeals.setText(selectedSortBy);
                    filterChangeDeal = true;
                }
                else if(selectedSortBy.equalsIgnoreCase("Free Shipping Eligible"))
                {
                    onSale="";
                    isFreeShip=true;
                    btnDeals.setText(selectedSortBy);
                    filterChangeDeal = true;

                }

                bottomSheetDialog.dismiss();
                filterList(searchView.getQuery().toString(), genderFilter, sortByPriceAsc, thirtyDaysAgo, onSale, price,isFreeShip);

            }
        }, btnDeals.getText().toString());


        bottomSheetDialog.findViewById(R.id.btn_clear_overlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSale = "";
                isFreeShip=null;
                btnDeals.setText("Deals");
                filterChangeDeal = false;
                if (numberFilter > 0) {
                    numberFilter -= 1;
                    filter.setText(String.valueOf(numberFilter));
                }
                filterList(searchView.getQuery().toString(), genderFilter, sortByPriceAsc, thirtyDaysAgo, onSale, price,isFreeShip);
                bottomSheetDialog.dismiss();
            }
        });
        recyleDeals.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyleDeals.setItemAnimator(new DefaultItemAnimator());
        recyleDeals.setAdapter(dealsAdapter);
        bottomSheetDialog.show();
    }
    //overlay price
    private void showPriceOverlay() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView =inflater.inflate(R.layout.price_overlay, null);

        bottomSheetDialog.setContentView(dialogView);

        TextView overlayTitle = dialogView.findViewById(R.id.overlay_title);
        overlayTitle.setText("Price");

        ImageButton btnClose = dialogView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        RecyclerView recylePrice = dialogView.findViewById(R.id.recy_price);
        ArrayList<String> lstPrice=new ArrayList<>();
        lstPrice.add(0, "Min");
        lstPrice.add(1, "Max");

        SortByAdapter priceAdapter = new SortByAdapter(lstPrice, getApplicationContext(), new SortByAdapter.OnSortBySelectedListener() {
            @Override
            public void onSortBySelected(String selectedSortBy) {

                btnPrice.setText(selectedSortBy);
                if (!filterChangedPrice) {
                    numberFilter += 1;
                    filter.setText(String.valueOf(numberFilter));

                }

                if (selectedSortBy.equalsIgnoreCase("Min")) {
                    price = "Min";
                    filterChangedPrice = true;

                } else if (selectedSortBy.equalsIgnoreCase("Max")) {
                    price = "Max";
                    filterChangedPrice = true;
                }
                bottomSheetDialog.dismiss();
                filterList(searchView.getQuery().toString(), genderFilter, sortByPriceAsc, thirtyDaysAgo, onSale, price,isFreeShip);

            }
        }, btnPrice.getText().toString());


        bottomSheetDialog.findViewById(R.id.btn_clear_overlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price = "";
                btnPrice.setText("Price");
                filterChangedPrice = false;
                if (numberFilter > 0) {
                    numberFilter -= 1;
                    filter.setText(String.valueOf(numberFilter));
                }
                filterList(searchView.getQuery().toString(), genderFilter, sortByPriceAsc, thirtyDaysAgo, onSale, price,isFreeShip);
                bottomSheetDialog.dismiss();
            }
        });
        recylePrice.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recylePrice.setItemAnimator(new DefaultItemAnimator());
        recylePrice.setAdapter(priceAdapter);
        bottomSheetDialog.show();
    }
}