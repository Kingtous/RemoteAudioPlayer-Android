package com.kingtous.remoteaudioplayer.task;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kingtous.remoteaudioplayer.ConfigHolder;
import com.kingtous.remoteaudioplayer.ConnectionHolder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class FileSender extends AsyncTask<Void,String,Void> {

    Context context;

    ProgressDialog dialog;

    public FileSender(Context context){
        this.context=context;
        dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //init progress
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("正在传输请求，请稍候");
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        BluetoothDevice device = ConnectionHolder.bluetoothAdapter.getRemoteDevice(ConfigHolder.btModel.getMac());
        try {
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.fromString(ConfigHolder.uuid));
            socket.connect();
            OutputStream stream = socket.getOutputStream();
            InputStream fileInputStream = context.getContentResolver().openInputStream(ConfigHolder.fileURI);
            publishProgress(new String[]{"正在推送歌曲，请稍候"});
            int size = -1;
            byte[] buffer = new byte[1024];
            while((size = fileInputStream.read(buffer, 0, 1024)) != -1){
                stream.write(buffer, 0, size);
            }
            fileInputStream.close();
            stream.close();
            socket.close();
        } catch (IOException e) {
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
