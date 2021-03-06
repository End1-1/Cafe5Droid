package com.cafe5.cafe5droid.Activities;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cafe5.cafe5droid.Adapters.OrderAdapter;
import com.cafe5.cafe5droid.Classes.CActivity;
import com.cafe5.cafe5droid.Classes.CDatabase;
import com.cafe5.cafe5droid.Classes.CMenu;
import com.cafe5.cafe5droid.Classes.CPref;
import com.cafe5.cafe5droid.Classes.CSocketClientTask;
import com.cafe5.cafe5droid.Fragments.Card;
import com.cafe5.cafe5droid.Fragments.Order;
import com.cafe5.cafe5droid.R;
import com.cafe5.cafe5droid.Structures.SDish;
import com.cafe5.cafe5droid.Structures.SStaff;
import com.cafe5.cafe5droid.Structures.STable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AOrder extends CActivity implements  OrderAdapter.OrderDishSelected {

    public final String FRAGMENT_ORDER = "FRAGMENT_ORDER";
    public boolean allowBack = false;
    private Order order;
    private Card card;
    private TextView place;
    public STable table;
    private ImageView ivCheckout;
    public OrderAdapter adOrder;

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            adOrder.refreshData(AOrder.this, table.id);
            card.orderDishSelected(null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        findViewById(R.id.lvBasket).setOnClickListener(this);
        findViewById(R.id.tvTable).setOnClickListener(this);
        place = textView(R.id.tvTable);
        //ivCheckout = imageView(R.id.ivShowCard, this);
        imageView(R.id.ivCallStaff, this);
        //textView(R.id.tvBasket).setOnClickListener(this);
        table = (STable) getIntent().getSerializableExtra("table");
        if (table != null) {
            place.setText(String.format("%s: %s", getString(R.string.Place), table.name));
        } else {
            alertDialogText(R.string.Error, "Invalid table");
            finish();
        }
        if (CMenu.listOfDishes.isEmpty()) {
            createProgressDialog(R.string.Empty, R.string.ProcessMenu);
            CMenu.loadFromDB(this);
            hideProgressDialog();
        }
        if (CPref.menuName.isEmpty() == false) {
            //Filter menu
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("cleardroid"));
        adOrder = new OrderAdapter(getLayoutInflater(), this);
        order = new Order();
        card = new Card();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.lvContainer, order, FRAGMENT_ORDER);
        fragmentTransaction.commit();
        try {
            JSONObject o = new JSONObject();
            o.put("cmd", CSocketClientTask.cmd_menu);
            sendMessage(o.toString(), R.string.ProcessMenu);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        super.onDestroy();
    }

    @Override
    protected void buttonClick(int id) {
        super.buttonClick(id);
        switch (id) {
            case R.id.ivShowCard:
                adOrder.refreshData(this, table.id);
                adOrder.orderDishSelected = card;
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.lvContainer, card, FRAGMENT_ORDER);
                fragmentTransaction.commit();
                break;
            //case R.id.tvBasket:
            case R.id.lvBasket:
                buttonClick(R.id.ivShowCard);
                break;
            case R.id.ivCallStaff:
                callStaff();
                break;
            case R.id.tvTable:
                if (CPref.settingsPassword.isEmpty() || allowBack) {
                    CPref.backFromOrder = true;
                    super.onBackPressed();
                } else {
                    createPasswordDialog(getString(R.string.Empty), 0);
                }
                break;
        }
    }

    public void callReceipt() {
        if (adOrder.getItemCount() == 0) {
            alertDialog(R.string.Error, R.string.EmptyOrder);
            return;
        }
        try {
            JSONObject o = new JSONObject();
            o.put("cmd", CSocketClientTask.cmd_callreceipt);
            o.put("tablename", table.name);
            sendMessage(o.toString(), R.string.Receipt);
        } catch (JSONException e) {
            e.printStackTrace();
            alertDialogText(R.string.Error, e.getMessage());
        }
    }

    public void checkout() {
        try {
            if (adOrder.getItemCount() == 0) {
                alertDialog(R.string.Error, R.string.EmptyOrder);
                return;
            }
            JSONObject o = new JSONObject();
            SStaff staff = new SStaff();
            staff.get(this);
            o.put("cmd", CSocketClientTask.cmd_apporder);
            o.put("tableid", String.valueOf(table.id));
            o.put("hallid", String.valueOf(table.hall));
            o.put("header", table.header);
            o.put("staffid", String.valueOf(staff.id));
            o.put("tablename", table.name);
            o.put("staffname", staff.fullName());
            o.put("servicevalue", Double.toString(CPref.serviceValue));
            JSONArray ja = new JSONArray();
            for (int i = 0; i < adOrder.getItemCount(); i++) {
                SDish d = adOrder.getDish(i);
                JSONObject jd = new JSONObject();
                jd.put("state", "1");
                jd.put("dishcode", d.id);
                jd.put("dishname", d.name);
                jd.put("adgcode", d.adgCode);
                jd.put("qty1", d.qty1);
                jd.put("qty2", d.qty2);
                jd.put("price", d.price);
                jd.put("print1", d.print1);
                jd.put("print2", d.print2);
                jd.put("store", d.store);
                jd.put("internalid", String.valueOf(d.internalId));
                jd.put("remoteid", d.remoteId);
                ja.put(jd);
            }
            o.put("dishes", ja);
            sendMessage(o.toString(), R.string.Checkout);
        } catch (JSONException e) {
            e.printStackTrace();
            alertDialogText(R.string.Error, e.getMessage());
        }
    }

    private void callStaff() {
        try {
            JSONObject o = new JSONObject();
            o.put("cmd", CSocketClientTask.cmd_callstaff);
            o.put("table", table.name);
            sendMessage(o.toString(), R.string.CallStaff);
        } catch (JSONException e) {
            alertDialog(R.string.Error, R.string.CouldNotCallStaff);
            e.printStackTrace();
        }
    }

    public void goToMenu() {
        adOrder.orderDishSelected = null;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.lvContainer, order, FRAGMENT_ORDER);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.lvContainer);
        if(f instanceof Card) {
            goToMenu();
            return;
        }
        if (f instanceof Order) {
            Order o = (Order) f;
            o.onClick(o.ivExpandPart2);
            return;
        }
    }

    public void addDish(SDish dish) {
        CDatabase db = new CDatabase(this);
        ContentValues cv = db.getContentValues();
        cv.put("tableid", table.id);
        cv.put("header", table.header);
        cv.put("dishcode", dish.id);
        cv.put("dishname", dish.name);
        cv.put("qty1", "1");
        cv.put("qty2", "0");
        cv.put("price", dish.price);
        cv.put("service", "0");
        cv.put("discount", "0");
        cv.put("total", dish.price);
        cv.put("store", dish.store);
        cv.put("print1", dish.print1);
        cv.put("print2", dish.print2);
        cv.put("comment", "");
        cv.put("remind", "0");
        cv.put("adgcode", dish.adgCode);
        cv.put("removereason", "");
        dish.internalId = db.insertWithId("body");
        if (dish.internalId == 0) {
            alertDialogText(R.string.Error, "Cannot insert into body");
            return;
        }
        adOrder.addDish(dish);
        Toast.makeText(this, String.format("%s + 1", dish.name), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void orderDishSelected(SDish dish) {

    }

    @Override
    public void onPassword(String pwd, int id) {
        if (pwd.equals(CPref.settingsPassword)) {
            finish();
        } else {
            Toast.makeText(this, R.string.AccessDenied, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void messageResponse(JSONObject o) {
        super.messageResponse(o);
        try {
            int code = o.getInt("reply");
            if (code == -1) {
                alertDialog(R.string.Error, R.string.ConnectionError);
                return;
            }
            if (code == 0) {
                alertDialogText(R.string.Error, o.getString("msg"));
                return;
            }
            int cmd = o.getInt("cmd");
            CDatabase db = new CDatabase(this);
            String sql;
            switch (cmd) {
                case CSocketClientTask.cmd_apporder:
                    JSONObject jh = o.getJSONObject("header");
                    table.header = jh.getString("f_id");
                    sql = String.format("update tables set header='%s' where id=%d", table.header, table.id);
                    db.exec(sql);
                    JSONArray ja = o.getJSONArray("body");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jd = ja.getJSONObject(i);
                        for (int j = 0; j < adOrder.getItemCount(); j++) {
                            if (adOrder.getDish(j).internalId == jd.getInt("internalid")) {
                                SDish dish = adOrder.getDish(j);
                                dish.remoteId = jd.getString("remoteid");
                                dish.qty2 = jd.getString("qty2");
                                sql = String.format("update body set header='%s', bodyid='%s', qty2='%s' where id=%d",
                                        table.header, dish.remoteId, dish.qty2, dish.internalId);
                                db.exec(sql);
                            }
                        }
                    }
                    adOrder.refreshData(this, table.id);
                    alertDialogText(R.string.Info, o.getString("msg"));
                    break;
                case CSocketClientTask.cmd_callstaff:
                    if (o.getInt("reply") == 1) {
                        alertDialog(R.string.Info, R.string.CallStaffAccepted);
                    } else {
                        alertDialog(R.string.Error, R.string.CouldNotCallStaff);
                    }
                    break;
                case CSocketClientTask.cmd_callreceipt:
                    if (o.getInt("reply") == 1) {
                        alertDialog(R.string.Info, R.string.CallReceiptAccepted);
                    } else {
                        alertDialog(R.string.Error, R.string.CallReceiptIncomplete);
                    }
                    break;
                case CSocketClientTask.cmd_menu:
                    try {
                        ja = o.getJSONArray("menu");
                        CMenu.processDishes(ja);
                        CMenu.save(this);
                        adOrder.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
