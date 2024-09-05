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
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity {

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
    private TextView textViewTitle,tvResult;
    private LinearLayout categoryContainer,productContainer,layoutFilter;
    private AppCompatButton filter,btnDeals,btnGender,btnSortBy,btnPrice;
    private String genderFilter = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
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
    }

    void addControls()
    {
        imgBack = (ImageView) findViewById(R.id.btnBack);
        searchView = (SearchView)  findViewById(R.id.searchView);
        searchView.clearFocus();
        layoutFilter = (LinearLayout) findViewById(R.id.layoutFilter);
        recyViewSearchPro = (RecyclerView) findViewById(R.id.recyProductSearch);
        lstPro = (ArrayList<Product>) App.getCache().getIfPresent("allPro");
        proAdapter = new ProductCardAdapter(lstPro,SearchActivity.this);

        scrollViewPro = (ScrollView) findViewById(R.id.scrollViewProduct);
        recyViewCateSearch = (RecyclerView) findViewById(R.id.recyclerViewCategoriesSearch);
        linearLayoutSearch = (LinearLayout) findViewById(R.id.linearLayoutSearch);
        textViewTitle = (TextView) findViewById(R.id.titleSearch);
        recyViewSearchPro.setLayoutManager(new GridLayoutManager(this,2));
        recyViewSearchPro.setItemAnimator(new DefaultItemAnimator());
        recyViewSearchPro.setAdapter(proAdapter);
        tvResult = findViewById(R.id.tvResult);
        //Container
        categoryContainer = findViewById(R.id.categoryContainer);
        productContainer = findViewById(R.id.productContainer);

        //filter btn
        filter = findViewById(R.id.filter);
        btnSortBy = findViewById(R.id.btnSortBy);
        btnPrice = findViewById(R.id.btnPrice);
        btnGender = findViewById(R.id.btnGender);
        btnDeals = findViewById(R.id.btnDeals);
    }

    private void loadRecycleViewCategories() {
        categoryList = new ArrayList<>();
        categoryList = CategoriesHandler.getData();
        categoriesAdapter = new CategoriesAdapter(categoryList, this,R.layout.item_category);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        recyViewCateSearch.setLayoutManager(layoutManager);
        recyViewCateSearch.setAdapter(categoriesAdapter);
    }

    void addEvent()
    {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("SearchQuery", "Current query: " + newText);
                filterList(newText,"");
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
    void filterList(String text,String genderFilter)
    {
        ArrayList <Product> filterList = new ArrayList<>();
        filterList = lstPro.stream()
                .filter(product -> product.getProduct_name().toLowerCase().contains(text.toLowerCase()))
                .filter(product -> genderFilter.isEmpty() || product.getProductObject().getObject_name().equalsIgnoreCase(genderFilter))
                .collect(Collectors.toCollection(ArrayList::new));

        if (text.isEmpty())
        {
            categoryContainer.setVisibility(View.VISIBLE);
            linearLayoutSearch.setVisibility(View.GONE);
            productContainer.setVisibility(View.GONE);

        } else if (filterList.isEmpty()) {
            linearLayoutSearch.setVisibility(View.VISIBLE);
            categoryContainer.setVisibility(View.GONE);
            productContainer.setVisibility(View.GONE);
        }else {

            productContainer.setVisibility(View.VISIBLE);
            categoryContainer.setVisibility(View.GONE);
            linearLayoutSearch.setVisibility(View.GONE);
            proAdapter.setFilteredList(filterList);
            recyViewSearchPro.setLayoutManager(new GridLayoutManager(this,2));
            tvResult.setText(""+filterList.size()+" Result Found");
        }

    }

    private void showSortByOverlay(String type) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.sortby_overlay, null);
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

        RecyclerView recyleSortBy = dialogView.findViewById(R.id.recy_overlay);
        SortByAdapter sortByAdapter = getSortByAdapter(type,bottomSheetDialog);
        recyleSortBy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyleSortBy.setItemAnimator(new DefaultItemAnimator());

        recyleSortBy.setAdapter(sortByAdapter);
        bottomSheetDialog.show();
    }
    private @NonNull SortByAdapter getSortByAdapter(String type, BottomSheetDialog dialog) {
        ArrayList<String> sortByArr =  new ArrayList<>();
        if(type.contains("Sort by")){
            sortByArr.add(0,"Recommended");
            sortByArr.add(1,"Newest");
            sortByArr.add(2,"Lowest-Highest Price");
            sortByArr.add(3,"Highest-Lowest Price");
        }else if(type.contains("Gender")){
            sortByArr.add(0,"Men");
            sortByArr.add(1,"Women");
        }else if(type.contains("Deals")){
            sortByArr.add(0,"On sale");
        }else{
            sortByArr.add(0,"Min");
            sortByArr.add(1,"Max");
        }

        SortByAdapter sortByAdapter = new SortByAdapter(sortByArr ,getApplicationContext(), new SortByAdapter.OnSortBySelectedListener() {
            @Override
            public void onSortBySelected(String selectedSortBy) {
                //Add filter function
                if(type.contains("Gender")){
                    if (selectedSortBy.equalsIgnoreCase("Men")) {
                        genderFilter = "Men";
                    } else if (selectedSortBy.equalsIgnoreCase("Women")) {
                        genderFilter = "Women";
                    }
                    filterList(searchView.getQuery().toString(), genderFilter);
                    btnGender.setText(genderFilter);
                    dialog.dismiss();
                }

            }
        },genderFilter);
        return sortByAdapter;
    }
}