package com.voyager.voyager_station.socket;

import com.voyager.voyager_station.type.Spaceship;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Roberto Maffucci
 */
public class CommanderThread extends Thread {

    private final Socket socket;

    private boolean run = true;

    public CommanderThread(Socket socket) {
        this.socket = socket;
    }

    public CommanderThread(Socket socket, String name) {
        super(name);
        this.socket = socket;
    }

    private PrintWriter out = null;

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            while (run) {
                String str = in.readLine();
                if (str != null) {
                    str = str.trim();
                    Pattern pattern = Pattern.compile("\\S+");
                    Matcher matcher = pattern.matcher(str);
                    boolean findcmd = matcher.find();
                    if (findcmd && matcher.group().equalsIgnoreCase("@turn_on")) {
                        try {
                            if (!Spaceship.getInstance().isTurnedOn()) {
                                out.println("#turned on");
                                Spaceship.getInstance().setTurnedOn(true);
                            } else {
                                out.println("#alreay turned on");
                            }
                        } catch (Exception ex) {
                            out.println("#error " + ex.getMessage());
                        }
                    } else if (findcmd && matcher.group().equalsIgnoreCase("@activate_motors")) {
                        try {
                            if (!Spaceship.getInstance().isMotorsActivate() && Spaceship.getInstance().isTurnedOn()) {
                                out.println("#motors activated");
                                Spaceship.getInstance().setMotorsActivate(true);
                            } else if (!Spaceship.getInstance().isTurnedOn()) {
                                out.println("#please be sure to turn on the ship before activating motors");
                            } else {
                                out.println("#already activated");
                            }
                        } catch (Exception ex) {
                            out.println("#error " + ex.getMessage());
                        }
                    } else if (findcmd && matcher.group().equalsIgnoreCase("@activate_thrusters")) {
                        try {
                            if (Spaceship.getInstance().isMotorsActivate() 
                                    && Spaceship.getInstance().isTurnedOn()
                                    && !Spaceship.getInstance().isThrustersActivate()) {
                                out.println("#thrusters activated");
                                Spaceship.getInstance().setThrustersActivate(true);
                            } else if (!Spaceship.getInstance().isTurnedOn()) {
                                out.println("#please be sure to turn on the ship before activating thrusters");
                            } else if (Spaceship.getInstance().isTurnedOn() && !Spaceship.getInstance().isMotorsActivate()) {
                                out.println("#you need to turn on the motors, otherwise thruster will not work");
                            } else if (Spaceship.getInstance().isThrustersActivate()) {
                                out.println("#thrusters already activated");
                            }
                        } catch (Exception ex) {
                            out.println("#error " + ex.getMessage());
                        }
                    } else if (findcmd && matcher.group().equalsIgnoreCase("@close_doors")) {
                        if (Spaceship.getInstance().isTurnedOn()) {
                            out.println("#doors are now closed");
                            Spaceship.getInstance().setDoorsClosed(true);
                        } else if (Spaceship.getInstance().isDoorsClosed()) {
                            out.println("#doors already closed");
                        } else {
                            out.println("#turn on the ship before closing doors");
                        }
                    } else if (findcmd && matcher.group().equalsIgnoreCase("@depressurize_on")) {
                        if (Spaceship.getInstance().isTurnedOn()
                                && Spaceship.getInstance().isDoorsClosed()
                                && Spaceship.getInstance().isMotorsActivate() 
                                && !Spaceship.getInstance().isDepressurizeOn()) {
                            out.println("#depressurization completed");
                            Spaceship.getInstance().setDepressurizeOn(true);
                        } else if (Spaceship.getInstance().isDepressurizeOn()
                                && Spaceship.getInstance().isDoorsClosed()
                                && !Spaceship.getInstance().isMotorsActivate()) {
                            out.println("#can't deprussurizate because msotors are disactivated");
                        } else if (Spaceship.getInstance().isDepressurizeOn()
                                && !Spaceship.getInstance().isDoorsClosed()
                                && Spaceship.getInstance().isMotorsActivate()) {
                            out.println("#close doors before depressurizate");
                        } else if (Spaceship.getInstance().isDepressurizeOn()) {
                            out.println("#depressurization already did");
                        } else {
                            if (!Spaceship.getInstance().isTurnedOn()) {
                                out.println("#turn on the ship before depressurize");
                            }
                            if (!Spaceship.getInstance().isMotorsActivate()) {
                                out.println("#turn on motors before depressurize");
                            }
                            if (!Spaceship.getInstance().isDoorsClosed()) {
                                out.println("#close doors before depressurize");
                            }
                        }
                    } else if (findcmd && matcher.group().equalsIgnoreCase("@automatic_pilot_on")) {
                        if (Spaceship.getInstance().isTurnedOn() && Spaceship.getInstance().isMotorsActivate()) {
                            out.println("#automatic pilot setted!");
                            Spaceship.getInstance().setAutomaticPilot(true);
                        } else if (Spaceship.getInstance().isAutomaticPilot()) {
                            out.println("#automatic pilot already setted");
                        } else if (!Spaceship.getInstance().isTurnedOn()) {
                            out.println("#turn on the ship before setting automatic pilot");
                        } else if (!Spaceship.getInstance().isMotorsActivate()) {
                            out.println("#turn on the motors before setting automatic pilot");
                        }
                    } else if (findcmd && matcher.group().equalsIgnoreCase("@protection_on")) {
                        if (Spaceship.getInstance().isTurnedOn() && Spaceship.getInstance().isMotorsActivate()) {
                            out.println("#more protection of the ship activated on");
                            Spaceship.getInstance().setProtectionState(true);
                        } else if (Spaceship.getInstance().isProtectionState()) {
                            out.println("#protected already inserted");
                        } else if (!Spaceship.getInstance().isTurnedOn()) {
                            out.println("#turn on the ship before setting protection");
                        } else if (!Spaceship.getInstance().isMotorsActivate()) {
                            out.println("#turn on the motors before protection");
                        }
                    } else if (findcmd && matcher.group().equalsIgnoreCase("@liftoff")) {
                        if (!Spaceship.getInstance().isTurnedOn()) {
                            out.println("#you can't start the liftoff procedure without turning on the ship and the motors");
                        } else if (Spaceship.getInstance().isTurnedOn()) {
                            if (Spaceship.getInstance().isDoorsClosed()
                                    && Spaceship.getInstance().isAutomaticPilot()
                                    && Spaceship.getInstance().isDoorsClosed()
                                    && Spaceship.getInstance().isMotorsActivate()
                                    && Spaceship.getInstance().isThrustersActivate()
                                    && Spaceship.getInstance().isDepressurizeOn()
                                    && Spaceship.getInstance().isDestinationSetting()
                                    && Spaceship.getInstance().isRadioOn()) {
                                out.println("#procedure of litfoff complete!");
                                Spaceship.getInstance().setLiftOff(true);
                            } else if (Spaceship.getInstance().isLiftOff()) {
                                out.println("#procedure of litfoff already completed!");
                            } else {
                                out.println("#you must activate all the necessary to start the procedure of liftoff");
                            }
                        }
                    } else if (findcmd && matcher.group().equalsIgnoreCase("@radio_on")) {
                        if (Spaceship.getInstance().isTurnedOn()) {
                            out.println("#radio on, now you are connected wirth earth station");
                            Spaceship.getInstance().setRadioOn(true);
                        } else if (Spaceship.getInstance().isRadioOn()) {
                            out.println("#radio is already on");
                        } else {
                            out.println("#turn on the ship before turning on the radio");
                        }
                    } else if (findcmd && matcher.group().equalsIgnoreCase("@land")) {
                        if (Spaceship.getInstance().isDoorsClosed()
                                && Spaceship.getInstance().isAutomaticPilot()
                                && Spaceship.getInstance().isDoorsClosed()
                                && Spaceship.getInstance().isMotorsActivate()
                                && Spaceship.getInstance().isThrustersActivate()
                                && Spaceship.getInstance().isDepressurizeOn()
                                && Spaceship.getInstance().isDestinationSetting()
                                && Spaceship.getInstance().isRadioOn()
                                && Spaceship.getInstance().isDestinationSetting()
                                && Spaceship.getInstance().isLiftOff()
                                && Spaceship.getInstance().isProtectionState()) {
                            out.println("#launching in 3...2...1.liftoff!!");
                        } else {
                            out.println("#setting the necessary to land!");
                        }
                    } else if (findcmd && matcher.group().equalsIgnoreCase("@set_emergency_state")) {
                        if (Spaceship.getInstance().isTurnedOn()) {
                            out.println("#emergency state setted, you will have now the 100% of priority");
                            Spaceship.getInstance().setEmergencyState(true);
                        } else if (Spaceship.getInstance().isEmergencyState()) {
                            out.println("#emergency state already setted");
                        } else {
                            out.println("#turn on the ship before setting the emergency state");
                        }
                    } else if (findcmd && matcher.group().equalsIgnoreCase("@set_destination_earth")) {
                        if (Spaceship.getInstance().isTurnedOn()) {
                            out.println("#california will be you place planned for your return");
                            Spaceship.getInstance().setDestinationSetting(true);
                        } else if (Spaceship.getInstance().isDestinationSetting()){
                            out.println("#destination already setted");
                        } else {
                            out.println("#turn on the ship before setting the destination");
                        }
                    } else if (findcmd && matcher.group().equalsIgnoreCase("@exit")) {
                        run = false;
                        out.println("Connection lost!");
                    } else {
                        out.println("#error unknown command");
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println(ex);
        } finally {
            out.println("Connection lost!");
            try {
                socket.close();
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }
}
