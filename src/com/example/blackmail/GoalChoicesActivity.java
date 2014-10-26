package com.example.blackmail;

import java.util.Calendar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.internal.widget.AdapterViewCompat.OnItemSelectedListener;
import android.text.format.DateFormat;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

public class GoalChoicesActivity extends FragmentActivity implements OnItemSelectedListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goal_choices);
		
		//Make and populate the spinner that allows which platform to send a
		//blackmail message on (Facebook, Twitter, etc.)
		Spinner platformSpinner = (Spinner)findViewById(R.id.platform_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.platforms_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		platformSpinner.setAdapter(adapter);
	}

	@Override
	//This is for the platform chooser I think, check tomorrow
	public void onItemSelected(AdapterViewCompat<?> parent, View view, int pos,
			long id) {
        //Facebook = 1, Twitter=2, I think, not tested.
		parent.getItemAtPosition(pos);
		
	}

	@Override
	public void onNothingSelected(AdapterViewCompat<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	public void showTimePickerDialog(View v){
 		DialogFragment newFragment = new TimePickerFragment();
 		newFragment.show(getSupportFragmentManager(), "timePicker"+v.getId());
	}
	
	public void showDatePickerDialog(View v){
 		DialogFragment newFragment = new DatePickerFragment();
 		newFragment.show(getSupportFragmentManager(), "datePicker"+v.getId());
	}
	
	//How the start/end times of a goal are picked.
	public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
	        @Override
	        public Dialog onCreateDialog(Bundle savedInstanceState) {
	                //Current time is default value
	                final Calendar c = Calendar.getInstance();
	                int hour = c.get(Calendar.HOUR_OF_DAY);
	                int minute = c.get(Calendar.MINUTE);
	                return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
	        }
	
	        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
	                //Do something with the time that was chosen
	                
	        }
	}
	
	//How the start/end dates of a goal are picked.
	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
                //Current time is default value
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                
                return new DatePickerDialog(getActivity(), this, year, month, day);
        }

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			//Do something with the date that was chosen
			
		}
    }
	
	public void finishButtonClicked(View v){
		//We should check if all of the settings have been properly filled out
		//If they have been, then save all of the settings in SQLlite database,
		//and transfer to the view goals activity.
	}

	
	        
}

