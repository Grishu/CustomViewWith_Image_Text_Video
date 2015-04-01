package com.customview.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

import com.demo.customview.CustomViewVo;

public class DBParse {

	static Context m_context;

	public DBParse(Context con) {
		m_context = con;
	}

	/**
	 * Get all the viewslist from the database.
	 * 
	 * @return-Arraylist of contacts
	 */
	public ArrayList<CustomViewVo> getAllMotivationalList() {
		ArrayList<CustomViewVo> m_arryContVo = new ArrayList<CustomViewVo>();
		SQLiteDBProvider m_provider = DatabaseSingleton.getInstance(m_context);
		m_provider.openToRead();

		Cursor m_contCursor = m_provider.getAllMotivational();
		if (m_contCursor.getCount() > 0) {
			m_contCursor.moveToFirst();
			do {
				CustomViewVo m_conVo = new CustomViewVo();
				m_conVo.set_id(m_contCursor.getString(m_contCursor
						.getColumnIndex(SQLiteDBProvider.VIEW_ID)));
				m_conVo.setText(m_contCursor.getString(m_contCursor
						.getColumnIndex(SQLiteDBProvider.VIEW_CONTENT)));
				m_conVo.setType(m_contCursor.getString(m_contCursor
						.getColumnIndex(SQLiteDBProvider.VIEW_TYPE)));
				m_conVo.setFav(m_contCursor.getString(m_contCursor
						.getColumnIndex(SQLiteDBProvider.VIEW_FAV)));
				m_arryContVo.add(m_conVo);

			} while (m_contCursor.moveToNext());
			m_contCursor.close();
			m_provider.close();
		}
		return m_arryContVo;
	}
	
	public boolean updateSetViews(String id, String value){
		SQLiteDBProvider m_provider = DatabaseSingleton.getInstance(m_context);
		m_provider.openToWrite();
		return m_provider.updateViewsValue(id, value);
	}
	
	public boolean isSetViewAsFavorite(String id){
		SQLiteDBProvider m_provider = DatabaseSingleton.getInstance(m_context);
		m_provider.openToRead();
		return m_provider.isSetAsFavorite(id);
	}
	
	public CustomViewVo getFavoriteList(){
		SQLiteDBProvider m_provider = DatabaseSingleton.getInstance(m_context);
		m_provider.openToRead();
		return m_provider.getSetAsFavoriteList();
	}
	
	/**
	 * 
	 * Remove motivational from Database.
	 * 
	 * @return-Arraylist of contacts
	 */
	public boolean removeView(String p_ID) {
		SQLiteDBProvider m_provider = DatabaseSingleton.getInstance(m_context);
		m_provider.openToWrite();
		return m_provider.deleteRecord(p_ID);
	}
	
}
