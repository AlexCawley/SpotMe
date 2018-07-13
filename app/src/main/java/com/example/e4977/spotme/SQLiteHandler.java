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
    /*----------------------------------------------------------------------------------------*
     *  Constants                                                                             *
     *----------------------------------------------------------------------------------------*/
    private static final String TAG = SQLiteHandler.class.getSimpleName();
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "SpotMe";

    /*--------------------------------------------------------------------------------------------*
     *  Constructor                                                                               *
     *--------------------------------------------------------------------------------------------*/
    public SQLiteHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*--------------------------------------------------------------------------------------------*
     *  OnCreate                                                                                  *
     *--------------------------------------------------------------------------------------------*/
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Create tables
        db.execSQL(UserDBContract.User.CREATE_USER_TABLE);
        db.execSQL(ContactDBContract.Contact.CREATE_CONTACT_TABLE);

        Log.d(TAG, "Database tables created");
    }

    /*--------------------------------------------------------------------------------------------*
     *  OnUpgrade                                                                                 *
     *--------------------------------------------------------------------------------------------*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop tables
        db.execSQL(UserDBContract.User.DELETE_USER_TABLE);
        db.execSQL(ContactDBContract.Contact.DELETE_CONTACT_TABLE);

        // Create tables
        onCreate(db);
    }

    /*--------------------------------------------------------------------------------------------*
     *  OnDowngrade                                                                               *
     *--------------------------------------------------------------------------------------------*/
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop, then create tables
        onUpgrade(db, oldVersion, newVersion);
    }

    /*--------------------------------------------------------------------------------------------*
     *  AddUser                                                                                   *
     *--------------------------------------------------------------------------------------------*
     *  Adds a user to the database                                                               *
     *--------------------------------------------------------------------------------------------*/
    public void addUser(String name, String email, String password)
    {
        // Get a writable version of the database
        SQLiteDatabase db = this.getWritableDatabase();

        // Initialize values to write to
        ContentValues values = new ContentValues();

        // Add the name email password and date to the values
        values.put(UserDBContract.User.COLUMN_NAME, name);
        values.put(UserDBContract.User.COLUMN_EMAIL, email);
        values.put(UserDBContract.User.COLUMN_PASSWORD, password);
        values.put(UserDBContract.User.COLUMN_CREATED_AT, getCurrentTimeString());

        // Insert the user into the database
        long id = db.insert(UserDBContract.User.TABLE_NAME, null, values);

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /*--------------------------------------------------------------------------------------------*
     *  AddContact                                                                                *
     *--------------------------------------------------------------------------------------------*
     *  Adds a contact to the database                                                            *
     *--------------------------------------------------------------------------------------------*/
    public void addContact(String name, String email, String phone)
    {
        // Get a writeable version of the databse
        SQLiteDatabase db = this.getWritableDatabase();

        // Initialize a values object to write to
        ContentValues values = new ContentValues();

        // Add the name email phone and time to the values
        values.put(ContactDBContract.Contact.COLUMN_NAME, name);
        values.put(ContactDBContract.Contact.COLUMN_EMAIL, email);
        values.put(ContactDBContract.Contact.COLUMN_PHONE, phone);
        values.put(ContactDBContract.Contact.COLUMN_CREATED_AT, getCurrentTimeString());

        // Insert the new contact
        long id = db.insert(ContactDBContract.Contact.TABLE_NAME, null, values);

        Log.d(TAG, "New contact inserted into sqlite: " + id);
    }

    /*--------------------------------------------------------------------------------------------*
     *  GetAllContacts                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Returns an ArrayList of all contacts in the database                                      *
     *--------------------------------------------------------------------------------------------*/
    public ArrayList<Contact> getAllContacts()
    {
        // Get a readable database
        SQLiteDatabase db = this.getReadableDatabase();

        // Initialize an array list to store the contacts found
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        // SELECT * FROM contact
        Cursor cursor = db.rawQuery("SELECT * FROM " + ContactDBContract.Contact.TABLE_NAME, null);

        /*----------------------------------------------------------------------------------------*
         *  If the cursor has values                                                              *
         *----------------------------------------------------------------------------------------*/
        if (cursor.moveToFirst())
        {
            /*------------------------------------------------------------------------------------*
             *  while cursor can move to the next entry                                           *
             *------------------------------------------------------------------------------------*/
            do
            {
                // Get the name email and phone that was found
                String name = cursor.getString(1);
                String email = cursor.getString(2);
                String phone = cursor.getString(3);

                // Create a contact with those fields
                Contact contact = new Contact(name, email, phone);

                // Add the new contact to the contacts array
                contacts.add(contact);
            }
            while (cursor.moveToNext());
        }

        // Return the array of contacts created
        return contacts;
    }

    /*--------------------------------------------------------------------------------------------*
     *  AuthenticateUser                                                                          *
     *--------------------------------------------------------------------------------------------*
     *  Make sure the user passed exists in the database                                          *
     *--------------------------------------------------------------------------------------------*/
    public User authenticateUser(User userToFind)
    {
        // Get a readable database
        SQLiteDatabase db = this.getReadableDatabase();

        // Initialize projection of the columns that will be used
        String[] projection =
                {
                        UserDBContract.User._ID,
                        UserDBContract.User.COLUMN_NAME,
                        UserDBContract.User.COLUMN_EMAIL,
                        UserDBContract.User.COLUMN_PASSWORD
                };

        //  Create the WHERE statement to query with the email
        String selection = UserDBContract.User.COLUMN_EMAIL + " = ?";
        String[] selectionArgs =
                {
                        userToFind.email
                };

        // Get all of the Users with a matching email
        Cursor cursor = db.query(UserDBContract.User.TABLE_NAME,
                                 projection,
                                 selection,
                                 selectionArgs,
                                 null,
                                 null,
                                 null);

        /*----------------------------------------------------------------------------------------*
         *  If the cursor is not null and the cursor contains at least one entry                  *
         *----------------------------------------------------------------------------------------*/
        if (cursor != null && cursor.getCount() > 0)
        {
            // Move the user to the first entry
            cursor.moveToFirst();

            // Create a user with the databse entries found
            User userTocheck = new User(cursor.getString(0), cursor.getString(1),
                                        cursor.getString(2), cursor.getString(3));

            /*------------------------------------------------------------------------------------*
             *  If the user created mathes the password of the user passed                        *
             *------------------------------------------------------------------------------------*/
            if (userToFind.password.equalsIgnoreCase(userTocheck.password))
            {
                Log.d(TAG, "Fetching user from Sqlite: " + userTocheck.toString());

                // Return the user found
                return userTocheck;
            }
        }

        Log.d(TAG, "User no found");

        // return null
        return null;
    }

    /*--------------------------------------------------------------------------------------------*
     *  EmailExists                                                                               *
     *--------------------------------------------------------------------------------------------*
     *  Checks the database to see if the email passed already exists in                          *
     *--------------------------------------------------------------------------------------------*/
    public boolean emailExists(String email)
    {
        // Get a readable database
        SQLiteDatabase db = this.getReadableDatabase();

        // Initialize a projection of the columns that will be used
        String[] projection =
                {
                        UserDBContract.User._ID,
                        UserDBContract.User.COLUMN_NAME,
                        UserDBContract.User.COLUMN_EMAIL,
                        UserDBContract.User.COLUMN_PASSWORD
                };

        // Make the WHERE statement to query for an email
        String selection = UserDBContract.User.COLUMN_EMAIL + " = ?";
        String[] selectionArgs =
                {
                        email
                };

        // Send the query to the table
        Cursor cursor = db.query(UserDBContract.User.TABLE_NAME,
                                 projection,
                                 selection,
                                 selectionArgs,
                                 null,
                                 null,
                                 null);

        /*----------------------------------------------------------------------------------------*
         *  If the cursor is not null and it found at least one entry                             *
         *----------------------------------------------------------------------------------------*/
        if (cursor != null && cursor.getCount() > 0)
        {
            // Move the cursor to the first entry
            cursor.moveToFirst();

            // return true because the email was found
            return true;
        }

        // Return false
        return false;
    }

    /*--------------------------------------------------------------------------------------------*
     *  DeleteUsers                                                                               *
     *--------------------------------------------------------------------------------------------*
     *  Removes all users from the database                                                       *
     *--------------------------------------------------------------------------------------------*/
    public void deleteUsers()
    {
        // Get a writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete the users and close the database
        db.delete(UserDBContract.User.TABLE_NAME, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    /*--------------------------------------------------------------------------------------------*
     *  DeleteContacts                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Removes all contacts from the database                                                    *
     *--------------------------------------------------------------------------------------------*/
    public void deleteContacts()
    {
        // Get a writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete the contacts and close the database
        db.delete(ContactDBContract.Contact.DELETE_CONTACT_TABLE, null, null);
        db.close();

        Log.d(TAG, "Deleted all contact info from sqlite");
    }

    /*--------------------------------------------------------------------------------------------*
     *  GetCurrentTimeString                                                                      *
     *--------------------------------------------------------------------------------------------*
     *  Returns the current time of the device in dd/MM/yyy format                                *
     *--------------------------------------------------------------------------------------------*/
    private String getCurrentTimeString()
    {
        /*----------------------------------------------------------------------------------------*
         *  Try to get the date                                                                   *
         *----------------------------------------------------------------------------------------*/
        try
        {
            // Get the current time
            Date today = Calendar.getInstance().getTime();

            // Create a new format and apply it
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = simpleDateFormat.format(today);

            // Return the date found
            return dateString;
        }

        /*----------------------------------------------------------------------------------------*
         *  Catch any exceptions                                                                  *
         *----------------------------------------------------------------------------------------*/
        catch (Exception e)
        {
            // Return a new date set to be 00/00/0000
            Log.e(TAG, "Error", e);
            return "00/00/0000";
        }
    }

    /*--------------------------------------------------------------------------------------------*
     *  DoesTableExist                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Checks the given database for the given table name                                        *
     *--------------------------------------------------------------------------------------------*/
    public static boolean doesTableExist(SQLiteDatabase db, String tableName)
    {
        // query for the database
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);

        /*----------------------------------------------------------------------------------------*
         *  If the cursor is not null                                                             *
         *----------------------------------------------------------------------------------------*/
        if (cursor != null)
        {
            /*------------------------------------------------------------------------------------*
             *  If the cursor has at least one entry                                              *
             *------------------------------------------------------------------------------------*/
            if (cursor.getCount() > 0)
            {
                // Close the cursor
                cursor.close();

                // Return true because the table was found
                return true;
            }

            // Close the cursor
            cursor.close();
        }

        // Return false because the table was not found
        return false;
    }
}
