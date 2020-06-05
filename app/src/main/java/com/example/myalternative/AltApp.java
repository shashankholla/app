package com.example.myalternative;

import android.graphics.drawable.Drawable;

public class AltApp {
    String theApp;
    String altApp;
    String altAppLink;
    String theAppIcon;
    String altAppIcon;
    public AltApp(String theApp, String altApp, String altAppLink, String theAppIcon, String altAppIcon) {
        this.theApp = theApp;
        this.altApp = altApp;
        this.theAppIcon = theAppIcon;
        this.altAppIcon = altAppIcon;
        this.altAppLink = altAppLink;
    }

    public AltApp(String theApp, String altApp, String altAppLink){
        this.theApp = theApp;
        this.altApp = altApp;
        this.altAppLink = altAppLink;

    }




}
