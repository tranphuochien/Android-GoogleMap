package com.example.op.tutorial678;

import android.graphics.Bitmap;

/**
 * Created by OP on 3/14/2017.
 */

public class Trailer {
    String name;
    String url;
    Bitmap image;

    public  Trailer(String name, String url, Bitmap image)
    {
        this.name = name;
        this.url = url;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
