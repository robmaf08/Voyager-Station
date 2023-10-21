package com.voyager.voyager_station.game.Parameters;

import java.io.Serializable;

/**
 *
 * @author Roberto Maffucci
 */
public class ItalianCommandNames implements InterfaceCommandNames, Serializable {

    @Override
    public String north() {
        return "nord";
    }

    @Override
    public String[] northAliases() {
        return new String[]{"n", "N", "Nord", "NORD"};
    }

    @Override
    public String south() {
        return "sud";
    }

    @Override
    public String[] southAliases() {
        return new String[]{"s", "S", "Sud", "SUD"};
    }

    @Override
    public String east() {
        return "est";
    }

    @Override
    public String[] eastAliases() {
        return new String[]{"e", "E", "Est", "EST"};
    }

    @Override
    public String west() {
        return "ovest";
    }

    @Override
    public String[] westAliases() {
        return new String[]{"o", "O", "Ovest", "OVEST"};
    }

    @Override
    public String inventory() {
        return "inventario";
    }

    @Override
    public String[] inventoryAliases() {
        return new String[]{"inv", "i", "I"};
    }

    @Override
    public String end() {
        return "esci";
    }

    @Override
    public String[] endAliases() {
        return new String[]{"sterminati", "fine", "muori", "ammazzati", "ucciditi", "suicidati"};
    }

    @Override
    public String look() {
        return "osserva";
    }

    @Override
    public String[] lookAliases() {
        return new String[]{};
    }

    @Override
    public String examine() {
        return "osserva";
    }

    @Override
    public String[] examineAliases() {
        return new String[]{"guarda", "vedi", "trova", "cerca", "descrivi", "ispeziona", "analizza", "soffermati", "esamina"};
    }

    @Override
    public String open() {
        return "apri";
    }

    @Override
    public String[] openAliases() {
        return new String[]{};
    }

    @Override
    public String close() {
        return "chiudi";
    }

    @Override
    public String[] closeAliases() {
        return new String[]{};
    }

    @Override
    public String put() {
        return "metti";
    }

    @Override
    public String[] putAliases() {
        return new String[]{"inserisci", "infila", "schiaffa", "riponi"};
    }

    @Override
    public String push() {
        return "premi";
    }

    @Override
    public String[] pushAliases() {
        return new String[]{"spingi", "attiva", "pigia", "schiaccia"};
    }

    @Override
    public String attack() {
        return "attacca";
    }

    @Override
    public String[] attackAliases() {
        return new String[]{"spara", "colpisci", "danneggia", "distruggi", "ammazza", "uccidi", "stermina", "massacra", "fredda", "sopprimi",
            "annienta", "assassina"};
    }

    @Override
    public String use() {
        return "usa";
    }

    @Override
    public String[] useAliases() {
        return new String[]{"utilizza", "sfrutta", "adopera", "impiega"};
    }

    @Override
    public String talkTo() {
        return "parla";
    }

    @Override
    public String[] talkToAliases() {
        return new String[]{"conversa", "chiama", "chiedi", "discuti", "chiacchiera"};
    }

    @Override
    public String unlock() {
        return "sblocca";
    }

    @Override
    public String[] unlockAliases() {
        return new String[]{"utilizza", "sblocca", "digita", "immetti"};
    }

    @Override
    public String pickUp() {
        return "raccogli";
    }

    @Override
    public String[] pickUpAliases() {
        return new String[]{"prendi", "arraffa", "agguanta", "preleva"};
    }

    @Override
    public String plunder() {
        return "depreda";
    }

    @Override
    public String[] plunderAliases() {
        return new String[]{"saccheggia", "deruba", "preda", "spoglia"};
    }

    @Override
    public String sleep() {
        return "dormi";
    }

    @Override
    public String[] sleepAliases() {
        return new String[]{"riposa", "coricati", "sdraiati", "addormentati", "appisolati"};
    }

}
