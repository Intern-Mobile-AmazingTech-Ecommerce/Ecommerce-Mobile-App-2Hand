package com.example.ecommercemobileapp2hand.API;

import com.example.ecommercemobileapp2hand.Models.District;
import com.example.ecommercemobileapp2hand.Models.Province;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AddressApi {
    // Địa chỉ API cho lấy danh sách tất cả tỉnh thành
    @GET("api/?depth=2") // Địa chỉ API để lấy danh sách tỉnh
    Call<List<Province>> getAllProvinces(); // Phương thức lấy danh sách tất cả tỉnh thành



}