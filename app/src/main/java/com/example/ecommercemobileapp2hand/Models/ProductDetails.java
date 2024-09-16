package com.example.ecommercemobileapp2hand.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.OptionalDouble;

public class ProductDetails implements Parcelable {
    @JsonProperty("product_details_id")
    private int product_details_id;
    @JsonProperty("product_id")
    private int product_id;
    private ProductColor productColor;
    @JsonProperty("description")
    private String description;
    @JsonProperty("sale_price")
    private BigDecimal sale_price;
    @JsonProperty("details_created_at")
    private Timestamp createdAt;
    @JsonProperty("product_details_img_array")
    private ArrayList<ProductDetailsImg> imgDetailsArrayList;
    @JsonProperty("product_details_size_array")
    private ArrayList<ProductDetailsSize> sizeArrayList;
    @JsonProperty("product_reviews_array")
    private ArrayList<ProductReview> productReviews;

    public ArrayList<ProductReview> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(ArrayList<ProductReview> productReviews) {
        this.productReviews = productReviews;
    }

    private Boolean isFavorite;

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public ProductDetails() {
    }

    public ProductDetails(int product_details_id, int product_id, ProductColor productColor, String description, BigDecimal sale_price, Timestamp createdAt, ArrayList<ProductDetailsImg> imgDetailsArrayList, ArrayList<ProductDetailsSize> sizeArrayList, ArrayList<ProductReview> productReviews, Boolean isFavorite) {
        this.product_details_id = product_details_id;
        this.product_id = product_id;
        this.productColor = productColor;
        this.description = description;
        this.sale_price = sale_price;
        this.createdAt = createdAt;
        this.imgDetailsArrayList = imgDetailsArrayList;
        this.sizeArrayList = sizeArrayList;
//        this.productReviews = productReviews;
        this.isFavorite = isFavorite;
    }

    protected ProductDetails(Parcel in) {
        product_details_id = in.readInt();
        product_id = in.readInt();
        productColor = in.readParcelable(ProductColor.class.getClassLoader());
        description = in.readString();
        sale_price = BigDecimal.valueOf(in.readDouble());
        imgDetailsArrayList = in.createTypedArrayList(ProductDetailsImg.CREATOR);
        sizeArrayList = in.createTypedArrayList(ProductDetailsSize.CREATOR);
        productReviews = in.createTypedArrayList(ProductReview.CREATOR);
    }

    public static final Creator<ProductDetails> CREATOR = new Creator<ProductDetails>() {
        @Override
        public ProductDetails createFromParcel(Parcel in) {
            return new ProductDetails(in);
        }

        @Override
        public ProductDetails[] newArray(int size) {
            return new ProductDetails[size];
        }
    };

    public ArrayList<ProductDetailsSize> getSizeArrayList() {
        return sizeArrayList;
    }

    public void setSizeArrayList(ArrayList<ProductDetailsSize> sizeArrayList) {
        this.sizeArrayList = sizeArrayList;
    }

    @JsonProperty("product_color_id")
    public void setProductColorId(int productColorId) {
        if (this.productColor == null) {
            this.productColor = new ProductColor();
        }
        this.productColor.setProduct_color_id(productColorId);
    }

    @JsonProperty("product_color_name")
    public void setProductColorName(String productColorName) {
        if (this.productColor == null) {
            this.productColor = new ProductColor();
        }
        this.productColor.setProduct_color_name(productColorName);
    }

    @JsonProperty("product_color_value")
    public void setProductColorValue(String productColorValue) {
        if (this.productColor == null) {
            this.productColor = new ProductColor();
        }
        this.productColor.setProduct_color_value(productColorValue);
    }

    public ProductColor getProductColor() {
        return productColor;
    }

    public void setProductColor(ProductColor productColor) {
        this.productColor = productColor;
    }

    public ArrayList<ProductDetailsImg> getImgDetailsArrayList() {
        return imgDetailsArrayList;
    }

    public void setImgDetailsArrayList(ArrayList<ProductDetailsImg> imgDetailsArrayList) {
        this.imgDetailsArrayList = imgDetailsArrayList;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getProduct_details_id() {
        return product_details_id;
    }

    public void setProduct_details_id(int product_details_id) {
        this.product_details_id = product_details_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getSale_price() {
        return sale_price;
    }

    public void setSale_price(BigDecimal sale_price) {
        this.sale_price = sale_price;
    }

    public String getAverageRatings(){
        if(getProductReviews() != null){
            OptionalDouble optionalDouble = getProductReviews().stream().mapToDouble(ProductReview::getRating).average();
            if(optionalDouble.isPresent())
                return String.format("%.1f", optionalDouble.getAsDouble());
        }
        return "0";
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(product_details_id);
        dest.writeInt(product_id);
        dest.writeParcelable(productColor, flags);
        dest.writeString(description);
        dest.writeDouble(sale_price != null ? sale_price.doubleValue() : 0.00);
        dest.writeTypedList(imgDetailsArrayList);
        dest.writeTypedList(sizeArrayList);
        dest.writeTypedList(productReviews);
    }
}
