package com.example.ecommercemobileapp2hand.Views.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.example.ecommercemobileapp2hand.Models.config.CloudinaryConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Map;

public class Util {
    public static Bitmap blur(Context context, Bitmap image) {
        final float BITMAP_SCALE = 0.4f;
        final float BLUR_RADIUS = 6f;

        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        scriptIntrinsicBlur.setRadius(BLUR_RADIUS);
        scriptIntrinsicBlur.setInput(tmpIn);
        scriptIntrinsicBlur.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }
    public static Bitmap convertStringToBitmapFromAccess(Context context,String filename){
        AssetManager assetManager = context.getAssets();
        try{
            InputStream is = assetManager.open(filename);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public static String formatCurrency(int price) {
        DecimalFormat formatter = new DecimalFormat("###,###");
        return formatter.format(price);
    }
    public static int dpToPx(Context context, int dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }
    public static String getCloudinaryImageUrl(Context context,String publicId,int w, int h) {
        Cloudinary cloudinary = CloudinaryConfig.getCloudinary();
        // Kiểm tra nếu w hoặc h bằng -1, bỏ qua bước đặt kích thước
        if (w == -1 || h == -1) {
            String url = cloudinary.url().generate(publicId);
            url = url.replace("http://", "https://");
            return url;
        }
        // Nếu không, tiếp tục với quy trình hiện tại
        int widthDP = dpToPx(context, w);
        int heightDP = dpToPx(context, h);
        String url = cloudinary.url().transformation(new Transformation().width(widthDP).height(heightDP))
                .generate(publicId);
        url = url.replace("http://", "https://");
        return url;
    }

    public static boolean uploadImage(String imgPath){
        try{
            if(imgPath!=null || !imgPath.isEmpty()){
                Cloudinary cloudinary = CloudinaryConfig.getCloudinary();
                Map options = ObjectUtils.asMap(
                        "folder","EcommerceApp"
                );
                File file = new File(imgPath);
                cloudinary.uploader().upload(file,options);
                return true;
            }

        }catch (Exception e){
            throw new RuntimeException(e);

        }
        return false;
    }
}
