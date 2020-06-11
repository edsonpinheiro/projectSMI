package br.com.daniel.ramos.projetosmi.Presenter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import br.com.daniel.ramos.projetosmi.R;
import br.com.daniel.ramos.projetosmi.View.ViewMVP;

public class DashboardPresenter implements PresenterMVP.DashboardPresenter {

    private ViewMVP.DashboardView mView;

    public DashboardPresenter(ViewMVP.DashboardView view) {
        mView = view;
    }

    @Override
    public void replaceFragment(FragmentManager fm, Fragment fragment, String tag) {
        fm.beginTransaction().replace(R.id.fragment_container, fragment, tag).commit();
    }

    /*
    * The second point is more worrying. Imagine you send a request to a server that takes 10 seconds,
    * but the user closes the activity after 5 seconds. By the time the callback is called and the UI is updated, it will crash because the activity is finishing.
    * */
    // Esse método evita chamar a activity em um estado inconsistente
    public void onDestroy() {
        mView = null;
    }
}
