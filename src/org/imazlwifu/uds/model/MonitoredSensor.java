package org.imazlwifu.uds.model;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Wrapper class for <code>android.hardware.Sensor</code>
 * @author Sascha Eiteneuer
 *
 */
public class MonitoredSensor implements Monitorable, SensorEventListener {
	private SensorManager sensorManager;
	private SensorEvent lastEvent = null;
	
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
	 * Because some protocols (e.g. HTTP Post parameters) behave like a set,
	 * a unique key is formed by the expression:
	 * </p >
	 * <p><code style="text-indent:4em;">SensorName + '@' + Integer.toHexString( this.hashCode() )</code></p>
	 * 
	 * @return &lt;key, value&gt;
	 */
	@Override
	public Map<String, Float> values() {
		return values;
	}

	@Override
	public void updateData() {
		if( lastEvent != null )
			for( float f : lastEvent.values )
				values.put( sensor.getName() +'@'+ Integer.toHexString( this.hashCode() ), f );
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		lastEvent = event;
	}

	@Override
	public boolean registerListener() {
		return sensorManager.registerListener( this, sensor, SensorManager.SENSOR_DELAY_NORMAL );
	}
	
	@Override
	public void unregisterListener() {
		sensorManager.unregisterListener( this );
	}
}
