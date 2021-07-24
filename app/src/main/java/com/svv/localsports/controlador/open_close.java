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
public class open_close extends Fragment {

    public open_close() {
        // Required empty public constructor
    }

    //Card 1, control de la luz
    Switch Switchauto_luz;
    Switch Switchon_luz;

    //Card 2, control de la luz
    Switch Switchsubir_persiana;
    Switch Switchbajar_persiana;

    //Card 3, control de la luz
    Switch Switchactiv_cons;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_openclose, container, false);

        //Card 1, control code
        Switchauto_luz = (Switch) view.findViewById(R.id.Sw_luz_auto);
        Switchon_luz = (Switch) view.findViewById(R.id.Sw_luz_on);


        Switchauto_luz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Switchauto_luz.isChecked()){
                    try {
                        MqttMessage message = new MqttMessage(("{\"luz\":2}").getBytes());
                        MainActivity.client.publish("luz", message);

                        Switchon_luz.setChecked(false);
                    }
                    catch(MqttException e){
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        MqttMessage message = new MqttMessage(("{\"luz\":0}").getBytes());
                        MainActivity.client.publish("luz", message);

                    }
                    catch(MqttException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        Switchon_luz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Switchon_luz.isChecked()){
                    try {
                        MqttMessage message = new MqttMessage(("{\"luz\":1}").getBytes());
                        MainActivity.client.publish("luz", message);

                        Switchauto_luz.setChecked(false);
                    }
                    catch(MqttException e){
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        MqttMessage message = new MqttMessage(("{\"luz\":0}").getBytes());
                        MainActivity.client.publish("luz", message);

                    }
                    catch(MqttException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        //Finish control code Card 1

        //Card 2, control code
        Switchsubir_persiana = (Switch) view.findViewById(R.id.Sw_persiana_up);
        Switchbajar_persiana = (Switch) view.findViewById(R.id.Sw_persiana_down);

        Switchsubir_persiana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Switchsubir_persiana.isChecked()){
                    try {
                        MqttMessage message = new MqttMessage(("{\"up\":1}").getBytes());
                        MainActivity.client.publish("persiana", message);
                        message = new MqttMessage(("{\"down\":0}").getBytes());
                        MainActivity.client.publish("persiana", message);
                        Switchbajar_persiana.setChecked(false);
                    }
                    catch(MqttException e){
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        MqttMessage message = new MqttMessage(("{\"down\":0}").getBytes());
                        MainActivity.client.publish("persiana", message);
                        message = new MqttMessage(("{\"up\":0}").getBytes());
                        MainActivity.client.publish("persiana", message);

                    }
                    catch(MqttException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        Switchbajar_persiana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Switchbajar_persiana.isChecked()){
                    try {
                        MqttMessage message = new MqttMessage(("{\"up\":0}").getBytes());
                        MainActivity.client.publish("persiana", message);
                        message = new MqttMessage(("{\"down\":1}").getBytes());
                        MainActivity.client.publish("persiana", message);
                        Switchsubir_persiana.setChecked(false);
                    }
                    catch(MqttException e){
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        MqttMessage message = new MqttMessage(("{\"down\":0}").getBytes());
                        MainActivity.client.publish("persiana", message);
                        message = new MqttMessage(("{\"up\":0}").getBytes());
                        MainActivity.client.publish("persiana", message);

                    }
                    catch(MqttException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        //Finish control code Card 2


        //Card 3, control code
        Switchactiv_cons = (Switch) view.findViewById(R.id.Sw_consumo);

        Switchactiv_cons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Switchactiv_cons.isChecked()){
                    try {
                        MqttMessage message = new MqttMessage(("{\"on\":1}").getBytes());
                        MainActivity.client.publish("consumo", message);
                    }
                    catch(MqttException e){
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        MqttMessage message = new MqttMessage(("{\"on\":0}").getBytes());
                        MainActivity.client.publish("consumo", message);
                    }
                    catch(MqttException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        //Finish control code Card 3

        // Inflate the layout for this fragment
        return view;



    }

}
