package org.imazlwifu.uds.ipc;

import org.imazlwifu.uds.DataService;
import org.imazlwifu.uds.Util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * called by notification to cancel <code>AlarmManager</code> and stop <code>DataService</code>
 * @author se26082011
 *
 */
public class StopServiceActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent myAlarm = new Intent( this, AlarmReceiver.class );
		PendingIntent recurringAlarm = PendingIntent.getBroadcast( this, 0, myAlarm, PendingIntent.FLAG_CANCEL_CURRENT );
		AlarmManager alarms = (AlarmManager) getSystemService( Context.ALARM_SERVICE );
		alarms.cancel( recurringAlarm );
		
		stopService( new Intent( this, DataService.class ) );
		
		((NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE )).cancel( 1 );
		
		Util.shortToast( this, "Service stopped" );
		
		finish();
	}
}
