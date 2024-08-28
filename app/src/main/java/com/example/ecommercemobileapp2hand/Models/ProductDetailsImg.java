package com.example.ecommercemobileapp2hand.Models;

public class ProductDetailsImg {
    private int product_img_id;
    private int product_details_id;
    private String img_url;

    private ProductDetailsImg(){}
    public ProductDetailsImg(int product_img_id, int product_details_id, String img_url) {
        this.product_img_id = product_img_id;
        this.product_details_id = product_details_id;
        this.img_url = img_url;
    }

    public int getProduct_img_id() {
        return product_img_id;
    }

    public void setProduct_img_id(int product_img_id) {
        this.product_img_id = product_img_id;
    }

    public int getProduct_details_id() {
        return product_details_id;
    }

    public void setProduct_details_id(int product_details_id) {
        this.product_details_id = product_details_id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
