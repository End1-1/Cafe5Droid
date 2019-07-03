package com.example.cafe5droid.Classes;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CSocketClient extends AsyncTask<String, Integer, Integer> {

    public final static int cmd_hall = 1;
    public final static int cmd_menu = 2;
    public final static int cmd_checkuser = 3;

    JSONObject reply = new JSONObject();
    public String strData;
    public CActivity activity = null;

    @Override
    protected Integer doInBackground(String... strings) {
        try {
            Socket s = new Socket(CPref.serverIP, CPref.serverPort);
            OutputStream os = s.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            ByteBuffer bb = ByteBuffer.allocate(4);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.putInt(strData.length());
            byte[] bytes = bb.array();
            dos.write(bytes, 0, 4);
            dos.flush();
            dos.writeBytes(strData);
            dos.flush();
            InputStream is = s.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            byte [] b = new byte[4];
            int read = dis.read(b, 0, 4);
            bb.clear();
            bb.put(b);
            bb.position(0);
            Integer datasize = bb.getInt();
            strData = "";
            while (dis.available() > 0 || datasize > 0) {
               int pt;
               byte [] bbb = new byte[8192];
               pt = dis.read(bbb, 0, 8192);
               datasize -= pt;
               strData += new String(bbb, 0, pt);
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                reply.put("msg", e.getMessage());
                reply.put("reply", -1);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return 1;
        }
        try {
            reply = new JSONObject(strData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if (activity != null) {
            activity.messageResponse(reply);
        }
    }
}
