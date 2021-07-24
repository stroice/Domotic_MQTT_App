package com.svv.localsports.controlador;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.widget.Button;
import android.widget.Switch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.svv.localsports.MainActivity;
import com.svv.localsports.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import pl.pawelkleczkowski.customgauge.CustomGauge;

/**
 * A simple {@link Fragment} subclass.
 */


public class temperature extends Fragment {

    //Control de temperatura, primer página
    //Card 1
    Switch Switchauto;
    Switch Switchcaldera;
    Switch Switchaire;

    //Carta 2
    Button boton_suma;
    Button boton_resta;
    TextView temp_target;

    Double termostato = 21.5;
    int termostato_int = 21500;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Código de controles de climatización:

        //Carta1, control de switches
        View view = inflater.inflate(R.layout.fragment_temperature, container, false);

        Switchauto = (Switch) view.findViewById(R.id.Sw_auto);
        Switchcaldera = (Switch) view.findViewById(R.id.Sw_caldera);
        Switchaire = (Switch) view.findViewById(R.id.Sw_aire);

        Switchauto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Switchauto.isChecked()){
                    try {
                        MqttMessage message = new MqttMessage(("{\"auto\":1}").getBytes());
                        MainActivity.client.publish("temperatura", message);
                        message = new MqttMessage(("{\"cale\":0}").getBytes());
                        MainActivity.client.publish("temperatura", message);
                        message = new MqttMessage(("{\"aire\":0}").getBytes());
                        MainActivity.client.publish("temperatura", message);

                        Switchcaldera.setChecked(false);
                        Switchaire.setChecked(false);
                    }
                    catch(MqttException e){
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        MqttMessage message = new MqttMessage(("{\"auto\":0}").getBytes());
                        MainActivity.client.publish("temperatura", message);

                    }
                    catch(MqttException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        Switchcaldera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Switchcaldera.isChecked()){
                    try {
                        MqttMessage message = new MqttMessage(("{\"cale\":1}").getBytes());
                        MainActivity.client.publish("temperatura", message);
                        message = new MqttMessage(("{\"auto\":0}").getBytes());
                        MainActivity.client.publish("temperatura", message);
                        message = new MqttMessage(("{\"aire\":0}").getBytes());
                        MainActivity.client.publish("temperatura", message);
                        Switchauto.setChecked(false);
                        Switchaire.setChecked(false);
                    }
                    catch(MqttException e){
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        MqttMessage message = new MqttMessage(("{\"cale\":0}").getBytes());
                        MainActivity.client.publish("temperatura", message);

                    }
                    catch(MqttException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        Switchaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Switchaire.isChecked()){
                    try {
                        MqttMessage message = new MqttMessage(("{\"aire\":1}").getBytes());
                        MainActivity.client.publish("temperatura", message);
                        message = new MqttMessage(("{\"cale\":0}").getBytes());
                        MainActivity.client.publish("temperatura", message);
                        message = new MqttMessage(("{\"auto\":0}").getBytes());
                        MainActivity.client.publish("temperatura", message);

                        Switchcaldera.setChecked(false);
                        Switchauto.setChecked(false);
                    }
                    catch(MqttException e){
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        MqttMessage message = new MqttMessage(("{\"aire\":0}").getBytes());
                        MainActivity.client.publish("temperatura", message);
                    }
                    catch(MqttException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        //Fin de código de activacion carta1


        //Carta2, control de temperatura objetarivo
        boton_suma = (Button) view.findViewById(R.id.button_suma);
        boton_resta = (Button) view.findViewById(R.id.button_resta);
        temp_target = (TextView) view.findViewById(R.id.Termostato_value);

        boton_suma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    termostato = termostato + 0.1;
                    termostato_int = (int) (termostato * 1000);
                    String term_text = String.format ("%.1f", termostato);
                    temp_target.setText(term_text + "ºC");
                    MqttMessage message = new MqttMessage(("{\"temp_target\":" + termostato_int + "}").getBytes());
                    MainActivity.client.publish("temperatura", message);
                    }
                    catch(MqttException e){
                        e.printStackTrace();
                    }
            }
        });

        boton_resta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    termostato = termostato - 0.1;
                    termostato_int = (int) (termostato * 1000);
                    String term_text = String.format ("%.1f", termostato);
                    temp_target.setText(term_text + "ºC");
                    MqttMessage message = new MqttMessage(("{\"temp_target\":" + termostato_int + "}").getBytes());
                    MainActivity.client.publish("temperatura", message);
                }
                catch(MqttException e){
                    e.printStackTrace();
                }
            }
        });






        // Inflate the layout for this fragment
        return view;

    }


}
