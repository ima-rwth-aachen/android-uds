package org.imazlwifu.uds.ipc;

import org.imazlwifu.uds.DataService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class ServiceStarter extends BroadcastReceiver {
	public static String INTERVAL = "interval";
	
	@Override
	public void onReceive( Context context, Intent intent ) {
		String bootAction = "android.intent.action.BOOT_COMPLETED";
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( context );
		
		if( intent.getAction().equals( bootAction ) && prefs.getBoolean( "auto_start", false ) )
			;
		
		int interval = intent.getIntExtra( INTERVAL, DataService.defaultInterval );
		
		Log.d( "ServiceStarter", "starting with interval "+ Integer.toString( interval ) );
		
		Intent myAlarm = new Intent( context, AlarmReceiver.class);
		PendingIntent recurringAlarm = PendingIntent.getBroadcast( context, 0, myAlarm, PendingIntent.FLAG_CANCEL_CURRENT );
		AlarmManager alarms = (AlarmManager) context.getSystemService( Context.ALARM_SERVICE );
		alarms.setRepeating( AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval*1000, recurringAlarm );
	}

}
