package br.com.daniel.ramos.projetosmi.View;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

public interface ViewMVP {

    /* Representa a View no MVP para o DashboardFragment */
    interface DashboardView {}

    /* Representa a View no MVP para o BluetoothFragment */
    interface BluetoothView {
        void showToastMessageShort(String message);
        void showToastMessageLong(String message);

        void enableBT();
        void enableDisableDiscoverableBT();

        void filterStatusBT();
        void filterDiscoverableBT();
        void filterDiscoverUnpairedBT();
        void filterBondStateChange();

        void refreshListViewDevices(Context context, ArrayList<BluetoothDevice> devices);



    }

}
