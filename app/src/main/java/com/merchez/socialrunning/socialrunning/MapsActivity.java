package com.merchez.socialrunning.socialrunning;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.merchez.socialrunning.socialrunning.Fragments.DrawerFragment;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GlobalState gs;
    private DrawerFragment drawer;

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private List<LatLng> lstLatLngs;
    private Button btnVisu;
    private Button btnValid;
    private Button btnVide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);


        drawer = new DrawerFragment();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gs = (GlobalState) getApplication();

        lstLatLngs = new ArrayList<LatLng>();

        btnVisu = (Button) findViewById(R.id.btn_visu);
        btnVisu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String q="";
                for(LatLng point : lstLatLngs){
                    if(lstLatLngs.indexOf(point) ==0) q+="saddr="+point.latitude+","+point.longitude+"&";
                    else if(lstLatLngs.indexOf(point) ==1) q+="daddr="+point.latitude+","+point.longitude+"";
                    else q+="+to:"+point.latitude+","+point.longitude;
                }


                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?"+q+"&units=metric&mode=walking"));
                startActivity(intent);
            }
        });

        btnValid = (Button) findViewById(R.id.btn_valid);
        btnValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("points", lstLatLngs.toString());
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        btnVide = (Button) findViewById(R.id.btn_vide);
        btnVide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
            }
        });
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

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            gs.alerter("erreur");
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                lstLatLngs.add(point);
                //mMap.clear();
                mMap.addMarker(new MarkerOptions().position(point));
            }
        });
    }
}
