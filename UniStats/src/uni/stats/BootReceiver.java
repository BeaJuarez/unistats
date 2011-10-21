
package uni.stats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		// Only start service is preference enabled
		SharedPreferences prefs = context.getSharedPreferences("prefs",context.MODE_PRIVATE);
		if (prefs.getBoolean("boot", false))
			context.startService(new Intent(context, UpdateService.class));
	}

}
