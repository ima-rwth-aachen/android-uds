package org.imazlwifu.uds.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

public class AppState extends Application {
// TODO UI test code
	private TextView textbox;
	
	public TextView getTextBox() {
		return textbox;
	}
	
	public void setTextBox( TextView view ) {
		this.textbox = view;
	}
	
	public void setText( String text ) {
		textbox.setText( text );
	}
	
	public void addText( String text ) {
		textbox.append( text );
	}
// TODO end of test code
	
	
	/**	interval given to Android AlarmManager */
	public int monitoringInterval;
	private Map<Monitorable, Boolean> preferenceBinding = new HashMap<Monitorable, Boolean>();
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences( this );
		
		if( getMonitorables().size() == 0 ) {
			Log.d( "AppState", "setup" );
			
			// adding battery monitoring
			putPreference( new Battery( this ), pref.getBoolean( Battery.NAME, false ) );
			
			// adding all available sensors
			SensorManager sensorManager = (SensorManager) getSystemService( Context.SENSOR_SERVICE );
			
			for( Sensor s : sensorManager.getSensorList( Sensor.TYPE_ALL ) ) {
				putPreference( new MonitoredSensor( this, s ) , pref.getBoolean( s.getName(), false ) );
			}
		}
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Log.d( "AppState", "low mem" );
	}
	
	public void putPreference( Monitorable key, boolean value ) {
			preferenceBinding.put( key, value );
			
			Editor e = PreferenceManager.getDefaultSharedPreferences( this ).edit();
			e.putBoolean( key.getName(), value );
			e.commit();
	}
	
	public boolean getPreference( Monitorable key ) {
		if( preferenceBinding.get( key ) != null )
			return preferenceBinding.get( key );
		
		return false;
	}
	
	public Set<Monitorable> getMonitorables() {
		return preferenceBinding.keySet();
	}
	
	
	/** debug TODO delete? */
	public String getDebugString() {
		StringBuilder b = new StringBuilder();
		
		b.append( "State "+ this +" {\n" );
		
		for( Monitorable m : getMonitorables() )
			if( preferenceBinding.get( m ) ) {
				String s = "unknown";
				if( m.values() != null )
					s = m.values().values().toString();
				
				b.append( "    "+ m.getName() +": "+ s + "\n" );
			}
		b.append( "}" );
		
		return b.toString();
	}
}
