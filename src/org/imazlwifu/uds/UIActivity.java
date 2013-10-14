package org.imazlwifu.uds;

import org.imazlwifu.uds.ipc.ServiceStarter;
import org.imazlwifu.uds.ipc.StopServiceActivity;
import org.imazlwifu.uds.model.AppState;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class UIActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		uiBinding();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Log.d( "UIActivity", "on destroy" );
	}
	
	private void startService( int interval ) {
		Log.d( "UIActivity", "starting service" );
		Intent i = new Intent( "org.imazlwifu.uds.StartService" );
		i.putExtra( ServiceStarter.INTERVAL, interval );
		
		sendBroadcast( i );
	}

	private void uiBinding() {
		TextView box = new TextView( this );
		( (AppState) getApplication()).setTextBox( box );
		
		setContentView( box );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add( 0, 0, 0, "Settings" ).setIcon( android.R.drawable.ic_menu_preferences );
		menu.add( 1, 1, 1, "start service" ).setIcon( android.R.drawable.ic_media_play );
		menu.add( 1, 2, 2, "STOP service" ).setIcon( android.R.drawable.ic_menu_delete );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch( item.getItemId() ) {
			case 0:
				startActivityForResult( new Intent( this, PreferencesActivity.class ), 0 );
				break;
			case 1:
				startService( DataService.defaultInterval );
				break;
			case 2:
				startActivity( new Intent( this, StopServiceActivity.class ) );
				break;
		}
		
		return true;
	}
}
