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
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_UID = "Uid";

        /*----------------------------------------------------------------------------------------*
         *  Create table                                                                          *
         *----------------------------------------------------------------------------------------*/
        static String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                                          + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT,"
                                          + COLUMN_EMAIL + " TEXT UNIQUE," + COLUMN_UID + " TEXT,"
                                          + COLUMN_CREATED_AT + " TEXT" + ")";

        /*----------------------------------------------------------------------------------------*
         *  Drop table                                                                            *
         *----------------------------------------------------------------------------------------*/
        static String DELETE_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
