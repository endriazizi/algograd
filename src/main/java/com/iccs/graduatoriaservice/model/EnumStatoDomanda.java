package com.iccs.graduatoriaservice.model;

public enum EnumStatoDomanda {

    AMMESSO,
    IN_PENDING,
    //ammesso PER ORA perchè quando ciclo la seconda lista di preferenze potrebbe essercene uno migliore!
    //AMMESSO_MA_IN_LISTA_ATTESA,
    // non ammesso PER ORA perchè qualcuno potrebbe cambiare idea, togliersi l'iscrizione ed entrare
    //NON_AMMESSO_MA_IN_LISTA_ATTESA,
    LISTA_DI_ATTESA,
    //GIA_PRESO_IN_UN_ALTRA_UNIVERSITA,
    GIA_PRESO_IN_UN_ALTRO_ISTITUTO,
    LISTA_ANTICIPATARI,
    NON_AMMESSO_MA_IN_LISTA_ANTICIPATARIO

    /*
    tolgo quelli che nel csv hanno nella colonna esito == Lista anticipatari

    Scuola	   IdDomanda	Studente	         Stato	                  Posizione
               	29008	    MASSENSINI LIVIA	LISTA_ANTICIPATARI


    però
     */

}