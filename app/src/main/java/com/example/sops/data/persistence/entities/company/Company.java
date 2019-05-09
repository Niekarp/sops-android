package com.example.sops.data.persistence.entities.company;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Companies")
public class Company
{
    @PrimaryKey
    private int id;
    private String name;
    private String kind;
    private String addressStreet;
    private String addressZipCode;
    private String addressCity;
    private String email;
    private String nip;
    private String regon;
    private Date joinDate;

    public Company(int id, String name, String kind, String addressStreet, String addressZipCode, String addressCity, String email, String nip, String regon, Date joinDate)
    {
        this.id = id;
        this.name = name;
        this.kind = kind;
        this.addressStreet = addressStreet;
        this.addressZipCode = addressZipCode;
        this.addressCity = addressCity;
        this.email = email;
        this.nip = nip;
        this.regon = regon;
        this.joinDate = joinDate;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getKind()
    {
        return kind;
    }

    public void setKind(String kind)
    {
        this.kind = kind;
    }

    public String getAddressStreet()
    {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet)
    {
        this.addressStreet = addressStreet;
    }

    public String getAddressZipCode()
    {
        return addressZipCode;
    }

    public void setAddressZipCode(String addressZipCode)
    {
        this.addressZipCode = addressZipCode;
    }

    public String getAddressCity()
    {
        return addressCity;
    }

    public void setAddressCity(String addressCity)
    {
        this.addressCity = addressCity;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getNip()
    {
        return nip;
    }

    public void setNip(String nip)
    {
        this.nip = nip;
    }

    public String getRegon()
    {
        return regon;
    }

    public void setRegon(String regon)
    {
        this.regon = regon;
    }

    public Date getJoinDate()
    {
        return joinDate;
    }

    public void setJoinDate(Date joinDate)
    {
        this.joinDate = joinDate;
    }
}
