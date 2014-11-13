package com.example.blackmail;

import java.io.File;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class AccountSetupActivity extends FragmentActivity implements OnClickListener  {
	
	private static final String PREF_NAME = "sample_twitter_pref";
	private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
	private static final String PREF_USER_NAME = "twitter_user_name";

	/* Any number for uniquely distinguish your request */
	public static final int WEBVIEW_REQUEST_CODE = 100;

	private static Twitter twitter;
	private static RequestToken requestToken;

	private ProgressDialog pDialog;
	private static SharedPreferences mSharedPreferences;

	public String tokenString;
	public String tokenSecret;

	private View loginLayout;
	private View shareLayout;

	private String consumerKey = null;
	private String consumerSecret = null;
	private String callbackUrl = null;
	private String oAuthVerifier = null;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Twitter API Initializers
		initTwitterConfigs();

		/* Enabling strict mode */
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		setContentView(R.layout.account_setup);

		loginLayout = (RelativeLayout) findViewById(R.id.login_layout);
		shareLayout = (LinearLayout) findViewById(R.id.share_layout);

		findViewById(R.id.btn_login).setOnClickListener(this);
		findViewById(R.id.updateTwitterButton).setOnClickListener(this);
		
		
		/* Initialize application preferences */
		mSharedPreferences = getSharedPreferences(PREF_NAME, 0);

		boolean isLoggedIn = mSharedPreferences.getBoolean(
				PREF_KEY_TWITTER_LOGIN, false);

		/* if already logged in, then hide login layout and show share layout */
		if (isLoggedIn) {
			loginLayout.setVisibility(View.GONE);
			shareLayout.setVisibility(View.VISIBLE);
		} else {
			loginLayout.setVisibility(View.VISIBLE);
			shareLayout.setVisibility(View.GONE);

			Uri uri = getIntent().getData();

			if (uri != null && uri.toString().startsWith(callbackUrl)) {

				String verifier = uri.getQueryParameter(oAuthVerifier);

				try {

					/* Getting oAuth authentication token */
					AccessToken accessToken = twitter.getOAuthAccessToken(
							requestToken, verifier);

					/* Getting user id form access token */
					// long userID = accessToken.getUserId();
					// final User user = twitter.showUser(userID);
					// final String username = user.getName();

					/* save updated token */
					saveTwitterInfo(accessToken);

					loginLayout.setVisibility(View.GONE);
					shareLayout.setVisibility(View.VISIBLE);
					// userName.setText(getString(R.string.hello) + username);

				} catch (Exception e) {
					Log.e("Failed to login Twitter!!", e.getMessage());
				}
			}

		}
		
		
		//Facebook API intializers
	}

	/**
	 * Saving user information, after user is authenticated for the first time.
	 * You don't need to show user to login, until user has a valid access toen
	 */
	private void saveTwitterInfo(AccessToken accessToken) {

		long userID = accessToken.getUserId();
		Log.d("Save twit info", "user id: " + userID);

		User user;
		try {
			user = twitter.showUser(userID);
			Log.d("Try block entrance", "Got into try block");
			String username = user.getName();

			/* Storing oAuth tokens to shared preferences */

			Editor e = mSharedPreferences.edit();
			Log.d("Try block entrance", "Got into try block");
			// Save in db instead
			e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());

			Log.d("sharedToken",
					mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, ""));
			// Save in db instead
			e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
			e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
			e.putString(PREF_USER_NAME, username);
			e.commit();

		} catch (TwitterException e1) {
			Log.d("Bad Twitter", "Caught exception Look at");
			e1.printStackTrace();
		}
	}

	/* Reading twitter essential configuration parameters from strings.xml */
	private void initTwitterConfigs() {
		consumerKey = getString(R.string.twitter_consumer_key);
		consumerSecret = getString(R.string.twitter_consumer_secret);
		callbackUrl = getString(R.string.twitter_callback);
		oAuthVerifier = getString(R.string.twitter_oauth_verifier);
	}

	private void loginToTwitter() {
		boolean isLoggedIn = mSharedPreferences.getBoolean(
				PREF_KEY_TWITTER_LOGIN, false);

		if (!isLoggedIn) {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(consumerKey);
			builder.setOAuthConsumerSecret(consumerSecret);
			Log.d("First", "Set consumer key and secret");

			final Configuration configuration = builder.build();
			final TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			try {
				requestToken = twitter.getOAuthRequestToken(callbackUrl);

				/**
				 * Loading twitter login page on webview for authorization Once
				 * authorized, results are received at onActivityResult
				 * */
				Log.d("Sec-A", "Set Getting request token");
				final Intent intent = new Intent(this, WebViewActivity.class);
				intent.putExtra(WebViewActivity.EXTRA_URL,
						requestToken.getAuthenticationURL());
				startActivityForResult(intent, WEBVIEW_REQUEST_CODE);

			} catch (TwitterException e) {
				Log.d("Sec-B", "No request");
				e.printStackTrace();
			}
		} else {

			loginLayout.setVisibility(View.GONE);
			shareLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			String verifier = data.getExtras().getString(oAuthVerifier);
			try {
				AccessToken accessToken = twitter.getOAuthAccessToken(
						requestToken, verifier);
				Log.d("Got in", "OAuth Access token");
				saveTwitterInfo(accessToken);

				loginLayout.setVisibility(View.GONE);
				Log.d("LS", "Login layout gone");
				shareLayout.setVisibility(View.VISIBLE);
				Log.d("SL", "Share layout should be visible");

			} catch (Exception e) {
				Log.e("Twitter Login Failed", e.getMessage());
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			loginToTwitter();
			break;
		case R.id.updateTwitterButton:
			new updateTwitterStatus().execute("Input Text");
			break;
		}
	}
		
	class updateTwitterStatus extends AsyncTask<String, String, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(AccountSetupActivity.this);
			pDialog.setMessage("Posting to twitter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected Void doInBackground(String... args) {

			String status = args[0];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(getString(R.string.twitter_consumer_key));
				builder.setOAuthConsumerSecret(getString(R.string.twitter_consumer_secret));

				// Access Token - Instead of preferences get from db
				String access_token = mSharedPreferences.getString(
						PREF_KEY_OAUTH_TOKEN, "");

				// Access Token Secret - Instead of preferences get from db
				String access_token_secret = mSharedPreferences.getString(
						PREF_KEY_OAUTH_SECRET, "");

				AccessToken accessToken = new AccessToken(access_token,
						access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build())
						.getInstance(accessToken);
				
				// Update status
				StatusUpdate statusUpdate = new StatusUpdate(status);

				// File dir = Environment.getExternalStorageDirectory();
				// File yourFile = new File(dir,
				// "Pictures/Screenshots/Green_Cat.png");

				// Need string path of image you want to upload
				File yourFile = new File(
						"/sdcard/Pictures/Messenger/reflection_mario.jpeg");

				// InputStream is =
				// getResources().openRawResource(R.drawable.lakeside_view);
				// statusUpdate.setMedia("test.jpg", is);
				statusUpdate.setMedia(yourFile);

				twitter4j.Status response = twitter.updateStatus(statusUpdate);


				Log.d("Status", response.getText());

			} catch (TwitterException e) {
				Log.d("Failed to post!", e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			/* Dismiss the progress dialog after sharing */
			pDialog.dismiss();

			Toast.makeText(AccountSetupActivity.this, "Posted to Twitter!",
					Toast.LENGTH_SHORT).show();

		}

	}
		
}
