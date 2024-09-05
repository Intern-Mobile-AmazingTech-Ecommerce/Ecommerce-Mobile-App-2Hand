package com.example.ecommercemobileapp2hand.Views.Homepage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecommercemobileapp2hand.Controllers.CategoriesHandler;
import com.example.ecommercemobileapp2hand.Controllers.ProductHandler;
import com.example.ecommercemobileapp2hand.Models.Product;
import com.example.ecommercemobileapp2hand.Models.ProductCategory;
import com.example.ecommercemobileapp2hand.Models.UserAccount;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.CategoriesAdapter;
import com.example.ecommercemobileapp2hand.Views.Adapters.ProductCardAdapter;
import com.example.ecommercemobileapp2hand.Views.App;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.example.ecommercemobileapp2hand.Views.Search.SearchActivity;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private String gender;
    private TextView tvSeeAll;
    private ArrayList<Product> allPro;
    private ArrayList<Product> lstPro;
    private RecyclerView recyclerViewCategories;
    private ArrayList<ProductCategory> categoryList;
    private CategoriesAdapter categoriesAdapter;

    private RecyclerView recyclerViewNewIn;
    private ArrayList<com.example.ecommercemobileapp2hand.Models.Product> lstProNewIn;
    private ProductCardAdapter NewInAdapter;
    private TextView tvNewInSeeAll, tvTopSellingSeeAll;
    private RecyclerView recyclerViewTopSelling;
    private ArrayList<com.example.ecommercemobileapp2hand.Models.Product> lstProTopSelling;
    private ProductCardAdapter TopSellingAdapter;

    private String genderTextView;
    private UserAccount userAccount;

    private SharedPreferences sharedPreferences;
    private MaterialButton btnSearch;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//            if (getArguments() != null)
//            {
//                userAccount = (UserAccount) getArguments().getSerializable("UserAccount");
//                genderTextView = getArguments().getString("UserGender");
//            }
            addControl(view);

        return view;
    }
    private void getGenderKey(){
        sharedPreferences = getActivity().getSharedPreferences("my_userID",Context.MODE_PRIVATE);
        gender = sharedPreferences.getString("gender_key","");
        if(gender.isEmpty()){
            gender = "Men";
        }
        Log.d("Gender",gender);
    }
    @Override
    public void onResume() {
        super.onResume();
        try {
            getGenderKey();
            loadListPro(gender);
            loadCategoriesData();
            loadTopSellingProductsData();
            loadNewInProductsData();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        addEvent();
    }
    private void loadListPro(String gen) throws ExecutionException {
        allPro = (ArrayList<Product>) App.getCache().get("allPro", new Callable<ArrayList<Product>>() {
            @Override
            public ArrayList<Product> call() throws Exception {
                return ProductHandler.getData();
            }
        });
        //GenerateListPro
        lstPro = (ArrayList<Product>) App.getCache().get("lstPro", new Callable<ArrayList<Product>>() {
            @Override
            public ArrayList<Product> call() throws Exception {
                return ProductHandler.getDataByObjectName(gen);
            }
        });
    }
    private void addControl(View view) {

        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        tvSeeAll = view.findViewById(R.id.tvSeeAll);

        recyclerViewNewIn = view.findViewById(R.id.recyclerViewNewIn);
        recyclerViewTopSelling = view.findViewById(R.id.recyclerViewTopSelling);

        tvNewInSeeAll = view.findViewById(R.id.tvNewInSeeAll);
        tvTopSellingSeeAll = view.findViewById(R.id.tvTopSellingSeeAll);
        btnSearch = view.findViewById(R.id.btnSearch);
    }

    private void loadCategoriesData() throws ExecutionException {

        ArrayList<ProductCategory> categoryList = (ArrayList<ProductCategory>) App.getCache().get("categories", new Callable<ArrayList<ProductCategory>>() {
            @Override
            public ArrayList<ProductCategory> call() throws Exception {
                ArrayList<ProductCategory> categories = CategoriesHandler.getData();
                return categories.size() > 5 ? new ArrayList<>(categories.subList(0, 5)) : categories;
            }
        });
        categoriesAdapter = new CategoriesAdapter(categoryList, getContext(), R.layout.custom_recycle_categories_homepage);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recyclerViewCategories.setAdapter(categoriesAdapter);


    }

    private void loadTopSellingProductsData() throws ExecutionException {

        lstProTopSelling = (ArrayList<Product>) App.getCache().get("TopSelling", new Callable<ArrayList<Product>>() {
            @Override
            public ArrayList<Product> call() throws Exception {
                    ArrayList<Product> lst = (ArrayList<Product>) lstPro.stream()
                            .filter(product -> product.getSold().compareTo(BigDecimal.ZERO) > 0)
                            .sorted(Comparator.comparing(Product::getSold).reversed())
                            .collect(Collectors.toList());
                return lstPro.size() > 0 ? new ArrayList<>(lst) : new ArrayList<>();
            }
        });
        if (lstProTopSelling.size() > 5) {
            ArrayList<Product> subTopSellingList = lstProTopSelling.subList(0, 5).stream().collect(Collectors.toCollection(ArrayList::new));
            TopSellingAdapter = new ProductCardAdapter(subTopSellingList, getActivity());
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTopSelling.setLayoutManager(layoutManager);
        recyclerViewTopSelling.setAdapter(TopSellingAdapter);

    }
    private void loadNewInProductsData() throws ExecutionException {

        lstProNewIn = (ArrayList<Product>) App.getCache().get("NewIn", new Callable<ArrayList<Product>>() {
            @Override
            public ArrayList<Product> call() throws Exception {
                if (lstPro.size() > 0) {
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime thirtyDaysAgo = now.minus(30, ChronoUnit.DAYS);
                    return lstPro.stream()
                            .filter(product -> product.getCreated_at().isAfter(thirtyDaysAgo))
                            .collect(Collectors.toCollection(ArrayList::new));
                }
                return new ArrayList<>();
            }
        });

        if (lstProNewIn.size() > 5) {
            ArrayList<Product> subNewInProductList = lstProNewIn.subList(0, 5).stream().collect(Collectors.toCollection(ArrayList::new));
            NewInAdapter = new ProductCardAdapter(subNewInProductList, getActivity());
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewNewIn.setLayoutManager(layoutManager);
        recyclerViewNewIn.setAdapter(NewInAdapter);

    }

    private void addEvent() {
        tvSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CategoriesActivity.class);
//                intent.putExtra("Gender", genderTextView);
                startActivity(intent);

            }
        });
        tvTopSellingSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CategoryProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("TopSellingList", lstProTopSelling);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        tvNewInSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CategoryProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("NewInList", lstProNewIn);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

}