package com.android.recyclerview;

import android.graphics.Bitmap;

/**
 * Created by Maheswari_V on 1/7/2017.
 */

public class ImageDescriptor{

    public String workOwner;
    public String workLogTime;
    public Bitmap workLogImage;

    public ImageDescriptor(){}

    public ImageDescriptor(String workOwner, String workLogTime, Bitmap workLogImage)
    {
        this.workOwner = workOwner;
        this.workLogTime = workLogTime;
        this.workLogImage = workLogImage;
    }

    public void setWorkOwner(String workOwner) {
        this.workOwner = workOwner;
    }

    public void setWorkLogImage(Bitmap workLogImage) {
        this.workLogImage = workLogImage;
    }

    public void setWorkLogTime(String workLogTime) {
        this.workLogTime = workLogTime;
    }

    public String getWorkOwner() {
        return workOwner;
    }

    public String getWorkLogTime() {
        return workLogTime;
    }

    public Bitmap getWorkLogImage() {
        return workLogImage;
    }
}