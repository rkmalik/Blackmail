package com.example.blackmail;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Toast;

public class BlackmailChoicesActivity extends FragmentActivity {

	public static int FACEBOOK = 1;
	public static int TWITTER = 2;

	public static int BM_TEXT = 1;
	public static int BM_PHOTO = 2;
	private String pathToPicture;
	private boolean photoFromCamera; // False == Import, True == Camera
	private Bitmap takenPicture;

	private int plat;
	private int BMtype;
	private NumberPicker numpicker;
	private Button importPicButton;
	private Button takePicButton;
	private EditText messageBox;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blackmail_choices);

		pathToPicture = "";

		// Set default platform choice as Facebook
		RadioButton platRadio = (RadioButton) findViewById(R.id.facebook_radio);
		platRadio.setChecked(true);
		plat = BlackmailChoicesActivity.FACEBOOK;

		// Set default blackmail type as Text
		RadioButton bmRadio = (RadioButton) findViewById(R.id.textBM_radio);
		importPicButton = (Button) findViewById(R.id.takePictureButton);
		takePicButton = (Button) findViewById(R.id.importPictureButton);
		messageBox = (EditText) findViewById(R.id.messageBox);

		bmRadio.setChecked(true);
		BMtype = BlackmailChoicesActivity.BM_TEXT;

		// Hide the picture buttons
		importPicButton.setVisibility(View.GONE);
		takePicButton.setVisibility(View.GONE);

		// Set up the backount counter number picker
		numpicker = (NumberPicker) findViewById(R.id.backoutPicker);
		numpicker.setMinValue(0); // Lowest number of backouts is 0
		numpicker.setMaxValue(getIntent().getExtras().getBundle("goalBundle").getInt("numOcc") - 1);
	}

	public void platformRadioClicked(View v) {
		boolean isChecked = ((RadioButton) v).isChecked();
		switch (v.getId()) {
		case R.id.facebook_radio:
			if (isChecked)
				plat = BlackmailChoicesActivity.FACEBOOK;
			break;
		case R.id.twitter_radio:
			if (isChecked)
				plat = BlackmailChoicesActivity.TWITTER;
			break;
		default:
		}
	}

	public void onBMTypeClicked(View v) {
		boolean isChecked = ((RadioButton) v).isChecked();
		switch (v.getId()) {
			case R.id.textBM_radio:
				if (isChecked) {
					BMtype = BlackmailChoicesActivity.BM_TEXT;
					// Hide the photo button(s)
					importPicButton.setVisibility(View.GONE);
					takePicButton.setVisibility(View.GONE);
					// Show the text box
					messageBox.setVisibility(View.VISIBLE);
	
				}
				break;
			case R.id.pictureBM_radio:
				if (isChecked) {
					BMtype = BlackmailChoicesActivity.BM_PHOTO;
					// Hide the text box
					messageBox.setVisibility(View.GONE);
					// Show the photo button(s)
					importPicButton.setVisibility(View.VISIBLE);
					takePicButton.setVisibility(View.VISIBLE);
				}
				break;
			default:
		}
	}

	public void takePicture(View v) {
		Intent inten = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(inten, 1);
	}

	public void importPicture(View v) {
		Intent inten = new Intent(Intent.ACTION_PICK,
				                  android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(inten, 2);
	}

	// Return from camera or gallery intent calls
	protected void OnActivityResult(int rqCode, int resCode, Intent imageData) {
		super.onActivityResult(rqCode, resCode, imageData);

		// CAMERA - gets the image data, we could also save to a file if that
		// was required
		if (rqCode == 1 && resCode == Activity.RESULT_OK && imageData != null) {
			takenPicture = (Bitmap) imageData.getExtras().get("imageData");
			// can set an image view to the bitmap of photo, but don't care for
			// now
			photoFromCamera = true; // Set that the image is from the camera
		}

		// GALLERY - Gets the selected image's file path, from there we can get
		// the data.
		else if (rqCode == 2 && resCode == Activity.RESULT_OK
				&& imageData != null) {
			Uri picture = imageData.getData();
			String[] filePath = { MediaStore.Images.Media.DATA };
			Cursor cs = getContentResolver().query(picture, filePath, null, null, null);
			cs.moveToFirst();
			int column = cs.getColumnIndex(filePath[0]);
			pathToPicture = cs.getString(column); // Voila
			cs.close();
			photoFromCamera = false; // Not from camera, we are importing it.
		}

	}

	public void finishSetupClicked(View v) {
		// Check to make sure an account exists for the chosen account
		// Check to make sure that the text/photo has been properly chosen

		// We could possibly add a prompt which shows specifically what was not
		// filled in
		// as an aid to the user, I'm not going to do that right now though.
		Toast notSoFast = Toast
				.makeText(getApplicationContext(),
						(CharSequence) "Implement database checks.",
						Toast.LENGTH_SHORT);
		notSoFast.show();

		// Unpackage the bundles to make the DBOS
		Bundle extras = getIntent().getExtras();
		Bundle goalBundle = extras.getBundle("goalBundle");
		Bundle mapBundle = extras.getBundle("mapBundle");

	}

}
