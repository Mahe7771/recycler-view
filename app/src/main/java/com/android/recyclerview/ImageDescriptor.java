package com.android.recyclerview;

/**
 * Created by Maheswari_V on 1/7/2017.
 */

public class ImageDescriptor{

    public String imgName;
    public String tagName;
    public String tagValue;

    public ImageDescriptor(){}

    public ImageDescriptor(String imgName, String tagName, String tagValue)
    {
        this.imgName = imgName;
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public String getImgName() {
        return imgName;
    }

    public String getTagName() {
        return tagName;
    }

    public String getTagValue() {
        return tagValue;
    }
}