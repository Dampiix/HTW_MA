package com.example.spacegame_ma.Controls;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.spacegame_ma.Logic.Constants;

public class SensorDataInput implements SensorEventListener {

    private SensorManager manager;
    private Sensor accelerometer;
    private Sensor magnetometer;


    private float[] accelerometerOutput;
    private float[] magnetometerOutput;

    private float[] orientation = new float[3];
    public float[] getOrientation() {
        return orientation;
    }

    private float[] startOrientation = null;
    public float[] getStartOrientation() {
        return startOrientation;
    }
    public void newGame() {
        startOrientation = null;
    }

    public SensorDataInput() {
        manager = (SensorManager)Constants.CURRENT_CONTEXT.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void register() {
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void unregister() {
        manager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            accelerometerOutput = event.values;
        else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            magnetometerOutput = event.values;
        if(accelerometerOutput != null && magnetometerOutput != null) {
            float[] rotation = new float[9];    //3x3 Matrix for the rotation
            float[] inclination = new float[9]; //3x3 Matrix for the inclination
            boolean successful = SensorManager.getRotationMatrix(rotation, inclination, accelerometerOutput, magnetometerOutput);

            if(successful) {
                SensorManager.getOrientation(rotation, orientation);

                //assign first data as start orientation
                if(startOrientation == null) {
                    startOrientation = new float[orientation.length];
                    System.arraycopy(orientation, 0, startOrientation, 0, orientation.length);

                    //System.out.println("StartY " + startOrientation[1]);
                    System.out.println("StartX " + startOrientation[2]);

                }
            }
        }
    }
}