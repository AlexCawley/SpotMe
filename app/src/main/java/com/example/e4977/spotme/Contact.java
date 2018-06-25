package com.example.e4977.spotme;

public class Contact
{
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public Contact()
    {
        super();
    }

    public Contact(String firstName)
    {
        this.firstName = firstName;
    }

    public Contact (String firstName,
                    String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public Contact (String firstName,
                    String lastName,
                    String email)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Contact (String firstName,
                    String lastName,
                    String email,
                    String phone)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }
}
