package com.example.ecommercemobileapp2hand.Views.Search;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.ecommercemobileapp2hand.Views.Adapters.ProductCardAdapter;
import com.example.ecommercemobileapp2hand.Views.App;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity {

    ImageView imgBack;
    SearchView searchView;
    ArrayList<com.example.ecommercemobileapp2hand.Models.Product> lstPro;
    ProductCardAdapter proAdapter;
    RecyclerView recyViewSearchPro;
    RecyclerView recyViewCateSearch;
    LinearLayout linearLayoutSearch;
    ScrollView scrollViewPro;
    ArrayList<ProductCategory> categoryList;
    CategoriesAdapter categoriesAdapter;
    TextView textViewTitle,tvResult;
    LinearLayout categoryContainer,productContainer,layoutFilter;
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
        lstPro = (ArrayList<Product>) App.getCache().getIfPresent("lstPro");
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

                filterLíst(newText);
                return false;
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    void filterLíst(String text)
    {
        ArrayList <Product> filterList = new ArrayList<>();
        filterList = lstPro.stream().filter(product -> product.getProduct_name().toLowerCase().contains(text.toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
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
}