package org.imazlwifu.uds.model;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class Battery implements Monitorable {
	public final static String NAME = "Battery";
	private Map<String, Float> values;
	
	private Context context;
	
	public Battery( Context context ) {
		this.context = context;
		values = new HashMap<String, Float>();
	}
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Map<String, Float> values() {
		return values;
	}

	@Override
	public void updateData() {
		Intent battery = context.registerReceiver( null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED) );
		Bundle extras = battery.getExtras();
		
		for( String s : extras.keySet() ) {
			try {
				float f = (Integer) extras.get( s );
				
				values.put( "Battery "+s, f );
			} catch( Exception e ) {}
		}
		
	}

}
