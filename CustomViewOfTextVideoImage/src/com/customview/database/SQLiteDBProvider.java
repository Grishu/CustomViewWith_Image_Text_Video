package com.customview.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.demo.customview.CustomViewVo;

public class SQLiteDBProvider {
	// Database properties
	private static final String DATABASE_NAME = "MyCustomViewDB";
	private static String DB_PATH;
	private static final int DATABASE_VERSION = 1;

//	public static final String TABLE_MY_CONTACT = "mycontact";
//	public static final String TABLE_HIGH_RISK_LOCATION = "highrisklocation";

//	// My contact table fields
//	public static final String CONTACT_ID = "cont_id";
//	public static final String CONTACT_NAME = "contact_name";
//	public static final String CONTACT_HOME_PHONE = "contact_home_phone";
//	public static final String CONTACT_MOBILE_PHONE = "contact_mobile_phone";

	// High-risk location table fields
//	public static final String LOCATION_ID = "loc_id";
//	public static final String LOCATION_NAME = "location_name";
//	public static final String LOCATION_ADDRESS = "location_address";
//
//	private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
//	private static final String DROP_CONTACT_TABLE = DROP_TABLE
//			+ TABLE_MY_CONTACT;
//	private static final String DROP_LOCATION_TABLE = DROP_TABLE
//			+ TABLE_HIGH_RISK_LOCATION;

	// motivational
	public static final String TABLE_CUSTOMVIEWS = "CustomViewsTable";
	public static final String VIEW_ID = "view_id";
	public static final String VIEW_TYPE = "view_type";
	public static final String VIEW_CONTENT = "view_content";
	public static final String VIEW_FAV = "view_favourite";

	/** Holds the database instance. */
	static SQLiteDatabase m_database = null;

	/** Context of the application using the database. */
	Context m_context;

	/** Database open/upgrade helper. */
	DBHelperClass m_dbhelper = null;

//	public static final String QUERY_CREATE_CONATCT_TABLE = "create table "
//			+ TABLE_MY_CONTACT + "(" + CONTACT_ID
//			+ " integer primary key autoincrement, " + CONTACT_NAME + " text, "
//			+ CONTACT_HOME_PHONE + " text, " + CONTACT_MOBILE_PHONE + " text )";
//
//	public static final String QUERY_CREATE_HISTORY_LOCATION_TABLE = "create table "
//			+ TABLE_HIGH_RISK_LOCATION
//			+ "("
//			+ LOCATION_ID
//			+ " integer primary key autoincrement, "
//			+ LOCATION_NAME
//			+ " text, " + LOCATION_ADDRESS + " text )";
//
//	public static final String GET_ALL_CONTACT_DETAILS = "SELECT * from "
//			+ TABLE_MY_CONTACT;

//	public static final String GET_HISTORY_RISK_LOCATION_DETAILS = "SELECT * from "
//			+ TABLE_HIGH_RISK_LOCATION;

	public static final String QUERY_CREATE_VIEWS_TABLE = "create table "
			+ TABLE_CUSTOMVIEWS
			+ "("
			+ VIEW_ID
			+ " integer primary key autoincrement, "
			+ VIEW_TYPE
			+ " text, "
			+ VIEW_CONTENT
			+ " text, "
			+ VIEW_FAV
			+ " text )";

	public static final String GET_ALL_VIEWDETAILS = "SELECT * from "
			+ TABLE_CUSTOMVIEWS;

	public SQLiteDBProvider(Context p_context) {
		super();
		this.m_context = p_context;
		// DB_PATH = "/data/data/"
		// + p_context.getApplicationContext().getPackageName()
		// + "/databases/";
		m_dbhelper = new DBHelperClass(p_context, DATABASE_NAME, null,
				DATABASE_VERSION);
			System.out.println("Database Created.");

	}

