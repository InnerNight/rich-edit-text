package com.liye.richedittextdemo.richedittext.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

/**
 * @author liye
 * @version 4.1.0
 * @since: 16/4/20 下午2:28
 */
public class BitmapUtils {
    public static Bitmap decodeScaleImage(String path, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        if (options.outWidth <= 0 || options.outHeight <= 0) {
            // 保护策略，防止下面出现崩溃
            return bitmap;
        }
        Bitmap decodeBitmap = createScaledBitmap(bitmap, options.outWidth, options.outHeight
                , maxWidth, maxHeight);
        int degree = readPictureDegree(path);
        if (decodeBitmap != null && degree != 0) {
            bitmap = rotateBitmap(degree, decodeBitmap);
            decodeBitmap.recycle();
            return bitmap;
        } else {
            return decodeBitmap;
        }
    }

    public static Bitmap createScaledBitmap(Bitmap bitmap, int width, int height
            , float maxWidth, float maxHeight) {
        Matrix matrix = new Matrix();
        float scale = calculateScale(width, height, maxWidth, maxHeight);
        matrix.postScale(scale, scale);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0
                , width, height, matrix, false);
        return newBitmap;
    }

    public static float calculateScale(int outWidth, int outHeight, float maxWidth, float maxHeight) {
        float ratioH = maxHeight / (float) outHeight;
        float ratioW = maxWidth / (float) outWidth;
        float scale = ratioH < ratioW ? ratioH : ratioW;
        return scale;
    }

    public static int readPictureDegree(String path) {
        short degree = 0;

        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt("Orientation", 1);
            switch (orientation) {
                case 3:
                    degree = 180;
                case 4:
                case 5:
                case 7:
                default:
                    break;
                case 6:
                    degree = 90;
                    break;
                case 8:
                    degree = 270;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return degree;
    }

    public static Bitmap rotateBitmap(int degree, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}
