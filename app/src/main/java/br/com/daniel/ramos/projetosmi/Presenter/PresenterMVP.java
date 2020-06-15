package br.com.daniel.ramos.projetosmi.Presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public interface PresenterMVP {

    /* Representa o Presenter no MVP para o DashboardFragment */
    interface DashboardPresenter {
        void replaceFragment(FragmentManager fm, Fragment fragment, String tag);
        void onDestroy();
    }

    /* Representa o Presenter no MVP para o BluetoothFragment */
    interface BluetoothPresenter {
        void enableDisableBT();
        void broadcastReceiver1(Context context, Intent intent);
    }
}
