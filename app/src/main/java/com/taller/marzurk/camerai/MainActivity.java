package com.taller.marzurk.camerai;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorEventListener sListener;
    SensorManager sManager;
    Sensor _sensor;

    TextView tvSensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Change ActionBar Background Color;
        try{
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0288D1")));
        }catch(Exception ex){
            Log.e("ERROR", ex.getMessage());
        }

        tvSensor = (TextView)(findViewById(R.id.tvSensor));

        sManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        try{_sensor = sManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);}
        catch(Exception e){
            Toast.makeText(this, R.string.no_compatible, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_acerca) {
            Toast.makeText(getApplicationContext(), "Dev By Rigo Ramírez", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent e) {
        if (e.sensor.getType() == Sensor.TYPE_PROXIMITY){
            tvSensor.setText("");
            tvSensor.setText("Lectura: ");
            for (int i = 0; i < e.values.length; i++){
                String valor = String.valueOf(e.values[i]);
                tvSensor.setText(tvSensor.getText() + valor + " - ");
            }

            //Posibles values 100 lejos y 3 cerca
            if (e.values[0] < 100){
                Intent oCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                startActivity(oCamara);
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sManager.registerListener(this, _sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sManager.unregisterListener(this);
    }
}