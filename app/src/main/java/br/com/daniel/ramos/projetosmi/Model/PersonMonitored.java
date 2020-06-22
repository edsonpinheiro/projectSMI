package br.com.daniel.ramos.projetosmi.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class PersonMonitored extends RealmObject {

    @PrimaryKey
    @Required
    private String id;
    @Required
    private String nome;
    @Required
    private String telefone;

    public PersonMonitored(){}

    public PersonMonitored(String telefone) {
        this.telefone = telefone;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
