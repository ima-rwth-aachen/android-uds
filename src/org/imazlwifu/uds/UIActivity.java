package org.imazlwifu.uds;

import org.imazlwifu.uds.model.Battery;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * An entry point example.
 * 
 * 
 * @author Sascha Eiteneuer
 *
 */
public class UIActivity extends Activity {
	private LibConfig config;
	private UDS uds;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setUpUDS();
		
		uiBinding();
	}
	
	private void setUpUDS() {
		config = (LibConfig) getApplication();
		// Test Code - not a clean implementation
		config.setTextBox( new TextView( this ) );
		
		uds = config.getUDS();
		uds.setMonitoringInterval( 10 );
		uds.putMonitoredSensors();
		uds.putCustomMonitorable( Battery.class );
/* TODO		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this)
			.addNextIntent( new Intent( this, StopServiceActivity.class) );
		
		Notification n = new NotificationCompat.Builder( this )
			.setContentTitle( "monitoring updated" )
	    	.setContentText( "swipe down to expand" )
	    	.setSmallIcon( android.R.drawable.stat_notify_sync_noanim )
//	    	.setContentInfo( "iteration: "+ iteration )
	    	.setAutoCancel( true )
	    	.addAction( android.R.drawable.ic_menu_delete, "stop monitoring", stackBuilder.getPendingIntent( 0, 0 ) )
//		    	.setContentIntent( stackBuilder.getPendingIntent( 0, 0 ) )
			.build();
		*/

	}

	private void uiBinding() {
		setContentView( config.getTextBox() );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add( 0, 0, 0, "settings" );
		menu.add( 1, 1, 1, "start service" );
		menu.add( 1, 2, 2, "STOP service" );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch( item.getItemId() ) {
			case 0:
				uds.startPreferenceActivity( this );
				break;
			case 1:
				uds.startService();
				break;
			case 2:
				uds.stopService();
				break;
		}
		
		return true;
	}
}
