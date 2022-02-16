package com.iccs.graduatoriaservice.utils;

import com.iccs.graduatoriaservice.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class AlgoritmoGraduatoria {


    public static List<String> avviaAlgoritmo(  List<Domanda> listDomande) {

        List<String> graduatoriaFinale = new ArrayList<>();
        // costruire struttura base
        List<Scuola> listScuole = initScuole();

        List<Graduatoria> listGraduatorie = initGraduatorieTest(listScuole);

//        // List<Domanda> listDomande = initDomandeTest(listScuole);
//        CSVReaderParser csvReaderParser = new CSVReaderParser();
//
//        // List<Domanda> listDomande =csvReaderParser.parser();
//        List<Domanda> listDomande = CSVReaderParser.parser();


        // Set<UUID> listSecondaSceltaPerchePrimaSceltaNonPassate = new HashSet<>();
        Set<Integer> listSecondaSceltaPerchePrimaSceltaNonPassate = new HashSet<>();
        Set<Integer> ammessiInSecondaScelta = new HashSet<>();

        Set<Integer> listPrimaSceltaListaAttesa = new HashSet<>();

        Map<Domanda, EnumStatoDomanda> mapGraduatoria = new HashMap<>();

        Map<Domanda, EnumStatoDomanda> mapGraduatoriaCopy = new HashMap<>();

        // PREPARAZIONE per ogni lista graduatoria e per ogni listDomanda asocio mapDomandaStato.put(domanda, EnumStatoDomanda.IN_PENDING);
        preaprazioneMappaDomandStatoConStatoInizialeInPending(listScuole, listGraduatorie, listDomande);

        Map<String, String> map = new HashMap<>();
        map.put("1", "Jan");
        map.put("2", "Feb");
        map.put("3", "Mar");
        map.put("4", "Apr");
        map.put("5", "May");
        map.put("6", "Jun");

        /**
         * N = numero scuole
         * M = numero di domande
         *
         * Risposta di tutte le prime scelte
         * NxM
         * for scuola
         * 	for domande : graduatoria
         * 		filter prima scelta
         * 			if(numero di domande permesse)
         * 			graduatoria -> domande -> stato (passato o meno)
         */
        listScuole.forEach(

                scuola -> {

                    if (scuola.getNomeScuola().equals("La Grande quercia di Via Leoncavallo")) {
                        System.out.println("FOR per la scuola: La Grande quercia di Via Leoncavallo");
                    }
//                    if(scuola.getNomeScuola().equals("Poi poi di Via Ferraris")){
//                        System.out.println("FOR per la scuola: Poi poi di Via Ferraris");
//                    }

                    Graduatoria grad = scuola.getGraduatoria();

                    Map<Domanda, EnumStatoDomanda> mapDomandaStato = grad.getMapDomande();

                    int postiDisp = scuola.getPostiDisponibili();

                    // FILTRO SCUOLA e PER DOMANDE e PER PRIMA SCELTA E LE METTO DENTRO LA LISTA listDomandePrimaScelta
                    List<Domanda> listDomandePrimaScelta = mapDomandaStato.keySet().stream().filter(
                            domanda -> (
                                    (domanda.getPrimaScelta().getIdScuola() == (scuola.getIdScuola()))
                                            &&
                                            (domanda.getEsito().getStringaEsitoNelCsv().equals("Pending"))
//                                                            (domanda.getEsito().getStringaEsitoNelCsv().equals(EnumEsitoDomanda.ESITO_AMMESSO))
                            )).collect(Collectors.toList());


                    // ORDINO LE DOMANDE listDomandePrimaScelta e VERIFICA I DOPPIONI
                    ordinamentoDelleDomandePerListDomandePrimaSceltaEVerificaIparimerito(scuola, listDomandePrimaScelta);


                    //
                    List<Domanda> listDomandaChePerPrimaSceltaRisultanoPassati = listDomandePrimaScelta.subList(
                            0, (listDomandePrimaScelta.size() < postiDisp) ? listDomandePrimaScelta.size() : postiDisp);


                    for (Map.Entry<Domanda, EnumStatoDomanda> domandaStato : mapDomandaStato.entrySet()) {

                        boolean a = domandaStato.getKey().getEsito().getStringaEsitoNelCsv().equals("Pending");
                        //COTTT
                        if (domandaStato.getKey().getPrimaScelta().getNomeScuola().equals("La Grande quercia di Via Leoncavallo")) {
//                            System.out.println("SONO QUI");
                        }
                        boolean scuolaTest = domandaStato.getKey().getPrimaScelta().getNomeScuola().equals("La Grande quercia di Via Leoncavallo");

                        //System.out.println(a);
                        if (listDomandaChePerPrimaSceltaRisultanoPassati.contains(domandaStato.getKey()) && domandaStato.getKey().getEsito().getStringaEsitoNelCsv().equals("Pending")) {
                            domandaStato.setValue(EnumStatoDomanda.AMMESSO);

                        } else {
                            if (listDomandePrimaScelta.contains(domandaStato.getKey()) && domandaStato.getKey().getEsito().getStringaEsitoNelCsv().equals("Pending")) {
                                domandaStato.setValue(EnumStatoDomanda.LISTA_DI_ATTESA);
                                listSecondaSceltaPerchePrimaSceltaNonPassate.add(domandaStato.getKey().getIdDomanda());
                            } else {
                                if (listDomandePrimaScelta.contains(domandaStato.getKey()) && domandaStato.getKey().getEsito().getStringaEsitoNelCsv().equals("Lista anticipatari")) {

                                    boolean b = domandaStato.getKey().getEsito().getStringaEsitoNelCsv().equals("Lista anticipatari");
                                    //System.out.println(b);

                                    domandaStato.setValue(EnumStatoDomanda.NON_AMMESSO_MA_IN_LISTA_ANTICIPATARIO);

                                    listSecondaSceltaPerchePrimaSceltaNonPassate.add(domandaStato.getKey().getIdDomanda());
                                } else {
                                    domandaStato.setValue(EnumStatoDomanda.GIA_PRESO_IN_UN_ALTRO_ISTITUTO);
                                }

                            }
                        }
                    }
                }); // FINE  listScuole.forEach - Risposta di tutte le prime scelte

        do {
            algoritmoDiCalcoloScelteGraduatorie(listScuole, listDomande, listSecondaSceltaPerchePrimaSceltaNonPassate);
        } while (!listSecondaSceltaPerchePrimaSceltaNonPassate.isEmpty());


        for (Scuola scuola : listScuole) {

            //---------------------------- ORDINAMENTO ANTICIPATARIO
            mapGraduatoria = scuola.getGraduatoria().getMapDomande().entrySet().stream().sorted((o1, o2) -> {
                int resultPrimoConfrontoDaPunteggio = -(new Integer((o1.getKey().getPrimaScelta().getIdScuola() == (scuola.getIdScuola())) ? o1.getKey().getPunteggioPrimaScelta() : o1.getKey().getPunteggioSecondaScelta()).compareTo(new Integer((o2.getKey().getPrimaScelta().getIdScuola() == (scuola.getIdScuola())) ? o2.getKey().getPunteggioPrimaScelta() : o2.getKey().getPunteggioSecondaScelta())));

                // o1 = ammesso // in pending
                // o2 = anticipatario // anticipatario
                // ammesso > anticipatario

                // o1 = anticipatario // anticipatario
                // o2 = ammesso // in pending
                // ammesso > anticipatario

                // o1 = ammesso // in pending
                // o2 = ammesso // in pending
                // ammesso > anticipatario

                // o1 = anticipatario // anticipatario
                // o2 = anticipatario // anticipatario
                // ammesso > anticipatario


                resultPrimoConfrontoDaPunteggio = (o1.getKey().getEsito().equals(o2.getKey().getEsito())) ? resultPrimoConfrontoDaPunteggio : -(o1.getKey().getEsito().getPeso().compareTo(o2.getKey().getEsito().getPeso()));


                if (resultPrimoConfrontoDaPunteggio == 0) {
                    return (o1.getKey().getNomePersona().compareTo(o2.getKey().getNomePersona()));
                } else {
                }
                return resultPrimoConfrontoDaPunteggio;
            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));


            //------------------------------ GESTIONE STATO AMMESSO IN ALTRA SCUOLA
            /*
            per ogni domanda, devo controllare che se sono stato preso nella prima scelta, la seconda scelta deve diventare già preso
            per ogni domanda, devo controllare che se sono stato preso nella seconda scelta, E non sono stato preso nella prima scelta
                allora devo settare la prima scelta come già preso (invece che non ammesso)
             */

            mapGraduatoria.entrySet().forEach(
                    entryDomandaEsito -> {
                        Integer idDomanda = entryDomandaEsito.getKey().getIdDomanda();

                        Map.Entry<Domanda, EnumStatoDomanda> domandaPrima = null;
                        Map.Entry<Domanda, EnumStatoDomanda> domandaSeconda = null;

                        if (entryDomandaEsito.getKey().getScelta() == EnumSceltaDomanda.PRIMA_SCELTA) {
                            domandaPrima = entryDomandaEsito;
                            // cerca seconda scelta
                            for (Scuola scuolaIndice : listScuole) {
                                Map<Domanda, EnumStatoDomanda> mappaDomande = scuolaIndice.getGraduatoria().getMapDomande();
                                for (Map.Entry<Domanda, EnumStatoDomanda> domandaIndice : mappaDomande.entrySet()) {
                                    if (domandaIndice.getKey().getIdDomanda().equals(idDomanda)
                                            && domandaIndice.getKey().getScelta().equals(EnumSceltaDomanda.SECONDA_SCELTA)) {
                                        domandaSeconda = domandaIndice;
                                    }
                                }
                            }
                        } else {
                            domandaSeconda = entryDomandaEsito;
                            // cerca prima scelta
                            for (Scuola scuolaIndice : listScuole) {
                                Map<Domanda, EnumStatoDomanda> mappaDomande = scuolaIndice.getGraduatoria().getMapDomande();
                                for (Map.Entry<Domanda, EnumStatoDomanda> domandaIndice : mappaDomande.entrySet()) {
                                    if (domandaIndice.getKey().getIdDomanda().equals(idDomanda)
                                            && domandaIndice.getKey().getScelta().equals(EnumSceltaDomanda.PRIMA_SCELTA)) {
                                        domandaPrima = domandaIndice;
                                    }
                                }
                            }
                        }

                        if (domandaPrima.getValue().equals(EnumStatoDomanda.AMMESSO)) {
                            domandaSeconda.setValue(EnumStatoDomanda.GIA_PRESO_IN_UN_ALTRO_ISTITUTO);
                        } else if (domandaSeconda.getValue().equals(EnumStatoDomanda.AMMESSO)) {
                            domandaPrima.setValue(EnumStatoDomanda.GIA_PRESO_IN_UN_ALTRO_ISTITUTO);
                        }
                    }
            );

            //////////
/*

            mapGraduatoriaCopy = new HashMap<>();
            mapGraduatoriaCopy.putAll(mapGraduatoria);

            // STAMPA DATI
//            System.out.println("Graduatoria");


            // Get the iterator over the HashMap
//            Iterator<Map.Entry<Domanda, EnumStatoDomanda>> iterator = mapGraduatoria.entrySet().iterator();
//
//            // Iterate over the HashMap
//            while (iterator.hasNext()) {
//
//                // Get the entry at this iteration
//                Map.Entry<Domanda, EnumStatoDomanda> entry = iterator.next();
//
//                // Check if this value is the required value
//                if (entry.getValue().equals("GIA_PRESO_IN_UN_ALTRA_UNIVERSITA")) {
//                    System.out.println(entry.getValue());
//                    System.out.println("TROVATO");
//                    // Remove this entry from HashMap
//                    iterator.remove();
//                }
//            }


            // removing all entries less-than 120 matches
            mapGraduatoria.entrySet().removeIf(matches -> matches.getValue().equals("GIA_PRESO_IN_UN_ALTRA_UNIVERSITA"));

            // domanda.getValue().equals(EnumStatoDomanda.GIA_PRESO_IN_UN_ALTRO_ISTITUTO))
            Map<Domanda, EnumStatoDomanda> collect = mapGraduatoriaCopy.entrySet().stream()
                    .filter(domanda -> {
                        boolean a = domanda.getValue().equals(EnumStatoDomanda.AMMESSO);
//                        System.out.println("LOL"+a);
                        if (domanda.getValue().equals(EnumStatoDomanda.AMMESSO)
                                && domanda.getKey().getSecondaScelta().getNomeScuola().equals(scuola.getNomeScuola())) {
                            //listSecondaSceltaPerchePrimaSceltaNonPassate.add(domandaStato.getKey().getIdDomanda());
                            System.out.println("ZIAMARA " + domanda.getKey().getNomePersona());
                            return true;
                        }
                        return false;
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

*/

            // Standard classic way, recommend!
//            System.out.println("\nExample 1...");
//            for (Map.Entry<String, String> entry : map.entrySet()) {
//                System.out.println("Mese: " + scuola + " Key : " + entry.getKey() + " Value : " + entry.getValue());
//            }

            int posizione = 0;
            for (Map.Entry<Domanda, EnumStatoDomanda> entry : mapGraduatoria.entrySet()) {

                if (mapGraduatoriaCopy.containsKey(entry)) {
                    //                 System.out.println("YUPPY" + " " + entry.getKey().getNomePersona());
                }

                /*boolean a = entry.getValue().equals(EnumStatoDomanda.AMMESSO);
//                        System.out.println("LOL"+a);
                if (entry.getValue().equals(EnumStatoDomanda.AMMESSO)
                        && entry.getKey().getSecondaScelta().getNomeScuola().equals(scuola.getNomeScuola())) {
                    ammessiInSecondaScelta.add(entry.getKey().getIdDomanda());

//                    ammessiInSecondaScelta.contains(entry.getKey().getIdDomanda())

//                    System.out.println("ZIAMARA " + " " + entry.getKey().getNomePersona());
                }*/

//                if (entry.getValue().equals("GIA_PRESO_IN_UN_ALTRA_UNIVERSITA")) {
//                    System.out.println(entry.getValue());
//                    System.out.println("TROVATO");
//                    // Remove this entry from HashMap
//                    iterator.remove();
//                }

//                System.out.println("Domanda "+ entry.getKey().getNomePersona() + "- stato : " + entry.getValue() + " - posizione : "+ counterMap);


                if (!entry.getValue().equals(EnumStatoDomanda.GIA_PRESO_IN_UN_ALTRO_ISTITUTO)) {
                    posizione++;
                    String record =                             scuola.getNomeScuola().toString() + ";"
                            + entry.getKey().getIdDomanda() + ";"
                            + entry.getKey().getNomePersona() + ";"
                            + entry.getValue() + ";"
                            + posizione + ";"
                            + entry.getKey().getEsito() + ";"
                            //////// AGGIUNTO PER VERIFICARE IL PUNTEGGIO PRESO
                            + (entry.getKey().getPrimaScelta().getIdScuola() == (scuola.getIdScuola()) ?
                            entry.getKey().getPunteggioPrimaScelta() : entry.getKey().getPunteggioSecondaScelta()) + ";"
                            + entry.getKey().getScelta() + "\n";

                    System.out.print(record);

                    graduatoriaFinale.add(record);
                }
//                }


            }


        }

//        for (Scuola scuola:
//                listScuole) {
//            // Standard classic way, recommend!
////            System.out.println("\nExample 1...");
//            for (Map.Entry<Domanda, EnumStatoDomanda> entry : mapGraduatoria.entrySet()) {
//                if (mapGraduatoriaCopy.containsKey(entry)) {
//                    System.out.println("YUPPY" + " " + entry.getKey().getNomePersona());
//
//                }
//                if(ammessiInSecondaScelta.contains(entry.getKey().getIdDomanda())){
//                    entry.setValue((EnumStatoDomanda.GIA_PRESO_IN_UN_ALTRO_ISTITUTO));
//                }
//            }
//        }


        return graduatoriaFinale;
    }


    // METODI STATICI
    private static void preaprazioneMappaDomandStatoConStatoInizialeInPending(List<Scuola> listScuole,
                                                                              List<Graduatoria> listGraduatorie,
                                                                              List<Domanda> listDomande) {
        listGraduatorie.stream().forEach(
                // operazione
                graduatoria -> {
                    Map<Domanda, EnumStatoDomanda> mapDomandaStato = new HashMap<>();
                    listDomande.forEach(domanda -> {

                        if (domanda.getPrimaScelta().getIdScuola() == (domanda.getSecondaScelta().getIdScuola())) {
                            domanda.setSecondaScelta(listScuole.get(29));//UNDEFINED???
                        }

                        Domanda domandaToInsert = new Domanda(domanda);
                        if (domandaToInsert.getPrimaScelta().getIdScuola() == (graduatoria.getScuola().getIdScuola())) {
                            domandaToInsert.setScelta(EnumSceltaDomanda.PRIMA_SCELTA);
                        } else if (domandaToInsert.getSecondaScelta().getIdScuola() == (graduatoria.getScuola().getIdScuola())) {
                            domandaToInsert.setScelta(EnumSceltaDomanda.SECONDA_SCELTA);
                        }

                        if (
                            // controllare che la domanda non sia vuota
                            // se la domanda include prima scuola oppure se la domanda include seconda scuola
                                ((domandaToInsert.getPrimaScelta().getIdScuola() == (graduatoria.getScuola().getIdScuola())) ||
                                        (domandaToInsert.getSecondaScelta().getIdScuola() == (graduatoria.getScuola().getIdScuola())))) {
                            mapDomandaStato.put(domandaToInsert, EnumStatoDomanda.IN_PENDING);
                        }
                    });

                    graduatoria.setMapDomande(mapDomandaStato);
                });
    }

    private static void ordinamentoDelleDomandePerListDomandePrimaSceltaEVerificaIparimerito(Scuola scuola, List<Domanda> listDomandePrimaScelta) {
        listDomandePrimaScelta.sort(new Comparator<Domanda>() {
            @Override
            public int compare(Domanda o1, Domanda o2) {
                // - ORDINE DECRESCENTE
                int resultPrimoConfrontoDaPunteggio = -(new Integer((o1.getPrimaScelta().getIdScuola() == (scuola.getIdScuola())) ? o1.getPunteggioPrimaScelta() : o1.getPunteggioSecondaScelta()).compareTo(new Integer((o2.getPrimaScelta().getIdScuola() == (scuola.getIdScuola())) ? o2.getPunteggioPrimaScelta() : o2.getPunteggioSecondaScelta())));

                // ==0 se il confronto ho due parimerito prendo la persono con in orfine alfabetico
                //crescente infatti non c'è il - che mi idica decrescente
                if (resultPrimoConfrontoDaPunteggio == 0) {
                    return (o1.getNomePersona().compareTo(o2.getNomePersona()));
                } else {
                }
                return resultPrimoConfrontoDaPunteggio;
            }
        });
    }

    private static void algoritmoDiCalcoloScelteGraduatorie(List<Scuola> listScuole, List<Domanda> listDomande,
//            Set<UUID> listSecondaSceltaPerchePrimaSceltaNonPassate)
                                                            Set<Integer> listSecondaSceltaPerchePrimaSceltaNonPassate) {

        /// SI OK MA ORA DOBBIAMO CALCOLARE TRAMITE ALGORITMO


        /**
         * Risposta di tutte le secondo scelte
         * N x (M - persone già ammesse)
         * for scuola
         * 	for domande : graduatori
         * 		filter seconda scelta
         * 			if(numero di domande permesse)
         * 			graduatori -> domande -> stato (passato o meno)
         */
        listScuole.forEach(

                scuola -> {

                    Graduatoria grad = scuola.getGraduatoria();

                    Map<Domanda, EnumStatoDomanda> mapDomandaStato = grad.getMapDomande();
                    int postiDisp = scuola.getPostiDisponibili();

                    List<Domanda> listDomandePrimaSceltaAndListDOmandeSecondaSceltaCheNonSonoStatePreseNellaPrimaPulizia = mapDomandaStato.keySet().stream().filter(

                            domanda -> (
                                    // prima scelta che sono ammessi ma in lista
                                    domanda.getPrimaScelta().getIdScuola() == (scuola.getIdScuola())
//                                                            && domanda.getEsito().getStringaEsitoNelCsv().equals("Pending")
                                            && mapDomandaStato.get(domanda).equals(EnumStatoDomanda.AMMESSO)
                                    //domandaStato.getPrimaScelta();
                            ) ||

                                    (
                                            // seconda scelta che non sono stati ammessi in prima scelta MA potrebbero essere ammessi in seconda!
                                            domanda.getSecondaScelta().getIdScuola() == (scuola.getIdScuola())
                                                    //&& domanda.getEsito().equals("Pending")
                                                    && listSecondaSceltaPerchePrimaSceltaNonPassate.contains(domanda.getIdDomanda())
                                            //domandaStato.getPrimaScelta();
                                    ) || (domanda.getSecondaScelta().getIdScuola() == (scuola.getIdScuola())
                                    //&& domanda.getEsito().equals("Pending")
                                    && mapDomandaStato.get(domanda).equals(EnumStatoDomanda.AMMESSO)
                                    //domandaStato.getPrimaScelta();

                            )).collect(Collectors.toList());


                    listDomandePrimaSceltaAndListDOmandeSecondaSceltaCheNonSonoStatePreseNellaPrimaPulizia.sort(new Comparator<Domanda>() {
                        @Override
                        public int compare(Domanda o1, Domanda o2) {
                            int resultPrimoConfrontoDaPunteggio = -(new Integer((o1.getPrimaScelta().getIdScuola() == (scuola.getIdScuola())) ? o1.getPunteggioPrimaScelta() : o1.getPunteggioSecondaScelta()).compareTo(new Integer((o2.getPrimaScelta().getIdScuola() == (scuola.getIdScuola())) ? o2.getPunteggioPrimaScelta() : o2.getPunteggioSecondaScelta())));


                            // confronto ISEE
                            /*resultPrimoConfrontoDaPunteggio =
                                    (o1.getIsee().equals(o2.getIsee())) ?
                                            resultPrimoConfrontoDaPunteggio :
                                            -(o1.getIsee().compareTo(o2.getIsee()));
                            */

                            // o1 = ammesso // in pending
                            // o2 = anticipatario // anticipatario
                            // ammesso > anticipatario

                            // o1 = anticipatario // anticipatario
                            // o2 = ammesso // in pending
                            // ammesso > anticipatario

                            // o1 = ammesso // in pending
                            // o2 = ammesso // in pending
                            // ammesso > anticipatario

                            // o1 = anticipatario // anticipatario
                            // o2 = anticipatario // anticipatario
                            // ammesso > anticipatario


                            resultPrimoConfrontoDaPunteggio = (o1.getEsito().equals(o2.getEsito())) ? resultPrimoConfrontoDaPunteggio : -(o1.getEsito().getPeso().compareTo(o2.getEsito().getPeso()));


                            if (resultPrimoConfrontoDaPunteggio == 0) {
                                return (o1.getNomePersona().compareTo(o2.getNomePersona()));
                            } else {
                            }
                            return resultPrimoConfrontoDaPunteggio;
                        }
                    });

                    List<Domanda> listDomandeAmmessi = listDomandePrimaSceltaAndListDOmandeSecondaSceltaCheNonSonoStatePreseNellaPrimaPulizia.subList(0, (listDomandePrimaSceltaAndListDOmandeSecondaSceltaCheNonSonoStatePreseNellaPrimaPulizia.size() < postiDisp) ? listDomandePrimaSceltaAndListDOmandeSecondaSceltaCheNonSonoStatePreseNellaPrimaPulizia.size() : postiDisp);

                    for (Map.Entry<Domanda, EnumStatoDomanda> domandaStato : mapDomandaStato.entrySet()) {

                        if (domandaStato.getKey().getNomePersona().equals("IANZANO EMILY")) {
//                            System.out.println("DAJEE" + scuola.getNomeScuola());
                        }
                        //QUI
                        if (listDomandeAmmessi.contains(domandaStato.getKey()) && domandaStato.getKey().getEsito().getStringaEsitoNelCsv().equals("Pending")) {

                            domandaStato.setValue(EnumStatoDomanda.AMMESSO);
                            listSecondaSceltaPerchePrimaSceltaNonPassate.remove(domandaStato.getKey().getIdDomanda());

                        } else {
                            if (listDomandeAmmessi.contains(domandaStato.getKey()) && domandaStato.getKey().getEsito().getStringaEsitoNelCsv().equals("Lista anticipatari")) {
                                domandaStato.setValue(EnumStatoDomanda.NON_AMMESSO_MA_IN_LISTA_ANTICIPATARIO);
                                listSecondaSceltaPerchePrimaSceltaNonPassate.remove(domandaStato.getKey().getIdDomanda());

                            } else {
                                if (listDomandePrimaSceltaAndListDOmandeSecondaSceltaCheNonSonoStatePreseNellaPrimaPulizia.contains(domandaStato.getKey())) {
                                    domandaStato.setValue(EnumStatoDomanda.LISTA_DI_ATTESA);
                                    if (domandaStato.getKey().getPrimaScelta().getIdScuola() == (scuola.getIdScuola())) {
//                                    listSecondaSceltaPerchePrimaSceltaNonPassate.add(domandaStato.getKey().getIdDomanda());
                                        listSecondaSceltaPerchePrimaSceltaNonPassate.add(domandaStato.getKey().getIdDomanda());
                                    } else {
                                        listSecondaSceltaPerchePrimaSceltaNonPassate.remove(domandaStato.getKey().getIdDomanda());
                                    }

                                }
                            }
//                            if(domandaStato.getValue().equals(EnumStatoDomanda.LISTA_DI_ATTESA)){
//                                domandaStato.setValue(EnumStatoDomanda.GIA_PRESO_IN_UN_ALTRO_ISTITUTO);
//                            }

                        }
                    }


                    /*System.out.println("FINE SECONDI GIRO");
                    System.out.println("Scuola :"+ scuola.getNomeScuola().toString());
                    System.out.println("Graduatoria");
                    for( Map.Entry<Domanda, EnumStatoDomanda> entry: scuola.getGraduatoria().getMapDomande().entrySet()) {
                        System.out.println("Domanda "+ entry.getKey().getNomePersona() + "- stato : " + entry.getValue());
                    }*/
                });
        System.out.println("----------------------------------------------------------------------------");
    }


    private static List<Scuola> initScuole() {
//        Scuola scuolaMilano = new Scuola(UUID.randomUUID(), "Milano", 2);
//        Scuola scuolaTorino = new Scuola(UUID.randomUUID(),"Torino", 2);
//        Scuola scuolaVenezia = new Scuola(UUID.randomUUID(), "Venezia",1);

        Scuola scuolaMilano0 = new Scuola(0, "Alighieri - Skarabocchio", 40);
        Scuola scuolaMilano1 = new Scuola(1, "Ambaraba'", 15);
        Scuola scuolaMilano2 = new Scuola(2, "Cappuccetto rosso", 9);
        Scuola scuolaMilano3 = new Scuola(3, "D.Alighieri Il Giardino delle Meraviglie  Vismara", 35);
        Scuola scuolaMilano4 = new Scuola(4, "D.Alighieri Mongolfiera S.Maria Fabbrecce ", 23);
        Scuola scuolaMilano5 = new Scuola(5, "Filo rosso", 21);
        Scuola scuolaMilano6 = new Scuola(6, "G. Galilei - Alice", 19);
        Scuola scuolaMilano7 = new Scuola(7, "G. Galilei - Il bosco incantato", 23);
        Scuola scuolaMilano8 = new Scuola(8, "G.Galilei Tresei B.go S.Maria ", 33);
        Scuola scuolaMilano9 = new Scuola(9, "G.GalileiPollicino Casebruciate ", 11);
        Scuola scuolaMilano10 = new Scuola(10, "Gaudiano - Mille colori", 7);
        Scuola scuolaMilano11 = new Scuola(11, "Grillo parlante", 8);
        Scuola scuolaMilano12 = new Scuola(12, "Gulliver di Via Flaminia", 28);
        Scuola scuolaMilano13 = new Scuola(13, "I Tre giardini sez primavera", 18);
        Scuola scuolaMilano14 = new Scuola(14, "Il Giardino fantastico di Via Madonna di Loreto", 8);
        Scuola scuolaMilano15 = new Scuola(15, "La giostra", 28);
        Scuola scuolaMilano16 = new Scuola(16, "La Grande quercia di Via Leoncavallo", 31);
        Scuola scuolaMilano17 = new Scuola(17, "Leopardi - Via Bonali", 22);
        Scuola scuolaMilano18 = new Scuola(18, "Leopardi - Via Fermi", 22);
        Scuola scuolaMilano19 = new Scuola(19, "Mary poppins di Colombarone", 11);
        Scuola scuolaMilano20 = new Scuola(20, "Olivieri - Glicine", 37);
        Scuola scuolaMilano21 = new Scuola(21, "Peter pan di Via Livorno", 19);
        Scuola scuolaMilano22 = new Scuola(22, "Pirandello - Dire fare...", 6);
        Scuola scuolaMilano23 = new Scuola(23, "Pirandello - Milleluci", 11);
        Scuola scuolaMilano24 = new Scuola(24, "Pirandello - Prato fiorito", 18);
        Scuola scuolaMilano25 = new Scuola(25, "Poi poi di Via Ferraris", 22);
        Scuola scuolaMilano26 = new Scuola(26, "Specchio magico", 17);
        Scuola scuolaMilano27 = new Scuola(27, "Villa San Martino - Via togliatti", 54);
        Scuola scuolaMilano28 = new Scuola(28, "VUOTO", 0);
        Scuola scuolaMilano29 = new Scuola(29, "UNDEFINED", 0);

        List<Scuola> listScuole = new ArrayList<>();
//        listScuole.add(scuolaMilano);
//        listScuole.add(scuolaTorino);
//        listScuole.add(scuolaVenezia);
        listScuole.add(scuolaMilano0);
        listScuole.add(scuolaMilano1);
        listScuole.add(scuolaMilano2);
        listScuole.add(scuolaMilano3);
        listScuole.add(scuolaMilano4);
        listScuole.add(scuolaMilano5);
        listScuole.add(scuolaMilano6);
        listScuole.add(scuolaMilano7);
        listScuole.add(scuolaMilano8);
        listScuole.add(scuolaMilano9);
        listScuole.add(scuolaMilano10);
        listScuole.add(scuolaMilano11);
        listScuole.add(scuolaMilano12);
        listScuole.add(scuolaMilano13);
        listScuole.add(scuolaMilano14);
        listScuole.add(scuolaMilano15);
        listScuole.add(scuolaMilano16);
        listScuole.add(scuolaMilano17);
        listScuole.add(scuolaMilano18);
        listScuole.add(scuolaMilano19);
        listScuole.add(scuolaMilano20);
        listScuole.add(scuolaMilano21);
        listScuole.add(scuolaMilano22);
        listScuole.add(scuolaMilano23);
        listScuole.add(scuolaMilano24);
        listScuole.add(scuolaMilano25);
        listScuole.add(scuolaMilano26);
        listScuole.add(scuolaMilano27);
        listScuole.add(scuolaMilano28);
        listScuole.add(scuolaMilano29);

        return listScuole;
    }

    private static List<Graduatoria> initGraduatorieTest(List<Scuola> listScuola) {
        Graduatoria graduatoria0 = new Graduatoria(listScuola.get(0));
        listScuola.get(0).setGraduatoria(graduatoria0);

        Graduatoria graduatoria1 = new Graduatoria(listScuola.get(1));
        listScuola.get(1).setGraduatoria(graduatoria1);

        Graduatoria graduatoria2 = new Graduatoria(listScuola.get(2));
        listScuola.get(2).setGraduatoria(graduatoria2);

        Graduatoria graduatoria3 = new Graduatoria(listScuola.get(3));
        listScuola.get(3).setGraduatoria(graduatoria3);
        Graduatoria graduatoria4 = new Graduatoria(listScuola.get(4));
        listScuola.get(4).setGraduatoria(graduatoria4);
        Graduatoria graduatoria5 = new Graduatoria(listScuola.get(5));
        listScuola.get(5).setGraduatoria(graduatoria5);
        Graduatoria graduatoria6 = new Graduatoria(listScuola.get(6));
        listScuola.get(6).setGraduatoria(graduatoria6);
        Graduatoria graduatoria7 = new Graduatoria(listScuola.get(7));
        listScuola.get(7).setGraduatoria(graduatoria7);
        Graduatoria graduatoria8 = new Graduatoria(listScuola.get(8));
        listScuola.get(8).setGraduatoria(graduatoria8);
        Graduatoria graduatoria9 = new Graduatoria(listScuola.get(9));
        listScuola.get(9).setGraduatoria(graduatoria9);
        Graduatoria graduatoria10 = new Graduatoria(listScuola.get(10));
        listScuola.get(10).setGraduatoria(graduatoria10);
        Graduatoria graduatoria11 = new Graduatoria(listScuola.get(11));
        listScuola.get(11).setGraduatoria(graduatoria11);
        Graduatoria graduatoria12 = new Graduatoria(listScuola.get(12));
        listScuola.get(12).setGraduatoria(graduatoria12);
        Graduatoria graduatoria13 = new Graduatoria(listScuola.get(13));
        listScuola.get(13).setGraduatoria(graduatoria13);
        Graduatoria graduatoria14 = new Graduatoria(listScuola.get(14));
        listScuola.get(14).setGraduatoria(graduatoria14);
        Graduatoria graduatoria15 = new Graduatoria(listScuola.get(15));
        listScuola.get(15).setGraduatoria(graduatoria15);
        Graduatoria graduatoria16 = new Graduatoria(listScuola.get(16));
        listScuola.get(16).setGraduatoria(graduatoria16);
        Graduatoria graduatoria17 = new Graduatoria(listScuola.get(17));
        listScuola.get(17).setGraduatoria(graduatoria17);
        Graduatoria graduatoria18 = new Graduatoria(listScuola.get(18));
        listScuola.get(18).setGraduatoria(graduatoria18);
        Graduatoria graduatoria19 = new Graduatoria(listScuola.get(19));
        listScuola.get(19).setGraduatoria(graduatoria19);
        Graduatoria graduatoria20 = new Graduatoria(listScuola.get(20));
        listScuola.get(20).setGraduatoria(graduatoria20);
        Graduatoria graduatoria21 = new Graduatoria(listScuola.get(21));
        listScuola.get(21).setGraduatoria(graduatoria21);
        Graduatoria graduatoria22 = new Graduatoria(listScuola.get(22));
        listScuola.get(22).setGraduatoria(graduatoria22);
        Graduatoria graduatoria23 = new Graduatoria(listScuola.get(23));
        listScuola.get(23).setGraduatoria(graduatoria23);
        Graduatoria graduatoria24 = new Graduatoria(listScuola.get(24));
        listScuola.get(24).setGraduatoria(graduatoria24);
        Graduatoria graduatoria25 = new Graduatoria(listScuola.get(25));
        listScuola.get(25).setGraduatoria(graduatoria25);
        Graduatoria graduatoria26 = new Graduatoria(listScuola.get(26));
        listScuola.get(26).setGraduatoria(graduatoria26);
        Graduatoria graduatoria27 = new Graduatoria(listScuola.get(27));
        listScuola.get(27).setGraduatoria(graduatoria27);
        Graduatoria graduatoria28 = new Graduatoria(listScuola.get(28));
        listScuola.get(28).setGraduatoria(graduatoria28);
        Graduatoria graduatoria29 = new Graduatoria(listScuola.get(29));
        listScuola.get(29).setGraduatoria(graduatoria29);

        List<Graduatoria> listGrad = new ArrayList<>();
//        listGrad.add(graduatoriaMilano);
//        listGrad.add(graduatoriaTorino);
//        listGrad.add(graduatoriaVenezia);
        listGrad.add(graduatoria0);
        listGrad.add(graduatoria1);
        listGrad.add(graduatoria2);
        listGrad.add(graduatoria3);
        listGrad.add(graduatoria4);
        listGrad.add(graduatoria5);
        listGrad.add(graduatoria6);
        listGrad.add(graduatoria7);
        listGrad.add(graduatoria8);
        listGrad.add(graduatoria9);
        listGrad.add(graduatoria10);
        listGrad.add(graduatoria11);
        listGrad.add(graduatoria12);
        listGrad.add(graduatoria13);
        listGrad.add(graduatoria14);
        listGrad.add(graduatoria15);
        listGrad.add(graduatoria16);
        listGrad.add(graduatoria17);
        listGrad.add(graduatoria18);
        listGrad.add(graduatoria19);
        listGrad.add(graduatoria20);
        listGrad.add(graduatoria21);
        listGrad.add(graduatoria22);
        listGrad.add(graduatoria23);
        listGrad.add(graduatoria24);
        listGrad.add(graduatoria25);
        listGrad.add(graduatoria26);
        listGrad.add(graduatoria27);
        listGrad.add(graduatoria28);
        listGrad.add(graduatoria29);

        return listGrad;
    }

    private static List<Domanda> initDomandeTest(List<Scuola> listScuole) {

        // primo test
        /*Domanda domandeAndrea = new Domanda("Andrea", UUID.randomUUID(), listScuole.get(0), listScuole.get(1), 10, 120);
        Domanda domandeEndri = new Domanda("Endri", UUID.randomUUID(), listScuole.get(0), listScuole.get(1), 120, 10);
        Domanda domandePluto = new Domanda("Pluto", UUID.randomUUID(), listScuole.get(1), listScuole.get(2), 200, 200);
        Domanda domandePippo = new Domanda("Pippo", UUID.randomUUID(), listScuole.get(2), listScuole.get(1), 150, 150);
        */

        //secondo test
        /*Domanda domandeAndrea = new Domanda("Andrea", UUID.randomUUID(), listScuole.get(0), listScuole.get(1), 10, 120);
        Domanda domandeEndri = new Domanda("Endri", UUID.randomUUID(), listScuole.get(0), listScuole.get(1), 120, 10);
        Domanda domandeCiccino = new Domanda("Ciccino", UUID.randomUUID(), listScuole.get(0), listScuole.get(1), 0, 150);
        Domanda domandePluto = new Domanda("Pluto", UUID.randomUUID(), listScuole.get(1), listScuole.get(2), 200, 200);
        Domanda domandePippo = new Domanda("Pippo", UUID.randomUUID(), listScuole.get(2), listScuole.get(1), 150, 150);
*/
        /*
        Domanda domandeAndrea = new Domanda("Andrea", UUID.randomUUID(), listScuole.get(0), listScuole.get(1), 10, 120);
        Domanda domandeEndri = new Domanda("Endri", UUID.randomUUID(), listScuole.get(0), listScuole.get(1), 120, 10);
        Domanda domandeCiccino = new Domanda("Ciccino", UUID.randomUUID(), listScuole.get(0), listScuole.get(1), 0, 160);
        Domanda domandePluto = new Domanda("Pluto", UUID.randomUUID(), listScuole.get(1), listScuole.get(2), 200, 200);
        Domanda domandePippo = new Domanda("Pippo", UUID.randomUUID(), listScuole.get(2), listScuole.get(1), 150, 150);
        Domanda domandeBastardo = new Domanda("Bastardo", UUID.randomUUID(), listScuole.get(1), listScuole.get(0), 0, 30000);
*/

        /*
        Domanda domandeAndrea = new Domanda("Andrea", UUID.randomUUID(), listScuole.get(0), listScuole.get(1), 10, 120);
        Domanda domandeEndri = new Domanda("Endri", UUID.randomUUID(), listScuole.get(0), listScuole.get(1), 120, 10);
        Domanda domandeCiccino = new Domanda("Ciccino", UUID.randomUUID(), listScuole.get(0), listScuole.get(2), 0, 150);
        Domanda domandePluto = new Domanda("Pluto", UUID.randomUUID(), listScuole.get(1), listScuole.get(2), 200, 200);
        Domanda domandePippo = new Domanda("Pippo", UUID.randomUUID(), listScuole.get(2), listScuole.get(1), 150, 150);
        Domanda domandeBastardo = new Domanda("Bastardo", UUID.randomUUID(), listScuole.get(1), listScuole.get(0), 30000, 0);
*/

        List<Domanda> listDomande = new ArrayList<>();
        /*
        listDomande.add(domandeAndrea);
        listDomande.add(domandeEndri);
        listDomande.add(domandePippo);
        listDomande.add(domandePluto);
        listDomande.add(domandeCiccino);
        listDomande.add(domandeBastardo);
*/

        return listDomande;
    }
}



