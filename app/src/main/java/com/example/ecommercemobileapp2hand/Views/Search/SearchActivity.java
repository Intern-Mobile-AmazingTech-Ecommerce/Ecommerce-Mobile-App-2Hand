package com.example.ecommercemobileapp2hand.Views.Search;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Homepage.CustomAdapter.ProductCardAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    ImageView imgBack;
    SearchView searchView;
    ArrayList<Product> lstPro;
    ProductCardAdapter proAdapter;
    RecyclerView recyViewSearchPro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        addControls();
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

        recyViewSearchPro = (RecyclerView) findViewById(R.id.recyProductSearch);
        lstPro = Product.initProduct();
        proAdapter = new ProductCardAdapter(lstPro,SearchActivity.this);

        recyViewSearchPro.setLayoutManager(new GridLayoutManager(this,2));
        recyViewSearchPro.setItemAnimator(new DefaultItemAnimator());
        recyViewSearchPro.setAdapter(proAdapter);
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
    void filterLíst(String text)
    {
        ArrayList <Product> filterList = new ArrayList<>();
        for(Product pro : lstPro)
        {
            if (pro.getProduct_name().toLowerCase().contains(text.toLowerCase()))
            {
                filterList.add(pro);
            }
        }
        if (filterList.isEmpty())
        {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else {
            proAdapter.setFilteredList(filterList);
        }
    }
}