package com.iccs.graduatoriaservice.model;

import java.util.UUID;

public class Scuola {

    private int idScuola;
    private String nomeScuola;
    private int postiDisponibili;
    private Graduatoria graduatoria;

    public Scuola(int idScuola, String nomeScuola, int postiDisponibili) {
        this.idScuola = idScuola;
        this.nomeScuola = nomeScuola;
        this.postiDisponibili = postiDisponibili;
    }

    public Scuola(int idScuola, String nomeScuola, int postiDisponibili, Graduatoria graduatoria) {
        this.idScuola = idScuola;
        this.nomeScuola = nomeScuola;
        this.postiDisponibili = postiDisponibili;
        this.graduatoria = graduatoria;
    }

    public String getNomeScuola() {
        return nomeScuola;
    }

    public void setNomeScuola(String nomeScuola) {
        this.nomeScuola = nomeScuola;
    }

    public int getIdScuola() {
        return idScuola;
    }

    public void setIdScuola(int idScuola) {
        this.idScuola = idScuola;
    }

    public int getPostiDisponibili() {
        return postiDisponibili;
    }

    public void setPostiDisponibili(int postiDisponibili) {
        this.postiDisponibili = postiDisponibili;
    }

    public Graduatoria getGraduatoria() {
        return graduatoria;
    }

    public void setGraduatoria(Graduatoria graduatoria) {
        this.graduatoria = graduatoria;
    }
}
