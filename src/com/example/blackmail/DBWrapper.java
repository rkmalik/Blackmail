package com.example.blackmail;

import java.sql.Date;
import java.sql.Time;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.blackmail.DBObjects.API_Info_Facebook;
import com.example.blackmail.DBObjects.API_Info_Twitter;
import com.example.blackmail.DBObjects.API_Master;
import com.example.blackmail.DBObjects.Blackmail;
import com.example.blackmail.DBObjects.GPS_Location;
import com.example.blackmail.DBObjects.GoalSetupData;
import com.example.blackmail.DBObjects.Motivation;
import com.example.blackmail.DBObjects.Motivation_Schedule;
import com.example.blackmail.DBObjects.User_App_Data;

public class DBWrapper extends BlackmailDBHandler {

	public DBWrapper(Context context) {
		super(context);
	}

	public String APIName_Facebook = "Facebook API";
	public String APIName_Twitter = "Twitter API";

	/*
	 * MISCELLANEOUS
	 */

	public String ConvertDateToString(Date date) {
		return date.toString();
	}

	public Date ConvertDateStringToDate(String datestring) {
		return Date.valueOf(datestring);
	}

	public String ConvertTimeToString(Time time) {
		return time.toString();
	}

	public Time ConvertTimeStringToTime(String timestring) {
		return Time.valueOf(timestring);
	}

	public int ConvertBooleanToInt(boolean isactive) {
		if (isactive == true)
			return 1;
		else
			return 0;
	}

	public boolean ConvertIntToBoolean(int isactive) {
		if (isactive == 1)
			return true;
		else
			return false;
	}

	public int GetNextMotivationId() {
		String countQuery = "SELECT  * FROM " + TABLE_MOTIVATION;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		int records = cursor.getCount();
		if (records == 0)
			return 1;
		else
			return (records + 1);
	}

	public int GetAPITypeID(String APIName) {
		String query = "SELECT " + API_MASTER_KEY_APITYPEID + " FROM "
				+ TABLE_API_MASTER + " WHERE " + API_MASTER_KEY_APINAME + "="
				+ APIName + " and IsActive = 1";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		cursor.close();

		if (cursor != null)
			cursor.moveToFirst();

		int APITypeID = cursor.getInt(0);

		return APITypeID;
	}

	/*
	 * USER_APP_DATA_TABLE
	 */

	public void Insert_User_App_Data(User_App_Data data) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		data.AppVersion = 1;
		data.DBVersion = BlackmailDBHandler.DATABASE_VERSION;
		String createddate = ConvertDateToString(data.CreatedDate);
		int isactive = ConvertBooleanToInt(data.IsActive);

		values.put(USER_APP_DATA_KEY_USERNAME, data.Username);
		values.put(USER_APP_DATA_KEY_FIRSTNAME, data.Firstname);
		values.put(USER_APP_DATA_KEY_LASTNAME, data.LastName);
		values.put(USER_APP_DATA_KEY_CONTACTNO, data.ContactNo);
		values.put(USER_APP_DATA_KEY_APPVERSION, data.AppVersion);
		values.put(USER_APP_DATA_KEY_DBVERSION, data.DBVersion);
		values.put(USER_APP_DATA_KEY_CREATEDDATE, createddate);
		values.put(USER_APP_DATA_KEY_ISACTIVE, isactive);

