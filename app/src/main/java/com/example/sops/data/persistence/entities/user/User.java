package com.example.sops.data.persistence.entities.user;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Users")
public class User
{
    @PrimaryKey
    @NonNull
    private String id;
    private String userName;
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private boolean isEmployee;

    public User(@NonNull String id, String userName, String name, String surname, String phoneNumber, String email, boolean isEmployee)
    {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isEmployee = isEmployee;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSurname()
    {
        return surname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public boolean isEmployee()
    {
        return isEmployee;
    }

    public void setEmployee(boolean employee)
    {
        isEmployee = employee;
    }
}
