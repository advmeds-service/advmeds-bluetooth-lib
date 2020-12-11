package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.advmeds.bluetooth_lib.measure.BaseBtCallBack;
import com.advmeds.bluetooth_lib.measure.BaseBtDevice;
import com.advmeds.bluetooth_lib.measure.BaseBtDeviceFactory;
import com.advmeds.bluetooth_lib.measure.VitalSign;
import com.vise.baseble.ViseBle;
import com.vise.baseble.callback.scan.IScanCallback;
import com.vise.baseble.callback.scan.ScanCallback;
import com.vise.baseble.model.BluetoothLeDevice;
import com.vise.baseble.model.BluetoothLeDeviceStore;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements BaseBtCallBack, IScanCallback {
    private BaseBtDevice baseBtDevice;

    private ScanCallback scanCallback = new ScanCallback(this);

    private String deviceName = "FORA D40";
    private String searchName = "FORA D40";

    // 632 = 30:45:11:E3:BC:67
    //
    // 633 = 30:45:11:E3:C7:86

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Device", Build.DEVICE);

        baseBtDevice = BaseBtDeviceFactory.createBtDevice(deviceName)
                .setAutoStopReceive(true)
                .setShutdownAfterReceive(true);

        baseBtDevice.setCallBack(this);

        ViseBle.getInstance().startScan(scanCallback);

        Timber.d((2 & 2) + "");
    }

    @Override
    public void onDeivceConnectFail() {
        Timber.d("onDeivceConnectFail");
    }

    @Override
    public void onNoData() {
        Timber.d("onNoData");
    }

    @Override
    public void onReceiveData(VitalSign vitalSign) {
        Timber.d("onReceiveData");
        Timber.d(vitalSign.getSystolic());

//        Timber.d("Data length = " + vitalSign.length);

//        Timber.d(vitalSign[0]);
    }

    @Override
    public void onDeviceConnected() {
        Timber.d("onDeviceConnected");
    }

    @Override
    public void onMeasureFail(String message) {
        Timber.d("onMeasureFail" + message);
    }

    @Override
    public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {
        Timber.d("onDeviceFound : %s", bluetoothLeDevice.getName());
//        Timber.d("onDeviceFound : %s", bluetoothLeDevice.getAddress());

        if (bluetoothLeDevice.getName() != null && bluetoothLeDevice.getName().contains(searchName) && scanCallback.isScanning()) {
            Timber.d("onDeviceFound2 : %s", bluetoothLeDevice.getAddress());
            ViseBle.getInstance().stopScan(scanCallback);

            baseBtDevice.startConnect(this, bluetoothLeDevice.getDevice());
        }
    }

    @Override
    public void onScanFinish(BluetoothLeDeviceStore bluetoothLeDeviceStore) {
        Timber.d("onScanFinish");
    }

    @Override
    public void onScanTimeout() {
        Timber.d("onScanTimeout");
    }
}
