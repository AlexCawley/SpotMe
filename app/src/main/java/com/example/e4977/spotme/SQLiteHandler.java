package com.example.e4977.spotme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SQLiteHandler extends SQLiteOpenHelper
{

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "SpotMe";

    public SQLiteHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(UserDBContract.User.CREATE_USER_TABLE);
        db.execSQL(ContactDBContract.Contact.CREATE_CONTACT_TABLE);

        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(UserDBContract.User.DELETE_USER_TABLE);
        db.execSQL(ContactDBContract.Contact.DELETE_CONTACT_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addUser(String name, String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(UserDBContract.User.COLUMN_NAME, name);
        values.put(UserDBContract.User.COLUMN_EMAIL, email);
        values.put(UserDBContract.User.COLUMN_PASSWORD, password);
        values.put(UserDBContract.User.COLUMN_CREATED_AT, getCurrentTimeString());

        long id = db.insert(UserDBContract.User.TABLE_NAME, null, values);

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void addContact(String name, String email, String phone)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContactDBContract.Contact.COLUMN_NAME, name);
        values.put(ContactDBContract.Contact.COLUMN_EMAIL, email);
        values.put(ContactDBContract.Contact.COLUMN_PHONE, phone);
        values.put(ContactDBContract.Contact.COLUMN_CREATED_AT, getCurrentTimeString());

        long id = db.insert(ContactDBContract.Contact.TABLE_NAME, null, values);

        Log.d(TAG, "New contact inserted into sqlite: " + id);
    }

    public ArrayList<Contact> getAllContacts()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Contact> contacts = new ArrayList<Contact>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + ContactDBContract.Contact.TABLE_NAME, null);
        if(cursor.moveToFirst())
        {
            do
            {
                String name = cursor.getString(1);
                String email = cursor.getString(2);
                String phone = cursor.getString(3);
                Contact contact = new Contact(name, email, phone);
                contacts.add(contact);
            } while (cursor.moveToNext());
        }
        return contacts;
    }

    public User authenticateUser(User userToFind)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection =
        {
            UserDBContract.User._ID,
            UserDBContract.User.COLUMN_NAME,
            UserDBContract.User.COLUMN_EMAIL,
            UserDBContract.User.COLUMN_PASSWORD
        };

        String selection = UserDBContract.User.COLUMN_EMAIL + " = ?";
        String[] selectionArgs =
        {
                userToFind.email
        };

        Cursor cursor = db.query(UserDBContract.User.TABLE_NAME,
                                 projection,
                                 selection,
                                 selectionArgs,
                                 null,
                                 null,
                                 null);

        if (cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();

            User userTocheck = new User(cursor.getString(0), cursor.getString(1),
                                  cursor.getString(2), cursor.getString(3));

            if (userToFind.password.equalsIgnoreCase(userTocheck.password))
            {
                Log.d(TAG, "Fetching user from Sqlite: " + userTocheck.toString());
                return userTocheck;
            }
        }

        Log.d(TAG, "User no found");

        return null;
    }

    public boolean emailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection =
                {
                        UserDBContract.User._ID,
                        UserDBContract.User.COLUMN_NAME,
                        UserDBContract.User.COLUMN_EMAIL,
                        UserDBContract.User.COLUMN_PASSWORD
                };

        String selection = UserDBContract.User.COLUMN_EMAIL + " = ?";
        String[] selectionArgs =
                {
                        email
                };

        Cursor cursor = db.query(UserDBContract.User.TABLE_NAME,
                                 projection,
                                 selection,
                                 selectionArgs,
                                 null,
                                 null,
                                 null);

        if (cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            return true;
        }

        return false;
    }

    public void deleteUsers()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UserDBContract.User.TABLE_NAME, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public void deleteContacts()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ContactDBContract.Contact.DELETE_CONTACT_TABLE, null, null);
        db.close();

        Log.d(TAG, "Deleted all contact info from sqlite");
    }

    private String getCurrentTimeString()
    {
        try {
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = simpleDateFormat.format(today);
            return dateString;
        }
        catch (Exception e) {
            Log.e(TAG, "Error", e);
            return "00/00/0000";
        }
    }

    public static boolean doesTableExist(SQLiteDatabase db, String tableName)
    {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);

        if (cursor != null)
        {
            if (cursor.getCount() > 0)
            {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

}
