package com.iccs.graduatoriaservice.model;

import java.util.Map;

public class Graduatoria {

    private Scuola scuola;
    private Map<Domanda, EnumStatoDomanda> mapDomande;

    public Graduatoria(Scuola scuola) {
        this.scuola = scuola;
    }

    public Graduatoria(Scuola scuola, Map<Domanda, EnumStatoDomanda> mapDomande) {
        this.scuola = scuola;
        this.mapDomande = mapDomande;
    }

    public Scuola getScuola() {
        return scuola;
    }

    public void setScuola(Scuola scuola) {
        this.scuola = scuola;
    }

    public Graduatoria(Map<Domanda, EnumStatoDomanda> mapDomande) {
        this.mapDomande = mapDomande;
    }

    public Map<Domanda, EnumStatoDomanda> getMapDomande() {
        return mapDomande;
    }

    public void setMapDomande(Map<Domanda, EnumStatoDomanda> mapDomande) {
        this.mapDomande = mapDomande;
    }
}

