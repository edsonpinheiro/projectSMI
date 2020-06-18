package br.com.daniel.ramos.projetosmi.Presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

import br.com.daniel.ramos.projetosmi.Services.BluetoothConnectionService;
import br.com.daniel.ramos.projetosmi.View.ViewMVP;



public class BluetoothPresenter implements PresenterMVP.BluetoothPresenter {
    private static final String TAG = "BluetoothPresenter";

    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    // UUID para HC-06 Bluetooth Module
    // private static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothDevice myDevice;

    private ViewMVP.BluetoothView mView;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothDevice> devices;
    private BluetoothConnectionService bluetoothConnectionService;

    public BluetoothPresenter(ViewMVP.BluetoothView mView, BluetoothAdapter mBluetoothAdapter) {
        this.mView = mView;
        this.mBluetoothAdapter = mBluetoothAdapter;
    }

    @Override
    public void enableDisableBT() {
        if(mBluetoothAdapter == null){
            // Dispositivo não pode usart o bluetooth
            Log.d(TAG, "enableDisableBT: Não possui capacidades Bluetooth");
        }
        if (!mBluetoothAdapter.isEnabled()){
            mView.enableBT();
            mView.filterStatusBT();
        }
        if (mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.disable();
            mView.filterStatusBT();
        }
    }

    @Override
    public void enableDisableDiscoverableBT() {
        mView.enableDisableDiscoverableBT();
        mView.filterDiscoverableBT();
    }

    @Override
    public void findUnpairedDevices(ArrayList<BluetoothDevice> devices) {
        this.devices = devices;
        Log.d(TAG, "btnDiscover: Looking for unpaired devices");
        if(mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery");

            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            mView.filterDiscoverUnpairedBT();
        } else {
            checkBTPermissions();
            mBluetoothAdapter.startDiscovery();
            mView.filterDiscoverUnpairedBT();
        }

    }


    // ============ BROADCAST RECEIVERS ============= //
    @Override
    public void mBroadcastReceiverStatus(Context context, Intent intent) {
        String action = intent.getAction();

        if(action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    Log.d(TAG, "onReceive: STATE OFF");
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                    break;
            }
        }
    }

    @Override
    public void mBroadcastReceiverDiscovery(Context context, Intent intent) {
        final String action = intent.getAction();

        if(action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
            int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

            switch (mode) {
                case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                    Log.d(TAG, "mBroadcastReceiverDiscovery: Discoverability Enabled");
                    break;
                case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                    Log.d(TAG, "mBroadcastReceiverDiscovery: Discoverability Disabled. Able to receive connections");
                    break;
                case BluetoothAdapter.SCAN_MODE_NONE:
                    Log.d(TAG, "mBroadcastReceiverDiscovery: Discoverability Disabled. Not able to receive connections");
                    break;
                case BluetoothAdapter.STATE_CONNECTING:
                    Log.d(TAG, "mBroadcastReceiverDiscovery: Connecting...");
                    break;
                case BluetoothAdapter.STATE_CONNECTED:
                    Log.d(TAG, "mBroadcastReceiverDiscovery: Connected.");
                    break;
            }
        }
    }

    @Override
    public void mBroadcastReceiverDiscoverUnpaired(Context context, Intent intent) {
        final String action = intent.getAction();
        Log.d(TAG, "onReceive: ACTION FOUND");

        if (action.equals(BluetoothDevice.ACTION_FOUND)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            devices.add(device);
            if (device.getName() == null){
                Log.d(TAG, "onReceive: Unknown" + ": " + device.getAddress());
            } else {
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
            }
            mView.refreshListViewDevices(context, devices);
        }
    }

    @Override
    public void mBroadcastReceiver4(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
            BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            switch (mDevice.getBondState()) {
                case BluetoothDevice.BOND_BONDED:
                    Log.d(TAG, "BroadcastReceiver:  BOND_BONDED.");
                    // Atribui o valor de mDevice para a variavel global myDevice
                    myDevice = mDevice;
                    break;
                case BluetoothDevice.BOND_BONDING:
                    Log.d(TAG, "BroadcastReceiver:  BOND_BONDING.");
                    break;
                case BluetoothDevice.BOND_NONE:
                    Log.d(TAG, "BroadcastReceiver:  BOND_NONE.");
                    break;
            }
        }
    }



    @Override
    public void checkBTPermissions() {
        //TODO: Requisitar permissões do que esta no manifest.xml
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

        }
    }

    @Override
    public void startBTConnection(BluetoothDevice device, UUID uuid) {
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection");
        bluetoothConnectionService.startClient(device, uuid);
        mView.showToastMessageShort("Conexão bluetooth iniciada");
    }

    @Override
    public void sendWriteBytes(EditText etSend) {
        byte[] bytes = etSend.getText().toString().getBytes(Charset.defaultCharset());
        bluetoothConnectionService.write(bytes);
    }

    // Metodo para iniciar a comunicação
    //**** A conexão irá falhar e crashar se você não parear primeiro
    @Override
    public void startConnection() {
        startBTConnection(myDevice, MY_UUID_INSECURE);
    }

    @Override
    public void initBluetoothAndAssignItemClicked(Context context, int position, ArrayList<BluetoothDevice> mBTDevices) {
        mBTDevices.get(position).createBond();
        myDevice = mBTDevices.get(position);
        bluetoothConnectionService = new BluetoothConnectionService(context);
    }
}
