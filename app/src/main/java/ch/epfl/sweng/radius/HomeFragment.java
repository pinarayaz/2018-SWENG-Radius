package ch.epfl.sweng.radius;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements OnMapReadyCallback, RadiusCircle {

    //constants
    private static final String TAG = "HomeFragment";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final double DEFAULT_RADIUS = 1500;

    //properties
    private boolean mLocationPermissionGranted = false;
    private MapView mapView;
    private GoogleMap mMap;
    private Location currentLocation;
    private CircleOptions radiusOptions;
    private Circle radiusCircle;
    private FusedLocationProviderClient mFusedLocationClient;

    //testing
    private ArrayList<User> users;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        users = new ArrayList<User>();
        users.add(new User(46.518532, 6.556455));
        users.add(new User(46.519331, 6.580971));
        users.add(new User(48.854457, 2.348560));
        //radiusCircle = new Circle( 5);
        getLocationPermission();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getContext(), "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            //markNearbyUsers(); // I don't know if this should go here
        }
    }

    private void getDeviceLocation() {
        Log.d( TAG, "getDeviceLocation: getting the device's current location");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient( getActivity());

        try {
            if ( mLocationPermissionGranted) {
                Task location = mFusedLocationClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if ( task.isSuccessful()) {
                            Log.d( TAG, "onComplete: found location.");
                            currentLocation = (Location) task.getResult();

                            System.out.println( currentLocation.getLatitude() + " " + currentLocation.getLongitude());

                            LatLng currentCoordinates = new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude());
                            radiusOptions = new CircleOptions().center(currentCoordinates).strokeColor(Color.RED).fillColor(Color.parseColor("#22FF0000")).radius(DEFAULT_RADIUS);
                            radiusCircle = mMap.addCircle(radiusOptions);
                            markNearbyUsers();
                            moveCamera( currentCoordinates, DEFAULT_ZOOM);
                        }
                        else {
                            Log.d( TAG, "onComplete: current location is null.");
                            System.out.println( "cry you little baby");
                            Toast.makeText( getContext(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch ( SecurityException e) {
            Log.e( TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d( TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + " long: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom( latLng, zoom));
    }

    private void getLocationPermission() {
        Log.d( TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if ( ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if ( ContextCompat.checkSelfPermission( getContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            }
            else {
                ActivityCompat.requestPermissions( getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else {
            ActivityCompat.requestPermissions( getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d( TAG, "onRequestPermissionResult: called.");
        mLocationPermissionGranted = false;

        switch ( requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if ( grantResults.length > 0) {
                    for ( int i = 0; i < grantResults.length; i++) {
                        if ( grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Log.d( TAG, "onRequestPermissionResult: permission denied.");
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    Log.d( TAG, "onRequestPermissionResult: permission granted.");
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    public double getRadius() {
        return radiusCircle.getRadius();
    }

    public double getLatitude() {
        return currentLocation.getLatitude();
    }

    public double getLongtitude() {
        return currentLocation.getLongitude();
    }

    public void setRadius(double radius) {
        radiusCircle.setRadius(radius);
    }

    public void setLatitude(double latitude) {
        currentLocation.setLatitude(latitude);
    }

    public void setLongtitude(double longtitude) {
        currentLocation.setLongitude(longtitude);
    }

    public boolean contains(double p2latitude, double p2longtitude) {
        double distance = findDistance(p2latitude, p2longtitude);

        if ( radiusCircle.getRadius() >= distance) {
            return true;
        }

        return false;
    }

    public double findDistance(double p2latitude, double p2longtitude) {
        float[] distance = new float[3];
        Location.distanceBetween( currentLocation.getLatitude(), currentLocation.getLongitude(), p2latitude, p2longtitude, distance);

        return distance[0];
    }

    public void markNearbyUsers() {
        for (int i = 0; i < users.size(); i++) {
            if ( contains(users.get(i).getLocation().latitude, users.get(i).getLocation().longitude) ) {
                mMap.addMarker(new MarkerOptions().position(users.get(i).getLocation()).title(users.get(i).getUserName() + ": "  + users.get(i).getStatus()));
            }
        }

    }

}
