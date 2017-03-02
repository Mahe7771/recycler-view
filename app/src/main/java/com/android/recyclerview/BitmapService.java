package com.android.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Maheswari_V on 3/2/2017.
 */

public class BitmapService {



    public Bitmap getBitmapFromFile(Context context,File imgFile, boolean isSubSampleRequired) {
        Bitmap bmp = null;
        if (imgFile.exists()) {
            if(isSubSampleRequired) {
                 Log.d("SubSampling", "Done");
                 bmp = subSampleImage(context, imgFile);
                 return bmp;
            }
            else{
                Log.d("SubSampling", "Not Done");
                bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                return bmp;
            }
        }
        return null;
    }

    public Bitmap subSampleImage(Context context, File f) {
        Bitmap bmp = null;

        try {
            final Resources res = context.getResources();
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResourceStream(res,null, new FileInputStream(f), null, options);
            options.inSampleSize = calculateInSampleSize(options, 150, 150);
            options.inJustDecodeBounds = false;

            bmp = BitmapFactory.decodeResourceStream(res, null, new FileInputStream(f), null, options);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
