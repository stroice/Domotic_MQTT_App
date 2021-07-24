package com.svv.localsports;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.svv.localsports.controlador.PagerController;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

//import org.eclipse.paho.client.mqttv3.MqttConnectOptions ;

import pl.pawelkleczkowski.customgauge.CustomGauge;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {
    public static MqttAndroidClient client;

    TabLayout tabLayout;
    AppBarLayout appBar;
    Toolbar toolbar;
    ViewPager viewPager;
    TabItem tabTemp, tabOpenClose, tabAirqual, tabMap;
    PagerController pagerAdapter;

    //Página 1:

    //Carta 2
    CustomGauge gauge1;
    TextView Text_temp;

    CustomGauge gauge2;
    TextView Text_temp_ext;
    //Fin Página 1


    //Página 2:

    //Carta 2
    TextView Tx_estado_persiana;

    //Carta 3
    TextView Tx_consumo;
    //Fin Página 2

    //Página 3
    //Carta 1
    TextView Tx_acceso;
    TextView Tx_est_ventana;
    TextView Tx_est_auto;
    TextView Tx_est_cale;
    TextView Tx_est_aire;

    //Carta 2
    TextView Tx_c02;
    TextView Tx_warning;

    //Guarda en qué vista estamos actualmente.
    int posicionTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        setSupportActionBar(toolbar);

        appBar = findViewById(R.id.appBar);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        tabTemp = findViewById(R.id.tabtemp);
        tabMap = findViewById(R.id.tabmap);
        tabOpenClose = findViewById(R.id.tabopen_close);
        tabAirqual = findViewById(R.id.tabairqual);



        //Código de control de los menús
        pagerAdapter = new PagerController(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        barraIconos(0);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                barraIconos(position);
            }

        });


        //Código control de MQTT

        String clientId = MqttClient.generateClientId();

        client =
                //Aquí es donde hay que modificar la IP objetivo ->
                new MqttAndroidClient(this.getApplicationContext(), "tcp://192.168.0.16:1883", //Insert your own IP for correct work
                        clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(MainActivity.this, "Conectado a MQTT", Toast.LENGTH_LONG).show();
                    suscriptionTopics();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_LONG).show();
                }
            });
        } catch (
                MqttException e) {
            e.printStackTrace();
        }


        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            //analisis de mensajes recividos


            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

                String mensaje = new String(message.getPayload());
                String[] subtopic = mensaje.split("\"");
                String[] valor_aux = mensaje.split(":");
                String valor = removeLastCharacter(valor_aux[1]);
                int IntValue = (int) Double.parseDouble(new String(valor));
                Double valor_good = Double.parseDouble(valor)/1024;
                String valor_doub_text= String.format("%.1f", valor_good);
                int IntValue_double = (int) Double.parseDouble(new String(valor_doub_text));

                //Carta 1
                if (topic.matches("temperatura")){

                    if(subtopic[1].equals("temperatura")){

                        Text_temp = (TextView) findViewById(R.id.labelTemnperatura_interior);
                        gauge1 = (CustomGauge) findViewById(R.id.gauge_interior);
                        Text_temp.setText( valor_doub_text + "ºC");
                        gauge1.setValue(IntValue_double);
                    }

                    if(subtopic[1].equals("temp_exterior")){
                        Text_temp_ext = (TextView) findViewById(R.id.labelTemnperatura_exterior);
                        gauge2 = (CustomGauge) findViewById(R.id.gauge_exterior);
                        Text_temp_ext.setText(valor_doub_text + "ºC");
                        gauge2.setValue(IntValue_double);
                    }

                }
                //Finalizar Carta 1

                //Carta2
                if (topic.matches("persiana")){
                    Tx_estado_persiana = (TextView) findViewById(R.id.tx_persiana);
                    if(subtopic[1].equals("top")){

                        if(valor.equals("1")){

                            Tx_estado_persiana.setText("Estado: ABIERTA ");
                        }
                        else{
                            Tx_estado_persiana.setText("Estado: ENTREABIERTA ");
                        }
                    }

                    if(subtopic[1].equals("bottom")){

                        if(valor.equals("1")){
                            Tx_estado_persiana.setText("Estado: CERRADA");
                        }
                        else{
                            Tx_estado_persiana.setText("Estado: ENTREABIERTA ");
                        }
                    }
                }
                //Finalizar Carta2

                //Carta 3
                if (topic.matches("consumo")){
                    Tx_consumo = (TextView)  findViewById(R.id.comsumo_texto);
                    if(subtopic[1].equals("energia")){
                        Tx_consumo.setText("Consumo: " + valor_doub_text + "kWh");
                    }
                }

                //Finalización Carta 3
                //Finalización página 2


                //Tercera página

                //carta 1

                if (topic.matches("acceso")) {
                    Tx_acceso = (TextView) findViewById(R.id.label_acceso);
                    if (subtopic[1].equals("timbre")) {
                        if (valor.equals("1")) {
                            Tx_acceso.setText("¡Llaman al timbre!");
                            Tx_acceso.setTextColor(getResources().getColor(R.color.md_red_900));
                            Toast.makeText(MainActivity.this, "¡Llaman al timbre!", Toast.LENGTH_LONG).show();
                        }

                        if (valor.equals("0")) {
                            Tx_acceso.setText("Llamadas al timbre atendidas");
                            Tx_acceso.setTextColor(getResources().getColor(R.color.md_blue_grey_600));
                        }


                    }
                }

                if (topic.matches("notificacion")){


                    Tx_est_ventana = (TextView)  findViewById(R.id.label_ventana);
                    if(subtopic[1].equals("ventana")){
                        if(valor.equals("1")){
                            Tx_est_ventana.setText("Estado de las ventanas: ABIERTAS");
                        }
                        else{
                            Tx_est_ventana.setText("Estado de las ventanas: CERRADAS");
                        }
                    }

                    Tx_est_auto = (TextView)  findViewById(R.id.label_auto_warn);
                    if(subtopic[1].equals("auto")){
                        if(valor.equals("1")){
                            Tx_est_auto.setText("Estado climatizador auto: ON");
                        }
                        else{
                            Tx_est_auto.setText("Estado climatizador auto: OFF");
                        }
                    }

                    Tx_est_cale = (TextView)  findViewById(R.id.label_cale_warn);
                    if(subtopic[1].equals("cale")){
                        if(valor.equals("1")){
                            Tx_est_cale.setText("Estado calefacción: ON");
                        }
                        else{
                            Tx_est_cale.setText("Estado calefacción: OFF");
                        }
                    }

                    Tx_est_aire = (TextView)  findViewById(R.id.label_aire_warn);
                    if(subtopic[1].equals("aire")){
                        if(valor.equals("1")){
                            Tx_est_aire.setText("Estado aire acondicionado: ON");
                        }
                        else{
                            Tx_est_aire.setText("Estado aire acondicionado: OFF");
                        }
                    }

                    //Carta 2
                    Tx_c02 = (TextView)  findViewById(R.id.label_co2);
                    if(subtopic[1].equals("co2")){
                        if(valor.equals("1")){
                            Tx_c02.setText("Co2 level: ¡EN NIVEL PELIGROSO!");
                            Tx_c02.setTextColor(getResources().getColor(R.color.md_red_900));
                        }
                        else{
                            Tx_c02.setText("Co2 level: OK");
                            Tx_c02.setTextColor(getResources().getColor(R.color.md_blue_grey_600));
                            Toast.makeText(MainActivity.this, "CO2 en nivel normal", Toast.LENGTH_LONG).show();
                        }
                    }

                    Tx_warning = (TextView)  findViewById(R.id.label_warn);
                    if(subtopic[1].equals("warning")){
                        if(valor.equals("1")){
                            Tx_warning.setText("Warning: VENTANA Y CLIMATIZADOR ON");
                            Tx_warning.setTextColor(getResources().getColor(R.color.md_red_900));
                            Toast.makeText(MainActivity.this, "¡ALERTA, REVISE VENTANAS, O APAGUE CLIMATIZADOR!", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Tx_warning.setText("Warning: OK");
                            Tx_warning.setTextColor(getResources().getColor(R.color.md_blue_grey_600));
                            Toast.makeText(MainActivity.this, "alerta finalizada, normalidad reestablecida", Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }

    private void suscriptionTopics(){
        try{
            client.subscribe("persiana", 1);
            client.subscribe("temperatura", 1);
            client.subscribe("notificacion", 1);
            client.subscribe("consumo", 1);
            client.subscribe("acceso", 1);

        }catch(MqttException e){
            e.printStackTrace();
        }
    }

    private void barraIconos(int posicionSeleccionada){
        //Se asegura de que la toolbar está presente cuando se cambia de tab.
        appBar.setExpanded(true, true);

        posicionTab = posicionSeleccionada;

        int[] arrayIconos = {R.drawable.ic_thermometer, R.drawable.ic_window, R.drawable.ic_baseline_warning_24, R.drawable.ic_pin};
        Drawable iconoMod = null;

        for (int i = 0; i<arrayIconos.length; i++) {
            if (i == posicionSeleccionada) {
                iconoMod = new VectorDrawableUtils().getDrawable(getApplicationContext(), arrayIconos[i], R.color.colorSeleccionado);
            } else {
                iconoMod = new VectorDrawableUtils().getDrawable(getApplicationContext(), arrayIconos[i], R.color.colorDeseleccionado);
            }
            tabLayout.getTabAt(i).setIcon(iconoMod);
        }
    }
    private class VectorDrawableUtils {

        Drawable getDrawable(Context context, int drawableResId) {
            return VectorDrawableCompat.create(context.getResources(), drawableResId, context.getTheme());
        }

        Drawable getDrawable(Context context, int drawableResId, int colorFilter) {
            Drawable drawable = getDrawable(context, drawableResId);
            drawable.setColorFilter(ContextCompat.getColor(context, colorFilter), PorterDuff.Mode.SRC_IN);
            return drawable;
        }
    }

    public static String removeLastCharacter(String str) {
        String result = null;
        if ((str != null) && (str.length() > 0)) {
            result = str.substring(0, str.length() - 1);
        }
        return result;
    }

}