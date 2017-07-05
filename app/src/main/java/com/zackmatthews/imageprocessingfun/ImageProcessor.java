package com.zackmatthews.imageprocessingfun;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by zackmatthews on 7/5/17.
 */

public class ImageProcessor {
    private static ImageProcessor instance;

    public static ImageProcessor getInstance() {
        if(instance == null){
            instance = new ImageProcessor();
        }
        return instance;
    }

    public int getAverageColor(Bitmap bitmap){
        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int pixelCount = 0;

        for (int y = 0; y < bitmap.getHeight(); y++)
        {
            for (int x = 0; x < bitmap.getWidth(); x++)
            {
                int c = bitmap.getPixel(x, y);

                pixelCount++;
                redBucket += Color.red(c);
                greenBucket += Color.green(c);
                blueBucket += Color.blue(c);
                // does alpha matter?
            }
        }

        int averageColor = Color.rgb(redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount);
        return averageColor;
    }
}
