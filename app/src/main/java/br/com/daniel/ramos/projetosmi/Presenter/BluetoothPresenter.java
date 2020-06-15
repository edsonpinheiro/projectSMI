package br.com.daniel.ramos.projetosmi.Presenter;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import br.com.daniel.ramos.projetosmi.View.ViewMVP;



public class BluetoothPresenter implements PresenterMVP.BluetoothPresenter {
    private static final String TAG = "BluetoothPresenter";

    private ViewMVP.BluetoothView mView;
    private BluetoothAdapter mBluetoothAdapter;

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
    public void broadcastReceiver1(Context context, Intent intent) {
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
}
