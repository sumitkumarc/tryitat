package com.app.tryitat.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.TypedValue;

public class ImageLoadingUtils {
    private Context context;

    public ImageLoadingUtils(Context context2) {
        this.context = context2;
    }

    public int convertDipToPixels(float f) {
        return (int) TypedValue.applyDimension(1, f, this.context.getResources().getDisplayMetrics());
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3;
        int i4 = options.outHeight;
        int i5 = options.outWidth;
        if (i4 > i2 || i5 > i) {
            i3 = Math.round(((float) i4) / ((float) i2));
            int round = Math.round(((float) i5) / ((float) i));
            if (i3 >= round) {
                i3 = round;
            }
        } else {
            i3 = 1;
        }
        while (((float) (i5 * i4)) / ((float) (i3 * i3)) > ((float) (i * i2 * 2))) {
            i3++;
        }
        return i3;
    }
}
