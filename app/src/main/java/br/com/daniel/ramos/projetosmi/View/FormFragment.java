package br.com.daniel.ramos.projetosmi.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import br.com.daniel.ramos.projetosmi.Presenter.FormPresenter;
import br.com.daniel.ramos.projetosmi.Presenter.PresenterMVP;
import br.com.daniel.ramos.projetosmi.R;
import io.realm.Realm;
import io.realm.RealmAsyncTask;

public class FormFragment extends Fragment implements  ViewMVP.FormView, View.OnClickListener{

    private static final String TAG = "FormFragment";


    private PresenterMVP.FormPresenter mPresenter;
    private Realm myRealm;
    private RealmAsyncTask realmTask;

    private ConstraintLayout mLayout;
    private TextView userTitleTv, monitoredTitleTv;
    private TextInputLayout nameUserTil, nameMonitoredTil, phoneMonitoredTil;
    private TextInputEditText nameUserTie, nameMonitoredTie, phoneMonitoredTie;
    private Button cancelBtn, nextBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeRealm();
    }

    public void initViews(View view) {

        mLayout = view.findViewById(R.id.contraint_layout);
        userTitleTv = view.findViewById(R.id.userTitleTv);
        monitoredTitleTv = view.findViewById(R.id.monitoredTitleTv);
        nameUserTil = view.findViewById(R.id.nameUserTil);
        nameMonitoredTil = view.findViewById(R.id.nameMonitoredTil);
        phoneMonitoredTil = view.findViewById(R.id.phoneMonitoredTil);

        nameUserTie = view.findViewById(R.id.nameUserTie);
        nameMonitoredTie = view.findViewById(R.id.nameMonitoredTie);
        phoneMonitoredTie = view.findViewById(R.id.phoneMonitoredTie);


        cancelBtn = view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(this);
        nextBtn = view.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(this);

        myRealm = Realm.getDefaultInstance();
        mPresenter = new FormPresenter(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelBtn:
                mPresenter.backDashboard();
                break;
            case R.id.nextBtn:
                mPresenter.saveNewUser(
                        nameUserTie.getText().toString(),
                        nameMonitoredTie.getText().toString(),
                        phoneMonitoredTie.getText().toString()
                );
                break;
        }
    }

    @Override
    public void nextView() {
        Log.d(TAG, "nextView: Called");
        Fragment bluetoothFrag = new BluetoothFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, bluetoothFrag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void cancelAction() {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment(), "dashboardFragment");
    }

    @Override
    public void setErrorForUserName(String msg) {
        nameUserTie.setError(msg);
    }

    @Override
    public void setErrorForPersonMonitoredName(String msg) {
        nameMonitoredTie.setError(msg);
    }

    @Override
    public void setErrorForPersonMonitoredPhone(String msg) {
        phoneMonitoredTie.setError(msg);
    }

    @Override
    public void showAddUserSuccess() {
        Log.d(TAG, "showAddUserSuccess: called");
        nextView();
    }

    @Override
    public void showAddUserError() {
        Snackbar.make(mLayout, R.string.add_user_error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void closeRealm() {
        mPresenter.closeRealm();
    }
}
