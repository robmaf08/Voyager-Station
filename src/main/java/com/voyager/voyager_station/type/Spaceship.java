package com.voyager.voyager_station.type;

/**
 *
 * @author Roberto Maffucci
 */
public class Spaceship {

    private boolean turnedOn = false;

    private boolean motorsActivate = false;

    private boolean automaticPilot = false;

    private boolean doorsClosed = false;

    private boolean emergencyState = false;

    private boolean destinationSetting = false;

    private boolean depressurizeOn = false;

    private boolean protectionState = false;

    private boolean thrustersActivate = false;

    private boolean liftOff = false;

    private boolean land = false;

    private boolean radioOn = false;

    private static Spaceship instance;

    private Spaceship() {

    }

    public static Spaceship getInstance() {
        if (instance == null) {
            instance = new Spaceship();
        }
        return instance;
    }

    public boolean isTurnedOn() {
        return turnedOn;
    }

    public void setTurnedOn(boolean turnedOn) {
        this.turnedOn = turnedOn;
    }

    public boolean isMotorsActivate() {
        return motorsActivate;
    }

    public void setMotorsActivate(boolean motorsActivate) {
        this.motorsActivate = motorsActivate;
    }

    public boolean isAutomaticPilot() {
        return automaticPilot;
    }

    public void setAutomaticPilot(boolean automaticPilot) {
        this.automaticPilot = automaticPilot;
    }

    public boolean isDoorsClosed() {
        return doorsClosed;
    }

    public void setDoorsClosed(boolean doorsClosed) {
        this.doorsClosed = doorsClosed;
    }

    public boolean isEmergencyState() {
        return emergencyState;
    }

    public void setEmergencyState(boolean emergencyState) {
        this.emergencyState = emergencyState;
    }

    public boolean isDestinationSetting() {
        return destinationSetting;
    }

    public void setDestinationSetting(boolean destinationSetting) {
        this.destinationSetting = destinationSetting;
    }

    public boolean isDepressurizeOn() {
        return depressurizeOn;
    }

    public void setDepressurizeOn(boolean depressurizeOn) {
        this.depressurizeOn = depressurizeOn;
    }

    public boolean isProtectionState() {
        return protectionState;
    }

    public void setProtectionState(boolean protectionState) {
        this.protectionState = protectionState;
    }

    public boolean isThrustersActivate() {
        return thrustersActivate;
    }

    public void setThrustersActivate(boolean thrustersActivate) {
        this.thrustersActivate = thrustersActivate;
    }

    public boolean isLiftOff() {
        return liftOff;
    }

    public void setLiftOff(boolean liftOff) {
        this.liftOff = liftOff;
    }

    public boolean isLand() {
        return land;
    }

    public void setLand(boolean land) {
        this.land = land;
    }

    public boolean isRadioOn() {
        return radioOn;
    }

    public void setRadioOn(boolean radioOn) {
        this.radioOn = radioOn;
    }

}
