package com.kingtous.remoteaudioplayer;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kingtous.remoteaudioplayer.adapter.BluetoothDeviceAdapter;
import com.kingtous.remoteaudioplayer.model.BluetoothDeviceModel;
import com.kingtous.remoteaudioplayer.task.CommandSender;
import com.kingtous.remoteaudioplayer.task.FileSender;

import java.util.ArrayList;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    ConnectionHolder connectionHolder;
    @BindView(R.id.text_music_name)
    TextView textMusicName;
    @BindView(R.id.text_play_status)
    TextView textPlayStatus;
    @BindView(R.id.text_connected_devices)
    TextView textConnectedDevices;
    @BindView(R.id.btn_select_devices)
    Button btnSelectDevices;
    @BindView(R.id.btn_select_music)
    Button btnSelectMusic;
    @BindView(R.id.btn_continue_play)
    Button btnContinuePlay;
    @BindView(R.id.btn_pause_song)
    Button btnPauseSong;
    @BindView(R.id.btn_stop_playing)
    Button btnStopPlaying;

    ArrayList<BluetoothDeviceModel> device_list = new ArrayList<>();
    @BindView(R.id.btn_vol_up)
    Button btnVolUp;
    @BindView(R.id.btn_vol_down)
    Button btnVolDown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initEvent();
    }


    private void initEvent() {
        connectionHolder = new ConnectionHolder(this);

        btnSelectDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<BluetoothDevice> pairedDevices = ConnectionHolder.bluetoothAdapter.getBondedDevices();
                for (BluetoothDevice device : pairedDevices) {
                    device_list.add(new BluetoothDeviceModel(device.getName(), device.getAddress()));
                }
                final Dialog dialog = new Dialog(MainActivity.this);
                View listview = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_bluetooth_list, null, false);
                RecyclerView recyclerView = listview.findViewById(R.id.rv_bluetooth);
                LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
                recyclerView.setLayoutManager(manager);
                BluetoothDeviceAdapter adapter = new BluetoothDeviceAdapter(device_list);
                adapter.setOnItemClickListener(new BluetoothDeviceAdapter.OnItemClickListener() {
                    @Override
                    public void OnClick(View view, int Position) {
                        ConfigHolder.btModel = device_list.get(Position);
                        textConnectedDevices.setText(ConfigHolder.btModel.getName());
                        dialog.dismiss();
                    }
                });
                recyclerView.setAdapter(adapter);
                dialog.setContentView(listview);
                dialog.show();
            }
        });

        btnSelectMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectMusic();
            }
        });

        btnContinuePlay.setOnClickListener(this);
        btnPauseSong.setOnClickListener(this);
        btnStopPlaying.setOnClickListener(this);
        btnVolUp.setOnClickListener(this);
        btnVolDown.setOnClickListener(this);
    }


    private void setSelectMusic() {
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, permissions)) {
            if (checkDeviceSelected()) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/mp3");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, ConfigHolder.REQUESTCODE_FILESELECT);
            }
        } else {
            EasyPermissions.requestPermissions(this, "本功能需要读写权限才能运行", ConfigHolder.REQUESTCODE_READ_EXTERNAL, permissions);
        }
    }

    private boolean checkDeviceSelected() {
        if (ConfigHolder.btModel == null) {
            Toast.makeText(this, "未选择设备", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ConfigHolder.REQUESTCODE_FILESELECT:
                if (resultCode == Activity.RESULT_OK) {
                    ConfigHolder.fileURI = data.getData();
                    String path = data.getData().getPath();
                    String name = path.substring(path.lastIndexOf('/') + 1);
                    textMusicName.setText(name);
                    // 传文件
                    FileSender sender = new FileSender(this);
                    sender.execute();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onClick(View view) {
        if (checkDeviceSelected()) {
            String action;
            if (view.getId() == btnContinuePlay.getId()) {
                action = ConfigHolder.ACT_PLAY;
            } else if (view.getId() == btnPauseSong.getId()) {
                action = ConfigHolder.ACT_PAUSE;
            } else if (view.getId() == btnStopPlaying.getId()) {
                action = ConfigHolder.ACT_STOP;
            } else if (view.getId() == btnVolUp.getId()) {
                action = ConfigHolder.ACT_VOL_UP;
            } else if (view.getId() == btnVolDown.getId()) {
                action = ConfigHolder.ACT_VOL_DOWN;
            } else {
                return;
            }
            CommandSender sender = new CommandSender(this, action, "");
            sender.execute();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