	/**
	 * method to insert motivational Text, Image, Video
	 * 
	 * @param p_Type
	 *            -Type it will be Text, Image or Video
	 * 
	 * @param p_Content
	 *            -It will be URL if Image or Video from SDCARD or it may be
	 *            text if content
	 */
	public void insertMotivationalDetail(String p_Type, String p_Content) {
		try {
			openToWrite();
			m_database.beginTransaction();
			ContentValues m_ContctVal = new ContentValues();
			m_ContctVal.put(VIEW_TYPE, p_Type);
			m_ContctVal.put(VIEW_CONTENT, p_Content);
			m_ContctVal.put(VIEW_FAV, 0);
			m_database.insert(TABLE_CUSTOMVIEWS, null, m_ContctVal);
			m_database.setTransactionSuccessful();
			m_database.endTransaction();
				Log.v("SQLiteDatabase",
						"Motivational Values Inserted successfully");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * method to insert motivational Text, Image, Video
	 * 
	 * @param p_Type
	 *            -Type it will be Text, Image or Video
	 * 
	 * @param p_Content
	 *            -It will be URL if Image or Video from SDCARD or it may be
	 *            text if content
	 */
	public int updateMotivationalDetail(String m_where_args, String p_Content) {
		try {
			openToWrite();
			m_database.beginTransaction();
			ContentValues m_ContctVal = new ContentValues();
			m_ContctVal.put(VIEW_CONTENT, p_Content);

			ContentValues m_conVal1 = new ContentValues();
			m_conVal1.put(VIEW_CONTENT, p_Content);
			int m_rowCount = m_database.update(TABLE_CUSTOMVIEWS, m_conVal1,
					VIEW_ID + "=?", new String[] { m_where_args });

			m_database.setTransactionSuccessful();
			m_database.endTransaction();

				Log.v("SQLiteDatabase",
						"Values Inserted successfully");

			return m_rowCount;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Method to set as main Motivational Page
	 * 
	 * @param p_ID
	 *            -Motivational ID
	 *
	 */
	public boolean updateViewsValue(String p_ID, String value) {
		int m_rowCount = 0;
		openToWrite();
		String m_where_args[] = { p_ID };
		ContentValues m_conVal = new ContentValues();
		m_conVal.put(VIEW_FAV, "0");
		/**
		 * if any one is selected as favorite we will first unfavorite all
		 * because only one item set as main
		 */
		m_database.update(TABLE_CUSTOMVIEWS, m_conVal, null, null);

		ContentValues m_conVal1 = new ContentValues();
		m_conVal1.put(VIEW_FAV, value);
		m_rowCount = m_database.update(TABLE_CUSTOMVIEWS, m_conVal1,
				VIEW_ID + "=?", m_where_args);
		if (m_rowCount > 0)
			return true;
		else
			return false;
	}

	/**
	 * Method to set as main Motivational Page
	 * 
	 * @param p_ID
	 *            -Motivational ID in tht you want to update text
	 * 
	 * @param value
	 *            -Motivational content
	 *
	 */
	public boolean updateMotivationalText(String p_ID, String value) {
		int m_rowCount = 0;
		openToWrite();
		String m_where_args[] = { p_ID };
		ContentValues m_conVal = new ContentValues();
		m_conVal.put(VIEW_CONTENT, value);
		m_rowCount = m_database.update(TABLE_CUSTOMVIEWS, m_conVal,
				VIEW_ID + "=?", m_where_args);
		if (m_rowCount > 0)
			return true;
		else
			return false;
	}

	public boolean isSetAsFavorite(String p_ID) {

		CustomViewVo motivationalOb = new CustomViewVo();
		Cursor c_temp = m_database.rawQuery("select * FROM "
				+ TABLE_CUSTOMVIEWS + " WHERE " + VIEW_ID + "='"
				+ p_ID + "'", null);
		c_temp.moveToFirst();

		motivationalOb.set_id(c_temp.getString(c_temp
				.getColumnIndex(VIEW_CONTENT)));
		motivationalOb.setType(c_temp.getString(c_temp
				.getColumnIndex(VIEW_TYPE)));
		motivationalOb.setFav(c_temp.getString(c_temp
				.getColumnIndex(VIEW_FAV)));

		boolean flag = c_temp
				.getString(c_temp.getColumnIndex(VIEW_FAV)).equals("1");
		c_temp.close();
		return flag;
	}

	public CustomViewVo getSetAsFavoriteList() {

		Cursor c_temp = m_database.rawQuery("select * FROM "
				+ TABLE_CUSTOMVIEWS + " WHERE " + VIEW_FAV + "=?",
				new String[] { "1" });
		c_temp.moveToFirst();
		if (c_temp.getCount() > 0) {
			CustomViewVo motivationalOb = new CustomViewVo();
			motivationalOb.set_id(c_temp.getString(c_temp
					.getColumnIndex(VIEW_ID)));
			motivationalOb.setType(c_temp.getString(c_temp
					.getColumnIndex(VIEW_TYPE)));
			motivationalOb.setText(c_temp.getString(c_temp
					.getColumnIndex(VIEW_CONTENT)));
			motivationalOb.setFav(c_temp.getString(c_temp
					.getColumnIndex(VIEW_FAV)));
			c_temp.close();
			return motivationalOb;
		} else {
			return null;
		}
	}

	/**
	 * Method to remove Motivational Item
	 * 
	 * @param p_ID
	 *            - Which item you want to remove
	 * 
	 * @return
	 */

	public boolean deleteRecord(String p_ID) {

		boolean present = false;
		Cursor c_temp = m_database.rawQuery("DELETE FROM " + TABLE_CUSTOMVIEWS
				+ " WHERE " + VIEW_ID + "='" + p_ID + "'", null);
		c_temp.moveToFirst();

		int count = c_temp.getCount();
		if (count >= 1) {
			present = true;

		} else
			present = false;
		c_temp.close();
		return present;
	}

	/**
	 * method to insert data into the contacts table.
	 * 
	 * @param p_Name
	 *            -Contact name
	 * @param p_HPhone
	 *            -home phone number.
	 * @param p_MPhone
	 *            - mobile phone number.
	 */
//	public void insertContactDateValues(String p_Name, String p_HPhone,
//			String p_MPhone) {
//		try {
//			openToWrite();
//			m_database.beginTransaction();
//			ContentValues m_ContctVal = new ContentValues();
//			m_ContctVal.put(CONTACT_NAME, p_Name);
//			m_ContctVal.put(CONTACT_HOME_PHONE, p_HPhone);
//			m_ContctVal.put(CONTACT_MOBILE_PHONE, p_MPhone);
//
//			m_database.insert(TABLE_MY_CONTACT, null, m_ContctVal);
//			m_database.setTransactionSuccessful();
//			m_database.endTransaction();
//				Log.v("SQLiteDatabase", "Values Inserted successfully");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//	}

	/**
	 * Method to insert values in history risk location table
	 * 
	 * @param p_name
	 *            -Name of location
	 * @param p_Address
	 *            -An address of location.
	 */
//	public long insertHistoryLocationValues(String p_name, String p_Address) {
//		try {
//			openToWrite();
//			m_database.beginTransaction();
//			ContentValues m_historyValues = new ContentValues();
//			m_historyValues.put(LOCATION_NAME, p_name);
//			m_historyValues.put(LOCATION_ADDRESS, p_Address);
//			long _id = m_database.insert(TABLE_HIGH_RISK_LOCATION, null, m_historyValues);
//			m_database.setTransactionSuccessful();
//			m_database.endTransaction();
//			return _id;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return -1;
//		}
//	}

	/**
	 * Get all the contacts list from the database.
	 * 
	 * @return
	 */
//	public Cursor getAllContactDetails() {
//		Cursor m_cursor = null;
//		try {
//			openToWrite();
//			m_cursor = m_database.rawQuery(GET_ALL_CONTACT_DETAILS, null);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return m_cursor;
//	}

	/**
	 * Get all the Motivational list from the database.
	 * 
	 * @return
	 */
	public Cursor getAllMotivational() {
		Cursor m_cursor = null;
		try {
			openToWrite();
			m_cursor = m_database.rawQuery(GET_ALL_VIEWDETAILS, null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return m_cursor;
	}

	/**
	 * Get all the Histroy location details.
	 * 
	 * @return
	 */
//	public Cursor getAllHistoryLocationsDetails() {
//		Cursor m_hisCursor = null;
//		try {
//			openToWrite();
//			m_hisCursor = m_database.rawQuery(
//					GET_HISTORY_RISK_LOCATION_DETAILS, null);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return m_hisCursor;
//	}

	/**
	 * Method to update contact details in database.
	 * 
	 * @param p_ContID
	 *            -Contact ID
	 * @param p_Name
	 *            -Contact person name.
	 * @param p_HomePhone
	 *            -Contact home number
	 * @param p_Mob
	 *            -Contact mobile number.
	 * @return
	 */
//	public int UpdateContactDetails(String p_ContID, String p_Name,
//			String p_HomePhone, String p_Mob) {
//		int m_rowCount = 0;
//		openToWrite();
//		String m_where_args[] = { p_ContID };
//		ContentValues m_conVal = new ContentValues();
//		m_conVal.put(CONTACT_NAME, p_Name);
//		m_conVal.put(CONTACT_HOME_PHONE, p_HomePhone);
//		m_conVal.put(CONTACT_MOBILE_PHONE, p_Mob);
//		m_rowCount = m_database.update(TABLE_MY_CONTACT, m_conVal, CONTACT_ID
//				+ "=?", m_where_args);
//
//		return m_rowCount;
//	}

	/**
	 * Update history location details
	 * 
	 * @param p_hisId
	 *            -Location Id
	 * @param p_LocName
	 *            -Location name
	 * @return
	 */
//	public int UpdateHistoryLocationDetails(String p_hisId, String p_LocName) {
//		int m_rowCount = 0;
//		openToWrite();
//		String m_where_args[] = { p_hisId };
//		ContentValues m_LocVal = new ContentValues();
//		m_LocVal.put(LOCATION_NAME, p_LocName);
//		// m_conVal.put(LOCATION_ADDRESS, p_Mob);
//		m_rowCount = m_database.update(TABLE_HIGH_RISK_LOCATION, m_LocVal,
//				LOCATION_ID + "=?", m_where_args);
//
//		return m_rowCount;
//	}

//	public boolean deleteHighRiskLocation(int id_of_favorite_job) {
//
//		boolean present = false;
//		Cursor c_temp = m_database.rawQuery("DELETE FROM "
//				+ TABLE_HIGH_RISK_LOCATION + " WHERE " + LOCATION_ID + "='"
//				+ id_of_favorite_job + "'", null);
//		c_temp.moveToFirst();
//		int count = c_temp.getCount();
//		if (count >= 1) {
//			present = true;
//
//		} else
//			present = false;
//		c_temp.close();
//		return present;
//	}

	public void openToRead() {

		try {
			m_database = m_dbhelper.getReadableDatabase();
		} catch (SQLException e) {
		}
	}

	public void openToWrite() {
		try {
			m_database = m_dbhelper.getWritableDatabase();
		} catch (SQLException e) {
		}
	}

	public void close() {

		if (m_database != null) {
			m_database.close();
		}

		m_database = null;
	}

	public class DBHelperClass extends SQLiteOpenHelper {

		public DBHelperClass(Context context, String name,
				CursorFactory factory, int version) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			Log.v("DBHelperClass==", "Constructor called.");
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.v("SQLiteDBProvider==", "Database & Table Created");
//			db.execSQL(QUERY_CREATE_CONATCT_TABLE);
//			db.execSQL(QUERY_CREATE_HISTORY_LOCATION_TABLE);
			db.execSQL(QUERY_CREATE_VIEWS_TABLE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// delete all tables
			// create a new database
			onCreate(db);
		}

	}

}
