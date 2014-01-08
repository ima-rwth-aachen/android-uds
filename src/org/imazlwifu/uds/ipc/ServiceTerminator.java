package org.imazlwifu.uds.ipc;

import org.imazlwifu.uds.DataService;
import org.imazlwifu.uds.LibConfig;
import org.imazlwifu.uds.Util;
import org.imazlwifu.uds.model.Monitorable;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * called by notification to cancel <code>AlarmManager</code> and stop <code>DataService</code>
 * @author se26082011
 *
 */
public class ServiceTerminator extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent myAlarm = new Intent( context, AlarmReceiver.class );
		PendingIntent recurringAlarm = PendingIntent.getBroadcast( context, 0, myAlarm, PendingIntent.FLAG_CANCEL_CURRENT );
		AlarmManager alarms = (AlarmManager) context.getSystemService( Context.ALARM_SERVICE );
		alarms.cancel( recurringAlarm );
		
		context.stopService( new Intent( context, DataService.class ) );
		
		((NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE )).cancel( 1 );
		

		for( Monitorable m : LibConfig.instance.getMonitorables() ) {
			m.unregisterListener();
		}
		
		Util.shortToast( context, "Service stopped" );
	}
}
