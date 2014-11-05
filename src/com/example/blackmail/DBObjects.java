package com.example.blackmail;

import java.util.*;
import java.sql.Time;

//Encapsulation for all DB data
public class DBObjects {

	//All user / application related data
	public class User_App_Data
	{
		String Username;
		String Firstname;
		String LastName;
		int ContactNo;
		int AppVersion;
		int DBVersion;
		
		Date CreatedDate;
		boolean IsActive;
		
		public User_App_Data()
		{}
	}	

	//Master table to map APIs to tables
	public class API_Master
	{
		int APITypeId;
		String APIName;
		String APITableName;
		
		Date CreatedDate;
		boolean IsActive;
		
		public API_Master()
		{}
	}
	
	//Facebook API
	public class API_Info_Facebook
	{
		int Motivation_Id;
		
		Date CreatedDate;
		boolean IsActive;
		
		public API_Info_Facebook()
		{}
	}
	
	//Twitter API
	public class API_Info_Twitter
	{
		int Motivation_Id;
		
		Date CreatedDate;
		boolean IsActive;
		
		public API_Info_Twitter()
		{}
	}
	
	//Motivation Data
	public class Motivation
	{
		int Motivation_Id;
		String MotivationText;
		Date MotivationStartDate;
		Date MotivationEndDate;
		int Duration;
		int NoOfOccurrences;
		Time MotivationTime;
		
		boolean IsLocked; //Locked once setup is completed by user.
		boolean IsCompleted; //Sets after specified motivation time passes.
		
		Date CreatedDate;
		boolean IsActive;
		
		public Motivation()
		{}
	}
	
	//Blackmail Data
	public class Blackmail
	{
		int Motivation_Id;
		int APITypeId;
		String BlackmailImagePath;
		String BlackmailText;
		int NoOfBackouts;
		
		boolean IsValid; //
		String Status; // Status of the overall Blackmail i.e. Success/Failure
		
		Date CreatedDate;
		boolean IsActive;
		
		public Blackmail()
		{}
	}
	
	//GPS Location for Motivation
	public class GPS_Location
	{
		int Motivation_Id;
		double Latitude;
		double Longitude;
		boolean Check; // True - check at location, False - check not at location
		
		Date CreatedDate;
		boolean IsActive;
		
		public GPS_Location()
		{}
	}
	
	//Alarm / Notification schedules for Motivations
	public class Motivation_Schedule
	{
		int Motivation_Id;
		Date ForDate;
		Time ForTime;
		
		String Status; // Individual alarm status i.e. Pending,Execute,Backout.
		
		Date CreatedDate;
		boolean IsActive;
		
		public Motivation_Schedule()
		{}
	}
}