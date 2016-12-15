package com.mss.msschat.DataBase.Dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


import com.mss.msschat.AppUtils.AppPreferences;
import com.mss.msschat.DataBase.DatabaseHelper;
import com.mss.msschat.DataBase.Dto.Dto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * This interface defines the common methods it is data access object
 * 
 * 
 * @param <T>
 */
public abstract class Dao<T> {
	protected String table;

	protected Dao(String table) {
		this.table = table;
	}

	/**
	 * Inserts or replaces a DTO in the DB
	 */
	public void insertOrReplace(T obj, String field, String value, Context context) {
		Dto dto = (Dto) obj;
		// if (value != "-1") {
		if (exists(field, value,context)) {
			replaceDto(dto, " WHERE " + field + " = '" + value + "'",context);
		} else {
			insertDto(dto,context);
		}
		// } else {
		// insertDto(dto);
		// }
	}

	public void insertData(T obj, String field, String value, Context context) {
		Dto dto = (Dto) obj;
		// if (value != "-1") {
		insertDto(dto,context);
		// } else {
		// insertDto(dto);
		// }
	}



	private boolean exists(String field, String value, Context context) {
		boolean found = false;
		String sql = "SELECT * FROM " + table + " WHERE " + " " + field + " = '" + value + "';";
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sql, null);
		if (cursor.moveToFirst())
			found = true;
		dbHelper.close();
		return found;
	}


	/**
	 * Inserts of replaces a JSON representation of a DTO in the DB
	 * 
	 * @param json
	 * @throws JSONException
	 */
	public void insertOrReplace(JSONObject json, String field, String value, Context context) throws JSONException {
		T obj = buildDto(json);
		if (obj != null) {
			insertOrReplace(obj, field, value,context);
		} else {
			Log.e("table name", "Failed to create DTO from JSON " + json);
		}
	}

	/**
	 * This method is used to escape single quotes in SQLlite statements
	 * 
	 * @param input
	 * @return
	 */
	protected String escape(String input) {
		if (input == null) {
			return null;
		}
		return input.replace("'", "''");
	}

	@SuppressLint("NewApi")
	protected void insertDto(Dto dto,Context context) {
		List<String> columns = new ArrayList<String>();
		List<Object> values = new ArrayList<Object>();

		try {
			dto.prepareDbFields(columns, values);
			StringBuilder builder = new StringBuilder("INSERT INTO " + table + " (");
			for (int i = 0; i < columns.size(); i++) {
				if (i != 0) {
					builder.append(',');
				}
				builder.append(columns.get(i));
			}
			builder.append(") VALUES (");
			for (int i = 0; i < values.size(); i++) {
				if (i != 0) {
					builder.append(',');
				}
				builder.append(values.get(i));
			}
			builder.append(")");
			String sql = builder.toString();
			Log.v("SQL INSERT " + table, sql);
			DatabaseHelper dbHelper = new DatabaseHelper(context);
			dbHelper.setWriteAheadLoggingEnabled(true);
			dbHelper.getWritableDatabase().execSQL(sql);
			dbHelper.close();
		} catch (Exception e) {
			Log.e(table, "Failed to insert DTO", e);
			e.printStackTrace();
		}
	}

	protected void replaceDto(Dto dto, String where, Context context) {
		List<String> columns = new ArrayList<String>();
		List<Object> values = new ArrayList<Object>();

		try {
			dto.prepareDbFields(columns, values);
			StringBuilder builder = new StringBuilder("UPDATE " + table + " SET ");
			for (int i = 1; i < columns.size(); i++) {
				if (i != 1) {
					builder.append(',');
				}
				builder.append(columns.get(i));
				builder.append("=");
				builder.append(values.get(i));
			}
			// builder.append(" WHERE id = '" + dto.getId() + "'");
			builder.append(where);
			String sql = builder.toString();
			Log.v("SQL UPDATE " + table, sql);
			// DataBaseHelper.getDataBase().execSQL(sql);
			DatabaseHelper dbHelper = new DatabaseHelper(context);
			dbHelper.getWritableDatabase().execSQL(sql);
			dbHelper.close();
		} catch (Exception e) {
			Log.e(table, "Failed to update DTO", e);
			e.printStackTrace();
		}
	}

	public void deletebyId(String id, String field, Context context) {
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		dbHelper.getWritableDatabase().delete(table, field + " = '" + id + "'", null);
		dbHelper.close();

	}

	/**
	 * Returns all DTOs currently in the DB
	 */
	public List<T> listAll(Context context) {
		List<T> list = new ArrayList<T>();

		String sql = "SELECT * FROM " + table + ";";
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sql, null);
		while (cursor.moveToNext()) {
			try {
				T dto = buildDto(cursor);
				if (dto != null) {
					list.add(dto);
				}
			} catch (Exception e) {
				Log.e(sql, "field not found", e);
			}
		}
		cursor.close();
		dbHelper.close();
		return list;

	}

	/**
	 * Returns all DTOs currently in the DB
	 */
	public List<T> listDirty(String where) {
		List<T> list = new ArrayList<T>();
		// " WHERE " + field + " < 0;"
		String sql = "SELECT * FROM " + table + where;
		DatabaseHelper dbHelper = new DatabaseHelper(AppPreferences.mContext);
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sql, null);
		while (cursor.moveToNext()) {
			try {
				T dto = buildDto(cursor);
				if (dto != null) {
					list.add(dto);
					// System.out.println("Nazar Abbas Naqvi");
				}
			} catch (Exception e) {
				Log.e(sql, "field not found", e);
			}
		}
		cursor.close();
		dbHelper.close();
		return list;

	}

	public void clearTable(Context context) {
		// String sql = "DELETE * FROM " + table + ";";
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		dbHelper.getWritableDatabase().delete(table, null, null);
		dbHelper.close();
	}

	/**
	 * Inserts a new DTO in the DB
	 */
	abstract public void insert(T dto);

	/**
	 * Inserts a new Dirty DTO in the DB
	 */
	// abstract public void insertDirty(T dto);

	/**
	 * Replaces an existing DTO in the DB
	 */
	abstract public void replace(T dto);

	/**
	 * Deletes an existing record in the DB by id
	 */
	abstract public void delete(String id);

	/**
	 * This method builds the DTO object based on a JSON object (received from
	 * the server)
	 * 
	 * @throws JSONException
	 */
	abstract public T buildDto(JSONObject json) throws JSONException;

	/**
	 * This method builds the DTO object based on a database cursor.
	 */
	abstract protected T buildDto(Cursor cursor);

	/**
	 * This method builds the DTO object based on a database cursor.
	 */
	abstract protected List<T> listDirtyDto(String field);

}
