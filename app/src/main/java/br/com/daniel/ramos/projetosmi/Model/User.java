package br.com.daniel.ramos.projetosmi.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class User extends RealmObject {

    @PrimaryKey
    @Required
    private String id;
    @Required
    private String nome;

    private PersonMonitored personMonitored;

    public User() {
    }

    public User(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public PersonMonitored getPersonMonitored() {
        return personMonitored;
    }

    public void setPersonMonitored(PersonMonitored personMonitored) {
        this.personMonitored = personMonitored;
    }
}
