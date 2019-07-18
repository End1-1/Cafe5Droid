package com.example.cafe5droid.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.cafe5droid.Classes.CActivity;
import com.example.cafe5droid.Classes.CPref;
import com.example.cafe5droid.Classes.CSocketClient;
import com.example.cafe5droid.Classes.LocaleHelper;
import com.example.cafe5droid.R;
import com.example.cafe5droid.Structures.SStaff;

import org.json.JSONException;
import org.json.JSONObject;

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
            case ASettings.arc_Settings:
                break;
        }
    }

    @Override
    protected void buttonClick(int id) {
        super.buttonClick(id);
        switch (id) {
            case R.id.btnLogin:
                createPasswordDialog(getString(R.string.Password), 0);
                break;
            case R.id.btnSettings:
                createActivityForResult(ASettings.class, ASettings.arc_Settings);
                break;
        }
    }

    @Override
    public void messageResponse(JSONObject o) {
        super.messageResponse(o);
        try {
            int errCode = o.getInt("reply");
            if (errCode == -1) {
                SStaff.unset(this);
                alertDialogText(R.string.Error, o.getString("msg"));
                return;
            }
            if (errCode == 0) {
                SStaff.unset(this);
                alertDialog(R.string.Error, R.string.AccessDenied);
                return;
            }
            SStaff staff = SStaff.create(o);
            if (staff == null) {
                SStaff.unset(this);
                alertDialogText(R.string.Error, "Json read failed");
                return;
            } else {
                staff.saveToPreference(this);
                setResult(RESULT_OK);
            }
            createActivity(AHall.class);
        } catch (JSONException e) {
            e.printStackTrace();
            SStaff.unset(this);
        }
    }

    @Override
    public void onPassword(String pwd, int id) {
        try {
            JSONObject o = new JSONObject();
            o.put("cmd", CSocketClient.cmd_checkuser);
            o.put("pass", pwd);
            o.put("md5", 1);
            sendMessage(o.toString(), R.string.LoginIntoServer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
