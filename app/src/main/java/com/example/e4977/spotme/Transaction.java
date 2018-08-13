package com.example.e4977.spotme;

import java.util.Date;

public class Transaction
{

    /*--------------------------------------------------------------------------------------------*
     *  Class variables                                                                           *
     *--------------------------------------------------------------------------------------------*/
    private String description;
    private float amount;
    private Date date;
    private User user;
    private Contact contact;
    private Boolean userPaying;

    /*--------------------------------------------------------------------------------------------*
     *  Constructor                                                                               *
     *--------------------------------------------------------------------------------------------*/
    public Transaction(String description, float amount, Date date, User user, Contact contact, Boolean userPaying)
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        this.description = description;
        this.amount = amount;
        this.date = date;
        this.user = user;
        this.contact = contact;
        this.userPaying = userPaying;

        // Log method exit
        methodLogger.end();
    }

    /*--------------------------------------------------------------------------------------------*
     *  Getters and setters                                                                       *
     *--------------------------------------------------------------------------------------------*/
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public float getAmount()
    {
        return amount;
    }

    public void setAmount(float amount)
    {
        this.amount = amount;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Contact getContact()
    {
        return contact;
    }

    public void setContact(Contact contact)
    {
        this.contact = contact;
    }

    public Boolean getUserPaying()
    {
        return userPaying;
    }

    public void setUserPaying(Boolean userPaying)
    {
        this.userPaying = userPaying;
    }
}
