package br.com.daniel.ramos.projetosmi.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import br.com.daniel.ramos.projetosmi.Presenter.DashboardPresenter;
import br.com.daniel.ramos.projetosmi.Presenter.PresenterMVP;
import br.com.daniel.ramos.projetosmi.R;

public class DashboardFragment extends Fragment implements ViewMVP.DashboardView, View.OnClickListener{

    private static final String TAG = "DashboardFragment";

    private CardView reportsCard, localizationCard, callCard, alarmCard, setupCard;
    private PresenterMVP.DashboardPresenter mPresenter;
    private FragmentManager fm;


    @Nullable
    @Override
    // Chamado quando a view está prestes a ser criada
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initViews(view);

        // Instancia o presenter
        mPresenter = new DashboardPresenter(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        fm = getFragmentManager();
        // TODO: Quando selecionado por meio dos botões não é atualizado o navigationDrawer, assim o fragment atual não é marcado
        switch (v.getId()){
            case R.id.dash_reportsID :
                mPresenter.replaceFragment(fm, new ReportsFragment(), "reportsFragment");
            break;
            case R.id.dash_localizationID :
                mPresenter.replaceFragment(fm, new LocalizationFragment(), "localizationFragment");
            break;
            case R.id.dash_callID :
                mPresenter.replaceFragment(fm, new CallFragment(), "callFragment");
            break;
            case R.id.dash_setupID:
                // TODO: Adicionar Breadcumb
                mPresenter.replaceFragment(fm, new FormFragment(), "setupFragment");
                break;
            case R.id.dash_alarmID :
                mPresenter.replaceFragment(fm, new AlertFragment(), "alertFragment");
            break;
        }
    }

    public void initViews(View view) {
        reportsCard = view.findViewById(R.id.dash_reportsID);
        reportsCard.setOnClickListener(this);
        localizationCard = view.findViewById(R.id.dash_localizationID);
        localizationCard.setOnClickListener(this);
        callCard = view.findViewById(R.id.dash_callID);
        callCard.setOnClickListener(this);
        alarmCard = view.findViewById(R.id.dash_alarmID);
        alarmCard.setOnClickListener(this);
        setupCard = view.findViewById(R.id.dash_setupID);
        setupCard.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}
