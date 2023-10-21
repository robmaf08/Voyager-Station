package com.voyager.voyager_station.game.Parameters;

import java.io.Serializable;

/**
 *
 * @author Roberto Maffucci
 */
public class ItalianObjectParameters implements InterfaceObjectParameters, Serializable {

    @Override
    public String torchName() {
        return "torcia";
    }

    @Override
    public String[] torchAliases() {
        return new String[]{"luce elettrica", "luce", "dinamo", "faro"};
    }

    @Override
    public String journalName() {
        return "giornale";
    }

    @Override
    public String[] journalAliases() {
        return new String[]{"foglio", "carta", "libretto", "libro", "pagina"};
    }

    @Override
    public String knifeName() {
        return "bisturi";
    }

    @Override
    public String[] knifeAliases() {
        return new String[]{"coltello", "lama", "lametta"};
    }

    @Override
    public String gunName() {
        return "raygun";
    }

    @Override
    public String[] gunAliases() {
        return new String[]{"pistola", "pistola laser", "laser", "ray-gun", "fucile"};
    }

    @Override
    public String computerName() {
        return "computer";
    }

    @Override
    public String[] computerAliases() {
        return new String[]{"pc", "computer", "macchina", "elaboratore"};
    }

    @Override
    public String keyName() {
        return "chiave";
    }

    @Override
    public String[] keyAliases() {
        return new String[]{"chiave", "chiave elettrica", "usb", "penna elettronica", "pass"};
    }

    @Override
    public String dossierName() {
        return "dossier";
    }

    @Override
    public String[] dossierAliases() {
        return new String[]{"fogli", "carte", "libretto", "libro", "archivio"};
    }

    @Override
    public String suiteName() {
        return "spacesuit";
    }

    @Override
    public String[] suiteNameAliases() {
        return new String[]{"tuta spaziale", "spacesuite", "tuta", "armatura", "spacesuit", "uniforme", "divisa"};
    }

    @Override
    public String serverName() {
        return "server";
    }

    @Override
    public String[] serverAliases() {
        return new String[]{"pc", "computer", "macchina", "elaboratore"};
    }

    @Override
    public String commandManualName() {
        return "commandManual";
    }

    @Override
    public String[] commandManualAliases() {
        return new String[]{"manuale", "manuale comandi", "libricino"};
    }

    @Override
    public String emergencyKitName() {
        return "kit emergenza";
    }

    @Override
    public String[] emergencyKitAliases() {
        return new String[]{"cassa strumenti", "kit", "zaino", "borsa", "medikit"};
    }

    @Override
    public String weaponsBagName() {
        return "cassad'armi";
    }

    @Override
    public String[] weaponsBagAliases() {
        return new String[]{"zaino", "borsa", "arsenale", "borsa armi", "zaino armi"};
    }
}
