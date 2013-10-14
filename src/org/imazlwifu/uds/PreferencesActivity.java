package org.imazlwifu.uds;

import org.imazlwifu.uds.model.AppState;
import org.imazlwifu.uds.model.Monitorable;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;

public class PreferencesActivity extends PreferenceActivity {
	private AppState state;
	private PreferenceGroup prefGroup;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		state = (AppState) getApplication();
		
		addPreferencesFromResource(R.xml.preferences);
		
		prefGroup = (PreferenceGroup) findPreference("monitored");
		
		if( state.getMonitorables().size() > 0 ) {
			CheckBoxPreference pref;
			
			for( Monitorable m : state.getMonitorables() ) {
				pref = createCheckBox( m );
				prefGroup.addPreference( pref );
			}
			
			getPreferenceScreen().addPreference( prefGroup );
		}
	}
	
	private CheckBoxPreference createCheckBox( final Monitorable m ) {
		CheckBoxPreference pref = new CheckBoxPreference( this );
		
		pref.setTitle( m.getName() );
		pref.setChecked( state.getPreference( m ) );
		pref.setOrder( Preference.DEFAULT_ORDER );
		
		pref.setOnPreferenceClickListener( new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				boolean currentValue = state.getPreference( m );
				
				state.putPreference( m, !currentValue );
				
				return true;
			}
		} );
		
		return pref;
	}
	
	
}
