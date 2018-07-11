package br.edu.ifspsaocarlos.sdm.agendasdm.model;


import java.io.Serializable;

public class Contato implements Serializable{
    private long id;
    private String nome;
    private String fone;
    private String email;

    public Contato() {
        this.id = -1;
    }

    public Contato(long id, String nome, String fone, String email) {
        this.id = id;
        this.nome = nome;
        this.fone = fone;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
