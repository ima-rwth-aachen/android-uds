package org.imazlwifu.uds;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import org.imazlwifu.uds.model.Monitorable;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Controller class created by LibConfig
 * 
 * @author Sascha Eiteneuer
 *
 */
public class UDS {
	final public static String ACTION_ALARM = "com.company.android.AlarmReceiver";
	final public static String ACTION_DATA_BROADCAST = "org.imazlwifu.uds.DataBroadcast";
	final public static String ACTION_START_SERVICE = "org.imazlwifu.uds.StartService";
	final public static String ACTION_STOP_SERVICE = "org.imazlwifu.uds.StopService";
	final public static String EXTRA_INTERVAL = "interval";
	final public static int DEFAULT_INTERVAL = 10;
	
	private LibConfig config;
	private Context context;
	
	UDS( LibConfig config ) {
		this.config = config;
		this.context = config.getApplicationContext();
	}
	
	public void startService() {
		if( LibConfig.instance.getMonitorables( true ).size() < 1 )
			Util.shortToast( context, "Nothing to monitor\nService not started\nvisit settings" );
		else {
			Log.d( "UDS", "starting service" );
			
			Intent i = new Intent( ACTION_START_SERVICE );
			i.putExtra( EXTRA_INTERVAL, config.getMonitoringInterval() );
			
			context.sendBroadcast( i );
		}
	}
	
	public void stopService() {
		Log.d( "UDS", "stopping service" );
		
		context.sendBroadcast( new Intent( ACTION_STOP_SERVICE ) );
	}
	
	public void startPreferenceActivity( Activity callingActivity ) {
		callingActivity.startActivityForResult( new Intent( callingActivity, PreferencesActivity.class ), 0 );
	}
	
	/**
	 * makes all on the device available sensors accessable for monitoring
	 */
	public void putMonitoredSensors() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences( context );
		Editor e = sp.edit();
		e.putBoolean( "useDefaults", true );
		e.commit();
		
		config.setupMonitorables( sp );
	}
	
	/**
	 * makes parameter class available for monitoring
	 */
	public void putCustomMonitorable( Class<?> c ) {
		// preventing multiple creation
		for( String name : PreferenceManager.getDefaultSharedPreferences( context ).getAll().keySet() )
			if( name.equals( c.getName() ) ) {
				Log.d( "UDS", ""+c.getName()+" already in existance" );
				return;
			}
		
		putCustomMonitorable( PreferenceManager.getDefaultSharedPreferences( context ), c.getName(), true );
	}
	
	// package protected
	void putCustomMonitorable( SharedPreferences pref, String name, boolean monitoring ) {
		Log.d( "UDS", "creating "+ name );
		
		Monitorable m;
		try {
			@SuppressWarnings("unchecked")
			Class<Monitorable> c = (Class<Monitorable>) Class.forName( name );
			Constructor<Monitorable> ctor = c.getConstructor( Context.class );
			m = ctor.newInstance( context );
			
			getBindings().put( m, monitoring );
			
			Editor e = pref.edit();
			e.putBoolean( name, monitoring );
			e.commit();
		} catch( ClassNotFoundException e ) {
			Log.d( "", "Error while putting custom monitorable. No class found matching "+ name +"\nStack Trace: " );
			e.printStackTrace();
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// package protected
	void putDefaultMonitorable( Monitorable key, boolean value ) {
			getBindings().put( key, value );
			
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences( context );
			Editor e = sp.edit();
			e.putBoolean( key.getName(), value );
			e.commit();
	}

	protected Notification updateNotification( int iteration ) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Set<Monitorable> getMonitorables() {
		return config.getMonitorables();
	}
	
	private Map<Monitorable, Boolean> getBindings() {
		return config.getBindings();
	}
	
	public void setMonitoringInterval( int monitoringInterval ) {
		config.setMonitoringInterval( monitoringInterval );
	}
	
	public int getMonitoringInterval() {
		return config.getMonitoringInterval();
	}
	
	/** debug TODO delete? */
	public String getDebugString() {
		StringBuilder b = new StringBuilder();

		b.append("State " + this + " {\n");

		for( Monitorable m : getMonitorables() )
			if( getBindings().get(m) ) {
				String s = "unknown";
				if (m.values() != null)
					s = m.values().values().toString();

				b.append("    " + m.getName() + ": " + s + "\n");
			}
		b.append("}");

		return b.toString();
	}

	public boolean isEnabled(Monitorable m) {
		return config.getMonitorable( m );
	}
}
