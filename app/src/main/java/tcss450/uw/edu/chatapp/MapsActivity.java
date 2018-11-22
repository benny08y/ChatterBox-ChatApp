package tcss450.uw.edu.chatapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mCurrentLocation = getIntent().getParcelableExtra("LOCATION");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in the current device location and move the camera
        LatLng current = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(current).title("Current Location"));
        //Zoom levels are from 2.0f (zoomed out) to 21.f (zoomed in)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15.0f));

        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.map);
        if (!(currentFragment instanceof CurrentConditionsLatLngFragment)) {
            Log.d("LAT/LONG", latLng.toString());

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("New Marker"));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));

            View v = findViewById(android.R.id.content);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
            alertDialog.setTitle("Change location?");
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            alertDialog.setPositiveButton("Select",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            CurrentConditionsLatLngFragment currentConditionsLatLngFragment = new CurrentConditionsLatLngFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("lat", latLng.latitude);
                            bundle.putSerializable("lon", latLng.longitude);
                            currentConditionsLatLngFragment.setArguments(bundle);
                            FragmentTransaction transaction = getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.map, currentConditionsLatLngFragment)
                                    .addToBackStack(null);
                            transaction.commit();
                        }
                    });
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }
    }
}
