package org.imazlwifu.uds.rpc;

import android.os.AsyncTask;
import android.util.Log;

/**
 * AsyncTask that sends data of given <code>RemoteConnection</code>s to <code>RemoteAddress</code>.
 * @author Sascha Eiteneuer
 *
 */
public class DataPublisher extends AsyncTask<RemoteConnection, Void, Void> {
	
	@Override
	protected Void doInBackground(RemoteConnection... params) {
		Log.d( "DataPublisher", "publishing" );
		
		for( RemoteConnection rc : params ) {
			rc.publishData();
		}
		
		return null;
	}

}
