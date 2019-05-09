package com.example.sops.views.other;

public class LabelValuePair
{
    private String Label;
    private String Value;

    public LabelValuePair(String label, String value)
    {
        Label = label;
        Value = value;
    }

    public String getLabel()
    {
        return Label;
    }

    public void setLabel(String label)
    {
        Label = label;
    }

    public String getValue()
    {
        return Value;
    }

    public void setValue(String value)
    {
        Value = value;
    }
}
