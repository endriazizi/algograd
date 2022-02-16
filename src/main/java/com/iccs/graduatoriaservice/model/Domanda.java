package com.iccs.graduatoriaservice.model;
public class Domanda {

    private String nomePersona;
    private int idDomanda;

    // list domande graduatorie
    // per ogni domanda, noi abbiamo un esito perchè consideriamo ogni domanda come un oggetto duplicato per ogni graduatoria
    // TODO sarebbe bello se invece di avere una variabile esito, avremmo un esito di prima scelta e un esito di seconda scelta
    // in modo da evitare di duplicare gli oggetti
    /*
    List<DomandeGrad> 0-1
    DomandeGrad {
        scelta,
        punteggio,
        esito,
        EnumSceltaDomanda
    }
     */
    // prima scelta
    // putenggio scelta
    // esito scelta
    private Scuola primaScelta;
    private Scuola secondaScelta;
    private int punteggioPrimaScelta;
    private int punteggioSecondaScelta;

    private EnumEsitoDomanda esito;
    private EnumSceltaDomanda scelta;

    public Domanda(String nomePersona, int idDomanda, Scuola primaScelta, Scuola secondaScelta, int punteggioPrimaScelta, int punteggioSecondaScelta, String esito) {
        this.nomePersona = nomePersona;
        this.idDomanda = idDomanda;
        this.primaScelta = primaScelta;
        this.secondaScelta = secondaScelta;
        this.punteggioPrimaScelta = punteggioPrimaScelta;
        this.punteggioSecondaScelta = punteggioSecondaScelta;
        this.esito = EnumEsitoDomanda.getEnumByString(esito);
    }

    // deep copy
    public Domanda(Domanda otherDomanda) {
        this.nomePersona = otherDomanda.getNomePersona();
        this.idDomanda = otherDomanda.getIdDomanda();
        this.primaScelta = otherDomanda.getPrimaScelta();
        this.secondaScelta = otherDomanda.getSecondaScelta();
        this.punteggioPrimaScelta = otherDomanda.getPunteggioPrimaScelta();
        this.punteggioSecondaScelta = otherDomanda.getPunteggioSecondaScelta();
        this.esito = otherDomanda.getEsito();
    }

    public String getNomePersona() {
        return nomePersona;
    }

    public void setNomePersona(String nomePersona) {
        this.nomePersona = nomePersona;
    }

    public Integer getIdDomanda() {
        return idDomanda;
    }

    public void setIdDomanda(int idDomanda) {
        this.idDomanda = idDomanda;
    }

    public Scuola getPrimaScelta() {
        return primaScelta;
    }

    public void setPrimaScelta(Scuola primaScelta) {
        this.primaScelta = primaScelta;
    }

    public Scuola getSecondaScelta() {
        return secondaScelta;
    }

    public void setSecondaScelta(Scuola secondaScelta) {
        this.secondaScelta = secondaScelta;
    }

    public int getPunteggioPrimaScelta() {
        return punteggioPrimaScelta;
    }

    public void setPunteggioPrimaScelta(int punteggioPrimaScelta) {
        this.punteggioPrimaScelta = punteggioPrimaScelta;
    }

    public int getPunteggioSecondaScelta() {
        return punteggioSecondaScelta;
    }

    public void setPunteggioSecondaScelta(int punteggioSecondaScelta) {
        this.punteggioSecondaScelta = punteggioSecondaScelta;
    }

    public EnumEsitoDomanda getEsito() {
        return esito;
    }

    public void setEsito(EnumEsitoDomanda esito) {
        this.esito = esito;
    }

    public EnumSceltaDomanda getScelta() {
        return scelta;
    }

    public void setScelta(EnumSceltaDomanda scelta) {
        this.scelta = scelta;
    }

    @Override
    public String toString() {
        return "Domanda{" +
                "nomePersona='" + nomePersona + '\'' +
                ", idDomanda=" + idDomanda +
                ", primaScelta=" + primaScelta +
                ", secondaScelta=" + secondaScelta +
                ", punteggioPrimaScelta=" + punteggioPrimaScelta +
                ", punteggioSecondaScelta=" + punteggioSecondaScelta +
                ", esito=" + esito +
                '}';
    }

}
