package br.com.daniel.ramos.projetosmi.Model;

import android.util.Log;

import java.util.UUID;

import br.com.daniel.ramos.projetosmi.Presenter.FormPresenter;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmResults;

public class UserRepository {

    private final static String TAG = "UserRepository";

    private Realm mRealm;
    private FormPresenter mPresenter;
    private RealmAsyncTask realmTask;

    public UserRepository(FormPresenter mPresenter) {
        mRealm = Realm.getDefaultInstance();
        this.mPresenter = mPresenter;
    }

    public void closeRealm(){
        mRealm.close();
    }

    // Dados devem ser validados antes de chamar esse método
    public void addUserAsync(final String nameUser, final String namePersonMonitored, final String phonePersonMonitored,
                             final OnTransactionCallBack onTransactionCallBack) {
        Log.d(TAG, "addUserAsync: called");
        realmTask = mRealm.executeTransactionAsync(
                new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Log.d(TAG, "addUserAsync: executeTransactionAsync: Called");
                        User user = realm.createObject(User.class, UUID.randomUUID().toString());
                        user.setNome(nameUser);
                        PersonMonitored personMonitored = realm.createObject(PersonMonitored.class, UUID.randomUUID().toString());
                        personMonitored.setNome(namePersonMonitored);
                        personMonitored.setTelefone(phonePersonMonitored);
                        user.setPersonMonitored(personMonitored);
                    }
                },
                new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "addUserAsync: Realm.Transaction.OnSuccess: Called");
                        if (onTransactionCallBack != null) {
                            onTransactionCallBack.onRealmSuccess();
                        }
                    }
                },
                new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        if (onTransactionCallBack != null) {
                            onTransactionCallBack.onRealmError(error);
                        }
                    }
                }
        );
    }

    // Método chamado caso queira alterar nome ou telefone
    public void updateUser() {

    }

    //Método chamado caso o usuário queira deletar sua conta
    public void deleteUser() {

    }

    // Verificamos a consistencia dos dados passados
    public boolean verifyData() {

        return true;
    }

    public interface OnTransactionCallBack {
        void onRealmSuccess();
        void onRealmError(final Throwable e);
    }



}
