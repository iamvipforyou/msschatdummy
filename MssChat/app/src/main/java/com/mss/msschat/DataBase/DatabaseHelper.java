package com.mss.msschat.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.mss.msschat.AppUtils.Constants;

import java.util.LinkedHashMap;


public class DatabaseHelper extends SQLiteOpenHelper {

    private boolean locked;
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mssChat";
    private static StringBuilder CREATE_TABLE = new StringBuilder("CREATE TABLE ");

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static private DatabaseHelper instance_s;

    synchronized static public DatabaseHelper getInstance(Context context) {
        if (instance_s == null) {
            instance_s = new DatabaseHelper(context);
        }
        return instance_s;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db, TableHelper.getAllContacts(), Constants.DataBaseParms.CONTACTS_TABLE);

        createTable(db, TableHelper.getAllMessages(), Constants.DataBaseParms.MESSAGE_TABLE);

        createTable(db, TableHelper.getAllRecentMessages(), Constants.DataBaseParms.RECENT_CHAT_MESSAGES);
    }

    private void createTable(SQLiteDatabase db, LinkedHashMap<String, String> fields, String tableName) {
        CREATE_TABLE.append(tableName + "(");
        for (String key : fields.keySet()) {

            CREATE_TABLE.append(key);
            CREATE_TABLE.append(" " + fields.get(key) + ",");
        }
        CREATE_TABLE.setLength(CREATE_TABLE.length() - 1);
        CREATE_TABLE.append(")");
        db.execSQL(CREATE_TABLE.toString());
        CREATE_TABLE = new StringBuilder("CREATE TABLE ");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS create_group");
        onCreate(db);
        // db.execSQL("DROP TABLE IF EXISTS Events");
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
