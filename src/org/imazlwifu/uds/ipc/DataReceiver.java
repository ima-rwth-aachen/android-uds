package org.imazlwifu.uds.ipc;

import org.imazlwifu.uds.model.AppState;
import org.imazlwifu.uds.model.Monitorable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * updating UI
 * @author se26082011
 *
 */
public class DataReceiver extends BroadcastReceiver {
	private AppState state;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// UI TODO encapsulate
		if( state != null && state.getTextBox() != null ) {
			state.setText( "" );
		
			for( Monitorable m : state.getMonitorables() ) {
				if( state.getPreference( m ) ) {
					// UI TODO encapsulate
					if( m.values().size() > 0 ) {
						for( String s : m.values().keySet() )
							state.addText( s +"  "+ Float.toString( m.values().get( s ) ) + "\n" );
					
						state.addText( "\n" );
					}
				}
			}
		}
	}

	public AppState getState() {
		return state;
	}

	public void setState(AppState state) {
		this.state = state;
	}

	
}
