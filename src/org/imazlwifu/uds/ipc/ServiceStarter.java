package org.imazlwifu.uds.ipc;

import org.imazlwifu.uds.DataService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

public class ServiceStarter extends BroadcastReceiver {
	public static String INTERVAL = "interval";
	/** true if BOOT_COMPLETED action is received */
	boolean isBootAction;
	/** true if preference "auto_start" is set */
	boolean startOnBoot;
	
	@Override
	public void onReceive( Context context, Intent intent ) {
		isBootAction = intent.getAction().equals("android.intent.action.BOOT_COMPLETED");
		startOnBoot = PreferenceManager.getDefaultSharedPreferences( context ).getBoolean( "auto_start", false );
		
		/* flipped truth table needs less space
		 * isBootAction		0	0	1	1
		 * startOnBoot		0	1	0	1
		 * start service	1	1	0	1
		 * 
		 * XNOR: !(a^b)
		 */
		if( !( isBootAction ^ startOnBoot ) || startOnBoot ) {
			int interval = intent.getIntExtra( INTERVAL, DataService.defaultInterval );
			Log.d( "ServiceStarter", "starting with interval "+ Integer.toString( interval ) );
			
			Intent myAlarm = new Intent( context, AlarmReceiver.class);
			PendingIntent recurringAlarm = PendingIntent.getBroadcast( context, 0, myAlarm, PendingIntent.FLAG_CANCEL_CURRENT );
			AlarmManager alarms = (AlarmManager) context.getSystemService( Context.ALARM_SERVICE );
			alarms.setRepeating( AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval*1000, recurringAlarm );
		}
	}

}
