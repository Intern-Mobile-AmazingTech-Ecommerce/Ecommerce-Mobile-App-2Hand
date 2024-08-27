package com.example.ecommercemobileapp2hand.Models.config;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ecommercemobileapp2hand.BuildConfig;

public class CloudinaryConfig {
    private static Cloudinary cloudinary;
    public static Cloudinary getCloudinary(){
        if(cloudinary == null){
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", BuildConfig.CLOUDINARY_CLOUD_NAME,
                    "api_key", BuildConfig.CLOUDINARY_API_KEY,
                    "api_secret", BuildConfig.CLOUDINARY_API_SECRET
            ));
        }
        return cloudinary;
    }

}
