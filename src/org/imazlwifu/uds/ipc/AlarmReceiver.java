package org.imazlwifu.uds.ipc;

import org.imazlwifu.uds.DataService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Starts the <code>DataService</code> on broadcast by <code>AlarmManager</code>
 * @author Sascha Eiteneuer
 *
 */
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive( Context context, Intent intent ) {
		context.startService( new Intent( context, DataService.class ) );
	}

}
