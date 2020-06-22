package com.example.myalternative;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import android.content.res.Configuration;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.Gravity;
import android.widget.Toast;
import java.util.Calendar;
import android.net.Uri;

public class AboutActivity extends AppCompatActivity {
    private final String PLAYSTORE_URL = getString(R.string.sharPlaystoreUrl);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.second_activity);
        simulateDayNight(/* DAY */ 0);
        Element adsElement = new Element();
        View second_activity = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.appicon)
                .addGroup("Chinese App Alternative")
                .addItem(new Element().setTitle("Version 0.1"))
                .addItem(launchHelp())
                .addGroup("Connect with us")
                .addPlayStore("shar.listviewjson")
                .addEmail("sharfeedback@gmail.com")
                .addYoutube("UCGGi-VJ6pkpA7skNAus9lDQ")
                .addItem(OtherElement())
                .addItem(ShareElement())
                .addItem(getCopyRightsElement())
                .create();
        setContentView(second_activity);
    }
    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.about_icon_copy_right);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this, copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }

    Element launchHelp() {
        Element launchHelp = new Element();

        launchHelp.setTitle("See Tutorial Again?");
        launchHelp.setIconDrawable(R.drawable.ic_about);
        launchHelp.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        launchHelp.setIconNightTint(android.R.color.white);
        launchHelp.setGravity(Gravity.CENTER);
        launchHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), tutorial.class));
            }
        });
        return launchHelp;
    }






    Element OtherElement() {
        Element otherElement = new Element();
        final String copyrights = String.format(getString(R.string.otherapps));
        otherElement.setTitle(copyrights);
        otherElement.setIconDrawable(R.drawable.about_icon_google_play);
        otherElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        otherElement.setIconNightTint(android.R.color.white);
        otherElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAYSTORE_URL));
                startActivity(intent);
            }
        });
        return otherElement;
    }

    Element ShareElement() {
        Element shareElement = new Element();
        final String copyrights = String.format(getString(R.string.share));
        shareElement.setTitle(copyrights);
        shareElement.setIconDrawable(R.drawable.sharedark);
        shareElement.setIconTint(android.R.color.holo_blue_light);
        shareElement.setIconNightTint(android.R.color.white);
        shareElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Hey check out Medo app at: https://play.google.com/store/apps/details?id=shar.listviewjson");
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, getString(R.string.share)));
            }
        });
        return shareElement;
    }

    void simulateDayNight(int currentSetting) {
        final int DAY = 0;
        final int NIGHT = 1;
        final int FOLLOW_SYSTEM = 3;

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        } else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else if (currentSetting == FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }




}