package org.imazlwifu.uds.rpc;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import org.imazlwifu.uds.model.Monitorable;
import org.imazlwifu.uds.model.LibConfig;

import android.util.Log;

/**
 * implementation of <code>RemoteConncetion</code>
 * @author se26082011
 *
 */
public class HTTPRemoteConnection implements RemoteConnection {
	private LibConfig uds = LibConfig.instance;
	
	public void publishData() {
		String dataString = buildDataString();
		
		HttpURLConnection connection = setupConnection( dataString.length() );
		
		DataOutputStream output = null;
		
		try {
			Log.d( "HTTPRemoteConnection", "connecting" );
			connection.connect();
			
			output = new DataOutputStream( connection.getOutputStream() );
			
			output.writeBytes( dataString );
			output.flush();
			
			Log.d( "HTTPRemoteConnection", "Response Code: "+ connection.getResponseCode() );
		} catch( IOException e ) {
			e.printStackTrace();
		} finally {
			if( output != null )
				try {
					output.close();
				} catch (IOException e) {}
		}
	}
	
	private String buildDataString() {
		StringBuilder b = new StringBuilder();
		
		for( Monitorable m : uds.getMonitorables() )
			if( uds.getPreference( m ) )
				for( String s : m.values().keySet() )
					b.append( convertSpaces( s ) +"="+ m.values().get( s ) +"&" );
		
		if( b.length() > 1 && b.charAt( b.length() - 1 ) == '&' )
			b.deleteCharAt( b.length() - 1 );
		
		Log.d( "HTTPRemoteConnection", "Data: "+ b.toString() );
		
		return b.toString();
	}

	/**
	 * replaces empty spaces with their hexadecimal value prepended by '%'<br />
	 * e.g. "Gravity Sensor" -> "Gravity%20Sensor"
	 * 
	 * @param s  
	 * @return
	 */
	private String convertSpaces( String s ) {
		return s.replace( " ", "%20" );
	}
	
	private HttpURLConnection setupConnection( int dataLength ) {
		HttpURLConnection connection = null;
		
		try {
			connection = (HttpURLConnection) uds.getRemoteAddress().openConnection();
			
			connection.setRequestMethod( "POST" );
			connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
			connection.setRequestProperty( "Content-Length", "" + dataLength );
			
		} catch( IOException e ) {
			e.printStackTrace();
		}
		
		return connection;
	}
}
