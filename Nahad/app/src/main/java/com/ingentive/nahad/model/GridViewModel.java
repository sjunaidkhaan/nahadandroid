package com.ingentive.nahad.model;

import android.graphics.Bitmap;

/**
 * Created by PC on 03-06-2016.
 */
public class GridViewModel {
    Bitmap image;
    int pageNo;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public GridViewModel() {
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
