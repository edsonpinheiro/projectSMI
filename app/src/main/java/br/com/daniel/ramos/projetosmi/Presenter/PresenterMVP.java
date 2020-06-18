package br.com.daniel.ramos.projetosmi.Presenter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

public interface PresenterMVP {

    /* Representa o Presenter no MVP para o DashboardFragment */
    interface DashboardPresenter {
        void replaceFragment(FragmentManager fm, Fragment fragment, String tag);
        void onDestroy();
    }

    /* Representa o Presenter no MVP para o BluetoothFragment */
    interface BluetoothPresenter {
        void enableDisableBT();
        void enableDisableDiscoverableBT();
        void findUnpairedDevices(ArrayList<BluetoothDevice> devices);

        void mBroadcastReceiverStatus(Context context, Intent intent);
        void mBroadcastReceiverDiscovery(Context context, Intent intent);
        void mBroadcastReceiverDiscoverUnpaired(Context context, Intent intent);
        void mBroadcastReceiver4(Context context, Intent intent);

        void startBTConnection(BluetoothDevice device, UUID uuid);
        void sendWriteBytes(EditText etSend);
        void startConnection();

        void initBluetoothAndAssignItemClicked(Context context, int position, ArrayList<BluetoothDevice> mBTDevices);


        void checkBTPermissions();
    }
}
