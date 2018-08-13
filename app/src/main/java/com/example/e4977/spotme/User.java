package com.example.e4977.spotme;

public class User
{
    /*--------------------------------------------------------------------------------------------*
     *  Class variables                                                                           *
     *--------------------------------------------------------------------------------------------*/
    public String id;
    public String userName;
    public String email;
    public String createdAt;

    /*--------------------------------------------------------------------------------------------*
     *  Constructor                                                                               *
     *--------------------------------------------------------------------------------------------*/
    public User(String id, String userName, String email, String createdAt)
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        this.id = id;
        this.userName = userName;
        this.email = email;
        this.createdAt = createdAt;

        // Log method exit
        methodLogger.end();
    }

    /*--------------------------------------------------------------------------------------------*
     *  Getters and setters                                                                       *
     *--------------------------------------------------------------------------------------------*/
    public String getId()
    {
        return id;
    }
}
