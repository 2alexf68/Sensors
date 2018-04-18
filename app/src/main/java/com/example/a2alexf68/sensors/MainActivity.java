package com.example.a2alexf68.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    // to access sensors on sensor change
    //atributes of the class
    Sensor accel, magField;

    // float arrays
    float [] accelValues, magFieldValues, orientationMatrix, orientations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create the sensor manager onCreate
        SensorManager sMgr = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        //obtain your chosen sensors
        //Sensor
        accel = sMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magField = sMgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //register a listener
        //this this,the sensor, the parameter
        //remember to implement sensor event listener
        sMgr.registerListener(this,accel,SensorManager.SENSOR_DELAY_UI);
        sMgr.registerListener(this,magField,SensorManager.SENSOR_DELAY_UI);

        //allocate space memory as an array of 3, 3, 16 values
        accelValues = new float[3];
        magFieldValues = new float [3];
        //it needs 16 values in order to translate the orientation, rotation research more about this
        orientationMatrix = new float [16];
        orientations = new float [3];
    }

    //implement methods
    public void onSensorChanged(SensorEvent ev){
        //format the number according to what was specified
        DecimalFormat df = new DecimalFormat("#.##");

        //finding the text views by id
        TextView tvX = (TextView)findViewById(R.id.tvX);
        TextView tvY = (TextView)findViewById(R.id.tvY);
        TextView tvZ = (TextView)findViewById(R.id.tvZ);

        /*
        tvX.setText(df.format(ev.values[0]));
        tvY.setText(df.format(ev.values[1]));
        tvZ.setText(df.format(ev.values[2]));
        */


        //identify which sensor is
        if(ev.sensor == accel)
        {
            //matrix = grid of numbers representing a transformation / rotation, default orientation, translation
            //clone makes a copy of the values
            //accelValues = ev.values.clone();
            for(int  i=0; i<3; i++){
                accelValues[i] = ev.values[i];
            }
        }
        else if (ev.sensor == magField)
        {
            for(int  i=0; i<3; i++){
                magFieldValues[i] = ev.values[i];
            }

        }

        //api that
        SensorManager.getRotationMatrix(orientationMatrix, null, accelValues, magFieldValues);

        //api that gets the orientation
        SensorManager.getOrientation(orientationMatrix, orientations);

        //radians of angles, a different representation of angles
        //azimuth the direction of the device
        //pitch
        tvX.setText(df.format(orientations[0] * 180/Math.PI)); // X azimuth compass bearing
        tvY.setText(df.format(orientations[1] * 180/Math.PI)); // Y pitch
        tvZ.setText(df.format(orientations[2] * 180/Math.PI)); // Z roll
    }

    public void onAccuracyChanged(Sensor sensor, int acc){

    }
}