		// Inserting Row
		db.insert(TABLE_USER_APP_DATA, null, values);
		db.close();
	}

	/*
	 * API_MASTER_TABLE
	 */

	public void Insert_API_Master_Data(API_Master data) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		String createddate = ConvertDateToString(data.CreatedDate);
		int isactive = ConvertBooleanToInt(data.IsActive);

		values.put(API_MASTER_KEY_APITYPEID, data.APITypeId);
		values.put(API_MASTER_KEY_APINAME, data.APIName);
		values.put(API_MASTER_KEY_APITABLENAME, data.APITableName);
		values.put(API_MASTER_KEY_ACCESSTOKEN, data.AccessToken);
		values.put(API_MASTER_KEY_ACCESSTOKENSECRET, data.AccessTokenSecret);
		values.put(API_MASTER_KEY_CREATEDDATE, createddate);
		values.put(API_MASTER_KEY_ISACTIVE, isactive);

		// Inserting Row
		db.insert(TABLE_API_MASTER, null, values);
		db.close();
	}

	/*
	 * API_INFO_FACEBOOK
	 */

	public void Insert_API_Info_Facebook(API_Info_Facebook data) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		String createddate = ConvertDateToString(data.CreatedDate);
		int isactive = ConvertBooleanToInt(data.IsActive);

		values.put(API_INFO_FACEBOOK_KEY_MOTIVATIONID, data.Motivation_Id);
		values.put(API_INFO_FACEBOOK_KEY_ACCESSTOKEN, data.AccessToken);
		values.put(API_INFO_FACEBOOK_KEY_STATUS, data.Status);
		values.put(API_INFO_FACEBOOK_KEY_IMAGEPATH, data.ImagePath);
		values.put(API_INFO_FACEBOOK_KEY_CREATEDDATE, createddate);
		values.put(API_INFO_FACEBOOK_KEY_ISACTIVE, isactive);

		// Inserting Row
		db.insert(TABLE_API_INFO_FACEBOOK, null, values);
		db.close();
	}

	/*
	 * API_INFO_TWITTER
	 */

	public void Insert_API_Info_Twitter(API_Info_Twitter data) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		String createddate = ConvertDateToString(data.CreatedDate);
		int isactive = ConvertBooleanToInt(data.IsActive);

		values.put(API_INFO_TWITTER_KEY_MOTIVATIONID, data.Motivation_Id);
		values.put(API_INFO_TWITTER_KEY_ACCESSTOKEN, data.AccessToken);
		values.put(API_INFO_TWITTER_KEY_ACCESSTOKENSECRET,
				data.AccessTokenSecret);
		values.put(API_INFO_TWITTER_KEY_STATUS, data.Status);
		values.put(API_INFO_TWITTER_KEY_IMAGEPATH, data.ImagePath);
		values.put(API_INFO_TWITTER_KEY_CREATEDDATE, createddate);
		values.put(API_INFO_TWITTER_KEY_ISACTIVE, isactive);

		// Inserting Row
		db.insert(TABLE_API_INFO_TWITTER, null, values);
		db.close();
	}

	/*
	 * MOTIVATION
	 */

	public void Insert_Motivation(Motivation data) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		String motivationstartdate = ConvertDateToString(data.MotivationStartDate);
		String motivationenddate = ConvertDateToString(data.MotivationEndDate);
		String motivationtime = ConvertTimeToString(data.MotivationTime);

		String createddate = ConvertDateToString(data.CreatedDate);
		int isactive = ConvertBooleanToInt(data.IsActive);

		values.put(MOTIVATION_KEY_MOTIVATIONID, data.Motivation_Id);
		values.put(MOTIVATION_KEY_MOTIVATIONTEXT, data.MotivationText);
		values.put(MOTIVATION_KEY_MOTIVATIONSTARTDATE, motivationstartdate);
		values.put(MOTIVATION_KEY_MOTIVATIONENDDATE, motivationenddate);
		values.put(MOTIVATION_KEY_MOTIVATIONTIME, motivationtime);
		// values.put(MOTIVATION_KEY_DURATION, data.Duration);
		values.put(MOTIVATION_KEY_NOOFOCCURRENCES, data.NoOfOccurrences);
		values.put(MOTIVATION_KEY_ISLOCKED, 1);
		values.put(MOTIVATION_KEY_ISCOMPLETED, 0);
		values.put(MOTIVATION_KEY_CREATEDDATE, createddate);
		values.put(MOTIVATION_KEY_ISACTIVE, isactive);

		// Inserting Row
		db.insert(TABLE_MOTIVATION, null, values);
		db.close();
	}

	/*
	 * BLACKMAIL
	 */

	public void Insert_Blackmail(Blackmail data) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		String createddate = ConvertDateToString(data.CreatedDate);
		int isactive = ConvertBooleanToInt(data.IsActive);

		values.put(BLACKMAIL_KEY_MOTIVATIONID, data.Motivation_Id);
		values.put(BLACKMAIL_KEY_APITYPEID, data.APITypeId);
		values.put(BLACKMAIL_KEY_BLACKMAILTEXT, data.BlackmailText);
		values.put(BLACKMAIL_KEY_BLACKMAILIMAGEPATH, data.BlackmailImagePath);
		values.put(BLACKMAIL_KEY_NOOFBACKOUTS, data.NoOfBackouts);
		values.put(BLACKMAIL_KEY_ISVALID, 1);
		values.put(BLACKMAIL_KEY_STATUS, "Pending");
		values.put(BLACKMAIL_KEY_CREATEDDATE, createddate);
		values.put(BLACKMAIL_KEY_ISACTIVE, isactive);

		// Inserting Row
		db.insert(TABLE_BLACKMAIL, null, values);
		db.close();
	}

	/*
	 * GPS_LOCATION
	 */

	public void Insert_GPS_Location(GPS_Location data) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		String createddate = ConvertDateToString(data.CreatedDate);
		int isactive = ConvertBooleanToInt(data.IsActive);
		int check = ConvertBooleanToInt(data.Check);

		values.put(GPS_LOCATION_KEY_MOTIVATIONID, data.Motivation_Id);
		values.put(GPS_LOCATION_KEY_LATITUDE, data.Latitude);
		values.put(GPS_LOCATION_KEY_LONGITUDE, data.Longitude);
		values.put(GPS_LOCATION_KEY_CHECK, check);
		values.put(GPS_LOCATION_KEY_CREATEDDATE, createddate);
		values.put(GPS_LOCATION_KEY_ISACTIVE, isactive);

		// Inserting Row
		db.insert(TABLE_GPS_LOCATION, null, values);
		db.close();
	}

	/*
	 * MOTIVATION_SCHEDULE
	 */

	public void Insert_Motivation_Schedule(Motivation_Schedule data) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		String fordate = ConvertDateToString(data.ForDate);
		String fortime = ConvertTimeToString(data.ForTime);
		String createddate = ConvertDateToString(data.CreatedDate);
		int isactive = ConvertBooleanToInt(data.IsActive);

		values.put(MOTIVATION_SCHEDULE_KEY_MOTIVATIONID, data.Motivation_Id);
		values.put(MOTIVATION_SCHEDULE_KEY_FORDATE, fordate);
		values.put(MOTIVATION_SCHEDULE_KEY_FORTIME, fortime);
		values.put(MOTIVATION_SCHEDULE_KEY_STATUS, "Unset");
		values.put(MOTIVATION_SCHEDULE_KEY_CREATEDDATE, createddate);
		values.put(MOTIVATION_SCHEDULE_KEY_ISACTIVE, isactive);

		// Inserting Row
		db.insert(TABLE_MOTIVATION_SCHEDULE, null, values);
		db.close();
	}

	/*
	 * GOAL SETUP
	 */
	public void Insert_Goal_Setup(GoalSetupData data) {

		int nextMotivationID = GetNextMotivationId();
		data.MotivationData.Motivation_Id = nextMotivationID;
		data.BlackmailData.Motivation_Id = nextMotivationID;
		data.GPSLocationData.Motivation_Id = nextMotivationID;

		Insert_Motivation(data.MotivationData);
		Insert_Blackmail(data.BlackmailData);
		Insert_GPS_Location(data.GPSLocationData);

	}
}
