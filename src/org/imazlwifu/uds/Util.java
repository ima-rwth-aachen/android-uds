package org.imazlwifu.uds;

import android.content.Context;
import android.widget.Toast;

public class Util {
	public static void shortToast( Context context, String text ) {
		Toast.makeText( context, text, Toast.LENGTH_SHORT ).show();
	}
	

	public static void longToast( Context context, String text ) {
		Toast.makeText( context, text, Toast.LENGTH_LONG ).show();
	}
}
