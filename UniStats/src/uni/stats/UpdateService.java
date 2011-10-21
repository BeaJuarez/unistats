/* ========================================================================
 * Copyright 2010, Handy Codeworks LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================================
 */
package uni.stats;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import winterwell.jtwitter.Twitter.Status;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class UpdateService extends Service implements
		OnSharedPreferenceChangeListener {
	static final String TAG = "UpdateService";
	static final int NOTIFICATION_ID = 1334;
	static final String ACTION_NEW_TWEETS = "ACTION_NEW_TWEETS";

	// Instance variables
	Twitter mTwitter;
	Handler mHandler;
	Updater mUpdater;
	DatabaseOpenHelper mDBhelper;
	SQLiteDatabase mDb;
	SharedPreferences prefs;
	NotificationManager mNotificationManager;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// Get shared preferences
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		// Init twitter connection
		mTwitter = getTwitter();

		// Handler for threads
		mHandler = new Handler();

		// Initialize database
		mDBhelper = new DatabaseOpenHelper(this);
		mDb = mDBhelper.getWritableDatabase();

		// Notification manager
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	private Twitter getTwitter() {

		if (mTwitter == null) {
			String username = prefs.getString("username", null);
			String password = prefs.getString("password", null);
			if (username != null && password != null) {
				mTwitter = new Twitter(username, password);
				mTwitter.setSource(TAG);
			}
		}
		
		return mTwitter;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		mUpdater = new Updater();
		mHandler.post(mUpdater);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacks(mUpdater);
		mDb.close();
	}

	class Updater implements Runnable {
		private static final String TAG = "Updater";
		private static final long DELAY = 60000L; // 60s

		public void run() {
			boolean hasNewTweets = false;

			try {
				for (Status s : getTwitter().getFriendsTimeline()) {
					ContentValues v = DatabaseOpenHelper
							.statusToContentValues(s);
					try {
						// Insert will throw exceptions for duplicate IDs
						mDb.insertOrThrow(DatabaseOpenHelper.TABLE, null, v);
						hasNewTweets = true; // Only runs if no exception form
												// insert
					} catch (SQLException e) {
					}
				}
			} catch (NullPointerException npe) {
				npe.printStackTrace();
			} catch (TwitterException.RateLimit ter) {
				Toast.makeText(UpdateService.this,
						"Rate limited (Too much Twitter!", Toast.LENGTH_SHORT)
						.show();
			} catch (TwitterException.Timeout tet) {
				tet.printStackTrace();
			} catch (TwitterException te) {
				te.printStackTrace();
			}

			// Run again in DELAY seconds
			mHandler.postDelayed(this, DELAY);
		}

	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		mTwitter = null;
	}

}
