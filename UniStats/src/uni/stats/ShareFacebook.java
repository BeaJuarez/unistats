/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uni.stats;

import org.json.JSONException;
import org.json.JSONObject;

import uni.stats.SessionEvents.AuthListener;
import uni.stats.SessionEvents.LogoutListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class ShareFacebook extends Activity {

	// Facebook Application ID
	public static final String APP_ID = "205340276201357";

	private LoginButton mLoginButton;
	private TextView mText;

	private Button mPostButton;
	private Button mPostButton2;
	private Facebook mFacebook;
	private AsyncFacebookRunner mAsyncRunner;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.facebook);
		mLoginButton = (LoginButton) findViewById(R.id.login);
		mText = (TextView) ShareFacebook.this.findViewById(R.id.txt);
		mPostButton = (Button) findViewById(R.id.postButton);
		mPostButton2 = (Button) findViewById(R.id.postButton2);
		mFacebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);

		SessionStore.restore(mFacebook, this);
		SessionEvents.addAuthListener(new SampleAuthListener());
		SessionEvents.addLogoutListener(new SampleLogoutListener());
		mLoginButton.init(this, mFacebook);

		mPostButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mFacebook.dialog(ShareFacebook.this, "feed",
						new SampleDialogListener());
			}
		});
		mPostButton.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE
				: View.INVISIBLE);

		mPostButton2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mFacebook.dialog(ShareFacebook.this, "feed",
						new SampleDialogListener());
			}
		});
		mPostButton2.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE
				: View.INVISIBLE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mFacebook.authorizeCallback(requestCode, resultCode, data);
	}

	public class SampleAuthListener implements AuthListener {

		public void onAuthSucceed() {
			mText.setText("You have logged in! ");
			mPostButton.setVisibility(View.VISIBLE);
			mPostButton2.setVisibility(View.VISIBLE);

		}

		public void onAuthFail(String error) {
			mText.setText("Login Failed: " + error);
		}
	}

	public class SampleLogoutListener implements LogoutListener {
		public void onLogoutBegin() {
			mText.setText("Logging out...");
			mPostButton.setVisibility(View.INVISIBLE);
			mPostButton2.setVisibility(View.INVISIBLE);

		}

		public void onLogoutFinish() {
			mText.setText("You have logged out! ");
			mPostButton.setVisibility(View.INVISIBLE);
			mPostButton2.setVisibility(View.INVISIBLE);

		}
	}

	public class SampleRequestListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {
			try {
				// process the response here: executed in background thread
				Log.d("Facebook-Example", "Response: " + response.toString());
				JSONObject json = Util.parseJson(response);
				final String name = json.getString("name");

				// then post the processed result back to the UI thread
				// if we do not do this, an runtime exception will be generated
				// e.g. "CalledFromWrongThreadException: Only the original
				// thread that created a view hierarchy can touch its views."
				ShareFacebook.this.runOnUiThread(new Runnable() {
					public void run() {
						mText.setText("Hello there, " + name + "!");
					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (FacebookError e) {
				e.printStackTrace();
			}
		}
	}

	public class WallPostRequestListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {

			String message = "";
			try {
				JSONObject json = Util.parseJson(response);
				message = json.getString("message");
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (FacebookError e) {
				e.printStackTrace();
			}
			final String text = "Your message has been posted!";

			ShareFacebook.this.runOnUiThread(new Runnable() {
				public void run() {
					mText.setText(text);
				}
			});
		}
	}

	public class SampleDialogListener extends BaseDialogListener {
		public void onComplete(Bundle values) {
			final String postId = values.getString("post_id");
			if (postId != null) {
				mAsyncRunner.request(postId, new WallPostRequestListener());
			}
		}
	}

}
