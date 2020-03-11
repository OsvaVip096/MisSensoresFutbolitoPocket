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

    /* Componentes utilizados en la interfaz: TextView y ImageView.*/
    TextView equipoA, equipoB, marcadorA, marcadorB; // Es el texto plano de marcador de equipos.
    ImageView pelota; // Imagen del balón que se movera con los sensores.

    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;

    /* La altura y ancho son variables pertenecientes a la pantalla en que se muestra.
     * El puntaje A y B son de los equipos que anotan un gol, aumenta el del equipo que anoto. */
    int ancho = 0, alto = 0, puntajeA = 0, puntajeB = 0;

    /* DisplayMetrics permite describir información general sobre una pantalla, como su tamaño,
     *  densidad y escala de fuentes. */
    DisplayMetrics metrics;

    /**
     * Método onCreate que carga todos los componentes de la aplicación y se encargara de detectar
     * los cambios que percibe en movimiento del dispositivo con los sensores de aceleración y
     * modificará la posición de la imagén del balón.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Instanciamos los elementos pertenecientes al diseño como los TextView e ImageView
         *  a través de su ID. */
        equipoA = findViewById(R.id.lblEquipoA);
        equipoB = findViewById(R.id.lblEquipoB);
        marcadorA = findViewById(R.id.lblMarcadorA);
        marcadorB = findViewById(R.id.lblMarcadorB);
        pelota = findViewById(R.id.imgPelota);

        /* Obtenemos las medidas de la pantalla en que se está mostrando. */
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ancho = metrics.widthPixels; // De las medidas extraídas, asigmanos el ancho total.
        alto = metrics.heightPixels; // De las mediad extraídas, asignamos el alto total.

        /* Invocamos el sensor de aceleración por defecto. */
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        /* Si no existe el sensor de aceleración en el dispositivo móvil la aplicación se cerrará
         *  automáticamente. */
        if (sensor == null) {
            Toast.makeText(
                    this,
                    "El dispositivo no cuenta con sensor de acelerometro.",
                    Toast.LENGTH_LONG
            ).show();

            finish();
        }

        /* TODO: Escuchador del sensor de acelerometro que interpreta los cambios de movimiento. */
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0]; // Valores en el eje X de movimiento.
                float y = event.values[1]; // Valores en el eje Y de movimiento.
                float z = event.values[2]; // Valores en el eje Z de movimiento.

                if (x < (-1)) {
                    if (pelota.getX() < (ancho - pelota.getWidth())) {
                        pelota.setX(pelota.getX() + 5);
                    }
                } else if (x > 1) {
                    if (pelota.getX() > 1) {
                        pelota.setX(pelota.getX() - 5);
                    }
                }

                if (y < (-1)) {
                    if (pelota.getY() > 0) {
                        pelota.setY(pelota.getY() - 5);
                    } else {
                        if ((pelota.getX() > 400) && (pelota.getX() < 580)) {
                            Gol();
                            puntajeB++;
                            marcadorB.setText(String.valueOf(puntajeB));
                        }
                    }
                } else if (y > 1) {
                    if (pelota.getY() < ((ancho - pelota.getHeight()) + 625)) {
                        pelota.setY(pelota.getY() + 5);
                    } else {
                        if ((pelota.getX() > 400) && (pelota.getX() < 580)) {
                            Gol();
                            puntajeA++;
                            marcadorA.setText(String.valueOf(puntajeA));
                        }
                    }
                }

                if (z < (-1)) {
                    pelota.setMaxWidth(100);
                    pelota.setMaxHeight(100);
                } else if (z > 1) {
                    pelota.setMaxWidth(100);
                    pelota.setMaxHeight(100);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        /*TODO: registra el evento de movimiento, indica el sensor a usar y el delay del sensor. */
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                sensorManager.SENSOR_DELAY_FASTEST
        );
    }

    /**
     * Cuando se realiza un Gol, el balón vuelve a la posición de inicio (al centro de la pantalla).
     */
    private void Gol() {
        pelota.setX(540);
        pelota.setY(888);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Suspende el registro de movimiento del sensor de aceleración porque cambió el estado
     * a onPause.
     */
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(sensorEventListener);
        super.onPause();
    }

    /**
     * Reanuda nuevamente el registro del sensor de aceleración, llama al metodo Gol para
     * que inicie con la posición del balón en el centro de la pantalla.
     */
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
