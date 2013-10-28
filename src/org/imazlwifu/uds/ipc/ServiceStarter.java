package org.imazlwifu.uds.ipc;

import org.imazlwifu.uds.UDS;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * 
 * 
 * @author Sascha Eiteneuer
 *
 */
public class ServiceStarter extends BroadcastReceiver {
	/** true if BOOT_COMPLETED action is received */
	boolean isBootAction;
	/** true if preference "auto_start" is set */
	boolean startOnBoot;
	
	@Override
	public void onReceive( Context context, Intent intent ) {
		isBootAction = intent.getAction().equals( "android.intent.action.BOOT_COMPLETED" );
		startOnBoot = PreferenceManager.getDefaultSharedPreferences( context ).getBoolean( "auto_start", false );
		
		/* isBootAction		0	0	1	1
		 * startOnBoot		0	1	0	1
		 * execute			1	1	0	1
		 */
		if( !( isBootAction ^ startOnBoot ) || startOnBoot ) {
			int interval = intent.getIntExtra( UDS.EXTRA_INTERVAL, UDS.defaultInterval );
			
			Log.d( "ServiceStarter", "starting with interval "+ Integer.toString( interval ) );
			
			Intent myAlarm = new Intent( context, AlarmReceiver.class);
			PendingIntent recurringAlarm = PendingIntent.getBroadcast( context, 0, myAlarm, PendingIntent.FLAG_UPDATE_CURRENT );
			AlarmManager alarms = (AlarmManager) context.getSystemService( Context.ALARM_SERVICE );
			alarms.setRepeating( AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval*1000, recurringAlarm );
		}
	}

}
