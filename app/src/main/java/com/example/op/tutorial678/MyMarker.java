package com.example.op.tutorial678;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by OP on 3/19/2017.
 */

public class MyMarker {
    LatLng position;
    String title;
    Bitmap icon;

    public MyMarker(LatLng position, String title, Bitmap icon){
        this.position = position;
        this.title = title;
        this.icon = icon;
    }
    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
}
