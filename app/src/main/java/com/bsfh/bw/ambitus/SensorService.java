package com.bsfh.bw.ambitus;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.Instant;

public class SensorService extends Service implements SensorEventListener {

    private SensorManager sensorManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        Sensor ambientTemperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        Sensor humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        Sensor pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        Sensor magnetSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, ambientTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        sensorManager.unregisterListener(this);
        return super.onUnbind(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_TEMPERATURE:
                    if (prefs.getBoolean("battery-notification", true)) {
                        long last = prefs.getLong("battery-last-notification", 0L);
                        //if 1 minute has passed since the last notification - send a notification
                        if (last + 60000 <= Instant.now().toEpochMilli()) {
                            checkBattery(event.values[0], prefs);
                        }
                    }
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    if (prefs.getBoolean("temp-notification", true)) {
                        long last = prefs.getLong("temp-last-notification", 0L);
                        if (last + 60000 <= Instant.now().toEpochMilli()) {
                            checkTemp(event.values[0], prefs);
                        }
                    }
                    break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    if (prefs.getBoolean("humidity-notification", true)) {
                        long last = prefs.getLong("humidity-last-notification", 0L);
                        if (last + 60000 <= Instant.now().toEpochMilli()) {
                            checkHumidity(event.values[0], prefs);
                        }
                    }
                    break;
                case Sensor.TYPE_PRESSURE:
                    if (prefs.getBoolean("pressure-notification", true)) {
                        long last = prefs.getLong("pressure-last-notification", 0L);
                        if (last + 60000 <= Instant.now().toEpochMilli()) {
                            checkPressure(event.values[0], prefs);
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void checkBattery(float value, SharedPreferences prefs) {
        int min = prefs.getInt("battery-min", 5);
        int max = prefs.getInt("battery-max", 55);
        if (value < min) {
            sendNotification("Battery temperature too low: " + value + "°C!");
            updatePreference(prefs, "battery-last-notification");
        } else if (value > max) {
            sendNotification("Battery temperature too high: " + value + "°C!");
            updatePreference(prefs, "battery-last-notification");
        }
    }

    private void checkTemp(float value, SharedPreferences prefs) {
        int min = prefs.getInt("temp-min", -5);
        int max = prefs.getInt("temp-max", 30);
        if (value < min) {
            sendNotification("Temperature too low: " + value + "°C!");
            updatePreference(prefs, "temp-last-notification");
        } else if (value > max) {
            sendNotification("Temperature too high: " + value + "°C!");
            updatePreference(prefs, "temp-last-notification");
        }
    }

    private void checkHumidity(float value, SharedPreferences prefs) {
        int min = prefs.getInt("humidity-min", 10);
        int max = prefs.getInt("humidity-max", 80);
        if (value < min) {
            sendNotification("Humidity too low: " + value + "%!");
            updatePreference(prefs, "humidity-last-notification");
        } else if (value > max) {
            sendNotification("Humidity too high: " + value + "%!");
            updatePreference(prefs, "humidity-last-notification");
        }
    }

    private void checkPressure(float value, SharedPreferences prefs) {
        int min = prefs.getInt("pressure-min", 550);
        int max = prefs.getInt("pressure-max", 1240);
        if (value < min) {
            sendNotification("Air pressure too low: " + value + " hPa!");
            updatePreference(prefs, "pressure-last-notification");
        } else if (value > max) {
            sendNotification("Air pressure too high: " + value + "hPa!");
            updatePreference(prefs, "pressure-last-notification");
        }
    }

    private void sendNotification(String message) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification","My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager =getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(SensorService.this,"My Notification");
            builder.setContentTitle("Unstable environment!");
            builder.setContentText(message);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setAutoCancel(true);
            NotificationManagerCompat managerCompat=NotificationManagerCompat.from(SensorService.this);
            managerCompat.notify(1, builder.build());
        }
    }

    private void updatePreference(SharedPreferences prefs, String key) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            prefs.edit().putLong(key, Instant.now().toEpochMilli()).apply();
        }
    }
}
