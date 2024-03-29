package com.example.blackmail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//We could possibly combine the two groups of instructions into one,
		//but for further modification keeping it like this will be easier
		View showGoalsButton = findViewById(R.id.showGoalsButton);
		View createGoalButton = findViewById(R.id.createGoalButton);
		View accountSetupButton = findViewById(R.id.accountSetupButton);
		View aboutButton = findViewById(R.id.aboutButton);
		
		//Listen for each button to be pressed
		showGoalsButton.setOnClickListener(this);
		createGoalButton.setOnClickListener(this);
		accountSetupButton.setOnClickListener(this);
		aboutButton.setOnClickListener(this);
		
		
	}

	@Override
	//When any button is clicked, this method is called. We have to
	//identify which button was clicked, then we can perform the corresponding
	//action.
	public void onClick(View v) {
		Intent create;
		switch(v.getId()){
			case R.id.createGoalButton:
				create = new Intent(this, GoalChoicesActivity.class);
				startActivity(create);
				break;
			case R.id.accountSetupButton:
				create = new Intent(this, AccountSetupActivity.class);
				startActivity(create);
				break;
			case R.id.aboutButton:
				create = new Intent(this, AboutActivity.class);
				startActivity(create);
				break;
			default:
				Toast toast = Toast.makeText(getApplicationContext(), (CharSequence)"Not implemented yet", Toast.LENGTH_SHORT);
				toast.show();
				break;		
		}
		
	}
	        
}
