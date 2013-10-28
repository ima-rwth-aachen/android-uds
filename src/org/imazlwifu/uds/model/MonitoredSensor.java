package org.imazlwifu.uds.model;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MonitoredSensor implements Monitorable, SensorEventListener {
	SensorManager sensorManager;
	
	private Sensor sensor;
	private Map<String, Float> values;

	public MonitoredSensor( Context context, Sensor s ) {
		sensorManager = (SensorManager) context.getSystemService( Context.SENSOR_SERVICE );
		
		this.sensor = s;
		values = new HashMap<String, Float>();
	}
	
	@Override
	public String getName() {
		return sensor.getName();
	}

	/**
	 * <p>
	 * There might be multiple sensors with the same name.
	 * E.g. HTTP Post Params behave like a set.
	 * Therefore a unique key is formed by the expression:
	 * </p >
	 * <p><code style="text-indent:4em;">SensorName + '@' + Integer.toHexString( this.hashCode() )</code></p>
	 */
	@Override
	public Map<String, Float> values() {
		return values;
	}

	@Override
	public void updateData() {
		sensorManager.registerListener( this, sensor, SensorManager.SENSOR_DELAY_NORMAL );
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		for( float f : event.values )
			values.put( sensor.getName() +'@'+ Integer.toHexString( this.hashCode() ), f );

		sensorManager.unregisterListener( this );
	}

	@Override
	public void setContext(Context context) {
		// TODO Auto-generated method stub
		
	}

}
