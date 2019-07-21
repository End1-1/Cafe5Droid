package com.cafe5.cafe5droid.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.cafe5.cafe5droid.Classes.CActivity;
import com.cafe5.cafe5droid.Classes.CPref;
import com.cafe5.cafe5droid.Classes.CSocketClientTask;
import com.cafe5.cafe5droid.Classes.LocaleHelper;
import com.cafe5.cafe5droid.R;
import com.cafe5.cafe5droid.Services.BgService;
import com.cafe5.cafe5droid.Structures.SStaff;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends CActivity {

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = new Intent(this, BgService.class);
        startService(i);
    }

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
            o.put("cmd", CSocketClientTask.cmd_checkuser);
            o.put("pass", pwd);
            o.put("md5", 1);
            sendMessage(o.toString(), R.string.LoginIntoServer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
