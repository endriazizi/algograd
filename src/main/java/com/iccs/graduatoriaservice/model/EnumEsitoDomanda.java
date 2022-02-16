package com.iccs.graduatoriaservice.model;

public enum EnumEsitoDomanda {

    ESITO_AMMESSO("Pending", 1),
    ESITO_LISTA_ANTICIPATARIO("Lista anticipatari", 0);

    private String stringaEsitoNelCsv;
    private Integer peso;

    EnumEsitoDomanda(String stringaEsitoNelCsv, int peso) {
        this.stringaEsitoNelCsv = stringaEsitoNelCsv;
        this.peso = peso;
    }

    public String getStringaEsitoNelCsv() {
        return stringaEsitoNelCsv;
    }

    public Integer getPeso() {
        return peso;
    }

    public static EnumEsitoDomanda getEnumByString(String stringaEsitoNelCsv) {
        for (EnumEsitoDomanda enumVal : EnumEsitoDomanda.values()) {
            if (enumVal.getStringaEsitoNelCsv().equals(stringaEsitoNelCsv)) {
                return enumVal;
            }
        }

        return null;
    }
}
