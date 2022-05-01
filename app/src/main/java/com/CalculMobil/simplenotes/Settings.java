package com.CalculMobil.simplenotes;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

public class Settings extends AppCompatActivity {

    SensorManager sensorManager;
    Sensor lightSensor;
    boolean ambientThemeChangeEnable;
    boolean viewThemeSwitchState;
    boolean ambientSwitchState;
    boolean viewThemeSwitchEnabled;
    SharedPreferences sharedPreferences;

    boolean darkMode = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // recovering the instance state
        if (savedInstanceState != null) {
            ambientThemeChangeEnable = savedInstanceState.getBoolean("ambientThemeChangeEnable");
            viewThemeSwitchState = savedInstanceState.getBoolean("viewThemeSwitchState");
            ambientSwitchState = savedInstanceState.getBoolean("ambientSwitchState");
            viewThemeSwitchEnabled = savedInstanceState.getBoolean("viewThemeSwitchEnabled");
        }

        SwitchCompat viewModeSwitch;
        SwitchCompat ambientThemeSwitch;

        darkMode = true;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C53F15")));
        getSupportActionBar().setTitle("Settings");

        setContentView(R.layout.activity_settings);

        viewModeSwitch = findViewById(R.id.viewModeSwitch);
        ambientThemeSwitch = findViewById(R.id.ambientThemeSwitch);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if(lightSensor != null){
            sensorManager.registerListener(
                lightSensorListener,
                lightSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        }

        sharedPreferences = getSharedPreferences("night", 0);
        ambientThemeChangeEnable = sharedPreferences.getBoolean("ambientThemeChangeEnable", false);


        Boolean booleanValue = sharedPreferences.getBoolean("night_mode", true);
        if (booleanValue)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            viewModeSwitch.setChecked(true);
        }

        ambientSwitchState = sharedPreferences.getBoolean("ambientSwitchState", false);
        if (ambientSwitchState)
        {
            ambientThemeSwitch.setChecked(true);
        }

        viewThemeSwitchEnabled = sharedPreferences.getBoolean("viewThemeSwitchEnabled", false);
        if (viewThemeSwitchEnabled)
        {
            viewModeSwitch.setEnabled(true);
        }
        else
        {
            viewModeSwitch.setEnabled(false);
        }

        viewModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isPressed())
                {
                    if (isChecked)
                    {
                        viewModeSwitch.setOnCheckedChangeListener(null);
                        viewModeSwitch.setChecked(true);
                        viewThemeSwitchState = true;
                        viewModeSwitch.setOnCheckedChangeListener(this);

                        setDarkMode();
                    }
                    else
                    {
                        viewModeSwitch.setOnCheckedChangeListener(null);
                        viewModeSwitch.setChecked(false);
                        viewThemeSwitchState = false;
                        viewModeSwitch.setOnCheckedChangeListener(this);

                        setLightMode();
                    }
                }
            }
        });

        ambientThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isPressed())
                {
                    if (isChecked)
                    {
                        viewModeSwitch.setEnabled(false);
                        viewThemeSwitchEnabled = false;

                        ambientThemeSwitch.setOnCheckedChangeListener(null);
                        ambientThemeSwitch.setChecked(true);
                        ambientSwitchState = true;
                        ambientThemeSwitch.setOnCheckedChangeListener(this);

                        ambientThemeChangeEnable = true;
                    }
                    else
                    {
                        viewModeSwitch.setEnabled(true);
                        viewThemeSwitchEnabled = true;

                        ambientThemeSwitch.setOnCheckedChangeListener(null);
                        ambientThemeSwitch.setChecked(false);
                        ambientSwitchState = false;
                        ambientThemeSwitch.setOnCheckedChangeListener(this);

                        ambientThemeChangeEnable = false;

                        if (viewThemeSwitchState)
                        {
                            setDarkMode();
                        }
                        else
                        {
                            setLightMode();
                        }
                    }
                }
            }
        });
    }

    private final SensorEventListener lightSensorListener
            = new SensorEventListener(){

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_LIGHT){
                //lightSensorText.setText("Value = " + event.values[0]);
                if (ambientThemeChangeEnable)
                {
                    if (event.values[0] < 35)
                    {
                        setDarkMode();
                    }
                    else
                    {
                        setLightMode();
                    }
                }
            }
        }
    };

    public void setDarkMode()
    {
        darkMode = sharedPreferences.getBoolean("night_mode", false);
        if (!darkMode)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            SharedPreferences settings = getSharedPreferences("night",0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("night_mode", true);
            editor.apply();
        }
    }

    public void setLightMode()
    {
        darkMode = sharedPreferences.getBoolean("night_mode", true);
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            SharedPreferences settings = getSharedPreferences("night",0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("night_mode", false);
            editor.apply();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("ambientThemeChangeEnable", ambientThemeChangeEnable);
        outState.putBoolean("ambientSwitchState", ambientSwitchState);
        outState.putBoolean("viewThemeSwitchState", viewThemeSwitchState);
        outState.putBoolean("viewThemeSwitchEnabled", viewThemeSwitchEnabled);
        //outState.putBoolean("night_mode", true);
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    protected void onPause(){
        super.onPause();

        SharedPreferences settings = getSharedPreferences("night",0);
        SharedPreferences.Editor editor = settings.edit();
        // Necessary to clear first if we save preferences onPause.
        //editor.clear();
        editor.putBoolean("ambientThemeChangeEnable", ambientThemeChangeEnable);
        editor.putBoolean("ambientSwitchState", ambientSwitchState);
        editor.putBoolean("viewThemeSwitchState", viewThemeSwitchState);
        editor.putBoolean("viewThemeSwitchEnabled", viewThemeSwitchEnabled);
        //editor.putBoolean("night_mode", true);
        editor.commit();
    }
}
