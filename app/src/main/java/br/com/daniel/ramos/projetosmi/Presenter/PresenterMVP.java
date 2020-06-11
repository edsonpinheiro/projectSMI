package br.com.daniel.ramos.projetosmi.Presenter;

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

    }
}
