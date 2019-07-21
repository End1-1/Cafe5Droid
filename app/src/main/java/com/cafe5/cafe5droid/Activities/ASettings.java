package com.cafe5.cafe5droid.Activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cafe5.cafe5droid.Classes.CActivity;
import com.cafe5.cafe5droid.Classes.CMenu;
import com.cafe5.cafe5droid.Classes.CPref;
import com.cafe5.cafe5droid.Classes.CSocketClientTask;
import com.cafe5.cafe5droid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ASettings extends CActivity {

    public static final int arc_Settings = 2;
    private Spinner spMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asettings);
        setEditText(R.id.edServerIP, CPref.serverIP);
        setEditInteger(R.id.edServerPort, CPref.serverPort);
        setEditText(R.id.edSettingsPassword, CPref.settingsPassword);
        button(R.id.btnDownloadMenu);
        button(R.id.btnSave);
        button(R.id.btnDownloadHall);
        spMenu = findViewById(R.id.spMenu);
        if (CMenu.listOfMenu.isEmpty()) {
            CMenu.loadFromDB(this);
        }
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CMenu.listOfMenu);
        spMenu.setAdapter(aa);
        spMenu.setSelection(aa.getPosition(CPref.menuName));
    }

    @Override
    protected void buttonClick(int id) {
        switch (id) {
            case R.id.btnSave:
                CPref.setString(this, "server_id", editTextText(R.id.edServerIP));
                CPref.setInt(this, "server_port", editTextInteger(R.id.edServerPort));
                CPref.setSettingsPassword(this, editTextText(R.id.edSettingsPassword));
                if (spMenu.getSelectedItem() != null) {
                    CPref.setString(this, "menu", spMenu.getSelectedItem().toString());
                }
                CPref.init(this);
                alertDialog(R.string.Empty, R.string.Saved);
                break;
            case R.id.btnDownloadMenu:
                downloadMenu();
                break;
            case R.id.btnDownloadHall:
                downloadHall();
                break;
        }
    }

    private void downloadMenu() {
        try {
            JSONObject o = new JSONObject();
            o.put("cmd", CSocketClientTask.cmd_menu);
            sendMessage(o.toString(), R.string.ProcessMenu);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void downloadHall() {
        try {
            JSONObject o = new JSONObject();
            o.put("cmd", CSocketClientTask.cmd_hall);
            sendMessage(o.toString(), R.string.ProcessMenu);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageResponse(JSONObject o) {
        super.messageResponse(o);
        try {
            int cmd = o.getInt("cmd");
            switch (cmd) {
                case CSocketClientTask.cmd_menu:
                    JSONArray ja = o.getJSONArray("menu");
                    CMenu.processDishes(ja);
                    CMenu.save(this);
                    ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CMenu.listOfMenu);
                    spMenu.setAdapter(aa);
                    break;
                case CSocketClientTask.cmd_hall:
                    JSONArray halls = o.getJSONArray("halls");
                    JSONArray tables = o.getJSONArray("tables");
                    AHall.hall.setupHall(this, halls, tables);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hideProgressDialog();
    }
}
