package com.example.ecommercemobileapp2hand.Models.Singleton;

import com.example.ecommercemobileapp2hand.Models.ProductCategory;

import java.util.ArrayList;

public class CategoriesManager {
    public static CategoriesManager instance;
    private ArrayList<ProductCategory> productCategories;

    private CategoriesManager(){}

    public static synchronized CategoriesManager getInstance(){
        if(instance == null){
            instance = new CategoriesManager();
        }
        return instance;
    }

    public ArrayList<ProductCategory> getProductCategories(){
        return productCategories;
    }

    public void setProductCategories(ArrayList<ProductCategory> productCategories){
        this.productCategories = productCategories;
    }

}
