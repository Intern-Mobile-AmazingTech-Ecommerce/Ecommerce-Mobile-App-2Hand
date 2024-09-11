package com.example.ecommercemobileapp2hand.Models.Singleton;

import com.example.ecommercemobileapp2hand.Models.Product;

import java.util.ArrayList;

public class ProductManager {
    public static ProductManager instance;
    private Product product;
    private ArrayList<Product> lstPro;
    private ArrayList<Product> allListPro;
    private ProductManager() {
    }

    public static synchronized ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ArrayList<Product> getLstPro() {
        return lstPro;
    }
    public void setLstPro(ArrayList<Product> listPro){
        this.lstPro  = listPro;
    }

    public ArrayList<Product> getAllListPro(){
        return allListPro;
    }
    public void getAllListPro(ArrayList<Product> allList){
        this.allListPro  = allList;
    }
}
