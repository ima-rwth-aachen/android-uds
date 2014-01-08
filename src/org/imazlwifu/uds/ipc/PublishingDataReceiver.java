package org.imazlwifu.uds.ipc;

import org.imazlwifu.uds.LibConfig;
import org.imazlwifu.uds.Util;
import org.imazlwifu.uds.model.Monitorable;
import org.imazlwifu.uds.rpc.DataPublisher;
import org.imazlwifu.uds.rpc.HTTPRemoteConnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * base implementation updating the UI and
 * publishing the monitored data using <code>HTTPRemoteConnection</code> (base
 * implementation of <code>RemoteConnection</code>).
 * 
 * @author Sascha Eiteneuer
 * 
 */
public class PublishingDataReceiver extends BroadcastReceiver {
	private LibConfig uds = LibConfig.instance;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		updateUI();
		
		publishData();
	}
	
	private void updateUI() {
		if( uds != null && uds.getTextBox() != null ) {
			uds.setText( "" );
		
			for( Monitorable m : uds.getMonitorables() ) {
				if( uds.getPreference( m ) ) {
					if( m.values().size() > 0 ) {
						for( String s : m.values().keySet() )
							uds.addText( Util.clipObjectID( s ) +": "+ Float.toString( m.values().get( s ) ) + "\n" );
					
						uds.addText( "\n" );
					}
				}
			}
		}
	}
	
	private void publishData() {
		if( null != uds.getRemoteAddress() )
			new DataPublisher().execute( new HTTPRemoteConnection() );
	}
}
