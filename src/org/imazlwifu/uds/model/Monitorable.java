package org.imazlwifu.uds.model;

import java.util.Map;

import android.content.Context;

public interface Monitorable {
	public String getName();
	
	/**
	 * label, value
	 * e.g. "Battery Level", 94.0
	 * @return
	 */
	public Map<String, Float> values();

	/**
	 * e.g. register receiver/listener and read intent extras<br />
	 * see <code>Battery</code>
	 */
	public void updateData();

	void setContext(Context context);
}
