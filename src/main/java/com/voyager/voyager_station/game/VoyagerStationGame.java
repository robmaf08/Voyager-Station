package com.voyager.voyager_station.game;

import com.voyager.voyager_station.game.Parameters.InterfaceNPCParameters;
import com.voyager.voyager_station.game.Parameters.InterfaceObjectParameters;
import com.voyager.voyager_station.game.Parameters.ItalianNPCParameters;
import com.voyager.voyager_station.game.Parameters.ItalianObjectParameters;
import com.voyager.voyager_station.game.Parameters.InterfaceCommandNames;
import com.voyager.voyager_station.adventure.GameDescription;
import com.voyager.voyager_station.adventure.Utils;
import com.voyager.voyager_station.jdbc.ObjectDatabase;
import com.voyager.voyager_station.jdbc.PasswordRooms;
import com.voyager.voyager_station.jdbc.RoomDatabase;
import com.voyager.voyager_station.parser.Parser;
import com.voyager.voyager_station.parser.ParserOutput;
import com.voyager.voyager_station.sound.CostantsSoundPath;
import com.voyager.voyager_station.sound.SoundGame;
import com.voyager.voyager_station.sound.SoundGame.BackgroundSound;
import com.voyager.voyager_station.type.AdvCharacter;
import com.voyager.voyager_station.type.AdvEnemy;
import com.voyager.voyager_station.type.AdvNPC;
import com.voyager.voyager_station.type.AdvObject;
import com.voyager.voyager_station.type.AdvObjectContainer;
import com.voyager.voyager_station.type.Command;
import com.voyager.voyager_station.type.CommandType;
import com.voyager.voyager_station.type.Room;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roberto Maffucci
 */
public class VoyagerStationGame extends GameDescription implements Serializable {

    private final InterfaceCommandNames commandNames;

    private final InterfaceObjectParameters objNames;

    private boolean endGame = false;

    private boolean hasSleep = false;

    private boolean containerOpened = false;

    private boolean npcInventoryOpened = false;

    private final boolean firstEnemyFound = false;

    private Room lastRoomVisited = null;

    private boolean soundEnabled = true;

    private static Map<AdvObject, Room> usableObjectRoom;

    private transient SoundGame sound = new SoundGame();

    private BackgroundSound backgroundSound;

