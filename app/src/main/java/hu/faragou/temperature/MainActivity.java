package hu.faragou.temperature;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private TextView limitTextView;
    private TextView currentTemperatureTextView;
    private TextView warningTextView;
    private Button plusButton;
    private Button minusButton;
    private int limitValue = 30; // Alapértelmezett limit érték
    private SensorManager sensorManager;
    private Sensor temperatureSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        limitTextView = findViewById(R.id.textView2);
        currentTemperatureTextView = findViewById(R.id.textView3);
        warningTextView = findViewById(R.id.textView4);
        plusButton = findViewById(R.id.button3);
        minusButton = findViewById(R.id.button2);

        // Szenzor inicializálása
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        // Gombok kattintásának kezelése
        plusButton.setOnClickListener(v -> increaseLimit());
        minusButton.setOnClickListener(v -> decreaseLimit());

        // Kezdeti értékek beállítása
        updateLimitDisplay();
    }

    // Hőmérséklet növelése
    private void increaseLimit() {
        limitValue++;
        updateLimitDisplay();
    }

    // Limit csökkentése
    private void decreaseLimit() {
        limitValue--;
        updateLimitDisplay();
    }

    // Limit értékének megjelenítése
    private void updateLimitDisplay() {
        limitTextView.setText(String.valueOf(limitValue));
        if (Integer.parseInt((limitTextView.getText().toString())) > Double.parseDouble(currentTemperatureTextView.getText().toString())){
            warningTextView.setText("");
        }
        else {
            warningTextView.setText("DANGER");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        // Szenzor regisztrálása az élettartam során
        sensorManager.registerListener((SensorEventListener) this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Szenzor kiválasztása a háttérbe lépés során
        sensorManager.unregisterListener((SensorListener) this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
       if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float currentTemp = event.values[0];
            currentTemperatureTextView.setText(String.valueOf(currentTemp));
            updateLimitDisplay();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //ez muszáj de nemtudom miért hihi
    }
}

