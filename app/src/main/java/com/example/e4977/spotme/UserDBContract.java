package com.example.e4977.spotme;

import android.provider.BaseColumns;

public final class UserDBContract
{

    /*--------------------------------------------------------------------------------------------*
     *  Constructor                                                                               *
     *--------------------------------------------------------------------------------------------*/
    private UserDBContract()
    {
    }

    public static class User implements BaseColumns
    {
        /*----------------------------------------------------------------------------------------*
         *  Constants                                                                             *
         *----------------------------------------------------------------------------------------*/
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_CREATED_AT = "created_at";

        /*----------------------------------------------------------------------------------------*
         *  Create table                                                                          *
         *----------------------------------------------------------------------------------------*/
        static String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                                          + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                          + COLUMN_NAME + " TEXT, " + COLUMN_EMAIL + " TEXT UNIQUE, "
                                          + COLUMN_PASSWORD + " TEXT, " + COLUMN_CREATED_AT + " INTEGER)";

        /*----------------------------------------------------------------------------------------*
         *  Drop table                                                                            *
         *----------------------------------------------------------------------------------------*/
        static String DELETE_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
