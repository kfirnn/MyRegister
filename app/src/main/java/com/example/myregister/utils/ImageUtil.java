package com.example.myregister.utils;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;

/// Utility class for image operations
/// Contains methods for requesting permissions, converting images to base64 and vice versa
public class ImageUtil {
    private static final String TAG = "ImageUtil";

    /// Request permissions for camera and storage
    /// @param activity The activity to request permissions from
    /// @see ActivityCompat#requestPermissions(Activity, String[], int)
    public static void requestPermission(@NotNull Activity activity) {
        // Request permissions for camera and storage
        ActivityCompat.requestPermissions(activity,
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 1);
    }

    /// Convert an image to a base64 string
    /// @param postImage The image to convert
    /// @return The base64 string representation of the image
    public static @Nullable String convertTo64Base(@NotNull final ImageView postImage) {
        try {
            if (postImage.getDrawable() == null) {
                Log.w(TAG, "No drawable found in ImageView");
                return null;
            }
            Bitmap bitmap = ((BitmapDrawable) postImage.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String base64String = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.d(TAG, "Successfully converted image to base64 string of length: " + base64String.length());
            return base64String;
        } catch (Exception e) {
            Log.e(TAG, "Error converting image to base64", e);
            return null;
        }
    }

    /// Convert a base64 string to an image
    /// @param base64Code The base64 string to convert
    /// @return The image represented by the base64 string
    public static @Nullable Bitmap convertFrom64base(final String base64Code) {
        try {
            if (base64Code == null || base64Code.isEmpty()) {
                Log.w(TAG, "Empty or null base64 string provided");
                return null;
            }
            
            byte[] decodedString = Base64.decode(base64Code, Base64.DEFAULT);
            Log.d(TAG, "Decoded base64 string to byte array of length: " + decodedString.length);
            
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            if (bitmap == null) {
                Log.e(TAG, "Failed to decode bitmap from byte array");
                return null;
            }
            
            Log.d(TAG, "Successfully converted base64 to bitmap of size: " + bitmap.getWidth() + "x" + bitmap.getHeight());
            return bitmap;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Invalid base64 string provided", e);
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Error converting base64 to bitmap", e);
            return null;
        }
    }
}