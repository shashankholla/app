package com.example.myalternative;

public class pojoApp {
    public  String name ;
    public String packageName ;
    public Alternative[] alternative;
    public String appIcon;

    public String altAppName(){
        return alternative[0].altName;
    }
    public String altAppLink(){
        return alternative[0].link;
    }
    public String altAppIcon(){
        return alternative[0].altAppIcon;
    }
}


class Alternative{
   public String altName ;
   public  String link;
   public String altAppIcon;
}