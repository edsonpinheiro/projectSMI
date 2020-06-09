package br.com.daniel.ramos.projetosmi.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import br.com.daniel.ramos.projetosmi.R;

public class DashboardFragment extends Fragment {

    private CardView reportsCard, localizationCard, callCard, alarmCard, bluetoothCard;

    @Nullable
    @Override
    // Chamado quando a view está prestes a ser criada
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

}
