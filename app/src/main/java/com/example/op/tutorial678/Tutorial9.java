package com.example.op.tutorial678;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

public class Tutorial9 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    MapWrapperLayout mapWrapperLayout;
    View convertView;
    Button detailBtn;
    OnInterInfoWindowTouchListener lsClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial9);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_wrapper);
    }

    private static final CharSequence[] MAP_TYPE_ITEMS =
            {"Road Map", "Hybrid", "Satellite", "Terrain"};

    private void showMapTypeSelectorDialog() {
        // Prepare the dialog by setting up a Builder.
        final String fDialogTitle = "Select Map Type";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(fDialogTitle);

        // Find the current map type to pre-check the item representing the current state.
        int checkItem = mMap.getMapType() - 1;

        // Add an OnClickListener to the dialog, so that the selection will be handled.
        builder.setSingleChoiceItems(
                MAP_TYPE_ITEMS,
                checkItem,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int item) {
                        // Locally create a finalised object.
                        // Perform an action depending on which item was selected.
                        switch (item) {
                            case 1:
                                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                break;
                            case 2:
                                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                break;
                            case 3:
                                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                break;
                            default:
                                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        }
                        dialog.dismiss();
                    }
                }
        );

        // Build the dialog and show it.
        AlertDialog fMapTypeDialog = builder.create();
        fMapTypeDialog.setCanceledOnTouchOutside(true);
        fMapTypeDialog.show();
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
        mapWrapperLayout.init(mMap, this);

        // enable the button to move to your current location on map
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        showMapTypeSelectorDialog();

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);



        // Add some places on map
        // create a LatLng object = position
        LatLng hcmus = new LatLng(10.762668, 106.682554);

        Bitmap bm = BitMapHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.marker, 24,24);

        // add a marker
        mMap.addMarker(new MarkerOptions()
                        .position(hcmus)
                        .title("HoChiMinh City University of Science")
                        .icon(BitmapDescriptorFactory.fromBitmap(bm)) // ypu can add other bitmap to marker
                // such as avartar of this location or some special marker
                // for an example: when you create marker for beatiful places, you can use its bitmap as an icon
        );

        // set some feature of map
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(hcmus)      // Sets the center of the map to HCMUS
                .zoom(18)                   // Sets the zoom (1<= zoom <= 20)
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();
        // create an animation for map while moving to this location
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
        // do animation to move to this location
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        convertView = LayoutInflater.from(Tutorial9.this).inflate(R.layout.content_location, null);
        detailBtn = (Button)convertView.findViewById(R.id.detailBtn);
        lsClick = new OnInterInfoWindowTouchListener(detailBtn) {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                Log.d("INFO WINDOW", "Clicked");
                Toast.makeText(Tutorial9.this, "dvfd", Toast.LENGTH_SHORT).show();
            }
        };
        detailBtn.setOnTouchListener(lsClick);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                lsClick.setMarker(marker);
                TextView name= (TextView)convertView.findViewById(R.id.nameLocation);
                TextView location = (TextView)convertView.findViewById(R.id.location);

                name.setText(marker.getTitle());
                location.setText(marker.getPosition().toString());

                mapWrapperLayout.setMarkerWithInfoWindow(marker, convertView);
                return  convertView;
            }
        });
    }
}
