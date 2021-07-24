package com.svv.localsports.controlador;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.svv.localsports.R;


import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class map extends Fragment implements OnMapReadyCallback {
    GoogleMap mMap;
    MapView mapView;
    public map() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.mapF);

        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap =googleMap;
        LatLng casa = new LatLng(40.468935, -3.725771);
        googleMap.addMarker(new MarkerOptions()
                .position(casa)
                .title("Pisuco en Madrid"));
        LatLng casa_padres = new LatLng(42.895605, -8.533197);
        googleMap.addMarker(new MarkerOptions()
                .position(casa_padres)
                .title("Casa de Santiago"));
        LatLng oficina = new LatLng(40.4535, -3.726);
        googleMap.addMarker(new MarkerOptions()
                .position(oficina)
                .title("Oficina en la escuela"));

    }
}
