package com.osvaldovillalobosperez.missensoresfutbolitopocket;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView equipoA, equipoB, marcadorA, marcadorB;
    ImageView pelota;

    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;

    int ancho = 0, alto = 0, puntajeA = 0, puntajeB = 0;

    DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        equipoA = findViewById(R.id.lblEquipoA);
        equipoB = findViewById(R.id.lblEquipoB);
        marcadorA = findViewById(R.id.lblMarcadorA);
        marcadorB = findViewById(R.id.lblMarcadorB);
        pelota = findViewById(R.id.imgPelota);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ancho = metrics.widthPixels;
        alto = metrics.heightPixels;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (sensor == null) {
            Toast.makeText(
                    this,
                    "El dispositivo no cuenta con sensor de acelerometro.",
                    Toast.LENGTH_LONG
            ).show();

            finish();
        }

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];

                if (x < (-1)) {
                    if (pelota.getX() < (ancho - pelota.getWidth())) {
                        pelota.setX(pelota.getX() + 30);
                    }
                } else if (x > 1) {
                    if (pelota.getX() > 1) {
                        pelota.setX(pelota.getX() - 30);
                    }
                }

                if (y < (-1)) {
                    if (pelota.getY() > 0) {
                        pelota.setY(pelota.getY() - 50);
                    } else {
                        if ((pelota.getX() > 400) && (pelota.getX() < 580)) {
                            Gol();
                            puntajeB++;
                            marcadorB.setText(String.valueOf(puntajeB));
                        }
                    }
                } else if (y > 1) {
                    if (pelota.getY() < ((ancho - pelota.getHeight()) + 625)) {
                        pelota.setY(pelota.getY() + 50);
                    } else {
                        if ((pelota.getX() > 400) && (pelota.getX() < 580)) {
                            Gol();
                            puntajeA++;
                            marcadorA.setText(String.valueOf(puntajeA));
                        }
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                sensorManager.SENSOR_DELAY_FASTEST
        );
    }

    private void Gol() {
        pelota.setX(540);
        pelota.setY(888);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(sensorEventListener);
        super.onPause();
    }

    @Override
    protected void onResume() {
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                sensorManager.SENSOR_DELAY_FASTEST
        );

        super.onResume();

        Gol();
    }
}
