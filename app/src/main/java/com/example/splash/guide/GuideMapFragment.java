package com.example.splash.guide;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.splash.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GuideMapFragment extends Fragment implements OnMapReadyCallback {

    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;
    int REQUEST_CODE = 111;

    GoogleMap mMap;
    Button sendLocation;
    Marker marker;
    Geocoder geocoder;
    double selectedLat,selectedLng;
    List<Address> addresses;
    String selectedAddress;
    SendLocation sendLoc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_guide_map, container, false);

        sendLocation = inflate.findViewById(R.id.location_button);

        mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getCurrentLocation();
        }
        else{
            ActivityCompat.requestPermissions((getActivity()), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        return inflate;
    }

    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){

                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            mMap = googleMap;

                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
                            googleMap.addMarker(markerOptions).showInfoWindow();

                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(@NonNull LatLng latLng) {
                                    selectedLat = latLng.latitude;
                                    selectedLng = latLng.longitude;

                                    geocoder = new Geocoder(getActivity(), Locale.getDefault());

                                    if(selectedLat != 0){
                                        try {
                                            addresses = geocoder.getFromLocation(selectedLat, selectedLng, 1);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        if(addresses != null){
                                            String mAddress = addresses.get(0).getAddressLine(0);

                                            selectedAddress = (String) mAddress;

                                            if(mAddress != null){
                                                MarkerOptions markerOptions = new MarkerOptions();
                                                LatLng latLng1 = new LatLng(selectedLat, selectedLng);

                                                markerOptions.position(latLng).title(selectedAddress);

                                                mMap.addMarker(markerOptions).showInfoWindow();
                                            }
                                            else{
                                                Toast.makeText(getActivity(), "Неуспешно!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else{
                                            Toast.makeText(getActivity(), "Неуспешно", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                    else{
                                        Toast.makeText(getActivity(), "Latlng null!", Toast.LENGTH_SHORT).show();

                                    }

                                    sendLocation.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sendLoc.sendLoc(selectedAddress);
                                        }
                                    });

                                }
                            });
                        }
                    });
                }
            }
        });
    }

    interface SendLocation{
        void sendLoc (String address);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            sendLoc = (SendLocation) getActivity();
        }
        catch (ClassCastException e){
            throw new ClassCastException("Error in retrieving data!");
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}