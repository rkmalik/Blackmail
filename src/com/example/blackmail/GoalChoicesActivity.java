package com.example.blackmail;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.internal.widget.AdapterViewCompat.OnItemSelectedListener;
import android.text.format.DateFormat;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class GoalChoicesActivity extends FragmentActivity {
	
	private EditText nameBox;
	private TimePicker startTime;
	private TimePicker endTime;
	private DatePicker startDate;
	private DatePicker endDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goal_choices);
		
		nameBox = (EditText)findViewById(R.id.goal_name_box);
		startTime = (TimePicker)findViewById(R.id.startTimePick);
		endTime = (TimePicker)findViewById(R.id.endTimePick);
		startDate = (DatePicker)findViewById(R.id.startDatePick);
		endDate = (DatePicker)findViewById(R.id.endDatePick);
		
		//Used for setting and comparing dates
		Calendar cal = Calendar.getInstance();
		startDate.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		cal.add(Calendar.HOUR_OF_DAY, 1);
		endTime.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
		endTime.setCurrentMinute(cal.get(Calendar.MINUTE));
		cal.add(Calendar.HOUR_OF_DAY, -1);
		cal.add(Calendar.MONTH, 1);
		endDate.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		
		
	}
	
	public void continueButtonClicked(View v){
		//Compare the two times
		int startHour = startTime.getCurrentHour();
		int startMinute = startTime.getCurrentMinute();
		int endHour = endTime.getCurrentHour();
		int endMinute = endTime.getCurrentMinute();
		boolean timeIsGood = false;
		boolean dateIsGood = false;
		boolean nameIsGood = false;
		
		nameIsGood = !nameBox.getText().toString().equals("");
		
		//Does the time range to check make sense? (Is the start actually before the end?)
		//Note: Hour is in 24 hour format, so no worries about AM/PM
		timeIsGood = (startHour < endHour || (startHour == endHour && startMinute < endMinute));
		
		//Does the date range make sense?
		Calendar start = Calendar.getInstance();
		start.set(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth());
		Calendar end = Calendar.getInstance();
		end.set(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth());
		
		int dateCompare = start.compareTo(end);
		dateIsGood = ((dateCompare == 0 && timeIsGood) || dateCompare < 0);
		
		//party on Wayne, party on Garth
		if(nameIsGood && dateIsGood && timeIsGood){
			Intent goToMap = new Intent(getApplicationContext(), GoalMapActivity.class);
			//We need to implement Serializable or Parceable for our DBOs
			//Time can be passed as two ints
			//Date can be passed as a String
			//Name can be passed as a String
			//goToMap.putExtra();
			startActivity(goToMap);
		}
		else {
			//Make the appropriate toast
			String errorString = "Can't continue because:\n";
			if(!nameIsGood) errorString += "You forgot to name your goal!\n";
			if(!dateIsGood) errorString += "Goal start date is not before goal end date.\n";
			if(!timeIsGood) errorString += "Time to begin checking is not before time to end checking.\n";
			Toast notSoFast = Toast.makeText(getApplicationContext(), (CharSequence)errorString, Toast.LENGTH_LONG);
		    notSoFast.show();
		}
	}

	
	        
}

