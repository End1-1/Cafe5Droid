package com.example.cafe5droid.Classes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cafe5droid.R;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public class CActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog pd = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
    }

    protected void buttonClick(int id) {
    }

    public final EditText editText(int id) {
        return (EditText) findViewById(id);
    };

    public final void setEditText(int id, String text) {
        editText(id).setText(text);
    }

    public final void setEditInteger(int id, int value) {
        String text = Integer.toString(value);
        setEditText(id, text);
    }

    public final String editTextText(int id) {
        return editText(id).getText().toString();
    }

    public final int editTextInteger(int id) {
        return Integer.valueOf(editTextText(id));
    }

    public final TextView textView(int id) {
        return (TextView) findViewById(id);
    }

    public final RecyclerView recyclerView(int id) {
        return (RecyclerView) findViewById(id);
    }

    public final ImageView imageView(int id, View.OnClickListener clickListener) {
        ImageView iv = findViewById(id);
        if (iv != null) {
            if (clickListener != null) {
                iv.setOnClickListener(clickListener);
            }
        }
        return iv;
    }

    public final Button button(int id) {
        Button btn = (Button) findViewById(id);
        btn.setOnClickListener(this);
        return btn;
    }

    public final void createActivity(java.lang.Class T) {
        Intent intent = new Intent(this, T);
        startActivity(intent);
    }

    public final void createActivityWithData(java.lang.Class T, ArrayMap<String, Serializable> data) {
        Intent intent = new Intent(this, T);
        for (Map.Entry<String, Serializable> v: data.entrySet()) {
            intent.putExtra(v.getKey(), v.getValue());
        }
        startActivity(intent);
    }

    public final void createActivityForResult(java.lang.Class T, int code) {
        Intent intent = new Intent(this, T);
        startActivityForResult(intent, code);
    }

    protected void createProgressDialog(int title, int text) {
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle(getString(title));
        pd.setMessage(getString(text));
        pd.setIndeterminate(true);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    protected void hideProgressDialog() {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    protected void alertDialog(int title, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(message))
                .setTitle(getString(title));
        builder.setCancelable(false);
        builder.setNeutralButton(R.string.OK, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void alertDialogText(int title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(getString(title));
        builder.setNeutralButton(R.string.OK, null);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void messageResponse(JSONObject o) {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    protected void sendMessage(String msg, int dlgTitle) {
        createProgressDialog(R.string.Empty, dlgTitle);
        CSocketClient sc = new CSocketClient();
        sc.strData = msg;
        sc.activity = this;
        sc.execute("AA");
    }

    @Override
    public void onClick(View v) {
        buttonClick(v.getId());
    }

    public void log(String msg, String tag) {
        Log.d(tag, msg);
    }
}
