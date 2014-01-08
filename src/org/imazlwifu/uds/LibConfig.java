package org.imazlwifu.uds;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.imazlwifu.uds.model.Monitorable;
import org.imazlwifu.uds.model.MonitoredSensor;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

/**
 * value object extending <code>Application</code>
 * <br/>
 * creates controller object for the library (<code>getUDS()</code> method)
 * 
 * @author Sascha Eiteneuer
 *
 */
public class LibConfig extends Application {
	final private String tag = "LibConfig";
	
	
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
	
	
	public static LibConfig instance;
	private int monitoringInterval;
	private URL remoteAddress = null;
	private Map<Monitorable, Boolean> preferenceBinding = new HashMap<Monitorable, Boolean>();
	
	private UDS uds;
	
	@Override
	public void onCreate() {
		super.onCreate();

		if( instance == null )
			instance = this;
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences( this );
		
		setRemoteAddress( pref.getString( "remote_address", "" ) );
		
		setupMonitorables( pref );
		
		uds = new UDS( this );
	}
	
	void setupMonitorables( SharedPreferences pref ) {
		if( getMonitorables().size() == 0 ) {
			Log.d( tag, "setup" );
			
			if( pref.getBoolean( "useDefaults", false ) ) {
				// adding all available sensors
				SensorManager sensorManager = (SensorManager) getSystemService( Context.SENSOR_SERVICE );
				
				for( Sensor s : sensorManager.getSensorList( Sensor.TYPE_ALL ) ) {
					putDefaultMonitorable( new MonitoredSensor( this, s ) , pref.getBoolean( s.getName(), false ) );
				}
			}
			
			// adding custom monitorables
			for( String name : pref.getAll().keySet() ) {
				if( name.contains( "." ) ) {
					try {
						putCustomMonitorable( pref, Class.forName( name ), pref.getBoolean( name, false ) );
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void putDefaultMonitorables() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences( this );
		Editor e = sp.edit();
		e.putBoolean( "useDefaults", true );
		e.commit();
	}
	
	public void putDefaultMonitorable( Monitorable key, boolean value ) {
			preferenceBinding.put( key, value );
			
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences( this );
			Editor e = sp.edit();
			e.putBoolean( key.getName(), value );
			e.commit();
	}

	public void putCustomMonitorable( Class<?> c ) {
		// preventing multiple creation
		for( String name : PreferenceManager.getDefaultSharedPreferences( this ).getAll().keySet() )
			if( name.equals( c.getName() ) )
				return;
		
		putCustomMonitorable( PreferenceManager.getDefaultSharedPreferences( this ), c, true );
	}
	
	private void putCustomMonitorable( SharedPreferences pref, Class<?> c, boolean monitoring ) {
		Log.d( tag, "creating "+ c.getName() );
		
		Monitorable m;
		try {
			@SuppressWarnings("unchecked")
			Class<Monitorable> clazz = (Class<Monitorable>) Class.forName( c.getName() );
			Constructor<Monitorable> ctor = clazz.getConstructor( Context.class );
			m = ctor.newInstance( this.getApplicationContext() );
			
			preferenceBinding.put( m, monitoring );
			
			Editor e = pref.edit();
			e.putBoolean( c.getName(), monitoring );
			e.commit();
		} catch( InstantiationException e1 ) {
			e1.printStackTrace();
		} catch( IllegalAccessException e1 ) {
			e1.printStackTrace();
		} catch( ClassNotFoundException e1 ) {
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public boolean getPreference( Monitorable key ) {
		if( preferenceBinding.get( key ) != null )
			return preferenceBinding.get( key );
		
		return false;
	}
	
	/**
	 * see also <code>getMonitorables( boolean enabled )</code>
	 * @return the set of all instances available for monitoring
	 */
	public Set<Monitorable> getMonitorables() {
		return preferenceBinding.keySet();
	}
	
	/**
	 * see also <code>getMonitorables()</code>
	 * @param enabled <code>true</code> for monitored | <code>false</code> for <b>not</b> monitored
	 * @return the subset of all monitored resp. <b>not</b> monitored <code>Monitorable</code>s
	 */
	public Set<Monitorable> getMonitorables( boolean enabled ) {
		Set<Monitorable> set = new HashSet<Monitorable>();
		
		for( Monitorable m : getMonitorables() )
			if( getPreference( m ) == enabled )
				set.add( m );
		
		return set;
	}
	
	public URL getRemoteAddress() {
		return remoteAddress;
	}
	
	public boolean setRemoteAddress( String url ) {
		if( url.equals( "" ) ) {
			Log.d( tag, "no addresse set" );
		} else {
			try {
				remoteAddress = new URL( url );
			} catch( MalformedURLException e ) {
				return false;
			}
		}
		
		return true;
	}
	
	public int getMonitoringInterval() {
		return monitoringInterval;
	}
	
	public void setMonitoringInterval( int monitoringInterval ) {
		this.monitoringInterval = monitoringInterval;
	}
	
	/**
	 * @return Controller for the library
	 */
	public UDS getUDS() {
		return uds;
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Log.d( tag, "low mem" );
	}

	Map<Monitorable, Boolean> getBindings() {
		return preferenceBinding;
	}

	public boolean getMonitorable(Monitorable m) {
		for( Monitorable tmp : getMonitorables( true ) )
			if( tmp.equals( m ) )
				return true;
		
		return false;
	}
}
