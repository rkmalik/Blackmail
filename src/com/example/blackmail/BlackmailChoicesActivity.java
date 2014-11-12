package com.example.blackmail;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Toast;

public class BlackmailChoicesActivity extends FragmentActivity {

	public static int FACEBOOK = 1;
	public static int TWITTER  = 2;
	
	public static int BM_TEXT  = 1;
	public static int BM_PHOTO = 2;
	
	private int plat;
	private int BMtype; 
	private NumberPicker numpicker;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blackmail_choices);
		
		//Set default platform choice as Facebook
		RadioButton platRadio = (RadioButton)findViewById(R.id.facebook_radio);
		platRadio.setChecked(true);
		plat = BlackmailChoicesActivity.FACEBOOK; 
		
		//Set default blackmail type as Text
		RadioButton bmRadio = (RadioButton)findViewById(R.id.textBM_radio);
		bmRadio.setChecked(true);
		BMtype = BlackmailChoicesActivity.BM_TEXT;
	
		//Set up the backount counter number picker
		numpicker = (NumberPicker) findViewById(R.id.backoutPicker);
		numpicker.setMinValue(0); //Lowest number of backouts is 0
		//numpicker.setMaxValue(n-1); //Max number is goal-1 PASSED IN INTENT
		

	}
	
	public void platformRadioClicked(View v){
		boolean isChecked = ((RadioButton) v).isChecked();
		switch(v.getId()){
			case R.id.facebook_radio:
				if(isChecked)
					plat = BlackmailChoicesActivity.FACEBOOK;
				break;
			case R.id.twitter_radio:
				if(isChecked)
					plat = BlackmailChoicesActivity.TWITTER;
				break;
			default:
		}
	}
	
	public void onBMTypeClicked(View v){
		boolean isChecked = ((RadioButton) v).isChecked();
		switch(v.getId()){
			case R.id.textBM_radio:
				if(isChecked)
					BMtype = BlackmailChoicesActivity.BM_TEXT;
				break;
			case R.id.pictureBM_radio:
				if(isChecked)
					BMtype = BlackmailChoicesActivity.BM_PHOTO;
				break;
			default:
		}
	}
	
	public void finishSetupClicked(View v){
		//Check to make sure an account exists for the chosen account
		//Check to make sure that the text/photo has been properly chosen
		
		
		// We could possibly add a prompt which shows specifically what was not filled in
		// as an aid to the user, I'm not going to do that right now though.
		Toast notSoFast = Toast.makeText(getApplicationContext(), (CharSequence)"Implement database checks.", Toast.LENGTH_SHORT);
	    notSoFast.show();
		
	}

}
