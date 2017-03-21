package com.example.op.tutorial678;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.op.tutorial678.Model.MyItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

public class MyMap extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ArrayList<MyMarker> listMarkers = new ArrayList<>();
    private LatLng here;

    private ClusterManager<MyItem> mClusterManager;

    private void setUpClusterer() {
        // set some feature of map
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(51.503186, -0.126446))      // Sets the center of the map to HCMUS
                .zoom(10)                   // Sets the zoom (1<= zoom <= 20)
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();
        // create an animation for map while moving to this location
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 4000, null);
        // do animation to move to this location
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            MyItem offsetItem = new MyItem(lat, lng, "hihi", "hehe");
            mClusterManager.addItem(offsetItem);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        setUpClusterer();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private void addListMarkers() {
        Bitmap bm = BitMapHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.marker, 24, 24);

        listMarkers.add(new MyMarker(new LatLng(10.762668, 106.682554), "HCMUS", bm));
        listMarkers.add(new MyMarker(new LatLng(10.7627585, 106.686009), "Sư phạm", bm));
        listMarkers.add(new MyMarker(new LatLng(10.7649774, 106.682642), "Trần Phú", bm));
        listMarkers.add(new MyMarker(new LatLng(10.7634175, 106.685957), "Nowzone", bm));
        listMarkers.add(new MyMarker(new LatLng(10.7618154, 106.685743), "Nguyễn trãi", bm));
        listMarkers.add(new MyMarker(new LatLng(10.762968, 106.682854), "HCMUS", bm));
        listMarkers.add(new MyMarker(new LatLng(10.7627585, 106.686009), "Sư phạm", bm));
        listMarkers.add(new MyMarker(new LatLng(10.7649874, 106.685642), "Trần Phú", bm));
        listMarkers.add(new MyMarker(new LatLng(10.7634375, 106.685157), "Nowzone", bm));
        listMarkers.add(new MyMarker(new LatLng(10.7618854, 106.685443), "Nguyễn trãi", bm));

        int n = listMarkers.size();

        for (int i = 0; i < n; i++) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(listMarkers.get(i).getPosition())
                    .title(listMarkers.get(i).getTitle())
                    .icon(BitmapDescriptorFactory.fromBitmap(listMarkers.get(i).getIcon()))
                    .draggable(true)
            );
            marker.setTag(0);
        }

    }
}
