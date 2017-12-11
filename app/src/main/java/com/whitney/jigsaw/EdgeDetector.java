package com.whitney.jigsaw;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.ColorUtils;
import android.widget.TextView;

/**
 * Created by Dracle on 12/9/2017.
 */

public class EdgeDetector
{
//    // truncate color component to be between 0 and 255
//    public static int truncate(int a) {
//        if (a < 0) return 0;
//        else if (a > 255) return 255;
//        else return a;
//    }
//
//    public static void main(String[] args) {
//
//        int[][] filter1 = {
//                {-1, 0, 1},
//                {-2, 0, 2},
//                {-1, 0, 1}
//        };
//
//        int[][] filter2 = {
//                {1, 2, 1},
//                {0, 0, 0},
//                {-1, -2, -1}
//        };
//
//        Picture picture0 = new Picture(args[0]);
//        int width = picture0.width();
//        int height = picture0.height();
//        Picture picture1 = new Picture(width, height);
//
//
//        for (int y = 1; y < height - 1; y++) {
//            for (int x = 1; x < width - 1; x++) {
//
//                // get 3-by-3 array of colors in neighborhood
//                int[][] gray = new int[3][3];
//                for (int i = 0; i < 3; i++) {
//                    for (int j = 0; j < 3; j++) {
//                        gray[i][j] = (int) Luminance.intensity(picture0.get(x - 1 + i, y - 1 + j));
//                    }
//                }
//
//                // apply filter
//                int gray1 = 0, gray2 = 0;
//                for (int i = 0; i < 3; i++) {
//                    for (int j = 0; j < 3; j++) {
//                        gray1 += gray[i][j] * filter1[i][j];
//                        gray2 += gray[i][j] * filter2[i][j];
//                    }
//                }
//                // int magnitude = 255 - truncate(Math.abs(gray1) + Math.abs(gray2));
//                int magnitude = 255 - truncate((int) Math.sqrt(gray1 * gray1 + gray2 * gray2));
//                Color grayscale = new Color(magnitude, magnitude, magnitude);
//                picture1.set(x, y, grayscale);
//            }
//        }
//        picture0.show();
//        picture1.show();
//        // picture1.save("baboon-edge.jpg");
//    }

    // truncate color component to be between 0 and 255
    private static int truncate(int a)
    {
        if (a < 0) return 0;
        if (a > 255) return 255;
        return a;
    }

    public static Bitmap sobel(BitmapDrawable bitmapDrawable, final TextView progressView, Activity activity)
    {
        return sobel(bitmapDrawable.getBitmap(), progressView, activity);
    }

    public static Bitmap sobel(Resources resources, int imageResId, final TextView progressView, Activity activity)
    {
        return sobel(BitmapFactory.decodeResource(resources, imageResId), progressView, activity);
    }

    public static Bitmap sobel(Bitmap inputBitmap, final TextView progressView, Activity activity)
    {
        int[][] filter1 = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };

        int[][] filter2 = {
                {1, 2, 1},
                {0, 0, 0},
                {-1, -2, -1}
        };

        int width = inputBitmap.getWidth();
        int height = inputBitmap.getHeight();
        Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int[][] luminanceMatrix = buildLuminanceMatrix(inputBitmap);

        int gray1;
        int gray2;
        int magnitude;
        int grayscale;
        int x;
        for (int y = 1; y < height - 1; y++)
        {
            for (x = 1; x < width - 1; x++)
            {
                gray1 = 0;
                gray2 = 0;

                for (int i = 0; i < 3; i++)
                {
                    for (int j = 0; j < 3; j++)
                    {
                        gray1 += luminanceMatrix[x - 1 + i][y - 1 + j] * filter1[i][j];
                        gray2 += luminanceMatrix[x - 1 + i][y - 1 + j] * filter2[i][j];
                    }
                }

                magnitude = 255 - truncate((int) Math.sqrt(gray1 * gray1 + gray2 * gray2));
                grayscale = Color.argb(255, magnitude, magnitude, magnitude);
                outputBitmap.setPixel(x, y, grayscale);

                // Update progress
                final float percent = 100.0f * (float) ((y * width) + x) / (float) (height * width);
                activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressView.setText("" + percent);
                    }
                });
            }
        }

        progressView.setText("Done");

        return outputBitmap;
    }

    private static int[][] buildLuminanceMatrix(Bitmap source)
    {
        int width = source.getWidth();
        int height = source.getHeight();
        int[] pixels = new int[width * height];

        source.getPixels(pixels, 0, width, 0, 0, width, height);

        int[][] result = new int[width][height];
        double luminance;
        int x;
        for (int y = 0; y < height; y++)
        {
            for (x = 0; x < width; x++)
            {
                luminance = ColorUtils.calculateLuminance(pixels[y * width + x]);
                result[x][y] = (int) (255 * luminance);
            }
        }

        return result;
    }
}