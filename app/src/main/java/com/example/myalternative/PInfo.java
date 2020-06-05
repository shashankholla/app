package com.example.myalternative;

import android.graphics.drawable.Drawable;
import android.util.Log;
public class PInfo implements Comparable<PInfo>{
    public String pname = "";
    public String appname = "";
    public String versionName = "";
    public int versionCode = 0;
    public Drawable icon;
    public void prettyPrint() {
        Log.i("appInfo", appname + "\t" + pname + "\t" + versionName + "\t" + versionCode);
    }

    @Override
    public int compareTo(PInfo o) {
        return this.appname.compareTo(o.appname);
    }
}
