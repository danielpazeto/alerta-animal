package pazeto.apps.alertaanimal.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pazeto.alertaanimal.DTO.Alert;
import pazeto.alertaanimal.model.FilterObject;
import pazeto.apps.alertaanimal.R;
import pazeto.apps.alertaanimal.connection.AlertServerRequest;
import pazeto.apps.alertaanimal.preferences.FilterStoreManager;

public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnCameraIdleListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private FloatingActionButton filterBtn;
    private Button addAlertBtn;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private Marker userLocationSelectMarker;
    private String TAG = MapsFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maps_fragment, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        filterBtn = (FloatingActionButton) view.findViewById(R.id.filter_button);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent filterIntent = new Intent(getActivity(), FilterDialogActivity.class);
                startActivityForResult(filterIntent, FilterDialogActivity.NEW_FILTER_REQUEST_CODE);
            }
        });

        addAlertBtn = (Button) view.findViewById(R.id.add_animal_alert);
        addAlertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userLocationSelectMarker!=null){
                    mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                        @Override
                        public void onSnapshotReady(Bitmap bitmap) {

                            Intent i = new Intent(getActivity(), AlertActivity.class);
                            ByteArrayOutputStream bs = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
                            i.putExtra("byteArray", bs.toByteArray());

                            i.putExtra("lat", userLocationSelectMarker.getPosition().latitude);
                            i.putExtra("lng", userLocationSelectMarker.getPosition().longitude);

                            startActivity(i);

                        }
                    });
                }else{
                    Toast.makeText(getActivity(), getString(R.string.select_map_location),
                            Toast.LENGTH_LONG).show();
                }


//                Intent filterIntent = new Intent(MapsFragment.this, FilterDialogActivity.class);
//                startActivity(filterIntent);
                //TODO com fazer essa tela de adicionar alerta THINK ABOUT THAT
            }
        });

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);
        }


        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(MapsFragment.this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        return view;
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
        getView().findViewById(R.id.map_layout).setVisibility(View.VISIBLE);

        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setOnCameraIdleListener(this);
    }

    int MY_LOCATION_REQUEST_CODE = 5854;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
//                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        Log.d("Map", "Map clicked");

        if(userLocationSelectMarker != null) {
            userLocationSelectMarker.remove();
        }
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(point.latitude, point.longitude))
                .zoom(20).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));


        userLocationSelectMarker = mMap.addMarker(new MarkerOptions()
                .position(point)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_dialog_alert))
                .title("Existe um animal aqui?"));
        userLocationSelectMarker.showInfoWindow();

        mMap.isMyLocationEnabled();
        addAlertBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case FilterDialogActivity.NEW_FILTER_REQUEST_CODE:
                fetchData();
                break;
            case AlertActivity.NEW_ALERT_REQUEST_CODE:
//                if(resultCode == RESULT_OK){
//                    mMap.clear();
//                }
                break;

        }

    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

        return false;
    }

    @Override
    public void onCameraIdle() {
        fetchData();
    }

    private void fetchData() {
        Log.d(TAG, "Fetching data...");
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

        Location center = new Location("center");
        center.setLatitude(bounds.getCenter().latitude);
        center.setLongitude(bounds.getCenter().longitude);

        Location northeast = new Location("northeast");
        northeast.setLatitude(bounds.northeast.latitude);
        northeast.setLongitude(bounds.northeast.longitude);

        float km = center.distanceTo(northeast) / 1000;
        Log.d(TAG, center.toString());
        Log.d(TAG, northeast.toString());
        Log.d(TAG, "Obtaining data from "+ km + " km distance");

        FilterObject filter = FilterStoreManager.getInstance(getActivity()).getFilter();

        if(filter.getLat() == 0 && filter.getLng() == 0){
            filter.setLat((float)center.getLatitude());
            filter.setLng((float)center.getLongitude());
            filter.setRange(km);
        }
        AlertServerRequest.getInstance(getActivity()).listAlertByFilter(listAlertCallback, filter);
    }

    private Response.Listener<List<Alert>> listAlertCallback = new Response.Listener<List<Alert>>() {
        @Override
        public void onResponse(List<Alert> alerts) {
            addAlertMarkersToMap(alerts, true);
        }
    };


    private Set<Marker> currentScreenMarkers;

    private void addAlertMarkersToMap(List<Alert> alerts, boolean clearBefore){
        if(clearBefore) {
            if(currentScreenMarkers != null){
                for (Marker marker : currentScreenMarkers) {
                    marker.remove();
                }
            }else{
                currentScreenMarkers = new HashSet<>();
            }
        }
        for (Alert alert : alerts) {
            currentScreenMarkers.add(addMarker(alert.getLat(), alert.getLng(), alert.getAnimal().getName(), null, 0));
        }
    }

    protected Marker addMarker(double latitude, double longitude, String title, String snippet, int iconResID) {

        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title));
//                .snippet(snippet)
//                .icon(BitmapDescriptorFactory.fromResource(iconResID)));
    }
    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() { // Disconnecting the client invalidates it.
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        // only stop if it's connected, otherwise we crash
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    public void onConnected(Bundle dataBundle) {
        // Get last known recent location.
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // Note that this can be NULL if last location isn't already known.
        if (mCurrentLocation != null) {
            // Print current location if not null
            Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        }
        // Begin polling for new location updates.
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(getActivity(), "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(getActivity(), "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    public void onLocationChanged(Location location) {
        //go to location just case user still have not selected one location
        if(userLocationSelectMarker == null) {
            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(15).build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));

        }
        if (mGoogleApiClient != null) {
            Log.d(TAG, "Disable location updates..");
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, connectionResult.getErrorMessage());
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
}
