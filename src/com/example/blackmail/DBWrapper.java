package com.example.blackmail;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.blackmail.DBObjects.User_App_Data;

public class DBWrapper extends BlackmailDBHandler {

	public DBWrapper(Context context) {
		super(context);
	}

	/*
	 * USER_APP_DATA_TABLE
	 */

	public void Insert_User_App_Data(User_App_Data data) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(USER_APP_DATA_KEY_USERNAME, data.Username);
		values.put(USER_APP_DATA_KEY_FIRSTNAME, data.Firstname);
		values.put(USER_APP_DATA_KEY_LASTNAME, data.LastName);
		values.put(USER_APP_DATA_KEY_CONTACTNO, data.ContactNo);
		values.put(USER_APP_DATA_KEY_APPVERSION, data.AppVersion);
		values.put(USER_APP_DATA_KEY_DBVERSION, data.DBVersion);
		values.put(USER_APP_DATA_KEY_DBVERSION, data.DBVersion);
		values.put(USER_APP_DATA_KEY_DBVERSION, data.DBVersion);

		// Inserting Row
		db.insert(TABLE_USER_APP_DATA, null, values);
		db.close();
	}
}
