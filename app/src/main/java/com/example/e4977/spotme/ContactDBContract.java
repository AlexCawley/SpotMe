package com.example.e4977.spotme;

import android.provider.BaseColumns;

public final class ContactDBContract
{

    private ContactDBContract()
    {
    }

    public static class Contact implements BaseColumns
    {
        public static final String TABLE_NAME = "contact";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_CREATED_AT = "created_at";

        static String CREATE_CONTACT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                                             + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                             + COLUMN_NAME + " TEXT, "
                                             + COLUMN_EMAIL + " TEXT, "
                                             + COLUMN_PHONE + " TEXT, "
                                             + COLUMN_CREATED_AT + " INTEGER)";
        static String DELETE_CONTACT_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
