package com.svv.localsports.controlador;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.svv.localsports.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class air_quality extends Fragment {

    public air_quality() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_airqual, container, false);
    }
}
