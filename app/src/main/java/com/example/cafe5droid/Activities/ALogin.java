package com.example.cafe5droid.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.cafe5droid.Classes.CActivity;
import com.example.cafe5droid.Classes.CPref;
import com.example.cafe5droid.Classes.CSocketClient;
import com.example.cafe5droid.R;
import com.example.cafe5droid.Structures.SStaff;

import org.json.JSONException;
import org.json.JSONObject;

public class ALogin extends CActivity {

    public static final int arc_login = 1;

    private EditText edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alogin);
        edPassword = editText(R.id.edPassword);
        button(R.id.btnLogin2);
    }

    @Override
    protected void buttonClick(int id) {
        super.buttonClick(id);
        switch (id) {
            case R.id.btnLogin2:
                login();
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
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            SStaff.unset(this);
        }
    }

    void login() {
        try {
            JSONObject o = new JSONObject();
            o.put("cmd", CSocketClient.cmd_checkuser);
            o.put("pass", edPassword.getText());
            o.put("md5", 1);
            sendMessage(o.toString(), R.string.LoginIntoServer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
