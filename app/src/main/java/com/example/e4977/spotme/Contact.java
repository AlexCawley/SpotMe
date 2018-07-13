package com.example.e4977.spotme;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact
{
    /*--------------------------------------------------------------------------------------------*
     *  Class variables                                                                           *
     *--------------------------------------------------------------------------------------------*/
    private String name;
    private String email;
    private String phone;

    /*--------------------------------------------------------------------------------------------*
     *  Constructor                                                                               *
     *--------------------------------------------------------------------------------------------*/
    public Contact(String name,
                   String email,
                   String phone)
    {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    /*--------------------------------------------------------------------------------------------*
     *  Getters and setters                                                                       *
     *--------------------------------------------------------------------------------------------*/
    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPhone()
    {
        return phone;
    }
}
