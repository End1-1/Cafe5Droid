package com.example.cafe5droid.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.cafe5droid.Classes.CActivity;
import com.example.cafe5droid.Classes.CPref;
import com.example.cafe5droid.Classes.LocaleHelper;
import com.example.cafe5droid.R;
import com.example.cafe5droid.Structures.SStaff;

import java.util.Locale;

public class MainActivity extends CActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocaleHelper.setLocale(MainActivity.this, "hy");
        CPref.init(this);
        button(R.id.btnLogin);
        button(R.id.btnSettings);
        SStaff staff = new SStaff();
        if (staff.get(this)) {
            createActivity(AHall.class);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ALogin.arc_login:
                SStaff staff = new SStaff();
                if (staff.get(this)) {
                    createActivity(AHall.class);
                }
                break;
            case ASettings.arc_Settings:
                break;
        }
    }

    @Override
    protected void buttonClick(int id) {
        super.buttonClick(id);
        switch (id) {
            case R.id.btnLogin:
                createActivityForResult(ALogin.class, ALogin.arc_login);
                break;
            case R.id.btnSettings:
                createActivityForResult(ASettings.class, ASettings.arc_Settings);
                break;
        }
    }
}
