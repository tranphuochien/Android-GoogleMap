package com.example.op.tutorial678;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.op.tutorial678.Model.MultiDrawable;
import com.example.op.tutorial678.Model.Person;
import com.example.op.tutorial678.ModuleDirection.DirectionFinder;
import com.example.op.tutorial678.ModuleDirection.Distance;
import com.example.op.tutorial678.ModuleDirection.Duration;
import com.example.op.tutorial678.ModuleDirection.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomMarker extends FragmentActivity implements OnMapReadyCallback,
        ClusterManager.OnClusterClickListener<Person>,
        ClusterManager.OnClusterInfoWindowClickListener<Person>,
        ClusterManager.OnClusterItemClickListener<Person>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Person>  {

    private GoogleMap mMap;
    private ClusterManager<Person> mClusterManager;
    private Random mRandom = new Random(1984);
    private View convertView;
    private OnInterInfoWindowTouchListener lsClick;
    private static final String URL_REQUEST = "http://www.vneasyrider.com/dummydb.json";
    MapWrapperLayout mapWrapperLayout;
    private HashMap<String, Person> markerPersonMap= new HashMap<>();

    private static final CharSequence[] MAP_TYPE_ITEMS =
            {"Road Map", "Hybrid", "Satellite", "Terrain"};

    public void onClickBtnTypeMap(View view) {
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
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    private class PersonRenderer extends DefaultClusterRenderer<Person> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        //private final ImageView mImageView;
        private final CircleImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public PersonRenderer() {
            super(getApplicationContext(), mMap, mClusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new CircleImageView(getApplicationContext()); //mImageView = new ImageView(getApplicationContext());

            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);

            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(Person person, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.

            //mImageView.setImageResource(person.profilePhoto);
            mImageView.setImageResource(person.profilePhoto);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(person.name);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Person> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (Person p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                Drawable drawable = getResources().getDrawable(p.profilePhoto);
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }

        @Override
        protected void onClusterItemRendered(Person person, Marker marker) {
            super.onClusterItemRendered(person, marker);
            markerPersonMap.put(marker.getId(), person);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_marker);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_wrapper);
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

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 9.5f));
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
        mClusterManager = new ClusterManager<Person>(this, mMap);
        mClusterManager.setRenderer(new PersonRenderer());
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);

        convertView = LayoutInflater.from(CustomMarker.this).inflate(R.layout.content_location, null);
        Button detailBtn = (Button)convertView.findViewById(R.id.detailBtn);

        lsClick = new OnInterInfoWindowTouchListener(detailBtn) {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                Person person = markerPersonMap.get(marker.getId());

                Intent intent = new Intent(CustomMarker.this, Profile.class );

                intent.putExtra("phoneNumber","+841267861996");
                intent.putExtra("name", person.getName());
                intent.putExtra("email", person.getEmail());
                intent.putExtra("idAvatar", person.getProfilePhoto() );
                startActivity(intent);
            }
        };
        detailBtn.setOnTouchListener(lsClick);

        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {return null; }

            @Override
            public View getInfoContents(Marker marker) {
                Person person = markerPersonMap.get(marker.getId());
                lsClick.setMarker(marker);
                TextView name= (TextView)convertView.findViewById(R.id.nameLocation);
                TextView location = (TextView)convertView.findViewById(R.id.location);
                TextView email = (TextView)convertView.findViewById(R.id.email);

                name.setText(person.getName());
                location.setText(person.getPosition().toString());
                email.setText(person.getEmail());

                mapWrapperLayout.setMarkerWithInfoWindow(marker, convertView);
                return convertView;
            }
        });

        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        try {
            addItems();
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mClusterManager.cluster();
    }

    @Override
    public boolean onClusterClick(Cluster<Person> cluster) {
        // Show a toast with some info when the cluster is clicked.
        String firstName = cluster.getItems().iterator().next().name;
        Toast.makeText(this, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Person> cluster) {
        // Does nothing, but you could go to a list of the users.
    }

    @Override
    public boolean onClusterItemClick(Person item) {
        Toast.makeText(CustomMarker.this, "Hello " + item.getName(),Toast.LENGTH_SHORT ).show();
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Person item) {
    }

    private void addItems() throws JSONException, UnsupportedEncodingException {
        //Create dummy data to test
        //createDummyData();

        //Read data from json file in resource
        /*
        InputStream inputStream = getResources().openRawResource(R.raw.radar_search);
        List<Person> items = new MyItemReader().read(inputStream);
        mClusterManager.addItems(items);
        */

        //Read data from httpRequest
        new DownloadRawData().execute(URL_REQUEST);
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {
        private ArrayList<Integer> listAvatars = new ArrayList<>();
        private Random random = new Random();
        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                return buffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            //parseJSon(res);
            List<Person> items = new ArrayList<>();

            try {
                items = parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mClusterManager.addItems(items);
        }

        private List<Person> parseJSon(String res) throws JSONException {
            if (res == null)
                return null;

            int n = 0;
            List<Person> items = new ArrayList<>();
            listAvatars.add(R.drawable.avatar);
            listAvatars.add(R.drawable.avatar2);
            listAvatars.add(R.drawable.avatar3);
            listAvatars.add(R.drawable.bighero6);
            listAvatars.add(R.drawable.ff8);
            listAvatars.add(R.drawable.avatar_film);

            String name = null;
            String email = null;
            String phoneNumber = null;

            JSONArray array = new JSONArray(res);
            n = array.length();
            for (int i = 0; i < n; i++) {
                JSONObject object = array.getJSONObject(i);
                double lat = object.getDouble("lat");
                double lng = object.getDouble("lng");
                //pictureResource =
                if (!object.isNull("name")) {
                    name = object.getString("name");
                }
                if (!object.isNull("email")) {
                    email = object.getString("email");
                }
                if (!object.isNull("phoneNumber")) {
                    phoneNumber = object.getString("phoneNumber");
                }
                int idx = random.nextInt(listAvatars.size());
                items.add(new Person(new LatLng(lat, lng), name, email, phoneNumber, listAvatars.get(idx)));
            }
            return items;
        }
    }

    private void createDummyData() {
        mClusterManager.addItem(new Person(position(), "Dog1","dog1@gmail.com", "+841267861996", R.drawable.avatar));
        mClusterManager.addItem(new Person(position(), "Dog2","dog1@gmail.com", "+841267861996", R.drawable.avatar2));
        mClusterManager.addItem(new Person(position(), "Dog3","dog1@gmail.com", "+841267861996", R.drawable.avatar3));
        mClusterManager.addItem(new Person(position(), "Dog4","dog1@gmail.com", "+841267861996", R.drawable.avatar));
        mClusterManager.addItem(new Person(position(), "Dog5","dog1@gmail.com", "+841267861996", R.drawable.avatar2));
        mClusterManager.addItem(new Person(position(), "Dog6","dog1@gmail.com", "+841267861996", R.drawable.avatar3));
        mClusterManager.addItem(new Person(position(), "Dog7","dog1@gmail.com", "+841267861996", R.drawable.avatar));
        mClusterManager.addItem(new Person(position(), "Dog8","dog1@gmail.com", "+841267861996", R.drawable.avatar2));
        mClusterManager.addItem(new Person(position(), "Dog9","dog1@gmail.com", "+841267861996", R.drawable.avatar3));
    }

    private LatLng position() {
        return new LatLng(random(51.6723432, 51.38494009999999), random(0.148271, -0.3514683));
    }

    private double random(double min, double max) {
        return mRandom.nextDouble() * (max - min) + min;
    }
}
