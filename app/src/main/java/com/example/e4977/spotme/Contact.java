package com.example.e4977.spotme;

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
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        this.name = name;
        this.email = email;
        this.phone = phone;

        // Log method exit
        methodLogger.end();
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
