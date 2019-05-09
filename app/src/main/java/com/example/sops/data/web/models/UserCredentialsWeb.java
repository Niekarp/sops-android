package com.example.sops.data.web.models;

import com.google.gson.annotations.SerializedName;

public class UserCredentialsWeb
{
    @SerializedName("grant_type")
    private final String grantType = "password";

    @SerializedName("username")
    public String username;

    @SerializedName("password")
    public String password;

    public String getGrantType()
    {
        return grantType;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
