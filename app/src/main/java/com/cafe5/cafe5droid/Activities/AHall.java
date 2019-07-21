package com.cafe5.cafe5droid.Activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cafe5.cafe5droid.Adapters.HallAdapter;
import com.cafe5.cafe5droid.Adapters.TablesAdapter;
import com.cafe5.cafe5droid.Classes.CActivity;
import com.cafe5.cafe5droid.Classes.CHall;
import com.cafe5.cafe5droid.Classes.CPref;
import com.cafe5.cafe5droid.Classes.CSocketClientTask;
import com.cafe5.cafe5droid.R;
import com.cafe5.cafe5droid.Structures.SHall;
import com.cafe5.cafe5droid.Structures.SStaff;
import com.cafe5.cafe5droid.Structures.STable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AHall extends CActivity implements HallAdapter.HallAdapterSelected, TablesAdapter.TablesOptionSelected {

    public static CHall hall = new CHall();
    private SHall currentHall;
    private HallAdapter adHall;
    private RecyclerView rvHall;
    private ImageView ivHallExpander;
    private TextView tvHallName;
    private TablesAdapter adTables;
    private RecyclerView rvTables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ahall);
        tvHallName = textView(R.id.idHallName);
        ivHallExpander = imageView(R.id.ivExpandHall, this);
        adHall = new HallAdapter(this, this);
        rvHall = recyclerView(R.id.rvHall);
        rvHall.setLayoutManager(new LinearLayoutManager(this));
        rvHall.setAdapter(adHall);
        rvHall.setVisibility(View.GONE);

        adTables = new TablesAdapter(this, this);
        rvTables = recyclerView(R.id.rvTables);
        rvTables.setLayoutManager(new GridLayoutManager(this, 4));
        rvTables.setAdapter(adTables);
        rvTables.setVisibility(View.VISIBLE);

        CHall.loadFromDB(this);
        if (hall.empty()) {
            try {
                JSONObject o = new JSONObject();
                o.put("cmd", CSocketClientTask.cmd_hall);
                o.put("hall", 1);
                sendMessage(o.toString(), R.string.ProcessHall);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            if (CPref.currentHall > 0) {
                SHall hall = CHall.getHall(CPref.currentHall);
                if (hall != null) {
                    hallOptionSelected(hall);
                } else {
                    CHall.filterTables(0);
                    adTables.notifyDataSetChanged();
                }
            } else {
                CHall.filterTables(0);
                adTables.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void messageResponse(JSONObject o) {
        super.messageResponse(o);
        try {
            createProgressDialog(R.string.Loading, R.string.ProcessHall);
            JSONArray halls = o.getJSONArray("halls");
            JSONArray tables = o.getJSONArray("tables");
            hall.setupHall(this, halls, tables);
            hideProgressDialog();
            rvHall.setVisibility(View.GONE);
            if (CPref.currentHall > 0) {
                SHall hall = CHall.getHall(CPref.currentHall);
                if (hall != null) {
                    hallOptionSelected(hall);
                }
            }
        } catch (JSONException e) {
            hideProgressDialog();
            rvHall.setVisibility(View.GONE);
            alertDialogText(R.string.Error, "Empty hall arrays");
            e.printStackTrace();
        }
    }

    @Override
    protected void buttonClick(int id) {
        super.buttonClick(id);
        switch (id) {
            case R.id.ivExpandHall:
                if (rvHall.getVisibility() == View.GONE) {
                    rvHall.setVisibility(View.VISIBLE);
                    ivHallExpander.setImageDrawable(getResources().getDrawable(R.drawable.iv_expander_off));
                } else {
                    rvHall.setVisibility(View.GONE);
                    ivHallExpander.setImageDrawable(getResources().getDrawable(R.drawable.iv_expander_on));
                }
                break;
        }
    }

    @Override
    public void hallOptionSelected(SHall hall) {
        currentHall = hall;
        rvHall.setVisibility(View.GONE);
        tvHallName.setText(currentHall.name);
        ivHallExpander.setImageDrawable(getResources().getDrawable(R.drawable.iv_expander_on));
        CHall.filterTables(hall.id);
        adTables.notifyDataSetChanged();
        CPref.setCurrentHall(this, hall.id);
        if (CPref.lastTable > 0) {
            STable table = CHall.getTable(CPref.lastTable);
            if (table != null) {
                tablesOptionSelected(table);
            }
        }
    }

    @Override
    public void tablesOptionSelected(STable table) {
        ArrayMap<String, Serializable> v = new ArrayMap<>();
        v.put("table", table);
        createActivityWithData(AOrder.class, v);
        CPref.setLastTable(this, table.id);
    }

    @Override
    public void onBackPressed() {
        SStaff.unset(this);
        super.onBackPressed();
    }
}
