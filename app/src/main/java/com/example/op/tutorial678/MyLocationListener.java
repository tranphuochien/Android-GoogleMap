package com.example.op.tutorial678;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by OP on 3/19/2017.
 */

class MyLocationListener implements LocationListener {
    private LatLng position;
    @Override
    public void onLocationChanged(Location loc) {
        position = new LatLng(loc.getLatitude(), loc.getLongitude());
    }
    public LatLng getPosition() {
        return position;
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
