package com.bsfh.bw.ambitus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton homeButton = view.findViewById(R.id.icon_button);
        homeButton.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());

        FloatingActionButton settingsButton = view.findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        displaySharedPreferences(view, prefs);
        setListeners(view, prefs);
    }

    private void displaySharedPreferences(View view, SharedPreferences prefs) {
        displayBatteryPreferences(prefs, view);
        displayBatteryNotification(prefs, view);
        displayTempPreferences(prefs, view);
        displayTempNotification(prefs, view);
        displayHumidityPreferences(prefs, view);
        displayHumidityNotification(prefs, view);
        displayPressurePreferences(prefs, view);
        displayPressureNotification(prefs, view);
    }

    private void displayBatteryPreferences(SharedPreferences prefs, View view) {
        int min = prefs.getInt("battery-min", 5);
        int max = prefs.getInt("battery-max", 55);
        TextView minView = view.findViewById(R.id.battery_min_value);
        TextView maxView = view.findViewById(R.id.battery_max_value);
        minView.setText(String.valueOf(min));
        maxView.setText(String.valueOf(max));
    }

    private void displayBatteryNotification(SharedPreferences prefs, View view) {
        boolean notification = prefs.getBoolean("battery-notification", true);
        SwitchCompat notificationView = view.findViewById(R.id.battery_notifications);
        notificationView.setChecked(notification);
    }

    private void displayTempPreferences(SharedPreferences prefs, View view) {
        int min = prefs.getInt("temp-min", -5);
        int max = prefs.getInt("temp-max", 30);
        TextView minView = view.findViewById(R.id.temp_min_value);
        TextView maxView = view.findViewById(R.id.temp_max_value);
        minView.setText(String.valueOf(min));
        maxView.setText(String.valueOf(max));
    }

    private void displayTempNotification(SharedPreferences prefs, View view) {
        boolean notification = prefs.getBoolean("temp-notification", true);
        SwitchCompat notificationView = view.findViewById(R.id.temp_notifications);
        notificationView.setChecked(notification);
    }

    private void displayHumidityPreferences(SharedPreferences prefs, View view) {
        int min = prefs.getInt("humidity-min", 10);
        int max = prefs.getInt("humidity-max", 80);
        TextView minView = view.findViewById(R.id.humidity_min_value);
        TextView maxView = view.findViewById(R.id.humidity_max_value);
        minView.setText(String.valueOf(min));
        maxView.setText(String.valueOf(max));
    }

    private void displayHumidityNotification(SharedPreferences prefs, View view) {
        boolean notification = prefs.getBoolean("humidity-notification", true);
        SwitchCompat notificationView = view.findViewById(R.id.humidity_notifications);
        notificationView.setChecked(notification);
    }

    private void displayPressurePreferences(SharedPreferences prefs, View view) {
        int min = prefs.getInt("pressure-min", 550);
        int max = prefs.getInt("pressure-max", 1241);
        TextView minView = view.findViewById(R.id.pressure_min_value);
        TextView maxView = view.findViewById(R.id.pressure_max_value);
        minView.setText(String.valueOf(min));
        maxView.setText(String.valueOf(max));
    }

    private void displayPressureNotification(SharedPreferences prefs, View view) {
        boolean notification = prefs.getBoolean("pressure-notification", true);
        SwitchCompat notificationView = view.findViewById(R.id.pressure_notifications);
        notificationView.setChecked(notification);
    }

    private void setListeners(View view, SharedPreferences prefs) {
        EditText batteryMin = view.findViewById(R.id.battery_min_value);
        EditText batteryMax = view.findViewById(R.id.battery_max_value);
        SwitchCompat batteryNotification = view.findViewById(R.id.battery_notifications);
        Button batteryReset = view.findViewById(R.id.battery_reset_button);

        EditText tempMin = view.findViewById(R.id.temp_min_value);
        EditText tempMax = view.findViewById(R.id.temp_max_value);
        SwitchCompat tempNotification = view.findViewById(R.id.temp_notifications);
        Button tempReset = view.findViewById(R.id.temp_reset_button);

        EditText humidityMin = view.findViewById(R.id.humidity_min_value);
        EditText humidityMax = view.findViewById(R.id.humidity_max_value);
        SwitchCompat humidityNotification = view.findViewById(R.id.humidity_notifications);
        Button humidityReset = view.findViewById(R.id.humidity_reset_button);

        EditText pressureMin = view.findViewById(R.id.pressure_min_value);
        EditText pressureMax = view.findViewById(R.id.pressure_max_value);
        SwitchCompat pressureNotification = view.findViewById(R.id.pressure_notifications);
        Button pressureReset = view.findViewById(R.id.pressure_reset_button);

        SwitchCompat allNotifications = view.findViewById(R.id.all_notifications);
        Button resetAll = view.findViewById(R.id.all_reset_button);

        batteryMin.addTextChangedListener(new MyTextWatcher("battery-min", prefs));
        batteryMax.addTextChangedListener(new MyTextWatcher("battery-max", prefs));
        batteryNotification.setOnCheckedChangeListener((buttonView, isChecked) -> setPref(prefs, "battery-notification", isChecked));
        batteryReset.setOnClickListener(v -> resetBattery(prefs, view));

        tempMin.addTextChangedListener(new MyTextWatcher("temp-min", prefs));
        tempMax.addTextChangedListener(new MyTextWatcher("temp-max", prefs));
        tempNotification.setOnCheckedChangeListener((buttonView, isChecked) -> setPref(prefs, "temp-notification", isChecked));
        tempReset.setOnClickListener(v ->resetTemp(prefs, view));

        humidityMin.addTextChangedListener(new MyTextWatcher("humidity-min", prefs));
        humidityMax.addTextChangedListener(new MyTextWatcher("humidity-max", prefs));
        humidityNotification.setOnCheckedChangeListener((buttonView, isChecked) -> setPref(prefs, "humidity-notification", isChecked));
        humidityReset.setOnClickListener(v -> resetHumidity(prefs, view));

        pressureMin.addTextChangedListener(new MyTextWatcher("pressure-min", prefs));
        pressureMax.addTextChangedListener(new MyTextWatcher("pressure-max", prefs));
        pressureNotification.setOnCheckedChangeListener((buttonView, isChecked) -> setPref(prefs, "pressure-notification", isChecked));
        pressureReset.setOnClickListener(v -> resetPressure(prefs, view));

        allNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setPref(prefs, "battery-notification", isChecked);
            setPref(prefs, "temp-notification", isChecked);
            setPref(prefs, "humidity-notification", isChecked);
            setPref(prefs, "pressure-notification", isChecked);
            displayBatteryNotification(prefs, view);
            displayTempNotification(prefs, view);
            displayHumidityNotification(prefs, view);
            displayPressureNotification(prefs, view);
        });

        resetAll.setOnClickListener(v -> {
            resetBattery(prefs, view);
            resetTemp(prefs, view);
            resetHumidity(prefs, view);
            resetPressure(prefs, view);
        });
    }

    private void resetBattery(SharedPreferences prefs, View view) {
        setPref(prefs, "battery-min", 5);
        setPref(prefs, "battery-max", 55);
        setPref(prefs, "battery-notification", true);

        displayBatteryPreferences(prefs, view);
        displayBatteryNotification(prefs, view);
    }

    private void resetTemp(SharedPreferences prefs, View view) {
        setPref(prefs, "temp-min", -5);
        setPref(prefs, "temp-max", 30);
        setPref(prefs, "temp-notification", true);

        displayTempPreferences(prefs, view);
        displayTempNotification(prefs, view);
    }

    private void resetHumidity(SharedPreferences prefs, View view) {
        setPref(prefs, "humidity-min", 10);
        setPref(prefs, "humidity-max", 80);
        setPref(prefs, "humidity-notification", true);

        displayHumidityPreferences(prefs, view);
        displayHumidityNotification(prefs, view);
    }

    private void resetPressure(SharedPreferences prefs, View view) {
        setPref(prefs, "pressure-min", 550);
        setPref(prefs, "pressure-max", 1240);
        setPref(prefs, "pressure-notification", true);

        displayPressurePreferences(prefs, view);
        displayPressureNotification(prefs, view);
    }

    private void setPref(SharedPreferences prefs, String key, int value) {
        prefs.edit().putInt(key, value).apply();
    }

    private void setPref(SharedPreferences prefs, String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    public static class MyTextWatcher implements TextWatcher {
        private final String key;
        private final SharedPreferences prefs;

        public MyTextWatcher(String key, SharedPreferences prefs) {
            this.key = key;
            this.prefs = prefs;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (validate(s)) {
                prefs.edit().putInt(key, Integer.parseInt(String.valueOf(s))).apply();
            }
        }

        private static boolean validate(Editable s) {
            try {
                Integer.parseInt(String.valueOf(s));
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
