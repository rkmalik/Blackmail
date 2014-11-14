package com.example.blackmail;

import java.io.File;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class AccountSetupActivity extends FragmentActivity implements OnClickListener  {

	protected SharedPreferences twitterAccessInfo; 
	
	protected View loginLayout;
	protected View postLayout;
	
	protected  Twitter twitter;
	protected  RequestToken requestToken;
	
	private static final String consumerKey = "WTchQTjvGFsbswbW6FTF8kFCL";
	private static final String consumerKeySecret = "Ii1K2xzyVQCABMjIsYK2v0D6Q3S7soHYu3mYAPfPDMNMYVQEtd";
	private String callback = "http://blackmail.android.app";
	
	public static final int webCode = 100;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_setup);

		//Probably this but may be needed
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	
		loginLayout = (RelativeLayout) findViewById(R.id.login_layout);
		postLayout = (LinearLayout) findViewById(R.id.post_layout);
		
		findViewById(R.id.loginTwitterButton).setOnClickListener(this);
		findViewById(R.id.updateTwitterButton).setOnClickListener(this);

		twitterAccessInfo = getSharedPreferences("twitterAccessInfo", 0);
		boolean loggedIn = twitterAccessInfo.getBoolean("loggedIn", false);
		
		if(loggedIn){
			loginLayout.setVisibility(View.GONE);
			postLayout.setVisibility(View.VISIBLE);
		}
	}
	
	protected void loginToTwitter(){
	
		boolean loggedIn = twitterAccessInfo.getBoolean("loggedIn", false);
	
		if(loggedIn){
			loginLayout.setVisibility(View.GONE);
			postLayout.setVisibility(View.VISIBLE);
		}
		else{
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(consumerKey);
			builder.setOAuthConsumerSecret(consumerKeySecret);

			Configuration configuration = builder.build();
			TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			try {
				requestToken = twitter.getOAuthRequestToken(callback);
				Intent intent = new Intent(this, WebViewActivity.class);
				intent.putExtra(WebViewActivity.requestURL, requestToken.getAuthenticationURL());
				startActivityForResult(intent, webCode);	
			} 
			catch(TwitterException e){
				e.printStackTrace();
			}
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			String verifier = data.getExtras().getString("oauth_verifier");
			try {
				AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
				
				Editor editor = twitterAccessInfo.edit();
				editor.putString("accessToken", accessToken.getToken());
				editor.putString("accessTokenSecret", accessToken.getTokenSecret());
				editor.putBoolean("loggedIn", true);
				editor.commit();
				
				loginLayout.setVisibility(View.GONE);
				postLayout.setVisibility(View.VISIBLE);
			} 
			catch(Exception e){
				Log.e("Twitter Login Failed", e.getMessage());
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	class updateTwitterStatus extends AsyncTask<String, String, Void> {

		protected Void doInBackground(String... args) {

			String status = args[0];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(consumerKey);
				builder.setOAuthConsumerSecret(consumerKeySecret);

				// Access Token - Instead of preferences get from db
				String accessToken = twitterAccessInfo.getString("accessToken", "");

				// Access Token Secret - Instead of preferences get from db
				String accessTokenSecret = twitterAccessInfo.getString("accessTokenSecret", "");

				AccessToken token = new AccessToken(accessToken, accessTokenSecret);
				Twitter twitter = new TwitterFactory(builder.build()).getInstance(token);
				
				// Update status
				StatusUpdate statusUpdate = new StatusUpdate(status);

				// Need string path of image you want to upload
				File yourFile = new File("/sdcard/Pictures/Messenger/reflection_mario.jpeg");				
				
				statusUpdate.setMedia(yourFile);
				twitter.updateStatus(statusUpdate);

			} catch (TwitterException e) {
				Log.d("No Twitter Post", e.getMessage());
			}
			return null;
		}
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.loginTwitterButton:
				loginToTwitter();
				break;
			case R.id.updateTwitterButton:
				new updateTwitterStatus().execute("Anything");
				break;
		}
	}
}