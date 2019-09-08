package com.kingtous.remoteaudioplayer.task;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kingtous.remoteaudioplayer.ConfigHolder;
import com.kingtous.remoteaudioplayer.ConnectionHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class CommandSender extends AsyncTask<Void,String,Void> {

    Context context;
    String action;
    String value;

    ProgressDialog dialog;

    public CommandSender(Context context, String action, String value){
        this.context = context;
        this.action = action;
        this.value = value;
        dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //init progress
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("正在发送指令");
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        BluetoothDevice device = ConnectionHolder.bluetoothAdapter.getRemoteDevice(ConfigHolder.btModel.getMac());
        try {
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.fromString(ConfigHolder.uuid));
            socket.connect();
            OutputStream stream = socket.getOutputStream();
            JSONObject object=new JSONObject();
            object.put("act",action);
            object.put("value",value);
            if (stream ==null){
                throw new IOException("对方关闭连接");
            }
            stream.write(object.toString().getBytes(StandardCharsets.UTF_8));
            stream.close();
        } catch (IOException e) {
            publishProgress(new String[]{e.getMessage()});
        } catch (JSONException e) {
            publishProgress(new String[]{e.getMessage()});
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Toast.makeText(context,values[0],Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
    }
}
