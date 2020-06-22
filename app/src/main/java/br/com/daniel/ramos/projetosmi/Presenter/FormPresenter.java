package br.com.daniel.ramos.projetosmi.Presenter;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import br.com.daniel.ramos.projetosmi.Model.User;
import br.com.daniel.ramos.projetosmi.Model.UserRepository;
import br.com.daniel.ramos.projetosmi.Utils.Utils;
import br.com.daniel.ramos.projetosmi.View.ViewMVP;
import io.realm.internal.Util;

public class FormPresenter implements PresenterMVP.FormPresenter, UserRepository.OnTransactionCallBack {
    private static final String TAG = "FormPresenter";

    // Presenter deve ter uma referencia ao UserRepository e a View


    private ViewMVP.FormView mView;
    private final UserRepository userRepository;

    public FormPresenter(ViewMVP.FormView view) {
        mView = view;
        userRepository = new UserRepository(this);
    }

    @Override
    public boolean verifyContent(final String nameUser, final String namePersonMonitored, final String phonePersonMonitored) {
        boolean tempFlag = true;
        //TODO: Fazer a validacao]
        if(Utils.isEmpty(nameUser)){
            mView.setErrorForUserName("Por favor insira um nome");
            tempFlag = false;
        }
        if(Utils.isEmpty(namePersonMonitored)) {
            mView.setErrorForPersonMonitoredName("Por favor insira um nome");
            tempFlag = false;
        }
        if(Utils.isEmpty(phonePersonMonitored)) {
            mView.setErrorForPersonMonitoredPhone("Por favor insira um telefone");
            tempFlag = false;
        }
        return tempFlag;
    }


    @Override
    public void saveNewUser(final String nameUser, final String namePersonMonitored, final String phonePersonMonitored) {
        if (verifyContent(nameUser, namePersonMonitored, phonePersonMonitored)) {
            // Se os dados forem validos
            userRepository.addUserAsync(nameUser, namePersonMonitored, phonePersonMonitored, this);
        }
    }

    @Override
    public void updateUser() {

    }

    @Override
    public void deleteUserFromReal() {

    }

    @Override
    public void backDashboard() {
        mView.cancelAction();
    }

    @Override
    public void onRealmSuccess() {
        // TODO: SHOW SUCCESS ADD
        Log.d(TAG, "onRealmSuccess: Called");
        mView.showAddUserSuccess();
    }

    @Override
    public void onRealmError(Throwable e) {
        // TODO: SHOW ERROR
        e.printStackTrace();
        mView.showAddUserSuccess();
    }

    @Override
    public void closeRealm() {
        userRepository.closeRealm();
    }
}
