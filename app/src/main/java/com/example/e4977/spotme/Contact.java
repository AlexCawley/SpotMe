package com.example.e4977.spotme;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable
{
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeString(firstName);
        out.writeString(lastName);
        out.writeString(email);

        out.writeString(phone);
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>()
    {
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return  new Contact[size];
        }
    };

    private Contact (Parcel in)
    {
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        phone = in.readString();
    }
}
