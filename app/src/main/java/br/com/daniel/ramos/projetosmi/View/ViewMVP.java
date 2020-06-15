package br.com.daniel.ramos.projetosmi.View;

import android.content.Intent;

import androidx.annotation.Nullable;

public interface ViewMVP {

    /* Representa a View no MVP para o DashboardFragment */
    interface DashboardView {}

    /* Representa a View no MVP para o BluetoothFragment */
    interface BluetoothView {
        void enableBT();
        void filterStatusBT();
    }

}
