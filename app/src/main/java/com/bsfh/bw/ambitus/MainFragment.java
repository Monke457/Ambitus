package com.bsfh.bw.ambitus;

import static android.content.Context.SENSOR_SERVICE;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class MainFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    private Sensor ambientTemperatureSensor;
    private Sensor humiditySensor;
    private Sensor pressureSensor;
    private Sensor magnetSensor;

    private TextView tempValueView;
    private TextView ambientTempValueView;
    private TextView humidityValueView;
    private TextView pressureValueView;
    private TextView magnetValueViewX;
    private TextView magnetValueViewY;
    private TextView magnetValueViewZ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tempValueView = view.findViewById(R.id.battery_value);
        ambientTempValueView = view.findViewById(R.id.temp_value);
        humidityValueView = view.findViewById(R.id.humidity_value);
        pressureValueView = view.findViewById(R.id.pressure_value);
        magnetValueViewX = view.findViewById(R.id.magnet_value_x);
        magnetValueViewY = view.findViewById(R.id.magnet_value_y);
        magnetValueViewZ = view.findViewById(R.id.magnet_value_z);

        if (this.getActivity() != null) {
            sensorManager = (SensorManager) this.getActivity().getSystemService(SENSOR_SERVICE);
            temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
            ambientTemperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
            pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
            magnetSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

            if (temperatureSensor == null) tempValueView.setText(R.string.sensor_unavailable);
            if (ambientTemperatureSensor == null) ambientTempValueView.setText(R.string.sensor_unavailable);
            if (humiditySensor == null) humidityValueView.setText(R.string.sensor_unavailable);
            if (pressureSensor == null) pressureValueView.setText(R.string.sensor_unavailable);
            if (magnetSensor == null) {
                magnetValueViewX.setText(R.string.sensor_unavailable);
                magnetValueViewY.setText(R.string.sensor_unavailable);
                magnetValueViewZ.setText(R.string.sensor_unavailable);
            }
        }

        FloatingActionButton settingsButton = view.findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_settingsFragment));
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, ambientTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_TEMPERATURE:
                updateTempValue(event.values);
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                updateAmbientTempValue(event.values);
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                updateHumidityValue(event.values);
                break;
            case Sensor.TYPE_PRESSURE:
                updatePressureValue(event.values);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                updateMagnetValue(event.values);
                break;
            default: break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void updateTempValue(float[] values) {
        String text = values[0] + "??C";
        tempValueView.setText(text);
    }

    private void updateAmbientTempValue(float[] values) {
        String text = values[0] + "??C";
        ambientTempValueView.setText(text);
    }

    private void updateHumidityValue(float[] values) {
        String text = values[0] + "%";
        humidityValueView.setText(text);
    }

    private void updatePressureValue(float[] values) {
        String text = values[0] + " hPa";
        pressureValueView.setText(text);
    }

    private void updateMagnetValue(float[] values) {
        String textX = String.format(Locale.ENGLISH, "%.2f", values[0]) + " ??T";
        String textY =  String.format(Locale.ENGLISH, "%.2f", values[1]) + " ??T";
        String textZ =  String.format(Locale.ENGLISH, "%.2f", values[2]) + " ??T";
        magnetValueViewX.setText(textX);
        magnetValueViewY.setText(textY);
        magnetValueViewZ.setText(textZ);
    }
}
