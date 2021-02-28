package com.app.tryitat.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
}
