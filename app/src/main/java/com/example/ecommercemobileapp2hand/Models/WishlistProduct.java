package com.example.ecommercemobileapp2hand.Models;

public class WishlistProduct {
    private int wishlist_id;
    private int product_details_id;

    public WishlistProduct(int wishlist_id, int product_details_id) {
        this.wishlist_id = wishlist_id;
        this.product_details_id = product_details_id;
    }

    public int getWishlist_id() {
        return wishlist_id;
    }

    public void setWishlist_id(int wishlist_id) {
        this.wishlist_id = wishlist_id;
    }

    public int getProduct_details_id() {
        return product_details_id;
    }

    public void setProduct_details_id(int product_details_id) {
        this.product_details_id = product_details_id;
    }
}
