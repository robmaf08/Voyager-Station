package com.voyager.voyager_station.game.Parameters;

/**
 *
 * @author Roberto Maffucci
 */
public class ItalianNPCParameters implements InterfaceNPCParameters {

    @Override
    public String alien1Name() {
        return "alien1";
    }

    @Override
    public String[] alien1Aliases() {
        return new String[]{"mostro", "alien", "extraterrestre", "extra-terrestre", "alieno", "mostro", "figura", "forma"};
    }

    @Override
    public String alien2Name() {
        return "alien2";
    }

    @Override
    public String[] alien2Aliases() {
        return alien1Aliases();
    }

    @Override
    public String astronautName() {
        return "astronauta";
    }

    @Override
    public String[] astronautAliases() {
        return new String[]{"uomo", "persona", "sconosciuto", "individuo"};
    }

    @Override
    public String captainName() {
        return "capitano";
    }

    @Override
    public String[] captainAliases() {
        return astronautAliases();
    }
}
