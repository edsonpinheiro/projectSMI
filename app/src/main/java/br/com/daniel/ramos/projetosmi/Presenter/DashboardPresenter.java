package br.com.daniel.ramos.projetosmi.Presenter;

import br.com.daniel.ramos.projetosmi.View.ViewMVP;

public class DashboardPresenter implements PresenterMVP.DashboardPresenter {

    private ViewMVP.DashboardView mView;

    public DashboardPresenter(ViewMVP.DashboardView view) {
        mView = view;
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
