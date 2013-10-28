package org.imazlwifu.uds;

import java.util.HashMap;
import java.util.Map;

import org.imazlwifu.uds.model.LibConfig;
import org.imazlwifu.uds.model.Monitorable;

import android.content.Context;
import android.content.Intent;
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
	final public static String EXTRA_INTERVAL = "interval";
	final public static int defaultInterval = 10;
	
	private Context context;
	private Map<Monitorable, Boolean> preferenceBinding = new HashMap<Monitorable, Boolean>();
	
	public UDS( Context context, Map<Monitorable, Boolean> preferenceBinding ) {
		this.context = context;
		this.preferenceBinding = preferenceBinding;
	}
	
	public void startService() {
		if( LibConfig.instance.getMonitorables( true ).size() < 1 )
			Util.shortToast( context, "Nothing to monitor\nService not started\nvisit settings" );
		else {
			Log.d( "UDS", "starting service" );
			
			Intent i = new Intent( ACTION_START_SERVICE );
			i.putExtra( EXTRA_INTERVAL, LibConfig.instance.getMonitoringInterval() );
			
			context.sendBroadcast( i );
		}
	}
	
	/** debug TODO delete? */
	public String getDebugString() {
		StringBuilder b = new StringBuilder();

		b.append("State " + this + " {\n");

		for( Monitorable m : preferenceBinding.keySet() )
			if (preferenceBinding.get(m)) {
				String s = "unknown";
				if (m.values() != null)
					s = m.values().values().toString();

				b.append("    " + m.getName() + ": " + s + "\n");
			}
		b.append("}");

		return b.toString();
	}
}
