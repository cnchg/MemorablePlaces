package com.tricloudcommunications.ce.memorableplaces;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, LocationListener {

    private GoogleMap mMap;

    int selectedLocation = -1;

    LocationManager locationManager;
    String provider;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        selectedLocation = i.getIntExtra("LocationInfo", -1);
        Log.i("LocationInfo", Integer.toString(selectedLocation));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(provider);

        try {
            if (location != null) {

                onLocationChanged(location);

            }
        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Getting your location... Please Wait", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                // When app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnMapLongClickListener(this);


        /** use this code if you are NOT making the first item in the List View to add a new location */
        if (selectedLocation != -1) {

            //Stop Location Updates
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(this);

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MainActivity.locationCoords.get(selectedLocation), 20));
            mMap.addMarker(new MarkerOptions().position(MainActivity.locationCoords.get(selectedLocation)).title(MainActivity.locationList.get(selectedLocation)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            mMap.addCircle(new CircleOptions().center(MainActivity.locationCoords.get(selectedLocation)).radius(3).strokeColor(Color.RED).fillColor(Color.BLUE));
        } else {

            locationManager.requestLocationUpdates(provider, 400, 1, this);
            onLocationChanged(location);

            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 18));
            //mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("You Are Here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            //mMap.addCircle(new CircleOptions().center(new LatLng(location.getLatitude(),location.getLongitude())).radius(10).strokeColor(Color.RED).fillColor(Color.BLUE));

         }

        /** use this code if you are making the firt item in the List View to add a new location
         * Also in MainActivity.java you need to uncommnent //locationList.add("Go Ahead! Add a new place");
         * Also in MainActivity.java you need to uncomment //locationCoords.add(new LatLng(0,0));

         if (selectedLocation != -1 && selectedLocation != 0) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MainActivity.locationCoords.get(selectedLocation), 20));
            mMap.addMarker(new MarkerOptions().position(MainActivity.locationCoords.get(selectedLocation)).title(MainActivity.locationList.get(selectedLocation)));
        }else{

            onLocationChanged(location);
         }
       */

        // Add a marker in Sydney, Australia, and move the camera.
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        String label = new Date().toString();

        try {
            List<Address> listAddress = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (listAddress != null && listAddress.size() > 0){

                label = listAddress.get(0).getAddressLine(0) + " " + listAddress.get(0).getAddressLine(1) + " " + listAddress.get(0).getAddressLine(2);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        MainActivity.locationList.add(label);
        MainActivity.arrayAdapter.notifyDataSetChanged();
        Log.i("Address List", "The Address List Has Been Updated.");
        MainActivity.locationCoords.add(latLng);


        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(label)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));


    }

    @Override
    public void onLocationChanged(Location userLocation) {

        Double lat = userLocation.getLatitude();
        Double lng = userLocation.getLongitude();

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {

            List<Address> addressList = geocoder.getFromLocation(lat,lng,1);

            if (addressList !=null && addressList.size() > 0){

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(addressList.get(0).getAddressLine(0)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng), 18));
                mMap.addCircle(new CircleOptions().center(new LatLng(lat,lng)).radius(10).strokeColor(Color.RED).fillColor(Color.BLUE));

                Log.i("Address Info 0", addressList.get(0).getAddressLine(0));
                Log.i("Address Info 1", addressList.get(0).getAddressLine(1));
                Log.i("Address Info 2", addressList.get(0).getAddressLine(2));

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

        Log.i("Latitude", lat.toString());
        Log.i("Longitude", lng.toString());

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }



}
