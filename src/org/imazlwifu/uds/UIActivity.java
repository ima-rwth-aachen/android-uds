package org.imazlwifu.uds;

import org.imazlwifu.uds.ipc.StopServiceActivity;
import org.imazlwifu.uds.model.Battery;
import org.imazlwifu.uds.model.LibConfig;

import android.app.Activity;
import android.content.Intent;
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
	UDS uds = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setUpUDS();
		
		uiBinding();
	}
	
	private void setUpUDS() {
		LibConfig.instance.setMonitoringInterval( 10 );
		LibConfig.instance.putDefaultMonitorables();
		LibConfig.instance.putCustomMonitorable( Battery.class );
		LibConfig.instance.setTextBox( new TextView( this ) );
		
		uds = LibConfig.instance.getUDS();
	}

	private void uiBinding() {
		setContentView( LibConfig.instance.getTextBox() );
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
				startActivityForResult( new Intent( this, PreferencesActivity.class ), 0 );
				break;
			case 1:
				uds.startService();
				break;
			case 2:
				startActivity( new Intent( this, StopServiceActivity.class ) );
				break;
		}
		
		return true;
	}
}
