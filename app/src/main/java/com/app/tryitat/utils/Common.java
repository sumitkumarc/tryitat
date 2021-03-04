package com.app.tryitat.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION;


public class Common {
    public static Toast toast;
    public static int OrderBy = 0;
    public static int MERCHANT_TYPE = 1;
    public static int MinPrice = 0;
    public static int MaxPrice = 0;
    public static String DELIVER_TYPE = "";
    public static int PAYMENT_TYPE = 4;
    public static String NOTE = "type note here...";
    public static int FILTER_TYPE = 0;
    public static int RESTAURANT_ID = 0;
    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());


    public static boolean edvalidateName(String name, EditText ti_name, String msg) {
        if (name.isEmpty()) {
            ti_name.requestFocus();
            ti_name.setError(msg);
            return false;
        } else {
            ti_name.setError(null);
            return true;
        }
    }

    public static boolean isEmpty(String value) {
        boolean isEmpty = TextUtils.isEmpty(value);
        if (!isEmpty) {
            isEmpty = value.toLowerCase().equals("null");
        }
        return isEmpty;
    }

    @NotNull
    public static String isStrempty(String s) {
        if (s == null) {
            return "-";
        } else {
            return s.trim();
        }
    }

    @NotNull
    public static int isIntempty(int s) {
        if (s == 0) {
            return 0;
        } else {
            return s;
        }
    }

    @NotNull
    public static int isIntSum(Integer quantity, int s) {
        if (quantity == 0) {
            return 0;
        } else {
            s = quantity;
            s++;
            return s;
        }
    }

    @NotNull
    public static int isIntSub(Integer quantity, int s) {
        if (quantity == 0) {
            return 0;
        } else {
            s = quantity;
            s--;
            return s;
        }
    }

    @NotNull
    public static String isStrBuildempty(String s) {
        if (s == null || s.equals(" ")) {
            return "";
        } else {
            String s1 = "[" + s + "]";
            return s1.trim();
        }
    }

    @NotNull
    public static String isRequired(Boolean s) {
        if (s) {
            return "<font color='#e4434b'> * </font>";
        } else {
            return "";
        }
    }

    public static boolean isInternetAvailable(Context foContext) {
        NetworkInfo loNetInfo = ((ConnectivityManager) foContext
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();

        if (loNetInfo != null)
            if (loNetInfo.isAvailable())
                if (loNetInfo.isConnected())
                    return true;

        return false;
    }

    public static void displayToastMessageShort(Context context, String msg, boolean isLong) {
        if (toast == null)
            toast = Toast.makeText(context, msg, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static String removeDuplicates(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (!result.toString().contains(String.valueOf(input.charAt(i)))) {
                result.append(String.valueOf(input.charAt(i)));
            }
        }
        return result.toString();
    }

    public static RecyclerView.LayoutManager isRvLayoutManger(boolean i, Activity activity, int i11, int i1) {
        if (i) {
            LinearLayoutManager rv_dynamicView = new LinearLayoutManager(activity);
            rv_dynamicView.setOrientation(i11);
            return rv_dynamicView;
        } else {
            GridLayoutManager rv_proManager = new GridLayoutManager(activity, i1);
            rv_proManager.setOrientation(i11);
            return rv_proManager;
        }
    }

    public static String getParenthesesContent1(String str) {
        String result = str.substring(str.indexOf("(\"") + 0, str.indexOf("\")"));

        return result;
    }

    public static int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    public static String addZeroBeforeDate(int n) {
        return String.valueOf((n < 10) ? ("0" + n) : n);
    }

    public static File getCompressed(Context context, String path) throws IOException {
        if (context == null)
            throw new NullPointerException();
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null)
            cacheDir = context.getCacheDir();
        String rootDir = cacheDir.getAbsolutePath() + "/ImageCompressor";
        File root = new File(rootDir);
        if (!root.exists())
            root.mkdirs();
//        Bitmap bitmap = decodeImageFromFiles(path, 300, 300);
        File compressed = new File(root, SDF.format(new Date()) + ".jpg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        getCroppedBitmap(path).compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        FileOutputStream fileOutputStream = new FileOutputStream(compressed);
        fileOutputStream.write(byteArrayOutputStream.toByteArray());
        fileOutputStream.flush();
        fileOutputStream.close();
        return compressed;
    }

    public static Bitmap getCroppedBitmap(String s) {
        Bitmap bitmap;
        Bitmap bitmap2;
        String realPathFromURI = s;
        Log.e("FILE_PATH", realPathFromURI);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap decodeFile = BitmapFactory.decodeFile(realPathFromURI, options);
        int i = options.outHeight;
        int i2 = options.outWidth;
        float f = (float) (i2 / i);
        float f2 = (float) i;
        if (f2 > 2048.0f || ((float) i2) > 1440.0f) {
            if (f < 0.703125f) {
                i2 = (int) (((float) i2) * (2048.0f / f2));
                i = 2048;
            } else {
                i = f > 0.703125f ? (int) (f2 * (1440.0f / ((float) i2))) : 2048;
                i2 = 1440;
            }
        }
        options.inSampleSize = ImageLoadingUtils.calculateInSampleSize(options, i2, i);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16384];
        try {
            decodeFile = BitmapFactory.decodeFile(realPathFromURI, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        try {
            bitmap = Bitmap.createBitmap(i2, i, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError e2) {
            e2.printStackTrace();
            bitmap = null;
        }
        Bitmap bitmap3 = bitmap;
        float f3 = (float) i2;
        float f4 = f3 / ((float) options.outWidth);
        float f5 = (float) i;
        float f6 = f5 / ((float) options.outHeight);
        float f7 = f3 / 2.0f;
        float f8 = f5 / 2.0f;
        Matrix matrix = new Matrix();
        matrix.setScale(f4, f6, f7, f8);
        Canvas canvas = new Canvas(bitmap3);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(decodeFile, f7 - ((float) (decodeFile.getWidth() / 2)), f8 - ((float) (decodeFile.getHeight() / 2)), new Paint(2));
        try {
            int attributeInt = new ExifInterface(realPathFromURI).getAttributeInt(TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + attributeInt);
            Matrix matrix2 = new Matrix();
            if (attributeInt == 6) {
                matrix2.postRotate(90.0f);
                Log.d("EXIF", "Exif: " + attributeInt);
            } else if (attributeInt == 3) {
                matrix2.postRotate(180.0f);
                Log.d("EXIF", "Exif: " + attributeInt);
            } else if (attributeInt == 8) {
                matrix2.postRotate(270.0f);
                Log.d("EXIF", "Exif: " + attributeInt);
            }
            bitmap2 = bitmap3;
            return Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix2, true);
        } catch (IOException e4) {
            bitmap2 = bitmap3;
            e4.printStackTrace();
            return bitmap2;
        }
    }
    private static Bitmap createSquaredBitmap(Bitmap srcBmp) {
        int dim = Math.max(srcBmp.getWidth(), srcBmp.getHeight());
        Bitmap dstBmp = Bitmap.createBitmap(dim, dim, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(dstBmp);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(srcBmp, (dim - srcBmp.getWidth()) / 2, (dim - srcBmp.getHeight()) / 2, null);

        return dstBmp;
    }
}
