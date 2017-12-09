package com.whitney.jigsaw;

import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;

/**
 * Created by Dracle on 12/9/2017.
 */

public class EdgeDetector {
    // truncate color component to be between 0 and 255
    public static int truncate(int a) {
        if (a < 0) return 0;
        else if (a > 255) return 255;
        else return a;
    }

    public static void main(String[] args) {

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

        Picture picture0 = new Picture(args[0]);
        int width = picture0.width();
        int height = picture0.height();
        Picture picture1 = new Picture(width, height);


        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {

                // get 3-by-3 array of colors in neighborhood
                int[][] gray = new int[3][3];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        gray[i][j] = (int) Luminance.intensity(picture0.get(x - 1 + i, y - 1 + j));
                    }
                }

                // apply filter
                int gray1 = 0, gray2 = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        gray1 += gray[i][j] * filter1[i][j];
                        gray2 += gray[i][j] * filter2[i][j];
                    }
                }
                // int magnitude = 255 - truncate(Math.abs(gray1) + Math.abs(gray2));
                int magnitude = 255 - truncate((int) Math.sqrt(gray1 * gray1 + gray2 * gray2));
                Color grayscale = new Color(magnitude, magnitude, magnitude);
                picture1.set(x, y, grayscale);
            }
        }
        picture0.show();
        picture1.show();
        // picture1.save("baboon-edge.jpg");
    }
}