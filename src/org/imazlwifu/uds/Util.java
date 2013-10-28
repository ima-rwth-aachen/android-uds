package org.imazlwifu.uds;

import android.content.Context;
import android.widget.Toast;

public class Util {
	public static void shortToast( Context context, String text ) {
		Toast.makeText( context, text, Toast.LENGTH_SHORT ).show();
	}
	
	public static void longToast( Context context, String text ) {
		Toast.makeText( context, text, Toast.LENGTH_LONG ).show();
	}
	
	/**
	 * clips the object id added in the method <code>onSensorChanged</code> of <code>MonitoredSensor</code>
	 * @param s e.g. "Gyroscope Sensor@420912b8"
	 * @return e.g. "Gyroscope Sensor"
	 */
	public static String clipObjectID( String s ) {
		if( !s.contains( "@" ) )
			return s;
		else {
			int firstOccurrence = s.indexOf( "@" );
			
			return s.substring( 0, firstOccurrence );
		}
	}
}
