package org.imazlwifu.uds;

import org.imazlwifu.uds.ipc.DataReceiver;
import org.imazlwifu.uds.ipc.StopServiceActivity;
import org.imazlwifu.uds.model.AppState;
import org.imazlwifu.uds.model.Monitorable;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class DataService extends Service {
	final public static int defaultInterval = 7;
	
	private AppState state;
	private DataReceiver receiver = new DataReceiver();
	
	@Override
	public void onCreate() {
		super.onCreate();
	
		Log.d( "DataService", "started" );
		
		state = (AppState) getApplication();
		
		receiver.setState( state );
		registerReceiver( receiver, new IntentFilter( "org.imazlwifu.uds.DataBroadcast" ) );
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d( "DataService", "id:"+ Integer.toString( startId ) );
		
		buildNotification();
		
		payload();
		
		return Service.START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		unregisterReceiver( receiver );
	}

	private void buildNotification() {
		Log.d( "DataService", "Notification setup" );
		
		if( !PreferenceManager.getDefaultSharedPreferences( this ).getBoolean( "show_notification", true ) ) {
			Log.d( "DataService", "canceling Notification" );
			
			((NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE )).cancel( 1 );
		} else {
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this)
				.addNextIntent( new Intent( this, StopServiceActivity.class) );
			
			Notification n = new NotificationCompat.Builder( this )
				.setContentTitle( "UDS monitoring update" )
		    	.setContentText( "tap to stop service" )
		    	.setSmallIcon( android.R.drawable.ic_menu_manage )
		    	.setAutoCancel( true )
		    	.setContentIntent( stackBuilder.getPendingIntent( 0, 0 ) )
				.build();
			
			((NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE )).notify( 1, n );
		}
	}
	
	private void payload() {
		Log.d( "DataService", state.getDebugString() );
		
		for( Monitorable m : state.getMonitorables() ) {
			if( state.getPreference( m ) ) {
				m.updateData();
				
				// update UI
				sendBroadcast( new Intent( "org.imazlwifu.uds.DataBroadcast" ) );
			}
		}
		
		
	}
}