    public boolean isEndGame() {
        return endGame;
    }

    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }

    public boolean isContainerOpened() {
        return containerOpened;
    }

    public void setContainerOpened(boolean containerOpened) {
        this.containerOpened = containerOpened;
    }

    public BackgroundSound getBackgroundSound() {
        return backgroundSound;
    }

    public void setBackgroundSound(BackgroundSound backgroundSound) {
        this.backgroundSound = backgroundSound;
    }

    public boolean isHasSleep() {
        return hasSleep;
    }

    public VoyagerStationGame(InterfaceCommandNames commandNames, InterfaceObjectParameters objNames) {
        this.commandNames = commandNames;
        this.objNames = objNames;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public InterfaceCommandNames getCommandNames() {
        return commandNames;
    }

    public InterfaceObjectParameters getObjNames() {
        return objNames;
    }

    @Override
    public void init() throws Exception {
        usableObjectRoom = new HashMap<>();
        RoomDatabase room = new RoomDatabase();
        ObjectDatabase objectS = new ObjectDatabase();
        PasswordRooms.getInstance().connect();
        room.init();
        objectS.init(new ItalianObjectParameters());

        //Setting comandi spostamento
        Command nord = new Command(CommandType.NORD, commandNames.north());
        nord.setAlias(commandNames.northAliases());
        nord.setDescription("Per andare a nord a partire da una stanza.");
        getCommands().add(nord);

        Command south = new Command(CommandType.SOUTH, commandNames.south());
        south.setAlias(commandNames.southAliases());
        south.setDescription("Per andare a sud a partire da una stanza.");
        getCommands().add(south);

        Command east = new Command(CommandType.EAST, commandNames.east());
        east.setAlias(commandNames.eastAliases());
        east.setDescription("Per andare a est a partire da una stanza.");
        getCommands().add(east);

        Command west = new Command(CommandType.WEST, commandNames.west());
        west.setAlias(commandNames.westAliases());
        west.setDescription("Per andare a ovest a partire da una stanza.");
        getCommands().add(west);

        Command inventory = new Command(CommandType.INVENTORY, commandNames.inventory());
        inventory.setAlias(commandNames.inventoryAliases());
        inventory.setDescription("Per osservare gli elementi/oggetti presenti nell'inventario. "
                + "Di ciascuno di essi è riportata una descrizione");
        getCommands().add(inventory);

        Command end = new Command(CommandType.END, commandNames.end());
        end.setAlias(commandNames.endAliases());
        end.setDescription("Per uscire dal gioco.");
        getCommands().add(end);

        Command look = new Command(CommandType.LOOK_AT, commandNames.examine());
        look.setAlias(commandNames.examineAliases());
        look.setDescription("Per osservare un oggetto, un nemico, un npc. Esempio: <osserva> <oggetto/nemico/npc>");
        getCommands().add(look);

        Command pickup = new Command(CommandType.PICK_UP, commandNames.pickUp());
        pickup.setAlias(commandNames.pickUpAliases());
        pickup.setDescription("Per raccogliere un oggetto ed inserirlo nell'inventario. Esempio: <raccogli> <oggetto>");
        getCommands().add(pickup);

        Command open = new Command(CommandType.OPEN, commandNames.open());
        open.setAlias(commandNames.openAliases());
        open.setDescription("Per aprire un oggetto/oggetto contenitore. Esempio: <apri> <oggetto/oggetto contenitore>");
        getCommands().add(open);

        Command push = new Command(CommandType.PUSH, commandNames.push());
        push.setAlias(commandNames.pushAliases());
        push.setDescription("Per premere un oggetto.");
        getCommands().add(push);

        Command use = new Command(CommandType.USE, commandNames.use());
        use.setAlias(commandNames.useAliases());
        use.setDescription("Per utilizzare un oggetto in una specifica stanza. Esempio: <usa> <oggetto/oggetto inventario>");
        getCommands().add(use);

        Command attack = new Command(CommandType.ATTACK, commandNames.attack());
        attack.setAlias(commandNames.attackAliases());
        attack.setDescription("Per colpire un nemico. Esempio: <attacca> <nemico>");
        getCommands().add(attack);

        Command sleep = new Command(CommandType.SLEEP, commandNames.sleep());
        sleep.setAlias(commandNames.sleepAliases());
        sleep.setDescription("Comando per dormire");
        getCommands().add(sleep);

        Command unlock = new Command(CommandType.UNLOCK, commandNames.unlock());
        unlock.setAlias(commandNames.unlockAliases());
        unlock.setDescription("Per sbloccare una stanza bloccata con un codice di accesso. Esempio: <sblocca> <direzione stanza> <codice>: sblocca ovest 123456");
        getCommands().add(unlock);

        Command talkTo = new Command(CommandType.TALK_TO, commandNames.talkTo());
        talkTo.setAlias(commandNames.talkToAliases());
        talkTo.setDescription("Per aprire un dialogo con un npc. Esempio: <parla> <npc>");
        getCommands().add(talkTo);

        Command plunder = new Command(CommandType.PLUNDER, commandNames.plunder());
        plunder.setAlias(commandNames.plunderAliases());
        plunder.setDescription("Per saccheggiare/depredare un npc, nemico. Esempio: <depreda> <npc/nemico> o anche <apri> <inventario> <npc/nemico>");
        getCommands().add(plunder);

        Room playerBedroom = room.getRoom("SELECT * FROM Room WHERE id = 1");
        Room bathRoom = room.getRoom("SELECT * FROM Room WHERE id = 2");
        Room corridoio = room.getRoom("SELECT * FROM Room WHERE id = 3");
        Room corridoio2 = room.getRoom("SELECT * FROM Room WHERE id = 4");
        Room armeria = room.getRoom("SELECT * FROM Room WHERE id = 5");
        Room bedroom2 = room.getRoom("SELECT * FROM Room WHERE id = 6");
        Room corridoio3 = room.getRoom("SELECT * FROM Room WHERE id = 7");
        Room computerRoom = room.getRoom("SELECT * FROM Room WHERE id = 8");
        Room corridoioAlieno = room.getRoom("SELECT * FROM Room WHERE id = 9");
        Room corridoioA57 = room.getRoom("SELECT * FROM Room WHERE id = 10");
        Room relaxRoom = room.getRoom("SELECT * FROM Room WHERE id = 11");
        Room infermieria = room.getRoom("SELECT * FROM Room WHERE id = 12");
        Room commandRoom = room.getRoom("SELECT * FROM Room WHERE id = 13");
        Room captainRoom = room.getRoom("SELECT * FROM Room WHERE id = 14");
        Room corridoioA58 = room.getRoom("SELECT * FROM Room WHERE id = 15");
        Room warehouse = room.getRoom("SELECT * FROM Room WHERE id = 16");
        Room corridoioA59 = room.getRoom("SELECT * FROM Room WHERE id = 17");
        Room vault = room.getRoom("SELECT * FROM Room WHERE id = 19");
        Room spaceship = room.getRoom("SELECT * FROM Room WHERE id = 20");

        AdvObject giornale = objectS.getObject("SELECT * FROM Object WHERE id = 1");
        AdvObject torcia = objectS.getObject("SELECT * FROM Object WHERE id = 2");
        AdvObject bisturi = objectS.getObject("SELECT * FROM Object WHERE id = 3");
        AdvObject computer = objectS.getObject("SELECT * FROM Object WHERE id = 4");
        AdvObject dossier = objectS.getObject("SELECT * FROM Object WHERE id = 5");
        AdvObject commandManual = objectS.getObject("SELECT * FROM Object WHERE id = 6");
        AdvObject spacesuit = objectS.getObject("SELECT * FROM Object WHERE id = 7");
        AdvObject gun1 = objectS.getObject("SELECT * FROM Object WHERE id = 8");
        AdvObject key = objectS.getObject("SELECT * FROM Object WHERE id = 9");
        AdvObject serverComputer = objectS.getObject("SELECT * FROM Object WHERE id = 10");

        giornale.setPushable(false);
        giornale.setAlias(objNames.journalAliases());
        torcia.setAlias(objNames.torchAliases());
        torcia.setPushable(true);
        torcia.setPickupable(true);
        torcia.setPush(false);
        torcia.setUsable(true);
        bisturi.setPushable(false);
        computer.setAlias(objNames.computerAliases());
        computer.setOpenable(false);
        computer.setPickupable(false);
        computer.setOpen(false);
        computer.setPushable(false);
        computer.setUsable(true);
        dossier.setAlias(objNames.dossierAliases());
        dossier.setOpenable(true);
        dossier.setPickupable(false);
        dossier.setOpen(false);
        dossier.setPushable(false);
        dossier.setUsable(false);
        commandManual.setAlias(objNames.commandManualAliases());
        commandManual.setOpenable(true);
        commandManual.setPickupable(false);
        commandManual.setOpen(false);
        commandManual.setPushable(false);
        commandManual.setUsable(false);
        spacesuit.setAlias(objNames.suiteNameAliases());
        spacesuit.setOpenable(false);
        spacesuit.setPickupable(true);
        spacesuit.setOpen(false);
        spacesuit.setPushable(false);
        spacesuit.setUsable(false);
        serverComputer.setAlias(objNames.computerAliases());
        serverComputer.setOpenable(false);
        serverComputer.setPickupable(false);
        serverComputer.setOpen(false);
        serverComputer.setPushable(false);
        serverComputer.setUsable(true);
        gun1.setAlias(objNames.gunAliases());
        gun1.setOpenable(false);
        gun1.setPickupable(true);
        gun1.setOpen(false);
        gun1.setPushable(false);
        gun1.setUsable(false);
        key.setAlias(objNames.keyAliases());
        key.setOpenable(false);
        key.setPickupable(true);
        key.setOpen(false);
        key.setPushable(false);
        key.setUsable(true);

        /* Setting Object container */
        AdvObjectContainer emergencykit = new AdvObjectContainer(4, objNames.emergencyKitName(), "Uno zaino con elementi per emergenza");
        emergencykit.setAlias(objNames.emergencyKitAliases());
        emergencykit.setOpenable(true);
        emergencykit.setPickupable(false);
        emergencykit.setPushable(false);
        emergencykit.setOpen(false);
        emergencykit.setImgObjRoom("emergencyKit.png");
        emergencykit.add(bisturi);
        AdvObjectContainer weaponsBag = new AdvObjectContainer(8, objNames.weaponsBagName(), "Uno zaino con diverse armi futuristiche.");
        weaponsBag.setAlias(objNames.weaponsBagAliases());
        weaponsBag.setPushable(false);
        weaponsBag.setOpenable(true);
        weaponsBag.setPickupable(false);
        weaponsBag.setOpen(false);
        weaponsBag.setImgObjRoom("weaponsBag.png");

        /* Setting NPCs and Enemy */
        InterfaceNPCParameters npc = new ItalianNPCParameters();
        AdvCharacter astronaut = new AdvCharacter(17, npc.astronautName(), "Osservando il corpo capisci che sia stato ferito dall'alieno che hai appena ucciso! "
                + "E' ormai in fin di vita, ma bisognerebbe chiederli cosa è successo e perché indossa una tuta spaziale!");
        astronaut.setAlias(npc.astronautAliases());
        astronaut.setFirstDialogue("Aiutami...mi ha colpito con la sual coda...non sono quello che sembrano, ci hanno mentito sin dall'inizio..."
                + "Avremm *cough* *cough*, avremmo dovuto capir..capi...capirlo sin dall'inizio...Scappa, dirigiti verso la navicella "
                + "di emergenza, e scappa....quelli esseri non sono...*muore*");
        relaxRoom.getNPCs().add(astronaut);
        AdvCharacter captain = new AdvCharacter(18, npc.captainName(), "Ha un'aspetto terribile anche lui dovrebbe essere in fin di vita....");
        captain.setAlias(npc.captainAliases());
        captain.setFirstDialogue("Era tutto un esperimento...mi hanno*cough* iniettato il NewAriston, eravamo le loro cavie... "
                + "Ci hanno ingannato dall'inizio...ci stanno iniettando il patogeno che ci trasforma *cough**cough**cough*...in mostri. "
                + "Mi sto per trasformare in uno di loro, ho paura e non c'è niente che tu possa fare per aiutarmi...*cough* "
                + "Prendi la navicella di emergenza, torna sulla terra e avverti tutti!! Il codice di accesso al vault è: 131517,"
                + " buona fortuna, ora scappa prima che mi trasformi in uno di loro...");
        captainRoom.getNPCs().add(captain);

        AdvEnemy alien = new AdvEnemy(15, npc.alien1Name(), "Esaminando il corpo dell'alieno trovi una tessera di"
                + "un dipendente della stazione, a cosa servirà?", "E' la cosa più macabra che tu abbia mai visto. L'alieno ti fissa "
                + "ma probabilmente possono muoversi solo producendo rumore!");
        alien.setExamineDead("Un fetore orribile, ma nelle sue mani sanguinanti di colore verde, trovi un foglio spezzato."
                + "C'è scritto 1317 ma non vi sono gli ultimi due numeri . A cosa si riferirà?");
        alien.setImgObjRoom("enemy.png");
        alien.setAlias(npc.alien1Aliases());
        corridoioAlieno.getNPCs().add(alien);

        AdvEnemy alien2 = new AdvEnemy(16, npc.alien2Name(), "Esaminando il corpo dell'alieno trovi una tessera di"
                + "un dipendente della stazione, a cosa servirà?", "Ancora un'altro?");
        alien2.setExamineDead("Ci sono due ulteriori numeri, a cosa si riferiscono?");
        alien2.setImgObjRoom("enemy2.png");
        alien2.setAlias(npc.alien2Aliases());
        warehouse.getNPCs().add(alien2);

        /* Adding objects room or container */
        weaponsBag.add(gun1);
        astronaut.addInventory(key);
        spaceship.getObjects().add(commandManual);
        warehouse.getObjects().add(spacesuit);
        bedroom2.getObjects().add(torcia);
        playerBedroom.getObjects().add(giornale);
        bathRoom.getObjects().add(emergencykit);
        armeria.getObjects().add(weaponsBag);
        relaxRoom.getNPCs().add(astronaut);
        computerRoom.getObjects().add(computer);
        spaceship.getObjects().add(serverComputer);
        infermieria.getObjects().add(dossier);

        /* Setting movement */
        playerBedroom.setWest(bathRoom);
        bathRoom.setEast(playerBedroom);
        playerBedroom.setEast(corridoio);
        corridoio.setWest(playerBedroom);
        corridoio.setNorth(corridoio2);
        corridoio2.setNorth(corridoioAlieno);
        corridoio2.setSouth(corridoio);
        corridoio2.setWest(armeria);
        corridoio2.setEast(corridoio3);
        corridoioAlieno.setSouth(corridoio2);
        corridoioAlieno.setNorth(corridoioA57);
        corridoioA57.setSouth(corridoioAlieno);
        corridoioA57.setNorth(infermieria);
        corridoioA57.setWest(relaxRoom);
        infermieria.setSouth(corridoioA57);
        relaxRoom.setEast(corridoioA57);
        relaxRoom.setNorth(commandRoom);
        commandRoom.setSouth(relaxRoom);
        commandRoom.setEast(corridoioA58);
        corridoioA58.setWest(commandRoom);
        corridoioA58.setEast(captainRoom);
        corridoioA58.setNorth(corridoioA59);
        corridoioA59.setSouth(corridoioA58);
        corridoioA59.setNorth(warehouse);
        warehouse.setSouth(corridoioA59);
        commandRoom.setWest(vault);
        spaceship.setEast(commandRoom);
        spaceship.setSouth(vault);
        vault.setNorth(spaceship);
        vault.setEast(commandRoom);
        captainRoom.setWest(corridoioA58);
        armeria.setEast(corridoio2);
        corridoio3.setEast(bedroom2);
        corridoio3.setWest(corridoio2);
        corridoio3.setNorth(computerRoom);
        computerRoom.setSouth(corridoio3);
        bedroom2.setWest(corridoio3);

        /* Setting unvisibile or locked rooms*/
        armeria.setVisible(false);
        armeria.setLocked(true);
        captainRoom.setVisible(false);
        captainRoom.setLocked(true);
        infermieria.setLocked(true);
        spaceship.setLocked(true);
        warehouse.setLocked(true);

        /* Setting Rooms Password */
        PasswordRooms.getInstance().insertRoomPassword(armeria, "122574");
        PasswordRooms.getInstance().insertRoomPassword(infermieria, "151719");
        PasswordRooms.getInstance().insertRoomPassword(spaceship, "131517");
        PasswordRooms.getInstance().insertRoomPassword(captainRoom, "131719");

        /* Adding Rooms */
        addRoom(playerBedroom);
        addRoom(bathRoom);
        addRoom(corridoio);
        addRoom(corridoio2);
        addRoom(corridoio3);
        addRoom(armeria);
        addRoom(bedroom2);
        addRoom(computerRoom);
        addRoom(corridoioAlieno);
        addRoom(corridoioA57);
        addRoom(relaxRoom);
        addRoom(infermieria);
        addRoom(commandRoom);
        addRoom(captainRoom);
        addRoom(corridoioA58);
        addRoom(corridoioA59);
        addRoom(warehouse);
        addRoom(vault);
        addRoom(spaceship);

        //Setting usabale object rooom
        usableObjectRoom.put(torcia, armeria);
        usableObjectRoom.put(computer, computerRoom);
        usableObjectRoom.put(serverComputer, spaceship);

        this.setCurrentRoom(playerBedroom);
        PasswordRooms.getInstance().disconnect();
    }

    @Override
    public String nextMove(ParserOutput p) {
        Room changeRoom = null;
        String text = null;
        if (p.getCommand() == null) {
            text = "Non ho capito cosa devo fare! Prova con un altro comando.";
        } else {
            boolean noroom = false;
            boolean move = false;

            if (null != p.getCommand().getType()) {
                switch (p.getCommand().getType()) {
                    case NORD:
                        if (getCurrentRoom().getNorth() != null) {
                            if (getCurrentRoom().getNorth().getId() == 20) {
                                AdvObject spacesuit = new AdvObject(7, "spacesuit");
                                if (!getInventory().getList().contains(spacesuit)) {
                                    text = "Non puoi prendere una navicella spaziale senza aver preso una tuta spaziale!";
                                } else {
                                    if (!getCurrentRoom().getNorth().isLocked()) {
                                        checkForOpenObjContainer();
                                        move = true;
                                    }
                                }
                            } else {
                                if (!getCurrentRoom().getNorth().isLocked()) {
                                    checkForOpenObjContainer();
                                    move = true;
                                } else {
                                    text = "La porta è bloccata. Occorre un codice di accesso per sbloccarla!";
                                }
                            }
                        } else {
                            noroom = true;
                        }
                        if (move) {
                            changeRoom = getCurrentRoom().getNorth();
                        }
                        break;
                    case SOUTH:
                        if (getCurrentRoom().getSouth() != null) {
                            if (!getCurrentRoom().getSouth().isLocked()) {
                                move = true;
                            } else {
                                text = "La porta è bloccata. Occorre un codice di accesso per sbloccarla!";
                            }
                        } else {
                            noroom = true;
                        }
                        if (move) {
                            changeRoom = getCurrentRoom().getSouth();
                        }
                        break;
                    case EAST:
                        if (getCurrentRoom().getEast() != null) {
                            if (getCurrentRoom().getId() == 1 && !hasSleep) {
                                text = "Non puoi uscire momentaneamente. Le porte sono bloccate. Dovresti riposare, domani"
                                        + "ti attente una magnifica gita sulla ISS.";
                            } else {
                                if (!getCurrentRoom().getEast().isLocked()) {
                                    text = getCurrentRoom().getDescription();
                                    move = true;
                                } else {
                                    text = "La porta è bloccata. Occorre un codice di accesso per sbloccarla!";
                                }
                            }
                        } else {
                            noroom = true;
                        }
                        if (move) {
                            changeRoom = getCurrentRoom().getEast();
                        }
                        break;
                    case WEST:
                        if (getCurrentRoom().getWest() != null) {
                            if (!getCurrentRoom().getWest().isLocked()) {
                                move = true;
                            } else {
                                text = "La porta è bloccata. Occorre un codice di accesso per sbloccarla!";
                            }
                        } else {
                            noroom = true;
                        }
                        if (move) {
                            changeRoom = getCurrentRoom().getWest();
                        }
                        break;
                    case USE:
                        if (p.getObject1() == null && p.getInvObject1() != null && p.getInvObject1().isUsable()) {
                            if (getCurrentRoom().getName().equals("corridoioA59")) {
                                if (p.getInvObject1().getName().equals(objNames.keyName())) {
                                    if (getCurrentRoom().getNorth().isLocked()) {
                                        if (soundEnabled) {
                                            sound.playOnce(CostantsSoundPath.KEY, 2);
                                            sound.stop();
                                        }
                                        text = "Infili la chiave molto delicatamente, comincia e muoversi la serrattura e...*boom*! Si è aperta!!";
                                        getCurrentRoom().getNorth().setLocked(false);

                                    } else {
                                        text = "Hai già sbloccato la porta...";
                                    }
                                } else {
                                    text = "Vuoi davvero aprire una porta con questo oggetto?";
                                }
                            }
                            if (usableObjectRoom.containsKey(p.getInvObject1())) {
                                if (p.getInvObject1().getName().equalsIgnoreCase(objNames.torchName())) {
                                    if (!getCurrentRoom().isVisible()) {
                                        getCurrentRoom().setVisible(true);
                                        text = "Ora si vede meglio! " + getCurrentRoom().getDescription();
                                    } else {
                                        text = "La stanza è già illuminata!";
                                    }
                                }
                            }
                        } else {
                            text = "Non so cosa utilizzare esattamente!";
                        }
                        break;
                    case LOOK_AT:
                        text = commandLookAt(p);
                        break;
                    case OPEN:
                        text = commandOpen(p);
                        break;
                    case PICK_UP:
                        text = commandPickUp(p);
                        break;
                    case SLEEP:
                        text = commandSleep();
                        break;
                    case INVENTORY:
                        StringBuilder textS = new StringBuilder("Oggetti nel tuo invetario: ");
                        if (!getInventory().getList().isEmpty()) {
                            if (getInventory().getList().size() == 1) {
                                textS.append(getInventory().getList().get(0).getName());
                            } else {
                                for (AdvObject o : getInventory().getList()) {
                                    textS.append(o.getName()).append(", ");
                                }
                            }
                            text = textS.toString();
                        } else {
                            text = "Nessun oggetto nell'inventario!";
                        }
                        break;
                    case ATTACK:
                        text = commandAttack(p);
                        checkForEnemySound();
                        break;
                    case UNLOCK:
                        text = commandUnlock(p);
                        break;
                    case TALK_TO:
                        text = commandTalkTo(p);
                        break;
                    case PLUNDER:
                        text = commandPlunder(p);
                    default:
                        break;
                }
            }
            if (noroom) {
                text = "C'è un muro da quella parte, purtroppo non hai ancora acquisito il potere dell'invisibilita'! "
                        + "Se continui così ti farai molti bernoccoli...";
            } else if (move) {
                if (hasEnemy(getCurrentRoom()) != null
                        && hasEnemy(getCurrentRoom()).isAlive()) {
                    if (!changeRoom.equals(lastRoomVisited)) {
                        text = "Non puoi andare in questa direzione, altrimenti verresti mangiato vivo!";
                        move = false;
                    } else {
                        move = true;
                    }
                }
                if (move) {
                    lastRoomVisited = getCurrentRoom();
                    setCurrentRoom(changeRoom);
                    getCurrentRoom().setVisited();
                    checkForEnemySound();
                    checkForOpenObjContainer();
                    if (getCurrentRoom().getTimeVisited() >= 5) {
                        text = setDescriptionMuchTimeVisited();
                    } else {
                        if (getCurrentRoom().isVisible()) {
                            text = getCurrentRoom().getDescription();
                        } else {
                            text = "La stanza non è illuminata, trova qualcosa con cui fare luce!";
                        }
                    }
                }
            } else {
                if (getCurrentRoom().isVisible() && text == null) {
                    text = getCurrentRoom().getDescription();
                }
            }
        }

        return text;
    }

    private AdvEnemy hasEnemy(Room room) {
        for (AdvNPC e : room.getNPCs()) {
            if (e instanceof AdvEnemy) {
                return ((AdvEnemy) e);
            }
        }
        return null;
    }

    public void checkForEnemySound() {
        if (hasEnemy(getCurrentRoom()) != null) {
            for (AdvNPC e : getCurrentRoom().getNPCs()) {
                if (e instanceof AdvEnemy) {
                    if (((AdvEnemy) e).isAlive()) {
                        if (soundEnabled) {
                            sound.play(CostantsSoundPath.ALIEN_VOICE);
                        }
                        break;
                    } else {
                        if (soundEnabled && sound.isReproducing()) {
                            sound.stop();
                        }
                    }
                }
            }
        } else {
            if (sound.isReproducing()) {
                sound.stop();
            }
        }
    }

    private void checkForOpenObjContainer() {
        if (containerOpened) {
            containerOpened = false;
        }
    }

    private String commandSleep() {
        String text = null;
        Room r = getCurrentRoom();
        if (r.getName().equals("corridoioAlieno")) {
            for (AdvNPC e : r.getNPCs()) {
                if (e instanceof AdvEnemy) {
                    if (((AdvEnemy) e).isAlive()) {
                        text = "Vorresti davvero dormire davanti ad un alieno!?";
                    } else {
                        text = "Non è comunque una buona idea dormire vicino ad un alieno morto";
                    }
                    break;
                }
            }
        } else if (r.getName().equals("camera")) {
            if (!hasSleep) {
                text = "Dopo esserti appisolato per 5 minuti, avverti rumori molto strani e vengono avviate "
                        + "le sirene di emergenza? Cosa sarà successo?";
                hasSleep = true;
            } else {
                if (!firstEnemyFound) {
                    text = "Capisco che il letto sia comodo, ma almeno devi capire cosa è successo.";
                } else {
                    text = "Si dormi pure, cosi l'alieno può mangiarti tranquillamente!";
                }
            }
        } else if (r.getName().equals("camera2")) {
            text = "Non è una buona idea!";
        } else {
            text = "Davvero vorresti dormire qui?";
        }

        return text;
    }

    private String commandLookAt(ParserOutput p) {
        String text = null;
        Room r = getCurrentRoom();
        if (getCurrentRoom().getLook() != null && !getCurrentRoom().isVisible()) {
            text = "La stanza non è illuminata! Trova qualcosa con cui fare luce.";
        } else if (getCurrentRoom().getLook() != null && p.getObject1() == null && p.getEnemy1() == null && p.getNpc1() == null) {
            if (p.getExtraWords() == null) {
                AdvEnemy enemyRoom = hasEnemy(r);
                boolean lookDefault = false;
                if (enemyRoom == null) {
                    lookDefault = true;
                } else {
                    if (enemyRoom.isAlive()) {
                        lookDefault = true;
                    } else {
                        lookDefault = false;
                        text = "E' meglio che fili via da qui, sia mai che possa risvegliarsi...";
                    }
                }
                if (lookDefault) {
                    StringBuilder textLook = new StringBuilder(getCurrentRoom().getLook());
                    if (!getCurrentRoom().getObjects().isEmpty()) {
                        textLook.append("Nella stanza corrente è presente: \n");
                        for (AdvObject o : getCurrentRoom().getObjects()) {
                            textLook.append(o.getName()).append("\n");
                        }
                    } else {
                        textLook.append("Nessuno oggetto in questa stanza!");
                    }
                    text = textLook.toString();
                }
            } else {
                text = "Non ho ben capito cosa dovrei osservare. Sii più chiaro...";
            }
        } else if (getCurrentRoom().getLook() != null && p.getObject1() != null) {
            text = p.getObject1().getDescription();
        } else if (r.getNPCs().isEmpty()) {
            if (p.getObject1() == null) {
                text = r.getDescription();
            } else {
                text = p.getObject1().getDescription();
            }
        } else if (!r.getNPCs().isEmpty()) {
            if (p.getObject1() == null && p.getEnemy1() != null) {
                if (p.getEnemy1().isAlive()) {
                    text = p.getEnemy1().getLook();
                } else {
                    text = p.getEnemy1().getExamineDead();
                }
            } else if (p.getObject1() != null && p.getEnemy1() == null) {
                text = p.getObject1().getDescription();
            } else if (p.getNpc1() != null) {
                if (p.getNpc1().isAlive()) {
                    text = p.getNpc1().getExamine();
                } else {
                    text = "Ormai è andato, riposi in pace...Noti però un biglietto con scritto 19, cosa vorrà dire?";
                }
            } else {
                AdvEnemy enemyRoom = hasEnemy(r);
                if (enemyRoom == null) {
                    text = r.getDescription();
                } else {
                    if (enemyRoom.isAlive()) {
                        text = r.getDescription();
                    } else {
                        text = "E' meglio che fili via da qui, sia mai che possa risvegliarsi...";
                    }
                }
            }
        }
        return text;
    }

    private String commandAttack(ParserOutput p) {
        String text = null;

        if (p.getInvObject2() == null && p.getEnemy1() != null && p.getEnemy1().isAlive()) {
            text = "Vuoi davvero sconfiggere un'alieno a mani nude? Devi almeno avere un'arma!";
        } else if (p.getInvObject2() != null && p.getEnemy1() != null) {
            for (AdvObject o : getInventory().getList()) {
                if (p.getInvObject2().equals(o)) {
                    if (o.getName().equals("raygun")) {
                        if (p.getEnemy1().isAlive()) {
                            if (sound.isReproducing() && soundEnabled) {
                                sound.stop();
                                sound.playOnce(CostantsSoundPath.RAYGUN_SHOT, 2);
                                sound.stop();
                            }
                            text = "Impaurito hai sparato 100 colpi e hai ucciso l'alieno! Ce ne saranno altri?";
                            p.getEnemy1().setAlive(false);
                            p.getEnemy1().setImgObjRoom("enemyDead.png");
                            break;
                        } else {
                            text = "Capisco che sia già morto, ma non sei un assassino!";
                        }
                    } else {
                        if (p.getEnemy1().isAlive()) {
                            text = "Sei serio? Vuoi uccidere un alieno con quest'oggetto?";
                        } else {
                            text = "Va be' che è morto, ma davvero vorresti dargli il colpo di grazia con questo oggetto?";
                        }
                        break;
                    }
                }
            }
        } else if (p.getInvObject2() == null && p.getEnemy1() != null) {
            text = "Capisco che sia già morto, ma non sei un assassino!";
        } else if (p.getInvObject2() != null && p.getEnemy1() == null) {
            text = "Chi dovrei attaccare con questo scusa?";
        } else {
            if (p.getEnemy1() != null && p.getEnemy1().isAlive()) {
                text = "Con cosa dovrei ucciderlo?";
            } else {
                if (p.getNpc1() != null) {
                    text = "Non sei un assassino!";
                } else {
                    text = "Non posso attaccare un fantasma!";
                }
            }
        }
        return text;
    }

    private String commandUnlock(ParserOutput p) {
        String text = null;
        String password = null;
        String nameRoom = null;
        boolean check = true;
        if (p.getCommand2() != null) {
            if (p.getCommand2().getType() == CommandType.NORD
                    || p.getCommand2().getType() == CommandType.WEST
                    | p.getCommand2().getType() == CommandType.EAST
                    | p.getCommand2().getType() == CommandType.SOUTH) {
                if (p.getExtraWords() != null) {
                    if (p.getExtraWords().matches("[0-9]*[a-zA-Z]+[0-9]*")) {
                        text = "il codice di accesso contiente solo numeri!";
                    } else if (p.getExtraWords().matches("[\\d]{6}")) {
                        text = "prova per vedere";
                        if (p.getCommand2().getType() == CommandType.NORD && getCurrentRoom().getNorth().isLocked()) {
                            nameRoom = getCurrentRoom().getNorth().getName();
                        } else if (p.getCommand2().getType() == CommandType.WEST && getCurrentRoom().getWest().isLocked()) {
                            nameRoom = getCurrentRoom().getWest().getName();
                        } else if (p.getCommand2().getType() == CommandType.EAST && getCurrentRoom().getEast().isLocked()) {
                            nameRoom = getCurrentRoom().getEast().getName();
                        } else if (p.getCommand2().getType() == CommandType.SOUTH && getCurrentRoom().getSouth().isLocked()) {
                            nameRoom = getCurrentRoom().getSouth().getName();
                        } else {
                            check = false;
                        }
                        if (check) {
                            check = true;
                            try {
                                PasswordRooms.getInstance().connect();
                                password = PasswordRooms.getInstance().searchRoomPassword(nameRoom);
                                if (password.equals(p.getExtraWords())) {
                                    text = "Porta sbloccata!";
                                    if (soundEnabled) {
                                        sound.playOnce(CostantsSoundPath.PASSWORD_CORRECT, 1);
                                        sound.stop();
                                    }
                                    if (null != p.getCommand2().getType()) {
                                        switch (p.getCommand2().getType()) {
                                            case NORD:
                                                getCurrentRoom().getNorth().setLocked(false);
                                                break;
                                            case WEST:
                                                getCurrentRoom().getWest().setLocked(false);
                                                break;
                                            case EAST:
                                                getCurrentRoom().getEast().setLocked(false);
                                                break;
                                            case SOUTH:
                                                getCurrentRoom().getSouth().setLocked(false);
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                } else {
                                    check = false;
                                    text = "Password errata!";
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(VoyagerStationGame.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            text = "Non c'è nessuna porta bloccata nella posizione specificata!";
                        }
                    } else {
                        check = false;
                        text = "Non hai notato che i codici d'accesso sono tutti a sei cifre?";
                    }
                } else {
                    text = "Bene, lo sbloccherò con il potere della mente se non vuoi dirmi quale codice inserire!";
                }
            }
        } else {
            text = "Se non mi dici dove devo sbloccare la porta come farò a sbloccarla?";
        }
        if (!check) {
            if (soundEnabled) {
                sound.playOnce(CostantsSoundPath.PASSWORD_UNCORRECT, 2);
                sound.stop();
            }
        }
        try {
            PasswordRooms.getInstance().disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(VoyagerStationGame.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return text;
    }

    private String commandTalkTo(ParserOutput p) {
        String text = null;
        if (p.getNpc1() != null && p.getNpc1() instanceof AdvCharacter) {
            for (AdvNPC c : getCurrentRoom().getNPCs()) {
                if (c.equals(p.getNpc1())) {
                    if (((AdvCharacter) c).isAlive()) {
                        text = ((AdvCharacter) c).getFirstDialogue();
                    } else {
                        text = "Ormai è arrivata la sua ora, non potrà più risponderti...";
                    }
                    if (((AdvCharacter) c).getName().equals("astronauta")) {
                        ((AdvCharacter) c).setAlive(false);
                    }
                    break;
                }
            }
        } else {
            text = "Mi dispiace ma al momento il tuo amico immaginario Giggino può aspettare...";
        }
        return text;
    }

    private String commandPickUp(ParserOutput p) {
        boolean hasEnemy = false;
        String text = null;
        if (p.getObject1() != null) {
            if (p.getObject1().isPickupable()) {
                for (AdvNPC o : getCurrentRoom().getNPCs()) {
                    if (o instanceof AdvEnemy && ((AdvEnemy) o).isAlive()) {
                        hasEnemy = true;
                        break;
                    }
                }
                if (!hasEnemy) {
                    getInventory().add(p.getObject1());
                    getCurrentRoom().getObjects().remove(p.getObject1());
                    text = "Hai raccolto: " + p.getObject1().getName();
                } else {
                    text = "Vuoi davvero rischiare di morire? ";
                }
            } else {
                text = "Non puoi raccogliere questo oggetto! Lo hai già!!";
            }
        } else {
            if (containerOpened) {
                if (p.getObject1() == null) {
                    AdvObjectContainer container = null;
                    for (AdvObject o : getCurrentRoom().getObjects()) {
                        if (o instanceof AdvObjectContainer) {
                            container = (AdvObjectContainer) o;
                        }
                    }
                    if (container != null) {
                        try {
                            Set<String> stopwords = Utils.loadFileListInSet(new File("./resources/stopwords"));
                            Parser parser = new Parser(stopwords);
                            String command = p.getCommand().toString() + " " + p.getExtraWords();
                            ParserOutput p2 = parser.parse(command, getCommands(), container.getList(), getInventory().getList(), getCurrentRoom().getNPCs());

                            if (p2.getObject1() != null) {
                                for (AdvObject o : container.getList()) {
                                    if (p2.getObject1().getName().equals(o.getName())) {
                                        text = "Hai raccolto: " + o.getName();
                                        break;
                                    }
                                }
                                text = "Hai raccolto l'oggetto: " + p2.getObject1();
                                getInventory().add(p2.getObject1());
                                container.remove(p2.getObject1());
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(VoyagerStationGame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        text = "Non c'è niente da raccogliere qui!";
                    }
                }
            }

            if (npcInventoryOpened) {
                try {
                    Set<String> stopwords = Utils.loadFileListInSet(new File("./resources/stopwords"));
                    Parser parser = new Parser(stopwords);
                    String command = p.getCommand().toString() + " " + p.getExtraWords();
                    ParserOutput p2 = null;
                    for (AdvNPC n : getCurrentRoom().getNPCs()) {
                        for (AdvObject o : n.getInventory().getList()) {
                            if (o.getName().equals(p.getExtraWords())) {
                                p2 = parser.parse(command, getCommands(), n.getInventory().getList(), getInventory().getList(), getCurrentRoom().getNPCs());
                                text = "Hai raccolto l'oggetto: " + p2.getObject1();
                                getInventory().add(p2.getObject1());
                                n.getInventory().getList().remove(o);
                                break;
                            }
                        }
                    }
                    if (p2 == null) {
                        text = "Non riesco a trovare l'oggetto indicato!";
                    }
                } catch (IOException ex) {
                    Logger.getLogger(VoyagerStationGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                text = "Non c'è niente da raccogliere qui!";
            }
        }
        return text;
    }

    private String commandOpen(ParserOutput p) {
        String text = null;
        StringBuilder builder = new StringBuilder();
        if (p.getObject1() != null && p.getCommand2() == null && p.getNpc1() == null) {
            if (p.getObject1().isOpenable()) {
                if (p.getObject1().getName().equals(objNames.dossierName())) {
                    text = readFromFile(new File("resources/files/dossierPazienti.txt"));
                } else if (p.getObject1().getName().equals(objNames.commandManualName())) {
                    text = readFromFile(new File("resources/files/manualeComandi.txt"));
                } else if (!p.getObject1().getName().equals(objNames.dossierName())) {
                    AdvObjectContainer container = (AdvObjectContainer) p.getObject1();
                    for (AdvObject o : container.getList()) {
                        builder.append("Nome oggetto: ").append(o.getName()).append("\nDescrizione: ").append(o.getDescription()).append("\n");
                    }
                    containerOpened = true;
                    text = builder.toString();
                }
            } else {
                /* This check if npc inventory is open */
                if (p.getNpc1() != null) {
                    for (AdvObject o : p.getNpc1().getInventory().getList()) {
                        builder.append("Nome oggetto: ").append(o.getName()).append("\nDescrizione: ").append(o.getDescription()).append("\n");
                    }
                }
            }
        } else if (p.getCommand2() != null && p.getCommand2().getType() == CommandType.INVENTORY && p.getNpc1() != null) {
            if (!p.getNpc1().isAlive()) {
                text = "Hai aperto l'inventario di: " + p.getNpc1().getName();
            } else {
                text = "Non è rispettoso frugare nelle tasca degli altri...";
            }
        } else {
            text = "Non c'è niente da raccogliere qui!";
        }
        return text;
    }

    private String commandPlunder(ParserOutput p) {
        String text = "";
        if (p.getNpc1() != null && !p.getNpc1().isAlive()) {
            text = "Hai aperto l'inventario di: " + p.getNpc1().getName();
            npcInventoryOpened = true;
        } else if (p.getNpc1() != null && p.getNpc1().isAlive()) {
            text = "Non è rispettoso frugare nelle tasca degli altri...";
        } else {
            if (p.getEnemy1() == null) {
                text = "Non posso depredare qualcuno che non esiste!";
            } else {
                text = "Non è una buona idea comunque...";
            }
        }
        return text;
    }

    private String setDescriptionMuchTimeVisited() {
        String description = null;
        if (getCurrentRoom().getId() == 1) {
            description = "Ti piace proprio questa stanza? Ti ricordo che è in atto una invasione aliena.";
        } else if (getCurrentRoom().getId() == 2) {
            description = "So che è un bagno spettacolare, ma c'è un alieno che gironzola tutto felicio."
                    + "Ma non sei preoccupato?";
        }
        return description;
    }

    private String readFromFile(File file) {
        StringBuilder textFile = new StringBuilder();
        String text = null;
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] lineFile = line.split("##");
                for (String str : lineFile) {
                    textFile.append(str + "\n");
                }
                textFile.append("\n");
            }
            text = textFile.toString();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VoyagerStationGame.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VoyagerStationGame.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (file != null) {

            }
        }
        return text;
    }

    /* Save */
    @Override
    public boolean save(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        boolean saved = false;
        File f = new File("./resources/saves/" + fileName + ".dat");
        if (!f.exists() && !f.isDirectory()) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./resources/saves/" + fileName + ".dat"))) {
                out.writeObject(this);
                saved = true;
            }
        }
        return saved;
    }

    @Override
    public GameDescription load(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        VoyagerStationGame game;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("./resources/saves/" + fileName))) {
            game = (VoyagerStationGame) in.readObject();
            game.sound = new SoundGame();
        }
        return game;
    }

}
