package com.example.cafe5droid.Activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.cafe5droid.Classes.CActivity;
import com.example.cafe5droid.Classes.CMenu;
import com.example.cafe5droid.Classes.CPref;
import com.example.cafe5droid.Classes.CSocketClient;
import com.example.cafe5droid.R;

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
        button(R.id.btnDownloadMenu);
        button(R.id.btnSave);
        spMenu = findViewById(R.id.spMenu);
    }

    @Override
    protected void buttonClick(int id) {
        switch (id) {
            case R.id.btnSave:
                CPref.setString(this, "server_id", editTextText(R.id.edServerIP));
                CPref.setInt(this, "server_port", editTextInteger(R.id.edServerPort));
                if (spMenu.getSelectedItem() != null) {
                    CPref.setString(this, "menu", spMenu.getSelectedItem().toString());
                }
                CPref.init(this);
                alertDialog(R.string.Empty, R.string.Saved);
                break;
            case R.id.btnDownloadMenu:
                downloadMenu();
                break;
        }
    }

    private void downloadMenu() {
        try {
            JSONObject o = new JSONObject();
            o.put("cmd", CSocketClient.cmd_menu);
            sendMessage(o.toString(), R.string.ProcessMenu);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageResponse(JSONObject o) {
        super.messageResponse(o);
        try {
            JSONArray ja = o.getJSONArray("menu");
            CMenu.processDishes(ja);
            CMenu.save(this);
            ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CMenu.listOfMenu);
            spMenu.setAdapter(aa);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hideProgressDialog();
    }
}
