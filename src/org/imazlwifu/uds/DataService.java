package org.imazlwifu.uds;

import org.imazlwifu.uds.ipc.StopServiceActivity;
import org.imazlwifu.uds.model.LibConfig;
import org.imazlwifu.uds.model.Monitorable;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

/**
 * triggers updates of <code>Monitorable</code>s and possibly builds notification
 * 
 * @author Sascha Eiteneuer
 *
 */
public class DataService extends Service {
	@Override
	public void onCreate() {
		super.onCreate();
	
		Log.d( "DataService", "started" );
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d( "DataService", "id:"+ Integer.toString( startId ) );
		
		buildNotification( startId );
		
		payload();
		
		return Service.START_STICKY;
	}

	private void buildNotification(  int iteration ) {
		Log.d( "DataService", "Notification setup" );
		
		if( !PreferenceManager.getDefaultSharedPreferences( this ).getBoolean( "show_notification", true ) ) {
			Log.d( "DataService", "canceling Notification" );
			
			((NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE )).cancel( 1 );
		} else {
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this)
				.addNextIntent( new Intent( this, StopServiceActivity.class) );
			
			Notification n = new NotificationCompat.Builder( this )
				.setContentTitle( "monitoring updated" )
		    	.setContentText( "swipe down to expand" )
		    	.setSmallIcon( android.R.drawable.stat_notify_sync_noanim )
		    	.setContentInfo( "iteration: "+ iteration )
		    	.setAutoCancel( true )
		    	.addAction( android.R.drawable.ic_menu_delete, "stop monitoring", stackBuilder.getPendingIntent( 0, 0 ) )
//		    	.setContentIntent( stackBuilder.getPendingIntent( 0, 0 ) )
				.build();
			
			((NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE )).notify( 1, n );
		}
	}
	
	private void payload() {
		for( Monitorable m : LibConfig.instance.getMonitorables() )
			if( LibConfig.instance.getPreference( m ) )
				m.updateData();
		
		Log.d( "debug", LibConfig.instance.getUDS().getDebugString() );
		
		sendBroadcast( new Intent( UDS.ACTION_DATA_BROADCAST ) );
	}
}
