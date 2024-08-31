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

import java.util.ArrayList;
import java.util.List;

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
    TextView textViewTitle;
    LinearLayout layoutFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        addControls();
        load();
        addEvent();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    void addControls()
    {
        imgBack = (ImageView) findViewById(R.id.btnBack);
        searchView = (SearchView)  findViewById(R.id.searchView);
        searchView.clearFocus();
        layoutFilter = (LinearLayout) findViewById(R.id.layoutFilter);
        recyViewSearchPro = (RecyclerView) findViewById(R.id.recyProductSearch);
        lstPro = ProductHandler.getDataByObjectName("Men");
        proAdapter = new ProductCardAdapter(lstPro,SearchActivity.this);

        scrollViewPro = (ScrollView) findViewById(R.id.scrollViewProduct);
        recyViewCateSearch = (RecyclerView) findViewById(R.id.recyclerViewCategoriesSearch);
        linearLayoutSearch = (LinearLayout) findViewById(R.id.linearLayoutSearch);
        textViewTitle = (TextView) findViewById(R.id.titleSearch);
        recyViewSearchPro.setLayoutManager(new GridLayoutManager(this,2));
        recyViewSearchPro.setItemAnimator(new DefaultItemAnimator());
        recyViewSearchPro.setAdapter(proAdapter);
        loadRecycleViewCategories();
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

    }
    void load()
    {
        recyViewCateSearch.setVisibility(View.VISIBLE);
        recyViewSearchPro.setVisibility(View.GONE);
        linearLayoutSearch.setVisibility(View.GONE);
        layoutFilter.setVisibility(View.GONE);
    }
    void filterLíst(String text)
    {
        ArrayList <Product> filterList = new ArrayList<>();
        for(com.example.ecommercemobileapp2hand.Models.Product pro : lstPro)
        {
            if (pro.getProduct_name().toLowerCase().contains(text.toLowerCase()))
            {
                filterList.add(pro);
            }
        }
        if (text.isEmpty())
        {
            recyViewCateSearch.setVisibility(View.VISIBLE);
            recyViewSearchPro.setVisibility(View.GONE);
            layoutFilter.setVisibility(View.GONE);
            linearLayoutSearch.setVisibility(View.GONE);

        } else if (filterList.isEmpty()) {
            textViewTitle.setVisibility(View.GONE);
            recyViewCateSearch.setVisibility(View.GONE);
            recyViewSearchPro.setVisibility(View.GONE);
            layoutFilter.setVisibility(View.GONE);
            linearLayoutSearch.setVisibility(View.VISIBLE);
        }else {
            textViewTitle.setVisibility(View.GONE);
            recyViewCateSearch.setVisibility(View.GONE);
            recyViewSearchPro.setVisibility(View.VISIBLE);
            layoutFilter.setVisibility(View.VISIBLE);
            linearLayoutSearch.setVisibility(View.GONE);
            proAdapter.setFilteredList(filterList);
        }

    }
}