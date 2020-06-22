package com.example.myalternative;

import android.graphics.drawable.Drawable;



public class App {
    public String appName;
    public Drawable appIcon;
    public String appLink;
    public String pname;

    public App(String appName, String pname, String appLink){
        this.appName = appName;
        this.appLink = appLink;
        this.pname = pname;
    }
    public App(String appName, String pname, String appLink, Drawable appIcon){
        this.appName = appName;
        this.appLink = appLink;
        this.pname = pname;
        this.appIcon = appIcon;
    }
}
