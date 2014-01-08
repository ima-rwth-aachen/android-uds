package org.imazlwifu.uds;

import org.imazlwifu.uds.model.Monitorable;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;

/**
 * 
 * 
 * @author Sascha Eiteneuer
 *
 */
public class PreferencesActivity extends PreferenceActivity {
	private PreferenceGroup prefGroup;
	
	@SuppressWarnings( "deprecation" )
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource( R.xml.preferences );
		
		/*
		 * saving remote address in LibConfig and giving feedback
		 */
		findPreference( "remote_address" ).setOnPreferenceChangeListener( new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if( LibConfig.instance.setRemoteAddress( (String) newValue ) )				
					return true;
				else {
					Util.longToast( getApplicationContext(), "invalid address" );
					
					return false;
				}
			}
		} );
		
		/*
		 * adding monitorables to preferences
		 */
		prefGroup = (PreferenceGroup) findPreference( "monitored" );
		
		if( LibConfig.instance.getMonitorables().size() > 0 ) {
			CheckBoxPreference pref;
			
			for( Monitorable m : LibConfig.instance.getMonitorables() ) {
				pref = createCheckBox( m );
				prefGroup.addPreference( pref );
			}
			
			getPreferenceScreen().addPreference( prefGroup );
		}
	}
	
	private CheckBoxPreference createCheckBox( final Monitorable m ) {
		CheckBoxPreference pref = new CheckBoxPreference( this );
		
		pref.setTitle( Util.clipObjectID( m.getName() ) );
		pref.setChecked( LibConfig.instance.getPreference( m ) );
		pref.setOrder( Preference.DEFAULT_ORDER );
		
		pref.setOnPreferenceClickListener( new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				boolean currentValue = LibConfig.instance.getPreference( m );
				
				LibConfig.instance.putDefaultMonitorable( m, !currentValue );
				
				return true;
			}
		} );
		
		return pref;
	}
	
	
}
