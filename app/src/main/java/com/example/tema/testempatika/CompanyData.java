package com.example.tema.testempatika;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.spec.ECField;

/**
 * Created by tema on 11.05.15.
 */
public class CompanyData {
    public String Name;
    public String Description;
    public String Hours;
    public String Phone;
    public String Email;

    public CompanyData(String Name, String Description, String Hours, String Phone, String Email) {
        this.Name = Name;
        this.Description = Description;
        this.Hours = Hours;
        this.Phone = Phone;
        this.Email = Email;
    }
}
