package br.com.daniel.ramos.projetosmi.Configuration;

import android.app.Application;
import io.realm.RealmConfiguration;

import io.realm.Realm;

public class RealmConfig extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("projectsmi.realm").build();
        Realm.setDefaultConfiguration(configuration);
    }
}
