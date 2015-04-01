package com.customview.database;

import android.content.Context;

public class DatabaseSingleton {
	private static SQLiteDBProvider m_DBinstance;;

	private DatabaseSingleton() {

	}

	public static SQLiteDBProvider getInstance(Context context) {
		if (m_DBinstance == null) {
				System.err.println("DatabaseSingleton Instance");
			m_DBinstance = new SQLiteDBProvider(context);
		}
		return m_DBinstance;
	}

}
