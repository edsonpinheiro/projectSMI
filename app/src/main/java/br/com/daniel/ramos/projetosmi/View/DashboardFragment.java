package br.com.daniel.ramos.projetosmi.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import br.com.daniel.ramos.projetosmi.R;

public class DashboardFragment extends Fragment implements View.OnClickListener{

    private CardView reportsCard, localizationCard, callCard, alarmCard, bluetoothCard;

    @Nullable
    @Override
    // Chamado quando a view está prestes a ser criada
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        reportsCard = view.findViewById(R.id.dash_reportsID);
        reportsCard.setOnClickListener(this);
        localizationCard = view.findViewById(R.id.dash_localizationID);
        localizationCard.setOnClickListener(this);
        callCard = view.findViewById(R.id.dash_callID);
        callCard.setOnClickListener(this);
        alarmCard = view.findViewById(R.id.dash_alarmID);
        alarmCard.setOnClickListener(this);
        bluetoothCard = view.findViewById(R.id.dash_bluetoothID);
        bluetoothCard.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
    // TODO: Quando selecionado por meio dos botões não é atualizado o navigationDrawer, assim o fragment atual não é marcado
        switch (v.getId()){
            case R.id.dash_reportsID :
                fm.beginTransaction().replace(R.id.fragment_container, new ReportsFragment(), "reportsFragment").commit();
            break;
            case R.id.dash_localizationID :
                fm.beginTransaction().replace(R.id.fragment_container, new LocalizationFragment(), "localizationFragment").commit();
            break;
            case R.id.dash_callID :
                fm.beginTransaction().replace(R.id.fragment_container, new CallFragment(), "callFragment").commit();
            break;
            case R.id.dash_bluetoothID :
                // TODO: Pensar na logica que será implementada aqui
            break;
            case R.id.dash_alarmID :
                fm.beginTransaction().replace(R.id.fragment_container, new AlertFragment(), "alertFragment").commit();
            break;
        }
    }

    public void initViews() {

    }
}
