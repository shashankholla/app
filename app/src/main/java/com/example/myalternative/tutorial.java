package com.example.myalternative;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

import org.jetbrains.annotations.Nullable;

public class tutorial extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntroFragment.newInstance(

                "Hey there,Get rid of Chinese Apps",
               "Click the arrow button to find out all the chinese apps in your phone and we will show you alternative apps for these",
                R.drawable.three,
                R.color.white



        ));
        addSlide(AppIntroFragment.newInstance(
                "Here you go,all alternative apps for chinese apps",
                "Its that simple,Let's get started!",
                R.drawable.intro,
                R.color.white

        ));
    }

    public void updateSharedPref(){
        SharedPreferences.Editor sharedPreferencesEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        sharedPreferencesEditor.putBoolean(
                "firstRun", false);
        sharedPreferencesEditor.apply();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDonePressed(@Nullable Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        updateSharedPref();
        finish();
    }

    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        updateSharedPref();

        finish();
    }
}
