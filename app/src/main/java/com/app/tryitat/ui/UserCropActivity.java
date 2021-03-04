package com.app.tryitat.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.app.tryitat.R;
import com.app.tryitat.ui.addpost.AddNewPostActivity;
import com.app.tryitat.ui.dashboard.user.UserDashboardActivity;
import com.app.tryitat.utils.ImageLoadingUtils;
import com.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION;
import static com.app.tryitat.utils.Common.getCompressed;

public class UserCropActivity extends Activity {
    private static final int GUIDELINES_ON_TOUCH = 1;
    CropImageView cropImageView;
    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_crop);
        cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        File f = new File(getIntent().getStringExtra("img_url"));
        Uri photoURI = FileProvider.getUriForFile(this, getPackageName() + ".provider", f);
        getBitmap(getIntent().getStringExtra("img_url"));
        cropImageView.setAspectRatio(10, 10);
        cropImageView.setAspectRatio(10, 10);
        cropImageView.setGuidelines(0);
        cropImageView.setFixedAspectRatio(true);

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        findViewById(R.id.Button_crop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bitmap croppedImage = cropImageView.getCroppedImage();

                File N_file = null;
                try {
                    N_file = getCompressed(UserCropActivity.this, croppedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String u_profile = N_file.getPath();
                Intent intent = new Intent(UserCropActivity.this, AddNewPostActivity.class);
                intent.putExtra("img_url", u_profile);
                startActivity(intent);
                finish();
                // croppedImageView.setImageBitmap(croppedImage);
            }
        });
    }

    public Bitmap getBitmap(String path) {
        Bitmap bitmap = null;
        try {
            File f = new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            cropImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static File getCompressed(Context context, Bitmap path) throws IOException {
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
        path.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
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
}